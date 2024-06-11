import static org.junit.Assert.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonReader;
import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testJsonTreeReaderConstruction() {
    JsonElement jsonElement = new JsonPrimitive(new JsonArray());
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    assertEquals("JsonTreeReader", reader.toString());
    reader.close();
  }

  @Test
  public void testBeginArray() throws IOException {
    JsonElement jsonElement = new JsonPrimitive(new JsonArray());
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginArray();
    assertFalse(reader.hasNext());
    reader.endArray();
    reader.close();
  }

  @Test
  public void testEndArray() throws IOException {
    JsonElement jsonElement = new JsonPrimitive(new JsonArray().add(new JsonPrimitive("foo"))
        .add(new JsonPrimitive("bar")));
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginArray();
    reader.nextString();
    reader.endArray();
    reader.nextString();
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayAfterEndArray() throws IOException {
    JsonElement jsonElement = new JsonPrimitive(new JsonArray());
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginArray();
    reader.endArray();
    reader.beginArray();
  }

  @Test
  public void testBeginObject() throws IOException {
    JsonElement jsonElement = new JsonPrimitive(new JsonObject().addProperty("foo", new JsonPrimitive("bar"))
        .addProperty("baz", new JsonArray().add(new JsonPrimitive("qux"))));
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    assertEquals("foo", reader.nextName());
    assertEquals("bar", reader.nextString());
    reader.beginArray();
    assertEquals("baz", reader.nextName());
    assertEquals("qux", reader.nextString());
    reader.endArray();
    reader.endObject();
    reader.close();
  }

  @Test
  public void testEndObject() throws IOException {
    JsonElement jsonElement = new JsonPrimitive(new JsonObject().addProperty("foo", new JsonPrimitive("bar"))
        .addProperty("baz", new JsonPrimitive("qux")));
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName();
    reader.nextString();
    reader.nextName();
    reader.nextString();
    reader.endObject();
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectAfterEndObject() throws IOException {
    JsonElement jsonElement = new JsonPrimitive(new JsonObject().addProperty("foo", new JsonPrimitive("bar")));
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName();
    reader.nextString();
    reader.endObject();
    reader.endObject();
  }

  @Test
  public void testHasNext() throws IOException {
    JsonElement jsonElement = new JsonPrimitive(new JsonArray().add(new JsonPrimitive("foo"))
        .add(new JsonPrimitive("bar")));
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginArray();
    assertTrue(reader.hasNext());
    reader.nextString();
    assertTrue(reader.hasNext());
    reader.nextString();
    assertFalse(reader.hasNext());
    reader.endArray();
    reader.close();
  }

  @Test
  public void testPeek() throws IOException {
    JsonElement jsonElement = new JsonPrimitive(new JsonObject().addProperty("foo", new JsonPrimitive("bar"))
        .addProperty("baz", new JsonPrimitive(123)));
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName();
    reader.expect(JsonToken.NAME);
    reader.nextName();
    reader.expect(JsonToken.NUMBER);
    reader.nextInt();
    reader.endObject();
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testPeekAfterEndDocument() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(UNREADABLE_READER);
    reader.peek();
  }

  @Test
  public void testPopStack() throws IOException {
    JsonElement jsonElement = new JsonPrimitive(new JsonObject().addProperty("foo", new JsonPrimitive("bar"))
        .addProperty("baz", new JsonPrimitive("qux"))).add("quux", new JsonArray());
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName();
    reader.nextString();
    reader.nextName();
    reader.nextString();
    reader.beginArray();
    reader.nextString();
    reader.endArray();
    reader.endObject();
    reader.close();

    reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName();
    reader.nextString();
    reader.nextName();
    reader.nextString();
    reader.beginArray();
    reader.popStack();
    reader.popStack();
    assertFalse(reader.hasNext());
    reader.endArray();
    reader.endObject();
    reader.close();
  }

  @Test
  public void testExpect() throws IOException {
    JsonElement jsonElement = new JsonPrimitive(new JsonObject().addProperty("foo", new JsonPrimitive("bar"))
        .addProperty("baz", new JsonPrimitive("qux"))).add("quux", new JsonArray());
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName();
    reader.nextString();
    reader.nextName();
    reader.nextString();
    reader.beginArray();
    reader.nextString();
    reader.expect(JsonToken.END_ARRAY);
    reader.expect(JsonToken.END_OBJECT);
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testExpectAfterEndDocument() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(UNREADABLE_READER);
    reader.expect(JsonToken.BEGIN_ARRAY);
  }

  @Test
  public void testNextName() throws IOException {
    JsonElement jsonElement = new JsonPrimitive(new JsonObject().addProperty("foo", new JsonPrimitive("bar"))
        .addProperty("baz", new JsonPrimitive("qux"))).addProperty("quux", new JsonPrimitive("quux"));
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    assertEquals("foo", reader.nextName());
    assertEquals("bar", reader.nextString());
    assertEquals("baz", reader.nextName());
    assertEquals("qux", reader.nextString());
    assertEquals("quux", reader.nextName());
    assertEquals("quux", reader.nextString());
    reader.endObject();
    reader.close();
  }

  @Test
  public void testNextString() throws IOException {
    JsonElement jsonElement = new JsonPrimitive(new JsonObject().addProperty("foo", new JsonPrimitive("bar"))
        .addProperty("baz", new JsonPrimitive("qux"))).addProperty("quux", new JsonPrimitive("quux"));
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName();
    reader.nextString();
    reader.nextName();
    reader.nextString();
    reader.nextName();
    reader.nextString();
    reader.endObject();
    reader.close();
  }

  @Test
  public void testNextBoolean() throws IOException {
    JsonElement jsonElement = new JsonPrimitive(new JsonObject().addProperty("foo", new JsonPrimitive(true))
        .addProperty("baz", new JsonPrimitive(false)));
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName();
    assertTrue(reader.nextBoolean());
    reader.nextName();
    assertFalse(reader.nextBoolean());
    reader.endObject();
    reader.close();
  }

  @Test
  public void testNextNull() throws IOException {
    JsonElement jsonElement = new JsonPrimitive(new JsonObject()
        .addProperty("foo", new JsonPrimitive("bar"))
        .addProperty("baz", new JsonPrimitive(null)));
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName();
    reader.nextString();
    reader.nextName();
    reader.nextNull();
    reader.endObject();
    reader.close();
  }

  @Test
  public void testNextDouble() throws IOException {
    JsonElement jsonElement = new JsonPrimitive(new JsonObject().addProperty("foo", new JsonPrimitive(3.14))
        .addProperty("baz", new JsonPrimitive(2.718)));
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName();
    reader.nextString();
    reader.nextName();
    reader.nextDouble();
    reader.nextDouble();
    reader.endObject();
    reader.close();
  }

  @Test
  public void testNextLong() throws IOException {
    JsonElement jsonElement = new JsonPrimitive(new JsonObject().addProperty("foo", new JsonPrimitive(42))
        .addProperty("baz", new JsonPrimitive(123456789012345L)));
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName();
    reader.nextString();
    reader.nextName();
    reader.nextLong();
    reader.nextLong();
    reader.endObject();
    reader.close();
  }

  @Test
  public void testNextInt() throws IOException {
    JsonElement jsonElement = new JsonPrimitive(new JsonObject().addProperty("foo", new JsonPrimitive(42))
        .addProperty("baz", new JsonPrimitive(1234567890)));
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName();
    reader.nextString();
    reader.nextName();
    reader.nextInt();
    reader.nextInt();
    reader.endObject();
    reader.close();
  }

  @Test
  public void testClose() throws IOException {
    JsonElement jsonElement = new JsonPrimitive(new JsonObject().addProperty("foo", new JsonPrimitive("bar"))
        .addProperty("baz", new JsonPrimitive("qux")));
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName();
    reader.nextString();
    reader.nextName();
    reader.nextString();
    reader.endObject();
    reader.close();
    reader.close();
  }

  @Test
  public void testSkipValue() throws IOException {
    JsonElement jsonElement = new JsonPrimitive(new JsonObject().addProperty("foo", new JsonPrimitive("bar"))
        .addProperty("baz", new JsonPrimitive(3.14)));
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName();
    reader.nextString();
    reader.nextName();
    reader.nextString();
    reader.skipValue();
    reader.nextString();
    reader.endObject();
    reader.close();
  }

  @Test
  public void testPromoteNameToValue() throws IOException {
    JsonElement jsonElement = new JsonPrimitive(new JsonObject().addProperty("foo", new JsonPrimitive("bar"))
        .addProperty("baz", new JsonPrimitive("qux")));
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    reader.nextName();
    reader.promoteNameToValue();
    assertEquals("bar", reader.nextString());
    reader.nextName();
    reader.promoteNameToValue();
    assertEquals("qux", reader.nextString());
    reader.endObject();
    reader.close();
  }
}