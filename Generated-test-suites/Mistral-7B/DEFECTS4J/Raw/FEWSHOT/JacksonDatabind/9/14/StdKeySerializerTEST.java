package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;

public class StdKeySerializerTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testSerializeDate() throws IOException, JsonProcessingException {
        StdKeySerializer serializer = new StdKeySerializer();
        Date date = new Date();
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(date, generator, provider);

        verify(generator).writeFieldName(date.toString());
    }

    @Test
    public void testSerializeString() throws IOException, JsonProcessingException {
        StdKeySerializer serializer = new StdKeySerializer();
        String str = "test";
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(str, generator, provider);

        verify(generator).writeFieldName(str);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonNode schema = serializer.getSchema(mock(SerializerProvider.class), mock(Type.class));

        assertEquals("string", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor).expectStringFormat(typeHint);
    }

    @Test
    public void testSerializeNull() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        serializer.serialize(null, generator, provider);

        // null values are skipped in JSON
        verify(generator, never()).writeFieldName(null);
    }
}