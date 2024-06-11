import static org.junit.Assert.*;
import org.junit.Test;

public class StringArrayDeserializerTest {

  @Test
  public void testDeserialize_WhenJsonArrayIsValid_ReturnsStringArray() {
    // Arrange
    StringArrayDeserializer deserializer = new StringArrayDeserializer();
    String json = "[\"value1\", \"value2\", \"value3\"]";

    // Act
    String[] result = deserializer.deserialize(json);

    // Assert
    assertNotNull(result);
    assertEquals(3, result.length);
    assertEquals("value1", result[0]);
    assertEquals("value2", result[1]);
    assertEquals("value3", result[2]);
  }

  @Test
  public void testDeserialize_WhenJsonArrayIsNull_ReturnsNullStringArray() {
    // Arrange
    StringArrayDeserializer deserializer = new StringArrayDeserializer();
    String json = "null";

    // Act
    String[] result = deserializer.deserialize(json);

    // Assert
    assertNull(result);
  }

  @Test
  public void testDeserialize_WhenJsonArrayIsEmpty_ReturnsEmptyStringArray() {
    // Arrange
    StringArrayDeserializer deserializer = new StringArrayDeserializer();
    String json = "[]";

    // Act
    String[] result = deserializer.deserialize(json);

    // Assert
    assertNotNull(result);
    assertEquals(0, result.length);
  }

  @Test
  public void testDeserialize_WhenJsonArrayContainsNull_ReturnsStringArrayWithNullElement() {
    // Arrange
    StringArrayDeserializer deserializer = new StringArrayDeserializer();
    String json = "[\"value1\", null, \"value2\"]";

    // Act
    String[] result = deserializer.deserialize(json);

    // Assert
    assertNotNull(result);
    assertEquals(3, result.length);
    assertEquals("value1", result[0]);
    assertNull(result[1]);
    assertEquals("value2", result[2]);
  }

  @Test
  public void testDeserialize_WhenJsonArrayContainsNonStringValues_ThrowsException() {
    // Arrange
    StringArrayDeserializer deserializer = new StringArrayDeserializer();
    String json = "[\"value1\", 123, true]";

    try {
      // Act
      deserializer.deserialize(json);
      fail("Expected IOException was not thrown.");
    } catch (IOException ex) {
      // Assert
      assertEquals("Expected String token but found 123.", ex.getMessage());
    }
  }

}