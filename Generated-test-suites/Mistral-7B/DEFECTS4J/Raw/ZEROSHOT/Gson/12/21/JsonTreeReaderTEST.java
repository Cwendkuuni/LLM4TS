import org.junit.Assert;
import org.junit.Test;

import static com.google.gson.internal.bind.JsonTreeReader.JsonToken.*;

public class JsonTreeReaderTest {

  @Test
  public void testJsonTreeReaderEmpty() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.NULL);
    Assert.assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testJsonTreeReaderArray() throws IOException {
    String json = "[1, 2, 3]";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonArray getAsJsonArray() {
        return new JsonArray() {{
          add(new JsonPrimitive(1));
          add(new JsonPrimitive(2));
          add(new JsonPrimitive(3));
        }};
      }

      @Override public JsonObject getAsJsonObject() {
        throw new UnsupportedOperationException();
      }

      @Override public JsonPrimitive getAsJsonPrimitive() {
        throw new UnsupportedOperationException();
      }

      @Override public JsonNull getAsJsonNull() {
        throw new UnsupportedOperationException();
      }
    });

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(BEGIN_ARRAY, reader.peek().getAsInt());
    reader.beginArray();

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(NUMBER, reader.peek().getAsInt());
    reader.nextInt();

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(NUMBER, reader.peek().getAsInt());
    reader.nextInt();

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(NUMBER, reader.peek().getAsInt());
    reader.nextInt();

    Assert.assertEquals(END_ARRAY, reader.peek().getAsInt());
    reader.endArray();

    Assert.assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testJsonTreeReaderObject() throws IOException {
    String json = "{\"name\": \"John\", \"age\": 30}";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonArray getAsJsonArray() {
        throw new UnsupportedOperationException();
      }

      @Override public JsonObject getAsJsonObject() {
        return new JsonObject() {{
          addProperty("name", new JsonPrimitive("John"));
          addProperty("age", new JsonPrimitive(30));
        }};
      }

      @Override public JsonPrimitive getAsJsonPrimitive() {
        throw new UnsupportedOperationException();
      }

      @Override public JsonNull getAsJsonNull() {
        throw new UnsupportedOperationException();
      }
    });

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(BEGIN_OBJECT, reader.peek().getAsInt());
    reader.beginObject();

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(NAME, reader.peek().getAsInt());
    reader.nextName();
    Assert.assertEquals("name", reader.nextString());

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(BEGIN_OBJECT, reader.peek().getAsInt());
    Assert.assertEquals("name", reader.nextName());

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(STRING, reader.peek().getAsInt());
    reader.nextString();
    Assert.assertEquals("John", reader.nextString());

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(NAME, reader.peek().getAsInt());
    reader.nextName();
    Assert.assertEquals("age", reader.nextString());

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(NUMBER, reader.peek().getAsInt());
    reader.nextInt();
    Assert.assertEquals(30, reader.nextInt());

    Assert.assertEquals(END_OBJECT, reader.peek().getAsInt());
    reader.endObject();

    Assert.assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testJsonTreeReaderNestedObjects() throws IOException {
    String json = "{\"name\": {\"first\": \"John\", \"last\": \"Doe\"}}";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonArray getAsJsonArray() {
        throw new UnsupportedOperationException();
      }

      @Override public JsonObject getAsJsonObject() {
        return new JsonObject() {{
          addProperty("name", new JsonObject() {{
            addProperty("first", new JsonPrimitive("John"));
            addProperty("last", new JsonPrimitive("Doe"));
          }});
        }};
      }

      @Override public JsonPrimitive getAsJsonPrimitive() {
        throw new UnsupportedOperationException();
      }

      @Override public JsonNull getAsJsonNull() {
        throw new UnsupportedOperationException();
      }
    });

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(BEGIN_OBJECT, reader.peek().getAsInt());
    reader.beginObject();

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(NAME, reader.peek().getAsInt());
    reader.nextName();
    Assert.assertEquals("name", reader.nextString());

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(BEGIN_OBJECT, reader.peek().getAsInt());
    reader.beginObject();

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(NAME, reader.peek().getAsInt());
    reader.nextName();
    Assert.assertEquals("first", reader.nextString());

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(STRING, reader.peek().getAsInt());
    reader.nextString();
    Assert.assertEquals("John", reader.nextString());

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(NAME, reader.peek().getAsInt());
    reader.nextName();
    Assert.assertEquals("last", reader.nextString());

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(STRING, reader.peek().getAsInt());
    reader.nextString();
    Assert.assertEquals("Doe", reader.nextString());

    Assert.assertEquals(END_OBJECT, reader.peek().getAsInt());
    reader.endObject();

    Assert.assertEquals(END_OBJECT, reader.peek().getAsInt());
    reader.endObject();

    Assert.assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testJsonTreeReaderNestedArrays() throws IOException {
    String json = "[1, [2, [3, 4], [5]]]";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonArray getAsJsonArray() {
        return new JsonArray() {{
          add(new JsonPrimitive(1));
          add(new JsonArray() {{
            add(new JsonPrimitive(2));
            add(new JsonArray() {{
              add(new JsonPrimitive(3));
              add(new JsonPrimitive(4));
            }});
            add(new JsonArray() {{
              add(new JsonPrimitive(5));
            }});
          }});
        }};
      }

      @Override public JsonObject getAsJsonObject() {
        throw new UnsupportedOperationException();
      }

      @Override public JsonPrimitive getAsJsonPrimitive() {
        throw new UnsupportedOperationException();
      }

      @Override public JsonNull getAsJsonNull() {
        throw new UnsupportedOperationException();
      }
    });

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(BEGIN_ARRAY, reader.peek().getAsInt());
    reader.beginArray();

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(NUMBER, reader.peek().getAsInt());
    reader.nextInt();

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(BEGIN_ARRAY, reader.peek().getAsInt());
    reader.beginArray();

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(NUMBER, reader.peek().getAsInt());
    reader.nextInt();

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(BEGIN_ARRAY, reader.peek().getAsInt());
    reader.beginArray();

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(NUMBER, reader.peek().getAsInt());
    reader.nextInt();

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(NUMBER, reader.peek().getAsInt());
    reader.nextInt();

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(END_ARRAY, reader.peek().getAsInt());
    reader.endArray();

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(BEGIN_ARRAY, reader.peek().getAsInt());
    reader.beginArray();

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(NUMBER, reader.peek().getAsInt());
    reader.nextInt();

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(BEGIN_ARRAY, reader.peek().getAsInt());
    reader.beginArray();

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(NUMBER, reader.peek().getAsInt());
    reader.nextInt();

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(NUMBER, reader.peek().getAsInt());
    reader.nextInt();

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(END_ARRAY, reader.peek().getAsInt());
    reader.endArray();

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(BEGIN_ARRAY, reader.peek().getAsInt());
    reader.beginArray();

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(NUMBER, reader.peek().getAsInt());
    reader.nextInt();

    Assert.assertEquals(END_ARRAY, reader.peek().getAsInt());
    reader.endArray();

    Assert.assertEquals(END_ARRAY, reader.peek().getAsInt());
    reader.endArray();

    Assert.assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testJsonTreeReaderNulls() throws IOException {
    String json = "null";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      @Override public JsonArray getAsJsonArray() {
        throw new UnsupportedOperationException();
      }

      @Override public JsonObject getAsJsonObject() {
        throw new UnsupportedOperationException();
      }

      @Override public JsonPrimitive getAsJsonPrimitive() {
        return new JsonPrimitive(null);
      }

      @Override public JsonNull getAsJsonNull() {
        return JsonNull.INSTANCE;
      }
    });

    Assert.assertTrue(reader.hasNext());
    Assert.assertEquals(NULL, reader.peek().getAsInt());
    reader.nextNull();

    Assert.assertFalse(reader.hasNext());
    reader.close();
  }
}