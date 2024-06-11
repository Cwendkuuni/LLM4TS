package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsPropertyTypeDeserializerTest {

    private static final AsPropertyTypeDeserializer defaultDeserializer = new AsPropertyTypeDeserializer(
            javaType, typeIdResolver, "typePropertyName", false, null, As.PROPERTY);
    
    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"typePropertyName\":\"String\", \"data\":\"test\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        assertNotNull(defaultDeserializer.deserializeTypedFromObject(parser, context));
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"@class\":\"java.lang.String\", \"data\":\"test\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        assertNotNull(defaultDeserializer.deserializeTypedFromObject(parser, context));
    }

    @Test
    public void testDeserializeUsingDefaultImpl() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"test\"]";
        JsonParser parser = mapper.getFactory().createParser(json);
        assertNotNull(defaultDeserializer.deserializeTypedUsingDefaultImpl(parser, context, null));
    }

    @Test
    public void testDeserializeUsingDefaultImplWithTokenBuffer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"test\"]";
        JsonParser parser = mapper.getFactory().createParser(json);
        TokenBuffer tokenBuffer = new TokenBuffer(parser, context);
        assertNotNull(defaultDeserializer._deserializeTypedUsingDefaultImpl(parser, context, tokenBuffer));
    }

    @Test
    public void testDeserializeTypedUsingDefaultImplWithNullTokenBuffer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"test\"]";
        JsonParser parser = mapper.getFactory().createParser(json);
        assertNotNull(defaultDeserializer._deserializeTypedUsingDefaultImpl(parser, context, null));
    }

    @Test
    public void testDeserializeTypedForIdWithNullTokenBuffer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"typePropertyName\":\"String\", \"data\":\"test\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        assertNull(defaultDeserializer._deserializeTypedForId(parser, context, null));
    }

    @Test
    public void testDeserializeTypedForId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"typePropertyName\":\"String\", \"data\":\"test\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        TokenBuffer tokenBuffer = new TokenBuffer(parser, context);
        assertNotNull(defaultDeserializer._deserializeTypedForId(parser, context, tokenBuffer));
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonArray = "[\"test\"]";
        String jsonObject = "{\"typePropertyName\":\"String\", \"data\":\"test\"}";
        JsonParser parserArray = mapper.getFactory().createParser(jsonArray);
        JsonParser parserObject = mapper.getFactory().createParser(jsonObject);
        assertNotNull(defaultDeserializer.deserializeTypedFromAny(parserArray, context));
        assertNotNull(defaultDeserializer.deserializeTypedFromAny(parserObject, context));
    }

    @Test(expected = JsonProcessingException.class)
    public void testReportWrongTokenException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"data\":\"test\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        defaultDeserializer.deserializeTypedFromObject(parser, context);
    }

    @Test
    public void testDeserializeWithNativeTypeId_Null() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"@class\":null, \"data\":\"test\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        assertNull(defaultDeserializer.deserializeTypedFromObject(parser, context));
    }

    @Test
    public void testDeserializeWithNativeTypeId_Empty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"@class\":\"\", \"data\":\"test\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        assertNull(defaultDeserializer.deserializeTypedFromObject(parser, context));
    }

    @Test
    public void testDeserializeWithNativeTypeId_UnknownType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"@class\":\"java.lang.UnknownType\", \"data\":\"test\"}";
        JsonParser parser = mapper.getFactory().createParser(json);
        assertNull(defaultDeserializer.deserializeTypedFromObject(parser, context));
    }

    @Test
    public void testFindDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> deserializer = mapper.findValueDeserializer(String.class);
        assertEquals(StringDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<List<String>> deserializer = mapper.findValueDeserializer(List.class);
        assertEquals(ListDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testDeserializeIfNatural() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonBoolean = "true";
        String jsonInt = "42";
        String jsonDouble = "3.14";
        String jsonString = "test";
        JsonParser parserBoolean = mapper.getFactory().createParser(jsonBoolean);
        JsonParser parserInt = mapper.getFactory().createParser(jsonInt);
        JsonParser parserDouble = mapper.getFactory().createParser(jsonDouble);
        JsonParser parserString = mapper.getFactory().createParser(jsonString);
        Object resultBoolean = defaultDeserializer.deserializeTypedFromAny(parserBoolean, context);
        Object resultInt = defaultDeserializer.deserializeTypedFromAny(parserInt, context);
        Object resultDouble = defaultDeserializer.deserializeTypedFromAny(parserDouble, context);
        Object resultString = defaultDeserializer.deserializeTypedFromAny(parserString, context);
        assertEquals(Boolean.TRUE, resultBoolean);
        assertEquals(Integer.valueOf(42), resultInt);
        assertEquals(Double.valueOf(3.14), resultDouble);
        assertEquals("test", resultString);
    }

    private static JavaType javaType = reflector.types.constructType(List.class, String.class);
    private static TypeIdResolver typeIdResolver = new StandardTypeIdResolver();
    private static DeserializationContext context = new DeserializationContext(new ObjectMapper());
    private static ObjectMapper mapper = new ObjectMapper();
    private static Reflector reflector = new Reflector();
}