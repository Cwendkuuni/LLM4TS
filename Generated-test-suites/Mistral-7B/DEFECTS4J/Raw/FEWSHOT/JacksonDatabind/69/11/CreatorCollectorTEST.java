package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Type;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.deser.CreatorProperty.FilterProvider;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Unit tests for the CreatorCollector class.
 */
public class CreatorCollectorTest {

    @Test
    public void testCreatorCollector() {
        MapperConfig<?> config = new MapperConfig<>();
        BeanDescription beanDesc = new BeanDescription(null, null, null);
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        // Test default creator
        AnnotatedConstructor<?> constructor = new AnnotatedConstructor<>(MyClass.class, null);
        collector.setDefaultCreator(constructor);
        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        assertNotNull(instantiator);

        // Test string creator
        AnnotatedMethod method = new AnnotatedMethod(MyClass.class, "getStringMethod", new Class[] { String.class });
        method.addAnnotation(new JacksonAnnotation(Creator.class));
        collector.addStringCreator(method, true);
        instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        Object result = instantiator.createValueInstance(null, null, null, new String[]{"test"});
        assertEquals("test", result);

        // Test int creator
        AnnotatedMethod methodInt = new AnnotatedMethod(MyClass.class, "getIntMethod", new Class[] { Integer.class });
        methodInt.addAnnotation(new JacksonAnnotation(Creator.class));
        collector.addIntCreator(methodInt, true);
        instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        result = instantiator.createValueInstance(null, null, null, new Object[]{42});
        assertEquals(42, result);

        // Test long creator
        AnnotatedMethod methodLong = new AnnotatedMethod(MyClass.class, "getLongMethod", new Class[] { Long.class });
        methodLong.addAnnotation(new JacksonAnnotation(Creator.class));
        collector.addLongCreator(methodLong, true);
        instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        result = instantiator.createValueInstance(null, null, null, new Object[]{42L});
        assertEquals(42L, result);

        // Test double creator
        AnnotatedMethod methodDouble = new AnnotatedMethod(MyClass.class, "getDoubleMethod", new Class[] { Double.class });
        methodDouble.addAnnotation(new JacksonAnnotation(Creator.class));
        collector.addDoubleCreator(methodDouble, true);
        instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        result = instantiator.createValueInstance(null, null, null, new Object[]{42.0});
        assertEquals(42.0, result);

        // Test boolean creator
        AnnotatedMethod methodBoolean = new AnnotatedMethod(MyClass.class, "getBooleanMethod", new Class[] { Boolean.class });
        methodBoolean.addAnnotation(new JacksonAnnotation(Creator.class));
        collector.addBooleanCreator(methodBoolean, true);
        instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        result = instantiator.createValueInstance(null, null, null, new Object[]{true});
        assertEquals(true, result);

        // Test delegating creator
        AnnotatedConstructor<?> constructorDelegate = new AnnotatedConstructor<>(MyDelegateClass.class, new Class[] {});
        SettableBeanProperty[] delegateArgs = new SettableBeanProperty[1];
        delegateArgs[0] = new SettableBeanProperty(MyDelegateClass.class, "delegate", MyClass.class);
        collector.addDelegatingCreator(constructorDelegate, true, delegateArgs);
        instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        result = instantiator.createValueInstance(null, null, null, null);
        assertNotNull(result);

        // Test property creator
        AnnotatedMethod methodProperty = new AnnotatedMethod(MyClass.class, "getPropertyMethod", new Class[] {});
        SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[1];
        propertyBasedArgs[0] = new SettableBeanProperty(MyClass.class, "property", String.class);
        collector.addPropertyCreator(methodProperty, true, propertyBasedArgs);
        instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        result = instantiator.createValueInstance(null, null, null, null);
        assertNotNull(result);
        assertEquals("test", result.getClass().getDeclaredField("property").get(result));

        // Test array delegating creator
        AnnotatedConstructor<?> constructorArrayDelegate = new AnnotatedConstructor<>(MyArrayDelegateClass.class, new Class[] { MyClass[].class });
        SettableBeanProperty[] arrayDelegateArgs = new SettableBeanProperty[1];
        arrayDelegateArgs[0] = new SettableBeanProperty(MyArrayDelegateClass.class, "delegate", MyClass[].class);
        collector.addDelegatingCreator(constructorArrayDelegate, true, arrayDelegateArgs);
        instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        result = instantiator.createValueInstance(null, null, null, new Object[]{new MyClass[0]});
        assertNotNull(result);

        // Test multiple creators of the same type
        AnnotatedMethod methodInt2 = new AnnotatedMethod(MyClass.class, "getIntMethod2", new Class[] { Integer.class });
        methodInt2.addAnnotation(new JacksonAnnotation(Creator.class));
        collector.addIntCreator(methodInt2, true);
        try {
            collector.constructValueInstantiator(new DeserializationConfig());
            fail("Should have thrown an error.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test incomplete parameter
        AnnotatedParameter parameter = new AnnotatedParameter(MyClass.class, "setParameterMethod", new Class[] { String.class }, 0);
        collector.addIncompleteParameter(parameter);
        instantiator = collector.constructValueInstantiator(new DeserializationConfig());
        result = instantiator.createValueInstance(null, null, null, null);
        assertNotNull(result);
    }

    private static class MyClass {
        private String property;

        public String getPropertyMethod() {
            return property;
        }

        public void setParameterMethod(String value) {
            this.property = value;
        }

        public String getStringMethod(String str) {
            return str;
        }

        public int getIntMethod(int i) {
            return i;
        }

        public long getLongMethod(long l) {
            return l;
        }

        public double getDoubleMethod(double d) {
            return d;
        }

        public boolean getBooleanMethod(boolean b) {
            return b;
        }
    }

    private static class MyDelegateClass {
        private MyClass delegate;

        public MyDelegateClass(MyClass delegate) {
            this.delegate = delegate;
        }

        public MyClass getDelegate() {
            return delegate;
        }
    }

    private static class MyArrayDelegateClass {
        private MyClass[] delegate;

        public MyArrayDelegateClass(MyClass[] delegate) {
            this.delegate = delegate;
        }

        public MyClass[] getDelegate() {
            return delegate;
        }
    }
}