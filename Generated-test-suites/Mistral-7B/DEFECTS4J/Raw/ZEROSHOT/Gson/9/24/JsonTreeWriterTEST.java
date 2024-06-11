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
  public void testJsonTreeWriter() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();

    // Test empty writer
    JsonElement result = writer.get();
    assertEquals(JsonNull.INSTANCE, result);

    // Test writing JSON object
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.endObject();
    result = writer.get();
    JsonObject expected = new JsonObject();
    expected.addProperty("name", "John Doe");
    assertEquals(expected, result);

    // Test writing JSON array
    writer.beginArray();
    writer.value(1);
    writer.value(2.5);
    writer.value("three");
    writer.endArray();
    result = writer.get();
    JsonArray expectedArray = new JsonArray();
    expectedArray.add(1);
    expectedArray.add(2.5);
    expectedArray.add("three");
    assertEquals(expectedArray, result);

    // Test nesting JSON objects and arrays
    writer.beginObject();
    writer.name("parent").beginObject();
    writer.name("child1").beginArray();
    writer.value(1);
    writer.value(2);
    writer.endArray();
    writer.name("child2").value("two");
    writer.endObject();
    writer.endObject();
    result = writer.get();
    JsonObject parent = new JsonObject();
    JsonObject childObject = new JsonObject();
    childObject.add("child1", new JsonArray().add(1).add(2));
    childObject.addProperty("child2", "two");
    parent.add("child", childObject);
    assertEquals(parent, result);

    // Test writing JSON object with null value
    writer.beginObject();
    writer.name("name").value(null);
    writer.endObject();
    result = writer.get();
    JsonObject expectedNull = new JsonObject();
    expectedNull.addProperty("name", null);
    assertEquals(expectedNull, result);

    // Test writing JSON object with empty value
    writer.beginObject();
    writer.name("name").value("");
    writer.endObject();
    result = writer.get();
    JsonObject expectedEmpty = new JsonObject();
    expectedEmpty.addProperty("name", "");
    assertEquals(expectedEmpty, result);

    // Test closing writer before all elements are written
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.endObject();
    writer.close();
    result = writer.get();
    JsonPrimitive expectedClosed = new JsonPrimitive("closed");
    assertEquals(expectedClosed, result);
  }
}