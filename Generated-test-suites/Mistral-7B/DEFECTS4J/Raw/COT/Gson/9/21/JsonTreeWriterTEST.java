import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class JsonTreeWriterTest {

  private JsonTreeWriter writer;
  private ByteArrayOutputStream outputStream;

  @Before
  public void setUp() {
    writer = new JsonTreeWriter();
    outputStream = new ByteArrayOutputStream();
    Writer writerForTest = new OutputStreamWriter(outputStream);
    writer.setWriter(writerForTest);
  }

  @Test
  public void testDefaultConstruction() {
    JsonTreeWriter writer = new JsonTreeWriter();
    assertNull(writer.get());
  }

  @Test
  public void testBeginArray() throws IOException {
    writer.beginArray();
    JsonArray array = writer.get();
    assertEquals(JsonArray.class, array.getClass());
    assertFalse(array.isJsonNull());
  }

  @Test
  public void testEndArray() throws IOException {
    writer.beginArray();
    writer.endArray();
    JsonElement element = writer.get();
    JsonArray array = (JsonArray) element;
    assertFalse(array.isJsonNull());
    assertSame(JsonNull.INSTANCE, array.get(0));
  }

  @Test
  public void testBeginObject() throws IOException {
    writer.beginObject();
    JsonObject object = writer.get();
    assertEquals(JsonObject.class, object.getClass());
    assertFalse(object.isJsonNull());
  }

  @Test
  public void testEndObject() throws IOException {
    writer.beginObject();
    writer.endObject();
    JsonElement element = writer.get();
    JsonObject object = (JsonObject) element;
    assertFalse(object.isJsonNull());
  }

  @Test
  public void testName() throws IOException {
    writer.name("name");
    writer.value("value");
    JsonObject object = (JsonObject) writer.get();
    JsonPrimitive namePrimitive = object.getAsJsonPrimitive("name");
    assertEquals("name", namePrimitive.getAsString());
    JsonPrimitive valuePrimitive = object.getAsJsonPrimitive("value");
    assertEquals("value", valuePrimitive.getAsString());
  }

  @Test
  public void testValueString() throws IOException {
    writer.value("value");
    JsonElement element = writer.get();
    JsonPrimitive jsonPrimitive = (JsonPrimitive) element;
    assertEquals("value", jsonPrimitive.getAsString());
  }

  @Test
  public void testValueBoolean() throws IOException {
    writer.value(true);
    JsonElement element = writer.get();
    JsonPrimitive jsonPrimitive = (JsonPrimitive) element;
    assertEquals(true, jsonPrimitive.getAsBoolean());
  }

  @Test
  public void testValueNumber() throws IOException {
    writer.value(1);
    JsonElement element = writer.get();
    JsonPrimitive jsonPrimitive = (JsonPrimitive) element;
    assertEquals(1, jsonPrimitive.getAsInt());

    writer.value(4.5);
    element = writer.get();
    jsonPrimitive = (JsonPrimitive) element;
    assertEquals(4.5, jsonPrimitive.getAsDouble(), 0.0);
  }

  @Test
  public void testValueNull() throws IOException {
    writer.nullValue();
    JsonElement element = writer.get();
    JsonNull jsonNull = (JsonNull) element;
    assertNull(jsonNull.isJsonNull());
  }

  @Test
  public void testBeginArrayAddValue() throws IOException {
    writer.beginArray();
    writer.value("value");
    JsonArray array = (JsonArray) writer.get();
    assertEquals(1, array.size());
    JsonPrimitive jsonPrimitive = array.get(0).getAsJsonPrimitive();
    assertEquals("value", jsonPrimitive.getAsString());
  }

  @Test
  public void testBeginObjectAddNameValue() throws IOException {
    writer.beginObject();
    writer.name("name");
    writer.value("value");
    JsonObject object = (JsonObject) writer.get();
    JsonPrimitive namePrimitive = object.getAsJsonPrimitive("name");
    assertEquals("name", namePrimitive.getAsString());
    JsonPrimitive valuePrimitive = object.getAsJsonPrimitive("value");
    assertEquals("value", valuePrimitive.getAsString());
  }

  @Test
  public void testBeginObjectAddNameArray() throws IOException {
    writer.beginObject();
    writer.name("name");
    writer.beginArray();
    writer.value("value1");
    writer.value("value2");
    writer.endArray();
    writer.endObject();
    JsonObject object = (JsonObject) writer.get();
    JsonPrimitive namePrimitive = object.getAsJsonPrimitive("name");
    assertEquals("name", namePrimitive.getAsString());
    JsonArray array = object.getAsJsonArray();
    assertEquals(2, array.size());
    JsonPrimitive value1Primitive = array.get(0).getAsJsonPrimitive();
    assertEquals("value1", value1Primitive.getAsString());
    JsonPrimitive value2Primitive = array.get(1).getAsJsonPrimitive();
    assertEquals("value2", value2Primitive.getAsString());
  }

  @Test
  public void testBeginObjectAddNameObject() throws IOException {
    writer.beginObject();
    writer.name("name");
    writer.beginObject();
    writer.name("key1");
    writer.value("value1");
    writer.name("key2");
    writer.value("value2");
    writer.endObject();
    writer.endObject();
    JsonObject outerObject = (JsonObject) writer.get();
    JsonPrimitive namePrimitive = outerObject.getAsJsonPrimitive("name");
    assertEquals("name", namePrimitive.getAsString());
    JsonObject innerObject = outerObject.getAsJsonObject().get("name").getAsJsonObject();
    JsonPrimitive key1Primitive = innerObject.getAsJsonPrimitive("key1");
    assertEquals("value1", key1Primitive.getAsString());
    JsonPrimitive key2Primitive = innerObject.getAsJsonPrimitive("key2");
    assertEquals("value2", key2Primitive.getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalStateExceptionBeginObjectBeforeBeginArray() throws IOException {
    writer.beginObject();
    writer.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalStateExceptionBeginArrayBeforeBeginObject() throws IOException {
    writer.beginArray();
    writer.beginObject();
  }

  @Test
  public void testFlush() throws IOException {
    writer.beginObject();
    writer.name("name");
    writer.value("value");
    writer.endObject();
    writer.flush();
    String jsonString = outputStream.toString();
    String expectedJsonString = "{\"name\":\"value\"}";
    assertEquals(expectedJsonString, jsonString);
  }

  @Test
  public void testClose() throws IOException {
    writer.beginObject();
    writer.name("name");
    writer.value("value");
    writer.endObject();
    writer.close();
    JsonElement element = writer.get();
    assertSame(JsonPrimitive.class, element.getClass());
    assertEquals("closed", element.getAsString());
  }
}