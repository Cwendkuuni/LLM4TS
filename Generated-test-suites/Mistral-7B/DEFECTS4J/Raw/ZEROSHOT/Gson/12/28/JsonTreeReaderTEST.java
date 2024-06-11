import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonToken;

public class JsonTreeReaderTest {

  @org.junit.Test
  public void testEmpty() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.NULL);
    assertFalse(reader.hasNext());
    reader.close();
  }

  @org.junit.Test
  public void testStringArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("[\"foo\",\"bar\"]"));
    assertEquals("foo", reader.nextString());
    assertEquals("bar", reader.nextString());
    assertFalse(reader.hasNext());
    reader.close();
  }

  @org.junit.Test
  public void testNumberArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("[-1,3.14]"));
    assertEquals(-1, reader.nextInt());
    assertEquals(3.14, reader.nextDouble(), 0.001);
    assertFalse(reader.hasNext());
    reader.close();
  }

  @org.junit.Test
  public void testBooleanArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("[\"true\",false]"));
    assertTrue(reader.nextBoolean());
    assertFalse(reader.nextBoolean());
    assertFalse(reader.hasNext());
    reader.close();
  }

  @org.junit.Test
  public void testObjectArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("[\n"
        + "  {\"foo\":1,\"bar\":[\"apples\",\"oranges\"]},\n"
        + "  {\"qux\":3.14,\"quux\":null}\n"
        + "]"));
    assertEquals("foo", reader.nextString());
    assertEquals(1, reader.nextInt());
    reader.promoteNameToValue();
    assertEquals(1, reader.nextInt());
    assertEquals("apples", reader.nextString());
    assertEquals("oranges", reader.nextString());
    assertNull(reader.nextString());
    assertEquals(3.14, reader.nextDouble(), 0.001);
    assertNull(reader.nextDouble());
    assertNull(reader.nextNull());
    assertFalse(reader.hasNext());
    reader.close();
  }

  @org.junit.Test
  public void testObjectWithArrayProperty() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("{"
        + "  \"foo\": [\"apples\",\"oranges\"],\n"
        + "  \"bar\": \"baz\",\n"
        + "  \"qux\": {\n"
        + "    \"quux\": 42,\n"
        + "    \"quuz\": null\n"
        + "  }\n"
        + "}"));
    assertEquals("foo", reader.nextString());
    assertEquals("apples", reader.nextString());
    assertEquals("oranges", reader.nextString());
    assertFalse(reader.hasNext());
    reader.close();

    reader = new JsonTreeReader(createJsonElement("{"
        + "  \"foo\": [\"apples\",\"oranges\"],\n"
        + "  \"bar\": \"baz\",\n"
        + "  \"qux\": {\n"
        + "    \"quux\": 42,\n"
        + "    \"quuz\": null,\n"
        + "    \"quuz2\": [1, 2]\n"
        + "  }\n"
        + "}"));
    assertEquals("foo", reader.nextString());
    assertEquals("apples", reader.nextString());
    assertEquals("oranges", reader.nextString());
    reader.promoteNameToValue();
    assertEquals(1, reader.nextInt());
    assertEquals(2, reader.nextInt());
    assertFalse(reader.hasNext());
    reader.close();
  }

  @org.junit.Test
  public void testObjectWithArrayPropertyAndNestedArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("{"
        + "  \"foo\": [\"apples\",\"oranges\"],\n"
        + "  \"bar\": [1,2],\n"
        + "  \"qux\": {\n"
        + "    \"quuz\": [0, [\"apples\",\"oranges\"]],\n"
        + "    \"quuz2\": 42\n"
        + "  }\n"
        + "}"));

    assertEquals("foo", reader.nextString());
    assertEquals("apples", reader.nextString());
    assertEquals("oranges", reader.nextString());
    reader.promoteNameToValue();
    assertEquals(1, reader.nextInt());
    assertEquals(2, reader.nextInt());
    reader.promoteNameToValue();
    assertEquals(0, reader.nextInt());
    assertEquals("apples", reader.nextString());
    assertEquals("oranges", reader.nextString());
    assertEquals(42, reader.nextInt());
    assertFalse(reader.hasNext());
    reader.close();
  }

  @org.junit.Test
  public void testComplexNestedObjects() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("{"
        + "    \"foo\": [{\"bar\": [1,2],\"qux\": 3},\n"
        + "             {\"bar\": [3,4],\"qux\": 5}]\n"
        + "  }\n"
        + "}"));

    reader.promoteNameToValue();
    assertEquals(1, reader.nextInt());
    assertEquals(2, reader.nextInt());
    reader.promoteNameToValue();
    assertEquals(3, reader.nextInt());
    assertEquals(4, reader.nextInt());
    assertEquals(3, reader.nextInt());
    assertEquals(5, reader.nextInt());
    assertFalse(reader.hasNext());
    reader.close();
  }

  @org.junit.Test
  public void testEmptyObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("{}"));
    assertFalse(reader.hasNext());
    reader.close();
  }

  @org.junit.Test
  public void testObjectWithNumberProperty() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("{\"foo\": 42}"));
    assertEquals(42.0, reader.nextDouble(), 0.0);
    assertFalse(reader.hasNext());
    reader.close();
  }

  @org.junit.Test
  public void testObjectWithStringProperty() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("{\"foo\": \"apples\"}"));
    assertEquals("apples", reader.nextString());
    assertFalse(reader.hasNext());
    reader.close();
  }

  @org.junit.Test
  public void testObjectWithBooleanProperty() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("{\"foo\": true}"));
    assertTrue(reader.nextBoolean());
    assertFalse(reader.hasNext());
    reader.close();
  }

  @org.junit.Test
  public void testObjectWithNullProperty() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("{\"foo\": null}"));
    assertNull(reader.nextString());
    assertFalse(reader.hasNext());
    reader.close();
  }

  @org.junit.Test
  public void testObjectWithArrayPropertyAndEmptyArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("{"
        + "  \"foo\": []\n"
        + "}"));
    assertFalse(reader.hasNext());
    reader.close();
  }

  @org.junit.Test
  public void testObjectWithArrayPropertyAndEmptyObjectArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("{"
        + "  \"foo\": [\n"
        + "    {}\n"
        + "  ]\n"
        + "}"));
    assertFalse(reader.hasNext());
    reader.close();
  }

  @org.junit.Test
  public void testObjectWithObjectProperty() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(createJsonElement("{"
        + "  \"foo\": {\n"
        + "    \"bar\": 1,\n"
        + "    \"qux\": [\"apples\",\"oranges\"]\n"
        + "  }\n"
        + "}"));
    assertEquals("bar", reader.nextString());
    assertEquals(1, reader.nextInt());
    reader.promoteNameToValue();
    assertEquals(1, reader.nextInt());
    assertEquals("apples", reader.nextString());
    assertEquals("oranges", reader.nextString());
    reader.close();
  }

  private JsonElement createJsonElement(String json) {
    JsonReader reader = new JsonReader(new StringReader(json));
    JsonElement element = reader.readTree();
    reader.close();
    return element;
  }

  private static class StringReader extends Reader {

    private final String text;

    public StringReader(String text) {
      this.text = text;
    }

    @Override
    public int read(char[] buffer, int offset, int length) throws IOException {
      if (text.length() < length) {
        throw new IOException("Java Tokenizer hit end of input.");
      }
      System.arraycopy(text.substring(offset).toCharArray(), 0, buffer, 0, length);
      text = text.substring(offset + length);
      return length;
    }

    @Override
    public void close() throws IOException {
      // no-op
    }

  }

}