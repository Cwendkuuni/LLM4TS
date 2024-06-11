import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
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
  public void testGet_WhenNoJsonElementAdded_ShouldReturnJsonNull() {
    JsonElement result = writer.get();
    assertEquals(JsonNull.INSTANCE, result);
  }

  @Test
  public void testBeginArray_WhenInsideJsonObject_ShouldAddJsonArrayToJsonObject() throws Exception {
    writer.beginObject();
    writer.name("array");
    writer.beginArray();
    writer.endArray();
    writer.endObject();

    JsonElement result = writer.get();
    assertTrue(result.isJsonObject());

    JsonObject jsonObject = result.getAsJsonObject();
    assertTrue(jsonObject.has("array"));
    assertTrue(jsonObject.get("array").isJsonArray());
  }

  @Test
  public void testBeginArray_WhenInsideJsonArray_ShouldAddJsonArrayToPreviousJsonArray() throws Exception {
    writer.beginArray();
    writer.beginArray();
    writer.endArray();
    writer.endArray();

    JsonElement result = writer.get();
    assertTrue(result.isJsonArray());

    assertTrue(result.getAsJsonArray().get(0).isJsonArray());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArray_WhenStackIsEmpty_ShouldThrowIllegalStateException() throws Exception {
    writer.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArray_WhenNotInsideJsonArray_ShouldThrowIllegalStateException() throws Exception {
    writer.beginObject();
    writer.endArray();
    writer.endObject();
  }

  @Test
  public void testBeginObject_WhenInsideJsonObject_ShouldAddJsonObjectToJsonObject() throws Exception {
    writer.beginObject();
    writer.name("object");
    writer.beginObject();
    writer.endObject();
    writer.endObject();

    JsonElement result = writer.get();
    assertTrue(result.isJsonObject());

    JsonObject jsonObject = result.getAsJsonObject();
    assertTrue(jsonObject.has("object"));
    assertTrue(jsonObject.get("object").isJsonObject());
  }

  @Test
  public void testBeginObject_WhenInsideJsonArray_ShouldAddJsonObjectToPreviousJsonArray() throws Exception {
    writer.beginArray();
    writer.beginObject();
    writer.endObject();
    writer.endArray();

    JsonElement result = writer.get();
    assertTrue(result.isJsonArray());

    assertTrue(result.getAsJsonArray().get(0).isJsonObject());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObject_WhenStackIsEmpty_ShouldThrowIllegalStateException() throws Exception {
    writer.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObject_WhenNotInsideJsonObject_ShouldThrowIllegalStateException() throws Exception {
    writer.beginArray();
    writer.endObject();
    writer.endArray();
  }

  @Test
  public void testName_WhenInsideJsonObject_ShouldSetNameOfNextJsonValue() throws Exception {
    writer.beginObject();
    writer.name("name");
    writer.value("value");
    writer.endObject();

    JsonElement result = writer.get();
    assertTrue(result.isJsonObject());

    JsonObject jsonObject = result.getAsJsonObject();
    assertTrue(jsonObject.has("name"));
    assertTrue(jsonObject.get("name").isJsonPrimitive());

    JsonPrimitive jsonPrimitive = jsonObject.get("name").getAsJsonPrimitive();
    assertTrue(jsonPrimitive.isString());
    assertEquals("value", jsonPrimitive.getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testName_WhenStackIsEmpty_ShouldThrowIllegalStateException() throws Exception {
    writer.name("name");
  }

  @Test(expected = IllegalStateException.class)
  public void testName_WhenNotInsideJsonObject_ShouldThrowIllegalStateException() throws Exception {
    writer.beginArray();
    writer.name("name");
    writer.endArray();
  }

  @Test
  public void testValue_WhenString_ShouldAddJsonPrimitiveWithStringValue() throws Exception {
    writer.beginObject();
    writer.name("value");
    writer.value("test");
    writer.endObject();

    JsonElement result = writer.get();
    assertTrue(result.isJsonObject());

    JsonObject jsonObject = result.getAsJsonObject();
    assertTrue(jsonObject.has("value"));
    assertTrue(jsonObject.get("value").isJsonPrimitive());

    JsonPrimitive jsonPrimitive = jsonObject.get("value").getAsJsonPrimitive();
    assertTrue(jsonPrimitive.isString());
    assertEquals("test", jsonPrimitive.getAsString());
  }

  @Test
  public void testValue_WhenBoolean_ShouldAddJsonPrimitiveWithBooleanValue() throws Exception {
    writer.beginObject();
    writer.name("value");
    writer.value(true);
    writer.endObject();

    JsonElement result = writer.get();
    assertTrue(result.isJsonObject());

    JsonObject jsonObject = result.getAsJsonObject();
    assertTrue(jsonObject.has("value"));
    assertTrue(jsonObject.get("value").isJsonPrimitive());

    JsonPrimitive jsonPrimitive = jsonObject.get("value").getAsJsonPrimitive();
    assertTrue(jsonPrimitive.isBoolean());
    assertTrue(jsonPrimitive.getAsBoolean());
  }

  @Test
  public void testValue_WhenLong_ShouldAddJsonPrimitiveWithLongValue() throws Exception {
    writer.beginObject();
    writer.name("value");
    writer.value(10L);
    writer.endObject();

    JsonElement result = writer.get();
    assertTrue(result.isJsonObject());

    JsonObject jsonObject = result.getAsJsonObject();
    assertTrue(jsonObject.has("value"));
    assertTrue(jsonObject.get("value").isJsonPrimitive());

    JsonPrimitive jsonPrimitive = jsonObject.get("value").getAsJsonPrimitive();
    assertTrue(jsonPrimitive.isNumber());
    assertEquals(10L, jsonPrimitive.getAsLong());
  }

  @Test
  public void testValue_WhenInteger_ShouldAddJsonPrimitiveWithIntegerValue() throws Exception {
    writer.beginObject();
    writer.name("value");
    writer.value(10);
    writer.endObject();

    JsonElement result = writer.get();
    assertTrue(result.isJsonObject());

    JsonObject jsonObject = result.getAsJsonObject();
    assertTrue(jsonObject.has("value"));
    assertTrue(jsonObject.get("value").isJsonPrimitive());

    JsonPrimitive jsonPrimitive = jsonObject.get("value").getAsJsonPrimitive();
    assertTrue(jsonPrimitive.isNumber());
    assertEquals(10, jsonPrimitive.getAsInt());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValue_WhenDoubleWithNaN_ShouldThrowIllegalArgumentException() throws Exception {
    writer.beginObject();
    writer.name("value");
    writer.value(Double.NaN);
    writer.endObject();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValue_WhenDoubleWithPositiveInfinity_ShouldThrowIllegalArgumentException() throws Exception {
    writer.beginObject();
    writer.name("value");
    writer.value(Double.POSITIVE_INFINITY);
    writer.endObject();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValue_WhenDoubleWithNegativeInfinity_ShouldThrowIllegalArgumentException() throws Exception {
    writer.beginObject();
    writer.name("value");
    writer.value(Double.NEGATIVE_INFINITY);
    writer.endObject();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValue_WhenDoubleWithInfinityInLenientMode_ShouldThrowIllegalArgumentException() throws Exception {
    writer.setLenient(true);
    writer.beginObject();
    writer.name("value");
    writer.value(Double.POSITIVE_INFINITY);
    writer.endObject();
  }

  @Test
  public void testNullValue_WhenInsideJsonObject_ShouldAddJsonNull() throws Exception {
    writer.beginObject();
    writer.name("value");
    writer.nullValue();
    writer.endObject();

    JsonElement result = writer.get();
    assertTrue(result.isJsonObject());

    JsonObject jsonObject = result.getAsJsonObject();
    assertTrue(jsonObject.has("value"));
    assertTrue(jsonObject.get("value").isJsonNull());
    assertEquals(JsonNull.INSTANCE, jsonObject.get("value"));
  }

  @Test(expected = IllegalStateException.class)
  public void testNullValue_WhenStackIsEmpty_ShouldThrowIllegalStateException() throws Exception {
    writer.nullValue();
  }

  @Test
  public void testFlush_WhenCalled_ShouldNotThrowException() throws Exception {
    writer.flush();
  }

  @Test(expected = IOException.class)
  public void testClose_WhenStackIsNotEmpty_ShouldThrowIOException() throws Exception {
    writer.beginObject();
    writer.close();
  }

  @Test
  public void testClose_WhenStackIsEmpty_ShouldAddSentinelClosedJsonPrimitiveToStack() throws Exception {
    writer.close();

    JsonElement result = writer.get();
    assertTrue(result.isJsonPrimitive());
    assertEquals("closed", result.getAsJsonPrimitive().getAsString());
  }
}