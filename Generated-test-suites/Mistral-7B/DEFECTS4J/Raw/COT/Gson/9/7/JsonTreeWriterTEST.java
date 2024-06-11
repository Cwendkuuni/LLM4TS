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
  public void testJsonTreeWriterConstructor() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertNull(jsonTreeWriter.get());
  }

  @Test
  public void testGetMethod() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.endObject();
    JsonElement result = jsonTreeWriter.get();
    JsonObject expected = new JsonObject();
    expected.addProperty("name", "John Doe");
    assertEquals(expected, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetMethodWithEmptyStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.get();
  }

  @Test
  public void testBeginArrayMethod() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("element1");
    jsonTreeWriter.value("element2");
    jsonTreeWriter.endArray();
    JsonElement result = jsonTreeWriter.get();
    JsonArray expected = new JsonArray();
    expected.add(new JsonPrimitive("element1"));
    expected.add(new JsonPrimitive("element2"));
    assertEquals(expected, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayMethodWithNonEmptyStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayMethodWithEmptyStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.endArray();
  }

  @Test
  public void testEndArrayMethod() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("element1");
    jsonTreeWriter.value("element2");
    jsonTreeWriter.endArray();
    JsonElement result = jsonTreeWriter.get();
    JsonArray expected = new JsonArray();
    expected.add(new JsonPrimitive("element1"));
    expected.add(new JsonPrimitive("element2"));
    assertEquals(expected, result);
  }

  @Test
  public void testBeginObjectMethod() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.name("age").value(30);
    jsonTreeWriter.endObject();
    JsonElement result = jsonTreeWriter.get();
    JsonObject expected = new JsonObject();
    expected.addProperty("name", "John Doe");
    expected.addProperty("age", 30);
    assertEquals(expected, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectMethodWithNonEmptyStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("element1");
    jsonTreeWriter.beginObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectMethodWithEmptyStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.endObject();
  }

  @Test
  public void testEndObjectMethod() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.name("age").value(30);
    jsonTreeWriter.endObject();
    JsonElement result = jsonTreeWriter.get();
    JsonObject expected = new JsonObject();
    expected.addProperty("name", "John Doe");
    expected.addProperty("age", 30);
    assertEquals(expected, result);
  }

  @Test
  public void testNameMethod() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.endObject();
    JsonElement result = jsonTreeWriter.get();
    JsonObject expected = new JsonObject();
    expected.addProperty("name", "John Doe");
    assertEquals(expected, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testNameMethodWithNonJsonObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.name("name");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueMethodWithNullString() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.name("name").value(null);
  }

  @Test
  public void testValueMethodWithString() throws IOException {
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
  public void testNullValueMethod() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(null);
    JsonElement result = jsonTreeWriter.get();
    JsonNull expected = JsonNull.INSTANCE;
    assertEquals(expected, result);
  }

  @Test
  public void testValueMethodWithBoolean() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(true);
    JsonElement result = jsonTreeWriter.get();
    JsonPrimitive expected = new JsonPrimitive(true);
    assertEquals(expected, result);
  }

  @Test
  public void testValueMethodWithNumber() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(123);
    JsonElement result = jsonTreeWriter.get();
    JsonPrimitive expected = new JsonPrimitive(123);
    assertEquals(expected, result);
  }

  @Test
  public void testValueMethodWithDouble() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(123.45);
    JsonElement result = jsonTreeWriter.get();
    JsonPrimitive expected = new JsonPrimitive(123.45);
    assertEquals(expected, result);
  }

  @Test
  public void testValueMethodWithJsonPrimitive() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(new JsonPrimitive("hello"));
    JsonElement result = jsonTreeWriter.get();
    JsonPrimitive expected = new JsonPrimitive("hello");
    assertEquals(expected, result);
  }

  @Test
  public void testFlushMethod() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.endObject();
    jsonTreeWriter.flush();
  }

  @Test
  public void testCloseMethod() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream);
    Writer writer = new JsonWriter(outputWriter);
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter(writer);
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.endObject();
    jsonTreeWriter.close();
    String jsonString = outputStream.toString();
    JsonElement expected = new JsonObject().get("name").getAsJsonPrimitive().getAsString();
    assertEquals(expected.getAsString(), jsonString);
  }

  @Test(expected = IOException.class)
  public void testCloseMethodWithNonEmptyStack() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream);
    Writer writer = new JsonWriter(outputWriter);
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter(writer);
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.close();
  }
}