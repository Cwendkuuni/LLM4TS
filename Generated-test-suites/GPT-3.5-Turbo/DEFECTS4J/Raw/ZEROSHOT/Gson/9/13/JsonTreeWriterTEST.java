import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeWriterTest {

  @Test
  public void testGet() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement jsonElement = writer.get();

    assertNotNull(jsonElement);
    assertTrue(jsonElement.isJsonNull());
  }

  @Test
  public void testBeginArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();

    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    assertTrue(jsonElement.isJsonArray());
  }

  @Test
  public void testEndArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();

    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    assertTrue(jsonElement.isJsonArray());
    assertEquals(0, ((JsonArray) jsonElement).size());
  }

  @Test
  public void testBeginObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();

    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    assertTrue(jsonElement.isJsonObject());
  }

  @Test
  public void testEndObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();

    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    assertTrue(jsonElement.isJsonObject());
    assertEquals(0, ((JsonObject) jsonElement).size());
  }

  @Test
  public void testName() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key");
    writer.value("value");
    writer.endObject();

    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    assertTrue(jsonElement.isJsonObject());
    assertTrue(((JsonObject) jsonElement).has("key"));
  }

  @Test
  public void testValueString() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key");
    writer.value("value");
    writer.endObject();

    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    assertTrue(jsonElement.isJsonObject());
    assertEquals("value", ((JsonObject) jsonElement).get("key").getAsString());
  }

  @Test
  public void testNullValue() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key");
    writer.nullValue();
    writer.endObject();

    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    assertTrue(jsonElement.isJsonObject());
    assertTrue(((JsonObject) jsonElement).get("key").isJsonNull());
  }

  @Test
  public void testValueBoolean() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key");
    writer.value(true);
    writer.endObject();

    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    assertTrue(jsonElement.isJsonObject());
    assertEquals(true, ((JsonObject) jsonElement).get("key").getAsBoolean());
  }

  @Test
  public void testValueDouble() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key");
    writer.value(3.14);
    writer.endObject();

    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    assertTrue(jsonElement.isJsonObject());
    assertEquals(3.14, ((JsonObject) jsonElement).get("key").getAsDouble(), 0.0001);
  }

  @Test
  public void testValueLong() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key");
    writer.value(42);
    writer.endObject();

    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    assertTrue(jsonElement.isJsonObject());
    assertEquals(42, ((JsonObject) jsonElement).get("key").getAsLong());
  }

  @Test
  public void testValueNumber() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key");
    writer.value(3.14);
    writer.endObject();

    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    assertTrue(jsonElement.isJsonObject());
    assertEquals(3.14, ((JsonObject) jsonElement).get("key").getAsDouble(), 0.0001);
  }
}