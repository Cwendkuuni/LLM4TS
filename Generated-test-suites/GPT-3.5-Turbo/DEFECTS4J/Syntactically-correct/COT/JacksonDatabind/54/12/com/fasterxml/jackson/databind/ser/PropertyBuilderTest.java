
package com.fasterxml.jackson.databind.ser;

import org.junit.Assert;
import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testBuildWriter() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = new JavaType();
        JsonSerializer<?> ser = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;

        try {
            BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
            Assert.assertNotNull(writer);
        } catch (JsonMappingException e) {
            Assert.fail("Failed to build writer: " + e.getMessage());
        }
    }

    @Test
    public void testFindSerializationType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Annotated a = null;
        boolean useStaticTyping = false;
        JavaType declaredType = new JavaType();

        try {
            JavaType serializationType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
            Assert.assertNull(serializationType);
        } catch (JsonMappingException e) {
            Assert.fail("Failed to find serialization type: " + e.getMessage());
        }
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        JavaType type = new JavaType(String.class);

        Object defaultValue = propertyBuilder.getDefaultValue(type);
        Assert.assertEquals("", defaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        String name = "propertyName";
        AnnotatedMember member = null;
        JavaType type = new JavaType(String.class);

        Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        Assert.assertEquals("", defaultValue);
    }

}
