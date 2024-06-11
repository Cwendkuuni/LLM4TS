package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import org.junit.Before;
import org.junit.Test;

public class StdKeySerializerTest {
    private StdKeySerializer serializer;
    private JsonGenerator jgen;
    private SerializerProvider provider;

    @Before
    public void setUp() throws Exception {
        serializer = new StdKeySerializer();
        jgen = mock(JsonGenerator.class);
        provider = mock(SerializerProvider.class);
    }

    @Test
    public void testSerialize() throws IOException {
        String key = "key";
        serializer.serialize(key, jgen, provider);
        verify(jgen).writeFieldName(key);
        
        Date date = new Date();
        serializer.serialize(date, jgen, provider);
        verify(provider).defaultSerializeDateKey(eq(date), eq(jgen));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectNode expectedNode = JsonNodeFactory.instance.objectNode();
        expectedNode.put("type", "string");

        JsonNode actualNode = serializer.getSchema(provider, null);
        assertEquals(expectedNode, actualNode);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visitor).expectStringFormat(eq(typeHint));
    }
}