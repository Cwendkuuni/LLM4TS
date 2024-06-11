import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

public class NumberSerializerTest {
  
  private NumberSerializer numberSerializer;
  private JsonGenerator jsonGenerator;
  private SerializerProvider serializerProvider;
  
  @Before
  public void setUp() {
    numberSerializer = new NumberSerializer(Number.class);
    jsonGenerator = mock(JsonGenerator.class);
    serializerProvider = mock(SerializerProvider.class);
  }
  
  @Test
  public void testSerialize_WhenValueIsBigDecimal() throws IOException {
    // Arrange
    BigDecimal value = new BigDecimal("1234567890.1234567890");
    
    // Act
    numberSerializer.serialize(value, jsonGenerator, serializerProvider);
    
    // Assert
    verify(jsonGenerator).writeNumber(value);
  }
  
  @Test
  public void testSerialize_WhenValueIsBigInteger() throws IOException {
    // Arrange
    BigInteger value = new BigInteger("12345678901234567890");
    
    // Act
    numberSerializer.serialize(value, jsonGenerator, serializerProvider);
    
    // Assert
    verify(jsonGenerator).writeNumber(value);
  }
  
  @Test
  public void testSerialize_WhenValueIsLong() throws IOException {
    // Arrange
    Long value = 1234567890L;
    
    // Act
    numberSerializer.serialize(value, jsonGenerator, serializerProvider);
    
    // Assert
    verify(jsonGenerator).writeNumber(value);
  }
  
  @Test
  public void testSerialize_WhenValueIsDouble() throws IOException {
    // Arrange
    Double value = 12345.6789;
    
    // Act
    numberSerializer.serialize(value, jsonGenerator, serializerProvider);
    
    // Assert
    verify(jsonGenerator).writeNumber(value);
  }
  
  @Test
  public void testSerialize_WhenValueIsFloat() throws IOException {
    // Arrange
    Float value = 123.456f;
    
    // Act
    numberSerializer.serialize(value, jsonGenerator, serializerProvider);
    
    // Assert
    verify(jsonGenerator).writeNumber(value);
  }
  
  @Test
  public void testSerialize_WhenValueIsInteger() throws IOException {
    // Arrange
    Integer value = 123456;
    
    // Act
    numberSerializer.serialize(value, jsonGenerator, serializerProvider);
    
    // Assert
    verify(jsonGenerator).writeNumber(value);
  }
  
  @Test
  public void testSerialize_WhenValueIsByte() throws IOException {
    // Arrange
    Byte value = 123;
    
    // Act
    numberSerializer.serialize(value, jsonGenerator, serializerProvider);
    
    // Assert
    verify(jsonGenerator).writeNumber(value.intValue());
  }
  
  @Test
  public void testSerialize_WhenValueIsShort() throws IOException {
    // Arrange
    Short value = 1234;
    
    // Act
    numberSerializer.serialize(value, jsonGenerator, serializerProvider);
    
    // Assert
    verify(jsonGenerator).writeNumber(value.intValue());
  }
  
  @Test
  public void testSerialize_WhenValueIsNotNumeric() throws IOException {
    // Arrange
    String value = "abc";
    
    // Act
    numberSerializer.serialize(value, jsonGenerator, serializerProvider);
    
    // Assert
    verify(jsonGenerator).writeNumber(value);
  }
  
  @Test
  public void testGetSchema_WhenIsInt() {
    // Assert
    assertEquals("integer", numberSerializer.getSchema(serializerProvider, null).asText());
  }
  
  @Test
  public void testGetSchema_WhenIsNotInt() {
    // Arrange
    numberSerializer = new NumberSerializer(BigDecimal.class);
    
    // Assert
    assertEquals("number", numberSerializer.getSchema(serializerProvider, null).asText());
  }
  
}