package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import static org.junit.Assert.*;

public class PropertyBuilderTest {
    private SerializationConfig _config;
    private BeanDescription _beanDesc;
    private AnnotationIntrospector _annotationIntrospector;

    @Before
    public void setup() {
        _config = new SerializationConfig();
        _beanDesc = new BeanDescription(Object.class);
        _annotationIntrospector = new StandardAnnotationIntrospector();
    }

    @Test
    public void testBuildWriter_NoCustomTypeSerializer() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        ObjectMapper objectMapper = new ObjectMapper(_config);
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        BeanPropertyDefinition beanPropertyDefinition = new BeanPropertyDefinition("testProperty", javaType);
        SerializerProvider serializerProvider = new SerializerProvider();
        serializerProvider.setSerializerInstance("testProperty", new StringSerializer());
        Object defaultBean = new Object();
        Object valueToSuppress = "defaultValue";
        BeanPropertyWriter actualResult = propertyBuilder.buildWriter(serializerProvider, beanPropertyDefinition, javaType, null, null, null, null, false);

        assertEquals("defaultValue", actualResult.getValueToSuppress());
        assertNull(actualResult.getNullSerializer());
    }

    @Test
    public void testBuildWriter_WithCustomNullSerializer() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        ObjectMapper objectMapper = new ObjectMapper(_config);
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        BeanPropertyDefinition beanPropertyDefinition = new BeanPropertyDefinition("testProperty", javaType);
        SerializerProvider serializerProvider = new SerializerProvider();
        JsonSerializer<String> customNullSerializer = new StringSerializer() {};
        serializerProvider.setSerializerInstance("testProperty", customNullSerializer);
        Object defaultBean = new Object();
        Object valueToSuppress = "defaultValue";
        BeanPropertyWriter actualResult = propertyBuilder.buildWriter(serializerProvider, beanPropertyDefinition, javaType, null, null, null, null, false);

        assertEquals("defaultValue", actualResult.getValueToSuppress());
        assertEquals(customNullSerializer, actualResult.getNullSerializer());
    }

    @Test
    public void testBuildWriter_WithContentTypeSerializer() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        ObjectMapper objectMapper = new ObjectMapper(_config);
        JavaType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
        BeanPropertyDefinition beanPropertyDefinition = new BeanPropertyDefinition("testProperty", collectionType);
        SerializerProvider serializerProvider = new SerializerProvider();
        TypeSerializer contentTypeSerializer = new ArrayTypeSerializer(null, String.class);
        Object defaultBean = new Object();
        Object valueToSuppress = Collections.emptyList();
        BeanPropertyWriter actualResult = propertyBuilder.buildWriter(serializerProvider, beanPropertyDefinition, null, null, null, contentTypeSerializer, null, false);

        assertEquals(valueToSuppress, actualResult.getValueToSuppress());
        assertNull(actualResult.getNullSerializer());
    }

    @Test
    public void testBuildWriter_WithTypeSerializer() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        ObjectMapper objectMapper = new ObjectMapper(_config);
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        BeanPropertyDefinition beanPropertyDefinition = new BeanPropertyDefinition("testProperty", javaType);
        SerializerProvider serializerProvider = new SerializerProvider();
        TypeSerializer typeSerializer = new StandardSerializer(String.class);
        Object defaultBean = new Object();
        Object valueToSuppress = "defaultValue";
        BeanPropertyWriter actualResult = propertyBuilder.buildWriter(serializerProvider, beanPropertyDefinition, javaType, null, typeSerializer, null, null, false);

        assertEquals("defaultValue", actualResult.getValueToSuppress());
        assertEquals(typeSerializer, actualResult.getTypeSerializer());
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        AnnotatedMember am = new AnnotatedMember(String.class, "testProperty");
        JavaType declaredType = objectMapper.getTypeFactory().constructType(String.class);
        JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);

        assertEquals(declaredType, serializationType);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindSerializationType_ThrowsException() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        AnnotatedMember am = new AnnotatedMember(Object.class, "testProperty");
        JavaType declaredType = objectMapper.getTypeFactory().constructType(String.class);
        propertyBuilder.findSerializationType(am, false, declaredType);
    }

    @Test
    public void testFindSerializationType_UsesStaticTyping() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        AnnotatedMember am = new AnnotatedMember(List.class, "testProperty");
        JavaType declaredType = objectMapper.getTypeFactory().constructType(List.class);
        JavaType staticTypedType = declaredType.withStaticTyping();
        JavaType actualResult = propertyBuilder.findSerializationType(am, true, declaredType);

        assertEquals(staticTypedType, actualResult);
    }

    @Test
    public void testGetDefaultBean() throws JsonMappingException, InstantiationException, IllegalAccessException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();

        assertNotNull(defaultBean);
    }

    @Test
    public void testGetDefaultBean_ReturnsNull() throws JsonMappingException, InstantiationException, IllegalAccessException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();

        assertNull(defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        AnnotatedMember am = new AnnotatedMember(String.class, "testProperty");
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        Object defaultBean = new Object();
        Object valueToSuppress = "defaultValue";

        assertEquals("defaultValue", propertyBuilder.getPropertyDefaultValue("testProperty", am, javaType));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValue_ThrowsException() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        AnnotatedMember am = new AnnotatedMember(Object.class, "testProperty");
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        propertyBuilder.getPropertyDefaultValue("testProperty", am, javaType);
    }

    @Test
    public void testGetPropertyDefaultValue_ReturnsNull() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(_config, _beanDesc);
        AnnotatedMember am = new AnnotatedMember(String.class, "testProperty");
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        Object defaultBean = new Object();

        assertNull(propertyBuilder.getPropertyDefaultValue("testProperty", am, javaType));
    }
}