import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonToken;

public class JsonTreeReaderTest {

  private static String jsonString = "{\"foo\": [1, 2, 3], \"bar\": {\"qux\": 42, \"quux\": null}}";

  @Test
  public void testEmpty() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
    reader.close();
    assertEquals("", reader.getPath());
  }

  @Test
  public void testRootArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[]"));
    reader.close();
    assertEquals("", reader.getPath());

    reader = new JsonTreeReader(JsonElement.parse("[1, 2, 3]"));
    reader.beginArray();
    assertEquals(1, reader.nextInt());
    reader.endArray();
    assertEquals("$[]", reader.getPath());

    reader = new JsonTreeReader(JsonElement.parse("[null]"));
    reader.beginArray();
    reader.nextNull();
    reader.endArray();
    assertEquals("$[]", reader.getPath());
  }

  @Test
  public void testRootObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
    reader.close();
    assertEquals("", reader.getPath());

    reader = new JsonTreeReader(JsonElement.parse("{\"foo\": null}"));
    reader.beginObject();
    reader.nextName();
    reader.nextNull();
    reader.endObject();
    assertEquals("$.", "foo", reader.getPath());
  }

  @Test
  public void testComplexJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new StringReader(jsonString));
    reader.beginObject();
    reader.nextName();
    assertEquals("foo", reader.nextName());
    reader.beginArray();
    assertEquals(1, reader.nextInt());
    assertEquals(2, reader.nextInt());
    assertEquals(3, reader.nextInt());
    reader.endArray();
    reader.nextName();
    reader.beginObject();
    reader.nextName();
    assertEquals("qux", reader.nextName());
    assertEquals(42, reader.nextInt());
    reader.nextName();
    reader.nextNull();
    reader.endObject();
    reader.endObject();
    reader.close();
    assertEquals("$.", "foo[0]", reader.getPath());
    assertEquals("$.", "foo[1]", reader.getPath());
    assertEquals("$.", "foo[2]", reader.getPath());
    assertEquals("$.", "bar.qux", reader.getPath());
  }

  @Test(expected = IllegalStateException.class)
  public void testClosedReader() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
    reader.close();
    reader.peek();
  }

  @Test
  public void testBeginObjectTwice() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
    reader.beginObject();
    reader.beginObject();
    reader.close();
    assertEquals("$", reader.getPath());
  }

  @Test
  public void testEndObjectTwice() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
    reader.beginObject();
    reader.endObject();
    reader.endObject();
    reader.close();
    assertEquals("", reader.getPath());
  }

  @Test
  public void testBeginArrayTwice() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
    reader.beginArray();
    reader.beginArray();
    reader.close();
    assertEquals("$", reader.getPath());
  }

  @Test
  public void testEndArrayTwice() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
    reader.beginArray();
    reader.endArray();
    reader.endArray();
    reader.close();
    assertEquals("", reader.getPath());
  }

  @Test
  public void testBeginArrayWithObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{[1]}"));
    reader.beginArray();
    reader.nextNull();
    reader.beginArray();
    reader.nextInt(1);
    reader.endArray();
    reader.endArray();
    reader.close();
    assertEquals("$[].", "1", reader.getPath());
  }

  @Test
  public void testBeginObjectWithArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{[1]}"));
    reader.beginObject();
    reader.beginArray();
    reader.nextInt(1);
    reader.endArray();
    reader.endObject();
    reader.close();
    assertEquals("$.", "1", reader.getPath());
  }

  @Test
  public void testBeginObjectTwiceWithArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{[1]}"));
    reader.beginObject();
    reader.beginObject();
    reader.beginArray();
    reader.nextInt(1);
    reader.endArray();
    reader.endObject();
    reader.endObject();
    reader.close();
    assertEquals("$", reader.getPath());
  }

  @Test
  public void testBeginObjectWithComplexJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
    reader.beginObject();
    reader.nextName();
    reader.beginObject();
    reader.nextName();
    reader.nextString();
    reader.nextName();
    reader.nextNull();
    reader.endObject();
    reader.nextName();
    reader.endObject();
    reader.close();
    assertEquals("$", reader.getPath());
  }

  @Test
  public void testPeekAndPop() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
    reader.push(JsonElement.parse("\"foo\""));
    reader.push(JsonPrimitive.number(1));
    reader.push(JsonPrimitive.number(2));
    reader.push(JsonPrimitive.number(3));
    reader.push(JsonElement.parse("{[\"bar\"]}"));
    reader.beginObject();
    reader.nextName();
    reader.nextName();
    reader.nextString();
    reader.nextName();
    reader.nextNull();
    reader.endObject();
    reader.nextName();
    reader.beginObject();
    reader.nextName();
    reader.nextArray();
    reader.nextInt(1);
    reader.nextInt(2);
    reader.nextInt(3);
    reader.nextName();
    reader.nextString();
    reader.nextName();
    reader.nextNull();
    reader.endObject();
    reader.endObject();
    reader.endObject();
    reader.endObject();
    reader.endObject();
    reader.endObject();
    reader.endObject();
    reader.endObject();
    reader.endObject();
    reader.endObject();
    reader.endObject();
    reader.endObject();
    reader.close();
    assertEquals("$", reader.getPath());

    Object[] stack = reader.stack;
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("path", reader.getPath());
    map.put("stackSize", reader.stackSize);
    for (int i = reader.stackSize - 1; i >= 0; i--) {
      map.put("stack[" + i + "]", stack[i]);
    }
    assertEquals(Arrays.toString(stack), map.toString());
  }

  @Test
  public void testBoolean() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{true}"));
    reader.beginObject();
    reader.nextName();
    assertTrue(reader.nextBoolean());
    reader.endObject();
    reader.close();
  }

  @Test
  public void testString() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{\"foo\": \"bar\"}"));
    reader.beginObject();
    reader.nextName();
    reader.nextName();
    assertEquals("bar", reader.nextString());
    reader.endObject();
    reader.close();
  }

  @Test
  public void testNull() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{null}"));
    reader.beginObject();
    reader.nextName();
    reader.nextNull();
    reader.endObject();
    reader.close();
  }

  @Test
  public void testNumber() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{1}"));
    reader.beginObject();
    reader.nextName();
    reader.nextInt(1);
    reader.endObject();
    reader.close();
  }

  @Test
  public void testArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[1, 2, 3]"));
    reader.beginArray();
    assertEquals(1, reader.nextInt());
    assertEquals(2, reader.nextInt());
    assertEquals(3, reader.nextInt());
    reader.endArray();
    reader.close();
  }

  @Test
  public void testArrayWithObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[1, {foo: \"bar\"}]"));
    reader.beginArray();
    assertEquals(1, reader.nextInt());
    reader.beginObject();
    reader.nextName();
    reader.nextName();
    assertEquals("bar", reader.nextString());
    reader.endObject();
    reader.endArray();
    reader.close();
  }

  @Test
  public void testObjectWithArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{\"foo\": [1, 2, 3]}"));
    reader.beginObject();
    reader.nextName();
    reader.nextName();
    reader.beginArray();
    assertEquals(1, reader.nextInt());
    assertEquals(2, reader.nextInt());
    assertEquals(3, reader.nextInt());
    reader.endArray();
    reader.endObject();
    reader.close();
  }

  @Test
  public void testObjectWithObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{\"foo\": {\"bar\": 1}}"));
    reader.beginObject();
    reader.nextName();
    reader.nextName();
    reader.beginObject();
    reader.nextName();
    reader.nextInt(1);
    reader.endObject();
    reader.endObject();
    reader.close();
  }

  @Test
  public void testErrorArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[1, error]"));
    reader.beginArray();
    assertEquals(1, reader.nextInt());
    reader.nextNull();
    reader.endArray();
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testErrorObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{error: 1}"));
    reader.beginObject();
    reader.nextName();
    reader.nextName();
    reader.endObject();
  }

  @Test
  public void testErrorRoot() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{error}"));
    reader.close();
  }

  @Test
  public void testErrorRootWithArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{error[], 1}"));
    reader.close();
  }

  @Test
  public void testErrorRootWithObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{error, foo: 1}"));
    reader.close();
  }

  @Test
  public void testErrorArrayWithArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[error[], 1]"));
    reader.close();
  }

  @Test
  public void testErrorArrayWithObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[error, foo: 1]"));
    reader.close();
  }

  @Test
  public void testErrorObjectWithArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{error[], foo: 1}"));
    reader.close();
  }

  @Test
  public void testErrorObjectWithObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{error, foo: {error: 1}}"));
    reader.close();
  }

  @Test
  public void testErrorNested() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{error: [{error: [error]}]}"));
    reader.close();
  }
}