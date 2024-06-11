import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;

@RunWith(JUnit4.class)
public class JsonTreeWriterTest {

  @Test
  public void testJsonTreeWriter_constructor() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertNull(jsonTreeWriter.get());
  }

  @Test(expected = AssertionError.class)
  public void testJsonTreeWriter_write_unwritableWriter() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.write('{');
  }

  @Test
  public void testJsonTreeWriter_beginArray() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    JsonArray jsonArray = jsonTreeWriter.get();
    assertEquals(JsonArray.class, jsonArray.getClass());
    assertFalse(jsonArray.isJsonNull());
    jsonTreeWriter.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriter_endArray_noArray() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.endArray();
  }

  @Test
  public void testJsonTreeWriter_endArray() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
  }

  @Test
  public void testJsonTreeWriter_beginObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    JsonObject jsonObject = jsonTreeWriter.get();
    assertEquals(JsonObject.class, jsonObject.getClass());
    assertFalse(jsonObject.isJsonNull());
    jsonTreeWriter.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriter_endObject_noObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.endObject();
  }

  @Test
  public void testJsonTreeWriter_endObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
  }

  @Test
  public void testJsonTreeWriter_name() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    JsonObject jsonObject = jsonTreeWriter.get();
    JsonElement jsonElement = jsonObject.get("key");
    assertEquals(JsonPrimitive.class, jsonElement.getClass());
    jsonTreeWriter.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriter_name_noObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.name("key");
  }

  @Test
  public void testJsonTreeWriter_value_string() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    jsonTreeWriter.value("value");
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    JsonPrimitive jsonPrimitive = jsonObject.getAsJsonPrimitive("key");
    assertEquals("value", jsonPrimitive.getAsString());
  }

  @Test
  public void testJsonTreeWriter_nullValue() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    jsonTreeWriter.nullValue();
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    JsonPrimitive jsonPrimitive = jsonObject.get("key");
    assertNull(jsonPrimitive.isJsonNull() ? jsonPrimitive.getAsString() : jsonPrimitive.getAsInt());
  }

  @Test
  public void testJsonTreeWriter_value_boolean() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    jsonTreeWriter.value(true);
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    JsonPrimitive jsonPrimitive = jsonObject.get("key");
    assertTrue(jsonPrimitive.getAsBoolean());
  }

  @Test
  public void testJsonTreeWriter_value_double() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    jsonTreeWriter.value(3.14);
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    JsonPrimitive jsonPrimitive = jsonObject.get("key");
    assertEquals(3.14, jsonPrimitive.getAsDouble(), 0);
  }

  @Test
  public void testJsonTreeWriter_value_long() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    jsonTreeWriter.value(42);
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    JsonPrimitive jsonPrimitive = jsonObject.get("key");
    assertEquals(42, jsonPrimitive.getAsLong());
  }

  @Test
  public void testJsonTreeWriter_value_number() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    jsonTreeWriter.value(new Long(42));
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    JsonPrimitive jsonPrimitive = jsonObject.get("key");
    assertEquals(42, jsonPrimitive.getAsLong());
  }

  @Test
  public void testJsonTreeWriter_multiple_values() {
    List<JsonElement> jsonElements = new ArrayList<>();
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter() {
      @Override
      public JsonWriter beginObject() throws IOException {
        JsonObject jsonObject = new JsonObject();
        put(jsonObject);
        return super.beginObject();
      }

      @Override
      public JsonWriter endObject() throws IOException {
        JsonElement jsonElement = get();
        jsonElements.add(jsonElement);
        super.endObject();
        return this;
      }

      @Override
      public void close() throws IOException {
        super.close();
      }
    };
    jsonTreeWriter.value("value1");
    jsonTreeWriter.name("key1").value("value2");
    jsonTreeWriter.value(42);
    jsonTreeWriter.name("key2").value(new Double(3.14));
    jsonTreeWriter.close();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    List<JsonElement> jsonElementsActual = jsonObject.getAsJsonArray();
    assertEquals(jsonElements.size(), jsonElementsActual.size());
    for (int i = 0; i < jsonElements.size(); i++) {
      JsonElement jsonElementActual = jsonElementsActual.get(i);
      assertEquals(jsonElements.get(i).getClass(), jsonElementActual.getClass());
      if (jsonElements.get(i) instanceof JsonPrimitive) {
        JsonPrimitive jsonPrimitive = jsonElements.get(i).getAsJsonPrimitive();
        JsonPrimitive jsonPrimitiveActual = jsonElementActual.getAsJsonPrimitive();
        if (jsonPrimitive instanceof JsonNull) {
          assertNull(jsonPrimitiveActual.isJsonNull() ? jsonPrimitiveActual.getAsString() : jsonPrimitiveActual.getAsInt());
        } else {
          assertEquals(jsonPrimitive.isString(), jsonPrimitiveActual.isString());
          if (jsonPrimitive.isString()) {
            assertEquals(jsonPrimitive.getAsString(), jsonPrimitiveActual.getAsString());
          } else {
            assertEquals(jsonPrimitive.getAsLong(), jsonPrimitiveActual.getAsLong());
            assertEquals(jsonPrimitive.getAsDouble(), jsonPrimitiveActual.getAsDouble(), 0);
          }
        }
      } else {
        JsonObject jsonObjectExpected = jsonElements.get(i).getAsJsonObject();
        JsonObject jsonObjectActual = jsonElementActual.getAsJsonObject();
        assertEquals(jsonObjectExpected.entrySet().size(), jsonObjectActual.entrySet().size());
        for (Map.Entry<String, JsonElement> entryExpected : jsonObjectExpected.entrySet()) {
          String key = entryExpected.getKey();
          JsonElement valueExpected = entryExpected.getValue();
          JsonElement valueActual = jsonObjectActual.get(key);
          assertEquals(valueExpected.getClass(), valueActual.getClass());
          if (valueExpected instanceof JsonPrimitive) {
            JsonPrimitive jsonPrimitiveExpected = valueExpected.getAsJsonPrimitive();
            JsonPrimitive jsonPrimitiveActual = valueActual.getAsJsonPrimitive();
            if (jsonPrimitiveExpected instanceof JsonNull) {
              assertNull(jsonPrimitiveActual.isJsonNull() ? jsonPrimitiveActual.getAsString() : jsonPrimitiveActual.getAsInt());
            } else {
              assertEquals(jsonPrimitiveExpected.isString(), jsonPrimitiveActual.isString());
              if (jsonPrimitiveExpected.isString()) {
                assertEquals(jsonPrimitiveExpected.getAsString(), jsonPrimitiveActual.getAsString());
              } else {
                assertEquals(jsonPrimitiveExpected.getAsLong(), jsonPrimitiveActual.getAsLong());
                assertEquals(jsonPrimitiveExpected.getAsDouble(), jsonPrimitiveActual.getAsDouble(), 0);
              }
            }
          } else {
            JsonObject jsonObjectExpectedChild = valueExpected.getAsJsonObject();
            JsonObject jsonObjectActualChild = valueActual.getAsJsonObject();
            testJsonTreeWriter_multiple_values(jsonObjectExpectedChild, jsonObjectActualChild);
          }
        }
      }
    }
  }

  @Test
  public void testJsonTreeWriter_close() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
    jsonTreeWriter.close();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonArray jsonArray = jsonElement.getAsJsonArray();
    assertEquals(0, jsonArray.size());
  }

  @Test(expected = IOException.class)
  public void testJsonTreeWriter_close_incomplete_document() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.close();
  }

  private void testJsonTreeWriter_multiple_values(JsonObject jsonObjectExpected, JsonObject jsonObjectActual) {
    assertEquals(jsonObjectExpected.entrySet().size(), jsonObjectActual.entrySet().size());
    for (Map.Entry<String, JsonElement> entryExpected : jsonObjectExpected.entrySet()) {
      String key = entryExpected.getKey();
      JsonElement valueExpected = entryExpected.getValue();
      JsonElement valueActual = jsonObjectActual.get(key);
      assertEquals(valueExpected.getClass(), valueActual.getClass());
      if (valueExpected instanceof JsonPrimitive) {
        JsonPrimitive jsonPrimitiveExpected = valueExpected.getAsJsonPrimitive();
        JsonPrimitive jsonPrimitiveActual = valueActual.getAsJsonPrimitive();
        if (jsonPrimitiveExpected instanceof JsonNull) {
          assertNull(jsonPrimitiveActual.isJsonNull() ? jsonPrimitiveActual.getAsString() : jsonPrimitiveActual.getAsInt());
        } else {
          assertEquals(jsonPrimitiveExpected.isString(), jsonPrimitiveActual.isString());
          if (jsonPrimitiveExpected.isString()) {
            assertEquals(jsonPrimitiveExpected.getAsString(), jsonPrimitiveActual.getAsString());
          } else {
            assertEquals(jsonPrimitiveExpected.getAsLong(), jsonPrimitiveActual.getAsLong());
            assertEquals(jsonPrimitiveExpected.getAsDouble(), jsonPrimitiveActual.getAsDouble(), 0);
          }
        }
      } else {
        JsonObject jsonObjectExpectedChild = valueExpected.getAsJsonObject();
        JsonObject jsonObjectActualChild = valueActual.getAsJsonObject();
        testJsonTreeWriter_multiple_values(jsonObjectExpectedChild, jsonObjectActualChild);
      }
    }
  }

  @Test
  public void testJsonTreeWriter_isLenient() {
    JsonTreeWriter jsonTreeWriter1 = new JsonTreeWriter();
    jsonTreeWriter1.value(Double.NaN);
    JsonTreeWriter jsonTreeWriter2 = new JsonTreeWriter();
    jsonTreeWriter2.setLenient(false);
    jsonTreeWriter2.value(Double.NaN);
    String jsonString1 = jsonTreeWriter1.toString();
    String jsonString2 = jsonTreeWriter2.toString();
    assertEquals(jsonString1, jsonString2);
    assertEquals(jsonString1, "{\"type\":\"null\",\"value\":null}");
  }

  @Test
  public void testJsonTreeWriter_writeToString() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    Writer writer = new OutputStreamWriter(outputStream);
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter(writer);
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value(JsonPrimitive.createNumber(1));
    jsonTreeWriter.value(JsonPrimitive.createNumber(2));
    jsonTreeWriter.endArray();
    jsonTreeWriter.close();
    String jsonString = outputStream.toString();
    assertEquals(jsonString, "[1,2]");
  }
}