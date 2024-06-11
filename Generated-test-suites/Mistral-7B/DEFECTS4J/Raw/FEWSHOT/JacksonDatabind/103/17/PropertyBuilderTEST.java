package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;
    private AnnotationIntrospector annotationIntrospector;

    @Before
    public void setup() {
        config = new SerializationConfig();
        beanDesc = new BeanDescription(Object.class);
        annotationIntrospector = new StandardAnnotationIntrospector();
    }

    @After
    public void tearDown() {
        config = null;
        beanDesc = null;
        annotationIntrospector = null;
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = beanDesc.getClassAnnotations();
        assertNotNull(annotations);
        assertTrue(annotations.isEmpty());
    }

    @Test
    public void testBuildWriterWithSimpleType() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        SerializerProvider serializerProvider = new SerializerProvider();
        ObjectIdWriter objectIdWriter = new ObjectIdWriter();
        BeanSerializerBase beanSerializerBase = new BeanSerializerBase(propertyBuilder, serializerProvider, beanDesc, javaType, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        JsonSerializer<Object> jsonSerializer = new StdSerializer<>(String.class) {};
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, null, javaType, jsonSerializer, null, null, null, false);
        assertNotNull(beanPropertyWriter);
    }

    @Test
    public void testBuildWriterWithContainerType() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, String.class);
        SerializerProvider serializerProvider = new SerializerProvider();
        ObjectIdWriter objectIdWriter = new ObjectIdWriter();
        BeanSerializerBase beanSerializerBase = new BeanSerializerBase(propertyBuilder, serializerProvider, beanDesc, javaType, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        JsonSerializer<Object> jsonSerializer = new StdSerializer<>(List.class) {};
        TypeSerializer typeSerializer = null;
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, null, javaType, jsonSerializer, null, typeSerializer, null, false);
        assertNotNull(beanPropertyWriter);
    }

    @Test
    public void testFindSerializationType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        propertyBuilder.findSerializationType(annotatedMember, false, javaType);
    }

    @Test
    public void testGetDefaultBean() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        Object defaultBean = new Object();
        when(annotatedMember.getValue(defaultBean)).thenReturn("defaultValue");
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("test", annotatedMember, javaType);
        assertEquals("defaultValue", propertyDefaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType javaType = new TypeReference<>("java.util.List<java.lang.String>") {}.getType();
        Object defaultValue = propertyBuilder.getDefaultValue(javaType);
        assertNull(defaultValue);
    }
}