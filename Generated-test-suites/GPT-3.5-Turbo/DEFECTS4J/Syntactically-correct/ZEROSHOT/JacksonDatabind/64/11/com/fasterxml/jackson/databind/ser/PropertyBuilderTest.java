package com.fasterxml.jackson.databind.ser;

import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyBuilderTest {

    @Test
    public void testBuildWriter() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = new JavaType();
        JsonSerializer ser = new JsonSerializer();
        TypeSerializer typeSer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        AnnotatedMember am = new AnnotatedMember();
        boolean defaultUseStaticTyping = true;

        try {
            BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
            assertNotNull(writer);
        } catch (JsonMappingException e) {
            fail("Should not throw JsonMappingException");
        }
    }

    @Test
    public void testFindSerializationType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
        Annotated a = new Annotated();
        boolean useStaticTyping = true;
        JavaType declaredType = new JavaType();

        try {
            JavaType serializationType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
            assertNull(serializationType);
        } catch (JsonMappingException e) {
            fail("Should not throw JsonMappingException");
        }
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
        JavaType type = new JavaType(Object.class);

        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertNull(defaultValue);
    }
}