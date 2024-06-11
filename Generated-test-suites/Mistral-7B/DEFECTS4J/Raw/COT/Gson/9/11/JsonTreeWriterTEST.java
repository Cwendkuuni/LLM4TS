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
  public void testGet() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key").value("value");
    JsonElement result = writer.get();
    assertEquals(new JsonObject().add("key", new JsonPrimitive("value")), result);
  }

  @Test
  public void testPeekAndPutForJsonObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonObject object = new JsonObject();
    writer.beginObject();
    writer.put(object);
    JsonObject expected = new JsonObject().add("name", new JsonPrimitive("value"));
    writer.name("name").value("value");
    JsonObject actual = (JsonObject) writer.peek();
    assertEquals(expected, actual);
  }

  @Test
  public void testPeekAndPutForJsonArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonArray array = new JsonArray();
    writer.beginArray();
    writer.put(array);
    writer.value(new JsonPrimitive(1));
    JsonArray actual = (JsonArray) writer.peek();
    JsonArray expected = new JsonArray().add(new JsonPrimitive(1));
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalStateException.class)
  public void testWriteAfterEndArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    writer.value("value");
  }

  @Test(expected = IllegalStateException.class)
  public void testWriteAfterEndObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    writer.name("name").value("value");
  }

  @Test
  public void testWriteNullValue() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.nullValue();
    JsonElement result = writer.get();
    assertEquals(JsonNull.INSTANCE, result);
  }

  @Test
  public void testWritePrimitiveString() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("value");
    JsonElement result = writer.get();
    assertEquals(new JsonPrimitive("value"), result);
  }

  @Test
  public void testWritePrimitiveBoolean() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(true);
    JsonElement result = writer.get();
    assertEquals(new JsonPrimitive(true), result);
  }

  @Test
  public void testWritePrimitiveNumber() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(1);
    JsonElement result = writer.get();
    assertEquals(new JsonPrimitive(1), result);
  }

  @Test
  public void testWritePrimitiveNumberWithDecimals() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(3.14);
    JsonElement result = writer.get();
    assertEquals(new JsonPrimitive(3.14), result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWritePrimitiveNaN() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(Double.NaN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWritePrimitiveInfinity() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(Double.POSITIVE_INFINITY);
  }

  @Test
  public void testBeginArrayAndEndArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value(new JsonPrimitive(1));
    writer.value(new JsonPrimitive(2));
    writer.endArray();
    JsonElement result = writer.get();
    JsonArray expected = new JsonArray().add(new JsonPrimitive(1)).add(new JsonPrimitive(2));
    assertEquals(expected, result);
  }

  @Test
  public void testBeginObjectAndEndObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name1").value(new JsonPrimitive("value1"));
    writer.name("name2").value(new JsonPrimitive("value2"));
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject expected = new JsonObject().add("name1", new JsonPrimitive("value1")).add("name2", new JsonPrimitive("value2"));
    assertEquals(expected, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testNameAfterEndObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    writer.name("name").value("value");
  }

  @Test(expected = IllegalStateException.class)
  public void testNameAfterBeginArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.name("name").value("value");
  }
}