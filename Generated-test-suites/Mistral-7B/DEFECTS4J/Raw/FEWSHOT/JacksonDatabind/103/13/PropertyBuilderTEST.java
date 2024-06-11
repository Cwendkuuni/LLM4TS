package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanDescription;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    @Test
    public void testPropertyBuilder() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        assertNotNull(propertyBuilder);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        // Test case for when annotations are used to force type
        AnnotatedMember am = new AnnotatedField();
        JavaType declaredType = new SimpleType(int.class);
        JavaType secondary = new SimpleType(Integer.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertEquals(secondary, serializationType);

        // Test case for when static typing for serialization is enabled
        propertyBuilder = new PropertyBuilder(null, null);
        useStaticTyping = true;
        declaredType = new SimpleType(int[].class);
        serializationType = propertyBuilder.findSerializationType(am, useStaticTyping, declaredType);
        assertEquals(declaredType, serializationType);

        // Test case for when no annotations or declared type are available
        propertyBuilder = new PropertyBuilder(null, null);
        am = new AnnotatedMethod();
        declaredType = new SimpleType(String.class);
        serializationType = propertyBuilder.findSerializationType(am, useStaticTyping, declaredType);
        assertNull(serializationType);
    }

    @Test
    public void testGetClassAnnotations() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDesc);
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        AnnotatedMember am = new AnnotatedField();
        JavaType declaredType = new SimpleType(String.class);
        JsonSerializer<?> ser = new JsonSerializer<>();
        TypeSerializer typeSer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        Object valueToSuppress = null;
        boolean suppressNulls = false;

        // Test case for when no type serializer for content is provided
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(null, null, declaredType, ser, typeSer, null, am, false);
        assertNotNull(bpw);

        // Test case for when type serializer for content is provided
        JavaType ct = new SimpleType(int.class);
        bpw = propertyBuilder.buildWriter(null, null, declaredType, ser, typeSer, contentTypeSer, am, false);
        assertNotNull(bpw);
        assertEquals(ct, bpw.getContentType());

        // Test case for when null is passed as serializationType
        declaredType = new SimpleType(String[].class);
        bpw = propertyBuilder.buildWriter(null, null, declaredType, ser, null, contentTypeSer, am, false);
        assertNotNull(bpw);
        assertNull(bpw.getSerializationType());

        // Test case for when contentTypeSer is null
        bpw = propertyBuilder.buildWriter(null, null, declaredType, ser, typeSer, null, am, false);
        assertNotNull(bpw);
        assertNull(bpw.getContentType());

        // Test case for when serializationType is null
        bpw = propertyBuilder.buildWriter(null, null, null, ser, typeSer, contentTypeSer, am, false);
        assertNotNull(bpw);
        assertNull(bpw.getSerializationType());
    }

    @Test
    public void testFindSerializationTyping() {
        AnnotatedMember am = new AnnotatedField();
        JsonSerialize.Typing typing = new JsonSerialize.Typing();
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        typing = propertyBuilder.findSerializationTyping(am);
        assertEquals(JsonSerialize.Typing.DEFAULT_TYPING, typing);
    }

    @Test
    public void testRefineSerializationType() throws JsonMappingException {
        AnnotatedMember am = new AnnotatedField();
        JavaType declaredType = new SimpleType(int.class);
        JavaType secondary = new SimpleType(Integer.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        JavaType serializationType = propertyBuilder.refineSerializationType(null, am, declaredType);
        assertEquals(secondary, serializationType);
    }
}