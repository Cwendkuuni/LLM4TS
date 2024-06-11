import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonTreeWriterTest {

  @Test
  public void testJsonTreeWriter() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();

    // Test empty writer
    assertEquals(JsonNull.INSTANCE, jsonTreeWriter.get());

    // Test writing JSON elements
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("element1");
    jsonTreeWriter.value(1);
    jsonTreeWriter.value(true);
    jsonTreeWriter.endArray();
    JsonArray jsonArray = jsonTreeWriter.get().getAsJsonArray();
    assertEquals(3, jsonArray.size());
    assertEquals("element1", jsonArray.get(0).getAsString());
    assertEquals(1, jsonArray.get(1).getAsInt());
    assertEquals(true, jsonArray.get(2).getAsBoolean());

    // Test writing JSON objects
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name1").value("value1");
    jsonTreeWriter.name("name2").value(1);
    jsonTreeWriter.name("name3").value(true);
    jsonTreeWriter.endObject();
    JsonObject jsonObject = jsonTreeWriter.get().getAsJsonObject();
    assertEquals(3, jsonObject.size());
    assertEquals("value1", jsonObject.get("name1").getAsString());
    assertEquals(1, jsonObject.get("name2").getAsInt());
    assertEquals(true, jsonObject.get("name3").getAsBoolean());

    // Test writing nested JSON objects and arrays
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("nameObject").beginObject();
    jsonTreeWriter.name("nameObjectName1").value("valueObjectName1");
    jsonTreeWriter.name("nameObjectName2").beginArray();
    jsonTreeWriter.value("valueArray1");
    jsonTreeWriter.value("valueArray2");
    jsonTreeWriter.endArray();
    jsonTreeWriter.endObject();
    jsonTreeWriter.value("value1");
    jsonTreeWriter.endObject();

    JsonObject jsonObjectRoot = jsonTreeWriter.get().getAsJsonObject();
    JsonObject jsonObjectNested = jsonObjectRoot.getAsJsonObject("nameObject");
    JsonObject jsonObjectNestedObject = jsonObjectNested.getAsJsonObject("nameObjectName1");
    JsonArray jsonArrayNested = jsonObjectNested.getAsJsonObject("nameObjectName2").getAsJsonArray();

    assertEquals("valueObjectName1", jsonObjectNestedObject.get("name").getAsString());
    assertEquals(2, jsonArrayNested.size());
    assertEquals("valueArray1", jsonArrayNested.get(0).getAsString());
    assertEquals("valueArray2", jsonArrayNested.get(1).getAsString());
    assertEquals("value1", jsonObjectRoot.get("nameObject").getAsJsonObject().get("value1").getAsString());

    // Test writing empty JSON objects and arrays
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    JsonObject jsonObjectEmpty = jsonTreeWriter.get().getAsJsonObject();
    assertTrue(jsonObjectEmpty.entrySet().isEmpty());

    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
    JsonArray jsonArrayEmpty = jsonTreeWriter.get().getAsJsonArray();
    assertTrue(jsonArrayEmpty.isEmpty());

    // Test writing JSON elements with the same name
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name1").value("value1");
    jsonTreeWriter.name("name1").value("value2");
    jsonTreeWriter.endObject();
    JsonObject jsonObjectSameName = jsonTreeWriter.get().getAsJsonObject();
    assertEquals(2, jsonObjectSameName.size());
    List<String> names = new ArrayList<String>(jsonObjectSameName.keySet());
    assertTrue(names.containsAll(Arrays.asList("name1", "name2")));
    assertFalse(names.contains("name3"));
  }

  @Test
  public void testJsonTreeWriterWithWriter() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream);
    jsonTreeWriter.setWriter(outputStreamWriter);

    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("element1");
    jsonTreeWriter.value(1);
    jsonTreeWriter.value(true);
    jsonTreeWriter.endArray();
    jsonTreeWriter.close();

    String jsonString = byteArrayOutputStream.toString();
    assertEquals("[\"element1\",1,true]", jsonString);
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterWrongOrder() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("element1");
    jsonTreeWriter.value(1);
    jsonTreeWriter.value(true);
    jsonTreeWriter.endArray();
    jsonTreeWriter.get();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterNullName() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name(null);
    jsonTreeWriter.value("value1");
    jsonTreeWriter.endObject();
    jsonTreeWriter.get();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterNullValue() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name1");
    jsonTreeWriter.value(null);
    jsonTreeWriter.endObject();
    jsonTreeWriter.get();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterNameWithoutValue() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name1");
    jsonTreeWriter.endObject();
    jsonTreeWriter.get();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterValueWithoutName() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.value("value1");
    jsonTreeWriter.endObject();
    jsonTreeWriter.get();
  }
}