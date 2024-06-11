package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.TypeSerializer;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.util.ArrayBuilders;
import com.fasterxml.jackson.annotation.JsonInclude;

public class PropertyBuilderTest {

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
        AnnotatedMember member = new AnnotatedMember();
        boolean defaultUseStaticTyping = true;
        
        try {
            BeanPropertyWriter actual = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, member, defaultUseStaticTyping);
            
            assertNotNull(actual);
        } catch (JsonMappingException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFindSerializationType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder();
        Annotated a = new Annotated();
        boolean useStaticTyping = true;
        JavaType declaredType = new JavaType();
        
        try {
            JavaType actual = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
            
            assertNull(actual);
        } catch (JsonMappingException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder();
        String name = "test";
        AnnotatedMember member = new AnnotatedMember();
        JavaType type = new JavaType();
        
        Object actual = propertyBuilder.getPropertyDefaultValue(name, member, type);
        
        assertNull(actual);
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder();
        JavaType type = new JavaType();
        
        Object actual = propertyBuilder.getDefaultValue(type);
        
        assertNull(actual);
    }

    @Test
    public void testThrowWrapped() {
        PropertyBuilder propertyBuilder = new PropertyBuilder();
        String propName = "test";
        Object defaultBean = new Object();
        Exception e = new Exception();
        
        try {
            propertyBuilder._throwWrapped(e, propName, defaultBean);
            fail("Exception not thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Failed to get property 'test' of default java.lang.Object instance", ex.getMessage());
        }
    }
}