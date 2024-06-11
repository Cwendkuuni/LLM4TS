package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BaseNodeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JsonNodeDeserializerTest {

    private JsonNodeFactory nodeFactory = mock(JsonNodeFactory.class);
    private DeserializationContext ctxt = mock(DeserializationContext.class);
    private JsonParser p = mock(JsonParser.class);

    @Test
    public void testGetDeserializer() {
        JsonNode node = new ObjectNode(nodeFactory);
        assertEquals(JsonNodeDeserializer.class, JsonNodeDeserializer.getDeserializer(JsonNode.class));
        assertEquals(ObjectDeserializer.class, JsonNodeDeserializer.getDeserializer(ObjectNode.class));
        assertEquals(ArrayDeserializer.class, JsonNodeDeserializer.getDeserializer(ArrayNode.class));
    }

    @Test
    public void testDeserializeNullValue() throws JsonProcessingException {
        when(p.getCurrentTokenId()).thenReturn(JsonTokenId.ID_NULL.getValue());
        JsonNode result = JsonNodeDeserializer.getInstance().deserialize(p, ctxt);
        assertNull(result);
    }

    @Test
    public void testDeserializeObject() throws JsonProcessingException {
        when(p.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT.getValue());
        ObjectNode expected = new ObjectNode(nodeFactory);
        expected.put("name", "John Doe");
        expected.put("age", 30);

        when(p.nextFieldName()).thenReturn("name");
        when(p.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(p.getText()).thenReturn("John Doe");

        when(p.nextFieldName()).thenReturn("age");
        when(p.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
        when(p.getIntValue()).thenReturn(30);

        JsonNode result = JsonNodeDeserializer.getInstance().deserialize(p, ctxt);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeArray() throws JsonProcessingException {
        when(p.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY.getValue());

        ArrayNode expected = new ArrayNode(nodeFactory);
        expected.add(JsonNodeFactory.numberNode(1));
        expected.add(JsonNodeFactory.numberNode(2));

        when(p.nextToken()).thenReturn(JsonToken.START_OBJECT);
        ObjectNode node = new ObjectNode(nodeFactory);
        node.put("value", JsonNodeFactory.numberNode(1));
        expected.add(node);

        when(p.nextToken()).thenReturn(JsonToken.START_ARRAY);
        ArrayNode arr = new ArrayNode(nodeFactory);
        arr.add(JsonNodeFactory.numberNode(2));
        expected.add(arr);

        when(p.nextToken()).thenReturn(JsonToken.END_ARRAY);

        JsonNode result = JsonNodeDeserializer.getInstance().deserialize(p, ctxt);
        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeAny() throws JsonProcessingException {
        when(p.getCurrentTokenId()).thenReturn(JsonTokenId.ID_STRING.getValue());
        JsonNode result = JsonNodeDeserializer.getInstance().deserialize(p, ctxt, nodeFactory);
        assertEquals(JsonNodeFactory.textNode("foo"), result);

        when(p.getCurrentTokenId()).thenReturn(JsonTokenId.ID_NUMBER_INT.getValue());
        result = JsonNodeDeserializer.getInstance().deserialize(p, ctxt, nodeFactory);
        assertEquals(JsonNodeFactory.numberNode(1), result);

        when(p.getCurrentTokenId()).thenReturn(JsonTokenId.ID_TRUE.getValue());
        result = JsonNodeDeserializer.getInstance().deserialize(p, ctxt, nodeFactory);
        assertEquals(JsonNodeFactory.booleanNode(true), result);

        when(p.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FALSE.getValue());
        result = JsonNodeDeserializer.getInstance().deserialize(p, ctxt, nodeFactory);
        assertEquals(JsonNodeFactory.booleanNode(false), result);

        when(p.getCurrentTokenId()).thenReturn(JsonTokenId.ID_NULL.getValue());
        result = JsonNodeDeserializer.getInstance().deserialize(p, ctxt, nodeFactory);
        assertNull(result);

        when(p.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY.getValue());
        result = JsonNodeDeserializer.getInstance().deserialize(p, ctxt, nodeFactory);
        assertEquals(JsonNodeFactory.arrayNode(), result);

        when(p.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT.getValue());
        result = JsonNodeDeserializer.getInstance().deserialize(p, ctxt, nodeFactory);
        assertEquals(JsonNodeFactory.objectNode(), result);
    }
}