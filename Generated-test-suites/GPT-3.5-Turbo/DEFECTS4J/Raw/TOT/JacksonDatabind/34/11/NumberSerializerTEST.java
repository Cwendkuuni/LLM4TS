package com.fasterxml.jackson.databind.ser.std;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

@RunWith(MockitoJUnitRunner.class)
public class NumberSerializerTest {

    @Mock
    private JsonGenerator jsonGeneratorMock;
    
    @Mock
    private SerializerProvider serializerProviderMock;
    
    @Mock
    private JsonFormatVisitorWrapper visitorWrapperMock;
    
    @Mock
    private JavaType javaTypeMock;
    
    private NumberSerializer numberSerializer;
    
    @Test
    public void testSerialize_withBigDecimal() throws Exception {
        // Arrange
        Number value = new BigDecimal(10.5);

        // Act
        numberSerializer.serialize(value, jsonGeneratorMock, serializerProviderMock);

        // Assert
        Mockito.verify(jsonGeneratorMock).writeNumber((BigDecimal) value);
    }
    
    @Test
    public void testSerialize_withBigInteger() throws Exception {
        // Arrange
        Number value = new BigInteger("1234567890");

        // Act
        numberSerializer.serialize(value, jsonGeneratorMock, serializerProviderMock);

        // Assert
        Mockito.verify(jsonGeneratorMock).writeNumber((BigInteger) value);
    }
    
    @Test
    public void testSerialize_withInteger() throws Exception {
        // Arrange
        Number value = new Integer(10);

        // Act
        numberSerializer.serialize(value, jsonGeneratorMock, serializerProviderMock);

        // Assert
        Mockito.verify(jsonGeneratorMock).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerialize_withLong() throws Exception {
        // Arrange
        Number value = new Long(10);

        // Act
        numberSerializer.serialize(value, jsonGeneratorMock, serializerProviderMock);

        // Assert
        Mockito.verify(jsonGeneratorMock).writeNumber(value.longValue());
    }
    
    @Test
    public void testSerialize_withDouble() throws Exception {
        // Arrange
        Number value = new Double(10.5);

        // Act
        numberSerializer.serialize(value, jsonGeneratorMock, serializerProviderMock);

        // Assert
        Mockito.verify(jsonGeneratorMock).writeNumber(value.doubleValue());
    }
    
    @Test
    public void testSerialize_withFloat() throws Exception {
        // Arrange
        Number value = new Float(10.5);

        // Act
        numberSerializer.serialize(value, jsonGeneratorMock, serializerProviderMock);

        // Assert
        Mockito.verify(jsonGeneratorMock).writeNumber(value.floatValue());
    }
    
    @Test
    public void testSerialize_withByte() throws Exception {
        // Arrange
        Number value = new Byte((byte) 10);

        // Act
        numberSerializer.serialize(value, jsonGeneratorMock, serializerProviderMock);

        // Assert
        Mockito.verify(jsonGeneratorMock).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerialize_withShort() throws Exception {
        // Arrange
        Number value = new Short((short) 10);

        // Act
        numberSerializer.serialize(value, jsonGeneratorMock, serializerProviderMock);

        // Assert
        Mockito.verify(jsonGeneratorMock).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerialize_withOtherNumberType() throws Exception {
        // Arrange
        Number value = new TestNumber(); // Custom number class

        // Act
        numberSerializer.serialize(value, jsonGeneratorMock, serializerProviderMock);

        // Assert
        Mockito.verify(jsonGeneratorMock).writeNumber(value.toString());
    }
    
    @Test
    public void testGetSchema_withIntType() {
        // Arrange
        numberSerializer = new NumberSerializer(Integer.class);

        // Act
        numberSerializer.getSchema(serializerProviderMock, javaTypeMock);

        // Assert
        // The schema node should be created with "integer" type
        Mockito.verify(serializerProviderMock).createSchemaNode("integer", true);
    }
    
    @Test
    public void testGetSchema_withBigIntegerType() {
        // Arrange
        numberSerializer = new NumberSerializer(BigInteger.class);

        // Act
        numberSerializer.getSchema(serializerProviderMock, javaTypeMock);

        // Assert
        // The schema node should be created with "integer" type
        Mockito.verify(serializerProviderMock).createSchemaNode("integer", true);
    }
    
    @Test
    public void testGetSchema_withBigDecimalType() {
        // Arrange
        numberSerializer = new NumberSerializer(BigDecimal.class);

        // Act
        numberSerializer.getSchema(serializerProviderMock, javaTypeMock);

        // Assert
        // The schema node should be created with "number" type
        Mockito.verify(serializerProviderMock).createSchemaNode("number", true);
    }
    
    @Test
    public void testGetSchema_withOtherNumberType() {
        // Arrange
        numberSerializer = new NumberSerializer(TestNumber.class);

        // Act
        numberSerializer.getSchema(serializerProviderMock, javaTypeMock);

        // Assert
        // The schema node should be created with "number" type
        Mockito.verify(serializerProviderMock).createSchemaNode("number", true);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_withIntType() throws Exception {
        // Arrange
        numberSerializer = new NumberSerializer(Integer.class);

        // Act
        numberSerializer.acceptJsonFormatVisitor(visitorWrapperMock, javaTypeMock);

        // Assert
        Mockito.verify(visitorWrapperMock).expectIntegerFormat(javaTypeMock);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_withBigIntegerType() throws Exception {
        // Arrange
        numberSerializer = new NumberSerializer(BigInteger.class);

        // Act
        numberSerializer.acceptJsonFormatVisitor(visitorWrapperMock, javaTypeMock);

        // Assert
        Mockito.verify(visitorWrapperMock).expectIntegerFormat(javaTypeMock);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_withBigDecimalType() throws Exception {
        // Arrange
        numberSerializer = new NumberSerializer(BigDecimal.class);

        // Act
        numberSerializer.acceptJsonFormatVisitor(visitorWrapperMock, javaTypeMock);

        // Assert
        Mockito.verify(visitorWrapperMock).expectFloatFormat(javaTypeMock);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_withOtherNumberType() throws Exception {
        // Arrange
        numberSerializer = new NumberSerializer(TestNumber.class);

        // Act
        numberSerializer.acceptJsonFormatVisitor(visitorWrapperMock, javaTypeMock);

        // Assert
        // Expecting fallback to "untyped" number write method
        Mockito.verify(visitorWrapperMock).expectNumberFormat(javaTypeMock);
    }

    // Custom number class for testing other number types
    private class TestNumber extends Number {
        private static final long serialVersionUID = 1L;

        @Override
        public double doubleValue() {
            return 0;
        }

        @Override
        public float floatValue() {
            return 0;
        }

        @Override
        public int intValue() {
            return 0;
        }

        @Override
        public long longValue() {
            return 0;
        }
    }
}