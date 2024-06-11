package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject_SanityCheck() throws Exception {
        // Arrange
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, "property", true, null);

        // Act
        Object result = deserializer.deserializeTypedFromObject(null, null);

        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObject_WithTypeId() throws Exception {
        // Arrange
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, "property", true, null);
        JsonParser parser = createJsonParser("testDeserializeTypedFromObject_WithTypeId.json");
        DeserializationContext context = createDeserializationContext(parser);
        TokenBuffer tokenBuffer = new TokenBuffer(parser, context);

        // Act
        Object result = deserializer.deserializeTypedFromObject(parser, context);

        // Assert
        assertNotNull(result);
        assertEquals("TestType", result.getClass().getSimpleName());
    }

    @Test
    public void testDeserializeTypedFromObject_TypedObjectIdVisible() throws Exception {
        // Arrange
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, "property", true, null);
        JsonParser parser = createJsonParser("testDeserializeTypedFromObject_TypedObjectIdVisible.json");
        DeserializationContext context = createDeserializationContext(parser);
        TokenBuffer tokenBuffer = new TokenBuffer(parser, context);

        // Act
        Object result = deserializer.deserializeTypedFromObject(parser, context);

        // Assert
        assertNotNull(result);
        assertEquals("TestType", result.getClass().getSimpleName());
    }

    @Test
    public void testDeserializeTypedFromObject_CustomHandlingWithDefaultImpl() throws Exception {
        // Arrange
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, "property", true, null);
        JsonParser parser = createJsonParser("testDeserializeTypedFromObject_CustomHandlingWithDefaultImpl.json");
        DeserializationContext context = createDeserializationContext(parser);
        TokenBuffer tokenBuffer = new TokenBuffer(parser, context);

        // Act
        Object result = deserializer.deserializeTypedFromObject(parser, context);

        // Assert
        assertEquals(TestType.class, result.getClass());
    }

    @Test
    public void testDeserializeTypedFromObject_WithNaturalValue() throws Exception {
        // Arrange
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, "property", true, null);
        JsonParser parser = createJsonParser("testDeserializeTypedFromObject_WithNaturalValue.json");
        DeserializationContext context = createDeserializationContext(parser);
        TokenBuffer tokenBuffer = new TokenBuffer(parser, context);

        // Act
        Object result = deserializer.deserializeTypedFromObject(parser, context);

        // Assert
        assertEquals("TestType", result);
    }

    private JsonParser createJsonParser(String jsonFileName) throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        return jsonFactory.createParser(getClass().getResource(jsonFileName));
    }

    private DeserializationContext createDeserializationContext(JsonParser parser) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new TestTypeModule());
        JsonDeserializer<?> deserializer = mapper.findDeserializer(TestType.class);
        return mapper.createDeserializationContext(parser, null);
    }
    
    class TestType {
    	
    }
    
    class TestTypeModule extends SimpleModule {
    	
    }

}