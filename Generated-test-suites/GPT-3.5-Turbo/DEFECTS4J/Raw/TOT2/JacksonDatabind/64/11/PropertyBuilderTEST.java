package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;
import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testGetPropertyBuilder() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        assertNotNull(propertyBuilder);
    }

    @Test
    public void testBuildWriter() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = new JavaType();
        JsonSerializer<?> ser = new JsonSerializer<>();
        TypeSerializer typeSer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        AnnotatedMember am = new AnnotatedMember();
        boolean defaultUseStaticTyping = true;

        try {
            BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
            assertNotNull(beanPropertyWriter);
        } catch (JsonMappingException e) {
            fail("Failed to build BeanPropertyWriter");
        }
    }

    @Test
    public void testFindSerializationType() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        AnnotatedMember a = new AnnotatedMember();
        boolean useStaticTyping = true;
        JavaType declaredType = new JavaType();

        try {
            JavaType serializationType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
            assertNotNull(serializationType);
        } catch (JsonMappingException e) {
            fail("Failed to find serialization type");
        }
    }

    @Test
    public void testGetDefaultBean() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        String name = "test";
        AnnotatedMember member = new AnnotatedMember();
        JavaType type = new JavaType();

        Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertNull(defaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType type = new JavaType();

        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertNull(defaultValue);
    }

    @Test
    public void testThrowWrapped() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Exception exception = new Exception();
        String propName = "test";
        Object defaultBean = new Object();

        try {
            propertyBuilder._throwWrapped(exception, propName, defaultBean);
        } catch (Exception e) {
            fail("Failed to throw wrapped exception");
        }
    }
}