package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class StdKeySerializerTest {

    private StdKeySerializer keySerializer;

    @Mock
    private SerializerProvider provider;
    @Mock
    private JsonGenerator generator;
    @Mock
    private Date date;

    @Before
    public void setUp() {
        keySerializer = new StdKeySerializer();
    }

    @Test
    public void testSerialize_WithDate() throws IOException {
        when(date.toString()).thenReturn("2021-01-01");
        keySerializer.serialize(date, generator, provider);
        verify(provider).defaultSerializeDateKey(date, generator);
    }

    @Test
    public void testSerialize_WithString() throws IOException {
        String value = "test";
        keySerializer.serialize(value, generator, provider);
        verify(generator).writeFieldName(value);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        JsonNode expectedSchema = keySerializer.createSchemaNode("string");
        JsonNode schema = keySerializer.getSchema(provider, null);
        assertEquals(expectedSchema, schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JavaType typeHint = mock(JavaType.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        keySerializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visitor).expectStringFormat(typeHint);
    }
}