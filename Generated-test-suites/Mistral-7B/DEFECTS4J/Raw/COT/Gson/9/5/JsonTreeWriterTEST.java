import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testGet() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.endObject();
    JsonElement result = writer.get();

    JsonObject expected = new JsonObject();
    expected.addProperty("name", "John Doe");

    assertEquals(expected, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetWithEmptyStack() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement result = writer.get();
  }

  @Test
  public void testPeek() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value(1);
    writer.value(2);
    writer.endArray();

    JsonArray expected = new JsonArray();
    expected.add(1);
    expected.add(2);

    JsonArray result = (JsonArray) writer.peek();

    assertEquals(expected, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testPeekWithEmptyStack() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement result = writer.peek();
  }

  @Test
  public void testPut() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.put(new JsonPrimitive("age"));
    writer.value(30);
    writer.endObject();
    JsonElement result = writer.get();

    JsonObject expected = new JsonObject();
    expected.addProperty("name", "John Doe");
    expected.addProperty("age", 30);

    assertEquals(expected, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testPutWithInvalidType() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.put(new JsonPrimitive(30).getAsInt()); // getAsInt() is not a method of JsonPrimitive
    writer.endObject();
  }

  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value(1);
    writer.value(2);
    writer.endArray();
    JsonArray result = (JsonArray) writer.get();

    JsonArray expected = new JsonArray();
    expected.add(1);
    expected.add(2);

    assertEquals(expected, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayWithName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray().name("arrayName");
  }

  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value(1);
    writer.value(2);
    writer.endArray();

    JsonElement result = writer.get();
    JsonArray expected = new JsonArray();
    expected.add(1);
    expected.add(2);

    assertEquals(expected, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWithEmptyStack() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endArray();
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("name").value("John Doe");
    writer.name("age").value(30);
    writer.endObject();
    JsonElement result = writer.get();

    JsonObject expected = new JsonObject();
    expected.addProperty("name", "John Doe");
    expected.addProperty("age", 30);

    assertEquals(expected, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectWithNameAndValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("name").value("John Doe").name("age").value(30);
  }

  @Test
  public void testEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("name").value("John Doe");
    writer.name("age").value(30);
    writer.endObject();

    JsonElement result = writer.get();
    JsonObject expected = new JsonObject();
    expected.addProperty("name", "John Doe");
    expected.addProperty("age", 30);

    assertEquals(expected, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWithEmptyStack() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endObject();
  }

  @Test
  public void testName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("name").value("John Doe");
    writer.name("age");
    writer.value(30);
    writer.endObject();

    JsonElement result = writer.get();
    JsonObject expected = new JsonObject();
    expected.addProperty("name", "John Doe");
    expected.addProperty("age", 30);

    assertEquals(expected, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testNameWithEmptyStack() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.name("name");
  }

  @Test
  public void testValueWithString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("name").value("John Doe");
    writer.endObject();
    JsonElement result = writer.get();

    JsonObject expected = new JsonObject();
    expected.addProperty("name", "John Doe");

    assertEquals(expected, result);
  }

  @Test
  public void testValueWithNull() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("name").value(null);
    writer.endObject();
    JsonElement result = writer.get();

    JsonObject expected = new JsonObject();
    expected.addProperty("name", null);

    assertEquals(expected, result);
  }

  @Test
  public void testValueWithBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("name").value(true);
    writer.endObject();
    JsonElement result = writer.get();

    JsonObject expected = new JsonObject();
    expected.addProperty("name", true);

    assertEquals(expected, result);
  }

  @Test
  public void testValueWithNumber() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("name").value(123);
    writer.endObject();
    JsonElement result = writer.get();

    JsonObject expected = new JsonObject();
    expected.addProperty("name", 123);

    assertEquals(expected, result);
  }

  @Test
  public void testValueWithDouble() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("name").value(3.14159265358979323846);
    writer.endObject();
    JsonElement result = writer.get();

    JsonObject expected = new JsonObject();
    expected.addProperty("name", 3.14159265358979323846);

    assertEquals(expected, result);
  }

  @Test
  public void testValueWithNumberWithLenient() {
    JsonTreeWriter writer = new JsonTreeWriter(true);
    writer.beginObject().name("name").value(Double.NaN);
    writer.endObject();
    JsonElement result = writer.get();

    JsonObject expected = new JsonObject();
    expected.addProperty("name", JsonNull.INSTANCE);

    assertEquals(expected, result);
  }

  @Test
  public void testValueWithNumberWithInfinite() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("name").value(Double.POSITIVE_INFINITY);
    writer.endObject();
    JsonElement result = writer.get();

    JsonObject expected = new JsonObject();
    expected.addProperty("name", "null"); // JSON forbids Infinity and NaN

    assertEquals(expected, result);
  }

  @Test
  public void testNullValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.nullValue();

    JsonNull expected = JsonNull.INSTANCE;

    assertEquals(expected, writer.peek());
  }

  @Test
  public void testFlush() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("name").value("John Doe");
    writer.endObject();

    writer.flush();

    JsonElement result = writer.get();
    JsonObject expected = new JsonObject();
    expected.addProperty("name", "John Doe");

    assertEquals(expected, result);
  }

  @Test
  public void testClose() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("name").value("John Doe");
    writer.endObject();

    writer.close();

    JsonElement result = writer.get();
    JsonObject expected = new JsonObject();
    expected.addProperty("name", "John Doe");

    assertEquals(expected, result);
  }

  @Test(expected = IOException.class)
  public void testCloseTwice() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("name").value("John Doe");
    writer.endObject();

    writer.close();
    writer.close();
  }

  @Test(expected = IOException.class)
  public void testCloseWithEmptyStack() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
  }
}