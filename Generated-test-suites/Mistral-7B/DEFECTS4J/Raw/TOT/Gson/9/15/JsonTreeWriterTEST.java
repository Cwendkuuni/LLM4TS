import static org.junit.Assert.*;
import static com.google.gson.stream.CoercingJsonWriter.*;

import com.google.gson.*;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class JsonTreeWriterTest {

  @org.junit.Test
  public void testJsonTreeWriter_basicUsage() throws IOException {
    // Given
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new GsonBuilder().setPrettyPrinting().createWriter(stringWriter);

    // When
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.name("age").value(30);
    jsonTreeWriter.name("address").beginObject();
    jsonTreeWriter.name("street").value("Main St");
    jsonTreeWriter.name("city").value("Anytown");
    jsonTreeWriter.name("state").value("CA");
    jsonTreeWriter.name("zip").value(12345);
    jsonTreeWriter.endObject(); // close address
    jsonTreeWriter.endObject(); // close root object
    jsonWriter.flush();

    // Then
    String jsonString = stringWriter.getBuffer().toString();
    JsonElement jsonElement = JsonParser.parseString(jsonString).getAsJsonTree();
    assertEquals("{\"name\":\"John Doe\",\"age\":30,\"address\":{\"street\":\"Main St\",\"city\":\"Anytown\",\"state\":\"CA\",\"zip\":12345}}", jsonElement.toString());

    // Cleanup
    jsonTreeWriter.close();
    jsonWriter.close();
    stringWriter.close();
  }

  @org.junit.Test
  public void testJsonTreeWriter_nestedArrays() throws IOException {
    // Given
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new GsonBuilder().setPrettyPrinting().createWriter(stringWriter);

    // When
    jsonTreeWriter.beginArray();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value(1);
    jsonTreeWriter.value(2);
    jsonTreeWriter.endArray();
    jsonTreeWriter.value(3);
    jsonTreeWriter.endArray(); // close first array
    jsonTreeWriter.value(4);
    jsonTreeWriter.endObject(); // close root object
    jsonWriter.flush();

    // Then
    String jsonString = stringWriter.getBuffer().toString();
    JsonElement jsonElement = JsonParser.parseString(jsonString).getAsJsonTree();
    assertEquals("[[\"1\",\"2\"],3]", jsonElement.toString());

    // Cleanup
    jsonTreeWriter.close();
    jsonWriter.close();
    stringWriter.close();
  }

  @org.junit.Test
  public void testJsonTreeWriter_serializeNulls() throws IOException {
    // Given
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new GsonBuilder().setLenient().createWriter(stringWriter);

    // When
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.name("age").value(null);
    jsonTreeWriter.name("address").beginObject();
    jsonTreeWriter.name("city").value("Anytown");
    jsonTreeWriter.name("state").value("CA");
    jsonTreeWriter.name("zip").value(12345);
    jsonTreeWriter.endObject(); // close address
    jsonTreeWriter.endObject(); // close root object
    jsonWriter.flush();

    // Then
    String jsonString = stringWriter.getBuffer().toString();
    JsonElement jsonElement = JsonParser.parseString(jsonString).getAsJsonTree();
    assertEquals("{\"name\":\"John Doe\",\"age\":null,\"address\":{\"city\":\"Anytown\",\"state\":\"CA\",\"zip\":12345}}", jsonElement.toString());

    // Cleanup
    jsonTreeWriter.close();
    jsonWriter.close();
    stringWriter.close();
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testJsonTreeWriter_addValueToEmptyStack_invalidCallToEndObject() throws IOException {
    // Given
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    // When
    jsonTreeWriter.endObject();

    // Then
    // expected IllegalStateException
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testJsonTreeWriter_addValueToEmptyStack_invalidCallToEndArray() throws IOException {
    // Given
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    // When
    jsonTreeWriter.endArray();

    // Then
    // expected IllegalStateException
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testJsonTreeWriter_closeWriterTwice() throws IOException {
    // Given
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new GsonBuilder().setPrettyPrinting().createWriter(stringWriter);

    // When
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.name("age").value(30);
    jsonTreeWriter.endObject(); // close first object
    jsonTreeWriter.close(); // close writer
    jsonTreeWriter.close(); // try to close writer again

    // Then
    // expected IllegalStateException

    // Cleanup
    jsonWriter.close();
    stringWriter.close();
  }
}