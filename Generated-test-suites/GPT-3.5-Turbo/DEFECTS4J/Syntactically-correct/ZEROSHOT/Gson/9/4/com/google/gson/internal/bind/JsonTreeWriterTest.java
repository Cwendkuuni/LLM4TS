import static org.junit.Assert.*;
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
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
  public void testBeginArray() throws IOException {
    jsonTreeWriter.beginArray();

    JsonElement result = jsonTreeWriter.get();
    assertTrue(result instanceof JsonArray);
  }

  @Test
  public void testEndArray() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();

    JsonElement result = jsonTreeWriter.get();
    assertEquals(JsonNull.INSTANCE, result);
  }

  @Test
  public void testBeginObject() throws IOException {
    jsonTreeWriter.beginObject();

    JsonElement result = jsonTreeWriter.get();
    assertTrue(result instanceof JsonObject);
  }

  @Test
  public void testEndObject() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();

    JsonElement result = jsonTreeWriter.get();
    assertEquals(JsonNull.INSTANCE, result);
  }

  @Test
  public void testName() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");

    JsonObject result = (JsonObject) jsonTreeWriter.get();
    assertTrue(result.has("name"));
  }

  @Test
  public void testValueString() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("value");
    jsonTreeWriter.value("test");

    JsonObject result = (JsonObject) jsonTreeWriter.get();
    assertEquals("test", result.get("value").getAsString());
  }

  @Test
  public void testNullValue() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("value");
    jsonTreeWriter.nullValue();

    JsonObject result = (JsonObject) jsonTreeWriter.get();
    assertEquals(JsonNull.INSTANCE, result.get("value"));
  }

  @Test
  public void testValueBoolean() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("value");
    jsonTreeWriter.value(true);

    JsonObject result = (JsonObject) jsonTreeWriter.get();
    assertTrue(result.get("value").getAsBoolean());
  }

  @Test
  public void testValueDouble() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("value");
    jsonTreeWriter.value(3.14);

    JsonObject result = (JsonObject) jsonTreeWriter.get();
    assertEquals(3.14, result.get("value").getAsDouble(), 0.001);
  }

  @Test
  public void testValueLong() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("value");
    jsonTreeWriter.value(100L);

    JsonObject result = (JsonObject) jsonTreeWriter.get();
    assertEquals(100L, result.get("value").getAsLong());
  }

  @Test
  public void testValueNumber() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("value");
    jsonTreeWriter.value(3.14);

    JsonObject result = (JsonObject) jsonTreeWriter.get();
    assertEquals(3.14, result.get("value").getAsDouble(), 0.001);
  }

  @Test(expected = IllegalStateException.class)
  public void testIncompleteDocument() throws IOException {
    jsonTreeWriter.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalStateException() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endObject();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalValue() throws IOException {
    jsonTreeWriter.value(Double.NaN);
  }

  @Test
  public void testGet() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value("test");
    jsonTreeWriter.endObject();

    JsonObject result = (JsonObject) jsonTreeWriter.get();
    assertEquals("test", result.get("name").getAsString());
  }

  @Test
  public void testValueNull() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("value");
    jsonTreeWriter.value(null);

    JsonObject result = (JsonObject) jsonTreeWriter.get();
    assertEquals(JsonNull.INSTANCE, result.get("value"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalValueNaN() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("value");
    jsonTreeWriter.value(Double.NaN);
    jsonTreeWriter.endObject();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalValueInfinity() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("value");
    jsonTreeWriter.value(Double.POSITIVE_INFINITY);
    jsonTreeWriter.endObject();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalValueNegativeInfinity() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("value");
    jsonTreeWriter.value(Double.NEGATIVE_INFINITY);
    jsonTreeWriter.endObject();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalNumberValueNaN() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("value");
    jsonTreeWriter.value(Double.NaN);
    jsonTreeWriter.endObject();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalNumberValueInfinity() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("value");
    jsonTreeWriter.value(Double.POSITIVE_INFINITY);
    jsonTreeWriter.endObject();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalNumberValueNegativeInfinity() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("value");
    jsonTreeWriter.value(Double.NEGATIVE_INFINITY);
    jsonTreeWriter.endObject();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalStateExceptionFlush() throws IOException {
    jsonTreeWriter.flush();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalStateExceptionValueNull() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value(null);
    jsonTreeWriter.endArray();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalStateExceptionIncompleteDocument() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.close();
  }

  @Test
  public void testClose() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.close();

    JsonElement result = jsonTreeWriter.get();
    assertTrue(result instanceof JsonArray);
  }

  @Test
  public void testFlush() throws IOException {
    // Test flushing an empty writer, should not throw an exception
    jsonTreeWriter.flush();

    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value("test");

    jsonTreeWriter.flush();

    JsonObject result = (JsonObject) jsonTreeWriter.get();
    assertEquals("test", result.get("name").getAsString());
  }

  @Test
  public void testCloseIncompleteDocument() {
    jsonTreeWriter.beginArray();
    assertThrows(IOException.class, () -> jsonTreeWriter.close());
  }
}