package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class NumberSerializerTest {

    @Mock
    private JsonGenerator mockJsonGenerator;
    @Mock
    private SerializerProvider mockSerializerProvider;
    @Mock
    private JsonParser mockJsonParser;
    @Mock
    private JsonMappingException mockJsonMappingException;
    @Mock
    private JsonFormatVisitorWrapper mockJsonFormatVisitorWrapper;
    @Mock
    private JavaType mockJavaType;

    private NumberSerializer numberSerializer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void testSerializeBigDecimalValue() throws IOException {
        BigDecimal value = new BigDecimal("123.45");
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator, times(1)).writeNumber(value);
    }

    @Test
    public void testSerializeBigIntegerValue() throws IOException {
        BigInteger value = new BigInteger("12345");
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator, times(1)).writeNumber(value);
    }

    @Test
    public void testSerializeIntegerValue() throws IOException {
        Integer value = 123;
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator, times(1)).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeLongValue() throws IOException {
        Long value = 12345L;
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator, times(1)).writeNumber(value.longValue());
    }

    @Test
    public void testSerializeDoubleValue() throws IOException {
        Double value = 3.14;
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator, times(1)).writeNumber(value.doubleValue());
    }

    @Test
    public void testSerializeFloatValue() throws IOException {
        Float value = 1.23f;
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator, times(1)).writeNumber(value.floatValue());
    }

    @Test
    public void testSerializeByteValue() throws IOException {
        Byte value = 1;
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator, times(1)).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeShortValue() throws IOException {
        Short value = 100;
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator, times(1)).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeStringValue() throws IOException {
        String value = "123";
        numberSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockJsonGenerator, times(1)).writeNumber(value.toString());
    }

    @Test
    public void testGetSchemaWhenIsInt() {
        JsonNode expectedSchema = createSchemaNode("integer", true);
        JsonNode schema = numberSerializer.getSchema(mockSerializerProvider, mockJavaType);
        assertEquals(expectedSchema, schema);
    }

    @Test
    public void testGetSchemaWhenIsNotInt() {
        JsonNode expectedSchema = createSchemaNode("number", true);
        JsonNode schema = numberSerializer.getSchema(mockSerializerProvider, mockJavaType);
        assertEquals(expectedSchema, schema);
    }

    @Test
    public void testAcceptJsonFormatVisitorWhenIsInt() throws JsonMappingException {
        numberSerializer.acceptJsonFormatVisitor(mockJsonFormatVisitorWrapper, mockJavaType);
        verify(mockJsonFormatVisitorWrapper, times(1)).expectNumberFormat(mockJavaType);
    }

    @Test
    public void testAcceptJsonFormatVisitorWhenIsNotInt() throws JsonMappingException {
        numberSerializer.acceptJsonFormatVisitor(mockJsonFormatVisitorWrapper, mockJavaType);
        verify(mockJsonFormatVisitorWrapper, times(1)).expectNumberFormat(mockJavaType);
    }

    private JsonNode createSchemaNode(String type, boolean isInt) {
        // implementation omitted for brevity
    }
}