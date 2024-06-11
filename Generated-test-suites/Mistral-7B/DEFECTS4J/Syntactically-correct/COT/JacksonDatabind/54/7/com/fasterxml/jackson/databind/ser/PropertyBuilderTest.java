package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private SerializationConfig config;
    private BeanDescription beanDesc;

    @Before
    public void setUp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        this.config = mapper.getSerializationConfig();
        this.beanDesc = mapper.findBeanDescription(Object[].class);
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = new PropertyBuilder(config, beanDesc).getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void testBuildWriter_NoArguments() throws JsonMappingException {
        ObjectWriter writer = new ObjectMapper().writerWithRootValue(new Object());
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        BeanPropertyWriter result = propertyBuilder.buildWriter(writer.getSerializerProvider(), null, null, null, null, null, null, false);
        assertNotNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildWriter_NullContentTypeSer() throws JsonMappingException {
        ObjectWriter writer = new ObjectMapper().writerWithRootValue(new Object());
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        BeanPropertyWriter result = propertyBuilder.buildWriter(writer.getSerializerProvider(), null, null, null, null, null, null, false);
        TypeSerializer contentTypeSer = null;
        result = propertyBuilder.buildWriter(writer.getSerializerProvider(), null, null, null, null, contentTypeSer, null, false);
    }

    @Test
    public void testFindSerializationType_ValidAnnotations() throws JsonMappingException {
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        JavaType declaredType = new TypeReference<Object[]>() {}.getType();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType result = propertyBuilder.findSerializationType(am, false, declaredType);
        assertEquals(Integer[].class, result.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationType_InvalidAnnotation() throws JsonMappingException {
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        JavaType declaredType = new TypeReference<Object[]>() {}.getType();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        propertyBuilder.findSerializationType(am, false, declaredType);
        JsonInclude annotation = Mockito.mock(JsonInclude.class);
        when(am.getAnnotation(JsonInclude.class)).thenReturn(annotation);
        when(annotation.value()).thenReturn("INVALID_VALUE");
    }

    @Test
    public void testGetPropertyDefaultValue_ValidProperty() throws Exception {
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        Object defaultBean = new Object();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object result = propertyBuilder.getPropertyDefaultValue("propertyName", member, new TypeReference<Object>() {}.getType());
        assertEquals(defaultBean, result);
    }

    @Test
    public void testGetPropertyDefaultValue_NullDefaultBean() throws Exception {
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        Object defaultBean = null;
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object result = propertyBuilder.getPropertyDefaultValue("propertyName", member, new TypeReference<Object>() {}.getType());
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValue_InvalidProperty() throws Exception {
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        Object defaultBean = new Object();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        propertyBuilder.getPropertyDefaultValue("invalidPropertyName", member, new TypeReference<Object>() {}.getType());
    }

    @Test
    public void testGetDefaultValue_PrimitiveType() {
        JavaType type = new TypeReference<Integer>() {}.getType();
        Object result = new PropertyBuilder(config, beanDesc).getDefaultValue(type);
        assertEquals(0, result);
    }

    @Test
    public void testGetDefaultValue_StringType() {
        JavaType type = new TypeReference<String>() {}.getType();
        Object result = new PropertyBuilder(config, beanDesc).getDefaultValue(type);
        assertEquals("", result);
    }

    @Test(expected = NullPointerException.class)
    public void testGetDefaultValue_NullType() {
        new PropertyBuilder(config, beanDesc).getDefaultValue(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultValue_InvalidType() {
        JavaType type = new TypeReference<Object>() {}.getType();
        new PropertyBuilder(config, beanDesc).getDefaultValue(type);
    }
}