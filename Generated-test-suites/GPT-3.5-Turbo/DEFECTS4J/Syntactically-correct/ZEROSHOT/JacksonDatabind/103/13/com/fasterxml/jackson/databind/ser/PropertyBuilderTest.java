package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.databind.*;
import org.junit.*;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    @Test
    public void testGetClassAnnotations() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotations annotations = propertyBuilder.getClassAnnotations();

        assertNull(annotations);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = new JavaType();
        JsonSerializer<?> ser = new JsonSerializer<>();
        TypeSerializer typeSer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        AnnotatedMember am = new AnnotatedMember();
        boolean defaultUseStaticTyping = true;

        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(prov, propDef, declaredType,
                ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        assertNull(beanPropertyWriter);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        Annotated a = new Annotated();
        boolean useStaticTyping = true;
        JavaType declaredType = new JavaType();

        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
        JavaType serializationType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);

        assertNull(serializationType);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        String name = "testProperty";
        AnnotatedMember member = new AnnotatedMember();
        JavaType type = new JavaType();

        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);

        assertNull(propertyDefaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        JavaType type = new JavaType();

        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
        Object defaultValue = propertyBuilder.getDefaultValue(type);

        assertNull(defaultValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThrowWrapped() {
        IllegalArgumentException ex = new IllegalArgumentException("test exception");
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());

        propertyBuilder._throwWrapped(ex, "testProperty", new Object());
    }

}