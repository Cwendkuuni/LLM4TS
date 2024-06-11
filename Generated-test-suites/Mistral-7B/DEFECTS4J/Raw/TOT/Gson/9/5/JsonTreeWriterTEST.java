import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.junit.Before;
import org.junit.Test;

public class JsonTreeWriterTest {

  private JsonTreeWriter writer;

  @Before
  public void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
  public void testBeginArray() throws IOException {
    writer.beginArray();
    JsonArray array = writer.get();
    assertEquals(JsonArray.class, array.getClass());
    assertTrue(array.isJsonArray());
  }

  @Test
  public void testEndArray() throws IOException {
    writer.beginArray();
    writer.endArray();
    JsonElement element = writer.get();
    assertEquals(JsonNull.INSTANCE, element);
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayTwice() throws IOException {
    writer.beginArray();
    writer.beginArray();
  }

  @Test
  public void testNameObject() throws IOException {
    writer.beginObject();
    writer.name("name");
    writer.value("value");
    writer.endObject();
    JsonObject object = writer.get();
    assertEquals("value", object.get("name").getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testNameArray() throws IOException {
    writer.beginObject();
    writer.name("name");
    writer.beginArray();
  }

  @Test
  public void testValueString() throws IOException {
    writer.beginObject();
    writer.name("name");
    writer.value("value");
    writer.endObject();
    JsonObject object = writer.get();
    assertEquals("value", object.get("name").getAsString());
  }

  @Test
  public void testValueBoolean() throws IOException {
    writer.beginObject();
    writer.name("name");
    writer.value(true);
    writer.endObject();
    JsonObject object = writer.get();
    assertTrue(object.get("name").getAsJsonPrimitive().isBoolean());
    assertTrue(object.get("name").getAsJsonPrimitive().getAsBoolean());
  }

  @Test
  public void testValueDouble() throws IOException {
    writer.beginObject();
    writer.name("name");
    writer.value(3.14);
    writer.endObject();
    JsonObject object = writer.get();
    assertTrue(object.get("name").isJsonPrimitive());
    assertEquals(3.14, object.get("name").getAsJsonPrimitive().getAsDouble(), 0.0);
  }

  @Test
  public void testValueLong() throws IOException {
    writer.beginObject();
    writer.name("name");
    writer.value(42);
    writer.endObject();
    JsonObject object = writer.get();
    assertTrue(object.get("name").isJsonPrimitive());
    assertEquals(42L, object.get("name").getAsJsonPrimitive().getAsLong());
  }

  @Test
  public void testValueNull() throws IOException {
    writer.beginObject();
    writer.name("name");
    writer.nullValue();
    writer.endObject();
    JsonObject object = writer.get();
    assertNull(object.get("name"));
  }

  @Test
  public void testBeginObjectTwice() throws IOException {
    writer.beginObject();
    writer.beginObject();
  }

  @Test
  public void testEndObjectWithoutBeginObject() throws IOException {
    writer.endObject();
  }

  @Test
  public void testValueArray() throws IOException {
    writer.beginObject();
    writer.name("name");
    writer.beginArray();
    writer.value(1);
    writer.value(2);
    writer.endArray();
    writer.endObject();
    JsonObject object = writer.get();
    JsonArray array = object.get("name").getAsJsonArray();
    assertEquals(2, array.size());
    assertEquals(1, array.get(0).getAsInt());
    assertEquals(2, array.get(1).getAsInt());
  }

  @Test
  public void testCloseWriter() throws IOException {
    writer.close();
    JsonElement element = writer.get();
    assertEquals(JsonPrimitive.class, element.getClass());
    assertEquals("closed", element.getAsString());
  }

  @Test
  public void testCloseWriterTwice() throws IOException {
    writer.close();
    writer.close();
  }
}