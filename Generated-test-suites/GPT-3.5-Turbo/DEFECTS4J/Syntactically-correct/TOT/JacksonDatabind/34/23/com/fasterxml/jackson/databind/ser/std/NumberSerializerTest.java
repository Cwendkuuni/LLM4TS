import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer;
    private SerializerProvider mockProvider;
    private JsonGenerator mockGenerator;
    private JsonFormatVisitorWrapper mockVisitor;
    private JavaType mockType;

    @Before
    public void setup() {
        numberSerializer = new NumberSerializer(Number.class);
        mockProvider = mock(SerializerProvider.class);
        mockGenerator = mock(JsonGenerator.class);
        mockVisitor = mock(JsonFormatVisitorWrapper.class);
        mockType = mock(JavaType.class);
    }
    
    @Test
    public void testSerialize_WhenBigDecimalGiven_ShouldCallWriteNumberWithBigDecimal() throws IOException {
        // Arrange
        BigDecimal value = new BigDecimal(10);
        
        // Act
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        // Assert
        verify(mockGenerator).writeNumber(value);
    }
    
    @Test
    public void testSerialize_WhenBigIntegerGiven_ShouldCallWriteNumberWithBigInteger() throws IOException {
        // Arrange
        BigInteger value = new BigInteger("10");
        
        // Act
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        // Assert
        verify(mockGenerator).writeNumber(value);
    }
    
    @Test
    public void testSerialize_WhenIntegerGiven_ShouldCallWriteNumberWithIntegerValue() throws IOException {
        // Arrange
        Integer value = 10;
        
        // Act
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        // Assert
        verify(mockGenerator).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerialize_WhenLongGiven_ShouldCallWriteNumberWithLongValue() throws IOException {
        // Arrange
        Long value = 10L;
        
        // Act
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        // Assert
        verify(mockGenerator).writeNumber(value.longValue());
    }
    
    @Test
    public void testSerialize_WhenDoubleGiven_ShouldCallWriteNumberWithDoubleValue() throws IOException {
        // Arrange
        Double value = 10.0;
        
        // Act
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        // Assert
        verify(mockGenerator).writeNumber(value.doubleValue());
    }
    
    @Test
    public void testSerialize_WhenFloatGiven_ShouldCallWriteNumberWithFloatValue() throws IOException {
        // Arrange
        Float value = 10.0f;
        
        // Act
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        // Assert
        verify(mockGenerator).writeNumber(value.floatValue());
    }
    
    @Test
    public void testSerialize_WhenByteGiven_ShouldCallWriteNumberWithIntValue() throws IOException {
        // Arrange
        Byte value = 10;
        
        // Act
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        // Assert
        verify(mockGenerator).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerialize_WhenShortGiven_ShouldCallWriteNumberWithIntValue() throws IOException {
        // Arrange
        Short value = 10;
        
        // Act
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        // Assert
        verify(mockGenerator).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerialize_WhenOtherNumberTypeGiven_ShouldCallWriteNumberWithString() throws IOException {
        // Arrange
        Number value = new CustomNumber(10);
        
        // Act
        numberSerializer.serialize(value, mockGenerator, mockProvider);
        
        // Assert
        verify(mockGenerator).writeNumber(value.toString());
    }
    
    @Test
    public void testGetSchema_WhenIsIntTrue_ShouldReturnIntegerSchemaNode() {
        // Arrange
        when(mockType.getRawClass()).thenReturn(BigInteger.class);
        
        // Act
        JsonNode schema = numberSerializer.getSchema(mockProvider, mockType);
        
        // Assert
        assertEquals("integer", schema.asText());
    }
    
    @Test
    public void testGetSchema_WhenIsIntFalse_ShouldReturnNumberSchemaNode() {
        // Arrange
        when(mockType.getRawClass()).thenReturn(BigDecimal.class);
        
        // Act
        JsonNode schema = numberSerializer.getSchema(mockProvider, mockType);
        
        // Assert
        assertEquals("number", schema.asText());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_WhenIsIntTrue_ShouldCallVisitIntFormatWithBigInteger() throws JsonMappingException {
        // Arrange
        when(mockType.getRawClass()).thenReturn(BigInteger.class);
        
        // Act
        numberSerializer.acceptJsonFormatVisitor(mockVisitor, mockType);
        
        // Assert
        verify(mockVisitor).visitIntFormat(mockType, JsonParser.NumberType.BIG_INTEGER);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_WhenIsIntFalseAndHandledTypeIsBigDecimal_ShouldCallVisitFloatFormatWithBigDecimal() throws JsonMappingException {
        // Arrange
        when(mockType.getRawClass()).thenReturn(BigDecimal.class);
        
        // Act
        numberSerializer.acceptJsonFormatVisitor(mockVisitor, mockType);
        
        // Assert
        verify(mockVisitor).visitFloatFormat(mockType, JsonParser.NumberType.BIG_INTEGER);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_WhenIsIntFalseAndHandledTypeIsNotBigDecimal_ShouldCallExpectNumberFormat() throws JsonMappingException {
        // Arrange
        when(mockType.getRawClass()).thenReturn(Double.class);
        
        // Act
        numberSerializer.acceptJsonFormatVisitor(mockVisitor, mockType);
        
        // Assert
        verify(mockVisitor).expectNumberFormat(mockType);
    }
    
    // Additional test cases for typical use cases, edge cases, and error scenarios can be added here
    
}

class CustomNumber extends Number {
    
    private int value;
    
    public CustomNumber(int value) {
        this.value = value;
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }
}