package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        SerializationConfig config = new SerializationConfig(null, null, null, null, null, null);
        BeanDescription beanDesc = new BeanDescription(null, null, null, null, null);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void testBuildWriter() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = new JavaType();
        JsonSerializer<?> ser = new JsonSerializer<>();
        TypeSerializer typeSer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        AnnotatedMember am = new AnnotatedMember();
        boolean defaultUseStaticTyping = true;
        try {
            propertyBuilder.buildWriter(null, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        } catch (JsonMappingException e) {
            fail("JsonMappingException should not be thrown");
        }
    }

    @Test
    public void testFindSerializationType() {
        Annotated a = new Annotated();
        boolean useStaticTyping = true;
        JavaType declaredType = new JavaType();
        try {
            propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
        } catch (JsonMappingException e) {
            fail("JsonMappingException should not be thrown");
        }
    }

    @Test
    public void testGetPropertyDefaultValue() {
        String name = "test";
        AnnotatedMember member = new AnnotatedMember();
        JavaType type = new JavaType();
        assertNull(propertyBuilder.getPropertyDefaultValue(name, member, type));
    }

    @Test
    public void testGetDefaultValue() {
        JavaType type = new JavaType();
        assertNull(propertyBuilder.getDefaultValue(type));
    }

    @Test
    public void testThrowWrapped() {
        Exception e = new Exception("test");
        String propName = "test";
        Object defaultBean = new Object();
        try {
            propertyBuilder._throwWrapped(e, propName, defaultBean);
        } catch (IllegalArgumentException ex) {
            fail("IllegalArgumentException should not be thrown");
        }
    }
}