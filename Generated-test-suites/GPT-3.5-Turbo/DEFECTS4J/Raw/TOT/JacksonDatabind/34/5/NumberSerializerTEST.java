
package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {

    @Test
    public void testSerialize_withBigDecimal_writesNumber() throws IOException {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        BigDecimal value = new BigDecimal("10.5");
        
        // Act
        serializer.serialize(value, generator, provider);
        
        // Assert
        verify(generator).writeNumber(value);
    }
    
    @Test
    public void testSerialize_withBigInteger_writesNumber() throws IOException {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        BigInteger value = new BigInteger("10");
        
        // Act
        serializer.serialize(value, generator, provider);
        
        // Assert
        verify(generator).writeNumber(value);
    }
    
    @Test
    public void testSerialize_withInteger_writesNumber() throws IOException {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Integer value = 10;
        
        // Act
        serializer.serialize(value, generator, provider);
        
        // Assert
        verify(generator).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerialize_withLong_writesNumber() throws IOException {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(Long.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Long value = 10L;
        
        // Act
        serializer.serialize(value, generator, provider);
        
        // Assert
        verify(generator).writeNumber(value.longValue());
    }
    
    @Test
    public void testSerialize_withDouble_writesNumber() throws IOException {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(Double.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Double value = 10.5;
        
        // Act
        serializer.serialize(value, generator, provider);
        
        // Assert
        verify(generator).writeNumber(value.doubleValue());
    }
    
    @Test
    public void testSerialize_withFloat_writesNumber() throws IOException {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(Float.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Float value = 10.5f;
        
        // Act
        serializer.serialize(value, generator, provider);
        
        // Assert
        verify(generator).writeNumber(value.floatValue());
    }
    
    @Test
    public void testSerialize_withByte_writesNumber() throws IOException {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Byte value = 10;
        
        // Act
        serializer.serialize(value, generator, provider);
        
        // Assert
        verify(generator).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerialize_withShort_writesNumber() throws IOException {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(Short.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Short value = 10;
        
        // Act
        serializer.serialize(value, generator, provider);
        
        // Assert
        verify(generator).writeNumber(value.intValue());
    }
    
    @Test
    public void testSerialize_withOtherNumber_writesToString() throws IOException {
        // Arrange
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        Number value = mock(Number.class);
        String stringValue = "10.5";
        when(value.toString()).thenReturn(stringValue);
        
        // Act
        serializer.serialize(value, generator, provider);
        
        // Assert
        verify(generator).writeNumber(stringValue);
    }

}
