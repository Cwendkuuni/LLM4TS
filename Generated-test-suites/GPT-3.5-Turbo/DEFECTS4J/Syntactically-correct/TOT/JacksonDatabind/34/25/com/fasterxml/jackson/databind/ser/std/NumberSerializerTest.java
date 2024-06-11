package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

public class NumberSerializerTest {

    // Test case for serialize method - BigDecimal value
    @Test
    public void testSerializeWithBigDecimalValue() throws IOException {
        //Arrange
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        BigDecimal value = new BigDecimal("123.456789");
        //Act
        serializer.serialize(value, generator, provider);
        //Assert
        verify(generator).writeNumber(value);
        verifyNoMoreInteractions(generator, provider);
    }

    // Test case for serialize method - BigInteger value
    @Test
    public void testSerializeWithBigIntegerValue() throws IOException {
        //Arrange
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        BigInteger value = new BigInteger("123456789123456789123456789");
        //Act
        serializer.serialize(value, generator, provider);
        //Assert
        verify(generator).writeNumber(value);
        verifyNoMoreInteractions(generator, provider);
    }

    // Test case for serialize method - Integer value
    @Test
    public void testSerializeWithIntegerValue() throws IOException {
        //Arrange
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Integer value = 123;
        //Act
        serializer.serialize(value, generator, provider);
        //Assert
        verify(generator).writeNumber(value.intValue());
        verifyNoMoreInteractions(generator, provider);
    }

    // Test case for serialize method - Null value
    @Test
    public void testSerializeWithNullValue() throws IOException {
        //Arrange
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Number value = null;
        //Act
        serializer.serialize(value, generator, provider);
        //Assert
        verify(generator).writeNumber(null);
        verifyNoMoreInteractions(generator, provider);
    }

    // Test case for getSchema method - Integer type
    @Test
    public void testGetSchemaWithIntegerType() throws IOException {
        //Arrange
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = Integer.class;
        //Act
        JsonNode schema = serializer.getSchema(provider, typeHint);
        //Assert
        assertNotNull(schema);
        assertEquals("integer", schema.get("type").asText());
        verifyNoMoreInteractions(provider);
    }

    // Test case for getSchema method - BigDecimal type
    @Test
    public void testGetSchemaWithBigDecimalType() throws IOException {
        //Arrange
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = BigDecimal.class;
        //Act
        JsonNode schema = serializer.getSchema(provider, typeHint);
        //Assert
        assertNotNull(schema);
        assertEquals("number", schema.get("type").asText());
        verifyNoMoreInteractions(provider);
    }

    // Test case for acceptJsonFormatVisitor method - Integer type
    @Test
    public void testAcceptJsonFormatVisitorWithIntegerType() throws IOException {
        //Arrange
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        //Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        //Assert
        verify(visitor).expectNumberFormat(typeHint);
        verifyNoMoreInteractions(visitor);
    }

    // Test case for acceptJsonFormatVisitor method - BigDecimal type
    @Test
    public void testAcceptJsonFormatVisitorWithBigDecimalType() throws IOException {
        //Arrange
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        //Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        //Assert
        verify(visitor).visitFloatFormat(typeHint, JsonParser.NumberType.BIG_INTEGER);
        verifyNoMoreInteractions(visitor);
    }

}