import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {
  
  @Test
  public void testDeserializeKey_NullKey_ReturnsNull() {
    StdKeyDeserializer deserializer = new StdKeyDeserializer.StdKeyDeserializer(TYPE_STRING, String.class);
    assertNull(deserializer.deserializeKey(null));
  }
  
  @Test
  public void testDeserializeKey_EmptyKey_ReturnsEmptyString() {
    StdKeyDeserializer deserializer = new StdKeyDeserializer.StdKeyDeserializer(TYPE_STRING, String.class);
    assertEquals("", deserializer.deserializeKey(""));
  }
  
  @Test
  public void testDeserializeKey_ValidStringKey_ReturnsKey() {
    StdKeyDeserializer deserializer = new StdKeyDeserializer.StdKeyDeserializer(TYPE_STRING, String.class);
    assertEquals("test", deserializer.deserializeKey("test"));
  }
  
  @Test
  public void testForType_StringClass_ReturnsStringDeserializer() {
    StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
    assertEquals(StdKeyDeserializer.StdKeyDeserializer(TYPE_STRING, String.class), deserializer);
  }
  
  @Test
  public void testForType_BooleanClass_ReturnsBooleanDeserializer() {
    StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Boolean.class);
    assertEquals(StdKeyDeserializer.StdKeyDeserializer(TYPE_BOOLEAN, Boolean.class), deserializer);
  }
  
  @Test
  public void testForType_IntegerClass_ReturnsIntegerDeserializer() {
    StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Integer.class);
    assertEquals(StdKeyDeserializer.StdKeyDeserializer(TYPE_INT, Integer.class), deserializer);
  }
  
  @Test
  public void testForType_LongClass_ReturnsLongDeserializer() {
    StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Long.class);
    assertEquals(StdKeyDeserializer.StdKeyDeserializer(TYPE_LONG, Long.class), deserializer);
  }
  
  @Test
  public void testForType_DateClass_ReturnsDateDeserializer() {
    StdKeyDeserializer deserializer = StdKeyDeserializer.forType(Date.class);
    assertEquals(StdKeyDeserializer.StdKeyDeserializer(TYPE_DATE, Date.class), deserializer);
  }
  
  @Test
  public void testForType_EnumClass_ReturnsEnumDeserializer() {
    StdKeyDeserializer deserializer = StdKeyDeserializer.forType(TestEnum.class);
    assertEquals(StdKeyDeserializer.EnumKD(EnumResolver.constructUnsafe(TestEnum.class, null), null), deserializer);
  }
  
  @Test
  public void testDeserializeKey_InvalidBooleanKey_ThrowsException() {
    StdKeyDeserializer deserializer = new StdKeyDeserializer.StdKeyDeserializer(TYPE_BOOLEAN, Boolean.class);
    assertThrows(IOException.class, () -> deserializer.deserializeKey("123"));
  }
  
  @Test
  public void testDeserializeKey_InvalidIntegerKey_ThrowsException() {
    StdKeyDeserializer deserializer = new StdKeyDeserializer.StdKeyDeserializer(TYPE_INT, Integer.class);
    assertThrows(IOException.class, () -> deserializer.deserializeKey("abc"));
  }
  
  @Test
  public void testDeserializeKey_InvalidLongKey_ThrowsException() {
    StdKeyDeserializer deserializer = new StdKeyDeserializer.StdKeyDeserializer(TYPE_LONG, Long.class);
    assertThrows(IOException.class, () -> deserializer.deserializeKey("abc"));
  }
  
  @Test
  public void testDeserializeKey_InvalidDateKey_ThrowsException() {
    StdKeyDeserializer deserializer = new StdKeyDeserializer.StdKeyDeserializer(TYPE_DATE, Date.class);
    assertThrows(IOException.class, () -> deserializer.deserializeKey("abc"));
  }
  
  enum TestEnum {
    VALUE1, VALUE2, VALUE3
  }
}