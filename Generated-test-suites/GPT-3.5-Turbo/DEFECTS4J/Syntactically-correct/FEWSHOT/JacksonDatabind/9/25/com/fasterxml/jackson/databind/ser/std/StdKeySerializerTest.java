package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;

import org.junit.Before;
import org.junit.Test;

public class StdKeySerializerTest {
    private StdKeySerializer serializer;
    private JsonGenerator mockGenerator;
    private SerializerProvider mockProvider;
    private JsonFormatVisitorWrapper mockVisitorWrapper;
    private Type mockType;

    @Before
    public void setUp() {
        serializer = new StdKeySerializer();
        mockGenerator = mock(JsonGenerator.class);
        mockProvider = mock(SerializerProvider.class);
        mockVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        mockType = mock(Type.class);
    }

    @Test
    public void testSerialize_DateValue_CallsDefaultSerializeDateKey() throws IOException {
        Date date = new Date();
        serializer.serialize(date, mockGenerator, mockProvider);

        verify(mockProvider).defaultSerializeDateKey(date, mockGenerator);
    }

    @Test
    public void testSerialize_NonDateValue_WritesFieldName() throws IOException {
        String value = "test";
        serializer.serialize(value, mockGenerator, mockProvider);

        verify(mockGenerator).writeFieldName(value);
    }

    @Test
    public void testGetSchema_ReturnsStringSchemaNode() throws JsonMappingException {
        JsonNode schema = serializer.getSchema(mockProvider, mockType);

        assertEquals("string", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_ExpectStringFormat() throws JsonMappingException {
        serializer.acceptJsonFormatVisitor(mockVisitorWrapper, mockType);

        verify(mockVisitorWrapper).expectStringFormat(mockType);
    }
}