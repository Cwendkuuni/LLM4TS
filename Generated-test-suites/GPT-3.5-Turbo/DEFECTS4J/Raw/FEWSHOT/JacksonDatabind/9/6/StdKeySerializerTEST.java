package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.lang.reflect.Type;

public class StdKeySerializerTest {
    private StdKeySerializer stdKeySerializer;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private String str;

    @Before
    public void setUp() {
        stdKeySerializer = new StdKeySerializer();
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
    }

    @Test
    public void testSerializeDateKey() throws IOException {
        Date date = new Date();
        stdKeySerializer.serialize(date, jsonGenerator, serializerProvider);
        verify(provider, times(1)).defaultSerializeDateKey(date, jgen);
    }

    @Test
    public void testSerializeToString() throws IOException {
        Object value = new Object();
        stdKeySerializer.serialize(value, jsonGenerator, serializerProvider);
        verify(jsonGenerator, times(1)).writeFieldName(value.toString());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        Type typeHint = new Type();
        JsonNode result = stdKeySerializer.getSchema(serializerProvider, typeHint);
        assertEquals(result, createSchemaNode("string"));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JavaType typeHint = new JavaType();
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        stdKeySerializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visistor, times(1)).expectStringFormat(typeHint);
    }
}