package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testBuildWriter() {
        PropertyBuilder propertyBuilder = new PropertyBuilder();
        JsonSerializer<?> serializer = new JsonSerializer<>();
        TypeSerializer typeSerializer = new TypeSerializer();
        AnnotatedMember annotatedMember = new AnnotatedMember();
        BeanPropertyDefinition beanPropertyDefinition = new BeanPropertyDefinition();
        JavaType javaType = new JavaType();
        boolean defaultUseStaticTyping = true;

        try {
            BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(
                    new SerializerProvider(),
                    beanPropertyDefinition,
                    javaType,
                    serializer,
                    typeSerializer,
                    null,
                    annotatedMember,
                    defaultUseStaticTyping);

            Assert.assertNotNull(beanPropertyWriter);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testFindSerializationType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder();
        Annotated annotated = new Annotated();
        boolean useStaticTyping = true;
        JavaType declaredType = new JavaType();

        try {
            JavaType serializationType = propertyBuilder.findSerializationType(
                    annotated,
                    useStaticTyping,
                    declaredType);

            Assert.assertNull(serializationType);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder();
        String name = "property";
        AnnotatedMember member = new AnnotatedMember();
        JavaType type = new JavaType();

        Object defaultValue = propertyBuilder.getPropertyDefaultValue(
                name,
                member,
                type);

        Assert.assertNull(defaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder();
        JavaType type = new JavaType();

        Object defaultValue = propertyBuilder.getDefaultValue(type);

        Assert.assertNull(defaultValue);
    }
}