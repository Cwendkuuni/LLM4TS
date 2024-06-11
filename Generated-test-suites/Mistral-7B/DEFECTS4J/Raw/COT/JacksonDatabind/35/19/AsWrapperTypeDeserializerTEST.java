package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Test;

public class AsWrapperTypeDeserializerTest {

    private ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    // Test constructor
    @Test
    public void testAsWrapperTypeDeserializerConstructor() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(javaType, null, null, false, String.class);
        assertNotNull(asWrapperTypeDeserializer);
    }

    // Test getTypeInclusion method
    @Test
    public void testgetTypeInclusion() throws Exception {
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(null, null, null, false, String.class);
        assertEquals(As.WRAPPER_OBJECT, asWrapperTypeDeserializer.getTypeInclusion());
    }

    // Test deserializeTypedFromObject method
    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(javaType, null, null, false, String.class);

        String jsonString = "{\"type\":\"java.lang.String\",\"value\":\"Hello World\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);

        Object result = asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, objectMapper.getDeserializationContext());

        assertEquals("Hello World", result);
    }

    // Test deserializeTypedFromObject with null typeId
    @Test(expected = IOException.class)
    public void testDeserializeTypedFromObjectWithNullTypeId() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(javaType, null, null, false, String.class);

        String jsonString = "{\"value\":\"Hello World\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);

        asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, objectMapper.getDeserializationContext());
    }

    // Test deserializeTypedFromObject with invalid type id
    @Test(expected = JsonMappingException.class)
    public void testDeserializeTypedFromObjectWithInvalidTypeId() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(javaType, null, null, false, String.class);

        String jsonString = "{\"type\":\"InvalidType\",\"value\":\"Hello World\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);

        asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, objectMapper.getDeserializationContext());
    }

    // Test deserializeTypedFromArray method
    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructType(int[].class);
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(javaType, null, null, false, int[].class);

        String jsonString = "[{\"type\":\"java.lang.Integer\",\"value\":1}]";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);

        Object result = asWrapperTypeDeserializer.deserializeTypedFromArray(jsonParser, objectMapper.getDeserializationContext());

        assertArrayEquals(new int[]{1}, (int[]) result);
    }

    // Test deserializeTypedFromArray with null typeId
    @Test(expected = IOException.class)
    public void testDeserializeTypedFromArrayWithNullTypeId() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructType(int[].class);
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(javaType, null, null, false, int[].class);

        String jsonString = "[{\"value\":1}]";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);

        asWrapperTypeDeserializer.deserializeTypedFromArray(jsonParser, objectMapper.getDeserializationContext());
    }

    // Test deserializeTypedFromScalar method
    @Test
    public void testDeserializeTypedFromScalar() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructType(Integer.class);
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(javaType, null, null, false, Integer.class);

        String jsonString = "{\"type\":\"java.lang.Integer\",\"value\":1}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);

        Object result = asWrapperTypeDeserializer.deserializeTypedFromScalar(jsonParser, objectMapper.getDeserializationContext());

        assertEquals(1, result);
    }

    // Test deserializeTypedFromScalar with null typeId
    @Test(expected = IOException.class)
    public void testDeserializeTypedFromScalarWithNullTypeId() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructType(Integer.class);
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(javaType, null, null, false, Integer.class);

        String jsonString = "{\"value\":1}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);

        asWrapperTypeDeserializer.deserializeTypedFromScalar(jsonParser, objectMapper.getDeserializationContext());
    }

    // Test deserializeTypedFromAny method
    @Test
    public void testDeserializeTypedFromAny() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructType(Double.class);
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(javaType, null, null, false, Double.class);

        String jsonString = "{\"type\":\"java.lang.Double\",\"value\":3.14}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);

        Object result = asWrapperTypeDeserializer.deserializeTypedFromAny(jsonParser, objectMapper.getDeserializationContext());

        assertEquals(3.14, result);
    }

    // Test deserializeWithNativeTypeId method
    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(javaType, null, null, false, String.class);

        String jsonString = "{\"@type\":\"java.lang.String\",\"value\":\"Hello World\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);

        Object result = asWrapperTypeDeserializer._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext(), null);

        assertEquals("Hello World", result);
    }

    // Test deserializeWithNativeTypeId with null typeId
    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdWithNullTypeId() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(javaType, null, null, false, String.class);

        String jsonString = "{\"value\":\"Hello World\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);

        asWrapperTypeDeserializer._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext(), null);
    }

    // Test findDeserializer method
    @Test
    public void testFindDeserializer() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(javaType, null, null, false, String.class);

        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerDeserializer(String.class, stringDeserializer);

        String typeId = "stringDeserializer";
        Object result = asWrapperTypeDeserializer._findDeserializer(objectMapper.getDeserializationContext(), typeId);

        assertEquals(stringDeserializer, result);
    }

    // Test findDeserializer method with null typeId
    @Test
    public void testFindDeserializerWithNullTypeId() throws Exception {
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(javaType, null, null, false, String.class);

        String typeId = null;
        Object result = asWrapperTypeDeserializer._findDeserializer(objectMapper.getDeserializationContext(), typeId);

        assertNull(result);
    }
}