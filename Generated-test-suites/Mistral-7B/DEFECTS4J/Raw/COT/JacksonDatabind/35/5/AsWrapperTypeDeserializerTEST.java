package com.example.junit.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.JsonParserSequence;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Test;

public class AsWrapperTypeDeserializerTest {

    private ObjectMapper objectMapper;

    public AsWrapperTypeDeserializerTest() {
        this.objectMapper = new ObjectMapper();
    }

    // Test for constructor
    @Test
    public void testAsWrapperTypeDeserializerConstructor() {
        JavaType baseType = objectMapper.getTypeFactory().constructType(String.class);
        TypeIdResolver idResolver = objectMapper.getTypeFactory().resolveValueType(String.class);
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(baseType, idResolver, "typePropertyName", true, String.class);
        assertNotNull(asWrapperTypeDeserializer);
    }

    // Test for getTypeInclusion method
    @Test
    public void testGetTypeInclusion() {
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        assertEquals(As.WRAPPER_OBJECT, asWrapperTypeDeserializer.getTypeInclusion());
    }

    // Test for deserializeTypedFromObject method
    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        JavaType baseType = objectMapper.getTypeFactory().constructType(String.class);
        TypeIdResolver idResolver = objectMapper.getTypeFactory().resolveValueType(String.class);
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(baseType, idResolver, "typePropertyName", true, String.class);

        String jsonString = "{\"type\":\"String\",\"value\":\"Hello World\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);

        Object result = asWrapperTypeDeserializer.deserializeTypedFromObject(jsonParser, objectMapper.getDeserializationContext());

        assertEquals("Hello World", result);
    }

    // Test for deserializeTypedFromArray method
    @Test
    public void testDeserializeTypedFromArray() throws Exception {
        JavaType baseType = objectMapper.getTypeFactory().constructType(String[].class);
        TypeIdResolver idResolver = objectMapper.getTypeFactory().resolveValueType(String[].class);
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(baseType, idResolver, "typePropertyName", true, String[].class);

        String jsonString = "[{\"type\":\"String\",\"value\":\"Hello World\"},{\"type\":\"String\",\"value\":\"Test\"}]";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);

        Object result = asWrapperTypeDeserializer.deserializeTypedFromArray(jsonParser, objectMapper.getDeserializationContext());

        assertArrayEquals(new String[]{"Hello World", "Test"}, (String[]) result);
    }

    // Test for deserializeTypedFromScalar method
    @Test
    public void testDeserializeTypedFromScalar() throws Exception {
        JavaType baseType = objectMapper.getTypeFactory().constructType(Integer.class);
        TypeIdResolver idResolver = objectMapper.getTypeFactory().resolveValueType(Integer.class);
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(baseType, idResolver, "typePropertyName", true, Integer.class);

        String jsonString = "{\"type\":\"Integer\",\"value\":42}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);

        Object result = asWrapperTypeDeserializer.deserializeTypedFromScalar(jsonParser, objectMapper.getDeserializationContext());

        assertEquals(42, result);
    }

    // Test for deserializeTypedFromAny method
    @Test
    public void testDeserializeTypedFromAny() throws Exception {
        JavaType baseType = objectMapper.getTypeFactory().constructType(Double.class);
        TypeIdResolver idResolver = objectMapper.getTypeFactory().resolveValueType(Double.class);
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(baseType, idResolver, "typePropertyName", true, Double.class);

        String jsonString = "{\"type\":\"Double\",\"value\":3.14}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);

        Object result = asWrapperTypeDeserializer.deserializeTypedFromAny(jsonParser, objectMapper.getDeserializationContext());

        assertEquals(3.14, result);
    }

    // Test for deserializeWithNativeTypeId method with valid type id
    @Test
    public void testDeserializeWithNativeTypeIdValidTypeId() throws Exception {
        JavaType baseType = objectMapper.getTypeFactory().constructType(String.class);
        TypeIdResolver idResolver = objectMapper.getTypeFactory().resolveValueType(String.class);
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(baseType, idResolver, "typePropertyName", true, String.class);

        String jsonString = "{\"type\":\"String\",\"value\":\"Hello World\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);

        Object result = asWrapperTypeDeserializer._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext(), "String");

        assertEquals("Hello World", result);
    }

    // Test for deserializeWithNativeTypeId method with invalid type id
    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdInvalidTypeId() throws Exception {
        JavaType baseType = objectMapper.getTypeFactory().constructType(String.class);
        TypeIdResolver idResolver = objectMapper.getTypeFactory().resolveValueType(String.class);
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(baseType, idResolver, "typePropertyName", true, String.class);

        String jsonString = "{\"type\":\"InvalidType\",\"value\":\"Hello World\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);

        asWrapperTypeDeserializer._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext(), "InvalidType");
    }

    // Test for _findDeserializer method with null arguments
    @Test(expected = IllegalArgumentException.class)
    public void testFindDeserializerNullArguments() {
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        asWrapperTypeDeserializer._findDeserializer(null, null);
    }

    // Test for _findDeserializer method with non-null arguments
    @Test
    public void testFindDeserializerNonNullArguments() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Integer.class, new IntDeserializer());
        objectMapper.registerModule(module);

        JavaType baseType = objectMapper.getTypeFactory().constructType(Integer.class);
        TypeIdResolver idResolver = objectMapper.getTypeFactory().resolveValueType(Integer.class);
        AsWrapperTypeDeserializer asWrapperTypeDeserializer = new AsWrapperTypeDeserializer(baseType, idResolver, "typePropertyName", true, Integer.class);

        Object result = asWrapperTypeDeserializer._findDeserializer(objectMapper.getDeserializationContext(), "Integer").deserialize(null, objectMapper.getDeserializationContext());

        assertEquals(0, result);
    }
}