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

import static org.junit.Assert.*;

public class JsonTreeWriterTest {

  private JsonTreeWriter jsonTreeWriter;

  @Before
  public void setup() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  // Test cases for beginArray() method

  @Test
  public void testBeginArray() throws IOException {
    jsonTreeWriter.beginArray();
    JsonElement result = jsonTreeWriter.get();

    assertTrue(result.isJsonArray());
  }

  // Test cases for endArray() method

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

  @Test
  public void testEndArrayWithValidInput() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
    JsonElement result = jsonTreeWriter.get();

    assertTrue(result.isJsonNull());
  }

  // Test cases for beginObject() method

  @Test
  public void testBeginObject() throws IOException {
    jsonTreeWriter.beginObject();
    JsonElement result = jsonTreeWriter.get();

    assertTrue(result.isJsonObject());
  }

  // Test cases for endObject() method

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

  @Test
  public void testEndObjectWithValidInput() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    JsonElement result = jsonTreeWriter.get();

    assertTrue(result.isJsonNull());
  }

  // Test cases for name() method

  @Test(expected = IllegalStateException.class)
  public void testNameWithEmptyStack() throws IOException {
    jsonTreeWriter.name("key");
  }

  @Test(expected = IllegalStateException.class)
  public void testNameWithNonJsonObject() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.name("key");
  }

  @Test
  public void testNameWithValidInput() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    jsonTreeWriter.value("value");
    jsonTreeWriter.endObject();
    JsonElement result = jsonTreeWriter.get();

    assertTrue(result.isJsonObject());
    JsonObject jsonObject = result.getAsJsonObject();
    assertTrue(jsonObject.has("key"));
    assertEquals("value", jsonObject.get("key").getAsString());
  }

  // Test cases for value() methods

  @Test
  public void testValueWithString() throws IOException {
    jsonTreeWriter.value("test");
    JsonElement result = jsonTreeWriter.get();

    assertTrue(result.isJsonPrimitive());
    assertEquals("test", result.getAsString());
  }

  @Test
  public void testValueWithNull() throws IOException {
    jsonTreeWriter.value((String) null);
    JsonElement result = jsonTreeWriter.get();

    assertTrue(result.isJsonNull());
  }

  @Test
  public void testValueWithBoolean() throws IOException {
    jsonTreeWriter.value(true);
    JsonElement result = jsonTreeWriter.get();

    assertTrue(result.isJsonPrimitive());
    assertTrue(result.getAsJsonPrimitive().isBoolean());
    assertTrue(result.getAsBoolean());
  }

  @Test
  public void testValueWithDouble() throws IOException {
    jsonTreeWriter.value(3.14);
    JsonElement result = jsonTreeWriter.get();

    assertTrue(result.isJsonPrimitive());
    assertTrue(result.getAsJsonPrimitive().isNumber());
    assertEquals(3.14, result.getAsDouble(), 0.0001);
  }

  @Test
  public void testValueWithLong() throws IOException {
    jsonTreeWriter.value(123456L);
    JsonElement result = jsonTreeWriter.get();

    assertTrue(result.isJsonPrimitive());
    assertTrue(result.getAsJsonPrimitive().isNumber());
    assertEquals(123456L, result.getAsLong());
  }

  @Test
  public void testValueWithNumber() throws IOException {
    jsonTreeWriter.value(3.14);
    JsonElement result = jsonTreeWriter.get();

    assertTrue(result.isJsonPrimitive());
    assertTrue(result.getAsJsonPrimitive().isNumber());
    assertEquals(3.14, result.getAsDouble(), 0.0001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueWithNaN() throws IOException {
    jsonTreeWriter.setLenient(false);
    jsonTreeWriter.value(Double.NaN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueWithInfinity() throws IOException {
    jsonTreeWriter.setLenient(false);
    jsonTreeWriter.value(Double.POSITIVE_INFINITY);
  }

  // Test case for flush() method

  @Test
  public void testFlush() throws IOException {
    jsonTreeWriter.flush();
    // No assertion, just verifying that the method does not throw any exceptions
  }

  // Test case for close() method

  @Test(expected = IOException.class)
  public void testCloseWithIncompleteDocument() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.close();
  }

  @Test
  public void testCloseWithCompleteDocument() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    jsonTreeWriter.close();
    JsonElement result = jsonTreeWriter.get();

    assertTrue(result.isJsonPrimitive());
    assertEquals("closed", result.getAsString());
  }

}