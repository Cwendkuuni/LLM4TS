import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testGet() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    JsonElement result = writer.get();
    assertEquals(JsonObject.class, result.getClass());
    JsonObject jsonObject = (JsonObject) result;
    assertEquals("John Doe", jsonObject.get("name").getAsString());
  }

  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value(1);
    writer.value(2);
    writer.value(3);
    JsonArray jsonArray = writer.get().getAsJsonArray();
    assertEquals(3, jsonArray.size());
    assertEquals(JsonPrimitive.class, jsonArray.get(0).getClass());
    assertEquals(1, jsonArray.get(0).getAsInt());
    assertEquals(JsonPrimitive.class, jsonArray.get(1).getClass());
    assertEquals(2, jsonArray.get(1).getAsInt());
    assertEquals(JsonPrimitive.class, jsonArray.get(2).getClass());
    assertEquals(3, jsonArray.get(2).getAsInt());
  }

  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value(1);
    writer.endArray();
    JsonElement result = writer.get();
    assertEquals(JsonArray.class, result.getClass());
    JsonArray jsonArray = (JsonArray) result;
    assertEquals(1, jsonArray.size());
    assertEquals(JsonPrimitive.class, jsonArray.get(0).getClass());
    assertEquals(1, jsonArray.get(0).getAsInt());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWithoutBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("value");
    writer.endArray();
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(30);
    JsonObject jsonObject = writer.get().getAsJsonObject();
    assertEquals("John Doe", jsonObject.get("name").getAsString());
    assertEquals(30, jsonObject.get("age").getAsInt());
  }

  @Test
  public void testEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.endObject();
    JsonElement result = writer.get();
    assertEquals(JsonObject.class, result.getClass());
    JsonObject jsonObject = (JsonObject) result;
    assertEquals("John Doe", jsonObject.get("name").getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWithoutBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("value");
    writer.endObject();
  }

  @Test
  public void testName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    JsonObject jsonObject = writer.get().getAsJsonObject();
    assertEquals("John Doe", jsonObject.get("name").getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testNameWithoutBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.name("name").value("John Doe");
  }

  @Test
  public void testNullValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(null);
    JsonElement result = writer.get();
    assertEquals(JsonNull.INSTANCE.getClass(), result.getClass());
  }

  @Test
  public void testValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("Hello World");
    JsonElement result = writer.get();
    assertEquals(JsonPrimitive.class, result.getClass());
    assertEquals("Hello World", result.getAsString());
  }

  @Test
  public void testValueBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(true);
    JsonElement result = writer.get();
    assertEquals(JsonPrimitive.class, result.getClass());
    assertEquals(true, result.getAsBoolean());
  }

  @Test
  public void testValueDouble() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(3.14);
    JsonElement result = writer.get();
    assertEquals(JsonPrimitive.class, result.getClass());
    assertEquals(3.14, result.getAsDouble(), 0);
  }

  @Test
  public void testValueLong() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(42L);
    JsonElement result = writer.get();
    assertEquals(JsonPrimitive.class, result.getClass());
    assertEquals(42, result.getAsLong());
  }

  @Test
  public void testValueNumber() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(new Double(3.14));
    JsonElement result = writer.get();
    assertEquals(JsonPrimitive.class, result.getClass());
    assertEquals(3.14, result.getAsDouble(), 0);
  }

  @Test
  public void testFlush() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    OutputStreamWriter out = new OutputStreamWriter(output);
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(30);
    writer.flush();
    String jsonString = output.toString();
    assertEquals("{\"name\":\"John Doe\",\"age\":30}", jsonString);
  }

  @Test
  public void testClose() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    OutputStreamWriter out = new OutputStreamWriter(output);
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(30);
    writer.close();
    String jsonString = output.toString();
    assertEquals("{\"name\":\"John Doe\",\"age\":30}", jsonString);
    // Verify that close method threw an exception
    try {
      writer.get();
    } catch (IllegalStateException e) {
      return;
    }
    fail("Expected IllegalStateException but was none");
  }

  @Test
  public void testCloseTwice() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    OutputStreamWriter out = new OutputStreamWriter(output);
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(30);
    writer.close();
    writer.close();
    String jsonString = output.toString();
    assertEquals("{\"name\":\"John Doe\",\"age\":30}", jsonString);
    // Verify that close method threw an exception
    try {
      writer.get();
    } catch (IllegalStateException e) {
      return;
    }
    fail("Expected IllegalStateException but was none");
  }

  @Test(expected = IllegalStateException.class)
  public void testCloseWithoutBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
  }
}