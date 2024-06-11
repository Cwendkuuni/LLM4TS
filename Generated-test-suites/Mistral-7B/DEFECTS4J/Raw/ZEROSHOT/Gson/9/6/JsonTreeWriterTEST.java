import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testEmptyWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement result = writer.get();
    assertNull(result);
  }

  @Test
  public void testBeginAndEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    JsonElement result = writer.get();
    JsonArray expected = new JsonArray();
    assertEquals(expected, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayWhenObjectIsPending() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.name("foo");
    writer.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWhenObjectIsPending() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.name("foo");
    writer.beginArray();
    writer.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayTwice() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.beginArray();
  }

  @Test
  public void testWriteArrayWithValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("foo");
    writer.endArray();
    JsonElement result = writer.get();
    JsonArray expected = new JsonArray().add("foo");
    assertEquals(expected, result);
  }

  @Test
  public void testWriteArrayWithMultipleValues() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("foo");
    writer.value("bar");
    writer.value(42);
    writer.endArray();
    JsonElement result = writer.get();
    JsonArray expected = new JsonArray().add("foo").add("bar").add(42);
    assertEquals(expected, result);
  }

  @Test
  public void testBeginAndEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(30);
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject expected = new JsonObject()
        .addProperty("name", "John Doe")
        .addProperty("age", 30);
    assertEquals(expected, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectWhenArrayIsPending() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.beginObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWhenArrayIsPending() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectTwice() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.beginObject();
  }

  @Test
  public void testWriteObjectWithKeyValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject expected = new JsonObject().addProperty("name", "John Doe");
    assertEquals(expected, result);
  }

  @Test
  public void testWriteObjectWithMultipleKeyValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(30);
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject expected = new JsonObject()
        .addProperty("name", "John Doe")
        .addProperty("age", 30);
    assertEquals(expected, result);
  }

  @Test
  public void testWriteObjectWithNestedObjects() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.beginObject().name("address").name("city").value("New York")
        .endObject().endObject();
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject expected = new JsonObject()
        .addProperty("name", "John Doe")
        .add(new JsonObject()
            .addProperty("city", "New York")
        );
    assertEquals(expected, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testWriteNullValueWhenObjectIsPending() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.nullValue();
  }

  @Test
  public void testWriteNullValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(null);
    JsonElement result = writer.get();
    assertEquals(JsonNull.INSTANCE, result);
  }

  @Test
  public void testWriteBooleanValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(true);
    JsonElement result = writer.get();
    assertEquals(new JsonPrimitive(true), result);
  }

  @Test
  public void testWriteNumberValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(42);
    JsonElement result = writer.get();
    assertEquals(new JsonPrimitive(42), result);
  }

  @Test
  public void testWriteStringValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("Hello World!");
    JsonElement result = writer.get();
    assertEquals(new JsonPrimitive("Hello World!"), result);
  }

  @Test(expected = IllegalStateException.class)
  public void testWriteValueWhenArrayIsPending() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("foo");
  }

  @Test(expected = IllegalStateException.class)
  public void testWriteValueWhenObjectIsPending() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.value("foo");
  }

  @Test
  public void testWriteValueTwice() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("foo");
    writer.value("bar");
    JsonElement result = writer.get();
    JsonArray expected = new JsonArray().add("foo").add("bar");
    assertEquals(expected, result);
  }
}