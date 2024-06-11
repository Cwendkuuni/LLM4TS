import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonTreeWriterTest {
  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    JsonElement expected = new JsonArray();
    assertEquals(expected, writer.get());
  }

  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    JsonElement expected = JsonNull.INSTANCE;
    assertEquals(expected, writer.get());
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    JsonObject expected = new JsonObject();
    assertEquals(expected, writer.get());
  }

  @Test
  public void testEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    JsonElement expected = JsonNull.INSTANCE;
    assertEquals(expected, writer.get());
  }

  @Test
  public void testName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("test");
    writer.value("hello");
    writer.endObject();
    JsonObject expected = new JsonObject();
    expected.addProperty("test", "hello");
    assertEquals(expected, writer.get());
  }

  @Test
  public void testValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("test");
    writer.value("hello");
    writer.endObject();
    JsonObject expected = new JsonObject();
    expected.addProperty("test", "hello");
    assertEquals(expected, writer.get());
  }

  @Test(expected = IllegalStateException.class)
  public void testValueAfterEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    writer.value("hello");
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayAfterBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.beginObject();
    writer.endObject();
    writer.endArray();
  }

  @Test
  public void testNullValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("test");
    writer.nullValue();
    writer.endObject();
    JsonObject expected = new JsonObject();
    expected.addProperty("test", null);
    assertEquals(expected, writer.get());
  }

  @Test
  public void testValueBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("test");
    writer.value(true);
    writer.endObject();
    JsonObject expected = new JsonObject();
    expected.addProperty("test", true);
    assertEquals(expected, writer.get());
  }

  @Test
  public void testValueDouble() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("test");
    writer.value(1.23);
    writer.endObject();
    JsonObject expected = new JsonObject();
    expected.addProperty("test", 1.23);
    assertEquals(expected, writer.get());
  }

  @Test
  public void testValueLong() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("test");
    writer.value(123);
    writer.endObject();
    JsonObject expected = new JsonObject();
    expected.addProperty("test", 123);
    assertEquals(expected, writer.get());
  }

  @Test
  public void testValueNumber() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("test");
    writer.value(new Double(123.45));
    writer.endObject();
    JsonObject expected = new JsonObject();
    expected.addProperty("test", 123.45);
    assertEquals(expected, writer.get());
  }

  @Test
  public void testFlush() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("test");
    writer.value("hello");
    writer.endObject();
    writer.flush();
    JsonObject expected = new JsonObject();
    expected.addProperty("test", "hello");
    assertEquals(expected, writer.get());
  }

  @Test
  public void testClose() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("test");
    writer.value("hello");
    writer.endObject();
    writer.close();
    JsonElement expected = SENTINEL_CLOSED;
    assertEquals(expected, writer.get());
  }

  @Test(expected = IOException.class)
  public void testCloseTwice() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("test");
    writer.value("hello");
    writer.endObject();
    writer.close();
    writer.close();
  }
}