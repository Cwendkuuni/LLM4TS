import static org.junit.Assert.*;
import static com.google.gson.stream.JsonReader.LengthLimitReader;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

@RunWith(Parameterized.class)
public class JsonTreeReaderTest {

  private static final String JSON_NULL = "{}";
  private static final String JSON_BOOLEAN = "{\"foo\": true}";
  private static final String JSON_NUMBER = "{\"foo\": 1.2}";
  private static final String JSON_STRING = "{\"foo\": \"hello\"}";
  private static final String JSON_ARRAY = "{\"foo\": [1, 2, 3]}";
  private static final String JSON_OBJECT = "{\"foo\": {\"bar\": 1, \"baz\": [1, 2]}, \"qux\": 3}";
  private static final String JSON_INVALID = "{\"foo\": bar}";

  private JsonTreeReader reader;
  private JsonElement element;

  public JsonTreeReaderTest(JsonElement element) {
    this.element = element;
  }

  @Parameterized.Parameters(name = "{index}: JsonElement = {0}")
  public static Iterable<Object[]> data() {
    return Arrays.asList(new Object[][] {
        {JsonElement.parse(JSON_NULL)},
        {JsonElement.parse(JSON_BOOLEAN)},
        {JsonElement.parse(JSON_NUMBER)},
        {JsonElement.parse(JSON_STRING)},
        {JsonElement.parse(JSON_ARRAY)},
        {JsonElement.parse(JSON_OBJECT)}
    });
  }

  @Test
  public void testBeginArray() throws IOException {
    reader = new JsonTreeReader(element);
    reader.beginArray();
    assertTrue(reader.hasNext());
    reader.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayTwice() throws IOException {
    reader = new JsonTreeReader(element);
    reader.beginArray();
    reader.beginArray();
  }

  @Test
  public void testBeginObject() throws IOException {
    reader = new JsonTreeReader(element);
    reader.beginObject();
    assertTrue(reader.hasNext());
    reader.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectTwice() throws IOException {
    reader = new JsonTreeReader(element);
    reader.beginObject();
    reader.beginObject();
  }

  @Test
  public void testEndArray() throws IOException {
    reader = new JsonTreeReader(element);
    reader.beginArray();
    reader.endArray();
    assertFalse(reader.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayBeforeBeginArray() throws IOException {
    reader = new JsonTreeReader(element);
    reader.endArray();
  }

  @Test
  public void testEndObject() throws IOException {
    reader = new JsonTreeReader(element);
    reader.beginObject();
    reader.endObject();
    assertFalse(reader.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectBeforeBeginObject() throws IOException {
    reader = new JsonTreeReader(element);
    reader.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testHasNextAfterEndDocument() throws IOException {
    reader = new JsonTreeReader(JsonElement.parse(new StringReader("")));
    assertFalse(reader.hasNext());
    reader.next();
  }

  @Test
  public void testNextArrayElement() throws IOException {
    reader = new JsonTreeReader(element);
    reader.beginArray();
    JsonElement actual = reader.nextElement();
    JsonElement expected = JsonElement.parse(JSON_NULL).getAsJsonArray().get(0);
    assertEquals(expected, actual);
    reader.endArray();
  }

  @Test
  public void testNextArrayElementWhenObjectExpected() throws IOException {
    reader = new JsonTreeReader(new JsonObject());
    reader.beginArray();
    try {
      reader.nextElement();
      fail();
    } catch (IllegalStateException e) {
      // expected
    }
    reader.endArray();
  }

  @Test
  public void testNextBoolean() throws IOException {
    reader = new JsonTreeReader(element);
    reader.beginObject();
    reader.name("foo");
    reader.beginObject();
    reader.name("bar");
    reader.value(new JsonPrimitive(true));
    reader.endObject();
    reader.endObject();
    assertTrue(reader.hasNextBoolean());
    assertTrue(reader.nextBoolean());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextBooleanBeforeBoolean() throws IOException {
    reader = new JsonTreeReader(element);
    reader.nextBoolean();
  }

  @Test(expected = IllegalStateException.class)
  public void testNextBooleanAfterEndOfDocument() throws IOException {
    reader = new JsonTreeReader(JsonElement.parse(new StringReader("")));
    reader.nextBoolean();
  }

  @Test
  public void testNextInt() throws IOException {
    reader = new JsonTreeReader(element);
    reader.beginObject();
    reader.name("foo");
    reader.beginObject();
    reader.name("bar");
    reader.value(new JsonPrimitive(1));
    reader.endObject();
    reader.endObject();
    assertEquals(1, reader.nextInt());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextIntBeforeInt() throws IOException {
    reader = new JsonTreeReader(element);
    reader.nextInt();
  }

  @Test(expected = IllegalStateException.class)
  public void testNextIntAfterEndOfDocument() throws IOException {
    reader = new JsonTreeReader(JsonElement.parse(new StringReader("")));
    reader.nextInt();
  }

  @Test
  public void testNextLong() throws IOException {
    reader = new JsonTreeReader(element);
    reader.beginObject();
    reader.name("foo");
    reader.beginObject();
    reader.name("bar");
    reader.value(new JsonPrimitive(1L));
    reader.endObject();
    reader.endObject();
    assertEquals(1L, reader.nextLong());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextLongBeforeLong() throws IOException {
    reader = new JsonTreeReader(element);
    reader.nextLong();
  }

  @Test(expected = IllegalStateException.class)
  public void testNextLongAfterEndOfDocument() throws IOException {
    reader = new JsonTreeReader(JsonElement.parse(new StringReader("")));
    reader.nextLong();
  }

  @Test
  public void testNextNull() throws IOException {
    reader = new JsonTreeReader(element);
    reader.beginObject();
    reader.name("foo");
    reader.value(JsonNull.INSTANCE);
    reader.endObject();
    assertTrue(reader.hasNextNull());
    reader.nextNull();
  }

  @Test(expected = IllegalStateException.class)
  public void testNextNullBeforeNull() throws IOException {
    reader = new JsonTreeReader(element);
    reader.nextNull();
  }

  @Test(expected = IllegalStateException.class)
  public void testNextNullAfterEndOfDocument() throws IOException {
    reader = new JsonTreeReader(JsonElement.parse(new StringReader("")));
    reader.nextNull();
  }

  @Test
  public void testNextString() throws IOException {
    reader = new JsonTreeReader(element);
    reader.beginObject();
    reader.name("foo");
    reader.beginObject();
    reader.name("bar");
    reader.value(new JsonPrimitive("hello"));
    reader.endObject();
    reader.endObject();
    assertEquals("hello", reader.nextString());
  }

  @Test(expected = IllegalStateException.class)
  public void testNextStringBeforeString() throws IOException {
    reader = new JsonTreeReader(element);
    reader.nextString();
  }

  @Test(expected = IllegalStateException.class)
  public void testNextStringAfterEndOfDocument() throws IOException {
    reader = new JsonTreeReader(JsonElement.parse(new StringReader("")));
    reader.nextString();
  }

  @Test
  public void testNextValue() throws IOException {
    reader = new JsonTreeReader(element);
    reader.beginObject();
    reader.name("foo");
    reader.beginObject();
    reader.name("bar");
    reader.value(new JsonPrimitive(1));
    reader.endObject();
    reader.endObject();
    JsonElement actual = reader.nextValue();
    JsonElement expected = JsonElement.parse(JSON_NUMBER);
    assertEquals(expected, actual);
  }

  @Test
  public void testNextValueWhenExpectedString() throws IOException {
    reader = new JsonTreeReader(element);
    reader.beginObject();
    reader.name("foo");
    reader.beginObject();
    reader.name("bar");
    reader.value(new JsonPrimitive("hello"));
    reader.endObject();
    reader.endObject();
    try {
      reader.nextString();
      reader.nextValue();
      fail();
    } catch (IllegalStateException e) {
      // expected
    }
  }

  @Test
  public void testReadNull() throws IOException {
    reader = new JsonTreeReader(JsonElement.parse(new StringReader(JSON_NULL)));
    reader.nextNull();
    reader.close();
  }

  @Test(expected = JsonSyntaxException.class)
  public void testReadInvalidJson() throws IOException {
    reader = new JsonTreeReader(new StringReader(JSON_INVALID));
    reader.close();
  }

  @Test
  public void testReadLengthLimitedJson() throws IOException {
    reader = new JsonTreeReader(new JsonElement().getAsJsonArray().get(0));
    try {
      reader = new JsonTreeReader(new LengthLimitReader(new StringReader(JSON_OBJECT), 0));
      reader.nextName();
      reader.beginObject();
      reader.name("foo");
      reader.name("bar");
      reader.endObject();
      fail();
    } catch (IOException e) {
      // expected
    }
  }

  @Test
  public void testReadStringEmpty() throws IOException {
    reader = new JsonTreeReader(JsonElement.parse(new StringReader("")));
    reader.close();
  }
}