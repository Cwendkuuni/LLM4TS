import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;

public class JsonTreeReaderTest {

  @org.junit.Test
  public void testEmptyJsonElement() throws Exception {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(JsonElement.NULL);
    assertFalse(jsonTreeReader.hasNext());
    jsonTreeReader.close();
  }

  @org.junit.Test
  public void testArrayWithSingleElement() throws Exception {
    String jsonString = "[1]";
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonElement() {
      @Override
      public JsonArray getAsJsonArray() {
        return new JsonArray() {{
          add(new JsonPrimitive(1));
        }};
      }

      @Override
      public JsonObject getAsJsonObject() {
        throw new UnsupportedOperationException();
      }

      @Override
      public String getAsString() {
        return jsonString;
      }
    });

    jsonTreeReader.beginArray();
    JsonElement element = jsonTreeReader.nextElement();
    jsonTreeReader.endArray();
    jsonTreeReader.close();

    assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek().getAsByte());
    assertEquals(1, jsonTreeReader.nextInt());
    jsonTreeReader.close();

    assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek().getAsByte());
    assertFalse(jsonTreeReader.hasNext());
  }

  @org.junit.Test
  public void testArrayWithMultipleElements() throws Exception {
    String jsonString = "[1, 2, 3]";
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonElement() {
      @Override
      public JsonArray getAsJsonArray() {
        return new JsonArray() {{
          add(new JsonPrimitive(1));
          add(new JsonPrimitive(2));
          add(new JsonPrimitive(3));
        }};
      }

      @Override
      public JsonObject getAsJsonObject() {
        throw new UnsupportedOperationException();
      }

      @Override
      public String getAsString() {
        return jsonString;
      }
    });

    jsonTreeReader.beginArray();
    for (int i = 0; i < 3; i++) {
      assertEquals(JsonToken.NUMBER, jsonTreeReader.peek().getAsByte());
      jsonTreeReader.nextInt();
    }
    jsonTreeReader.endArray();
    jsonTreeReader.close();

    assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek().getAsByte());
    assertFalse(jsonTreeReader.hasNext());
  }

  @org.junit.Test
  public void testObjectWithSingleProperty() throws Exception {
    String jsonString = "{\"name\": \"John\"}";
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonElement() {
      @Override
      public JsonArray getAsJsonArray() {
        throw new UnsupportedOperationException();
      }

      @Override
      public JsonObject getAsJsonObject() {
        return new JsonObject() {{
          addProperty("name", new JsonPrimitive("John"));
        }};
      }

      @Override
      public String getAsString() {
        return jsonString;
      }
    });

    jsonTreeReader.beginObject();
    assertEquals("name", jsonTreeReader.nextName());
    assertEquals("John", jsonTreeReader.nextString());
    jsonTreeReader.endObject();
    jsonTreeReader.close();

    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek().getAsByte());
    assertFalse(jsonTreeReader.hasNext());
  }

  @org.junit.Test
  public void testObjectWithMultipleProperties() throws Exception {
    String jsonString = "{\"name\": \"John\", \"age\": 30}";
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonElement() {
      @Override
      public JsonArray getAsJsonArray() {
        throw new UnsupportedOperationException();
      }

      @Override
      public JsonObject getAsJsonObject() {
        return new JsonObject() {{
          addProperty("name", new JsonPrimitive("John"));
          addProperty("age", new JsonPrimitive(30));
        }};
      }

      @Override
      public String getAsString() {
        return jsonString;
      }
    });

    jsonTreeReader.beginObject();
    assertEquals("name", jsonTreeReader.nextName());
    assertEquals("John", jsonTreeReader.nextString());
    assertEquals("age", jsonTreeReader.nextName());
    assertEquals(30, jsonTreeReader.nextInt());
    jsonTreeReader.endObject();
    jsonTreeReader.close();

    assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek().getAsByte());
    assertFalse(jsonTreeReader.hasNext());
  }

  @org.junit.Test
  public void testNullJsonElement() throws Exception {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(JsonElement.NULL);
    jsonTreeReader.nextNull();
    jsonTreeReader.close();

    assertEquals(JsonToken.NULL, jsonTreeReader.peek().getAsByte());
    assertFalse(jsonTreeReader.hasNext());
  }

  @org.junit.Test
  public void testNumberAsInt() throws Exception {
    String jsonString = "42";
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonElement() {
      @Override
      public JsonArray getAsJsonArray() {
        throw new UnsupportedOperationException();
      }

      @Override
      public JsonObject getAsJsonObject() {
        throw new UnsupportedOperationException();
      }

      @Override
      public String getAsString() {
        return jsonString;
      }
    });

    jsonTreeReader.nextInt();
    jsonTreeReader.close();

    assertEquals(JsonToken.NUMBER, jsonTreeReader.peek().getAsByte());
    assertFalse(jsonTreeReader.hasNext());
  }

  @org.junit.Test
  public void testNumberAsLong() throws Exception {
    String jsonString = "42";
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonElement() {
      @Override
      public JsonArray getAsJsonArray() {
        throw new UnsupportedOperationException();
      }

      @Override
      public JsonObject getAsJsonObject() {
        throw new UnsupportedOperationException();
      }

      @Override
      public String getAsString() {
        return jsonString;
      }

      @Override
      public long getAsLong() {
        return 42;
      }
    });

    jsonTreeReader.nextLong();
    jsonTreeReader.close();

    assertEquals(JsonToken.NUMBER, jsonTreeReader.peek().getAsByte());
    assertFalse(jsonTreeReader.hasNext());
  }

  @org.junit.Test
  public void testNumberAsDouble() throws Exception {
    String jsonString = "3.14";
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonElement() {
      @Override
      public JsonArray getAsJsonArray() {
        throw new UnsupportedOperationException();
      }

      @Override
      public JsonObject getAsJsonObject() {
        throw new UnsupportedOperationException();
      }

      @Override
      public String getAsString() {
        return jsonString;
      }

      @Override
      public double getAsDouble() {
        return 3.14;
      }
    });

    jsonTreeReader.nextDouble();
    jsonTreeReader.close();

    assertEquals(JsonToken.NUMBER, jsonTreeReader.peek().getAsByte());
    assertFalse(jsonTreeReader.hasNext());
  }

  @org.junit.Test
  public void testNumberAsString() throws Exception {
    String jsonString = "3.14";
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonElement() {
      @Override
      public JsonArray getAsJsonArray() {
        throw new UnsupportedOperationException();
      }

      @Override
      public JsonObject getAsJsonObject() {
        throw new UnsupportedOperationException();
      }

      @Override
      public String getAsString() {
        return jsonString;
      }

      @Override
      public JsonPrimitive getAsJsonPrimitive() {
        return new JsonPrimitive(3.14);
      }

      @Override
      public double getAsDouble() {
        throw new UnsupportedOperationException();
      }

      @Override
      public long getAsLong() {
        throw new UnsupportedOperationException();
      }

      @Override
      public int getAsInt() {
        throw new UnsupportedOperationException();
      }

      @Override
      public boolean getAsBoolean() {
        throw new UnsupportedOperationException();
      }
    });

    jsonTreeReader.nextString();
    jsonTreeReader.close();

    assertEquals(JsonToken.STRING, jsonTreeReader.peek().getAsByte());
    assertFalse(jsonTreeReader.hasNext());
  }

  @org.junit.Test
  public void testBooleanTrue() throws Exception {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonPrimitive(true));
    jsonTreeReader.nextBoolean();
    jsonTreeReader.close();

    assertEquals(JsonToken.BOOLEAN, jsonTreeReader.peek().getAsByte());
    assertFalse(jsonTreeReader.hasNext());
  }

  @org.junit.Test
  public void testBooleanFalse() throws Exception {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonPrimitive(false));
    jsonTreeReader.nextBoolean();
    jsonTreeReader.close();

    assertEquals(JsonToken.BOOLEAN, jsonTreeReader.peek().getAsByte());
    assertFalse(jsonTreeReader.hasNext());
  }

  @org.junit.Test
  public void testString() throws Exception {
    String jsonString = "John Doe";
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonElement() {
      @Override
      public JsonArray getAsJsonArray() {
        throw new UnsupportedOperationException();
      }

      @Override
      public JsonObject getAsJsonObject() {
        throw new UnsupportedOperationException();
      }

      @Override
      public String getAsString() {
        return jsonString;
      }
    });

    jsonTreeReader.nextString();
    jsonTreeReader.close();

    assertEquals(JsonToken.STRING, jsonTreeReader.peek().getAsByte());
    assertFalse(jsonTreeReader.hasNext());
  }
}