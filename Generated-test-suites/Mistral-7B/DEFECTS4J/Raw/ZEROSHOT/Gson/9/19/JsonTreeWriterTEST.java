import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonTreeWriterTest {

  @Test
  public void testJsonTreeWriterBasics() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();

    JsonElement jsonTree = writer.beginObject().name("key1").value("value1")
        .name("key2").beginArray().value("element1").value("element2")
        .endArray().endObject().get();

    assertEquals("{\"key1\":\"value1\",\"key2\":[\"element1\",\"element2\"]}", jsonTree.toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterIllegalStateException1() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("value");
    writer.beginObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterIllegalStateException2() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    writer.value("value");
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterIllegalStateException3() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("value1");
    writer.value("value2");
    writer.endArray();
    writer.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterIllegalStateException4() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("key");
    writer.endObject();
    writer.value("value");
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterIllegalStateException5() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("key1").name("key2");
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterIllegalStateException6() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray().value("value1");
    writer.name("key");
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterIllegalStateException7() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("key").value("value1");
    writer.name("key").value("value2");
  }

  @Test
  public void testJsonTreeWriterGet() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("key1").value("value1").endObject();
    JsonElement jsonTree = writer.get();
    assertEquals("{\"key1\":\"value1\"}", jsonTree.toString());
  }

  @Test
  public void testJsonTreeWriterPeek() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement jsonTree = writer.beginObject().name("key1").value("value1")
        .name("key2").beginArray().value("element1").value("element2")
        .endArray().endObject().get();
    assertSame(jsonTree, writer.peek());
  }

  @Test
  public void testJsonTreeWriterPut() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.put(new JsonPrimitive("value1"));
    JsonElement jsonTree = writer.get();
    assertEquals("{\"value1\":null}", jsonTree.toString());
  }

  @Test
  public void testJsonTreeWriterBeginArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray().value("value1").value("value2").endArray().get();
    JsonElement jsonTree = writer.get();
    assertEquals("[\"value1\",\"value2\"]", jsonTree.toString());
  }

  @Test
  public void testJsonTreeWriterBeginObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("key1").value("value1").name("key2").value("value2")
        .endObject().get();
    JsonElement jsonTree = writer.get();
    assertEquals("{\"key1\":\"value1\",\"key2\":\"value2\"}", jsonTree.toString());
  }

  @Test
  public void testJsonTreeWriterName() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("key1").value("value1").endObject().get();
    JsonElement jsonTree = writer.get();
    assertEquals("{\"key1\":\"value1\"}", jsonTree.toString());
  }

  @Test
  public void testJsonTreeWriterValue() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("key").value("value1").endObject().get();
    JsonElement jsonTree = writer.get();
    assertEquals("{\"key\":\"value1\"}", jsonTree.toString());
  }

  @Test
  public void testJsonTreeWriterNullValue() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("key").nullValue().endObject().get();
    JsonElement jsonTree = writer.get();
    assertEquals("{\"key\":null}", jsonTree.toString());
  }

  @Test
  public void testJsonTreeWriterBooleanValue() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("key").value(true).endObject().get();
    JsonElement jsonTree = writer.get();
    assertEquals("{\"key\":true}", jsonTree.toString());
  }

  @Test
  public void testJsonTreeWriterDoubleValue() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("key").value(3.1415926535897932384626433832795).endObject()
        .get();
    JsonElement jsonTree = writer.get();
    assertEquals("{\"key\":3.141592653589793}", jsonTree.toString());
  }

  @Test
  public void testJsonTreeWriterLongValue() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("key").value(1234567890).endObject().get();
    JsonElement jsonTree = writer.get();
    assertEquals("{\"key\":1234567890}", jsonTree.toString());
  }

  @Test
  public void testJsonTreeWriterNumberValue() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement jsonNumber = new JsonPrimitive(1234567890.7890123456789L);
    writer.beginObject().name("key").value(jsonNumber).endObject().get();
    JsonElement jsonTree = writer.get();
    assertEquals("{\"key\":1234567890.78901235}", jsonTree.toString());
  }

  @Test
  public void testJsonTreeWriterEndArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray().value("value1").value("value2").endArray().get();
    JsonElement jsonTree = writer.get();
    assertEquals("[\"value1\",\"value2\"]", jsonTree.toString());
  }

  @Test
  public void testJsonTreeWriterEndObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("key1").value("value1").name("key2").value("value2")
        .endObject().get();
    JsonElement jsonTree = writer.get();
    assertEquals("{\"key1\":\"value1\",\"key2\":\"value2\"}", jsonTree.toString());
  }

  @Test
  public void testJsonTreeWriterGetAfterClose() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    JsonElement jsonTree = writer.get();
    assertNull(jsonTree);
  }

  @Test
  public void testJsonTreeWriterFlush() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    OutputStreamWriter writerStream = new OutputStreamWriter(outputStream);
    writer.setWriter(writerStream);
    writer.beginObject().name("key1").value("value1").endObject();
    writer.flush();
    writer.close();
    String jsonString = outputStream.toString();
    assertEquals("{\"key1\":\"value1\"}", jsonString);
  }

  @Test
  public void testJsonTreeWriterToString() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement jsonTree = writer.beginObject().name("key1").value("value1")
        .name("key2").beginArray().value("element1").value("element2")
        .endArray().endObject().get();
    assertEquals(jsonTree.toString(), "[{\"key1\":\"value1\",\"key2\":[\"element1\",\"element2\"]}]");
  }

  @Test
  public void testJsonTreeWriterNameWithEmptyString() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("").value("value").endObject().get();
    JsonElement jsonTree = writer.get();
    assertEquals("{\"\":\"value\"}", jsonTree.toString());
  }

  @Test
  public void testJsonTreeWriterNameWithNull() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name(null).value("value").endObject().get();
    JsonElement jsonTree = writer.get();
    assertEquals("{\"null\":\"value\"}", jsonTree.toString());
  }

  @Test
  public void testJsonTreeWriterNestedObjects() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement jsonTree1 = writer.beginObject().name("key1").beginObject().name("key2")
        .value("value2").endObject().endObject().get();
    assertEquals("{\"key1\":{\"key2\":\"value2\"}}", jsonTree1.toString());
  }

  @Test
  public void testJsonTreeWriterNestedArrays() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement jsonTree1 = writer.beginObject().name("key1").beginArray()
        .beginArray().value("value2").value("value3").endArray().endArray().endObject()
        .get();
    assertEquals("{\"key1\":[[\"value2\",\"value3\"]]}", jsonTree1.toString());
  }

  @Test
  public void testJsonTreeWriterNestedObjectsAndArrays() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement jsonTree1 = writer.beginObject().name("key1").beginObject().name("key2")
        .beginArray().value("value2").value("value3").endArray().endObject().endObject()
        .get();
    assertEquals("{\"key1\":{\"key2\":[\"value2\",\"value3\"]}}", jsonTree1.toString());
  }

  @Test
  public void testJsonTreeWriterEmptyJsonTree() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement jsonTree = writer.get();
    assertEquals("null", jsonTree.toString());
  }

  @Test
  public void testJsonTreeWriterClosedState() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    try {
      writer.get();
    } catch (IllegalStateException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testJsonTreeWriterMultipleCloses() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    try {
      writer.get();
    } catch (IllegalStateException e) {
      //expected
    }
    writer.close();
    try {
      writer.get();
    } catch (IllegalStateException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testJsonTreeWriterMultipleWrites() throws IOException {
    List<JsonElement> jsonTrees = new ArrayList<>();
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement jsonTree1 = writer.beginObject().name("key1").value("value1")
        .endObject().get();
    jsonTrees.add(jsonTree1);
    JsonElement jsonTree2 = writer.beginObject().name("key2").value("value2")
        .endObject().get();
    jsonTrees.add(jsonTree2);
    JsonElement jsonTree3 = writer.beginObject().name("key3").value("value3")
        .endObject().get();
    jsonTrees.add(jsonTree3);
    for (JsonElement jsonTree : jsonTrees) {
      assertEquals("{\"key1\":\"value1\",\"key2\":\"value2\",\"key3\":\"value3\"}", jsonTree.toString());
    }
  }
}