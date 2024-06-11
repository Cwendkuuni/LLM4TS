package com.fasterxml.jackson.databind.jsontype.impl;

import static com.fasterxml.jackson.core.JsonParser.EventType.*;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.JsonParserSequence;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsPropertyTypeDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testDeserializeTypedFromObject() throws Exception {
        JavaType javaType = MAPPER.getTypeFactory().constructType(String.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, null, "type", false, null, As.PROPERTY);

        String json = "{\"type\":\"String\",\"value\":\"hello\"}";
        JsonParser jsonParser = MAPPER.getFactory().createParser(json);
        DeserializationContext context = MAPPER.getDeserializationContext();

        Object result = deserializer.deserializeTypedFromObject(jsonParser, context);
        assertEquals("hello", result);

        // Test with null input
        jsonParser = MAPPER.getFactory().createParser(null);
        result = deserializer.deserializeTypedFromObject(jsonParser, context);
        assertNull(result);

        // Test with incorrect JSON structure
        json = "{\"value\":\"hello\"}";
        jsonParser = MAPPER.getFactory().createParser(json);
        result = deserializer.deserializeTypedFromObject(jsonParser, context);
        assertNull(result);

        json = "{\"type\":\"Integer\",\"value\":42}";
        jsonParser = MAPPER.getFactory().createParser(json);
        TypeDeserializer deserializerForInt = new TypeDeserializer() {
            @Override
            public Object deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsInt();
            }
        };
        TypeIdResolver typeIdResolver = MAPPER.getTypeFactory().constructValueType(int.class);
        TypeDeserializer intDeserializer = new TypeDeserializer() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return deserializerForInt;
            }
        };
        ObjectMapper intMapper = new ObjectMapper();
        ObjectReader intObjectReader = intMapper.readerForType(new TypeReference<Object>() {});
        ObjectReader reader = MAPPER.readerForType(new TypeReference<Object>() {});
        DeserializationContext intContext = intMapper.getDeserializationContext();
        intContext.setActiveTypeIdResolver(typeIdResolver);
        DeserializationContext contextForInt = intMapper.getDeserializationContext();
        contextForInt.setActiveTypeIdResolver(intTypeIdResolver);
        contextForInt.setAnnotationIntrospector(intMapper.getAnnotationIntrospector());
        JsonParser jsonParserForInt = new JsonParser(intObjectReader.readValue(new StringReader("42")));
        Object resultInt = intDeserializer.deserializeTypedFromObject(jsonParserForInt, contextForInt);
        assertEquals(42, resultInt);

        jsonParserForInt = new JsonParser(reader.readValue(new StringReader(json)));
        result = deserializer.deserializeTypedFromObject(jsonParserForInt, context);
        assertNull(result);

        // Test with missing type property
        json = "{\"value\":\"hello\"}";
        jsonParser = MAPPER.getFactory().createParser(json);
        result = deserializer.deserializeTypedFromObject(jsonParser, context);
        assertNull(result);

        // Test with incorrect type property name
        json = "{\"foo\":\"String\",\"value\":\"hello\"}";
        jsonParser = MAPPER.getFactory().createParser(json);
        result = deserializer.deserializeTypedFromObject(jsonParser, context);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        JavaType javaType = MAPPER.getTypeFactory().constructType(String.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, null, "type", false, null, As.PROPERTY);

        String json = "{\"@type\":\"String\",\"value\":\"hello\"}";
        JsonParser jsonParser = MAPPER.getFactory().createParser(json);
        DeserializationContext context = MAPPER.getDeserializationContext();

        Object result = deserializer.deserializeTypedFromObject(jsonParser, context);
        assertEquals("hello", result);
    }

    @Test
    public void testDeserializeUsingDefaultImpl() throws Exception {
        JavaType javaType = MAPPER.getTypeFactory().constructType(String.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, null, "type", false, null, As.PROPERTY);

        String json = "[\"String\"]";
        JsonParser jsonParser = MAPPER.getFactory().createParser(json);
        Object result = deserializer.deserializeTypedUsingDefaultImpl(jsonParser, null, null);
        assertEquals("", result);
    }

    @Test
    public void testDeserializeForId() throws Exception {
        JavaType javaType = MAPPER.getTypeFactory().constructType(String.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, null, "type", false, null, As.PROPERTY);

        String json = "{\"type\":\"String\",\"value\":\"hello\"}";
        JsonParser jsonParser = MAPPER.getFactory().createParser(json);
        TokenBuffer tokenBuffer = new TokenBuffer(jsonParser, null);

        Object result = deserializer._deserializeTypedForId(jsonParser, null, tokenBuffer);
        assertEquals("hello", result);
    }

    @Test
    public void testDeserializeWithNullTokenBuffer() throws Exception {
        JavaType javaType = MAPPER.getTypeFactory().constructType(String.class);
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, null, "type", false, null, As.PROPERTY);

        String json = "{\"type\":\"String\",\"value\":\"hello\"}";
        JsonParser jsonParser = MAPPER.getFactory().createParser(json);
        Object result = deserializer._deserializeTypedForId(jsonParser, null, null);
        assertEquals("hello", result);
    }
}