package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jgen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Date date = new Date();

        serializer.serialize(date, jgen, provider);

        Mockito.verify(provider).defaultSerializeDateKey(date, jgen);
    }

    @Test
    public void testSerializeToString() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jgen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Object value = "test";

        serializer.serialize(value, jgen, provider);

        Mockito.verify(jgen).writeFieldName("test");
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        assertEquals("string", serializer.getSchema(provider, null).asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        Mockito.verify(visitor).expectStringFormat(typeHint);
    }

    @Test(expected = IOException.class)
    public void testSerializeException() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jgen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Object value = new Object();

        serializer.serialize(value, jgen, provider);
    }
}