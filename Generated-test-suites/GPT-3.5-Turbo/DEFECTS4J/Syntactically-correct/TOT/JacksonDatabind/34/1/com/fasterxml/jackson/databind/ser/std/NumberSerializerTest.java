
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class NumberSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        // Arrange
        NumberSerializer serializer = NumberSerializer.instance;
        BigDecimal number = BigDecimal.valueOf(3.14);
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);

        // Act
        serializer.serialize(number, generator, objectMapper.getSerializerProvider());

        // Assert
        generator.flush();
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        // Arrange
        NumberSerializer serializer = NumberSerializer.instance;
        BigInteger number = BigInteger.TEN;
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);

        // Act
        serializer.serialize(number, generator, objectMapper.getSerializerProvider());

        // Assert
        generator.flush();
    }

    @Test
    public void testSerializeInteger() throws IOException {
        // Arrange
        NumberSerializer serializer = NumberSerializer.instance;
        Integer number = 42;
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);

        // Act
        serializer.serialize(number, generator, objectMapper.getSerializerProvider());

        // Assert
        generator.flush();
    }

    @Test
    public void testSerializeLong() throws IOException {
        // Arrange
        NumberSerializer serializer = NumberSerializer.instance;
        Long number = 1000L;
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
 
        // Act
        serializer.serialize(number, generator, objectMapper.getSerializerProvider());

        // Assert
        generator.flush();
    }

    @Test
    public void testSerializeDouble() throws IOException {
        // Arrange
        NumberSerializer serializer = NumberSerializer.instance;
        Double number = 3.14;
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);

        // Act
        serializer.serialize(number, generator, objectMapper.getSerializerProvider());

        // Assert
        generator.flush();
    }

    @Test
    public void testSerializeFloat() throws IOException {
        // Arrange
        NumberSerializer serializer = NumberSerializer.instance;
        Float number = 1.23f;
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);

        // Act
        serializer.serialize(number, generator, objectMapper.getSerializerProvider());

        // Assert
        generator.flush();
    }

    @Test
    public void testSerializeByte() throws IOException {
        // Arrange
        NumberSerializer serializer = NumberSerializer.instance;
        Byte number = 8;
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);

        // Act
        serializer.serialize(number, generator, objectMapper.getSerializerProvider());

        // Assert
        generator.flush();
    }

    @Test
    public void testSerializeShort() throws IOException {
        // Arrange
        NumberSerializer serializer = NumberSerializer.instance;
        Short number = 16;
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);

        // Act
        serializer.serialize(number, generator, objectMapper.getSerializerProvider());

        // Assert
        generator.flush();
    }

    @Test
    public void testSerializeUnknownNumberType() throws IOException {
        // Arrange
        NumberSerializer serializer = NumberSerializer.instance;
        Number number = new Number() {
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
        };
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);

        // Act
        serializer.serialize(number, generator, objectMapper.getSerializerProvider());

        // Assert
        generator.flush();
    }

    @Test
    public void testGetSchema() {
        // Arrange
        NumberSerializer serializer = NumberSerializer.instance;

        // Act
        JsonNode schemaNode = serializer.getSchema(objectMapper.getSerializerProvider(), null);

        // Assert
        assertNotNull(schemaNode);
        assertEquals("number", schemaNode.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitorForInt() throws JsonMappingException {
        // Arrange
        NumberSerializer serializer = NumberSerializer.instance;
        JsonFormatVisitorWrapper visitor = objectMapper.getSerializerProvider().getVisitorWrapper();
        JavaType javaType = objectMapper.constructType(BigInteger.class);

        // Act
        serializer.acceptJsonFormatVisitor(visitor, javaType);

        // Assert
        assertNotNull(visitor);
    }

    @Test
    public void testAcceptJsonFormatVisitorForFloat() throws JsonMappingException {
        // Arrange
        NumberSerializer serializer = NumberSerializer.instance;
        JsonFormatVisitorWrapper visitor = objectMapper.getSerializerProvider().getVisitorWrapper();
        JavaType javaType = objectMapper.constructType(BigDecimal.class);

        // Act
        serializer.acceptJsonFormatVisitor(visitor, javaType);

        // Assert
        assertNotNull(visitor);
    }

    @Test
    public void testAcceptJsonFormatVisitorForUnknownType() throws JsonMappingException {
        // Arrange
        NumberSerializer serializer = NumberSerializer.instance;
        JsonFormatVisitorWrapper visitor = objectMapper.getSerializerProvider().getVisitorWrapper();
        JavaType javaType = objectMapper.constructType(Number.class);

        // Act
        serializer.acceptJsonFormatVisitor(visitor, javaType);

        // Assert
        assertNotNull(visitor);
    }
}

