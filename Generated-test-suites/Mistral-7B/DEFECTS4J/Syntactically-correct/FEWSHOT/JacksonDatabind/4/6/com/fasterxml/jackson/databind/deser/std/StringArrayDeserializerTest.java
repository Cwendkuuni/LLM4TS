package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import com.fasterxml.jackson.databind.util.JsonTestUtils;

/**
 * Test cases for StringArrayDeserializer class.
 */
public class StringArrayDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testEmptyArrayDeserialization() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonParser jp = MAPPER.getFactory().createParser("[ ]");

        String[] result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[0], result);
        ctxt.returnObjectBuffer(jp.getParserConfig());
    }

    @Test
    public void testStringArrayDeserialization() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonParser jp = MAPPER.getFactory().createParser("[\"foo\", \"bar\"]");

        String[] result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[]{"foo", "bar"}, result);
        ctxt.returnObjectBuffer(jp.getParserConfig());
    }

    @Test
    public void testNullElementDeserialization() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonParser jp = MAPPER.getFactory().createParser("[]");

        String[] result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[0], result);
        ctxt.returnObjectBuffer(jp.getParserConfig());

        jp = MAPPER.getFactory().createParser("[null]");

        result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[]{null}, result);
        ctxt.returnObjectBuffer(jp.getParserConfig());
    }

    @Test
    public void testCustomElementDeserialization() throws IOException {
        JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
        when(elementDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("customValue");
        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);

        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonParser jp = MAPPER.getFactory().createParser("[\"foo\", " + elementDeserializer.getClass().getName() + "()]");

        String[] result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[]{"foo", "customValue"}, result);
        ctxt.returnObjectBuffer(jp.getParserConfig());
    }

    @Test
    public void testCustomDeserializationWithType() throws IOException {
        JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
        when(elementDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("customValue");
        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);

        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeTypedFromArray(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn(new String[]{"foo", "customValue"});

        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonParser jp = MAPPER.getFactory().createParser("[\"foo\", " + TypeDeserializer.class.getName() + "()]");

        Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        assertArrayEquals(new String[]{"foo", "customValue"}, (String[]) result);
        ctxt.returnObjectBuffer(jp.getParserConfig());
    }

    @Test
    public void testSingleValueDeserialization() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonParser jp = MAPPER.getFactory().createParser("\"foo\"");

        String[] result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[]{"foo"}, result);
        ctxt.returnObjectBuffer(jp.getParserConfig());
    }

    @Test(expected = JsonMappingException.class)
    public void testNonArrayDeserialization() throws IOException {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonParser jp = MAPPER.getFactory().createParser("123");

        deserializer.deserialize(jp, ctxt);
        ctxt.returnObjectBuffer(jp.getParserConfig());
    }

    @Test
    public void testContextualDeserialization() throws IOException {
        JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
        when(elementDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("customValue");
        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);

        DeserializationContext ctxt = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        when(property.getType()).thenReturn(new TypeReference<String[]>() {}.getType());

        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);
        assertTrue(result instanceof StringArrayDeserializer);
        assertEquals(deserializer, result);

        elementDeserializer = mock(JsonDeserializer.class);
        when(elementDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("customValue");

        deserializer = new StringArrayDeserializer(elementDeserializer);
        when(ctxt.findContextualValueDeserializer(eq(String[].class), any(BeanProperty.class))).thenReturn(elementDeserializer);

        result = deserializer.createContextual(ctxt, property);
        assertTrue(result instanceof StringArrayDeserializer);
        assertNotEquals(deserializer, result);

        result = deserializer.createContextual(ctxt, property);
        assertTrue(result instanceof StringArrayDeserializer);
        assertNotEquals(deserializer, result);

        when(ctxt.handleSecondaryContextualization(eq(elementDeserializer), any(BeanProperty.class))).thenReturn(deserializer);

        result = deserializer.createContextual(ctxt, property);
        assertTrue(result instanceof StringArrayDeserializer);
        assertEquals(deserializer, result);
    }
}