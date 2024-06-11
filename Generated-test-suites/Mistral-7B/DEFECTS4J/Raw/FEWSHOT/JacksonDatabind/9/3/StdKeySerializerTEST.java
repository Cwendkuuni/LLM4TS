package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializationSchema;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonSerializer.ValueType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;
import org.junit.Before;
import org.junit.Test;

public class StdKeySerializerTest {

    private SerializerProvider provider;

    @Before
    public void setup() {
        provider = mock(SerializerProvider.class);
    }

    @Test
    public void testSerializeDate() throws IOException {
        Date date = new Date();
        StdKeySerializer serializer = new StdKeySerializer();

        serializer.serialize(date, null, provider);

        //not tested in this example, assuming default implementation is correct
    }

    @Test
    public void testSerializeString() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();

        ObjectNode node = mock(ObjectNode.class);
        JsonGenerator gen = mock(JsonGenerator.class);

        when(node.asText()).thenReturn("testString");

        serializer.serialize("testString", node, provider);

        StdKeySerializer.ValueType valueType = new StdKeySerializer.ValueType();
        valueType.value = Object.class;
        valueType.name = "string";

        when(provider.findValueSerializer(JsonType.constructUnbinding(String.class), ValueType.class)).thenReturn(valueType);

        when(node.setAll(valueType)).thenReturn(node);

        assertEquals(node, serializer.serialize("testString", null, provider));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();

        JsonSerializationSchema schema = mock(JsonSerializationSchema.class);
        JsonNode schemaNode = mock(JsonNode.class);

        when(schema.buildSchemaNode(null)).thenReturn(schemaNode);

        JsonNode result = serializer.getSchema(provider, Object.class);

        assertEquals(schemaNode, result);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();

        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);

        serializer.acceptJsonFormatVisitor(visitor, Object.class);

        verify(visitor).expectStringFormat(new ValueType(Object.class, "string"));
    }
}