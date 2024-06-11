import static org.junit.Assert.*;
import static com.google.gson.JsonFactory.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonTreeWriterTest {

  @Test
  public void testJsonTreeWriter_basicUsage() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.beginArray();
    writer.value(1);
    writer.value(2);
    writer.value(3.5);
    writer.endArray();
    writer.name("age").value(30);
    writer.endObject();

    JsonElement jsonTree = writer.get();
    JsonElement expectedJson = parse("{\"name\":\"John Doe\",\"age\":30,\"values\":[1,2,3.5]}");

    assertEquals(expectedJson, jsonTree);
  }

  @Test
  public void testJsonTreeWriter_nestedObjects() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.beginObject();
      writer.name("city").value("New York");
      writer.name("state").value("NY");
    writer.endObject();
    writer.beginArray();
      writer.value(1);
      writer.value(2);
      writer.value(3.5);
    writer.endArray();
    writer.name("age").value(30);
    writer.endObject();

    JsonElement jsonTree = writer.get();
    JsonElement expectedJson = parse("{"
        + "\"name\":{"
        + "\"city\":\"New York\","
        + "\"state\":\"NY\""
        + "},"
        + "\"age\":30,"
        + "\"values\":[1,2,3.5]"
        + "}");

    assertEquals(expectedJson, jsonTree);
  }

  @Test
  public void testJsonTreeWriter_errors() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();

    try {
      writer.beginArray();
      writer.name("name").value("John Doe");
      writer.endObject();
    } catch (IllegalStateException e) {
      // expected
    }

    try {
      writer.value(new Object());
    } catch (IllegalArgumentException e) {
      // expected
    }

    try {
      writer.close();
      writer.value(new JsonPrimitive("42"));
    } catch (IllegalStateException e) {
      // expected
    }
  }

  @Test
  public void testJsonTreeWriter_serializeNulls() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter(true); // set serializeNulls to true

    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(null);
    writer.endObject();

    JsonElement jsonTree = writer.get();
    JsonElement expectedJson = parse("{" + "\"name\":\"John Doe\"," + "\"age\":null" + "}");

    assertEquals(expectedJson, jsonTree);
  }

  @Test
  public void testJsonTreeWriter_writeTo() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();

    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    OutputStreamWriter output = new OutputStreamWriter(stream);

    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.beginArray();
      writer.value(1);
      writer.value(2);
      writer.value(3.5);
    writer.endArray();
    writer.name("age").value(30);
    writer.endObject();

    writer.writeTo(output);
    output.flush();

    String jsonString = stream.toString();
    JsonElement jsonTree = parse(jsonString);

    JsonElement expectedJson = parse("{"
        + "\"name\":\"John Doe\","
        + "\"age\":30,"
        + "\"values\":[1,2,3.5]"
        + "}");

    assertEquals(expectedJson, jsonTree);
  }

  private JsonElement parse(String jsonString) {
    return JsonParser.parseString(jsonString).getAsJson();
  }

  private JsonElement parse(String jsonString) {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    OutputStreamWriter output = new OutputStreamWriter(stream);
    JsonWriter writer = new JsonWriter(output);
    writer.write(jsonString);
    writer.close();
    return parse(stream.toString()).getAsJson();
  }

  @Test
  public void testJsonTreeWriter_lenientMode() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter(true); // set lenient to true

    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.beginArray();
      writer.value(1);
      writer.value(2);
      writer.value(3.5);
      writer.value("42"); // NaN or Infinite numbers are not allowed in lenient mode, but strings are
    writer.endArray();
    writer.name("age").value(30);
    writer.endObject();

    JsonElement jsonTree = writer.get();

    JsonElement expectedJson = parse("{"
        + "\"name\":\"John Doe\","
        + "\"age\":30,"
        + "\"values\":[1,2,3.5,\"42\"]"
        + "}");

    assertEquals(expectedJson, jsonTree);
  }
}