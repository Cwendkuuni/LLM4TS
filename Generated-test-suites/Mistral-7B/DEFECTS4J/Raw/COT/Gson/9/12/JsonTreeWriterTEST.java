import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testDefaultConstructor() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertNull(jsonTreeWriter.get());
  }

  @Test
  public void testGetMethod() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("test");
    jsonTreeWriter.endArray();
    JsonElement result = jsonTreeWriter.get();

    JsonArray jsonArray = (JsonArray) result;
    assertEquals(1, jsonArray.size());
    JsonPrimitive jsonPrimitive = jsonArray.get(0).getAsJsonPrimitive();
    assertEquals("test", jsonPrimitive.getAsString());
  }

  @Test
  public void testBeginArrayMethod() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("test1");
    jsonTreeWriter.value("test2");
    jsonTreeWriter.endArray();
    JsonElement result = jsonTreeWriter.get();

    JsonArray jsonArray = (JsonArray) result;
    assertEquals(2, jsonArray.size());
    List<JsonPrimitive> jsonPrimitives = new ArrayList<>();
    jsonPrimitives.add((JsonPrimitive) jsonArray.get(0));
    jsonPrimitives.add((JsonPrimitive) jsonArray.get(1));
    for (JsonPrimitive jsonPrimitive : jsonPrimitives) {
      assertEquals("test" + (jsonPrimitive.isBoolean() ? "" : (jsonPrimitive.isNumber() ? Long.toString(jsonPrimitive.getAsLong()) : jsonPrimitive.getAsString())), jsonPrimitive.getAsString());
    }
  }

  @Test
  public void testBeginObjectMethod() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.name("age").value(25);
    jsonTreeWriter.endObject();
    JsonElement result = jsonTreeWriter.get();

    JsonObject jsonObject = (JsonObject) result;
    assertEquals("John Doe", jsonObject.get("name").getAsString());
    assertEquals(25, jsonObject.get("age").getAsInt());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetMethodWhenStackIsEmpty() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.get();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayWhenStackIsNotEmptyOrPendingNameIsSet() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWhenStackIsNotArray() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWhenStackIsNotObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testNameMethodWhenStackIsNotObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.name("name");
  }

  @Test(expected = IllegalStateException.class)
  public void testValueMethodWhenStackIsEmpty() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value("test");
  }

  @Test(expected = IllegalStateException.class)
  public void testValueMethodWhenStackIsNotArrayAndNameIsSet() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.value("test");
  }

  @Test(expected = IllegalStateException.class)
  public void testValueMethodWhenStackIsArrayAndPendingNameIsSet() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.value("test");
  }

  @Test
  public void testToStringMethod() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    Writer writer = new OutputStreamWriter(outputStream);
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter(writer);
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("test1");
    jsonTreeWriter.value("test2");
    jsonTreeWriter.endArray();
    jsonTreeWriter.close();
    String expected = "[\n  \"test1\",\n  \"test2\"\n]";
    assertEquals(expected, outputStream.toString());
  }

  @Test
  public void testCloseMethod() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("test1");
    jsonTreeWriter.value("test2");
    jsonTreeWriter.endArray();
    jsonTreeWriter.close();
    JsonElement result = jsonTreeWriter.get();
    JsonArray jsonArray = (JsonArray) result;
    assertEquals(2, jsonArray.size());
  }

  @Test(expected = IOException.class)
  public void testFlushMethodWhenStreamIsNotOpen() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.flush();
  }

  @Test(expected = IOException.class)
  public void testCloseMethodWhenStreamIsNotOpen() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.close();
  }

  @Test(expected = AssertionError.class)
  public void testUnwritableWriter() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    Writer writer = jsonTreeWriter.getWriter();
    writer.write(new char[1]);
  }
}