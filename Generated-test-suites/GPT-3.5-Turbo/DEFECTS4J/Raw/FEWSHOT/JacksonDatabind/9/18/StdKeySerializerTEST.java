package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class StdKeySerializerTest {

    private StdKeySerializer serializer;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;

    @Before
    public void setup() throws IOException {
        serializer = new StdKeySerializer();
        jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        serializerProvider = mock(SerializerProvider.class);
    }

    @Test
    public void testSerialize_DateType() throws IOException {
        Date date = new Date();
        ArgumentCaptor<Date> argumentCaptor = ArgumentCaptor.forClass(Date.class);

        serializer.serialize(date, jsonGenerator, serializerProvider);

        verify(serializerProvider).defaultSerializeDateKey(argumentCaptor.capture(), ArgumentCaptor.any(JsonGenerator.class).capture());
        assertEquals(date, argumentCaptor.getValue());
    }

    @Test
    public void testSerialize_StringType() throws IOException {
        String value = "test";

        serializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeFieldName(value);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        Type typeHint = String.class;

        JsonNode schemaNode = serializer.getSchema(serializerProvider, typeHint);

        assertNotNull(schemaNode);
        assertEquals("string", schemaNode.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JavaType typeHint = mock(JavaType.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor).expectStringFormat(typeHint);
    }
}