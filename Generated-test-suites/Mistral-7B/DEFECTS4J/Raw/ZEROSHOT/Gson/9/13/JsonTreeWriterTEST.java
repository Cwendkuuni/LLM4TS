import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonWriter;
import com.google.gson.stream.JsonWriter.CloseListener;

public class JsonTreeWriterTest {

  private JsonTreeWriter writer;

  @Before
  public void setup() {
    writer = new JsonTreeWriter();
  }

  @Test
  public void testEmptyJsonTree() {
    JsonElement jsonTree = writer.get();
    assertEquals(JsonNull.INSTANCE, jsonTree);
  }

  @Test
  public void testJsonObject() throws IOException {
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(30);
    writer.endObject();

    JsonElement jsonTree = writer.get();
    JsonObject jsonObject = jsonTree.getAsJsonObject();

    assertEquals("John Doe", jsonObject.get("name").getAsString());
    assertEquals(30, jsonObject.get("age").getAsInt());
  }

  @Test
  public void testJsonArray() throws IOException {
    writer.beginArray();
    writer.value(1);
    writer.value(2);
    writer.value(3.5);
    writer.endArray();

    JsonElement jsonTree = writer.get();
    JsonArray jsonArray = jsonTree.getAsJsonArray();

    List<JsonElement> elements = new ArrayList<>();
    elements.add(new JsonPrimitive(1));
    elements.add(new JsonPrimitive(2));
    elements.add(new JsonPrimitive(3.5));

    assertEquals(elements, jsonArray.getAsJsonArray());
  }

  @Test(expected = IllegalStateException.class)
  public void testWrongOrderArrayAndObject() throws IOException {
    writer.beginArray();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.endObject();
    writer.value(1);

    writer.get();
  }

  @Test(expected = IllegalStateException.class)
  public void testWrongOrderObjectAndArray() throws IOException {
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.endObject();
    writer.beginArray();

    writer.get();
  }

  @Test(expected = IllegalStateException.class)
  public void testDoubleClose() throws IOException {
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.endObject();
    writer.close();
    writer.close();

    writer.get();
  }

  @Test(expected = IllegalStateException.class)
  public void testNullName() throws IOException {
    writer.beginObject();
    writer.name(null).value("John Doe");
    writer.endObject();

    writer.get();
  }

  @Test(expected = IllegalStateException.class)
  public void testNullValue() throws IOException {
    writer.beginArray();
    writer.value(null);
    writer.endArray();

    writer.get();
  }

  @Test(expected = IOException.class)
  public void testIOException() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    OutputStreamWriter writer = new OutputStreamWriter(outputStream);
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter(writer);

    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.endObject();

    jsonTreeWriter.get();

    jsonTreeWriter.flush();
    jsonTreeWriter.close();

    writer.flush();
    writer.close();

    String jsonString = outputStream.toString();

    // JSON forbids empty strings
    jsonString = jsonString.replace("\"\"", "\"");

    // Check that the JSON string is valid
    JsonParser parser = new JsonParser();
    JsonElement jsonElement = parser.parse(jsonString);

    // Check that the JSON string is the same as the expected JSON tree
    JsonTreeWriter expectedJsonTreeWriter = new JsonTreeWriter();
    expectedJsonTreeWriter.beginObject();
    expectedJsonTreeWriter.name("name").value("John Doe");
    expectedJsonTreeWriter.endObject();

    JsonElement expectedJsonTree = expectedJsonTreeWriter.get();

    assertEquals(expectedJsonTree, jsonElement);
  }

  @Test
  public void testJsonObjectNestedJsonArray() throws IOException {
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").beginArray();
    writer.value(1);
    writer.value(2);
    writer.endArray();
    writer.endObject();

    JsonElement jsonTree = writer.get();
    JsonObject jsonObject = jsonTree.getAsJsonObject();

    assertEquals("John Doe", jsonObject.get("name").getAsString());

    JsonArray jsonArray = jsonObject.get("age").getAsJsonArray();

    List<JsonElement> elements = new ArrayList<>();
    elements.add(new JsonPrimitive(1));
    elements.add(new JsonPrimitive(2));

    assertEquals(elements, jsonArray.getAsJsonArray());
  }

  @Test
  public void testJsonObjectNestedJsonObject() throws IOException {
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").beginObject();
    writer.name("years").value(30);
    writer.name("months").value(12);
    writer.endObject();
    writer.endObject();

    JsonElement jsonTree = writer.get();
    JsonObject jsonObject = jsonTree.getAsJsonObject();

    assertEquals("John Doe", jsonObject.get("name").getAsString());

    JsonObject ageObject = jsonObject.get("age").getAsJsonObject();

    assertEquals(30, ageObject.get("years").getAsInt());
    assertEquals(12, ageObject.get("months").getAsInt());
  }

  @Test
  public void testJsonObjectNestedJsonArrayAndJsonObject() throws IOException {
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").beginArray();
    writer.value(1);
    writer.value(2);
    writer.name("name2").beginObject();
    writer.name("name2_1").value("John Doe 2");
    writer.name("name2_2").value(30);
    writer.endObject();
    writer.endArray();
    writer.endObject();

    JsonElement jsonTree = writer.get();
    JsonObject jsonObject = jsonTree.getAsJsonObject();

    assertEquals("John Doe", jsonObject.get("name").getAsString());

    JsonArray ageArray = jsonObject.get("age").getAsJsonArray();

    List<JsonElement> ageElements = new ArrayList<>();
    ageElements.add(new JsonPrimitive(1));
    ageElements.add(new JsonPrimitive(2));

    assertEquals(ageElements, ageArray.getAsJsonArray());

    JsonObject ageObject = ageArray.get(0).getAsJsonObject();

    assertEquals("John Doe 2", ageObject.get("name2_1").getAsString());
    assertEquals(30, ageObject.get("name2_2").getAsInt());
  }

  @Test
  public void testJsonObjectNestedJsonObjectWithNullName() throws IOException {
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").beginObject();
    writer.name("nullName").value(null);
    writer.name("years").value(30);
    writer.name("months").value(12);
    writer.endObject();
    writer.endObject();

    JsonElement jsonTree = writer.get();
    JsonObject jsonObject = jsonTree.getAsJsonObject();

    assertEquals("John Doe", jsonObject.get("name").getAsString());

    JsonObject ageObject = jsonObject.get("age").getAsJsonObject();

    assertNull(ageObject.get("nullName"));
    assertEquals(30, ageObject.get("years").getAsInt());
    assertEquals(12, ageObject.get("months").getAsInt());
  }

  @Test
  public void testJsonObjectNestedJsonObjectWithNullValue() throws IOException {
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").beginObject();
    writer.name("years").value(null);
    writer.name("months").value(12);
    writer.endObject();
    writer.endObject();

    JsonElement jsonTree = writer.get();
    JsonObject jsonObject = jsonTree.getAsJsonObject();

    assertEquals("John Doe", jsonObject.get("name").getAsString());

    JsonObject ageObject = jsonObject.get("age").getAsJsonObject();

    assertNull(ageObject.get("years"));
    assertEquals(12, ageObject.get("months").getAsInt());
  }

  @Test
  public void testJsonObjectNestedJsonObjectWithNullBoth() throws IOException {
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").beginObject();
    writer.name("years").value(null);
    writer.name("months").value(null);
    writer.endObject();
    writer.endObject();

    JsonElement jsonTree = writer.get();
    JsonObject jsonObject = jsonTree.getAsJsonObject();

    assertEquals("John Doe", jsonObject.get("name").getAsString());

    JsonObject ageObject = jsonObject.get("age").getAsJsonObject();

    assertNull(ageObject);
  }

  @Test
  public void testJsonObjectNestedJsonObjectWithEmptyArray() throws IOException {
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").beginArray();
    writer.endArray();
    writer.endObject();
    writer.endObject();

    JsonElement jsonTree = writer.get();
    JsonObject jsonObject = jsonTree.getAsJsonObject();

    assertEquals("John Doe", jsonObject.get("name").getAsString());

    JsonArray ageArray = jsonObject.get("age").getAsJsonArray();

    assertTrue(ageArray.isJsonNull());
  }

  @Test
  public void testJsonObjectNestedJsonObjectWithEmptyObjects() throws IOException {
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").beginObject();
    writer.endObject();
    writer.name("address").beginObject();
    writer.endObject();
    writer.endObject();

    JsonElement jsonTree = writer.get();
    JsonObject jsonObject = jsonTree.getAsJsonObject();

    assertEquals("John Doe", jsonObject.get("name").getAsString());

    JsonObject ageObject = jsonObject.get("age").getAsJsonObject();

    assertTrue(ageObject.isJsonNull());

    JsonObject addressObject = jsonObject.get("address").getAsJsonObject();

    assertTrue(addressObject.isJsonNull());
  }

  @Test
  public void testJsonObjectNestedJsonArrayWithEmptyJsonObject() throws IOException {
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").beginArray();
    writer.beginObject();
    writer.endObject();
    writer.endArray();
    writer.endObject();

    JsonElement jsonTree = writer.get();
    JsonObject jsonObject = jsonTree.getAsJsonObject();

    assertEquals("John Doe", jsonObject.get("name").getAsString());

    JsonArray ageArray = jsonObject.get("age").getAsJsonArray();

    assertEquals(1, ageArray.size());

    JsonElement ageElement = ageArray.get(0);

    assertTrue(ageElement.isJsonObject());

    JsonObject ageObject = ageElement.getAsJsonObject();

    assertTrue(ageObject.isJsonNull());
  }

  @Test
  public void testJsonObjectNestedJsonArrayWithEmptyJsonObjectAndValue() throws IOException {
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").beginArray();
    writer.beginObject();
    writer.name("nullName").value(null);
    writer.name("years").value(30);
    writer.name("months").value(12);
    writer.endObject();
    writer.endArray();
    writer.endObject();

    JsonElement jsonTree = writer.get();
    JsonObject jsonObject = jsonTree.getAsJsonObject();

    assertEquals("John Doe", jsonObject.get("name").getAsString());

    JsonArray ageArray = jsonObject.get("age").getAsJsonArray();

    assertEquals(1, ageArray.size());

    JsonElement ageElement = ageArray.get(0);

    assertTrue(ageElement.isJsonObject());

    JsonObject ageObject = ageElement.getAsJsonObject();

    assertNull(ageObject.get("nullName"));
    assertEquals(30, ageObject.get("years").getAsInt());
    assertEquals(12, ageObject.get("months").getAsInt());
  }

  @Test
  public void testJsonObjectNestedJsonObjectWithEmptyJsonObjects() throws IOException {
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").beginObject();
    writer.endObject();
    writer.name("address").beginObject();
    writer.endObject();
    writer.name("city").beginObject();
    writer.endObject();
    writer.endObject();

    JsonElement jsonTree = writer.get();
    JsonObject jsonObject = jsonTree.getAsJsonObject();

    assertEquals("John Doe", jsonObject.get("name").getAsString());

    JsonObject ageObject = jsonObject.get("age").getAsJsonObject();

    assertTrue(ageObject.isJsonNull());

    JsonObject addressObject = jsonObject.get("address").getAsJsonObject();

    assertTrue(addressObject.isJsonNull());

    JsonObject cityObject = jsonObject.get("city").getAsJsonObject();

    assertTrue(cityObject.isJsonNull());
  }

  @Test
  public void testJsonTreeWriterClosedState() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.close();

    try {
      jsonTreeWriter.get();
    } catch (Exception e) {
      // Expected
      assertTrue(e instanceof IllegalStateException);
    }
  }

  @Test
  public void testJsonTreeWriterCloseListener() throws IOException {
    CloseListener closeListener = new CloseListener() {
      @Override public void onClose() {
        // Do something
      }
    };

    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter(new OutputStreamWriter(new ByteArrayOutputStream()), closeListener);

    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name").value("John Doe");
    jsonTreeWriter.endObject();

    jsonTreeWriter.get();

    jsonTreeWriter.close();

    jsonTreeWriter.onClose();
  }
}