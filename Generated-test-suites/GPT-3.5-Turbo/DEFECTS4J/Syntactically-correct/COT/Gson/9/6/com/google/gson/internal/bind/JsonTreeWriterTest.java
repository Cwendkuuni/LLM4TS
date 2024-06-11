import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Before;
import org.junit.Test;

public class JsonTreeWriterTest {

  private JsonTreeWriter writer;

  @Before
  public void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
  public void testGet() {
    // Test when calling get() after operations are performed
    writer.beginObject();
    writer.name("name").value("value");
    writer.endObject();
    JsonElement result = writer.get();
    assertTrue(result instanceof JsonObject);
    JsonObject jsonObject = (JsonObject) result;
    assertTrue(jsonObject.has("name"));
    assertEquals("value", jsonObject.get("name").getAsString());
  }

  @Test
  public void testBeginEndArray() {
    // Test when calling beginArray() and endArray() consecutively
    writer.beginArray().endArray();
    JsonElement result = writer.get();
    assertTrue(result instanceof JsonArray);
    JsonArray jsonArray = (JsonArray) result;
    assertEquals(0, jsonArray.size());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginEndArrayOrder() {
    // Test when calling beginArray() and endArray() in incorrect order
    writer.beginArray();
    writer.endObject();
  }

  @Test
  public void testBeginEndObject() {
    // Test when calling beginObject() and endObject() consecutively
    writer.beginObject().endObject();
    JsonElement result = writer.get();
    assertTrue(result instanceof JsonObject);
    JsonObject jsonObject = (JsonObject) result;
    assertTrue(jsonObject.entrySet().isEmpty());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginEndObjectOrder() {
    // Test when calling beginObject() and endObject() in incorrect order
    writer.beginObject();
    writer.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testNameOutsideObject() {
    // Test when calling name() outside an object context
    writer.name("name");
  }

  @Test(expected = IllegalStateException.class)
  public void testValueOutsideObject() {
    // Test when calling value() outside an object context
    writer.value("value");
  }

  @Test
  public void testValueWithDataType() {
    // Test when calling value() with different data types
    writer.beginArray();
    writer.value("string");
    writer.value(123);
    writer.value(true);
    writer.value(3.14159);
    writer.endArray();
    JsonElement result = writer.get();
    assertTrue(result instanceof JsonArray);
    JsonArray jsonArray = (JsonArray) result;
    assertEquals("string", jsonArray.get(0).getAsString());
    assertEquals(123, jsonArray.get(1).getAsInt());
    assertEquals(true, jsonArray.get(2).getAsBoolean());
    assertEquals(3.14159, jsonArray.get(3).getAsDouble(), 0.00001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueWithNaN() {
    // Test when calling value() with NaN
    writer.value(Double.NaN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueWithInfinity() {
    // Test when calling value() with infinity
    writer.value(Double.POSITIVE_INFINITY);
  }

  @Test
  public void testFlush() {
    // Test when calling flush()
    writer.flush(); // No exception should be thrown
  }

  @Test(expected = IOException.class)
  public void testClose() throws IOException {
    // Test when calling close()
    writer.close();
    writer.beginObject();
  }
}