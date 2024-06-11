package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Assert;
import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        // Create test objects
        Object testObject = new TestClassA();
        Object testObjectWithAnnotation = new TestClassB();

        // Test with no annotations
        BeanDescription beanDescNoAnnotations = new BeanDescription(TestClassA.class);
        PropertyBuilder propertyBuilderNoAnnotations = new PropertyBuilder(new SerializationConfig(null), beanDescNoAnnotations);
        JavaType actualTypeNoAnnotations = propertyBuilderNoAnnotations.findSerializationType(null, false, null);
        Assert.assertNull(actualTypeNoAnnotations);

        // Test with annotation that forces type to declared type
        BeanDescription beanDescAnnotation = new BeanDescription(TestClassB.class);
        PropertyBuilder propertyBuilderAnnotation = new PropertyBuilder(new SerializationConfig(null), beanDescAnnotation);
        JavaType actualTypeAnnotation = propertyBuilderAnnotation.findSerializationType(null, false, null);
        Assert.assertEquals(TestClassB.class, actualTypeAnnotation.getRawClass());

        // Test with annotation that forces type to super type
        BeanDescription beanDescSuperType = new BeanDescription(TestClassC.class);
        PropertyBuilder propertyBuilderSuperType = new PropertyBuilder(new SerializationConfig(null), beanDescSuperType);
        JavaType actualTypeSuperType = propertyBuilderSuperType.findSerializationType(null, false, null);
        Assert.assertEquals(TestClassA.class, actualTypeSuperType.getRawClass());
    }

    @Test
    public void testGetDefaultBean() {
        BeanDescription beanDesc = new BeanDescription(TestClassA.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(null), beanDesc);

        // Test with a POJO that has a default constructor
        Object defaultBean = propertyBuilder.getDefaultBean();
        Assert.assertNotNull(defaultBean);
        Assert.assertTrue(defaultBean instanceof TestClassA);

        // Test with a POJO that has no default constructor
        BeanDescription beanDescNoDefault = new BeanDescription(TestClassWithoutDefaultConstructor.class);
        PropertyBuilder propertyBuilderNoDefault = new PropertyBuilder(new SerializationConfig(null), beanDescNoDefault);
        Object noDefaultBean = propertyBuilderNoDefault.getDefaultBean();
        Assert.assertNull(noDefaultBean);
    }

    @Test
    public void testgetPropertyDefaultValue() {
        BeanDescription beanDesc = new BeanDescription(TestClassA.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(null), beanDesc);

        // Test with a property that has a default value
        String propertyName = "property1";
        AnnotatedMember member = new StubAnnotatedMember("property1", null, null, null, null);
        Object defaultBean = propertyBuilder.getDefaultBean();
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, member, null);
        Assert.assertEquals(0, defaultValue);

        // Test with a property that has no default value
        String propertyName2 = "property2";
        Object noDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName2, member, null);
        Assert.assertNull(noDefaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        SerializationConfig config = new SerializationConfig(null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, new BeanDescription(String.class));

        // Test with a primitive type
        Object defaultValueInt = propertyBuilder.getDefaultValue(ObjectTypeFactory.simpleValType(int.class));
        Assert.assertEquals(0, defaultValueInt);

        // Test with a wrapper type
        Object defaultValueInteger = propertyBuilder.getDefaultValue(ObjectTypeFactory.simpleValType(Integer.class));
        Assert.assertEquals(0, defaultValueInteger);

        // Test with a String
        Object defaultValueString = propertyBuilder.getDefaultValue(ObjectTypeFactory.simpleValType(String.class));
        Assert.assertEquals("", defaultValueString);

        // Test with a container type
        Object defaultValueArray = propertyBuilder.getDefaultValue(ObjectTypeFactory.simpleValType(Object[].class));
        Assert.assertEquals(JsonInclude.Include.NON_EMPTY, defaultValueArray);

        // Test with a reference type
        Object defaultValueBean = propertyBuilder.getDefaultValue(ObjectTypeFactory.simpleValType(TestClassA.class));
        Assert.assertEquals(JsonInclude.Include.NON_EMPTY, defaultValueBean);
    }

    static class TestClassWithoutDefaultConstructor {
        // Empty class with no default constructor
    }

    static class TestClassA {
        int property1 = 0;
    }

    static class TestClassB extends TestClassA {
        @JsonSerialize(using = ToStringSerializer.class)
        int property1B = 1;
    }

    static class TestClassC extends TestClassA {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        int property1C = 2;
    }

    static class StubAnnotatedMember implements AnnotatedMember {
        private final String name;

        public StubAnnotatedMember(String name, AnnotatedMember member, Annotation[] annotations,
                Annotation[] genericTypeAnnotations, JavaType type) {
            this.name = name;
        }

        @Override
        public Annotation[] getAnnotations() {
            return new Annotation[0];
        }

        @Override
        public Annotation[] getAnnotations(Class<? extends Annotation> annotationType) {
            return new Annotation[0];
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Type getGenericType() {
            return null;
        }

        @Override
        public JavaType getMemberType() {
            return null;
        }

        @Override
        public JavaType getType() {
            return null;
        }

        @Override
        public Object getValue(Object obj) throws IllegalAccessException, InvocationTargetException {
            return null;
        }

        @Override
        public void setValue(Object obj, Object value) throws IllegalAccessException, InvocationTargetException {
        }

        @Override
        public void setAccessible(boolean accessible) throws SecurityException {
        }
    }
}