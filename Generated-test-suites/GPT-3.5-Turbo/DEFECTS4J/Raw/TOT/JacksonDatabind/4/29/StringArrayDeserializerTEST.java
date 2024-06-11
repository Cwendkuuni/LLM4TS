import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

  private StringArrayDeserializer deserializer = new StringArrayDeserializer();

  @Test
  public void testDeserialize_ValidInput_ShouldReturnStringArray() {
    String json = "[\"value1\", \"value2\", \"value3\"]";
    String[] expected = { "value1", "value2", "value3" };
    try {
      String[] result = deserializer.deserialize(new JsonParser(json), new DeserializationContext());
      assertArrayEquals(expected, result);
    } catch (Exception e) {
      fail("An exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testDeserialize_NullStringValues_ShouldReturnNullValues() {
    String json = "[null, null, null]";
    String[] expected = { null, null, null };
    try {
      String[] result = deserializer.deserialize(new JsonParser(json), new DeserializationContext());
      assertArrayEquals(expected, result);
    } catch (Exception e) {
      fail("An exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testDeserialize_NonArrayInput_ShouldThrowException() {
    String json = "\"value\"";
    try {
      deserializer.deserialize(new JsonParser(json), new DeserializationContext());
      fail("Expected an exception to be thrown");
    } catch (Exception e) {
      assertTrue(e instanceof MappingException);
      assertEquals("MappingException: Cannot deserialize non-array JSON input", e.getMessage());
    }
  }

  @Test
  public void testDeserialize_EmptyArrayInput_ShouldReturnEmptyArray() {
    String json = "[]";
    String[] expected = {};
    try {
      String[] result = deserializer.deserialize(new JsonParser(json), new DeserializationContext());
      assertArrayEquals(expected, result);
    } catch (Exception e) {
      fail("An exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testDeserialize_InvalidJsonInput_ShouldThrowException() {
    String json = "[\"value1\", \"value2\",";
    try {
      deserializer.deserialize(new JsonParser(json), new DeserializationContext());
      fail("Expected an exception to be thrown");
    } catch (Exception e) {
      assertTrue(e instanceof IOException);
      assertEquals("IOException: Invalid JSON input", e.getMessage());
    }
  }

  @Test
  public void testDeserializeWithContextual_DefaultDeserializer_ShouldReturnStringArray() {
    String json = "[\"value1\", \"value2\", \"value3\"]";
    String[] expected = { "value1", "value2", "value3" };
    try {
      JsonDeserializer<String> elementDeserializer = new StringDeserializer();
      StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
      String[] result = deserializer.deserialize(new JsonParser(json), new DeserializationContext());
      assertArrayEquals(expected, result);
    } catch (Exception e) {
      fail("An exception occurred: " + e.getMessage());
    }
  }

  @Test
  public void testDeserializeWithType_ValidInput_ShouldDeserializeWithTypeInformation() {
    String json = "[\"value1\", \"value2\", \"value3\"]";
    String[] expected = { "value1", "value2", "value3" };
    try {
      String[] result = deserializer.deserializeWithType(new JsonParser(json), new DeserializationContext(), new TypeDeserializer());
      assertArrayEquals(expected, result);
    } catch (Exception e) {
      fail("An exception occurred: " + e.getMessage());
    }
  }

}