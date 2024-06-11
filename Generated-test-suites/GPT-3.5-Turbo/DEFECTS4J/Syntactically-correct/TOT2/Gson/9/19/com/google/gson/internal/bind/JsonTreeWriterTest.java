import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeWriterTest {
  
  @Test
  public void testGet() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonElement result = jsonTreeWriter.get();
    assertEquals(JsonNull.INSTANCE, result);
  }
  
  @Test
  public void testBeginArray() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertNotNull(jsonTreeWriter.beginArray());
  }
  
  @Test
  public void testEndArray() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.endArray());
  }
  
  @Test
  public void testBeginObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertNotNull(jsonTreeWriter.beginObject());
  }
  
  @Test
  public void testEndObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.endObject());
  }
  
  @Test
  public void testName() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.name("test"));
  }
  
  @Test
  public void testValueString() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertDoesNotThrow(() -> jsonTreeWriter.value("test"));
  }
  
  @Test
  public void testValueNull() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertDoesNotThrow(() -> jsonTreeWriter.value(null));
  }
  
  @Test
  public void testValueBoolean() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertDoesNotThrow(() -> jsonTreeWriter.value(true));
  }
  
  @Test
  public void testValueDouble() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertDoesNotThrow(() -> jsonTreeWriter.value(3.14));
  }
  
  @Test
  public void testValueLong() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertDoesNotThrow(() -> jsonTreeWriter.value(100L));
  }
  
  @Test
  public void testValueNumber() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertDoesNotThrow(() -> jsonTreeWriter.value(123));
  }
  
  @Test
  public void testFlush() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertDoesNotThrow(() -> jsonTreeWriter.flush());
  }
  
  @Test
  public void testClose() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertThrows(IOException.class, () -> jsonTreeWriter.close());
  }
}