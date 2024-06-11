import static org.junit.Assert.*;
import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testGet() {
    // Test case 1
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement result = writer.get();
    assertEquals(JsonNull.INSTANCE, result);

    // Test case 2
    writer.beginObject().name("name").value("John Doe").endObject();
    result = writer.get();
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", "John Doe");
    assertEquals(jsonObject, result);

    // Test case 3
    writer.beginArray().value("value1").value("value2").endArray();
    result = writer.get();
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive("value1"));
    jsonArray.add(new JsonPrimitive("value2"));
    assertEquals(jsonArray, result);
  }

  @Test
  public void testBeginArray() {
    // Test case 1
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    JsonElement result = writer.get();
    assertEquals(new JsonArray(), result);
  }

  @Test
  public void testEndArray() {
    // Test case 1
    JsonTreeWriter writer = new JsonTreeWriter();
    assertThrows(IllegalStateException.class, () -> {
      writer.endArray();
    });

    // Test case 2
    writer.beginArray();
    writer.endArray();
    JsonElement result = writer.get();
    assertEquals(new JsonArray(), result);
  }

  @Test
  public void testBeginObject() {
    // Test case 1
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    JsonElement result = writer.get();
    assertEquals(new JsonObject(), result);
  }

  @Test
  public void testEndObject() {
    // Test case 1
    JsonTreeWriter writer = new JsonTreeWriter();
    assertThrows(IllegalStateException.class, () -> {
      writer.endObject();
    });

    // Test case 2
    writer.beginObject();
    writer.endObject();
    JsonElement result = writer.get();
    assertEquals(new JsonObject(), result);
  }

  @Test
  public void testName() {
    // Test case 1
    JsonTreeWriter writer = new JsonTreeWriter();
    assertThrows(IllegalStateException.class, () -> {
      writer.name("name");
    });

    // Test case 2
    writer.beginObject();
    writer.name("name");
    writer.value("John Doe");
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", "John Doe");
    assertEquals(jsonObject, result);
  }

  @Test
  public void testValue() {
    // Test case 1
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("value1");
    JsonElement result = writer.get();
    JsonPrimitive jsonPrimitive1 = new JsonPrimitive("value1");
    assertEquals(jsonPrimitive1, result);

    // Test case 2
    writer.value(false);
    result = writer.get();
    JsonPrimitive jsonPrimitive2 = new JsonPrimitive(false);
    assertEquals(jsonPrimitive2, result);

    // Test case 3
    writer.value(3.14);
    result = writer.get();
    JsonPrimitive jsonPrimitive3 = new JsonPrimitive(3.14);
    assertEquals(jsonPrimitive3, result);

    // Test case 4
    writer.value(1000000000L);
    result = writer.get();
    JsonPrimitive jsonPrimitive4 = new JsonPrimitive(1000000000L);
    assertEquals(jsonPrimitive4, result);

    // Test case 5
    writer.value(null);
    result = writer.get();
    assertEquals(JsonNull.INSTANCE, result);
  }

  @Test
  public void testNullValue() {
    // Test case 1
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.nullValue();
    JsonElement result = writer.get();
    assertEquals(JsonNull.INSTANCE, result);
  }

  @Test
  public void testFlush() {
    // Test case 1
    JsonTreeWriter writer = new JsonTreeWriter();
    assertDoesNotThrow(() -> {
      writer.flush();
    });
  }

  @Test
  public void testClose() {
    // Test case 1
    JsonTreeWriter writer = new JsonTreeWriter();
    assertThrows(IOException.class, () -> {
      writer.close();
    });

    // Test case 2
    writer.beginObject().endObject();
    assertDoesNotThrow(() -> {
      writer.close();
    });
    JsonElement result = writer.get();
    assertEquals(JsonNull.INSTANCE, result);
  }
}