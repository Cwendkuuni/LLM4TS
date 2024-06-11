import static org.junit.Assert.*;
import static com.google.gson.stream.CoercingJsonWriter.*;

import com.google.gson.*;
import com.google.gson.internal.bind.*;
import com.google.gson.stream.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

public class JsonTreeWriterTest {

  @org.junit.Test
  public void testJsonTreeWriter_basicUsage() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject().name("name").value("John Doe");
    writer.beginArray().value(1).value(2.5).value(true).value(null);
    writer.endArray().name("age").value(30).name("isStudent").value(false).endObject();

    JsonElement jsonElement = writer.get();
    assertEquals(expectedJsonString(), jsonElement.toString());
  }

  private String expectedJsonString() {
    return "{\"name\":\"John Doe\",\"age\":30,\"isStudent\":false,\"properties\":[1.0,2.5,true,null]}";
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testJsonTreeWriter_invalidUsage_missingEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject().name("name").value("John Doe");
    writer.beginArray().value(1).value(2.5).value(true).value(null);

    writer.value(true); // expected error
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testJsonTreeWriter_invalidUsage_missingEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject().name("name").value("John Doe");
    writer.beginArray().value(1).value(2.5).value(true);

    writer.name("invalidKey"); // expected error
  }

  @org.junit.Test
  public void testJsonTreeWriter_nullValue() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject().name("name").value("John Doe");
    writer.beginArray().value(1).value(null).value(3).endArray().endObject();

    JsonElement jsonElement = writer.get();
    assertEquals(expectedJsonStringWithNull(), jsonElement.toString());
  }

  private String expectedJsonStringWithNull() {
    return "{\"name\":\"John Doe\",\"properties\":[1.0,null,3]}";
  }

  @org.junit.Test
  public void testJsonTreeWriter_serializeNulls() {
    JsonTreeWriter writer = new JsonTreeWriter(true); // Set serializeNulls to true

    writer.beginObject().name("name").value("John Doe");
    writer.beginArray().value(1).value(null).value(3).endArray().endObject();

    JsonElement jsonElement = writer.get();
    assertEquals(expectedJsonStringWithNullAndSerializedNull(), jsonElement.toString());
  }

  private String expectedJsonStringWithNullAndSerializedNull() {
    return "{\"name\":\"John Doe\",\"properties\":[1.0,null,3]}";
  }

  @org.junit.Test
  public void testJsonTreeWriter_serializeNulls_false() {
    JsonTreeWriter writer = new JsonTreeWriter(); // Set serializeNulls to false by default

    writer.beginObject().name("name").value("John Doe");
    writer.beginArray().value(1).value(null).value(3).endArray().endObject();

    JsonElement jsonElement = writer.get();
    assertEquals(expectedJsonStringWithNull(), jsonElement.toString());
  }

  @org.junit.Test
  public void testJsonTreeWriter_writeToStream() throws IOException {
    String jsonString = "{\"name\":\"John Doe\",\"age\":30,\"isStudent\":false,\"properties\":[1.0,2.5,true,null]}";

    JsonTreeWriter writer = new JsonTreeWriter();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    Writer writerWrapper = new OutputStreamWriter(outputStream);

    writer.beginObject().name("name").value("John Doe");
    writer.beginArray().value(1).value(2.5).value(true).value(null);
    writer.endArray().name("age").value(30).name("isStudent").value(false).endObject();
    writer.close();

    assertEquals(jsonString, outputStream.toString());
  }
}