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
  public void testJsonTreeWriterBasicUsage() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.name("age").value(30);
    jsonTreeWriter.name("city").value("New York");
    jsonTreeWriter.endObject();

    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    assertEquals("name", jsonObject.keySet().iterator().next());
    assertEquals("John Doe", jsonObject.get("name").getAsString());
    assertEquals(30, jsonObject.get("age").getAsInt());
    assertEquals("New York", jsonObject.get("city").getAsString());
  }

  @Test
  public void testJsonTreeWriterArrayUsage() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("fruits");
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("apple");
    jsonTreeWriter.value("banana");
    jsonTreeWriter.endArray();
    jsonTreeWriter.endObject();

    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    JsonArray fruits = jsonObject.get("fruits").getAsJsonArray();

    assertEquals(2, fruits.size());
    assertEquals("apple", fruits.get(0).getAsString());
    assertEquals("banana", fruits.get(1).getAsString());
  }

  @Test
  public void testJsonTreeWriterNestedObjectsUsage() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("person");
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.name("age").value(30);
    jsonTreeWriter.endObject();
    jsonTreeWriter.endObject();

    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    JsonObject person = jsonObject.get("person").getAsJsonObject();

    assertEquals("name", person.keySet().iterator().next());
    assertEquals("John Doe", person.get("name").getAsString());
    assertEquals(30, person.get("age").getAsInt());
  }

  @Test
  public void testJsonTreeWriterClosingWriter() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.name("age").value(30);

    try {
      jsonTreeWriter.close();
      fail("Should not reach this point");
    } catch (IllegalStateException e) {
      // Expected exception
    }

    jsonTreeWriter.endObject();

    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    assertEquals("name", jsonObject.keySet().iterator().next());
    assertEquals("John Doe", jsonObject.get("name").getAsString());
    assertEquals(30, jsonObject.get("age").getAsInt());
  }

  @Test
  public void testJsonTreeWriterLenientMode() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter(true);

    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.name("age").value(3.14);
    jsonTreeWriter.endObject();

    JsonElement jsonElement = jsonTreeWriter.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    assertEquals("name", jsonObject.keySet().iterator().next());
    assertEquals("John Doe", jsonObject.get("name").getAsString());
    assertEquals(3.14, jsonObject.get("age").getAsDouble(), 0.0);
  }

  @Test
  public void testJsonTreeWriterStringBuilderSerialization() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream);

    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter(outputStreamWriter);

    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.name("age").value(30);
    jsonTreeWriter.endObject();

    jsonTreeWriter.flush();
    jsonTreeWriter.close();

    String jsonString = new String(byteArrayOutputStream.toByteArray());

    JsonParser jsonParser = new JsonParser();
    JsonElement jsonElement = jsonParser.parse(jsonString);

    JsonObject jsonObject = jsonElement.getAsJsonObject();

    assertEquals("name", jsonObject.keySet().iterator().next());
    assertEquals("John Doe", jsonObject.get("name").getAsString());
    assertEquals(30, jsonObject.get("age").getAsInt());
  }
}