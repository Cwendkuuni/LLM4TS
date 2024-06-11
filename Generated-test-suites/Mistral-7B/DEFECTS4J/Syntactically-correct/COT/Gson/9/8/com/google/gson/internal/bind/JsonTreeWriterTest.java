import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testGet() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("John Doe");
    writer.endObject();
    JsonElement result = writer.get();

    JsonObject expected = new JsonObject();
    expected.addProperty("name", "John Doe");
    assertEquals(expected, result);
  }

  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value(1);
    writer.value(2);
    writer.endArray();
    JsonElement result = writer.get();

    JsonArray expected = new JsonArray();
    expected.add(new JsonPrimitive(1));
    expected.add(new JsonPrimitive(2));
    assertEquals(expected, result);
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("John Doe");
    writer.name("age");
    writer.value(30);
    writer.endObject();
    JsonElement result = writer.get();

    JsonObject expected = new JsonObject();
    expected.addProperty("name", "John Doe");
    expected.addProperty("age", 30);
    assertEquals(expected, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayAfterEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("John Doe");
    writer.endObject();
    writer.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWithoutBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.value("John Doe");
    writer.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectAfterEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value(1);
    writer.endArray();
    writer.beginObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testNameAfterBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.name("name");
  }

  @Test(expected = IllegalStateException.class)
  public void testValueAfterBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value(1);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetBeforeClosingWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    JsonElement result = writer.get();
    assertNull(result);
  }

  @Test(expected = IllegalStateException.class)
  public void testCloseMultipleTimes() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.close();
    writer.close();
  }
}