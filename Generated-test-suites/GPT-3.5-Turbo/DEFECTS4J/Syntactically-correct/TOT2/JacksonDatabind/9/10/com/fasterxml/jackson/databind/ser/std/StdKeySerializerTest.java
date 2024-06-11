package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class StdKeySerializerTest {

    private StdKeySerializer keySerializer;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;

    @Before
    public void setUp() {
        keySerializer = new StdKeySerializer();
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
    }

    @Test
    public void testSerializeWithString() throws IOException {
        String value = "test";
        
        keySerializer.serialize(value, jsonGenerator, serializerProvider);
        
        verify(jsonGenerator).writeFieldName(value);
    }

    @Test
    public void testSerializeWithDate() throws IOException {
        Date value = new Date();
        
        keySerializer.serialize(value, jsonGenerator, serializerProvider);
        
        verify(serializerProvider).defaultSerializeDateKey(value, jsonGenerator);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        JsonNode schema = keySerializer.getSchema(serializerProvider, null);
        
        assertEquals("string", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JavaType typeHint = mock(JavaType.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        
        keySerializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor).expectStringFormat(typeHint);
    }
}