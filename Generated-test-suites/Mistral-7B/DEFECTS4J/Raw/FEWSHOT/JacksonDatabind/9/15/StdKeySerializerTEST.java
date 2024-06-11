package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNodeReader;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StdKeySerializerTest {

    private SerializerProvider serializerProvider;
    private JsonGenerator jsonGenerator;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        serializerProvider = mock(SerializerProvider.class);
        jsonGenerator = mock(JsonGenerator.class);
        objectMapper = new ObjectMapper();
        when(serializerProvider.getJsonGenerator()).thenReturn(jsonGenerator);
    }

    @Test
    public void testSerializeDate() throws JsonProcessingException {
        Date date = new Date();
        StdKeySerializer stdKeySerializer = new StdKeySerializer();
        stdKeySerializer.serialize(date, jsonGenerator, serializerProvider);
        // Verify that defaultSerializeDateKey method was called
        when(jsonGenerator.writeFieldName((String) anyString())).thenCallRealMethod();
        stdKeySerializer.serialize(date, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeFieldName(date.toString());
    }

    @Test
    public void testSerializeString() throws JsonProcessingException {
        StdKeySerializer stdKeySerializer = new StdKeySerializer();
        String value = "test";
        stdKeySerializer.serialize(value, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeFieldName(value);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer stdKeySerializer = new StdKeySerializer();
        JavaType typeHint = mock(JavaType.class);
        JsonNode schemaNode = stdKeySerializer.getSchema(serializerProvider, typeHint);
        assertEquals("string", schemaNode.toString());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdKeySerializer stdKeySerializer = new StdKeySerializer();
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        stdKeySerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, typeHint);
        verify(jsonFormatVisitorWrapper).expectStringFormat(typeHint);
    }

    @Test
    public void testSerializeNull() throws JsonProcessingException {
        StdKeySerializer stdKeySerializer = new StdKeySerializer();
        stdKeySerializer.serialize(null, jsonGenerator, serializerProvider);
        verify(jsonGenerator, never()).writeFieldName("");
    }
}