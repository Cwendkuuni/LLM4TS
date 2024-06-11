import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;

public class JsonTreeWriterTest {

  private List<JsonElement> expectedStack;
  private JsonTreeWriter writer;

  @Before
  public void setUp() {
    expectedStack = new ArrayList<>();
    expectedStack.add(JsonObject.class, new JsonObject());
    expectedStack.add(JsonArray.class, new JsonArray());
    expectedStack.add(JsonPrimitive.class, new JsonPrimitive("value"));
    expectedStack.add(JsonPrimitive.class, new JsonPrimitive(42.0));
    expectedStack.add(JsonPrimitive.class, new JsonPrimitive(true));
    writer = new JsonTreeWriter();
  }

  @Test
  public void testGetEmpty() {
    assertNull(writer.get());
  }

  @Test
  public void testGetJsonObject() throws IOException {
    writer.beginObject();
    writer.name("key").value("value");
    JsonObject actual = (JsonObject) writer.get();
    assertEquals(new JsonParser().parse("{\"key\":\"value\"}"), actual);
  }

  @Test
  public void testGetJsonArray() throws IOException {
    writer.beginArray();
    writer.value("value1");
    writer.value(42);
    JsonArray actual = (JsonArray) writer.get();
    assertEquals(new JsonParser().parse("[\"value1\",42]"), actual);
  }

  @Test
  public void testGetJsonPrimitiveNumber() throws IOException {
    writer.beginObject();
    writer.name("number");
    writer.value(42.0);
    JsonElement actual = writer.get();
    assertEquals(new JsonPrimitive(42.0), actual);
  }

  @Test
  public void testGetJsonPrimitiveString() throws IOException {
    writer.beginObject();
    writer.name("string").value("value");
    JsonElement actual = writer.get();
    assertEquals(new JsonPrimitive("value"), actual);
  }

  @Test
  public void testGetJsonPrimitiveBoolean() throws IOException {
    writer.beginObject();
    writer.name("boolean").value(true);
    JsonElement actual = writer.get();
    assertEquals(new JsonPrimitive(true), actual);
  }

  @Test
  public void testGetJsonPrimitiveNull() throws IOException {
    writer.beginObject();
    writer.name("null");
    writer.nullValue();
    JsonElement actual = writer.get();
    assertEquals(JsonNull.INSTANCE, actual);
  }

  @Test
  public void testGetJsonPrimitiveNumberWithLenient() throws IOException {
    writer = new JsonTreeWriter(true);
    writer.beginObject();
    writer.name("number");
    writer.value(Double.NaN);
    JsonElement actual = writer.get();
    assertEquals(new JsonPrimitive(Double.toString(Double.NaN)), actual);
  }

  @Test
  public void testGetJsonPrimitiveInfiniteNumberWithLenient() throws IOException {
    writer = new JsonTreeWriter(true);
    writer.beginObject();
    writer.name("number");
    writer.value(Double.POSITIVE_INFINITY);
    JsonElement actual = writer.get();
    assertEquals(new JsonPrimitive(Double.toString(Double.POSITIVE_INFINITY)), actual);
  }

  @Test
  public void testGetJsonPrimitiveInfiniteNumberWithoutLenient() throws IOException {
    writer.beginObject();
    writer.name("number");
    writer.value(Double.POSITIVE_INFINITY);
    try {
      writer.get();
      fail("Expected IOException");
    } catch (IOException e) {
      String message = e.getMessage();
      assertTrue(message.contains("JSON forbids infinities"));
    }
  }

  @Test
  public void testGetJsonPrimitiveNaNWithoutLenient() throws IOException {
    writer.beginObject();
    writer.name("number");
    writer.value(Double.NaN);
    try {
      writer.get();
      fail("Expected IOException");
    } catch (IOException e) {
      String message = e.getMessage();
      assertTrue(message.contains("JSON forbids NaN"));
    }
  }

  @Test
  public void testBeginArray() throws IOException {
    writer.beginArray();
    assertEquals(1, writer.stack.size());
    assertEquals(JsonArray.class, writer.stack.get(0).getClass());
  }

  @Test
  public void testBeginObject() throws IOException {
    writer.beginObject();
    assertEquals(1, writer.stack.size());
    assertEquals(JsonObject.class, writer.stack.get(0).getClass());
  }

  @Test
  public void testBeginArrayTwice() throws IOException {
    writer.beginArray();
    writer.beginArray();
    assertEquals(2, writer.stack.size());
    assertEquals(JsonArray.class, writer.stack.get(1).getClass());
  }

  @Test
  public void testBeginObjectTwice() throws IOException {
    writer.beginObject();
    writer.beginObject();
    assertEquals(2, writer.stack.size());
    assertEquals(JsonObject.class, writer.stack.get(1).getClass());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayAfterBeginObject() throws IOException {
    writer.beginObject();
    writer.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectAfterBeginArray() throws IOException {
    writer.beginArray();
    writer.beginObject();
  }

  @Test
  public void testEndArray() throws IOException {
    writer.beginArray();
    writer.value("value1");
    writer.endArray();
    assertEquals(0, writer.stack.size());
  }

  @Test
  public void testEndArrayMultipleElements() throws IOException {
    writer.beginArray();
    writer.value("value1");
    writer.value(42);
    writer.endArray();
    assertEquals(0, writer.stack.size());
  }

  @Test
  public void testEndObject() throws IOException {
    writer.beginObject();
    writer.name("key1").value("value1");
    writer.name("key2").value("value2");
    writer.endObject();
    assertEquals(0, writer.stack.size());
  }

  @Test
  public void testEndObjectMultipleProperties() throws IOException {
    writer.beginObject();
    writer.name("key1").value("value1");
    writer.name("key2").value("value2");
    writer.name("key3").value("value3");
    writer.endObject();
    assertEquals(0, writer.stack.size());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWithoutBeginArray() throws IOException {
    writer.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWithoutBeginObject() throws IOException {
    writer.endObject();
  }

  @Test
  public void testName() throws IOException {
    writer.beginObject();
    writer.name("key1");
    writer.value("value1");
    writer.name("key2");
    writer.value("value2");
    writer.endObject();
    JsonObject actual = (JsonObject) writer.get();
    List<String> keys = Arrays.asList("key1", "key2");
    assertEquals(keys.toString(), actual.keySet().toString());
  }

  @Test
  public void testValueJsonPrimitiveString() throws IOException {
    writer.beginObject();
    writer.name("key").value("value");
    writer.endObject();
    writer.setValue("value");
    JsonObject actual = (JsonObject) writer.get();
    assertEquals(new JsonParser().parse("{\"key\":\"value\"}"), actual);
  }

  @Test
  public void testValueJsonPrimitiveNumber() throws IOException {
    writer.beginObject();
    writer.name("key").value(42);
    writer.endObject();
    writer.setValue(42);
    JsonObject actual = (JsonObject) writer.get();
    assertEquals(new JsonParser().parse("{\"key\":42}"), actual);
  }

  @Test
  public void testValueJsonPrimitiveBoolean() throws IOException {
    writer.beginObject();
    writer.name("key").value(true);
    writer.endObject();
    writer.setValue(true);
    JsonObject actual = (JsonObject) writer.get();
    assertEquals(new JsonParser().parse("{\"key\":true}"), actual);
  }

  @Test
  public void testValueJsonPrimitiveNull() throws IOException {
    writer.beginObject();
    writer.name("key").value(null);
    writer.endObject();
    writer.setValue(null);
    JsonObject actual = (JsonObject) writer.get();
    assertEquals(new JsonParser().parse("{\"key\":null}"), actual);
  }

  @Test(expected = IllegalStateException.class)
  public void testValueJsonPrimitiveStringAfterBeginArray() throws IOException {
    writer.beginArray();
    writer.setValue("value");
  }

  @Test(expected = IllegalStateException.class)
  public void testValueJsonPrimitiveNumberAfterBeginArray() throws IOException {
    writer.beginArray();
    writer.setValue(42);
  }

  @Test(expected = IllegalStateException.class)
  public void testValueJsonPrimitiveBooleanAfterBeginArray() throws IOException {
    writer.beginArray();
    writer.setValue(true);
  }

  @Test(expected = IllegalStateException.class)
  public void testValueJsonPrimitiveStringAfterBeginObject() throws IOException {
    writer.beginObject();
    writer.setValue("value");
  }

  @Test(expected = IllegalStateException.class)
  public void testValueJsonPrimitiveNumberAfterBeginObject() throws IOException {
    writer.beginObject();
    writer.setValue(42);
  }

  @Test(expected = IllegalStateException.class)
  public void testValueJsonPrimitiveBooleanAfterBeginObject() throws IOException {
    writer.beginObject();
    writer.setValue(true);
  }

  @Test
  public void testValueJsonArray() throws IOException {
    writer.beginObject();
    writer.name("array").value(new JsonArray());
    writer.endObject();
    writer.setValue(new JsonArray());
    JsonObject actual = (JsonObject) writer.get();
    JsonArray expected = new JsonArray();
    expected.add("[]");
    assertEquals(expected, actual.get("array"));
  }

  @Test
  public void testValueJsonObject() throws IOException {
    writer.beginObject();
    writer.name("object").value(new JsonObject());
    writer.endObject();
    writer.setValue(new JsonObject());
    JsonObject actual = (JsonObject) writer.get();
    JsonObject expected = new JsonObject();
    expected.add("object", new JsonObject());
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalStateException.class)
  public void testValueJsonArrayAfterName() throws IOException {
    writer.beginObject();
    writer.name("name");
    writer.setValue(new JsonArray());
  }

  @Test(expected = IllegalStateException.class)
  public void testValueJsonObjectAfterName() throws IOException {
    writer.beginObject();
    writer.name("name");
    writer.setValue(new JsonObject());
  }

  @Test
  public void testWriteToStream() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    OutputStreamWriter writer = new OutputStreamWriter(outputStream);
    writer.write("{\"key\":\"value\",\"array\":[1,2,3]}");
    writer.flush();
    writer.close();
    String actual = outputStream.toString();
    String expected = "{\"key\":\"value\",\"array\":[1,2,3]}";
    assertEquals(expected, actual);
  }

  @After
  public void tearDown() {
    writer.close();
  }
}