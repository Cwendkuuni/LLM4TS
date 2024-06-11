import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {
  
  @Test
  public void testDeserializeKey_WhenKeyIsNull_ShouldReturnNull() {
    StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CLASS, String.class);
    DeserializationContext ctxt = new DeserializationContext(null, null);
    
    Object result = deserializer.deserializeKey(null, ctxt);
    
    assertNull(result);
  }
  
  @Test
  public void testDeserializeKey_WhenKeyIsTrueAndTypeIsBoolean_ShouldReturnBooleanTrue() {
    StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
    DeserializationContext ctxt = new DeserializationContext(null, null);
    
    Object result = deserializer.deserializeKey("true", ctxt);
    
    assertEquals(Boolean.TRUE, result);
  }
  
  @Test
  public void testDeserializeKey_WhenKeyIsFalseAndTypeIsBoolean_ShouldReturnBooleanFalse() {
    StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
    DeserializationContext ctxt = new DeserializationContext(null, null);
    
    Object result = deserializer.deserializeKey("false", ctxt);
    
    assertEquals(Boolean.FALSE, result);
  }
  
  @Test(expected = WeirdKeyException.class)
  public void testDeserializeKey_WhenKeyIsNotTrueOrFalseAndTypeIsBoolean_ShouldThrowWeirdKeyException() {
    StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
    DeserializationContext ctxt = new DeserializationContext(null, null);
    
    deserializer.deserializeKey("invalid", ctxt);
  }
  
  @Test
  public void testDeserializeKey_WhenKeyIsStringAndTypeIsString_ShouldReturnSameString() {
    StdKeyDeserializer deserializer = new StdKeyDeserializer.StringKD(String.class);
    DeserializationContext ctxt = new DeserializationContext(null, null);
    String expected = "test";
    
    Object result = deserializer.deserializeKey(expected, ctxt);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testDeserializeKey_WhenKeyIsNegativeIntegerAndTypeIsByte_ShouldReturnByte() {
    StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
    DeserializationContext ctxt = new DeserializationContext(null, null);
    byte expected = -123;
    
    Object result = deserializer.deserializeKey("-123", ctxt);
    
    assertEquals(expected, result);
  }
  
  @Test(expected = WeirdKeyException.class)
  public void testDeserializeKey_WhenKeyIsOutOfRangeAndTypeIsByte_ShouldThrowWeirdKeyException() {
    StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
    DeserializationContext ctxt = new DeserializationContext(null, null);
    
    deserializer.deserializeKey("256", ctxt);
  }
  
  @Test
  public void testDeserializeKey_WhenKeyIsStringAndTypeIsChar_ShouldReturnChar() {
    StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class);
    DeserializationContext ctxt = new DeserializationContext(null, null);
    char expected = 'c';
    
    Object result = deserializer.deserializeKey("c", ctxt);
    
    assertEquals(expected, result);
  }
  
  // ... More test cases for the other methods ...
  
}