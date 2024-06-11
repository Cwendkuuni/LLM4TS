import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeReaderTest {

  @Test
  public void testBeginArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonArray getAsJsonArray() {
        return new JsonArray();
      }

      @Override public JsonObject getAsJsonObject() {
        throw new UnsupportedOperationException();
      }

      @Override public JsonPrimitive getAsJsonPrimitive() {
        throw new UnsupportedOperationException();
      }

      @Override public String getAsString() {
        throw new UnsupportedOperationException();
      }

      @Override public Number getAsNumber() {
        throw new UnsupportedOperationException();
      }

      @Override public boolean getAsBoolean() {
        throw new UnsupportedOperationException();
      }

      @Override public JsonNull getAsJsonNull() {
        throw new UnsupportedOperationException();
      }

      @Override public Iterator<Map.Entry<String, JsonElement>> getEntryIterator() {
        throw new UnsupportedOperationException();
      }
    });

    reader.beginArray();
    reader.endArray();

    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    assertEquals(JsonToken.END_ARRAY, reader.peek());
    reader.close();
  }

  @Test
  public void testBeginObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonArray getAsJsonArray() {
        throw new UnsupportedOperationException();
      }

      @Override public JsonObject getAsJsonObject() {
        return new JsonObject();
      }

      @Override public JsonPrimitive getAsJsonPrimitive() {
        throw new UnsupportedOperationException();
      }

      @Override public String getAsString() {
        throw new UnsupportedOperationException();
      }

      @Override public Number getAsNumber() {
        throw new UnsupportedOperationException();
      }

      @Override public boolean getAsBoolean() {
        throw new UnsupportedOperationException();
      }

      @Override public JsonNull getAsJsonNull() {
        throw new UnsupportedOperationException();
      }

      @Override public Iterator<Map.Entry<String, JsonElement>> getEntryIterator() {
        throw new UnsupportedOperationException();
      }
    });

    reader.beginObject();
    reader.endObject();

    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    assertEquals(JsonToken.END_OBJECT, reader.peek());
    reader.close();
  }

  @Test
  public void testHasNext() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonArray getAsJsonArray() {
        return new JsonArray();
      }

      @Override public JsonObject getAsJsonObject() {
        throw new UnsupportedOperationException();
      }

      @Override public JsonPrimitive getAsJsonPrimitive() {
        return new JsonPrimitive("test");
      }

      @Override public String getAsString() {
        throw new UnsupportedOperationException();
      }

      @Override public Number getAsNumber() {
        throw new UnsupportedOperationException();
      }

      @Override public boolean getAsBoolean() {
        throw new UnsupportedOperationException();
      }

      @Override public JsonNull getAsJsonNull() {
        throw new UnsupportedOperationException();
      }

      @Override public Iterator<Map.Entry<String, JsonElement>> getEntryIterator() {
        throw new UnsupportedOperationException();
      }

      @Override public boolean isJsonNull() {
        return false;
      }
    });

    reader.beginArray();
    reader.endArray();
    reader.beginObject();
    reader.endObject();
    reader.close();

    assertFalse(reader.hasNext());
  }

  @Test
  public void testPeek() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonArray getAsJsonArray() {
        return new JsonArray();
      }

      @Override public JsonObject getAsJsonObject() {
        return new JsonObject();
      }

      @Override public JsonPrimitive getAsJsonPrimitive() {
        return new JsonPrimitive("test");
      }

      @Override public String getAsString() {
        throw new UnsupportedOperationException();
      }

      @Override public Number getAsNumber() {
        throw new UnsupportedOperationException();
      }

      @Override public boolean getAsBoolean() {
        throw new UnsupportedOperationException();
      }

      @Override public JsonNull getAsJsonNull() {
        throw new UnsupportedOperationException();
      }

      @Override public Iterator<Map.Entry<String, JsonElement>> getEntryIterator() {
        throw new UnsupportedOperationException();
      }

      @Override public boolean isJsonNull() {
        return false;
      }
    });

    reader.beginArray();
    reader.add(new JsonPrimitive("test1"));
    reader.add(new JsonPrimitive("test2"));
    reader.endArray();
    reader.beginObject();
    reader.addProperty("name", "test");
    reader.addProperty("age", 10);
    reader.endObject();

    reader.close();

    reader.open();

    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    assertEquals(JsonToken.NULL, reader.peek()); // after closing the reader
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalStateException() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonArray getAsJsonArray() {
        return new JsonArray();
      }

      @Override public JsonObject getAsJsonObject() {
        throw new UnsupportedOperationException();
      }

      @Override public JsonPrimitive getAsJsonPrimitive() {
        throw new UnsupportedOperationException();
      }

      @Override public String getAsString() {
        throw new UnsupportedOperationException();
      }

      @Override public Number getAsNumber() {
        throw new UnsupportedOperationException();
      }

      @Override public boolean getAsBoolean() {
        throw new UnsupportedOperationException();
      }

      @Override public JsonNull getAsJsonNull() {
        throw new UnsupportedOperationException();
      }

      @Override public Iterator<Map.Entry<String, JsonElement>> getEntryIterator() {
        throw new UnsupportedOperationException();
      }

      @Override public boolean isJsonNull() {
        return false;
      }
    });

    reader.beginArray();
    reader.endArray();
    reader.close();

    reader.open();

    reader.next(); // should read 'null' after closing the reader
    reader.next(); // should throw an exception
  }
}