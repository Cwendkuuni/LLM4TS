package com.fasterxml.jackson.databind.ser;

import org.junit.Assert;
import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testBuildWriter() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        SerializerProvider prov = new SerializerProvider();
        JavaType declaredType = new JavaType();
        JsonSerializer<?> ser = new JsonSerializer<>();
        TypeSerializer typeSer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        AnnotatedMember am = new AnnotatedMember();
        boolean defaultUseStaticTyping = false;

        try {
            PropertyBuilder builder = new PropertyBuilder();
            BeanPropertyWriter writer = builder.buildWriter(prov, propDef, declaredType,
                    ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

            // Assert the writer is not null
            Assert.assertNotNull(writer);
        } catch (JsonMappingException e) {
            Assert.fail("Failed to build writer: " + e.getMessage());
        }
    }

    @Test
    public void testFindSerializationType() {
        Annotated a = new Annotated();
        boolean useStaticTyping = true;
        JavaType declaredType = new JavaType();

        try {
            PropertyBuilder builder = new PropertyBuilder();
            JavaType type = builder.findSerializationType(a, useStaticTyping, declaredType);

            // Assert the type is not null
            Assert.assertNotNull(type);
        } catch (JsonMappingException e) {
            Assert.fail("Failed to find serialization type: " + e.getMessage());
        }
    }

    @Test
    public void testGetDefaultValue() {
        JavaType type = new JavaType();

        PropertyBuilder builder = new PropertyBuilder();
        Object defaultValue = builder.getDefaultValue(type);

        // Assert the default value is not null
        Assert.assertNotNull(defaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        String propName = "propName";
        AnnotatedMember member = new AnnotatedMember();
        JavaType type = new JavaType();

        PropertyBuilder builder = new PropertyBuilder();
        Object propertyDefaultValue = builder.getPropertyDefaultValue(propName, member, type);

        // Assert the property default value is not null
        Assert.assertNotNull(propertyDefaultValue);
    }
}