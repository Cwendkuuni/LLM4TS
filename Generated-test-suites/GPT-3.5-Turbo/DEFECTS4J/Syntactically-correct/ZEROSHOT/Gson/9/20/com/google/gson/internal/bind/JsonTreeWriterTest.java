import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class JsonTreeWriterTest {

  private JsonTreeWriter jsonTreeWriter;

  @Before
  public void setup() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
  public void testGetEmpty() {
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result instanceof JsonNull);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetWithElementInStack() {
    JsonObject jsonObject = new JsonObject();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.put(jsonObject);
    jsonTreeWriter.get();
  }

  @Test
  public void testBeginArray() throws IOException {
    jsonTreeWriter.beginArray();
    JsonElement result = jsonTreeWriter.peek();
    assertTrue(result instanceof JsonArray);
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWithEmptyStack() throws IOException {
    jsonTreeWriter.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWithPendingName() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    jsonTreeWriter.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWithNonArrayTopElement() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endArray();
  }

  @Test
  public void testEndArraySuccess() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
    JsonElement result = jsonTreeWriter.peek();
    assertNull(result);
  }

  @Test
  public void testBeginObject() throws IOException {
    jsonTreeWriter.beginObject();
    JsonElement result = jsonTreeWriter.peek();
    assertTrue(result instanceof JsonObject);
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWithEmptyStack() throws IOException {
    jsonTreeWriter.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWithPendingName() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    jsonTreeWriter.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWithNonObjectTopElement() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endObject();
  }

  @Test
  public void testEndObjectSuccess() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    JsonElement result = jsonTreeWriter.peek();
    assertNull(result);
  }

  @Test(expected = IllegalStateException.class)
  public void testNameWithEmptyStack() throws IOException {
    jsonTreeWriter.name("key");
  }

  @Test(expected = IllegalStateException.class)
  public void testNameWithPendingName() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("first");
    jsonTreeWriter.name("second");
  }

  @Test(expected = IllegalStateException.class)
  public void testNameWithNonObjectTopElement() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.name("key");
  }

  @Test
  public void testNameSuccess() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    JsonObject result = (JsonObject) jsonTreeWriter.peek();
    assertTrue(result.has("key"));
  }

  @Test
  public void testValueWithString() throws IOException {
    String value = "test";
    jsonTreeWriter.value(value);
    JsonElement result = jsonTreeWriter.peek();
    assertTrue(result.isJsonPrimitive());
    assertEquals(value, result.getAsString());
  }

  @Test
  public void testValueWithNull() throws IOException {
    jsonTreeWriter.value(null);
    JsonElement result = jsonTreeWriter.peek();
    assertTrue(result instanceof JsonNull);
  }

  @Test
  public void testValueWithBoolean() throws IOException {
    boolean value = true;
    jsonTreeWriter.value(value);
    JsonElement result = jsonTreeWriter.peek();
    assertTrue(result.isJsonPrimitive());
    assertEquals(value, result.getAsBoolean());
  }

  @Test
  public void testValueWithDouble() throws IOException {
    double value = 3.14;
    jsonTreeWriter.value(value);
    JsonElement result = jsonTreeWriter.peek();
    assertTrue(result.isJsonPrimitive());
    assertEquals(value, result.getAsDouble(), 0);
  }

  @Test
  public void testValueWithLong() throws IOException {
    long value = 1000000000L;
    jsonTreeWriter.value(value);
    JsonElement result = jsonTreeWriter.peek();
    assertTrue(result.isJsonPrimitive());
    assertEquals(value, result.getAsLong());
  }

  @Test
  public void testValueWithNumber() throws IOException {
    int value = 123;
    jsonTreeWriter.value(value);
    JsonElement result = jsonTreeWriter.peek();
    assertTrue(result.isJsonPrimitive());
    assertEquals(value, result.getAsInt());
  }

  @Test
  public void testFlush() throws IOException {
    jsonTreeWriter.flush();
    JsonElement result = jsonTreeWriter.peek();
    assertNotNull(result);
  }

  @Test(expected = IOException.class)
  public void testCloseWithNonEmptyStack() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.close();
  }

  @Test(expected = IOException.class)
  public void testCloseWithPendingName() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    jsonTreeWriter.close();
  }

  @Test
  public void testCloseSuccess() throws IOException {
    jsonTreeWriter.close();
    JsonElement result = jsonTreeWriter.peek();
    assertTrue(result instanceof JsonPrimitive);
  }

}