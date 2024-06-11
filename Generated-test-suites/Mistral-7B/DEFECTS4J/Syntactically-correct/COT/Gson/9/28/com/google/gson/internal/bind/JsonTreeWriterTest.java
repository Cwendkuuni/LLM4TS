import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testJsonTreeWriter_Constructor() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertNotNull(jsonTreeWriter);
  }

  @Test
  public void testGet_WhenCalledWithEmptyStack_ReturnsNull() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonElement result = jsonTreeWriter.get();
    assertNull(result);
  }

  @Test
  public void testGet_WhenCalledWithNonEmptyStack_ReturnsTopElement() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.endObject();
    JsonElement result = jsonTreeWriter.get();
    JsonObject expected = new JsonObject();
    expected.addProperty("name", "John Doe");
    assertEquals(expected, result);
  }

  @Test
  public void testPeek_WhenCalledWithEmptyStack_ReturnsNull() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonElement result = jsonTreeWriter.peek();
    assertNull(result);
  }

  @Test
  public void testPeek_WhenCalledWithNonEmptyStack_ReturnsTopElement() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.endObject();
    JsonElement result = jsonTreeWriter.peek();
    JsonObject expected = new JsonObject();
    expected.addProperty("name", "John Doe");
    assertEquals(expected, result);
  }

  @Test
  public void testPut_WhenCalledWithJsonObjectAndPendingName_AddsNameValuePair() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value("John Doe");
    jsonTreeWriter.endObject();
    JsonObject expected = new JsonObject();
    expected.addProperty("name", "John Doe");
    JsonElement result = jsonTreeWriter.get();
    assertEquals(expected, result);
  }

  @Test
  public void testPut_WhenCalledWithJsonPrimitive_AddsToTopOfStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value("Hello");
    JsonElement result = jsonTreeWriter.peek();
    assertEquals(new JsonPrimitive("Hello"), result);
  }

  @Test
  public void testBeginArray_WhenCalled_AddsJsonArrayToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    JsonElement result = jsonTreeWriter.peek();
    assertTrue(result instanceof JsonArray);
  }

  @Test
  public void testEndArray_WhenCalledWithEmptyStack_ThrowsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.endArray();
    try {
      jsonTreeWriter.get();
      fail();
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testEndArray_WhenCalledWithNonEmptyJsonArray_RemovesJsonArrayFromStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("Hello");
    jsonTreeWriter.endArray();
    JsonElement result = jsonTreeWriter.peek();
    assertEquals(JsonNull.INSTANCE, result);
  }

  @Test
  public void testBeginObject_WhenCalled_AddsJsonObjectToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    JsonElement result = jsonTreeWriter.peek();
    assertTrue(result instanceof JsonObject);
  }

  @Test
  public void testEndObject_WhenCalledWithEmptyStack_ThrowsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.endObject();
    try {
      jsonTreeWriter.get();
      fail();
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testEndObject_WhenCalledWithNonEmptyJsonObject_RemovesJsonObjectFromStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.endObject();
    JsonElement result = jsonTreeWriter.peek();
    assertEquals(JsonNull.INSTANCE, result);
  }

  @Test
  public void testName_WhenCalledWithEmptyStack_ThrowsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    try {
      jsonTreeWriter.name("name");
      fail();
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testName_WhenCalledWithNonEmptyStack_SetsPendingName() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.endObject();
    assertEquals("name", jsonTreeWriter.pendingName);
  }

  @Test
  public void testValue_WhenCalledWithString_AddsJsonPrimitiveToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value("Hello");
    JsonElement result = jsonTreeWriter.peek();
    assertEquals(new JsonPrimitive("Hello"), result);
  }

  @Test
  public void testValue_WhenCalledWithNull_AddsJsonNullToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(null);
    JsonElement result = jsonTreeWriter.peek();
    assertEquals(JsonNull.INSTANCE, result);
  }

  @Test
  public void testValue_WhenCalledWithBoolean_AddsJsonPrimitiveToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(true);
    JsonElement result = jsonTreeWriter.peek();
    assertEquals(new JsonPrimitive(true), result);
  }

  @Test
  public void testValue_WhenCalledWithDouble_AddsJsonPrimitiveToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(3.14);
    JsonElement result = jsonTreeWriter.peek();
    assertEquals(new JsonPrimitive(3.14), result);
  }

  @Test
  public void testValue_WhenCalledWithLong_AddsJsonPrimitiveToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(42);
    JsonElement result = jsonTreeWriter.peek();
    assertEquals(new JsonPrimitive(42), result);
  }

  @Test
  public void testValue_WhenCalledWithNumber_AddsJsonPrimitiveToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(new Long(42));
    JsonElement result = jsonTreeWriter.peek();
    assertEquals(new JsonPrimitive(42), result);
  }

  @Test
  public void testFlush_DoesNotThrowIOException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.flush();
  }

  @Test
  public void testClose_WhenCalledWithEmptyStack_ThrowsIOException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    try {
      jsonTreeWriter.close();
      fail();
    } catch (IOException e) {
      // Expected
    }
  }

  @Test
  public void testClose_WhenCalledWithNonEmptyStack_ThrowsIOException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.close();
    try {
      jsonTreeWriter.get();
      fail();
    } catch (IOException e) {
      // Expected
    }
  }

  @Test
  public void testClose_WhenCalledWithNonEmptyStackAndLenientModeOff_ThrowsIllegalArgumentException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter(false);
    jsonTreeWriter.beginObject();
    try {
      jsonTreeWriter.close();
      fail();
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testClose_WhenCalledWithNonEmptyStackAndLenientModeOn_AddsSentinalElementToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter(false);
    jsonTreeWriter.beginObject();
    jsonTreeWriter.close();
    JsonElement result = jsonTreeWriter.peek();
    assertEquals(SENTINEL_CLOSED, result);
  }

  @Test
  public void testToString_WhenCalledWithEmptyStack_ReturnsEmptyString() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    String result = jsonTreeWriter.toString();
    assertEquals("", result);
  }

  @Test
  public void testToString_WhenCalledWithStackContainingJsonArray_ReturnsJsonStringRepresentationOfJsonArray() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("Hello");
    jsonTreeWriter.value("World");
    jsonTreeWriter.endArray();
    String result = jsonTreeWriter.toString();
    String expected = "[\"Hello\",\"World\"]";
    assertEquals(expected, result);
  }

  @Test
  public void testToString_WhenCalledWithStackContainingJsonObject_ReturnsJsonStringRepresentationOfJsonObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.name("age").value(30);
    jsonTreeWriter.endObject();
    String result = jsonTreeWriter.toString();
    String expected = "{\"name\":\"John Doe\",\"age\":30}";
    assertEquals(expected, result);
  }

  @Test
  public void testToString_WhenCalledWithStackContainingJsonPrimitive_ReturnsJsonStringRepresentationOfJsonPrimitive() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value("Hello");
    String result = jsonTreeWriter.toString();
    String expected = "\"Hello\"";
    assertEquals(expected, result);
  }

  @Test
  public void testToString_WhenCalledWithStackContainingNull_ReturnsJsonStringRepresentationOfJsonNull() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(null);
    String result = jsonTreeWriter.toString();
    String expected = "null";
    assertEquals(expected, result);
  }

  @Test
  public void testToString_WhenCalledWithStackContainingBoolean_ReturnsJsonStringRepresentationOfJsonPrimitive() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(true);
    String result = jsonTreeWriter.toString();
    String expected = "true";
    assertEquals(expected, result);
  }

  @Test
  public void testToString_WhenCalledWithStackContainingNumber_ReturnsJsonStringRepresentationOfJsonPrimitive() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(3.14);
    String result = jsonTreeWriter.toString();
    String expected = "3.14";
    assertEquals(expected, result);
  }

  @Test
  public void testToString_WhenCalledWithStackContainingMixedTypes_ReturnsJsonStringRepresentationOfStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("Hello");
    jsonTreeWriter.value(3.14);
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.name("age").value(30);
    jsonTreeWriter.endObject();
    jsonTreeWriter.value("World");
    jsonTreeWriter.endArray();
    String result = jsonTreeWriter.toString();
    String expected = "[\"Hello\",3.14,{\"name\":\"John Doe\",\"age\":30,\"\":\"World\"}]";
    assertEquals(expected, result);
  }

  @Test
  public void testToString_WhenCalledWithStackNotClosed_ThrowsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.name("age");
    try {
      jsonTreeWriter.toString();
      fail();
    } catch (IllegalStateException e) {
      // Expected
    }
  }
}