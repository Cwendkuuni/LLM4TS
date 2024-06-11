import static org.junit.Assert.*;
import org.junit.Test;

public class JsonTreeWriterTest {
  
  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    assertEquals("Expected JsonArray", JsonArray.class, writer.get().getClass());
  }
  
  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    assertEquals("Expected JsonObject", JsonObject.class, writer.get().getClass());
  }
  
  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    assertEquals("Expected JsonObject", JsonObject.class, writer.get().getClass());
  }
  
  @Test
  public void testEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    assertEquals("Expected JsonObject", JsonObject.class, writer.get().getClass());
  }
  
  @Test
  public void testName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("key");
    assertEquals("key", writer.get().getAsJsonObject().entrySet().iterator().next().getKey());
  }
  
  @Test
  public void testValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("value");
    assertEquals("value", writer.get().getAsString());
  }
  
  @Test
  public void testNullValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.nullValue();
    assertTrue(writer.get().isJsonNull());
  }
  
  @Test
  public void testValueBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(true);
    assertEquals(true, writer.get().getAsBoolean());
  }
  
  @Test
  public void testValueDouble() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(10.5);
    assertEquals(10.5, writer.get().getAsDouble(), 0.0);
  }
  
  @Test
  public void testValueLong() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(10L);
    assertEquals(10L, writer.get().getAsLong());
  }
  
  @Test
  public void testValueNumber() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(10);
    assertEquals(10, writer.get().getAsInt());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testInvalidEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endArray();
  }
  
  @Test(expected = IllegalStateException.class)
  public void testInvalidEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endObject();
  }
  
  @Test(expected = IllegalStateException.class)
  public void testInvalidName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.name("key");
  }
  
  @Test
  public void testFlush() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
        writer.flush();
    } catch (IOException e) {
        fail("IOException thrown");
    }
  }
  
  @Test(expected = IOException.class)
  public void testCloseIncompleteDocument() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.close();
  }
  
  @Test
  public void testClose() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
        writer.close();
    } catch (IOException e) {
        fail("IOException thrown");
    }
  }
}