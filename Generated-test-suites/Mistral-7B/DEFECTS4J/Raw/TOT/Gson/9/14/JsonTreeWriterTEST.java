import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testWriteBasicObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(30);
    writer.endObject();

    JsonElement json = writer.get();
    JsonObject obj = json.getAsJsonObject();
    assertNotNull(obj);
    assertEquals("John Doe", obj.get("name").getAsString());
    assertEquals(30, obj.get("age").getAsInt());
  }

  @Test
  public void testWriteEmptyArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();

    JsonElement json = writer.get();
    JsonArray arr = json.getAsJsonArray();
    assertNotNull(arr);
    assertEquals(0, arr.size());
  }

  @Test
  public void testWriteArrayWithElements() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("element1");
    writer.value(1);
    writer.value(new JsonPrimitive(3.14));
    writer.endArray();

    JsonElement json = writer.get();
    JsonArray arr = json.getAsJsonArray();
    assertNotNull(arr);
    assertEquals(3, arr.size());
    assertEquals("element1", arr.get(0).getAsString());
    assertEquals(1, arr.get(1).getAsInt());
    assertEquals(3.14, arr.get(2).getAsDouble(), 0.001);
  }

  @Test
  public void testWriteNestedObjects() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").beginObject();
    writer.name("years").value(30);
    writer.name("months").value(2);
    writer.endObject();
    writer.endObject();

    JsonElement json = writer.get();
    JsonObject obj = json.getAsJsonObject();
    assertNotNull(obj);
    assertEquals("John Doe", obj.get("name").getAsString());
    JsonElement age = obj.get("age");
    assertNotNull(age);
    assertTrue(age.isJsonObject());
    JsonObject ageObj = age.getAsJsonObject();
    assertNotNull(ageObj);
    assertEquals(30, ageObj.get("years").getAsInt());
    assertEquals(2, ageObj.get("months").getAsInt());
  }

  @Test
  public void testWriteNullValues() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value(null);
    writer.name("age").value(null);
    writer.endObject();

    JsonElement json = writer.get();
    JsonObject obj = json.getAsJsonObject();
    assertNotNull(obj);
    assertNull(obj.get("name"));
    assertNull(obj.get("age"));
  }

  @Test(expected = IllegalStateException.class)
  public void testWriteMissingEndArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("element1");
    // missing endArray()

    writer.value("element2");
  }

  @Test(expected = IllegalStateException.class)
  public void testWriteMissingEndObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    // missing endObject()

    writer.name("age").beginObject();
    writer.name("years").value(30);
  }
}