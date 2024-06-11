package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.As;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import com.fasterxml.jackson.databind.jsontype.impl.TypeResolverBuilderBase;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {

    private ObjectMapper mapper;
    private AsWrapperTypeDeserializer deserializer;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        deserializer = new AsWrapperTypeDeserializer(null, null, null, true, null);
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        // Given
        JsonParser jp = new JsonFactory().createParser("{\"key\":\"value\"}");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        // When
        Object obj = deserializer.deserializeTypedFromObject(jp, ctxt);

        // Then
        assertNotNull(obj);
        assertTrue(obj instanceof String);
        assertEquals("value", obj);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeTypedFromObjectWithInvalidToken() throws IOException {
        // Given
        JsonParser jp = new JsonFactory().createParser("value");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        // When
        deserializer.deserializeTypedFromObject(jp, ctxt);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeTypedFromObjectWithNullType() throws IOException {
        // Given
        JsonParser jp = new JsonFactory().createParser("{\"key\":null}");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        // When
        deserializer.deserializeTypedFromObject(jp, ctxt);
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        // Given
        JsonParser jp = new JsonFactory().createParser("[\"value\"]");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        // When
        Object obj = deserializer.deserializeTypedFromArray(jp, ctxt);

        // Then
        assertNotNull(obj);
        assertTrue(obj instanceof String);
        assertEquals("value", obj);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeTypedFromArrayWithInvalidToken() throws IOException {
        // Given
        JsonParser jp = new JsonFactory().createParser("value");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        // When
        deserializer.deserializeTypedFromArray(jp, ctxt);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeTypedFromArrayWithNullType() throws IOException {
        // Given
        JsonParser jp = new JsonFactory().createParser("[null]");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        // When
        deserializer.deserializeTypedFromArray(jp, ctxt);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        // Given
        JsonParser jp = new JsonFactory().createParser("\"value\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        // When
        Object obj = deserializer.deserializeTypedFromScalar(jp, ctxt);

        // Then
        assertNotNull(obj);
        assertTrue(obj instanceof String);
        assertEquals("value", obj);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeTypedFromScalarWithInvalidToken() throws IOException {
        // Given
        JsonParser jp = new JsonFactory().createParser("{");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        // When
        deserializer.deserializeTypedFromScalar(jp, ctxt);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        // Given
        JsonParser jp = new JsonFactory().createParser("\"value\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        // When
        Object obj = deserializer.deserializeTypedFromAny(jp, ctxt);

        // Then
        assertNotNull(obj);
        assertTrue(obj instanceof String);
        assertEquals("value", obj);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeTypedFromAnyWithInvalidToken() throws IOException {
        // Given
        JsonParser jp = new JsonFactory().createParser("{");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        // When
        deserializer.deserializeTypedFromAny(jp, ctxt);
    }

}