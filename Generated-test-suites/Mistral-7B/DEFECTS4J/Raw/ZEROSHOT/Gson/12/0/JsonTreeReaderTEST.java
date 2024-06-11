import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeReaderTest {

  private static JsonTreeReader reader;
  private static JsonElement jsonElement;

  @BeforeClass
  public static void setUp() {
    String jsonString = "{\"name\": \"John\", \"age\": 30, \"cars\": [\"Ford\", \"BMW\", \"Fiat\"]}";
    jsonElement = new JsonElement() {
      @Override public JsonPrimitive getAsJsonPrimitive() {
        return null;
      }

      @Override public JsonArray getAsJsonArray() {
        return null;
      }

      @Override public JsonObject getAsJsonObject() {
        return new JsonObject() {
          {
            addProperty("name", "John");
            addProperty("age", 30);
            add("cars", new JsonArray() {
              {
                add("Ford");
                add("BMW");
                add("Fiat");
              }
            });
          }
        };
      }

      @Override public String getAsString() {
        return jsonString;
      }
    }.getAsJsonObject();

    reader = new JsonTreeReader(jsonElement);
  }

  @Test
  public void testBeginArray() throws IOException {
    reader.beginArray();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getAsInt());
    reader.endArray();
  }

  @Test(expected = IOException.class)
  public void testBeginArrayTwice() throws IOException {
    reader.beginArray();
    reader.beginArray();
  }

  @Test
  public void testEndArray() throws IOException {
    reader.beginArray();
    reader.nextElement();
    reader.endArray();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().getAsInt());
  }

  @Test(expected = IOException.class)
  public void testEndArrayWithoutBeginArray() throws IOException {
    reader.nextString();
    reader.endArray();
  }

  @Test
  public void testBeginObject() throws IOException {
    reader.beginObject();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().getAsInt());
    reader.endObject();
  }

  @Test(expected = IOException.class)
  public void testBeginObjectTwice() throws IOException {
    reader.beginObject();
    reader.beginObject();
  }

  @Test
  public void testEndObject() throws IOException {
    reader.beginObject();
    reader.nextName();
    reader.endObject();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getAsInt());
  }

  @Test(expected = IOException.class)
  public void testEndObjectWithoutBeginObject() throws IOException {
    reader.nextString();
    reader.endObject();
  }

  @Test
  public void testHasNext() throws IOException {
    reader.beginArray();
    reader.nextElement();
    reader.beginObject();
    reader.nextName();
    reader.nextString();
    reader.nextBoolean();
    reader.nextNull();
    reader.endObject();
    reader.nextElement();
    reader.endArray();

    assertTrue(reader.hasNext());
    assertFalse(reader.hasNext());
  }

  @Test
  public void testPeek() throws IOException {
    reader.beginArray();
    reader.nextElement();
    reader.beginObject();
    reader.nextName();
    reader.nextString();
    reader.nextBoolean();
    reader.nextNull();
    reader.endObject();
    reader.nextElement();
    reader.endArray();

    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().getAsInt());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek(1).getAsInt());
    assertEquals(JsonToken.NULL, reader.peek(2).getAsInt());
    assertEquals(JsonToken.END_DOCUMENT, reader.peek(3).getAsInt());
  }

  @Test
  public void testNextName() throws IOException {
    reader.beginArray();
    reader.nextElement();
    reader.beginObject();
    reader.nextName();
    assertEquals("name", reader.nextString());
    assertEquals("age", reader.nextName());
    reader.endObject();
    assertEquals("cars", reader.nextName());
    reader.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testNextNameWithoutBeginObject() throws IOException {
    reader.nextName();
  }

  @Test
  public void testNextString() throws IOException {
    reader.beginArray();
    reader.nextElement();
    reader.beginObject();
    reader.nextName();
    reader.nextString();
    assertEquals("John", reader.nextString());
    reader.nextString();
    assertEquals("Ford", reader.nextString());
    reader.endObject();
    reader.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testNextStringWithoutBeginArray() throws IOException {
    reader.nextString();
  }

  @Test
  public void testNextBoolean() throws IOException {
    reader.beginArray();
    reader.nextElement();
    reader.beginObject();
    reader.nextName();
    reader.nextBoolean();
    assertEquals(true, reader.nextBoolean());
    reader.endObject();
    reader.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testNextBooleanWithoutBeginObject() throws IOException {
    reader.nextBoolean();
  }

  @Test
  public void testNextNull() throws IOException {
    reader.beginArray();
    reader.nextElement();
    reader.beginObject();
    reader.nextName();
    reader.nextNull();
    reader.nextNull();
    reader.endObject();
    reader.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testNextNullWithoutBeginObject() throws IOException {
    reader.nextNull();
  }

  @Test
  public void testNextDouble() throws IOException {
    reader.beginArray();
    reader.nextElement();
    reader.beginObject();
    reader.nextName();
    reader.nextDouble();
    assertEquals(30.0, reader.nextDouble(), 0.0);
    reader.endObject();
    reader.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testNextDoubleWithoutBeginArray() throws IOException {
    reader.nextDouble();
  }

  @Test
  public void testNextLong() throws IOException {
    reader.beginArray();
    reader.nextElement();
    reader.beginObject();
    reader.nextName();
    reader.nextLong();
    assertEquals(30L, reader.nextLong());
    reader.endObject();
    reader.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testNextLongWithoutBeginArray() throws IOException {
    reader.nextLong();
  }

  @Test
  public void testNextInt() throws IOException {
    reader.beginArray();
    reader.nextElement();
    reader.beginObject();
    reader.nextName();
    reader.nextInt();
    assertEquals(30, reader.nextInt());
    reader.endObject();
    reader.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testNextIntWithoutBeginArray() throws IOException {
    reader.nextInt();
  }

  @Test
  public void testClose() throws IOException {
    reader.close();
    assertFalse(reader.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testCloseTwice() throws IOException {
    reader.close();
    reader.close();
  }

  @Test
  public void testSkipValue() throws IOException {
    reader.beginArray();
    reader.nextElement();
    reader.beginObject();
    reader.nextName();
    reader.nextString();
    reader.nextBoolean();
    reader.nextNull();
    reader.endObject();
    reader.nextElement();
    reader.skipValue();
    reader.endArray();

    assertEquals("null", reader.nextString());
  }

  @Test(expected = IllegalStateException.class)
  public void testSkipValueWithoutBeginArray() throws IOException {
    reader.skipValue();
  }
}