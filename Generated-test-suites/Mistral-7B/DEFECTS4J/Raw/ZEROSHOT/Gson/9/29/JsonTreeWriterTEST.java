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

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;

public class JsonTreeWriterTest {

  @Test
  public void testJsonTreeWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();

    JsonElement rootElement = writer.beginObject()
        .name("foo")
        .value("bar")
        .name("baz")
        .beginArray()
          .value(1)
          .value(2.0)
          .value(true)
          .name("qux")
          .value("quux")
          .endArray()
        .endObject()
        .get();

    assertEquals(new JsonObject()
        .addProperty("foo", "bar")
        .add("baz", new JsonArray()
            .add(1)
            .add(2.0)
            .add(true)
            .addProperty("qux", "quux")
        )
        .toString(), rootElement.toString());
  }

  @Test
  public void testJsonTreeWriterWithLenient() {
    JsonTreeWriter writer = new JsonTreeWriter(true); // setting isLenient to true

    JsonElement rootElement = writer.beginObject()
        .name("foo")
        .value("bar")
        .name("baz")
        .beginArray()
          .value(1)
          .value(Double.NaN)
          .value(Double.POSITIVE_INFINITY)
          .name("qux")
          .value("quux")
          .endArray()
        .endObject()
        .get();

    assertEquals(new JsonObject()
        .addProperty("foo", "bar")
        .add("baz", new JsonArray()
            .add(1)
            .add(JsonNull.INSTANCE)
            .add(JsonNull.INSTANCE)
            .addProperty("qux", "quux")
        )
        .toString(), rootElement.toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterInvalidUseCase1() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endArray();
    writer.value("test");
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterInvalidUseCase2() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.name("test");
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterInvalidUseCase3() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("test");
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeWriterInvalidUseCase4() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.beginArray();
  }

  @Test
  public void testJsonTreeWriterGet() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement rootElement = writer.beginObject()
        .name("foo")
        .value("bar")
        .name("baz")
        .beginArray()
          .value(1)
          .value(2.0)
          .value(true)
          .name("qux")
          .value("quux")
          .endArray()
        .endObject()
        .get();

    assertEquals(rootElement, writer.get());
  }

  @Test
  public void testJsonTreeWriterPeek() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement rootElement = writer.beginObject()
        .name("foo")
        .value("bar")
        .name("baz")
        .beginArray()
          .value(1)
          .value(2.0)
          .value(true)
          .name("qux")
          .value("quux")
          .endArray()
        .endObject()
        .get();

    JsonElement peekedElement = writer.peek();
    assertEquals(peekedElement, rootElement);
  }

  @Test
  public void testJsonTreeWriterPut() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.put(new JsonArray());
    assertTrue(writer.stack.size() > 0);
    assertSame(writer.stack.get(0), writer.peek());

    writer.put(new JsonPrimitive(1));
    assertFalse(writer.stack.get(0) instanceof JsonArray);
    assertSame(writer.stack.get(0), writer.peek());

    writer.put(new JsonObject());
    assertTrue(writer.stack.get(0) instanceof JsonObject);
    assertSame(writer.stack.get(0), writer.peek());
  }

  @Test
  public void testJsonTreeWriterBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();

    JsonArray array = writer.beginArray();
    writer.put(new JsonPrimitive(1));
    writer.put(new JsonPrimitive(2));
    JsonArray resultArray = writer.endArray();

    List<JsonElement> expectedArray = new ArrayList<>();
    expectedArray.add(new JsonPrimitive(1));
    expectedArray.add(new JsonPrimitive(2));

    assertEquals(expectedArray, resultArray.getAsJsonArray().getAsJsonArray());
  }

  @Test
  public void testJsonTreeWriterEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();

    JsonArray array = writer.beginArray();
    JsonElement element1 = new JsonPrimitive(1);
    JsonElement element2 = new JsonObject();
    JsonArray resultArray = writer.endArray();

    List<JsonElement> expectedArray = new ArrayList<>();
    expectedArray.add(element1);
    expectedArray.add(element2);

    assertFalse(resultArray.isJsonNull());
    assertFalse(resultArray.isJsonArray());

    assertEquals(expectedArray, resultArray.getAsJsonArray().getAsJsonArray());
  }

  @Test
  public void testJsonTreeWriterBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();

    JsonObject object = writer.beginObject();
    writer.name("foo");
    writer.value("bar");
    writer.name("baz");
    JsonObject resultObject = writer.endObject();

    JsonObject expectedObject = new JsonObject();
    expectedObject.addProperty("foo", "bar");
    expectedObject.addProperty("baz", JsonNull.INSTANCE);

    assertEquals(expectedObject, resultObject);
  }

  @Test
  public void testJsonTreeWriterEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();

    JsonObject object = writer.beginObject();
    JsonElement element1 = new JsonPrimitive(1);
    JsonElement element2 = new JsonObject();
    writer.name("foo");
    writer.value(element1);
    writer.name("baz");
    writer.value(element2);
    JsonObject resultObject = writer.endObject();

    JsonObject expectedObject = new JsonObject();
    expectedObject.addProperty("foo", element1.getAsString());
    expectedObject.add("baz", element2);

    assertEquals(expectedObject, resultObject);
  }

  @Test
  public void testJsonTreeWriterValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.value("test");

    JsonPrimitive expectedValue = new JsonPrimitive("test");
    assertEquals(expectedValue, writer.peek());
  }

  @Test
  public void testJsonTreeWriterValueBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.value(true);

    JsonPrimitive expectedValue = new JsonPrimitive(true);
    assertEquals(expectedValue, writer.peek());
  }

  @Test
  public void testJsonTreeWriterValueDouble() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.value(1.5);

    JsonPrimitive expectedValue = new JsonPrimitive(1.5);
    assertEquals(expectedValue, writer.peek());
  }

  @Test
  public void testJsonTreeWriterValueLong() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.value(1L);

    JsonPrimitive expectedValue = new JsonPrimitive(1L);
    assertEquals(expectedValue, writer.peek());
  }

  @Test
  public void testJsonTreeWriterValueNumber() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.value(new JsonPrimitive(1));

    JsonPrimitive expectedValue = new JsonPrimitive(1);
    assertEquals(expectedValue, writer.peek());
  }

  @Test
  public void testJsonTreeWriterName() {
    JsonTreeWriter writer = new JsonTreeWriter();

    JsonObject object = writer.beginObject();
    writer.name("foo");

    JsonObject expectedObject = new JsonObject();
    expectedObject.addProperty("foo", JsonNull.INSTANCE);

    assertEquals(expectedObject, object);
  }

  @Test
  public void testJsonTreeWriterNullValue() {
    JsonTreeWriter writer = new JsonTreeWriter();

    JsonTreeWriter nullWriter = writer.nullValue();

    assertNull(nullWriter.peek());
  }

  @Test
  public void testJsonTreeWriterFlush() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    OutputStreamWriter writer = new OutputStreamWriter(outputStream);
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter(writer);

    jsonTreeWriter.beginObject()
        .name("foo")
        .value("bar")
        .endObject();

    jsonTreeWriter.flush();

    JsonElement jsonElement = new JsonParser(new StringReader(outputStream.toString())).parse();

    JsonObject expectedObject = new JsonObject();
    expectedObject.addProperty("foo", "bar");

    assertEquals(expectedObject, jsonElement);
  }

  @Test
  public void testJsonTreeWriterClose() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();

    JsonTreeWriter closedWriter = writer.close();

    assertSame(SENTINEL_CLOSED, closedWriter.peek());
  }
}