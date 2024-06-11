package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer.ToStringSerializer;

public class NumberSerializerTest {

    @Test
    public void testCreateContextual() {
        // Test when format is STRING
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        BeanProperty mockProperty = mock(BeanProperty.class);
        JsonFormat.Value mockFormat = new JsonFormat.Value();
        mockFormat.setShape(JsonFormat.Shape.STRING);
        when(mockProvider.findFormatOverrides(any(), any(), any())).thenReturn(mockFormat);

        NumberSerializer numberSerializer = new NumberSerializer(Integer.class);
        ContextualSerializer contextualSerializer = numberSerializer.createContextual(mockProvider, mockProperty);

        assertTrue(contextualSerializer instanceof ToStringSerializer);
    }

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);

        // Test for BigDecimal instance
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        numberSerializer.serialize(new BigDecimal("10.5"), mockJsonGenerator, mockProvider);
        verify(mockJsonGenerator).writeNumber(new BigDecimal("10.5"));

        // Test for Float instance
        numberSerializer.serialize(new Float(5.5), mockJsonGenerator, mockProvider);
        verify(mockJsonGenerator).writeNumber(5.5f);

        // Test for invalid type (should fallback)
        numberSerializer.serialize(123, mockJsonGenerator, mockProvider);
        verify(mockJsonGenerator).writeNumber("123");
    }

    @Test
    public void testGetSchema() {
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        Type typeHint = BigDecimal.class;

        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        JsonNode schema = numberSerializer.getSchema(mockProvider, typeHint);

        assertEquals("number", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper mockVisitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);
        numberSerializer.acceptJsonFormatVisitor(mockVisitor, typeHint);

        verify(mockVisitor).visitFloatFormat(typeHint, JsonParser.NumberType.BIG_DECIMAL);
    }

    // Test for potential edge cases and exception handling scenarios
    // To be added based on further analysis of the source code

}