import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JsonTreeWriterTest {

  private JsonTreeWriter writer;

  @Before
  public void setup() {
    writer = new JsonTreeWriter();
  }

  @Test
  public void testEmptyWriter() {
    JsonElement root = writer.get();
    assertNotNull(root);
    assertEquals(JsonNull.INSTANCE, root);
  }

  @Test
  public void testBasicObject() throws IOException {
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(30);
    writer.endObject();

    JsonElement root = writer.get();
    assertNotNull(root);
    assertEquals(JsonObject.class, root.getClass());

    JsonObject object = (JsonObject) root;
    assertEquals("name", object.keySet().iterator().next());
    assertEquals("John Doe", object.get("name").getAsString());
    assertEquals(30, object.get("age").getAsInt());
  }

  @Test
  public void testBasicArray() throws IOException {
    writer.beginArray();
    writer.value(1);
    writer.value(2);
    writer.value(3);
    writer.endArray();

    JsonElement root = writer.get();
    assertNotNull(root);
    assertEquals(JsonArray.class, root.getClass());

    JsonArray array = (JsonArray) root;
    assertEquals(3, array.size());
    assertEquals(1, array.get(0).getAsInt());
    assertEquals(2, array.get(1).getAsInt());
    assertEquals(3, array.get(2).getAsInt());
  }

  @Test(expected = IllegalStateException.class)
  public void testMismatchedEndObject() throws IOException {
    writer.beginObject();
    writer.name("name");
    writer.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testMismatchedEndArray() throws IOException {
    writer.beginArray();
    writer.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testMissingBeginObject() throws IOException {
    writer.name("name").value("John");
  }

  @Test(expected = IllegalStateException.class)
  public void testMissingEndObject() throws IOException {
    writer.beginObject();
    writer.name("name").value("John");
  }

  @Test(expected = IllegalStateException.class)
  public void testMissingBeginArray() throws IOException {
    writer.value(1);
  }

  @Test(expected = IllegalStateException.class)
  public void testMissingEndArray() throws IOException {
    writer.beginArray();
  }

  @Test
  public void testNullValues() throws IOException {
    writer.beginObject();
    writer.name("nullValue").value(null);
    writer.endObject();

    JsonElement root = writer.get();
    JsonObject object = (JsonObject) root;
    JsonElement nullValue = object.get("nullValue");
    assertNull(nullValue);
  }

  @After
  public void teardown() {
    writer.close();
  }
}