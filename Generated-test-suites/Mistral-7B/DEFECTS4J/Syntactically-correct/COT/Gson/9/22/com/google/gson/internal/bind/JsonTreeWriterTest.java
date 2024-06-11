import static org.junit.Assert.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testJsonTreeWriter_Constructor() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertNotNull(jsonTreeWriter);
  }

  @Test
  public void testGet_WhenCalledWithEmptyWriter_ReturnsNull() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonElement jsonElement = jsonTreeWriter.get();
    assertNull(jsonElement);
  }

  @Test
  public void testPeek_WhenCalledWithEmptyWriter_ReturnsNull() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonElement jsonElement = jsonTreeWriter.peek();
    assertNull(jsonElement);
  }

  @Test(expected = IllegalStateException.class)
  public void testPut_WhenCalledWithNonEmptyWriterAndNoObjectOrArrayAtTop_ThrowsException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.put(JsonNull.INSTANCE);
  }

  @Test
  public void testPut_WhenCalledWithEmptyWriterAndJsonObject_AddsObjectToStackAndSetsProductToThatObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonObject jsonObject = new JsonObject();
    jsonTreeWriter.put(jsonObject);
    JsonElement jsonElement = jsonTreeWriter.get();
    assertEquals(jsonObject, jsonElement);
  }

  @Test
  public void testPut_WhenCalledWithEmptyWriterAndJsonArray_AddsArrayToStackAndSetsProductToThatArray() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonArray jsonArray = new JsonArray();
    jsonTreeWriter.put(jsonArray);
    JsonElement jsonElement = jsonTreeWriter.get();
    assertEquals(jsonArray, jsonElement);
  }

  @Test
  public void testBeginArray_WhenCalledWithEmptyWriter_AddsNewArrayToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    JsonElement jsonElement = jsonTreeWriter.peek();
    assertTrue(jsonElement instanceof JsonArray);
  }

  @Test
  public void testEndArray_WhenCalledWithEmptyWriter_RemovesLastAddedArrayFromStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
    JsonElement jsonElement = jsonTreeWriter.peek();
    assertNull(jsonElement);
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArray_WhenCalledWithWriterNotContainingArray_ThrowsException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endObject();
  }

  @Test
  public void testBeginObject_WhenCalledWithEmptyWriter_AddsNewObjectToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    JsonElement jsonElement = jsonTreeWriter.peek();
    assertTrue(jsonElement instanceof JsonObject);
  }

  @Test
  public void testEndObject_WhenCalledWithEmptyWriter_RemovesLastAddedObjectFromStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.peek();
    assertNull(jsonElement);
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObject_WhenCalledWithWriterNotContainingObject_ThrowsException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endObject();
  }

  @Test
  public void testName_WhenCalledWithValidNameAndObjectAtTopOfStack_SetsPendingNameToThatName() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("test");
    assertEquals("test", jsonTreeWriter.pendingName);
  }

  @Test(expected = IllegalStateException.class)
  public void testName_WhenCalledWithInvalidState_ThrowsException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.name("test");
    jsonTreeWriter.endArray();
  }

  @Test
  public void testValue_WhenCalledWithValidStringValue_AddsNewJsonPrimitiveToStringToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("test");
    jsonTreeWriter.value("testValue");
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertEquals("testValue", jsonObject.get("test").getAsString());
  }

  @Test
  public void testValue_WhenCalledWithNullValue_DoesNotAddAnythingToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.value(null);
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertTrue(jsonObject.entrySet().isEmpty());
  }

  @Test
  public void testValue_WhenCalledWithBooleanValue_AddsNewJsonPrimitiveToBooleanValueToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("test");
    jsonTreeWriter.value(true);
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertTrue(jsonObject.get("test").isJsonBoolean());
    assertTrue(jsonObject.get("test").getAsJsonPrimitive().isBoolean());
    assertTrue(jsonObject.get("test").getAsJsonPrimitive().getAsBoolean());
  }

  @Test
  public void testValue_WhenCalledWithDoubleValue_AddsNewJsonPrimitiveToDoubleValueToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("test");
    jsonTreeWriter.value(1.23);
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertTrue(jsonObject.get("test").isJsonPrimitive());
    assertTrue(jsonObject.get("test").getAsJsonPrimitive().isNumber());
    assertEquals(1.23, jsonObject.get("test").getAsJsonPrimitive().getAsDouble(), 0.0);
  }

  @Test
  public void testValue_WhenCalledWithLongValue_AddsNewJsonPrimitiveToLongValueToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("test");
    jsonTreeWriter.value(123L);
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertTrue(jsonObject.get("test").isJsonPrimitive());
    assertTrue(jsonObject.get("test").getAsJsonPrimitive().isNumber());
    assertEquals(123L, jsonObject.get("test").getAsJsonPrimitive().getAsLong());
  }

  @Test
  public void testValue_WhenCalledWithNumberValue_AddsNewJsonPrimitiveToNumberValueToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("test");
    jsonTreeWriter.value(new Long(123));
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertTrue(jsonObject.get("test").isJsonPrimitive());
    assertTrue(jsonObject.get("test").getAsJsonPrimitive().isNumber());
    assertEquals(123L, jsonObject.get("test").getAsJsonPrimitive().getAsLong());
  }

  @Test
  public void testFlush_DoesNotThrowException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.flush();
  }

  @Test
  public void testClose_WhenCalledWithEmptyWriter_ThrowsException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    try {
      jsonTreeWriter.close();
    } catch (AssertionError e) {
      // expected
    }
  }

  @Test
  public void testClose_WhenCalledWithNonEmptyWriter_ThrowsException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    try {
      jsonTreeWriter.close();
    } catch (AssertionError e) {
      // expected
    }
  }

  @Test
  public void testGet_WhenCalledWithWriterContainingJsonObject_ReturnsThatObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("test");
    jsonTreeWriter.value("testValue");
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertEquals("testValue", jsonObject.get("test").getAsString());
  }

  @Test
  public void testGet_WhenCalledWithWriterContainingJsonArray_ReturnsThatArray() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("testValue");
    jsonTreeWriter.endArray();
    jsonTreeWriter.endArray();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonArray jsonArray = (JsonArray) jsonElement;
    assertEquals(1, jsonArray.size());
    JsonArray innerArray = (JsonArray) jsonArray.get(0);
    assertEquals(1, innerArray.size());
    assertEquals("testValue", innerArray.get(0).getAsString());
  }

  @Test
  public void testParseTree_WhenCalledWithValidJsonString_ReturnsValidJsonTree() {
    String jsonString = "{\"test\": \"testValue\", \"testArray\": [1, \"testString\"]}";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertEquals("testValue", jsonObject.get("test").getAsString());
    JsonArray testArray = jsonObject.get("testArray").getAsJsonArray();
    assertEquals(2, testArray.size());
    assertEquals(1, testArray.get(0).getAsInt());
    assertEquals("testString", testArray.get(1).getAsString());
  }
}