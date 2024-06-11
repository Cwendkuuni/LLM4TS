package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.test.BaseTest;

@RunWith(BaseTest.class)
public class StdKeySerializerTest {

    // Test for serialize method with null value
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullValue() throws JsonProcessingException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator mockJsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);

        serializer.serialize(null, mockJsonGenerator, mockProvider);
    }

    // Test for serialize method with Date value
    @Test
    public void testSerializeDateValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();
        Date date = new Date();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
        SerializerProvider serializerProvider = mapper.getSerializerProvider();

        serializer.serialize(date, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String actualOutput = jsonGenerator.getOutputFactory().getValueAsString();
        String expectedOutput = mapper.getSerializerProvider().defaultSerializeDateKey(date, jsonGenerator).toString();

        assertEquals(expectedOutput, actualOutput);
    }

    // Test for serialize method with String value
    @Test
    public void testSerializeStringValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdKeySerializer serializer = new StdKeySerializer();
        String value = "testValue";
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
        SerializerProvider serializerProvider = mapper.getSerializerProvider();

        serializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String actualOutput = jsonGenerator.getOutputFactory().getValueAsString();
        String expectedOutput = value;

        assertEquals(expectedOutput, actualOutput);
    }

    // Test for getSchema method
    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        TypeFactory typeFactory = Mockito.mock(TypeFactory.class);
        JsonFormatVisitorWrapper mockJsonFormatVisitorWrapper = Mockito.mock(JsonFormatVisitorWrapper.class);

        when(mockProvider.getTypeFactory()).thenReturn(typeFactory);

        JsonNode actualOutput = serializer.getSchema(mockProvider, Object.class);
        String expectedOutput = "string";

        assertEquals(expectedOutput, actualOutput.asText());
    }

    // Test for acceptJsonFormatVisitor method
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, typeHint);

        verify(jsonFormatVisitorWrapper).expectStringFormat(typeHint);
    }
}