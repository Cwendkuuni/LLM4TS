import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeWriterTest {

  @Test
  public void testGet() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement element = writer.get();

    assertNotNull(element);
    assertTrue(element.isJsonNull());
  }

  @Test
  public void testBeginArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();

    JsonElement element = writer.get();
    assertNotNull(element);
    assertTrue(element instanceof JsonArray);
    assertEquals(0, ((JsonArray) element).size());
  }

  @Test
  public void testEndArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("value1");
    writer.value("value2");
    writer.endArray();

    JsonElement element = writer.get();
    assertNotNull(element);
    assertTrue(element instanceof JsonArray);
    assertEquals(2, ((JsonArray) element).size());
  }

  @Test
  public void testBeginObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();

    JsonElement element = writer.get();
    assertNotNull(element);
    assertTrue(element instanceof JsonObject);
    assertEquals(0, ((JsonObject) element).size());
  }

  @Test
  public void testEndObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key1").value("value1");
    writer.name("key2").value("value2");
    writer.endObject();

    JsonElement element = writer.get();
    assertNotNull(element);
    assertTrue(element instanceof JsonObject);
    assertEquals(2, ((JsonObject) element).size());
  }

  @Test
  public void testName() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key1");

    JsonElement element = writer.get();
    assertNotNull(element);
    assertTrue(element instanceof JsonObject);
    assertEquals("key1", writer.pendingName);
  }

  @Test
  public void testValueString() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("value");

    JsonElement element = writer.get();
    assertNotNull(element);
    assertTrue(element.isJsonPrimitive());
    assertEquals("value", element.getAsString());
  }

  @Test
  public void testNullValue() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.nullValue();

    JsonElement element = writer.get();
    assertNotNull(element);
    assertTrue(element.isJsonNull());
  }

  @Test
  public void testValueBoolean() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(true);

    JsonElement element = writer.get();
    assertNotNull(element);
    assertTrue(element.isJsonPrimitive());
    assertEquals(true, element.getAsBoolean());
  }

  @Test
  public void testValueDouble() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(3.14);

    JsonElement element = writer.get();
    assertNotNull(element);
    assertTrue(element.isJsonPrimitive());
    assertEquals(3.14, element.getAsDouble(), 0.001);
  }

  @Test
  public void testValueLong() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(100);

    JsonElement element = writer.get();
    assertNotNull(element);
    assertTrue(element.isJsonPrimitive());
    assertEquals(100, element.getAsInt());
  }

  @Test
  public void testValueNumber() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(3.14);

    JsonElement element = writer.get();
    assertNotNull(element);
    assertTrue(element.isJsonPrimitive());
    assertEquals(3.14, element.getAsDouble(), 0.001);
  }

  @Test(expected = IOException.class)
  public void testCloseIncompleteDocument() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("value");

    writer.close();
  }
}