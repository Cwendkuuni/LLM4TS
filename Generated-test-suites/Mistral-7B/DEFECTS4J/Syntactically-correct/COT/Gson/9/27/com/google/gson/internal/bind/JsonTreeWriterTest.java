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
  public void testGet_whenEmpty_returnsNull() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonElement result = jsonTreeWriter.get();
    assertNull(result);
  }

  @Test
  public void testGet_whenNotEmpty_returnsExpectedJsonElement() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value("John Doe");
    jsonTreeWriter.endObject();
    JsonElement result = jsonTreeWriter.get();

    JsonObject expectedJsonObject = new JsonObject();
    expectedJsonObject.addProperty("name", "John Doe");

    assertEquals(expectedJsonObject, result);
  }

  @Test
  public void testPeek_whenEmpty_returnsNull() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonElement result = jsonTreeWriter.peek();
    assertNull(result);
  }

  @Test
  public void testPeek_whenStackContainsArray_returnsExpectedJsonArray() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("element1");
    jsonTreeWriter.value("element2");
    jsonTreeWriter.endArray();
    JsonElement result = jsonTreeWriter.peek();

    JsonArray expectedJsonArray = new JsonArray();
    expectedJsonArray.add(new JsonPrimitive("element1"));
    expectedJsonArray.add(new JsonPrimitive("element2"));

    assertEquals(expectedJsonArray, result);
  }

  @Test
  public void testPeek_whenStackContainsObject_returnsExpectedJsonObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value("John Doe");
    jsonTreeWriter.endObject();
    JsonElement result = jsonTreeWriter.peek();

    JsonObject expectedJsonObject = new JsonObject();
    expectedJsonObject.addProperty("name", "John Doe");

    assertEquals(expectedJsonObject, result);
  }

  @Test
  public void testPut_whenStackIsEmpty_addsExpectedJsonPrimitiveToStackAndProduct() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value("value1");

    JsonElement expectedJsonElement = new JsonPrimitive("value1");
    JsonElement result = jsonTreeWriter.get();

    assertEquals(expectedJsonElement, result);
  }

  @Test
  public void testPut_whenStackContainsArray_addsExpectedJsonPrimitiveToArray() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("element1");
    jsonTreeWriter.value("element2");
    jsonTreeWriter.put(new JsonPrimitive("element3"));
    jsonTreeWriter.endArray();

    JsonArray expectedJsonArray = new JsonArray();
    expectedJsonArray.add(new JsonPrimitive("element1"));
    expectedJsonArray.add(new JsonPrimitive("element2"));
    expectedJsonArray.add(new JsonPrimitive("element3"));

    JsonArray resultJsonArray = (JsonArray) jsonTreeWriter.get();

    assertEquals(expectedJsonArray, resultJsonArray);
  }

  @Test
  public void testPut_whenStackContainsObject_addsExpectedJsonPrimitiveToObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.put(new JsonPrimitive("John Doe"));
    jsonTreeWriter.endObject();

    JsonObject expectedJsonObject = new JsonObject();
    expectedJsonObject.addProperty("name", "John Doe");

    JsonElement result = jsonTreeWriter.get();

    assertEquals(expectedJsonObject, result);
  }

  @Test
  public void testBeginArray_whenCalledTwice_returnsExpectedJsonArray() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.beginArray();
    JsonElement result = jsonTreeWriter.peek();

    JsonArray expectedJsonArray = new JsonArray();
    expectedJsonArray.add(new JsonArray());

    assertEquals(expectedJsonArray, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArray_whenStackDoesNotContainObject_throwsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("element");
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObject_whenStackDoesNotContainArray_throwsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.value("element");
  }

  @Test
  public void testBeginObject_whenCalledTwice_returnsExpectedJsonObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.beginObject();
    JsonElement result = jsonTreeWriter.peek();

    JsonObject expectedJsonObject = new JsonObject();
    expectedJsonObject.add(new JsonObject());

    assertEquals(expectedJsonObject, result);
  }

  @Test
  public void testName_whenCalledMultipleTimes_addsExpectedNameToCurrentObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name1");
    jsonTreeWriter.name("name2");
    jsonTreeWriter.name("name3");
    jsonTreeWriter.endObject();

    JsonObject expectedJsonObject = new JsonObject();
    expectedJsonObject.addProperty("name1", null);
    expectedJsonObject.addProperty("name2", null);
    expectedJsonObject.addProperty("name3", null);

    JsonElement result = jsonTreeWriter.get();

    assertEquals(expectedJsonObject, result);
  }

  @Test
  public void testValue_whenCalledWithString_addsExpectedJsonPrimitiveToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value("John Doe");
    jsonTreeWriter.endObject();

    JsonObject expectedJsonObject = new JsonObject();
    expectedJsonObject.addProperty("name", "John Doe");

    JsonElement result = jsonTreeWriter.get();

    assertEquals(expectedJsonObject, result);
  }

  @Test
  public void testValue_whenCalledWithBoolean_addsExpectedJsonPrimitiveToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value(true);
    jsonTreeWriter.endObject();

    JsonObject expectedJsonObject = new JsonObject();
    expectedJsonObject.addProperty("name", true);

    JsonElement result = jsonTreeWriter.get();

    assertEquals(expectedJsonObject, result);
  }

  @Test
  public void testValue_whenCalledWithDouble_addsExpectedJsonPrimitiveToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value(3.14);
    jsonTreeWriter.endObject();

    JsonObject expectedJsonObject = new JsonObject();
    expectedJsonObject.addProperty("name", 3.14);

    JsonElement result = jsonTreeWriter.get();

    assertEquals(expectedJsonObject, result);
  }

  @Test
  public void testValue_whenCalledWithLong_addsExpectedJsonPrimitiveToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value(42);
    jsonTreeWriter.endObject();

    JsonObject expectedJsonObject = new JsonObject();
    expectedJsonObject.addProperty("name", 42);

    JsonElement result = jsonTreeWriter.get();

    assertEquals(expectedJsonObject, result);
  }

  @Test
  public void testValue_whenCalledWithNumber_addsExpectedJsonPrimitiveToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value(new Double(3.14));
    jsonTreeWriter.endObject();

    JsonObject expectedJsonObject = new JsonObject();
    expectedJsonObject.addProperty("name", 3.14);

    JsonElement result = jsonTreeWriter.get();

    assertEquals(expectedJsonObject, result);
  }

  @Test
  public void testNullValue_whenCalled_addsExpectedJsonNullToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.nullValue();
    jsonTreeWriter.endObject();

    JsonObject expectedJsonObject = new JsonObject();
    expectedJsonObject.addProperty("name", null);

    JsonElement result = jsonTreeWriter.get();

    assertEquals(expectedJsonObject, result);
  }

  @Test
  public void testFlush() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value("John Doe");
    jsonTreeWriter.endObject();

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream);
    jsonTreeWriter.setWriter(outputWriter);
    jsonTreeWriter.flush();

    String expectedJsonString = "{\"name\":\"John Doe\"}";
    String resultJsonString = outputStream.toString();

    assertEquals(expectedJsonString, resultJsonString);
  }

  @Test
  public void testClose_whenCalledTwice_throwsIOException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.close();
    jsonTreeWriter.close();

    try {
      jsonTreeWriter.get();
      fail();
    } catch (IllegalStateException e) {
      // expected
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testGet_whenClosed_throwsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.close();

    jsonTreeWriter.get();
  }
}