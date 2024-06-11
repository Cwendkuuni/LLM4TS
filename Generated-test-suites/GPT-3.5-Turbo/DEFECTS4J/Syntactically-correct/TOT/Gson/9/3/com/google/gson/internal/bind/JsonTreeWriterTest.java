import org.junit.*;
import static org.junit.Assert.*;

public class JsonTreeWriterTest {

  // Test for beginArray() method
  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    assertEquals(1, writer.get().getAsJsonArray().size());
  }

  // Test for endArray() method
  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    assertEquals(0, writer.get().getAsJsonArray().size());
  }

  // Test for beginObject() method
  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    assertEquals(1, writer.get().getAsJsonObject().entrySet().size());
  }

  // Test for endObject() method
  @Test
  public void testEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    assertEquals(0, writer.get().getAsJsonObject().entrySet().size());
  }

  // Test for name(String) method
  @Test
  public void testName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("John");
    writer.endObject();
    assertEquals("John", writer.get().getAsJsonObject().get("name").getAsString());
  }

  // Test for value(String) method
  @Test
  public void testValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("Hello");
    assertEquals("Hello", writer.get().getAsString());
  }

  // Test for nullValue() method
  @Test
  public void testNullValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.nullValue();
    assertTrue(writer.get().isJsonNull());
  }

  // Test for value(boolean) method
  @Test
  public void testValueBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(true);
    assertEquals(true, writer.get().getAsBoolean());
  }

  // Test for value(double) method
  @Test
  public void testValueDouble() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(3.14);
    assertEquals(3.14, writer.get().getAsDouble(), 0.001);
  }

  // Test for value(long) method
  @Test
  public void testValueLong() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(100);
    assertEquals(100, writer.get().getAsLong());
  }

  // Test for value(Number) method
  @Test
  public void testValueNumber() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(3.14);
    assertEquals(3.14, writer.get().getAsNumber());
  }

  // Test for flush() method
  @Test
  public void testFlush() {
    JsonTreeWriter writer = new JsonTreeWriter();
    assertDoesNotThrow(() -> writer.flush());
  }

  // Test for close() method
  @Test
  public void testClose() {
    JsonTreeWriter writer = new JsonTreeWriter();
    assertThrows(IOException.class, () -> writer.close());
  }

}