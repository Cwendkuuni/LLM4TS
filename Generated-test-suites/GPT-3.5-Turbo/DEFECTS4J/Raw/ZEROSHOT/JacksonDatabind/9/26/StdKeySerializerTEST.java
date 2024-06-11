
package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class StdKeySerializerTest {

    private StdKeySerializer stdKeySerializer;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private ByteArrayOutputStream outputStream;
    private String expected;

    @Before
    public void setup() throws IOException {
        stdKeySerializer = new StdKeySerializer();
        outputStream = new ByteArrayOutputStream();
        jsonGenerator = new JsonFactory().createGenerator(outputStream);
        serializerProvider = mock(SerializerProvider.class);
        expected = "";
    }
    
    @Test
    public void testSerialize_WhenValueIsDate_ShouldSerializeDateKey() throws IOException {
        Date date = new Date();
        stdKeySerializer.serialize(date, jsonGenerator, serializerProvider);
        jsonGenerator.flush();
        assertEquals(expected, outputStream.toString());
    }
    
    @Test
    public void testSerialize_WhenValueIsNotDate_ShouldSerializeToStringValue() throws IOException {
        Object value = "test";
        expected = "test";
        stdKeySerializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();
        assertEquals(expected, outputStream.toString());
    }
    
    @Test
    public void testGetSchema_ShouldReturnStringSchemaNode() throws JsonMappingException {
        Type typeHint = null;
        assertEquals("string", stdKeySerializer.getSchema(serializerProvider, typeHint).asText());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_ShouldExpectStringFormat() throws JsonMappingException {
        JavaType typeHint = null;
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        stdKeySerializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visitor).expectStringFormat(typeHint);
    }
}
