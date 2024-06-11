package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@JacksonStdImpl
public class NumberSerializerTest {

    private NumberSerializer numberSerializer;

    @Before
    public void setUp() {
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        // Prepare
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        BeanProperty mockProperty = mock(BeanProperty.class);
        // Create expected JsonSerializer
        JsonSerializer<?> expectedJsonSerializer = this.numberSerializer;

        // Test
        JsonSerializer<?> actualJsonSerializer = numberSerializer.createContextual(mockProvider, mockProperty);

        // Verify
        assertEquals(expectedJsonSerializer, actualJsonSerializer);
    }

    @Test
    public void testSerialize() throws IOException {
        // Prepare
        Number value = 42;
        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        // Expected values
        BigDecimal expectedBigDecimal = new BigDecimal(42);
        BigInteger expectedBigInteger = new BigInteger(String.valueOf(42));
        int expectedIntValue = 42;

        // Test
        numberSerializer.serialize(value, mockJsonGenerator, mockProvider);

        // Verify
        verify(mockJsonGenerator).writeNumber(expectedIntValue);

        // Prepare
        value = new BigDecimal(42);

        // Test
        numberSerializer.serialize(value, mockJsonGenerator, mockProvider);

        // Verify
        verify(mockJsonGenerator).writeNumber(expectedBigDecimal);

        // Prepare
        value = new BigInteger(String.valueOf(42));

        // Test
        numberSerializer.serialize(value, mockJsonGenerator, mockProvider);

        // Verify
        verify(mockJsonGenerator).writeNumber(expectedBigInteger);
    }

    @Test
    public void testGetSchema() {
        // Prepare
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        Type mockTypeHint = mock(Type.class);
        JsonNode expectedSchemaNode = createSchemaNode("integer", true);

        // Test
        JsonNode actualSchemaNode = numberSerializer.getSchema(mockProvider, mockTypeHint);

        // Verify
        assertEquals(expectedSchemaNode, actualSchemaNode);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        // Prepare
        JsonFormatVisitorWrapper mockVisitor = mock(JsonFormatVisitorWrapper.class);
        JavaType mockTypeHint = mock(JavaType.class);
        // Create expected JsonParser.NumberType
        JsonParser.NumberType expectedNumberType = JsonParser.NumberType.BIG_DECIMAL;

        // Test
        numberSerializer.acceptJsonFormatVisitor(mockVisitor, mockTypeHint);

        // Verify
        verify(mockVisitor).visitFloatFormat(mockTypeHint, expectedNumberType);

        // Prepare
        when(mockTypeHint.isTypeOrSubTypeOf(BigDecimal.class)).thenReturn(false);
        // Create expected JsonParser.NumberType
        expectedNumberType = JsonParser.NumberType.UNKNOWN;

        // Test
        numberSerializer.acceptJsonFormatVisitor(mockVisitor, mockTypeHint);

        // Verify
        verify(mockVisitor).expectNumberFormat(mockTypeHint);
    }

    // Helper method to create JsonNode Schema
    private JsonNode createSchemaNode(String format, boolean isNumber) {
        // Create JsonNode Schema
        ObjectNode node = JsonNodeFactory.instance.objectNode();

        node.put("type", "number");
        node.put("format", format);
        node.put("isNumber", isNumber);

        return node;
    }
}