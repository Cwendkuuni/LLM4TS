package com.fasterxml.jackson.databind.ser.test;

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

    private SerializationConfig config;
    private BeanDescription beanDesc;

    @Before
    public void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        config = objectMapper.getSerializationConfig();
        beanDesc = objectMapper.introspect(Foo.class);
    }

    @Test
    public void testClassAnnotations() {
        Annotations annotations = new PropertyBuilder(config, beanDesc).getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void testBuildWriter_default() throws JsonMappingException {
        SerializerProvider prov = new TestSerializerProvider();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(prov, null, null, null, null, null, null, true);
        assertNotNull(beanPropertyWriter);
    }

    @Test
    public void testBuildWriter_withSerializer() throws JsonMappingException {
        SerializerProvider prov = new TestSerializerProvider();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JsonSerializer<String> jsonSerializer = new StandardSerializer<>(String.class);
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(prov, null, null, jsonSerializer, null, null, null, true);
        assertNotNull(beanPropertyWriter);
        assertEquals(jsonSerializer, beanPropertyWriter.getSerializer());
    }

    @Test
    public void testFindSerializationType_withAnnotationOverride() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        AnnotatedMember am = new TestAnnotatedMember();
        JavaType declaredType = TypeFactory.type(String[].class, Integer.TYPE);
        JavaType serializationType = propertyBuilder.findSerializationType(am, true, declaredType);
        assertEquals(String[].class, serializationType.getRawClass());
    }

    @Test
    public void testFindSerializationType_withDeclaredType() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        AnnotatedMember am = new TestAnnotatedMember();
        JavaType declaredType = TypeFactory.type(String[].class, Integer.TYPE);
        JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertEquals(Integer.TYPE, serializationType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationType_incompatibleTypes() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        AnnotatedMember am = new TestAnnotatedMember();
        JavaType declaredType = TypeFactory.type(String[].class, Integer.TYPE);
        JavaType serializationType = propertyBuilder.findSerializationType(am, true, TypeFactory.type(Integer.TYPE, Integer.TYPE));
        fail("Should not reach this point");
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = new PropertyBuilder(config, beanDesc).getClassAnnotations();
        assertEquals(2, annotations.size());
    }

    @Test
    public void testGetPropertyDefaultValue() {
        Object defaultBean = new PropertyBuilder(config, beanDesc).getDefaultBean();
        Object propertyDefaultValue = new PropertyBuilder(config, beanDesc).getPropertyDefaultValue("foo", null, TypeFactory.type(Foo.class));
        assertNull(propertyDefaultValue);

        Object defaultBeanWithValue = new Foo();
        Object propertyDefaultValueWithValue = new PropertyBuilder(config, beanDesc).getPropertyDefaultValue("foo", null, TypeFactory.type(Foo.class));
        assertEquals(defaultBeanWithValue, propertyDefaultValueWithValue);
    }

    @Test
    public void testGetDefaultValue() {
        assertEquals("", new PropertyBuilder(config, beanDesc).getDefaultValue(TypeFactory.type(String.class)));
        assertEquals(null, new PropertyBuilder(config, beanDesc).getDefaultValue(TypeFactory.type(Object.class)));
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildWriter_emptyContentType() throws JsonMappingException {
        SerializerProvider prov = new TestSerializerProvider();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(prov, null, null, null, null, null, null, true);
        beanPropertyWriter.setContentTypeSerializer(null);
    }

}

class Foo {
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int[] foo;
}

class TestAnnotatedMember {
    @JsonSerialize(using = ObjectSerializer.class)
    private JavaType type;
}

class TestSerializerProvider extends SerializerProvider {
    @Override
    public <T> JsonSerializer<T> findValueSerializer(Class<T> type) {
        return null;
    }
}