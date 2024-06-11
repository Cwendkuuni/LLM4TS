import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Before;
import org.junit.Test;

public class JsonTreeWriterTest {
  
  private JsonTreeWriter jsonTreeWriter;
  
  @Before
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }
  
  @Test
  public void testBeginArray() {
    jsonTreeWriter.beginArray();
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result.isJsonArray());
  }

  @Test
  public void testEndArray() {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result.isJsonNull());
  }

  @Test
  public void testBeginObject() {
    jsonTreeWriter.beginObject();
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result.isJsonObject());
  }

  @Test
  public void testEndObject() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result.isJsonNull());
  }

  @Test
  public void testName() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result.isJsonObject());
    JsonObject jsonObject = result.getAsJsonObject();
    assertTrue(jsonObject.has("key"));
  }

  @Test
  public void testValueString() {
    jsonTreeWriter.value("value");
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result.isJsonPrimitive());
    JsonPrimitive jsonPrimitive = result.getAsJsonPrimitive();
    assertTrue(jsonPrimitive.isString());
    assertEquals("value", jsonPrimitive.getAsString());
  }

  @Test
  public void testNullValue() {
    jsonTreeWriter.nullValue();
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result.isJsonNull());
  }

  @Test
  public void testValueBoolean() {
    jsonTreeWriter.value(true);
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result.isJsonPrimitive());
    JsonPrimitive jsonPrimitive = result.getAsJsonPrimitive();
    assertTrue(jsonPrimitive.isBoolean());
    assertTrue(jsonPrimitive.getAsBoolean());
  }

  @Test
  public void testValueDouble() {
    jsonTreeWriter.value(3.14);
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result.isJsonPrimitive());
    JsonPrimitive jsonPrimitive = result.getAsJsonPrimitive();
    assertTrue(jsonPrimitive.isNumber());
    assertEquals(3.14, jsonPrimitive.getAsDouble(), 0.001);
  }

  @Test
  public void testValueLong() {
    jsonTreeWriter.value(42L);
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result.isJsonPrimitive());
    JsonPrimitive jsonPrimitive = result.getAsJsonPrimitive();
    assertTrue(jsonPrimitive.isNumber());
    assertEquals(42L, jsonPrimitive.getAsLong());
  }

  @Test
  public void testValueNumber() {
    jsonTreeWriter.value(123);
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result.isJsonPrimitive());
    JsonPrimitive jsonPrimitive = result.getAsJsonPrimitive();
    assertTrue(jsonPrimitive.isNumber());
    assertEquals(123, jsonPrimitive.getAsInt());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetOnNonEmptyStack() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.get();
  }

  @Test(expected = IllegalStateException.class)
  public void testGetOnEmptyStack() {
    jsonTreeWriter.get();
  }
  
  @Test(expected = IllegalStateException.class)
  public void testEndArrayOnEmptyStack() {
    jsonTreeWriter.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWithPendingName() {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.name("key");
    jsonTreeWriter.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectOnEmptyStack() {
    jsonTreeWriter.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWithPendingName() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    jsonTreeWriter.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testNameOnEmptyStack() {
    jsonTreeWriter.name("key");
  }

  @Test(expected = IllegalStateException.class)
  public void testNameWithPendingName() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key1");
    jsonTreeWriter.name("key2");
  }

  @Test
  public void testFlush() {
    // Nothing to test, just ensuring no exception is thrown
    jsonTreeWriter.flush();
  }

  @Test(expected = IOException.class)
  public void testCloseOnNonEmptyStack() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.close();
  }

  @Test(expected = IOException.class)
  public void testCloseOnEmptyStack() throws IOException {
    jsonTreeWriter.close();
  }
  
}