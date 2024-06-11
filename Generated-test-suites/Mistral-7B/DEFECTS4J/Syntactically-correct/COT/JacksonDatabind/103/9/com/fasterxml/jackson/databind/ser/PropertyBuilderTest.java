package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class PropertyBuilderTest {

    private ObjectMapper objectMapper;
    private SerializationConfig serializationConfig;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        serializationConfig = objectMapper.getSerializationConfig();
    }

    @Test
    public void testPropertyBuilderConstructor() {
        BeanDescription beanDescription = new BeanDescription(null, null, null, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);
        Assert.assertNotNull(propertyBuilder);
    }

    @Test
    public void testBuildWriterWithNullSerializerProvider() {
        BeanDescription beanDescription = new BeanDescription(null, null, null, null);
        SerializerProvider serializerProvider = null;
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);
        Assert.assertNull(propertyBuilder.buildWriter(serializerProvider, null, null, null, null, null, null, false));
    }

    @Test
    public void testBuildWriterWithNullBeanPropertyDefinition() {
        SerializerProvider serializerProvider = new TestSerializerProvider();
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, null);
        Assert.assertNull(propertyBuilder.buildWriter(serializerProvider, null, null, null, null, null, null, false));
    }

    @Test
    public void testBuildWriterWithNullDeclaredType() {
        SerializerProvider serializerProvider = new TestSerializerProvider();
        BeanPropertyDefinition beanPropertyDefinition = new BeanPropertyDefinition(null, null, null, null, null, null, null, null, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, null);
        Assert.assertNull(propertyBuilder.buildWriter(serializerProvider, beanPropertyDefinition, null, null, null, null, null, false));
    }

    @Test
    public void testBuildWriterWithNullJsonSerializer() {
        SerializerProvider serializerProvider = new TestSerializerProvider();
        BeanPropertyDefinition beanPropertyDefinition = new BeanPropertyDefinition(null, null, null, null, null, null, null, null, null);
        JavaType declaredType = new SimpleType(int[].class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, null);
        Assert.assertNull(propertyBuilder.buildWriter(serializerProvider, beanPropertyDefinition, declaredType, null, null, null, null, false));
    }

    @Test
    public void testBuildWriterWithNullTypeSerializer() {
        SerializerProvider serializerProvider = new TestSerializerProvider();
        BeanPropertyDefinition beanPropertyDefinition = new BeanPropertyDefinition(null, null, null, null, null, null, null, null, null);
        BeanPropertyWriter beanPropertyWriter = Mockito.mock(BeanPropertyWriter.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, null);
        Assert.assertNull(propertyBuilder.buildWriter(serializerProvider, beanPropertyDefinition, null, null, null, null, beanPropertyWriter, false));
    }

    @Test
    public void testBuildWriterWithNullAnnotatedMember() {
        SerializerProvider serializerProvider = new TestSerializerProvider();
        BeanPropertyDefinition beanPropertyDefinition = new BeanPropertyDefinition(null, null, null, null, null, null, null, null, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, null);
        Assert.assertNull(propertyBuilder.buildWriter(serializerProvider, beanPropertyDefinition, null, null, null, null, null, false));
    }

    @Test
    public void testBuildWriterWithBooleanDefaultUseStaticTyping() {
        SerializerProvider serializerProvider = new TestSerializerProvider();
        BeanPropertyDefinition beanPropertyDefinition = new BeanPropertyDefinition(null, null, null, null, null, null, null, null, null);
        JavaType declaredType = new SimpleType(int[].class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, null);
        Assert.assertNotNull(propertyBuilder.buildWriter(serializerProvider, beanPropertyDefinition, declaredType, null, null, null, null, true));
    }

    @Test
    public void testFindSerializationTypeWithNullAnnotatedMemberAndUseStaticTypingFalse() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, null);
        Assert.assertNull(propertyBuilder.findSerializationType(null, false, null));
    }

    @Test
    public void testFindSerializationTypeWithNullAnnotatedMemberAndUseStaticTypingTrue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, null);
        JavaType javaType = new SimpleType(int[].class);
        Assert.assertNotNull(propertyBuilder.findSerializationType(null, true, javaType));
    }

    @Test
    public void testFindSerializationTypeWithValidAnnotatedMemberAndUseStaticTypingFalse() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, null);
        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
        JavaType javaType = new SimpleType(int[].class);
        Assert.assertNull(propertyBuilder.findSerializationType(annotatedMember, false, javaType));
    }

    @Test
    public void testFindSerializationTypeWithValidAnnotatedMemberAndUseStaticTypingTrue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, null);
        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
        JavaType javaType = new SimpleType(int[].class);
        Assert.assertNotNull(propertyBuilder.findSerializationType(annotatedMember, true, javaType));
    }

    @Test
    public void testFindSerializationTypeWithValidAnnotatedMemberAndDifferentDeclaredType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, null);
        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
        JavaType javaType = new SimpleType(String.class);
        Assert.assertNull(propertyBuilder.findSerializationType(annotatedMember, false, javaType));
    }

    @Test
    public void testFindSerializationTypeWithValidAnnotatedMemberAndSameDeclaredType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, null);
        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
        JavaType javaType = new SimpleType(int[].class);
        Assert.assertSame(javaType, propertyBuilder.findSerializationType(annotatedMember, true, javaType));
    }

    @Test
    public void testGetClassAnnotations() {
        BeanDescription beanDescription = new BeanDescription(null, null, null, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);
        Assert.assertNotNull(propertyBuilder.getClassAnnotations());
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriterWithNullTypeSerializerThrowsJsonMappingException() {
        SerializerProvider serializerProvider = new TestSerializerProvider();
        BeanPropertyDefinition beanPropertyDefinition = new BeanPropertyDefinition(null, null, null, null, null, null, null, null, null);
        BeanPropertyWriter beanPropertyWriter = Mockito.mock(BeanPropertyWriter.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, null);
        propertyBuilder.buildWriter(serializerProvider, beanPropertyDefinition, null, null, null, null, beanPropertyWriter, false);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriterWithNullContentTypeSerializerThrowsJsonMappingException() {
        SerializerProvider serializerProvider = new TestSerializerProvider();
        BeanPropertyDefinition beanPropertyDefinition = new BeanPropertyDefinition(null, null, null, null, null, null, null, null, null);
        JavaType serializationType = new SimpleType(int[].class).withoutContentTypeHandler(null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, null);
        propertyBuilder.buildWriter(serializerProvider, beanPropertyDefinition, null, null, null, serializationType, null, false);
    }

    @Test
    public void testBuildWriterWithNonEmptyArrayValueToSuppress() {
        SerializerProvider serializerProvider = new TestSerializerProvider();
        BeanPropertyDefinition beanPropertyDefinition = new BeanPropertyDefinition(null, null, null, null, null, null, null, null, null);
        JavaType javaType = new SimpleType(int[].class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, null);
        Object valueToSuppress = new int[] {};
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, beanPropertyDefinition, javaType, null, null, null, null, false);
        Assert.assertEquals(BeanPropertyWriter.MARKER_FOR_EMPTY, valueToSuppress);
    }

    @Test
    public void testGetDefaultBean() {
        BeanDescription beanDescription = new BeanDescription(null, null, null, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);
        Object defaultBean = propertyBuilder.getDefaultBean();
        Assert.assertNotNull(defaultBean);
    }

    @Test
    public void testGetDefaultBeanReturnsNull() {
        BeanDescription beanDescription = new BeanDescription(null, null, null, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);
        Object defaultBean = propertyBuilder.getDefaultBean();
        Assert.assertNull(defaultBean);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetPropertyDefaultValueWithNullPropertyNameThrowsJsonMappingException() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, null);
        String propertyName = null;
        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
        JavaType javaType = new SimpleType(int[].class);
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, annotatedMember, javaType);
    }

    @Test(expected = JsonMappingException.class)
    public void testGetPropertyDefaultValueWithNullDefaultBeanThrowsJsonMappingException() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, null);
        String propertyName = "propertyName";
        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
        Object defaultBean = null;
        JavaType javaType = new SimpleType(int[].class);
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, annotatedMember, javaType);
    }

    @Test
    public void testGetPropertyDefaultValueWithStringPropertyNameAndIntType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, null);
        String propertyName = "propertyName";
        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
        JavaType javaType = new SimpleType(int.class);
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, annotatedMember, javaType);
        Assert.assertEquals(0, propertyDefaultValue);
    }

    @Test
    public void testGetPropertyDefaultValueWithStringPropertyNameAndArrayType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, null);
        String propertyName = "propertyName";
        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
        JavaType javaType = new SimpleType(int[].class);
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, annotatedMember, javaType);
        Assert.assertArrayEquals(new int[0], propertyDefaultValue);
    }

    @Test
    public void testGetPropertyDefaultValueWithNullJavaType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, null);
        String propertyName = "propertyName";
        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
        JavaType javaType = null;
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, annotatedMember, javaType);
        Assert.assertEquals(BeanUtil.getDefaultValue(javaType), propertyDefaultValue);
    }

    public static class TestSerializerProvider implements SerializerProvider {
        @Override
        public JsonSerializer<?> findValueSerializer(Class<?> type, JsonSerializer<?> serializer) {
            return null;
        }

        @Override
        public JsonSerializer<?> findSerializer(AnnotatedMember am, JsonSerializer<?> serializer) {
            return null;
        }

        @Override
        public void serializeValue(Object value, JsonGenerator jg, SerializerProvider provider) throws IOException {

        }

        @Override
        public void serializeValue(Object value, JsonGenerator jg, SerializerProvider provider, boolean flushed) throws IOException {

        }

        @Override
        public void serializeValue(Object value, JsonGenerator jg, SerializerProvider provider, TypeSerializer contentTypeSerializer) throws IOException {

        }

        @Override
        public void serializeValue(Object value, JsonGenerator jg, SerializerProvider provider, TypeSerializer contentTypeSerializer, boolean flushed) throws IOException {

        }

        @Override
        public void serializeWithType(Object value, JsonGenerator jg, SerializerProvider provider, TypeSerializer typeSerializer) throws IOException {

        }

        @Override
        public void serializeWithType(Object value, JsonGenerator jg, SerializerProvider provider, TypeSerializer typeSerializer, boolean flushed) throws IOException {

        }

        @Override
        public JsonSerializer<?> findNullSerializer(AnnotatedMember am) {
            return null;
        }
    }
}