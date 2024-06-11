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
  public void testBeginArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonArray);
  }
  
  @Test
  public void testEndArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonNull);
  }
  
  @Test
  public void testBeginObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonObject);
  }
  
  @Test
  public void testEndObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonNull);
  }
  
  @Test
  public void testName() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key");
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonObject);
    JsonObject object = (JsonObject) element;
    assertTrue(object.has("key"));
  }
  
  @Test
  public void testValueString() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("value");
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) element;
    assertEquals("value", primitive.getAsString());
  }
  
  @Test
  public void testNullValue() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.nullValue();
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonNull);
  }
  
  @Test
  public void testValueBoolean() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(true);
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) element;
    assertTrue(primitive.getAsBoolean());
  }
  
  @Test
  public void testValueDouble() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(1.5);
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) element;
    assertEquals(1.5, primitive.getAsDouble());
  }
  
  @Test
  public void testValueLong() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(100L);
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) element;
    assertEquals(100L, primitive.getAsLong());
  }
  
  @Test
  public void testValueNumber() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(42);
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) element;
    assertEquals(42, primitive.getAsInt());
  }
  
  @Test(expected = IOException.class)
  public void testCloseIncompleteDocument() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.close();
  }
  
  @Test
  public void testCloseCompleteDocument() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    writer.close();
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) element;
    assertEquals("closed", primitive.getAsString());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testEndArrayWithoutBeginArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endArray();
  }
  
  @Test(expected = IllegalStateException.class)
  public void testEndObjectWithoutBeginObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endObject();
  }
  
  @Test(expected = IllegalStateException.class)
  public void testNameWithoutBeginObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.name("key");
  }
  
  @Test(expected = IllegalStateException.class)
  public void testValueStringWithoutBeginArrayOrObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("value");
  }
  
  @Test(expected = IllegalStateException.class)
  public void testNullValueWithoutBeginArrayOrObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.nullValue();
  }
  
  @Test(expected = IllegalStateException.class)
  public void testValueBooleanWithoutBeginArrayOrObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(true);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testValueDoubleWithoutBeginArrayOrObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(1.5);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testValueLongWithoutBeginArrayOrObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(100L);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testValueNumberWithoutBeginArrayOrObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(42);
  }
  
  @Test(expected = IOException.class)
  public void testFlush() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.flush();
  }
}