import static org.junit.Assert.*;
import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testGet() {
    JsonTreeWriter writer = new JsonTreeWriter();
    assertEquals(JsonNull.INSTANCE, writer.get());
  }

  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    assertEquals(JsonArray.class, writer.peek().getClass());
  }

  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    assertEquals(JsonNull.INSTANCE, writer.peek());
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    assertEquals(JsonObject.class, writer.peek().getClass());
  }

  @Test
  public void testEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    assertEquals(JsonNull.INSTANCE, writer.peek());
  }

  @Test
  public void testName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    assertEquals("name", writer.pendingName);
  }

  @Test(expected = IllegalStateException.class)
  public void testNameWhenNotInObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.name("name");
  }

  @Test(expected = IllegalStateException.class)
  public void testNameWhenPendingNameNotNull() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name");
    writer.name("name2");
  }

  @Test
  public void testValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("value");
    assertEquals(JsonPrimitive.class, writer.peek().getClass());
    assertEquals("value", ((JsonPrimitive) writer.peek()).getAsString());
  }

  @Test
  public void testValueNull() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.nullValue();
    assertEquals(JsonNull.INSTANCE, writer.peek());
  }

  @Test
  public void testValueBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(true);
    assertEquals(JsonPrimitive.class, writer.peek().getClass());
    assertEquals(true, ((JsonPrimitive) writer.peek()).getAsBoolean());
  }

  @Test
  public void testValueDouble() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(1.23);
    assertEquals(JsonPrimitive.class, writer.peek().getClass());
    assertEquals(1.23, ((JsonPrimitive) writer.peek()).getAsDouble(), 0.0001);
  }

  @Test
  public void testValueLong() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(123L);
    assertEquals(JsonPrimitive.class, writer.peek().getClass());
    assertEquals(123L, ((JsonPrimitive) writer.peek()).getAsLong());
  }

  @Test
  public void testValueNumber() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(1.23);
    assertEquals(JsonPrimitive.class, writer.peek().getClass());
    assertEquals(1.23, ((JsonPrimitive) writer.peek()).getAsDouble(), 0.0001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueDoubleNaN() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(Double.NaN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueDoublePositiveInfinity() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(Double.POSITIVE_INFINITY);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueDoubleNegativeInfinity() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(Double.NEGATIVE_INFINITY);
  }

  @Test(expected = IllegalStateException.class)
  public void testFlush() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.flush();
  }

  @Test(expected = IOException.class)
  public void testCloseIncompleteDocument() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.close();
  }

  @Test
  public void testClose() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    assertEquals(1, writer.stack.size());
    assertEquals(JsonPrimitive.class, writer.stack.get(0).getClass());
  }

  @Test
  public void testCloseAfterClose() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    writer.close();
    assertEquals(2, writer.stack.size());
    assertEquals(JsonPrimitive.class, writer.stack.get(0).getClass());
  }

}