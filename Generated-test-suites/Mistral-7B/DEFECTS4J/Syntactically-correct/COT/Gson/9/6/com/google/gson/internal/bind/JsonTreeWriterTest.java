import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testJsonTreeWriter_DefaultConstructor() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertNull(jsonTreeWriter.get());
  }

  // Test get() method
  @Test
  public void testGet_WhenStackIsEmpty_ThrowsIllegalStateException() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    try {
      jsonTreeWriter.get();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testGet_WhenStackIsNotEmpty_ReturnsTopElement() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    JsonObject jsonObject = jsonTreeWriter.get();
    assertEquals("Expected JsonObject", jsonObject.getClass(), JsonObject.class);
    jsonTreeWriter.endObject();

    jsonTreeWriter.beginArray();
    JsonArray jsonArray = jsonTreeWriter.get();
    assertEquals("Expected JsonArray", jsonArray.getClass(), JsonArray.class);
    jsonTreeWriter.endArray();

    jsonTreeWriter.value("test string");
    JsonPrimitive jsonPrimitive = jsonTreeWriter.get();
    assertEquals("Expected JsonPrimitive", jsonPrimitive.getClass(), JsonPrimitive.class);
    assertEquals("Expected test string", jsonPrimitive.getAsString(), "test string");

    jsonTreeWriter.close();
    try {
      jsonTreeWriter.get();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  // beginObject() method tests

  @Test
  public void testBeginObject_WhenStackIsEmpty_ReturnsJsonObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonObject jsonObject = jsonTreeWriter.beginObject();
    assertEquals("Expected JsonObject", jsonObject.getClass(), JsonObject.class);
    jsonTreeWriter.endObject();
    assertEquals(0, jsonTreeWriter.stack.size());
  }

  @Test
  public void testBeginObject_WhenJsonObjectAlreadyOnStack_ThrowsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    try {
      jsonTreeWriter.beginObject();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testBeginObject_WhenJsonArrayOnStack_ThrowsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    try {
      jsonTreeWriter.beginObject();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
    jsonTreeWriter.endArray();
  }

  @Test
  public void testBeginObject_WhenJsonNullOnStack_ThrowsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    jsonTreeWriter.put(JsonNull.INSTANCE);
    try {
      jsonTreeWriter.beginObject();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  // endObject() method tests

  @Test
  public void testEndObject_WhenStackIsEmpty_ThrowsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    try {
      jsonTreeWriter.endObject();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testEndObject_WhenJsonObjectOnStack_ReturnsAndRemovesJsonObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonObject jsonObject1 = jsonTreeWriter.beginObject();
    JsonObject jsonObject2 = jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    JsonObject jsonObject3 = jsonTreeWriter.endObject();
    assertEquals("Expected JsonObject", jsonObject3.getClass(), JsonObject.class);
    assertEquals(1, jsonTreeWriter.stack.size());
    assertEquals(jsonObject3, jsonTreeWriter.peek());
  }

  @Test
  public void testEndObject_WhenJsonArrayOnStack_ThrowsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    try {
      jsonTreeWriter.endObject();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
    jsonTreeWriter.endArray();
  }

  @Test
  public void testEndObject_WhenJsonNullOnStack_ThrowsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    jsonTreeWriter.put(JsonNull.INSTANCE);
    try {
      jsonTreeWriter.endObject();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  // beginArray() method tests

  @Test
  public void testBeginArray_WhenStackIsEmpty_ReturnsJsonArray() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonArray jsonArray = jsonTreeWriter.beginArray();
    assertEquals("Expected JsonArray", jsonArray.getClass(), JsonArray.class);
    jsonTreeWriter.endArray();
    assertEquals(0, jsonTreeWriter.stack.size());
  }

  @Test
  public void testBeginArray_WhenJsonArrayAlreadyOnStack_ThrowsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
    try {
      jsonTreeWriter.beginArray();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testBeginArray_WhenJsonObjectOnStack_ThrowsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    try {
      jsonTreeWriter.beginArray();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
    jsonTreeWriter.endObject();
  }

  @Test
  public void testBeginArray_WhenJsonNullOnStack_ThrowsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    jsonTreeWriter.put(JsonNull.INSTANCE);
    try {
      jsonTreeWriter.beginArray();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  // endArray() method tests

  @Test
  public void testEndArray_WhenStackIsEmpty_ThrowsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    try {
      jsonTreeWriter.endArray();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testEndArray_WhenJsonArrayOnStack_ReturnsAndRemovesJsonArray() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonArray jsonArray1 = jsonTreeWriter.beginArray();
    JsonArray jsonArray2 = jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
    JsonArray jsonArray3 = jsonTreeWriter.endArray();
    assertEquals("Expected JsonArray", jsonArray3.getClass(), JsonArray.class);
    assertEquals(1, jsonTreeWriter.stack.size());
    assertEquals(jsonArray3, jsonTreeWriter.peek());
  }

  @Test
  public void testEndArray_WhenJsonObjectOnStack_ThrowsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    try {
      jsonTreeWriter.endArray();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
    jsonTreeWriter.endObject();
  }

  @Test
  public void testEndArray_WhenJsonNullOnStack_ThrowsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    jsonTreeWriter.put(JsonNull.INSTANCE);
    try {
      jsonTreeWriter.endArray();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  // name() method tests

  @Test
  public void testName_WhenStackIsEmpty_ThrowsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    try {
      jsonTreeWriter.name("test name");
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testName_WhenJsonObjectOnStack_SetsPendingName() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("test name");
    JsonObject jsonObject = jsonTreeWriter.get();
    assertEquals("Expected test name", jsonObject.name("test name").getAsString(), "test name");
    jsonTreeWriter.endObject();
  }

  @Test
  public void testName_WhenJsonArrayOnStack_ThrowsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    try {
      jsonTreeWriter.name("test name");
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
    jsonTreeWriter.endArray();
  }

  @Test
  public void testName_WhenJsonNullOnStack_ThrowsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    jsonTreeWriter.put(JsonNull.INSTANCE);
    try {
      jsonTreeWriter.name("test name");
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  // value() method tests

  @Test
  public void testValue_WhenNull_ReturnsNullValue() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(null);
    JsonPrimitive jsonPrimitive = jsonTreeWriter.get();
    assertNull(jsonPrimitive.isJsonNull());
  }

  @Test
  public void testValue_WhenNonNullableValue_ReturnsJsonPrimitive() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value("test string");
    JsonPrimitive jsonPrimitive = jsonTreeWriter.get();
    assertEquals("Expected test string", jsonPrimitive.getAsString(), "test string");
  }

  @Test
  public void testValue_WhenBooleanValue_ReturnsJsonPrimitive() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(true);
    JsonPrimitive jsonPrimitive = jsonTreeWriter.get();
    assertTrue(jsonPrimitive.isBoolean());
    assertTrue(jsonPrimitive.getAsBoolean());
  }

  @Test
  public void testValue_WhenDoubleValue_ReturnsJsonPrimitive() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(3.14);
    JsonPrimitive jsonPrimitive = jsonTreeWriter.get();
    assertEquals("Expected 3.14", jsonPrimitive.getAsDouble(), 3.14, 0.001);
  }

  @Test
  public void testValue_WhenLongValue_ReturnsJsonPrimitive() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(42);
    JsonPrimitive jsonPrimitive = jsonTreeWriter.get();
    assertEquals("Expected 42", jsonPrimitive.getAsLong(), 42);
  }

  @Test
  public void testValue_WhenNumberValue_ReturnsJsonPrimitive() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(new Integer(42));
    JsonPrimitive jsonPrimitive = jsonTreeWriter.get();
    assertEquals("Expected 42", jsonPrimitive.getAsInt(), 42);
  }

  // Test exception handling
  @Test(expected = IllegalStateException.class)
  public void testClose_WhenJsonObjectOrJsonArrayIsOnStack_ThrowsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testClose_WhenJsonArrayIsOnStack_ThrowsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.close();
  }

  @Test(expected = IOException.class)
  public void testClose_WhenStackIsEmpty_ThrowsIOException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.close();
  }
}