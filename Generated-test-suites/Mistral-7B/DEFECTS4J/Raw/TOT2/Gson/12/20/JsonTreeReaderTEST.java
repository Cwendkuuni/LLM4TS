import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;

public class JsonTreeReaderTest {

  @Test
  public void testEmptyJsonElement() {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.NULL);
    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testSimpleJsonObject() {
    String jsonString = "{\"name\": \"John\", \"age\": 30}";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override
      public JsonObject getAsJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John");
        jsonObject.addProperty("age", 30);
        return jsonObject;
      }

      @Override
      public JsonArray getAsJsonArray() {
        throw new UnsupportedOperationException();
      }

      @Override
      public JsonPrimitive getAsJsonPrimitive() {
        throw new UnsupportedOperationException();
      }

      @Override
      public JsonNull getAsJsonNull() {
        throw new UnsupportedOperationException();
      }

      @Override
      public String getAsString() {
        return jsonString;
      }
    });

    assertEquals("name", reader.nextName());
    assertEquals("John", reader.nextString());
    assertEquals("age", reader.nextName());
    assertEquals(30, reader.nextInt());
    reader.nextNull(); // consume null value
    reader.nextBoolean(); // should throw an exception
    reader.close();
  }

  @Test
  public void testSimpleJsonArray() {
    String jsonString = "[1, \"two\", 3.0]";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override
      public JsonObject getAsJsonObject() {
        throw new UnsupportedOperationException();
      }

      @Override
      public JsonArray getAsJsonArray() {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive(1));
        jsonArray.add(new JsonPrimitive("two"));
        jsonArray.add(new JsonPrimitive(3.0));
        return jsonArray;
      }

      @Override
      public JsonPrimitive getAsJsonPrimitive() {
        throw new UnsupportedOperationException();
      }

      @Override
      public JsonNull getAsJsonNull() {
        throw new UnsupportedOperationException();
      }

      @Override
      public String getAsString() {
        return jsonString;
      }
    });

    reader.beginArray();
    assertEquals(1, reader.nextInt());
    reader.nextString(); // consume "two"
    assertEquals(3.0, reader.nextDouble(), 0.001);
    reader.endArray();
    reader.nextBoolean(); // should throw an exception
    reader.close();
  }

  @Test
  public void testComplexJsonObject() {
    String jsonString = "{\"name\": {\"first\": \"John\", \"last\": \"Doe\"}, \"age\": 30, \"hobbies\": [\"reading\", \"swimming\", null]}";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override
      public JsonObject getAsJsonObject() {
        JsonObject jsonObject = new JsonObject();
        JsonObject innerObject = new JsonObject();
        innerObject.addProperty("first", "John");
        innerObject.addProperty("last", "Doe");
        jsonObject.add("name", innerObject);
        jsonObject.addProperty("age", 30);
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive("reading"));
        jsonArray.add(new JsonPrimitive("swimming"));
        jsonArray.add(JsonNull.INSTANCE);
        jsonObject.add("hobbies", jsonArray);
        return jsonObject;
      }

      @Override
      public JsonArray getAsJsonArray() {
        throw new UnsupportedOperationException();
      }

      @Override
      public JsonPrimitive getAsJsonPrimitive() {
        throw new UnsupportedOperationException();
      }

      @Override
      public JsonNull getAsJsonNull() {
        throw new UnsupportedOperationException();
      }

      @Override
      public String getAsString() {
        return jsonString;
      }
    });

    assertEquals("name", reader.nextName());
    reader.beginObject();
    assertEquals("first", reader.nextName());
    assertEquals("John", reader.nextString());
    reader.nextName(); // consume "last"
    assertEquals("Doe", reader.nextString());
    reader.endObject();
    assertEquals("age", reader.nextName());
    assertEquals(30, reader.nextInt());
    reader.nextName(); // consume "hobbies"
    reader.beginArray();
    assertEquals(3, reader.getDepth()); // check depth
    assertEquals("reading", reader.nextString());
    assertEquals(null, reader.nextNull());
    reader.endArray();
    reader.nextBoolean(); // should throw an exception
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonReaderClosedException() {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.NULL);
    reader.close();
    reader.nextBoolean();
  }

  @Test(expected = AssertionError.class)
  public void testJsonReaderUnexpectedTokenException() {
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override
      public JsonObject getAsJsonObject() {
        throw new UnsupportedOperationException();
      }

      @Override
      public JsonArray getAsJsonArray() {
        throw new UnsupportedOperationException();
      }

      @Override
      public JsonPrimitive getAsJsonPrimitive() {
        throw new UnsupportedOperationException();
      }

      @Override
      public JsonNull getAsJsonNull() {
        throw new UnsupportedOperationException();
      }

      @Override
      public String getAsString() {
        return "{\"name\": 1}";
      }
    });
    reader.expect(JsonToken.STRING);
  }
}