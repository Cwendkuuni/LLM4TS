import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.StringWriter;
import java.util.ArrayList;

public class JsonTreeWriterTest {

  private JsonTreeWriter writer;

  @org.junit.Before
  public void setup() {
    writer = new JsonTreeWriter();
  }

  @org.junit.Test
  public void testWriteObject() throws IOException {
    writer.beginObject();
    writer.name("name");
    writer.value("John Doe");
    writer.endObject();

    JsonElement result = writer.get();
    JsonObject expected = new JsonObject();
    expected.addProperty("name", "John Doe");

    assertEquals(expected.toString(), result.toString());
  }

  @org.junit.Test
  public void testWriteArray() throws IOException {
    writer.beginArray();
    writer.value(1);
    writer.value(2);
    writer.value(3);
    writer.endArray();

    JsonElement result = writer.get();
    JsonArray expected = new JsonArray();
    expected.add(new JsonPrimitive(1));
    expected.add(new JsonPrimitive(2));
    expected.add(new JsonPrimitive(3));

    assertEquals(expected.toString(), result.toString());
  }

  @org.junit.Test
  public void testWriteNull() throws IOException {
    writer.value(null);
    JsonElement result = writer.get();
    JsonNull expected = JsonNull.INSTANCE;

    assertEquals(expected, result);
  }

  @org.junit.Test
  public void testWriteEmpty() throws IOException {
    writer.beginObject();
    writer.endObject();

    JsonElement result = writer.get();
    JsonObject expected = new JsonObject();

    assertEquals(expected.toString(), result.toString());
  }

  @org.junit.Test
  public void testWriteNestedObjects() throws IOException {
    writer.beginObject();
    writer.name("name");
    writer.value("John Doe");
    writer.beginObject();
    writer.name("age");
    writer.value(30);
    writer.endObject();
    writer.endObject();

    JsonElement result = writer.get();
    JsonObject expected = new JsonObject();
    JsonObject inner = new JsonObject();
    inner.addProperty("age", 30);
    expected.addProperty("name", "John Doe");
    expected.add("age", inner);

    assertEquals(expected.toString(), result.toString());
  }

  @org.junit.Test
  public void testWriteNestedArrays() throws IOException {
    writer.beginObject();
    writer.name("name");
    writer.value("John Doe");
    writer.beginArray();
    writer.value(1);
    writer.beginArray();
    writer.value(2);
    writer.value(3);
    writer.endArray();
    writer.value(4);
    writer.endArray();
    writer.endObject();

    JsonElement result = writer.get();
    JsonObject expected = new JsonObject();
    JsonArray outerArray = new JsonArray();
    JsonObject innerObject = new JsonObject();
    JsonArray innerArray = new JsonArray();
    innerArray.add(new JsonPrimitive(2));
    innerArray.add(new JsonPrimitive(3));
    innerObject.add("array", innerArray);
    outerArray.add(new JsonObject().addProperty("name", "John Doe").add(innerObject));
    outerArray.add(new JsonPrimitive(4));
    expected.addProperty("name", "John Doe");
    expected.add("array", outerArray);

    assertEquals(expected.toString(), result.toString());
  }

  @org.junit.Test
  public void testWriteEmptyArray() throws IOException {
    writer.beginArray();
    writer.endArray();

    JsonElement result = writer.get();
    JsonArray expected = new JsonArray();

    assertEquals(expected.toString(), result.toString());
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testWriteAfterClose() throws IOException {
    writer.close();
    writer.beginArray();

    fail("Expected IllegalStateException");
  }
}