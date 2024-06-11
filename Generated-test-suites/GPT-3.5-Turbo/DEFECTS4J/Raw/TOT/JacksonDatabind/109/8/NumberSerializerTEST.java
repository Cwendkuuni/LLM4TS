
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer;

    @Before
    public void setUp() {
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        // Arrange
        SerializerProvider prov = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);

        // Act
        JsonSerializer<?> serializer = numberSerializer.createContextual(prov, property);

        // Assert
        assertEquals(numberSerializer, serializer);
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        // Arrange
        Number value = new BigDecimal("123.45");
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        numberSerializer.serialize(value, g, provider);

        // Assert
        verify(g).writeNumber(value);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        // Arrange
        Number value = new BigInteger("12345");
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        numberSerializer.serialize(value, g, provider);

        // Assert
        verify(g).writeNumber(value);
    }

    @Test
    public void testSerializeLong() throws IOException {
        // Arrange
        Number value = 12345L;
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        numberSerializer.serialize(value, g, provider);

        // Assert
        verify(g).writeNumber(value.longValue());
    }

    @Test
    public void testSerializeDouble() throws IOException {
        // Arrange
        Number value = 123.45;
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        numberSerializer.serialize(value, g, provider);

        // Assert
        verify(g).writeNumber(value.doubleValue());
    }

    @Test
    public void testSerializeFloat() throws IOException {
        // Arrange
        Number value = 123.45f;
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        numberSerializer.serialize(value, g, provider);

        // Assert
        verify(g).writeNumber(value.floatValue());
    }

    @Test
    public void testSerializeInteger() throws IOException {
        // Arrange
        Number value = 123;
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        numberSerializer.serialize(value, g, provider);

        // Assert
        verify(g).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeByte() throws IOException {
        // Arrange
        Number value = (byte) 123;
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        numberSerializer.serialize(value, g, provider);

        // Assert
        verify(g).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeShort() throws IOException {
        // Arrange
        Number value = (short) 123;
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        numberSerializer.serialize(value, g, provider);

        // Assert
        verify(g).writeNumber(value.intValue());
    }

    @Test
    public void testSerializeOtherTypes() throws IOException {
        // Arrange
        Number value = 123.45;
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        // Act
        numberSerializer.serialize(value, g, provider);

        // Assert
        verify(g).writeNumber(value.toString());
    }

    @Test
    public void testGetSchema() {
        // Arrange
        SerializerProvider provider = mock(SerializerProvider.class);
        JavaType typeHint = mock(JavaType.class);

        // Act
        JsonNode schema = numberSerializer.getSchema(provider, typeHint);

        // Assert
        assertEquals("number", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        // Act
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        verify(visitor).expectNumberFormat(typeHint);
    }

}
