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
        SerializationConfig config = new SerializationConfig(null, null, null);
        BeanDescription beanDesc = new BeanDescription(null, null);
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void testBuildWriter() {
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(null, null, null, null);
        JavaType declaredType = new JavaType(null, 0, null, null, false);
        JsonSerializer<?> ser = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;

        try {
            propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        } catch (JsonMappingException e) {
            fail("JsonMappingException was thrown");
        }
    }

    @Test
    public void testFindSerializationType() {
        Annotated a = new Annotated() {
            @Override
            public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> aClass) {
                return null;
            }

            @Override
            public java.lang.annotation.Annotation[] getAnnotations() {
                return new java.lang.annotation.Annotation[0];
            }

            @Override
            public java.lang.annotation.Annotation[] getDeclaredAnnotations() {
                return new java.lang.annotation.Annotation[0];
            }
        };
        boolean useStaticTyping = false;
        JavaType declaredType = new JavaType(null, 0, null, null, false);

        try {
            propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
        } catch (JsonMappingException e) {
            fail("JsonMappingException was thrown");
        }
    }

    @Test
    public void testGetDefaultBean() {
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        String name = "property";
        AnnotatedMember member = null;
        JavaType type = new JavaType(null, 0, null, null, false);

        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
        assertNull(propertyDefaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        JavaType type = new JavaType(null, 0, null, null, false);

        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertNull(defaultValue);
    }

    @Test
    public void testThrowWrapped() {
        Exception e = new Exception();
        String propName = "property";
        Object defaultBean = new Object();

        try {
            propertyBuilder._throwWrapped(e, propName, defaultBean);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Failed to get property 'property' of default java.lang.Object instance", ex.getMessage());
        }
    }
}