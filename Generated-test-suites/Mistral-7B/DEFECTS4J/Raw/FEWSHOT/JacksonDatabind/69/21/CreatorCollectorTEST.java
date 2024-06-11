package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.databind.DeserializationTest.createContext;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ValueIntrospector;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import java.io.IOException;
import java.lang.reflect.Member;
import java.util.*;

public class CreatorCollectorTest {

    @Test
    public void testCreatorCollector() throws Exception {
        MapperConfig<?> config = new MapperConfig(new AnnotationIntrospector[] { new NopAnnotationIntrospector() }, new JacksonAnnotationIntrospector());
        BeanDescription beanDesc = getTestBeanDescription();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        ValueInstantiator instantiator = collector.constructValueInstantiator(createContext(config));
        Object inst = inst.createValueInstance(null);
        assertEquals(beanDesc.getType().getRawClass(), inst.getClass());

        // Test default constructor
        assertNotNull(collector.hasDefaultCreator());

        // Test string constructor
        AnnotatedMethod stringConstructor = getTestStringConstructor();
        collector.addStringCreator(stringConstructor, true);
        inst = instantiator.createValueInstance(new Object[] {"Hello"});
        assertEquals("Hello", inst);

        // Test int constructor
        AnnotatedMethod intConstructor = getTestIntConstructor();
        collector.addIntCreator(intConstructor, true);
        inst = instantiator.createValueInstance(new Object[] {42});
        assertEquals(42, inst);

        // Test long constructor
        AnnotatedMethod longConstructor = getTestLongConstructor();
        collector.addLongCreator(longConstructor, true);
        inst = instantiator.createValueInstance(new Object[] {42L});
        assertEquals(42L, inst);

        // Test double constructor
        AnnotatedMethod doubleConstructor = getTestDoubleConstructor();
        collector.addDoubleCreator(doubleConstructor, true);
        inst = instantiator.createValueInstance(new Object[] {3.14159265D});
        assertEquals(3.14159265D, inst, 0.0000001);

        // Test boolean constructor
        AnnotatedMethod booleanConstructor = getTestBooleanConstructor();
        collector.addBooleanCreator(booleanConstructor, true);
        inst = instantiator.createValueInstance(new Object[] {true});
        assertTrue((Boolean) inst);

        // Test delegate constructor
        AnnotatedMethod delegateConstructor = getTestDelegateConstructor();
        SettableBeanProperty[] delegateArgs = new SettableBeanProperty[] { new StdFieldPropertyDefinition(new AnnotatedField(ClassUtil.getSimpleName(Delegate.class), "delegate", Delegate.class, null)),
                new StdFieldPropertyDefinition(new AnnotatedField(ClassUtil.getSimpleName(Delegate.class), "value", String.class, null)) };
        collector.addDelegatingCreator(delegateConstructor, true, delegateArgs);
        inst = instantiator.createValueInstance(new Object[] {"foo", "bar"});
        Delegate delegate = (Delegate) inst;
        assertEquals("foo", delegate.getDelegate());
        assertEquals("bar", delegate.getValue());

        // Test property-based constructor
        AnnotatedMethod propertyBasedConstructor = getTestPropertyBasedConstructor();
        SettableBeanProperty[] propertyBasedArgs = new SettableBeanProperty[] { new StdFieldPropertyDefinition(new AnnotatedField(ClassUtil.getSimpleName(PropertyBased.class), "property1", String.class, null)),
                new StdFieldPropertyDefinition(new AnnotatedField(ClassUtil.getSimpleName(PropertyBased.class), "property2", Integer.class, null)) };
        collector.addPropertyCreator(propertyBasedConstructor, true, propertyBasedArgs);
        inst = instantiator.createValueInstance(new Object[] {"foo", 42});
        PropertyBased propertyBased = (PropertyBased) inst;
        assertEquals("foo", propertyBased.getProperty1());
        assertEquals(42, propertyBased.getProperty2());

        // Test array delegate constructor
        AnnotatedMethod arrayDelegateConstructor = getTestArrayDelegateConstructor();
        SettableBeanProperty[] arrayDelegateArgs = new SettableBeanProperty[] { new StdFieldPropertyDefinition(new AnnotatedField(ClassUtil.getSimpleName(ArrayDelegate.class), "delegate", String[].class, null)) };
        collector.addDelegatingCreator(arrayDelegateConstructor, true, arrayDelegateArgs);
        inst = instantiator.createValueInstance(new Object[] {"foo", "bar", "baz"});
        ArrayDelegate arrayDelegate = (ArrayDelegate) inst;
        assertArrayEquals(new String[] {"foo", "bar", "baz"}, arrayDelegate.getDelegate());

        // Test incomplete parameter
        AnnotatedParameter incompleteParameter = getTestIncompleteParameter();
        collector.addIncompleteParameter(incompleteParameter);
        try {
            inst = instantiator.createValueInstance(null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test vanilla instantiator
        ValueInstantiator vanillaInstantiator = collector.constructValueInstantiator(createContext(config));
        inst = vanillaInstantiator.createValueInstance(null);
        assertEquals(List.class.getName(), vanillaInstantiator.getValueTypeDesc());
        inst = vanillaInstantiator.createValueInstance(new Object[] {});
        assertTrue(inst instanceof List);
        List list = (List) inst;
        list.add("foo");
        list.add(42);
        inst = vanillaInstantiator.createValueInstance(null);
        assertTrue(inst instanceof List);
        List expectedList = new ArrayList<>();
        expectedList.add("foo");
        expectedList.add(42);
        assertEquals(expectedList, inst);

        // Test null input to createValueInstance
        try {
            instantiator.createValueInstance(null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid input to createValueInstance
        try {
            instantiator.createValueInstance(new Object[] { new String[] { "foo" } });
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    private BeanDescription getTestBeanDescription() {
        AnnotatedClass ac = ClassIntrospector.findBeanDescription(PropertyBased.class, new ValueIntrospector.Default(), new JacksonAnnotationIntrospector());
        return ac.findPropertyContainer();
    }

    private AnnotatedMethod getTestStringConstructor() throws IOException {
        return ClassIntrospector.findMethod(PropertyBased.class, "PropertyBased", new Class[] { String.class });
    }

    private AnnotatedMethod getTestIntConstructor() throws IOException {
        return ClassIntrospector.findConstructor(PropertyBased.class, ConstructorProperties.class);
    }

    private AnnotatedMethod getTestLongConstructor() throws IOException {
        return ClassIntrospector.findConstructor(PropertyBased.class, LongConstructorProperties.class);
    }

    private AnnotatedMethod getTestDoubleConstructor() throws IOException {
        return ClassIntrospector.findConstructor(PropertyBased.class, DoubleConstructorProperties.class);
    }

    private AnnotatedMethod getTestBooleanConstructor() throws IOException {
        return ClassIntrospector.findConstructor(PropertyBased.class, BooleanConstructorProperties.class);
    }

    private AnnotatedMethod getTestDelegateConstructor() throws IOException {
        return ClassIntrospector.findConstructor(Delegate.class, DelegateProperties.class);
    }

    private AnnotatedMethod getTestPropertyBasedConstructor() throws IOException {
        return ClassIntrospector.findConstructor(PropertyBased.class, PropertyBasedProperties.class);
    }

    private AnnotatedMethod getTestArrayDelegateConstructor() throws IOException {
        return ClassIntrospector.findConstructor(ArrayDelegate.class, ArrayDelegateProperties.class);
    }

    private AnnotatedParameter getTestIncompleteParameter() throws IOException {
        return ClassIntrospector.findParameter(PropertyBased.class, 1);
    }

    private static void assertArrayEquals(Object a, Object b) {
        if (a == b) {
            return;
        }
        if (a.getClass().isArray() && b.getClass().isArray()) {
            Object[] aArr = (Object[]) a;
            Object[] bArr = (Object[]) b;
            if (aArr.length != bArr.length) {
                fail("Lengths differ: " + aArr.length + " vs " + bArr.length);
            }
            for (int i = 0; i < aArr.length; i++) {
                assertEquals(aArr[i], bArr[i]);
            }
        } else {
            fail("Arrays are not equal: " + a.getClass() + " vs " + b.getClass());
        }
    }

    private static DeserializationContext createContext(MapperConfig<?> config) {
        return new TestContext(config);
    }

    private static class TestContext extends BaseTestContext {
        public TestContext(MapperConfig<?> config) { super(config); }

        @Override
        public AnnotationIntrospector getAnnotationIntrospector() { return config.getDefaultAnnotationIntrospector(); }

        @Override
        public ValueIntrospector getValueIntrospector() { return config.getDefaultValueIntrospector(); }

        @Override
        public ObjectReader getObjectReader() { return config.getDefaultObjectMapper().getFactory().getValueFactory().getValueReader(Object.class); }

        @Override
        public ObjectReader getRootValueReader() { return config.getDefaultObjectMapper().getFactory().getValueFactory().getRootValueReader(); }

        @Override
        public ObjectMapper getObjectMapper() { return config.getDefaultObjectMapper(); }

        @Override
        public JavaType getMapperConfigType() { return ClassType.construct(MapperConfig.class, new JavaClass(config.getClass())); }
    }

    private static class Delegate {
        private String delegate;
        private String value;

        public Delegate(String delegate, String value) {
            this.delegate = delegate;
            this.value = value;
        }

        public String getDelegate() { return delegate; }

        public String getValue() { return value; }
    }

    private static class PropertyBased {
        private String property1;
        private int property2;

        @JsonProperty("property1")
        public String getProperty1() { return property1; }

        @JsonProperty("property2")
        public int getProperty2() { return property2; }

        @JsonCreator
        public PropertyBased(@JsonProperty("property1") String property1, @JsonProperty("property2") int property2) {
            this.property1 = property1;
            this.property2 = property2;
        }

        @JsonProperty
        public void setProperty1(String property1) { this.property1 = property1; }

        @JsonProperty
        public void setProperty2(int property2) { this.property2 = property2; }
    }

    private static class ArrayDelegate {
        private String[] delegate;

        @JsonCreator
        public ArrayDelegate(@JsonProperty("delegate") String[] delegate) { this.delegate = delegate; }

        public String[] getDelegate() { return delegate; }
    }
}