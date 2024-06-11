import static org.junit.Assert.*;

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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JsonTreeWriterTest {

  private JsonTreeWriter jsonTreeWriter;

  @Before
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @After
  public void tearDown() {
    jsonTreeWriter = null;
  }

  @Test
  public void testBeginArray() throws IOException {
    jsonTreeWriter.beginArray();
    JsonElement result = jsonTreeWriter.get();
    assertNotNull(result);
    assertTrue(result.isJsonArray());
  }

  @Test
  public void testEndArray() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
    JsonElement result = jsonTreeWriter.get();
    assertNotNull(result);
    assertFalse(result.isJsonArray());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWithoutBeginArray() throws IOException {
    jsonTreeWriter.endArray();
  }

  @Test
  public void testBeginObject() throws IOException {
    jsonTreeWriter.beginObject();
    JsonElement result = jsonTreeWriter.get();
    assertNotNull(result);
    assertTrue(result.isJsonObject());
  }

  @Test
  public void testEndObject() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    JsonElement result = jsonTreeWriter.get();
    assertNotNull(result);
    assertFalse(result.isJsonObject());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWithoutBeginObject() throws IOException {
    jsonTreeWriter.endObject();
  }

  @Test
  public void testName() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    JsonElement result = jsonTreeWriter.get();
    assertNotNull(result);
    assertTrue(result.isJsonObject());
    JsonObject jsonObject = (JsonObject) result;
    assertTrue(jsonObject.has("key"));
  }

  @Test(expected = IllegalStateException.class)
  public void testNameWithoutBeginObject() throws IOException {
    jsonTreeWriter.name("key");
  }

  @Test
  public void testValueString() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key").value("value");
    JsonElement result = jsonTreeWriter.get();
    assertNotNull(result);
    assertTrue(result.isJsonObject());
    JsonObject jsonObject = (JsonObject) result;
    assertTrue(jsonObject.has("key"));
    assertEquals("value", jsonObject.get("key").getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testValueStringWithoutName() throws IOException {
    jsonTreeWriter.value("value");
  }

  @Test
  public void testNullValue() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key").nullValue();
    JsonElement result = jsonTreeWriter.get();
    assertNotNull(result);
    assertTrue(result.isJsonObject());
    JsonObject jsonObject = (JsonObject) result;
    assertTrue(jsonObject.has("key"));
    assertTrue(jsonObject.get("key").isJsonNull());
  }

  @Test
  public void testValueBoolean() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key").value(true);
    JsonElement result = jsonTreeWriter.get();
    assertNotNull(result);
    assertTrue(result.isJsonObject());
    JsonObject jsonObject = (JsonObject) result;
    assertTrue(jsonObject.has("key"));
    assertTrue(jsonObject.get("key").getAsBoolean());
  }

  @Test(expected = IllegalStateException.class)
  public void testValueBooleanWithoutName() throws IOException {
    jsonTreeWriter.value(true);
  }

  @Test
  public void testValueDouble() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key").value(3.14);
    JsonElement result = jsonTreeWriter.get();
    assertNotNull(result);
    assertTrue(result.isJsonObject());
    JsonObject jsonObject = (JsonObject) result;
    assertTrue(jsonObject.has("key"));
    assertEquals(3.14, jsonObject.get("key").getAsDouble(), 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueDoubleNaN() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key").value(Double.NaN);
  }

  @Test(expected = IllegalStateException.class)
  public void testValueDoubleWithoutName() throws IOException {
    jsonTreeWriter.value(3.14);
  }

  @Test
  public void testValueLong() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key").value(42L);
    JsonElement result = jsonTreeWriter.get();
    assertNotNull(result);
    assertTrue(result.isJsonObject());
    JsonObject jsonObject = (JsonObject) result;
    assertTrue(jsonObject.has("key"));
    assertEquals(42, jsonObject.get("key").getAsLong());
  }

  @Test(expected = IllegalStateException.class)
  public void testValueLongWithoutName() throws IOException {
    jsonTreeWriter.value(42L);
  }

  @Test
  public void testValueNumber() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key").value(123);
    JsonElement result = jsonTreeWriter.get();
    assertNotNull(result);
    assertTrue(result.isJsonObject());
    JsonObject jsonObject = (JsonObject) result;
    assertTrue(jsonObject.has("key"));
    assertEquals(123, jsonObject.get("key").getAsInt());
  }

  @Test(expected = IllegalStateException.class)
  public void testValueNumberWithoutName() throws IOException {
    jsonTreeWriter.value(123);
  }

  @Test(expected = IOException.class)
  public void testCloseIncompleteDocument() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.close();
  }

  @Test
  public void testGet() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key").value("value");
    JsonElement result = jsonTreeWriter.get();
    assertNotNull(result);
    assertTrue(result.isJsonObject());
    JsonObject jsonObject = (JsonObject) result;
    assertTrue(jsonObject.has("key"));
    assertEquals("value", jsonObject.get("key").getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetWithNonEmptyStack() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key").value("value");
    jsonTreeWriter.get();
    jsonTreeWriter.get();
  }

  @Test(expected = IllegalStateException.class)
  public void testGetWithEmptyStack() throws IOException {
    jsonTreeWriter.get();
  }

  @Test
  public void testPut() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key").value("value");
    JsonElement result = jsonTreeWriter.get();
    assertNotNull(result);
    assertTrue(result.isJsonObject());
    JsonObject jsonObject = (JsonObject) result;
    assertTrue(jsonObject.has("key"));
    assertEquals("value", jsonObject.get("key").getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testPutWithNullName() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.put(new JsonPrimitive("value"));
  }

  @Test
  public void testPutWithNullValue() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    jsonTreeWriter.put(JsonNull.INSTANCE);
    JsonElement result = jsonTreeWriter.get();
    assertNotNull(result);
    assertTrue(result.isJsonObject());
    JsonObject jsonObject = (JsonObject) result;
    assertTrue(jsonObject.has("key"));
    assertTrue(jsonObject.get("key").isJsonNull());
  }

  @Test
  public void testPutWithEmptyStack() throws IOException {
    jsonTreeWriter.put(new JsonPrimitive("value"));
    JsonElement result = jsonTreeWriter.get();
    assertNotNull(result);
    assertFalse(result.isJsonObject() || result.isJsonArray());
  }

  @Test(expected = IllegalStateException.class)
  public void testPutWithNonEmptyStack() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.put(new JsonPrimitive("value"));
    jsonTreeWriter.put(new JsonPrimitive("value"));
  }
}