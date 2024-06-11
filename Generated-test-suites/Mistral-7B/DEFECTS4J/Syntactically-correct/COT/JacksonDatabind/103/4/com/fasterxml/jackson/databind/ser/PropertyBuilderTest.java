package com.example.test; // Replace this with the actual test package

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing;

import org.junit.Test;

public class PropertyBuilderTest {

    

    @Test
    public void testPropertyBuilderConstructor() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        assertNotNull(propertyBuilder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPropertyBuilderWithNullSerializationConfig() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        new PropertyBuilder(null, beanDesc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPropertyBuilderWithNullBeanDescription() {
        SerializationConfig config = new SerializationConfig();
        new PropertyBuilder(config, null);
    }

    @Test
    public void testGetClassAnnotations() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void testBuildWriterWithNoAnnotation() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        ObjectWriter objectWriter = config.builder();
        JavaType javaType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Integer[].class);
        SerializerProvider prov = new SimpleSerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(new AnnotatedMember(null, "array", null), null, null, null, null, null, null, null, null, javaType, null, null, null, null, null, null);
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(prov, propDef, javaType, null, null, null, null, false);
        assertNotNull(beanPropertyWriter);
    }

    @Test
    public void testBuildWriterWithAnnotation() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        ObjectWriter objectWriter = config.builder();
        JavaType javaType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Integer[].class);
        SerializerProvider prov = new SimpleSerializerProvider();
        JsonInclude annotation = new JsonInclude();
        annotation.setInclude(JsonInclude.Include.NON_NULL);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(new AnnotatedMember(null, "array", null), null, null, null, null, null, null, null, null, javaType, null, null, null, new Object[] { annotation }, null, null);
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(prov, propDef, javaType, null, null, null, null, false);
        assertNotNull(beanPropertyWriter);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriterWithNullBeanPropertyDefinition() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);
        ObjectWriter objectWriter = config.builder();
        JavaType javaType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Integer.class);
        SerializerProvider prov = new SimpleSerializerProvider();
        BeanPropertyDefinition propDef = null;
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(prov, propDef, javaType, null, null, null, null, false);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriterWithNullJavaType() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        ObjectWriter objectWriter = config.builder();
        SerializerProvider prov = new SimpleSerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(new AnnotatedMember(null, "array", null), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(prov, propDef, null, null, null, null, null, false);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriterWithNullJsonSerializer() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        ObjectWriter objectWriter = config.builder();
        JavaType javaType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Integer.class);
        SerializerProvider prov = new SimpleSerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(new AnnotatedMember(null, "array", null), null, null, null, null, null, null, null, null, javaType, null, null, null, null, null, null);
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(prov, propDef, javaType, null, null, null, null, false);
        JsonSerializer<Object> jsonSerializer = null;
        beanPropertyWriter.assignNullSerializer(jsonSerializer);
    }

    @Test
    public void testFindSerializationTypeWithNoAnnotation() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotated member = new AnnotatedMember(null, "name", null);
        JavaType javaType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, javaType, null, null, null, null, false);
        JavaType result = propertyBuilder.findSerializationType(member, false, javaType);
        assertTrue(result == null || result.equals(javaType));
    }

    @Test
    public void testFindSerializationTypeWithJsonSerializeAnnotation() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotated member = new AnnotatedMember(null, "name", null);
        JavaType javaType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        JsonSerialize jsonSerializeAnnotation = new JsonSerialize();
        jsonSerializeAnnotation.using(new StdSerializer<>(String.class));
        member.addAnnotation(jsonSerializeAnnotation);
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, javaType, null, null, null, null, false);
        JavaType result = propertyBuilder.findSerializationType(member, false, javaType);
        assertTrue(result != null && result.equals(jsonSerializeAnnotation.using().getClass()));
    }

    @Test
    public void testFindSerializationTypeWithJsonTypeNameSerializationAnnotation() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotated member = new AnnotatedMember(null, "name", null);
        JavaType javaType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        JsonTypeNameSerializer jsonTypeNameSerializationAnnotation = new JsonTypeNameSerializer();
        member.addAnnotation(jsonTypeNameSerializationAnnotation);
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, javaType, null, null, null, null, false);
        JavaType result = propertyBuilder.findSerializationType(member, false, javaType);
        assertTrue(result != null && result.equals(TypeSerializer.instance.getSerializerProvider().findValueSerializer(String.class).getClass()));
    }

    @Test(expected = JsonMappingException.class)
    public void testFindSerializationTypeWithNullAnnotatedMember() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType javaType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, javaType, null, null, null, null, false);
        JavaType result = propertyBuilder.findSerializationType(null, false, javaType);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindSerializationTypeWithNullJavaType() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotated member = new AnnotatedMember(null, "name", null);
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
        JavaType result = propertyBuilder.findSerializationType(member, false, null);
    }

    @Test
    public void testGetClassName() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        String result = propertyBuilder.getClass().getName();
        assertEquals("com.fasterxml.jackson.databind.ser.PropertyBuilder", result);
    }

    @Test
    public void testGetDefaultBean() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object result = propertyBuilder.getDefaultBean();
        assertNull(result);
    }

    @Test
    public void testGetPropertyDefaultValueWithDefaultBean() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();
        Object result = propertyBuilder.getPropertyDefaultValue("name", null, Object.class);
        assertNull(result);
        if (defaultBean != null) {
            result = propertyBuilder.getPropertyDefaultValue("name", new AnnotatedMember(null, "name", null), Object.class);
            assertNull(result);
        }
    }

    @Test
    public void testGetPropertyDefaultValueWithDefaultValue() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();
        Object result = propertyBuilder.getPropertyDefaultValue("name", null, Integer.class);
        assertEquals(Integer.valueOf(0), result);
        if (defaultBean != null) {
            result = propertyBuilder.getPropertyDefaultValue("name", new AnnotatedMember(null, "name", null), Integer.class);
            assertEquals(Integer.valueOf(0), result);
        }
    }

    @Test
    public void testGetPropertyDefaultValueWithAnnotation() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();
        Object result = propertyBuilder.getPropertyDefaultValue("name", new AnnotatedMember(null, "name", null), String[].class);
        assertNull(result);
        if (defaultBean != null) {
            result = propertyBuilder.getPropertyDefaultValue("name", new AnnotatedMember(null, "name", null), String[].class);
            assertNull(result);
        }
        JsonInclude jsonIncludeAnnotation = new JsonInclude();
        jsonIncludeAnnotation.setInclude(JsonInclude.Include.NON_DEFAULT);
        AnnotatedMember annotatedMember = new AnnotatedMember(null, "array", null);
        annotatedMember.addAnnotation(jsonIncludeAnnotation);
        result = propertyBuilder.getPropertyDefaultValue("array", annotatedMember, String[].class);
        assertTrue(result == null || result.equals(BeanUtil.getDefaultValue(String[].class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThrowWrappedWithNullException() {
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);
        String propName = "name";
        Object defaultBean = new Object();
        propertyBuilder._throwWrapped(null, propName, defaultBean);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThrowWrappedWithNonErrorException() {
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);
        String propName = "name";
        Object defaultBean = new Object();
        propertyBuilder._throwWrapped(new RuntimeException(), propName, defaultBean);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThrowWrappedWithNullPropertyName() {
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);
        Object defaultBean = new Object();
        propertyBuilder._throwWrapped(new IllegalArgumentException("Message"), null, defaultBean);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThrowWrappedWithNullDefaultBean() {
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);
        String propName = "name";
        propertyBuilder._throwWrapped(new IllegalArgumentException("Message"), propName, null);
    }

    
}