package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testBuildWriter() throws JsonMappingException, JsonProcessingException {
        ObjectNode node = JsonFactory.instance.createObjectNode();
        node.put("name", "John Doe");
        node.put("age", 30);
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Node.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        ObjectWriter writer = mapper.writerWithRootValue(node);

        JsonSerializer<Object> defaultSerializer = new StdSerializer<Object>(Object.class) {};
        JavaType declaredType = mapper.getTypeFactory().constructType(Object.class);
        TypeSerializer typeSerializer = TypeSerializer.empty;
        AnnotatedMember am = null;
        boolean defaultUseStaticTyping = false;

        BeanPropertyWriter actual = propertyBuilder.buildWriter(writer.asSerializerProvider(), null, declaredType, defaultSerializer, typeSerializer, null, am, defaultUseStaticTyping);

        assertNotNull(actual);
    }

    @Test
    public void testFindSerializationType() {
        ObjectNode node = JsonFactory.instance.createObjectNode();
        node.put("name", "John Doe");
        node.put("age", 30);
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Node.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        JavaType declaredType = mapper.getTypeFactory().constructType(String.class);
        boolean useStaticTyping = false;

        // Test with no annotations
        JavaType actual = propertyBuilder.findSerializationType(am, useStaticTyping, declaredType);
        assertNull(actual);

        // Test with annotation
        JsonSerialize annotation = Mockito.mock(JsonSerialize.class);
        when(am.getAnnotation(JsonSerialize.class)).thenReturn(annotation);

        actual = propertyBuilder.findSerializationType(am, useStaticTyping, declaredType);
        assertEquals(String.class, actual.getRawClass());
    }

    @Test
    public void testGetDefaultBean() {
        ObjectNode node = JsonFactory.instance.createObjectNode();
        node.put("name", "John Doe");
        node.put("age", 30);
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Node.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);

        propertyBuilder = new PropertyBuilder(config, beanDesc) {{
            _defaultBean = new Node();
        }};

        defaultBean = propertyBuilder.getDefaultBean();
        assertNotNull(defaultBean);
    }

    @Test
    public void testgetPropertyDefaultValue() throws Exception {
        ObjectNode node = JsonFactory.instance.createObjectNode();
        node.put("name", "John Doe");
        node.put("age", 30);
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        BeanDescription beanDesc = mapper.getTypeFactory().constructType(Node.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        JavaType type = mapper.getTypeFactory().constructType(Integer.class);

        Object defaultValue = propertyBuilder.getPropertyDefaultValue("age", am, type);
        assertEquals(0, defaultValue);

        type = mapper.getTypeFactory().constructType(String.class);

        defaultValue = propertyBuilder.getPropertyDefaultValue("name", am, type);
        assertEquals("", defaultValue);

        type = mapper.getTypeFactory().constructType(Node.class);

        defaultValue = propertyBuilder.getPropertyDefaultValue("name", am, type);
        assertNull(defaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        Object defaultValue = propertyBuilder.getDefaultValue(null);
        assertNull(defaultValue);

        JavaType type = TypeFactory.defaultInstance.constructType(Integer.class);

        defaultValue = propertyBuilder.getDefaultValue(type);
        assertEquals(0, defaultValue);

        type = TypeFactory.defaultInstance.constructType(String.class);

        defaultValue = propertyBuilder.getDefaultValue(type);
        assertEquals("", defaultValue);

        type = TypeFactory.defaultInstance.constructType(Node.class);

        defaultValue = propertyBuilder.getDefaultValue(type);
        assertNull(defaultValue);
    }
}