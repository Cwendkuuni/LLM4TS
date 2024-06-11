package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedParameter;
import java.lang.reflect.Method;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.AnnotatedClass;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

    @Test
    public void testCreatorCollector() throws Exception {
        // Given
        ObjectMapper objectMapper = new ObjectMapper();
        BeanDescription beanDescription = objectMapper.getFactory().findBeanDescription(String.class, null);
        CreatorCollector creatorCollector = new CreatorCollector(beanDescription, objectMapper.getConfig());

        // When - testing default constructor
        creatorCollector.setDefaultCreator(new AnnotatedMethod(String.class.getDeclaredConstructor()));
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        String instance = (String) valueInstantiator.createValueInstance(null);
        assertEquals("", instance);

        // When - testing string creator
        Class<String> stringClass = String.class;
        Method stringConstructor = stringClass.getDeclaredMethod("valueOf", String.class);
        AnnotatedWithParams stringCreator = new AnnotatedWithParams(stringConstructor, new AnnotatedElement[] { stringConstructor });
        creatorCollector.addStringCreator(stringCreator, true);
        valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        instance = (String) valueInstantiator.createValueInstance(null);
        assertEquals("test", instance);

        // When - testing int creator
        Class<Integer> intClass = Integer.class;
        Constructor<Integer> intConstructor = intClass.constructor(int.class);
        AnnotatedWithParams intCreator = new AnnotatedWithParams(intConstructor, new AnnotatedElement[] { intConstructor });
        creatorCollector.addIntCreator(intCreator, true);
        valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        instance = (Integer) valueInstantiator.createValueInstance(null);
        assertEquals(42, instance.intValue());

        // When - testing long creator
        Class<Long> longClass = Long.class;
        Constructor<Long> longConstructor = longClass.constructor(long.class);
        AnnotatedWithParams longCreator = new AnnotatedWithParams(longConstructor, new AnnotatedElement[] { longConstructor });
        creatorCollector.addLongCreator(longCreator, true);
        valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        instance = (Long) valueInstantiator.createValueInstance(null);
        assertEquals(42L, instance.longValue());

        // When - testing double creator
        Class<Double> doubleClass = Double.class;
        Constructor<Double> doubleConstructor = doubleClass.constructor(double.class);
        AnnotatedWithParams doubleCreator = new AnnotatedWithParams(doubleConstructor, new AnnotatedElement[] { doubleConstructor });
        creatorCollector.addDoubleCreator(doubleCreator, true);
        valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        instance = (Double) valueInstantiator.createValueInstance(null);
        assertEquals(3.14, instance.doubleValue(), 0.001);

        // When - testing boolean creator
        Class<Boolean> booleanClass = Boolean.class;
        Constructor<Boolean> booleanConstructor = booleanClass.constructor(boolean.class);
        AnnotatedWithParams booleanCreator = new AnnotatedWithParams(booleanConstructor, new AnnotatedElement[] { booleanConstructor });
        creatorCollector.addBooleanCreator(booleanCreator, true);
        valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        instance = (Boolean) valueInstantiator.createValueInstance(null);
        assertTrue(instance);

        // When - testing delegate creator
        Class<Foo> fooClass = Foo.class;
        Constructor<Foo> fooConstructor = fooClass.constructor(Bar.class);
        AnnotatedWithParams delegateCreator = new AnnotatedWithParams(fooConstructor, new AnnotatedElement[] { fooConstructor });
        SettableBeanProperty[] injectables = new SettableBeanProperty[] { new SettableBeanProperty("bar", new JsonProperty(), null) };
        creatorCollector.addDelegatingCreator(delegateCreator, true, injectables);
        valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        Foo instanceFoo = (Foo) valueInstantiator.createValueInstance(new Bar());
        assertNotNull(instanceFoo.getBar());

        // When - testing property-based creator
        Class<Baz> bazClass = Baz.class;
        Constructor<Baz> bazConstructor = bazClass.constructor(Baz.class, String.class);
        AnnotatedWithParams propertyCreator = new AnnotatedWithParams(bazConstructor, new AnnotatedElement[] { bazConstructor });
        CreatorProperty[] properties = new CreatorProperty[] { new CreatorProperty("name", new JsonProperty()) };
        creatorCollector.addPropertyCreator(propertyCreator, true, properties);
        valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        Baz instanceBaz = (Baz) valueInstantiator.createValueInstance(new Object[] { "test" });
        assertEquals("test", instanceBaz.getName());

        // When - testing array delegate creator
        Class<BazArray> bazArrayClass = BazArray.class;
        Constructor<BazArray> bazArrayConstructor = bazArrayClass.constructor(Baz[].class);
        AnnotatedWithParams arrayDelegateCreator = new AnnotatedWithParams(bazArrayConstructor, new AnnotatedElement[] { bazArrayConstructor });
        SettableBeanProperty[] arrayDelegateArgs = new SettableBeanProperty[] { new SettableBeanProperty("value", new JsonProperty(), null) };
        creatorCollector.addArrayDelegateCreator(arrayDelegateCreator, true, arrayDelegateArgs);
        valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        BazArray instanceBazArray = (BazArray) valueInstantiator.createValueInstance(new Object[] { new Baz[0] });
        assertNotNull(instanceBazArray.getValue());

        // When - testing incomplete parameter
        Class<Quux> quuxClass = Quux.class;
        AnnotatedParameter incompleteParameter = quuxClass.getDeclaredConstructor().getParameters()[0];
        creatorCollector.addIncompleteParameter(incompleteParameter);
        valueInstantiator = creatorCollector.constructValueInstantiator(objectMapper.getConfig());
        Quux instanceQuux = (Quux) valueInstantiator.createValueInstance(null);
        assertNotNull(instanceQuux);

        // Then - check for deprecated methods
        try {
            creatorCollector.addStringCreator(new AnnotatedMethod(String.class.getDeclaredMethod("valueOf", String.class)));
            fail("addStringCreator should be deprecated");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            creatorCollector.addIntCreator(new AnnotatedMethod(Integer.class.getDeclaredMethod("valueOf", int.class)));
            fail("addIntCreator should be deprecated");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            creatorCollector.addLongCreator(new AnnotatedMethod(Long.class.getDeclaredMethod("valueOf", long.class)));
            fail("addLongCreator should be deprecated");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            creatorCollector.addDoubleCreator(new AnnotatedMethod(Double.class.getDeclaredMethod("valueOf", double.class)));
            fail("addDoubleCreator should be deprecated");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            creatorCollector.addBooleanCreator(new AnnotatedMethod(Boolean.class.getDeclaredMethod("valueOf", boolean.class)));
            fail("addBooleanCreator should be deprecated");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            creatorCollector.addDelegatingCreator(new AnnotatedMethod(Foo.class.getDeclaredConstructor()), new CreatorProperty[0]);
            fail("addDelegatingCreator should be deprecated");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            creatorCollector.addPropertyCreator(new AnnotatedMethod(Baz.class.getDeclaredConstructor(String.class)), new CreatorProperty[0]);
            fail("addPropertyCreator should be deprecated");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Then - test null check
        try {
            creatorCollector = null;
            creatorCollector.constructValueInstantiator(objectMapper.getConfig());
            fail("constructValueInstantiator should throw NPE when called on null CreatorCollector");
        } catch (NullPointerException e) {
            // expected
        }
    }

    static class Foo {
        @JsonProperty
        private Bar bar;

        public Bar getBar() {
            return bar;
        }

        @JsonCreator
        public Foo(@JsonProperty("bar") Bar bar) {
            this.bar = bar;
        }
    }

    static class Baz {
        @JsonProperty
        private String name;

        public String getName() {
            return name;
        }

        @JsonCreator
        public Baz(@JsonProperty("name") String name) {
            this.name = name;
        }

        public Baz(Baz baz, String name) {
            this.name = name;
        }
    }

    static class BazArray {
        @JsonProperty
        private Baz[] value;

        public Baz[] getValue() {
            return value;
        }

        @JsonCreator
        public BazArray(@JsonProperty("value") Baz[] value) {
            this.value = value;
        }
    }

    static class Quux {
        @JsonCreator
        public Quux() {
        }
    }
}