import static org.junit.Assert.*;
import static com.google.gson.stream.CoercingJsonWriter.*;

import com.google.gson.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;

import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testJsonTreeWriter_beginObject_name_value_endObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key1").value("value1");
    writer.endObject();
    JsonElement element = writer.get();
    JsonObject jsonObject = element.getAsJsonObject();
    assertEquals("value1", jsonObject.get("key1").getAsString());
  }

  @Test
  public void testJsonTreeWriter_beginArray_endArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.beginArray();
    writer.value(1);
    writer.value(2);
    writer.endArray();
    writer.endArray();
    JsonElement element = writer.get();
    JsonArray jsonArray = element.getAsJsonArray();
    JsonArray innerArray = jsonArray.get(0).getAsJsonArray();
    assertEquals(2, innerArray.size());
    assertEquals(1, innerArray.get(0).getAsInt());
    assertEquals(2, innerArray.get(1).getAsInt());
  }

  @Test
  public void testJsonTreeWriter_name_value_endObject_name_value_endObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key1").value("value1");
    writer.beginObject();
    writer.name("key2").value("value2");
    writer.endObject();
    writer.endObject();
    JsonElement element = writer.get();
    JsonObject jsonObject = element.getAsJsonObject();
    JsonObject innerObject = jsonObject.getAsJsonObject().get("key1").getAsJsonObject();
    assertEquals("value2", innerObject.get("key2").getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriter_beginArray_name_value() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.name("key1").value("value1");
    writer.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriter_beginObject_value() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.value("value1");
    writer.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriter_endArray_beforeBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endArray();
    writer.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriter_endObject_beforeBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endObject();
    writer.beginObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriter_name_nullValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key1").value(null);
    writer.endObject();
  }
}