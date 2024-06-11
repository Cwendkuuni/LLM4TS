package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class StdKeySerializerTest {

    private StdKeySerializer keySerializer;

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        keySerializer = new StdKeySerializer();
    }

    @Test
    public void testSerialize_Date() throws IOException {
        Date date = new Date();
        keySerializer.serialize(date, jsonGenerator, serializerProvider);
        verify(serializerProvider).defaultSerializeDateKey(eq(date), any(JsonGenerator.class));
    }

    @Test
    public void testSerialize_NonDate() throws IOException {
        String value = "test";
        keySerializer.serialize(value, jsonGenerator, serializerProvider);
        verify(jsonGenerator).writeFieldName(value);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        JsonNode schemaNode = keySerializer.getSchema(serializerProvider, null);
        assertEquals("string", schemaNode.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JavaType typeHint = mock(JavaType.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        keySerializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visitor).expectStringFormat(typeHint);
    }
}