import static org.junit.Assert.*;
import java.io.IOException;
import org.junit.Test;

public class JsonTreeWriterTest {
  
  @Test
  public void testGet() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertEquals(JsonNull.INSTANCE, jsonTreeWriter.get());
    
    jsonTreeWriter.beginObject();
    JsonObject object = new JsonObject();
    jsonTreeWriter.put(object);
    jsonTreeWriter.stack.add(object);
    assertEquals(object, jsonTreeWriter.get());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testGetException() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.stack.add(new JsonPrimitive("value"));
    jsonTreeWriter.get();
  }
  
  @Test
  public void testPeek() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    
    JsonObject object = (JsonObject) jsonTreeWriter.peek();
    assertEquals(object, jsonTreeWriter.stack.get(jsonTreeWriter.stack.size() - 1));
  }
  
  @Test
  public void testPut() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonObject object = new JsonObject();
    jsonTreeWriter.put(object);
    
    assertEquals(object, jsonTreeWriter.peek());
  }
  
  @Test
  public void testBeginArray() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    
    JsonArray array = (JsonArray) jsonTreeWriter.peek();
    assertEquals(array, jsonTreeWriter.get());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testEndArray() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
    jsonTreeWriter.endArray();
  }
  
  @Test
  public void testBeginObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    
    JsonObject object = (JsonObject) jsonTreeWriter.peek();
    assertEquals(object, jsonTreeWriter.get());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testEndObject() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    jsonTreeWriter.endObject();
  }
  
  @Test(expected = IllegalStateException.class)
  public void testNameException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.name("name");
  }
  
  @Test(expected = IllegalStateException.class)
  public void testValueWithNull() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertNull(jsonTreeWriter.value(null));
  }
  
  @Test
  public void testValueWithBoolean() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertTrue(jsonTreeWriter.value(true) instanceof JsonTreeWriter);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testValueWithDouble() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(Double.NaN);
  }
  
  @Test
  public void testValueWithLong() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertTrue(jsonTreeWriter.value(123L) instanceof JsonTreeWriter);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testValueWithNumberException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.value(null);
  }
  
  @Test
  public void testFlush() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.flush();
  }
  
  @Test(expected = IOException.class)
  public void testCloseException() throws IOException {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.close();
    jsonTreeWriter.close();
  }
}