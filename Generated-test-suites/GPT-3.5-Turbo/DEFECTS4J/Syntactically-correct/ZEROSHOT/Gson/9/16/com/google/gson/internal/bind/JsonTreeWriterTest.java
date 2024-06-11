import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonTreeWriterTest {
  
  @Test
  public void testGet() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement element = writer.get();
    assertEquals(JsonNull.INSTANCE, element);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testGetThrowsException() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonObject object = new JsonObject();
    writer.beginObject().endObject();
    JsonElement element = writer.get();
  }
  
  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    assertEquals(1, writer.stack.size());
    assertTrue(writer.peek() instanceof JsonArray);
  }
  
  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    assertEquals(0, writer.stack.size());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testEndArrayThrowsException() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endArray();
  }
  
  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    assertEquals(1, writer.stack.size());
    assertTrue(writer.peek() instanceof JsonObject);
  }
  
  @Test
  public void testEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    assertEquals(0, writer.stack.size());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testEndObjectThrowsException() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endObject();
  }
  
  @Test(expected = IllegalStateException.class)
  public void testNameThrowsExceptionWhenStackIsEmpty() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.name("name");
  }
  
  @Test(expected = IllegalStateException.class)
  public void testNameThrowsExceptionWhenNotInObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.name("name");
  }
  
  @Test
  public void testName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    assertEquals("name", writer.pendingName);
  }
  
  @Test
  public void testValueWithNull() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value((String) null);
    assertEquals(JsonNull.INSTANCE, writer.product.getAsJsonPrimitive());
  }
  
  @Test
  public void testValueWithString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("string");
    assertEquals("string", writer.product.getAsString());
  }
  
  @Test
  public void testNullValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.nullValue();
    assertEquals(JsonNull.INSTANCE, writer.product.getAsJsonPrimitive());
  }
  
  @Test
  public void testValueWithBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(true);
    assertEquals(true, writer.product.getAsBoolean());
    writer.value(false);
    assertEquals(false, writer.product.getAsBoolean());
  }
  
  @Test
  public void testValueWithDouble() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(2.5);
    assertEquals(2.5, writer.product.getAsDouble(), 0.001);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testValueWithNaN() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(Double.NaN);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testValueWithInfinite() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(Double.POSITIVE_INFINITY);
  }
  
  @Test
  public void testValueWithLong() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(10L);
    assertEquals(10L, writer.product.getAsLong());
  }
  
  @Test
  public void testValueWithNumber() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(10);
    assertEquals(10, writer.product.getAsInt());
    writer.value(10.3f);
    assertEquals(10.3f, writer.product.getAsFloat(), 0.001);
  }
  
  @Test(expected = IOException.class)
  public void testFlush() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.flush();
  }
  
  @Test(expected = IOException.class)
  public void testCloseThrowsException() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
  }
  
  @Test(expected = IOException.class)
  public void testCloseThrowsExceptionWhenStackIsNotEmpty() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().endObject().close();
  }
  
  @Test(expected = IOException.class)
  public void testCloseThrowsExceptionWhenPendingNameIsNotNull() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("name").close();
  }
  
  @Test
  public void testClose() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    assertEquals(JsonTreeWriter.SENTINEL_CLOSED, writer.stack.get(0));
  }
  
}