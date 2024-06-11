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
  public void testJsonTreeWriter_constructor() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertNotNull(jsonTreeWriter);
  }

  @Test
  public void testGet_whenCalledWithEmptyStack_returnsNull() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertEquals(JsonNull.INSTANCE, jsonTreeWriter.get());
  }

  @Test
  public void testGet_whenCalledWithNonEmptyStack_returnsJsonElement() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("test");
    jsonTreeWriter.endArray();
    JsonElement jsonElement = jsonTreeWriter.get();
    assertNotNull(jsonElement);
    assertTrue(jsonElement instanceof JsonArray);
  }

  @Test(expected = IllegalStateException.class)
  public void testGet_whenCalledWithNonEmptyStackAndUnexpectedJsonType_throwsException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("test");
    jsonTreeWriter.endObject();
    jsonTreeWriter.endArray();
    jsonTreeWriter.get();
  }

  @Test
  public void testPeek_whenCalledWithEmptyStack_returnsNull() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertEquals(null, jsonTreeWriter.peek());
  }

  @Test
  public void testPeek_whenCalledWithNonEmptyArrayStack_returnsJsonArray() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("test");
    JsonArray jsonArray = (JsonArray) jsonTreeWriter.peek();
    assertNotNull(jsonArray);
  }

  @Test
  public void testPeek_whenCalledWithNonEmptyObjectStack_returnsJsonObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("test");
    JsonObject jsonObject = (JsonObject) jsonTreeWriter.peek();
    assertNotNull(jsonObject);
  }

  @Test(expected = IllegalStateException.class)
  public void testPeek_whenCalledWithEmptyObjectStackAndNameSet_throwsException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("test");
    jsonTreeWriter.peek();
  }

  @Test(expected = IllegalStateException.class)
  public void testPeek_whenCalledWithNonEmptyStackAndUnexpectedJsonType_throwsException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("test");
    jsonTreeWriter.peek();
  }

  @Test
  public void testPut_whenCalledWithJsonNull_addsNullToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.put(JsonNull.INSTANCE);
    JsonElement jsonElement = jsonTreeWriter.get();
    assertEquals(JsonNull.INSTANCE, jsonElement);
  }

  @Test
  public void testPut_whenCalledWithJsonPrimitive_addsJsonPrimitiveToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.put(new JsonPrimitive("test"));
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonArray jsonArray = (JsonArray) jsonElement;
    assertEquals(1, jsonArray.size());
    assertEquals("test", jsonArray.get(0).getAsString());
  }

  @Test
  public void testPut_whenCalledWithJsonObject_addsJsonObjectToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("test");
    jsonTreeWriter.put(new JsonObject());
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertTrue(jsonObject.entrySet().isEmpty());
  }

  @Test
  public void testBeginArray_whenCalled_addsJsonArrayToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonArray jsonArray = (JsonArray) jsonElement;
    assertEquals(0, jsonArray.size());
  }

  @Test
  public void testEndArray_whenCalledWithEmptyStack_throwsException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.endArray();
    try {
      jsonTreeWriter.get();
      fail();
    } catch (IllegalStateException e) {
      // expected
    }
  }

  @Test
  public void testEndArray_whenCalledWithNonEmptyStack_removesLastElement() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("test1");
    jsonTreeWriter.value("test2");
    jsonTreeWriter.endArray();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonArray jsonArray = (JsonArray) jsonElement;
    assertEquals(1, jsonArray.size());
    assertEquals("test2", jsonArray.get(0).getAsString());
  }

  @Test
  public void testBeginObject_whenCalled_addsJsonObjectToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertTrue(jsonObject.entrySet().isEmpty());
  }

  @Test
  public void testEndObject_whenCalledWithEmptyStack_throwsException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.endObject();
    try {
      jsonTreeWriter.get();
      fail();
    } catch (IllegalStateException e) {
      // expected
    }
  }

  @Test
  public void testEndObject_whenCalledWithNonEmptyStack_removesLastElement() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("test1");
    jsonTreeWriter.value("testValue1");
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertEquals(1, jsonObject.size());
    assertEquals("testValue1", jsonObject.entrySet().iterator().next().getValue().getAsString());
  }

  @Test
  public void testName_whenCalledWithNullName_throwsException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    try {
      jsonTreeWriter.name(null);
      fail();
    } catch (IllegalStateException e) {
      // expected
    }
    jsonTreeWriter.endObject();
  }

  @Test
  public void testName_whenCalledWithNonEmptyStackAndEmptyName_throwsException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("test");
    try {
      jsonTreeWriter.name("");
      fail();
    } catch (IllegalStateException e) {
      // expected
    }
    jsonTreeWriter.endObject();
  }

  @Test
  public void testName_whenCalledWithNonEmptyStackAndNonEmptyName_addsNameToLastObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("test1");
    jsonTreeWriter.name("test2");
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertEquals(1, jsonObject.size());
    JsonObject lastObject = jsonObject.entrySet().iterator().next().getValue().getAsJsonObject();
    assertEquals(1, lastObject.size());
    assertEquals("test2", lastObject.entrySet().iterator().next().getName());
  }

  @Test
  public void testValue_whenCalledWithNullValue_addsNullToLastElement() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value(null);
    jsonTreeWriter.endArray();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonArray jsonArray = (JsonArray) jsonElement;
    assertEquals(1, jsonArray.size());
    assertEquals(JsonNull.INSTANCE, jsonArray.get(0));
  }

  @Test
  public void testValue_whenCalledWithNonEmptyStackAndStringValue_addsStringToLastElement() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("test");
    jsonTreeWriter.endArray();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonArray jsonArray = (JsonArray) jsonElement;
    assertEquals(1, jsonArray.size());
    assertEquals("test", jsonArray.get(0).getAsString());
  }

  @Test
  public void testValue_whenCalledWithNonEmptyStackAndBooleanValue_addsBooleanToLastElement() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("test");
    jsonTreeWriter.value(true);
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertEquals(1, jsonObject.size());
    JsonPrimitive jsonPrimitive = jsonObject.entrySet().iterator().next().getValue().getAsJsonPrimitive();
    assertTrue(jsonPrimitive.isBoolean());
    assertTrue(jsonPrimitive.getAsBoolean());
  }

  @Test
  public void testValue_whenCalledWithNonEmptyStackAndDoubleValue_addsDoubleToLastElement() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("test");
    jsonTreeWriter.value(3.14159265);
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertEquals(1, jsonObject.size());
    JsonPrimitive jsonPrimitive = jsonObject.entrySet().iterator().next().getValue().getAsJsonPrimitive();
    assertTrue(jsonPrimitive.isNumber());
    assertEquals(3.14159265, jsonPrimitive.getAsDouble(), 0.0000001);
  }

  @Test
  public void testValue_whenCalledWithNonEmptyStackAndLongValue_addsLongToLastElement() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("test");
    jsonTreeWriter.value(42L);
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertEquals(1, jsonObject.size());
    JsonPrimitive jsonPrimitive = jsonObject.entrySet().iterator().next().getValue().getAsJsonPrimitive();
    assertTrue(jsonPrimitive.isNumber());
    assertEquals(42L, jsonPrimitive.getAsLong());
  }

  @Test
  public void testValue_whenCalledWithNonEmptyStackAndNumberValue_addsNumberToLastElement() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("test");
    jsonTreeWriter.value(new JsonPrimitive(3.14159265));
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = (JsonObject) jsonElement;
    assertEquals(1, jsonObject.size());
    JsonPrimitive jsonPrimitive = jsonObject.entrySet().iterator().next().getValue().getAsJsonPrimitive();
    assertTrue(jsonPrimitive.isNumber());
    assertEquals(3.14159265, jsonPrimitive.getAsDouble(), 0.0000001);
  }

  @Test(expected = IllegalStateException.class)
  public void testValue_whenCalledWithNonEmptyStackAndUnexpectedType_throwsException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value(new JsonObject());
    jsonTreeWriter.endArray();
  }

  @Test
  public void testFlush_doesNotHaveEffect() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.flush();
    JsonElement jsonElement = jsonTreeWriter.get();
    assertEquals(JsonNull.INSTANCE, jsonElement);
  }

  @Test
  public void testClose_whenCalledWithEmptyStack_throwsException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.close();
    try {
      jsonTreeWriter.get();
      fail();
    } catch (IllegalStateException e) {
      // expected
    }
  }

  @Test
  public void testClose_whenCalledWithNonEmptyStack_addsSentinalToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("test");
    jsonTreeWriter.close();
    JsonElement jsonElement = jsonTreeWriter.get();
    JsonArray jsonArray = (JsonArray) jsonElement;
    assertEquals(1, jsonArray.size());
    assertEquals("test", jsonArray.get(0).getAsString());
    List<JsonElement> stack = jsonTreeWriter.stack;
    JsonPrimitive sentinel = (JsonPrimitive) stack.get(stack.size() - 1);
    assertEquals("closed", sentinel.getAsString());
  }

  @Test
  public void testToString_convertsJsonTreeWriterToJsonString() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("test1");
    jsonTreeWriter.value("test2");
    jsonTreeWriter.endArray();
    String jsonString = jsonTreeWriter.toString();
    assertEquals("[\n  \"test1\"\n,\n  \"test2\"\n]", jsonString);
  }

  @Test
  public void testToString_convertsJsonTreeWriterToJsonObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("test1");
    jsonTreeWriter.value("testValue1");
    jsonTreeWriter.name("test2");
    jsonTreeWriter.value("testValue2");
    jsonTreeWriter.endObject();
    String jsonString = jsonTreeWriter.toString();
    assertEquals("{\n  \"test1\": \"testValue1\"\n,\n  \"test2\": \"testValue2\"\n}", jsonString);
  }
}