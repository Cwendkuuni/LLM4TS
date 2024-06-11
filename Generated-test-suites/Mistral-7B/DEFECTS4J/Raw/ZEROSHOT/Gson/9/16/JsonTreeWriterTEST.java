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
  public void testJsonTreeWriter() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();

    // Test empty JSON object
    writer.beginObject();
    writer.endObject();
    JsonElement result = writer.get();
    assertEquals(JsonObject.class, result.getClass());
    assertEquals(0, result.size());

    // Test JSON object with one key-value pair
    writer.beginObject();
    writer.name("key");
    writer.value("value");
    writer.endObject();
    result = writer.get();
    JsonObject jsonObject = result.getAsJsonObject();
    assertEquals("value", jsonObject.get("key").getAsString());

    // Test JSON object with multiple key-value pairs
    writer.beginObject();
    writer.name("key1");
    writer.value("value1");
    writer.name("key2");
    writer.value("value2");
    writer.endObject();
    result = writer.get();
    jsonObject = result.getAsJsonObject();
    assertEquals("value1", jsonObject.get("key1").getAsString());
    assertEquals("value2", jsonObject.get("key2").getAsString());

    // Test JSON array
    writer.beginArray();
    writer.value(1);
    writer.value(2.0);
    writer.value("three");
    writer.endArray();
    result = writer.get();
    JsonArray jsonArray = result.getAsJsonArray();
    assertEquals(3, jsonArray.size());
    assertEquals(Integer.class, jsonArray.get(0).getClass());
    assertEquals(Double.class, jsonArray.get(1).getClass());
    assertEquals(String.class, jsonArray.get(2).getClass());

    // Test nested JSON objects and arrays
    writer.beginObject();
    writer.name("obj1");
    writer.beginObject();
    writer.name("key1");
    writer.value("value1");
    writer.name("key2");
    writer.value(new JsonPrimitive(42));
    writer.endObject();
    writer.endObject();
    result = writer.get();
    jsonObject = result.getAsJsonObject();
    JsonElement obj1 = jsonObject.get("obj1");
    JsonObject obj1JsonObject = obj1.getAsJsonObject();
    assertEquals("value1", obj1JsonObject.get("key1").getAsString());
    assertEquals(42, obj1JsonObject.get("key2").getAsInt());

    // Test write to a String
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    OutputStreamWriter writerForString = new OutputStreamWriter(bos);
    writer.beginObject();
    writer.name("root");
    writer.beginArray();
    writer.value(1);
    writer.value(2.0);
    writer.value("three");
    writer.endArray();
    writer.endObject();
    writer.close();
    String jsonString = bos.toString();
    assertEquals("{\"root\":[1,2.0,\"three\"]}", jsonString);

    // Test error cases
    try {
      writer.beginArray();
      writer.name("key");
      writer.endObject();
      fail();
    } catch (IllegalStateException e) {
      // pass
    }

    try {
      writer.beginObject();
      writer.value("value");
      writer.name("key");
      writer.endObject();
      fail();
    } catch (IllegalStateException e) {
      // pass
    }

    try {
      writer.beginObject();
      writer.name("key");
      writer.endArray();
      fail();
    } catch (IllegalStateException e) {
      // pass
    }

    try {
      writer.beginArray();
      writer.name("key");
      writer.endObject();
      fail();
    } catch (IllegalStateException e) {
      // pass
    }

    try {
      writer.value("value");
      writer.name("key");
      fail();
    } catch (IllegalStateException e) {
      // pass
    }

    try {
      writer.name("key");
      writer.value("value");
      writer.name("key");
      fail();
    } catch (IllegalStateException e) {
      // pass
    }

    try {
      writer.close();
      writer.value("value");
      fail();
    } catch (IllegalStateException e) {
      // pass
    }

    try {
      writer.close();
      writer.close();
      fail();
    } catch (IllegalStateException e) {
      // pass
    }

    try {
      writer.name("key");
      writer.close();
      fail();
    } catch (IllegalStateException e) {
      // pass
    }
  }

  @Test
  public void testGetNullValue() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(null);
    JsonElement result = writer.get();
    assertEquals(JsonNull.INSTANCE, result);
  }
}