package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import org.junit.Before;
import org.junit.Test;

public class StdKeySerializerTest {

    private StdKeySerializer serializer;
    private JsonGenerator jgen;
    private SerializerProvider provider;

    @Before
    public void setUp() {
        serializer = new StdKeySerializer();
        jgen = mock(JsonGenerator.class);
        provider = mock(SerializerProvider.class);
    }

    @Test
    public void testSerialize_DateValue() throws IOException {
        Date date = new Date();
        serializer.serialize(date, jgen, provider);
        verify(provider).defaultSerializeDateKey(date, jgen);
    }

    @Test
    public void testSerialize_StringValue() throws IOException {
        String str = "test";
        serializer.serialize(str, jgen, provider);
        verify(jgen).writeFieldName(str);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        JsonNode schema = serializer.getSchema(provider, null);
        assertEquals("string", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        serializer.acceptJsonFormatVisitor(visitor, null);
        verify(visitor).expectStringFormat(null);
    }
}