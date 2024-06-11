import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.Field;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class StdKeyDeserializerTest {

  private ObjectMapper objectMapper;
  private StdKeyDeserializer keyDeserializer;

  @Before
  public void setUp() {
    objectMapper = JsonMapper.builder().build();
    keyDeserializer = new StdKeyDeserializer();
  }

  @Test
  public void testDeserializeKey_NullKey_ReturnsNull() throws IOException {
    assertNull(keyDeserializer.deserializeKey(null, createContext()));
  }

  @Test
  public void testDeserializeKey_ValidStringKey_ReturnsString() throws IOException {
    String key = "test";
    assertEquals(key, keyDeserializer.deserializeKey(key, createContext()));
  }

  @Test
  public void testDeserializeKey_InvalidStringKey_ThrowsException() {
    String key = "invalid";
    assertThrows(IOException.class, () -> keyDeserializer.deserializeKey(key, createContext()));
  }

  @Test
  public void testDeserializeKey_ValidUUIDKey_ReturnsUUID() throws IOException {
    String key = "9512e1c9-76df-4fb1-8d4e-8ca1edcc1546";
    assertEquals(UUID.fromString(key), keyDeserializer.deserializeKey(key, createContext()));
  }

  @Test
  public void testDeserializeKey_InvalidUUIDKey_ThrowsException() {
    String key = "invalid";
    assertThrows(IOException.class, () -> keyDeserializer.deserializeKey(key, createContext()));
  }

  @Test
  public void testDeserializeKey_ValidIntegerKey_ReturnsInteger() throws IOException {
    String key = "10";
    assertEquals(Integer.valueOf(key), keyDeserializer.deserializeKey(key, createContext()));
  }

  @Test
  public void testDeserializeKey_InvalidIntegerKey_ThrowsException() {
    String key = "invalid";
    assertThrows(IOException.class, () -> keyDeserializer.deserializeKey(key, createContext()));
  }

  @Test
  public void testDeserializeKey_ValidLongKey_ReturnsLong() throws IOException {
    String key = "10000000000";
    assertEquals(Long.valueOf(key), keyDeserializer.deserializeKey(key, createContext()));
  }

  @Test
  public void testDeserializeKey_InvalidLongKey_ThrowsException() {
    String key = "invalid";
    assertThrows(IOException.class, () -> keyDeserializer.deserializeKey(key, createContext()));
  }

  private DeserializationContext createContext() {
    return new JsonMapper().createContext(null, null, null);
  }
}