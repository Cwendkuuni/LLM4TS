package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import org.junit.Before;
import org.junit.Test;

public class StdKeySerializerTest {

    private StdKeySerializer stdKeySerializer;

    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;

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
        
        verify(serializerProvider).defaultSerializeDateKey(date, jsonGenerator);
    }

    @Test
    public void testSerializeOtherKey() throws IOException {
        Object value = new Object();
        String str = value.toString();

        stdKeySerializer.serialize(value, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeFieldName(str);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        Type typeHint = Object.class;

        JsonNode expectedResult = createSchemaNode("string");
        JsonNode actualResult = stdKeySerializer.getSchema(serializerProvider, typeHint);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JavaType typeHint = mock(JavaType.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);

        stdKeySerializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor).expectStringFormat(typeHint);
    }

    private JsonNode createSchemaNode(String type) {
        // implementation of createSchemaNode method
        return null;
    }
}