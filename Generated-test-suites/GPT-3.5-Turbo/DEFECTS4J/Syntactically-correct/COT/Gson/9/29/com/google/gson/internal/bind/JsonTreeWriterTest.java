import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void get() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement element = writer.get();
    assertNotNull(element);
    assertSame(JsonNull.INSTANCE, element);
  }

  @Test
  public void beginArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    JsonElement element = writer.get();
    assertNotNull(element);
    assertTrue(element instanceof JsonArray);
  }

  @Test
  public void endArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    JsonElement element = writer.get();
    assertNotNull(element);
    assertSame(JsonNull.INSTANCE, element);
  }

  @Test
  public void beginObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    JsonElement element = writer.get();
    assertNotNull(element);
    assertTrue(element instanceof JsonObject);
  }

  @Test
  public void endObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    JsonElement element = writer.get();
    assertNotNull(element);
    assertSame(JsonNull.INSTANCE, element);
  }

  @Test
  public void name() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key");
    JsonElement element = writer.get();
    assertNotNull(element);
    assertTrue(element instanceof JsonObject);
    JsonObject object = (JsonObject) element;
    assertTrue(object.has("key"));
  }

  @Test
  public void valueString() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("value");
    JsonElement element = writer.get();
    assertNotNull(element);
    assertTrue(element instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) element;
    assertTrue(primitive.isString());
    assertEquals("value", primitive.getAsString());
  }

  @Test
  public void nullValue() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.nullValue();
    JsonElement element = writer.get();
    assertNotNull(element);
    assertSame(JsonNull.INSTANCE, element);
  }

  @Test
  public void valueBoolean() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(true);
    JsonElement element = writer.get();
    assertNotNull(element);
    assertTrue(element instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) element;
    assertTrue(primitive.isBoolean());
    assertTrue(primitive.getAsBoolean());
  }

  @Test
  public void valueDouble() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(3.14);
    JsonElement element = writer.get();
    assertNotNull(element);
    assertTrue(element instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) element;
    assertTrue(primitive.isNumber());
    assertEquals(3.14, primitive.getAsDouble(), 0.001);
  }

  @Test
  public void valueLong() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(42L);
    JsonElement element = writer.get();
    assertNotNull(element);
    assertTrue(element instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) element;
    assertTrue(primitive.isNumber());
    assertEquals(42L, primitive.getAsLong());
  }

  @Test
  public void valueNumber() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(42);
    JsonElement element = writer.get();
    assertNotNull(element);
    assertTrue(element instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) element;
    assertTrue(primitive.isNumber());
    assertEquals(42, primitive.getAsInt());
  }

  @Test(expected = IllegalStateException.class)
  public void valueNumberWithLenientFalseAndInfinity() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.setLenient(false);
    writer.value(Double.POSITIVE_INFINITY);
  }

  @Test(expected = IllegalStateException.class)
  public void valueNumberWithLenientFalseAndNaN() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.setLenient(false);
    writer.value(Double.NaN);
  }

  @Test
  public void flush() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.flush();
    // No testable behavior since flush method is empty
  }

  @Test(expected = IOException.class)
  public void closeWithIncompleteDocument() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.close();
  }

  @Test
  public void close() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();
    JsonElement element = writer.get();
    assertNotNull(element);
    assertSame(JsonNull.INSTANCE, element);
  }
}