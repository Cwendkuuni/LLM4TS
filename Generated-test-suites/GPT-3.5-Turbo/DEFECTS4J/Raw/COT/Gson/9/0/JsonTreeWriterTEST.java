
import org.junit.*;
import static org.junit.Assert.*;

public class JsonTreeWriterTest {

  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    JsonElement result = writer.get();
    assertTrue(result instanceof JsonArray);
  }
  
  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    JsonElement result = writer.get();
    assertTrue(result instanceof JsonObject);
    assertTrue(result.getAsJsonObject().get("$isClosed") instanceof JsonPrimitive);
    assertEquals("closed", result.getAsJsonObject().get("$isClosed").getAsString());
  }
  
  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    JsonElement result = writer.get();
    assertTrue(result instanceof JsonObject);
  }
  
  @Test
  public void testEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    JsonElement result = writer.get();
    assertTrue(result instanceof JsonObject);
    assertTrue(result.getAsJsonObject().get("$isClosed") instanceof JsonPrimitive);
    assertEquals("closed", result.getAsJsonObject().get("$isClosed").getAsString());
  }
  
  @Test
  public void testName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key");
    JsonElement result = writer.get();
    assertTrue(result instanceof JsonObject);
    assertTrue(result.getAsJsonObject().has("key"));
  }
  
  @Test
  public void testValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("value");
    JsonElement result = writer.get();
    assertTrue(result instanceof JsonPrimitive);
    assertEquals("value", result.getAsString());
  }
  
  @Test
  public void testNullValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.nullValue();
    JsonElement result = writer.get();
    assertTrue(result instanceof JsonNull);
  }
  
  @Test
  public void testValueBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(true);
    JsonElement result = writer.get();
    assertTrue(result instanceof JsonPrimitive);
    assertEquals(true, result.getAsBoolean());
  }
  
  @Test
  public void testValueDouble() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(3.14);
    JsonElement result = writer.get();
    assertTrue(result instanceof JsonPrimitive);
    assertEquals(3.14, result.getAsDouble(), 0.001);
  }
  
  @Test
  public void testValueLong() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(100L);
    JsonElement result = writer.get();
    assertTrue(result instanceof JsonPrimitive);
    assertEquals(100L, result.getAsLong());
  }
  
  @Test
  public void testValueNumber() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(123.45);
    JsonElement result = writer.get();
    assertTrue(result instanceof JsonPrimitive);
    assertEquals(123.45, result.getAsDouble(), 0.001);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testIncompleteDocumentException() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.close();
  }
}
