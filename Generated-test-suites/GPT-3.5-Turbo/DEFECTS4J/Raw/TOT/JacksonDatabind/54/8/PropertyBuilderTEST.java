package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    @Test
    public void testGetClassAnnotations() {
        SerializationConfig config = new SerializationConfig(null, null, null);
        BeanDescription beanDesc = new BeanDescription(null, null, null);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

        Annotations annotations = builder.getClassAnnotations();
        
        assertNotNull(annotations);
    }
    
    @Test
    public void testBuildWriter() {
        SerializationConfig config = new SerializationConfig(null, null, null);
        BeanDescription beanDesc = new BeanDescription(null, null, null);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

        SerializerProvider prov = new SerializerProvider(null, null, null);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(null, null, null, null, null, null);
        JavaType declaredType = new JavaType(null, null, null);
        JsonSerializer<?> ser = new JsonSerializer<>(null);
        TypeSerializer typeSer = new TypeSerializer(null, null);
        TypeSerializer contentTypeSer = new TypeSerializer(null, null);
        AnnotatedMember am = new AnnotatedMember(null, null);
        boolean defaultUseStaticTyping = false;

        try {
            BeanPropertyWriter writer = builder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
            
            assertNotNull(writer);
        } catch (JsonMappingException e) {
            fail("JsonMappingException thrown");
        }
    }
    
    @Test
    public void testFindSerializationType() {
        SerializationConfig config = new SerializationConfig(null, null, null);
        BeanDescription beanDesc = new BeanDescription(null, null, null);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

        Annotated a = new Annotated(null, null);
        boolean useStaticTyping = false;
        JavaType declaredType = new JavaType(null, null, null);

        try {
            JavaType serializationType = builder.findSerializationType(a, useStaticTyping, declaredType);
            
            assertNull(serializationType);
        } catch (JsonMappingException e) {
            fail("JsonMappingException thrown");
        }
    }
    
    @Test
    public void testGetDefaultBean() {
        SerializationConfig config = new SerializationConfig(null, null, null);
        BeanDescription beanDesc = new BeanDescription(null, null, null);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

        Object defaultBean = builder.getDefaultBean();
        
        assertNull(defaultBean);
    }
    
    @Test
    public void testGetPropertyDefaultValue() {
        SerializationConfig config = new SerializationConfig(null, null, null);
        BeanDescription beanDesc = new BeanDescription(null, null, null);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

        String name = "testName";
        AnnotatedMember member = new AnnotatedMember(null, null);
        JavaType type = new JavaType(null, null, null);

        Object defaultValue = builder.getPropertyDefaultValue(name, member, type);
        
        assertNull(defaultValue);
    }
    
    @Test
    public void testGetDefaultValue() {
        SerializationConfig config = new SerializationConfig(null, null, null);
        BeanDescription beanDesc = new BeanDescription(null, null, null);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

        JavaType type = new JavaType(null, null, null);

        Object defaultValue = builder.getDefaultValue(type);
        
        assertNull(defaultValue);
    }
    
    @Test
    public void testThrowWrapped() {
        SerializationConfig config = new SerializationConfig(null, null, null);
        BeanDescription beanDesc = new BeanDescription(null, null, null);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

        Exception e = new Exception();
        String propName = "testProp";
        Object defaultBean = new Object();

        try {
            builder._throwWrapped(e, propName, defaultBean);
        } catch (IllegalArgumentException ex) {
            assertEquals(ex.getMessage(), "Failed to get property 'testProp' of default java.lang.Object instance");
        }
    }
}