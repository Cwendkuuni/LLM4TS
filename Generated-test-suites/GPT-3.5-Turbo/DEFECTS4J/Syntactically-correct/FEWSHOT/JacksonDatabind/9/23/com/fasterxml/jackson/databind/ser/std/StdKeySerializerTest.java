package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StdKeySerializerTest {
    @Test
    public void testStdKeySerializer() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        
        // Mocking
        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Date date = new Date();
        
        // Test when the value is a Date
        serializer.serialize(date, jgen, provider);
        ArgumentCaptor<Date> dateCaptor = ArgumentCaptor.forClass(Date.class);
        verify(provider).defaultSerializeDateKey(dateCaptor.capture(), eq(jgen));
        assertEquals(date, dateCaptor.getValue());
        
        // Test when the value is not a Date
        String value = "testValue";
        serializer.serialize(value, jgen, provider);
        verify(jgen).writeFieldName(value);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = String.class;

        JsonNode expectedSchema = JsonNodeFactory.instance.objectNode().put("type", "string");
        
        JsonNode schema = serializer.getSchema(provider, typeHint);
        
        assertEquals(expectedSchema, schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor).expectStringFormat(typeHint);
    }
}