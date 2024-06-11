import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class JsonTreeWriterTest {

  @Test
  public void testGet() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonElement result = jsonTreeWriter.get();
    assertEquals(JsonNull.INSTANCE, result);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetNotEmptyStack() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    List<JsonElement> stack = new ArrayList<>();
    stack.add(new JsonObject());
    jsonTreeWriter.stack = stack;
    jsonTreeWriter.get();
  }

  @Test
  public void testPeek() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    List<JsonElement> stack = new ArrayList<>();
    stack.add(new JsonObject());
    jsonTreeWriter.stack = stack;
    JsonElement result = jsonTreeWriter.peek();
    assertTrue(result instanceof JsonObject);
  }

  @Test
  public void testPutWithPendingName() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.pendingName = "name";
    JsonObject object = new JsonObject();
    List<JsonElement> stack = new ArrayList<>();
    stack.add(object);
    jsonTreeWriter.stack = stack;
    jsonTreeWriter.put(new JsonPrimitive("value"));
    assertEquals("value", object.get("name").getAsString());
  }

  @Test
  public void testPutWithArrayValue() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonArray array = new JsonArray();
    List<JsonElement> stack = new ArrayList<>();
    stack.add(array);
    jsonTreeWriter.stack = stack;
    jsonTreeWriter.put(new JsonPrimitive("value"));
    assertEquals("value", array.get(0).getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testPutWithInvalidElement() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.stack.add(new JsonPrimitive("value"));
    jsonTreeWriter.put(new JsonPrimitive("value"));
  }

  @Test
  public void testBeginArray() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonArray array = new JsonArray();
    jsonTreeWriter.put(array);
    jsonTreeWriter.stack.add(array);
    jsonTreeWriter.beginArray();
    assertEquals(1, jsonTreeWriter.stack.size());
    assertTrue(jsonTreeWriter.stack.get(0) instanceof JsonArray);
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWithEmptyStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWithPendingName() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.pendingName = "name";
    jsonTreeWriter.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWithInvalidElement() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.stack.add(new JsonObject());
    jsonTreeWriter.endArray();
  }

  @Test
  public void testBeginObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonObject object = new JsonObject();
    jsonTreeWriter.put(object);
    jsonTreeWriter.stack.add(object);
    jsonTreeWriter.beginObject();
    assertEquals(1, jsonTreeWriter.stack.size());
    assertTrue(jsonTreeWriter.stack.get(0) instanceof JsonObject);
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWithEmptyStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWithPendingName() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.pendingName = "name";
    jsonTreeWriter.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWithInvalidElement() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.stack.add(new JsonArray());
    jsonTreeWriter.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testNameWithEmptyStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.name("name");
  }

  @Test(expected = IllegalStateException.class)
  public void testNameWithPendingName() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.pendingName = "name1";
    jsonTreeWriter.name("name2");
  }

  @Test(expected = IllegalStateException.class)
  public void testNameWithInvalidElement() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.stack.add(new JsonPrimitive("value"));
    jsonTreeWriter.name("name");
  }

  @Test
  public void testValueWithString() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value("value");
    JsonElement result = jsonTreeWriter.stack.get(0);
    assertEquals("value", result.getAsString());
  }

  @Test
  public void testValueWithNull() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(null);
    JsonElement result = jsonTreeWriter.stack.get(0);
    assertEquals(JsonNull.INSTANCE, result);
  }

  @Test
  public void testValueWithBoolean() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(true);
    JsonElement result = jsonTreeWriter.stack.get(0);
    assertTrue(result.getAsBoolean());
  }

  @Test
  public void testValueWithDouble() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(1.0);
    JsonElement result = jsonTreeWriter.stack.get(0);
    assertEquals(1.0, result.getAsDouble(), 0);
  }

  @Test
  public void testValueWithLong() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(1L);
    JsonElement result = jsonTreeWriter.stack.get(0);
    assertEquals(1L, result.getAsLong());
  }

  @Test
  public void testValueWithNumber() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(1);
    JsonElement result = jsonTreeWriter.stack.get(0);
    assertEquals(1, result.getAsInt());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueWithInvalidDouble() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.setLenient(false);
    jsonTreeWriter.value(Double.NaN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueWithInvalidLong() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.setLenient(false);
    jsonTreeWriter.value(Long.MAX_VALUE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueWithInvalidNumber() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.setLenient(false);
    jsonTreeWriter.value(Double.POSITIVE_INFINITY);
  }

  @Test
  public void testValueWithInvalidString() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value((String) null);
    JsonElement result = jsonTreeWriter.stack.get(0);
    assertEquals(JsonNull.INSTANCE, result);
  }

  @Test
  public void testNullValue() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.nullValue();
    JsonElement result = jsonTreeWriter.stack.get(0);
    assertEquals(JsonNull.INSTANCE, result);
  }

  @Test
  public void testFlush() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.flush();
  }

  @Test(expected = IOException.class)
  public void testCloseWithNotEmptyStack() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.stack.add(new JsonObject());
    jsonTreeWriter.close();
  }

  @Test
  public void testClose() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.close();
    assertEquals(1, jsonTreeWriter.stack.size());
    assertTrue(jsonTreeWriter.stack.get(0) instanceof JsonPrimitive);
  }

}