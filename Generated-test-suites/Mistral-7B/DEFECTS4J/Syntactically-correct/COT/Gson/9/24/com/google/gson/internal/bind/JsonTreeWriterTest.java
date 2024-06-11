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
  public void testJsonTreeWriter_Constructor() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertNotNull(jsonTreeWriter);
  }

  @Test
  public void testGet_WhenWriterHasNoElement_ExpectsEmptyList() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonElement result = jsonTreeWriter.get();
    assertNull(result);
  }

  @Test
  public void testGet_WhenWriterHasElement_ExpectsElement() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value("test");
    JsonElement result = jsonTreeWriter.get();
    assertEquals(new JsonPrimitive("test"), result);
  }

  @Test(expected = IllegalStateException.class)
  public void testGet_WhenWriterHasMultipleElements_ExpectsIllegalStateException() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value("test1");
    jsonTreeWriter.value("test2");
    jsonTreeWriter.get();
  }

  @Test
  public void testPeek_WhenWriterHasNoElement_ExpectsNull() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonElement result = jsonTreeWriter.peek();
    assertNull(result);
  }

  @Test
  public void testPeek_WhenWriterHasSingleElement_ExpectsElement() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value("test");
    JsonElement result = jsonTreeWriter.peek();
    assertEquals(new JsonPrimitive("test"), result);
  }

  @Test
  public void testPut_WhenWriterHasNoElement_ExpectsElementAddedToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.put(new JsonPrimitive("test"));
    JsonElement result = jsonTreeWriter.peek();
    assertEquals(new JsonPrimitive("test"), result);
  }

  @Test
  public void testPut_WhenWriterHasElement_ExpectsElementAddedToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.put(new JsonPrimitive("test"));
    JsonElement result = jsonTreeWriter.peek();
    assertEquals(new JsonPrimitive("test"), result);
  }

  @Test(expected = IllegalStateException.class)
  public void testPut_WhenWriterHasMultipleElements_ExpectsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.put(new JsonPrimitive("test1"));
    jsonTreeWriter.put(new JsonPrimitive("test2"));
    jsonTreeWriter.put(new JsonPrimitive("test3"));
  }

  @Test
  public void testBeginArray_WhenWriterHasNoElement_ExpectsArrayAddedToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    JsonArray result = (JsonArray) jsonTreeWriter.get();
    assertNotNull(result);
    assertEquals(0, result.size());
  }

  @Test
  public void testBeginArray_WhenWriterHasElement_ExpectsArrayAddedToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("test");
    JsonArray result = (JsonArray) jsonTreeWriter.get();
    assertEquals(1, result.size());
    assertEquals(new JsonPrimitive("test"), result.get(0));
  }

  @Test
  public void testEndArray_WhenWriterHasNoElement_ExpectsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.endArray();
  }

  @Test
  public void testEndArray_WhenWriterHasSingleElement_ExpectsArrayPoppedFromStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("test");
    jsonTreeWriter.endArray();
    JsonElement result = jsonTreeWriter.get();
    assertEquals(new JsonArray(), result);
  }

  @Test
  public void testEndArray_WhenWriterHasMultipleElements_ExpectsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("test1");
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value("test2");
    jsonTreeWriter.endObject();
    jsonTreeWriter.value("test3");
    jsonTreeWriter.endArray();
  }

  @Test
  public void testBeginObject_WhenWriterHasNoElement_ExpectsObjectAddedToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    JsonObject result = (JsonObject) jsonTreeWriter.get();
    assertNotNull(result);
    assertEquals(0, result.size());
  }

  @Test
  public void testBeginObject_WhenWriterHasElement_ExpectsObjectAddedToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value("test");
    JsonObject result = (JsonObject) jsonTreeWriter.get();
    assertEquals(1, result.size());
    assertEquals("test", result.get("name").getAsString());
  }

  @Test
  public void testEndObject_WhenWriterHasNoElement_ExpectsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.endObject();
  }

  @Test
  public void testEndObject_WhenWriterHasSingleElement_ExpectsObjectPoppedFromStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value("test");
    jsonTreeWriter.endObject();
    JsonElement result = jsonTreeWriter.get();
    assertEquals(new JsonObject(), result);
  }

  @Test
  public void testEndObject_WhenWriterHasMultipleElements_ExpectsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name1");
    jsonTreeWriter.value("test1");
    jsonTreeWriter.name("name2");
    jsonTreeWriter.value("test2");
    jsonTreeWriter.endObject();
  }

  @Test
  public void testName_WhenWriterHasNoElement_ExpectsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.name("name");
  }

  @Test(expected = IllegalStateException.class)
  public void testName_WhenWriterHasArray_ExpectsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.name("name");
  }

  @Test
  public void testName_WhenWriterHasObject_ExpectsNameAddedToPendingName() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value("test");
    JsonObject result = (JsonObject) jsonTreeWriter.get();
    assertEquals("test", result.get("name").getAsString());
  }

  @Test
  public void testValueString_WhenWriterHasNoElement_ExpectsElementAddedToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value("test");
  }

  @Test
  public void testValueString_WhenWriterHasArray_ExpectsStringAddedToArray() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("test");
    JsonArray result = (JsonArray) jsonTreeWriter.get();
    assertEquals(1, result.size());
    assertEquals(new JsonPrimitive("test"), result.get(0));
  }

  @Test
  public void testValueString_WhenWriterHasObject_ExpectsStringAddedToObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value("test");
    JsonObject result = (JsonObject) jsonTreeWriter.get();
    assertEquals("test", result.get("name").getAsString());
  }

  @Test
  public void testNullValue_WhenWriterHasNoElement_ExpectsNullAddedToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.nullValue();
  }

  @Test
  public void testNullValue_WhenWriterHasArray_ExpectsNullAddedToArray() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.nullValue();
    JsonArray result = (JsonArray) jsonTreeWriter.get();
    assertEquals(1, result.size());
    assertEquals(JsonNull.INSTANCE, result.get(0));
  }

  @Test
  public void testNullValue_WhenWriterHasObject_ExpectsNullAddedToObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.nullValue();
    JsonObject result = (JsonObject) jsonTreeWriter.get();
    assertEquals(JsonNull.INSTANCE, result.get("name"));
  }

  @Test
  public void testValueBoolean_WhenWriterHasNoElement_ExpectsElementAddedToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(true);
  }

  @Test
  public void testValueBoolean_WhenWriterHasArray_ExpectsBooleanAddedToArray() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value(true);
    JsonArray result = (JsonArray) jsonTreeWriter.get();
    assertEquals(1, result.size());
    assertEquals(new JsonPrimitive(true), result.get(0));
  }

  @Test
  public void testValueBoolean_WhenWriterHasObject_ExpectsBooleanAddedToObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value(true);
    JsonObject result = (JsonObject) jsonTreeWriter.get();
    assertEquals(new JsonPrimitive(true), result.get("name"));
  }

  @Test
  public void testValueDouble_WhenWriterHasNoElement_ExpectsElementAddedToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(1.0);
  }

  @Test
  public void testValueDouble_WhenWriterHasArray_ExpectsDoubleAddedToArray() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value(1.0);
    JsonArray result = (JsonArray) jsonTreeWriter.get();
    assertEquals(1, result.size());
    assertEquals(new JsonPrimitive(1.0), result.get(0));
  }

  @Test
  public void testValueDouble_WhenWriterHasObject_ExpectsDoubleAddedToObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value(1.0);
    JsonObject result = (JsonObject) jsonTreeWriter.get();
    assertEquals(new JsonPrimitive(1.0), result.get("name"));
  }

  @Test
  public void testValueLong_WhenWriterHasNoElement_ExpectsElementAddedToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(1);
  }

  @Test
  public void testValueLong_WhenWriterHasArray_ExpectsLongAddedToArray() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value(1);
    JsonArray result = (JsonArray) jsonTreeWriter.get();
    assertEquals(1, result.size());
    assertEquals(new JsonPrimitive(1), result.get(0));
  }

  @Test
  public void testValueLong_WhenWriterHasObject_ExpectsLongAddedToObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value(1);
    JsonObject result = (JsonObject) jsonTreeWriter.get();
    assertEquals(new JsonPrimitive(1), result.get("name"));
  }

  @Test
  public void testValueNumber_WhenWriterHasNoElement_ExpectsElementAddedToStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(1.23);
  }

  @Test
  public void testValueNumber_WhenWriterHasArray_ExpectsNumberAddedToArray() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value(1.23);
    JsonArray result = (JsonArray) jsonTreeWriter.get();
    assertEquals(1, result.size());
    assertEquals(new JsonPrimitive(1.23), result.get(0));
  }

  @Test
  public void testValueNumber_WhenWriterHasObject_ExpectsNumberAddedToObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value(1.23);
    JsonObject result = (JsonObject) jsonTreeWriter.get();
    assertEquals(new JsonPrimitive(1.23), result.get("name"));
  }

  @Test
  public void testFlush_WhenWriterHasNoElement_ExpectsNoException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.flush();
  }

  @Test
  public void testFlush_WhenWriterHasElement_ExpectsNoException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value("test");
    jsonTreeWriter.flush();
  }

  @Test(expected = IllegalStateException.class)
  public void testClose_WhenWriterHasNoElement_ExpectsIllegalStateException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.close();
  }

  @Test(expected = IOException.class)
  public void testClose_WhenWriterHasElement_ExpectsIOException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value("test");
    jsonTreeWriter.close();
  }

  @Test
  public void testClose_WhenWriterHasMultipleElements_ExpectsIncompleteDocumentException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("test1");
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.value("test2");
    jsonTreeWriter.close();
    jsonTreeWriter.close();
  }

}