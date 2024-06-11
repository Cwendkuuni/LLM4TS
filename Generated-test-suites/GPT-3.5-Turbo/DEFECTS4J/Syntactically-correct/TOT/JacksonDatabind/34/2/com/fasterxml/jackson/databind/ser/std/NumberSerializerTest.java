
package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class NumberSerializerTest {

    @Test
    public void testSerializeBigDecimal() throws IOException {
        // Arrange
        BigDecimal input = new BigDecimal("10.5");
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        NumberSerializer serializer = new NumberSerializer(Number.class);

        // Act
        serializer.serialize(input, g, provider);

        // Assert
        verify(g, times(1)).writeNumber(input);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        // Arrange
        BigInteger input = new BigInteger("100");
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        NumberSerializer serializer = new NumberSerializer(Number.class);

        // Act
        serializer.serialize(input, g, provider);

        // Assert
        verify(g, times(1)).writeNumber(input);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        // Arrange
        Integer input = 5;
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        NumberSerializer serializer = new NumberSerializer(Number.class);

        // Act
        serializer.serialize(input, g, provider);

        // Assert
        verify(g, times(1)).writeNumber(input);
    }

    @Test
    public void testSerializeLong() throws IOException {
        // Arrange
        Long input = 500L;
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        NumberSerializer serializer = new NumberSerializer(Number.class);

        // Act
        serializer.serialize(input, g, provider);

        // Assert
        verify(g, times(1)).writeNumber(input);
    }

    @Test
    public void testSerializeDouble() throws IOException {
        // Arrange
        Double input = 5.5;
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        NumberSerializer serializer = new NumberSerializer(Number.class);

        // Act
        serializer.serialize(input, g, provider);

        // Assert
        verify(g, times(1)).writeNumber(input);
    }

    @Test
    public void testSerializeFloat() throws IOException {
        // Arrange
        Float input = 5.5f;
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        NumberSerializer serializer = new NumberSerializer(Number.class);

        // Act
        serializer.serialize(input, g, provider);

        // Assert
        verify(g, times(1)).writeNumber(input);
    }

    @Test
    public void testSerializeByte() throws IOException {
        // Arrange
        Byte input = 10;
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        NumberSerializer serializer = new NumberSerializer(Number.class);

        // Act
        serializer.serialize(input, g, provider);

        // Assert
        verify(g, times(1)).writeNumber(input.intValue());
    }

    @Test
    public void testSerializeShort() throws IOException {
        // Arrange
        Short input = 100;
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        NumberSerializer serializer = new NumberSerializer(Number.class);

        // Act
        serializer.serialize(input, g, provider);

        // Assert
        verify(g, times(1)).writeNumber(input.intValue());
    }

    @Test
    public void testSerializeFallback() throws IOException {
        // Arrange
        Number input = new Number() {
            @Override
            public int intValue() {
                return 0;
            }

            @Override
            public long longValue() {
                return 0;
            }

            @Override
            public float floatValue() {
                return 0;
            }

            @Override
            public double doubleValue() {
                return 0;
            }

            @Override
            public String toString() {
                return "fallback";
            }
        };
        JsonGenerator g = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        NumberSerializer serializer = new NumberSerializer(Number.class);

        // Act
        serializer.serialize(input, g, provider);

        // Assert
        verify(g, times(1)).writeNumber(input.toString());
    }

    @Test
    public void testGetSchema() {
        // Arrange
        SerializerProvider provider = mock(SerializerProvider.class);
        NumberSerializer serializer = new NumberSerializer(Number.class);

        // Act
        JsonNode schema = serializer.getSchema(provider, null);

        // Assert
        // Schema validation
    }

    @Test
    public void testAcceptJsonFormatVisitorForInt() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);

        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        verify(visitor, times(1)).visitIntFormat(eq(JsonParser.NumberType.BIG_INTEGER));
    }

    @Test
    public void testAcceptJsonFormatVisitorForBigDecimal() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);

        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        verify(visitor, times(1)).visitFloatFormat(eq(JsonParser.NumberType.BIG_INTEGER));
    }

    @Test
    public void testAcceptJsonFormatVisitorForOtherType() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        NumberSerializer serializer = new NumberSerializer(Number.class);

        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        // Check other assertions
    }
}
