package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

    @Test
    public void testGetDeserializer() throws Exception {
        // Test for ObjectNode
        JsonDeserializer<ObjectNode> objDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
        assertNotNull(objDeserializer);
        assertEquals(ObjectNode.class, objDeserializer.getType());

        // Test for ArrayNode
        JsonDeserializer<ArrayNode> arrayDeserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
        assertNotNull(arrayDeserializer);
        assertEquals(ArrayNode.class, arrayDeserializer.getType());

        // Test for other JsonNode types
        JsonDeserializer<JsonNode> jsonNodeDeserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
        assertNotNull(jsonNodeDeserializer);
        assertEquals(JsonNode.class, jsonNodeDeserializer.getType());
    }

    @Test
    public void testGetNullValue() throws Exception {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.getNullValue(null);
        assertNotNull(result);
        assertTrue(result instanceof NullNode);
    }

    @Test
    public void testDeserializeObject() throws Exception {
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext mockContext = mock(DeserializationContext.class);
        JsonNodeFactory nodeFactory = mock(JsonNodeFactory.class);

        when(mockParser.isExpectedStartObjectToken()).thenReturn(true);
        when(mockParser.nextFieldName()).thenReturn("fieldName");

        ObjectDeserializer objectDeserializer = JsonNodeDeserializer.ObjectDeserializer.getInstance();
        ObjectNode result = (ObjectNode) objectDeserializer.deserialize(mockParser, mockContext, nodeFactory);

        assertEquals("fieldName", result.get("fieldName").asText());
        verify(mockParser).nextToken();
        verify(mockParser).nextFieldName();
    }

    @Test
    public void testDeserializeArray() throws Exception {
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext mockContext = mock(DeserializationContext.class);
        JsonNodeFactory nodeFactory = mock(JsonNodeFactory.class);

        when(mockParser.isExpectedStartArrayToken()).thenReturn(true);
        when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockParser.getText()).thenReturn("element1");
        when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockParser.getText()).thenReturn("element2");
        when(mockParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        ArrayDeserializer arrayDeserializer = JsonNodeDeserializer.ArrayDeserializer.getInstance();
        ArrayNode result = (ArrayNode) arrayDeserializer.deserialize(mockParser, mockContext, nodeFactory);

        assertEquals(2, result.size());
        assertEquals("element1", result.get(0).asText());
        assertEquals("element2", result.get(1).asText());
        verify(mockParser, times(4)).nextToken();
    }

    @Test
    public void testDeserializeAny() throws Exception {
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext mockContext = mock(DeserializationContext.class);
        JsonNodeFactory nodeFactory = mock(JsonNodeFactory.class);

        when(mockParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_STRING.getValue());
        when(mockParser.getText()).thenReturn("stringValue");

        JsonNode result = new JsonNodeDeserializer().deserializeAny(mockParser, mockContext, nodeFactory);

        assertEquals("stringValue", result.asText());
        verify(mockParser).getText();
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeInvalidJson() throws Exception {
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext mockContext = mock(DeserializationContext.class);
        JsonNodeFactory nodeFactory = mock(JsonNodeFactory.class);

        when(mockParser.isExpectedStartObjectToken()).thenReturn(false);

        new JsonNodeDeserializer().deserialize(mockParser, mockContext, nodeFactory);

        fail("Should throw exception");
    }
}