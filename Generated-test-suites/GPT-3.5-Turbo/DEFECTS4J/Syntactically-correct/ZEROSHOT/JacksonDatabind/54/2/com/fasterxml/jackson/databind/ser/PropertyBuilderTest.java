package com.fasterxml.jackson.databind.ser;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;

public class PropertyBuilderTest {

    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = new JavaType();
        JsonSerializer ser = new JsonSerializer();
        TypeSerializer typeSer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        AnnotatedMember am = new AnnotatedMember();
        boolean defaultUseStaticTyping = true;

        BeanPropertyWriter actual = propertyBuilder.buildWriter(null, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(actual);
    }
    
    @Test
    public void testFindSerializationType() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Annotated a = new Annotated();

        JavaType expected = new JavaType();
        JavaType actual = propertyBuilder.findSerializationType(a, true, expected);
        assertNull(actual);
    }
    
    @Test
    public void testGetPropertyDefaultValue() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        String name = "test";
        AnnotatedMember member = new AnnotatedMember();
        JavaType type = new JavaType();

        Object actual = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertNull(actual);
    }
    
    @Test
    public void testGetDefaultValue() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType type = new JavaType();

        Object actual = propertyBuilder.getDefaultValue(type);
        assertNull(actual);
    }

    @Test
    public void testThrowWrapped() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Exception e = new Exception();
        String propName = "test";
        Object defaultBean = new Object();

        try {
            propertyBuilder._throwWrapped(e, propName, defaultBean);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Failed to get property 'test' of default java.lang.Object instance", ex.getMessage());
        }
    }

}