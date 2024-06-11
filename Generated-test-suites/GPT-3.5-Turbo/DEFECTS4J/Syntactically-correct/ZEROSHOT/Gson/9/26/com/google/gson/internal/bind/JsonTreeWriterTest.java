import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class JsonTreeWriterTest {
  
  private JsonTreeWriter writer;
  
  @Before
  public void setUp() {
    writer = new JsonTreeWriter();
  }
  
  @Test
  public void testGet() {
    assertEquals(JsonNull.INSTANCE, writer.get());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testGetWithNonEmptyStack() {
    writer.beginArray();
    writer.get();
  }
  
  @Test
  public void testBeginArray() {
    writer.beginArray();
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonArray);
  }
  
  @Test
  public void testEndArray() {
    writer.beginArray();
    writer.endArray();
    JsonElement element = writer.get();
    assertEquals(JsonNull.INSTANCE, element);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testEndArrayBeforeBeginArray() {
    writer.endArray();
  }
  
  @Test
  public void testBeginObject() {
    writer.beginObject();
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonObject);
  }
  
  @Test
  public void testEndObject() {
    writer.beginObject();
    writer.endObject();
    JsonElement element = writer.get();
    assertEquals(JsonNull.INSTANCE, element);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testEndObjectBeforeBeginObject() {
    writer.endObject();
  }
  
  @Test
  public void testName() {
    writer.beginObject();
    writer.name("name");
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonObject);
    JsonObject object = (JsonObject) element;
    assertTrue(object.has("name"));
  }
  
  @Test(expected = IllegalStateException.class)
  public void testNameBeforeBeginObject() {
    writer.name("name");
  }
  
  @Test(expected = IllegalStateException.class)
  public void testNameAfterValue() {
    writer.beginObject();
    writer.value("value");
    writer.name("name");
  }
  
  @Test
  public void testValue() {
    writer.beginArray();
    writer.value("value");
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonArray);
    JsonArray array = (JsonArray) element;
    assertEquals(1, array.size());
    assertTrue(array.get(0) instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) array.get(0);
    assertEquals("value", primitive.getAsString());
  }
  
  @Test
  public void testValueWithNull() {
    writer.beginArray();
    writer.value(null);
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonArray);
    JsonArray array = (JsonArray) element;
    assertEquals(1, array.size());
    assertTrue(array.get(0) instanceof JsonNull);
  }
  
  @Test
  public void testValueWithBoolean() {
    writer.beginArray();
    writer.value(true);
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonArray);
    JsonArray array = (JsonArray) element;
    assertEquals(1, array.size());
    assertTrue(array.get(0) instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) array.get(0);
    assertEquals(true, primitive.getAsBoolean());
  }
  
  @Test
  public void testValueWithDouble() {
    writer.beginArray();
    writer.value(1.0);
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonArray);
    JsonArray array = (JsonArray) element;
    assertEquals(1, array.size());
    assertTrue(array.get(0) instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) array.get(0);
    assertEquals(1.0, primitive.getAsDouble(), 0.0);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testValueWithIllegalDouble() {
    writer.value(Double.NaN);
  }
  
  @Test
  public void testValueWithLong() {
    writer.beginArray();
    writer.value(1L);
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonArray);
    JsonArray array = (JsonArray) element;
    assertEquals(1, array.size());
    assertTrue(array.get(0) instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) array.get(0);
    assertEquals(1L, primitive.getAsLong());
  }
  
  @Test
  public void testValueWithNumber() {
    writer.beginArray();
    writer.value(1);
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonArray);
    JsonArray array = (JsonArray) element;
    assertEquals(1, array.size());
    assertTrue(array.get(0) instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) array.get(0);
    assertEquals(1, primitive.getAsInt());
  }
  
  @Test(expected = IOException.class)
  public void testFlush() throws IOException {
    writer.flush();
  }
  
  @Test(expected = IOException.class)
  public void testCloseWithNonEmptyStack() throws IOException {
    writer.beginArray();
    writer.close();
  }
  
  @Test
  public void testClose() throws IOException {
    writer.close();
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) element;
    assertEquals("closed", primitive.getAsString());
  }
}