import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JsonTreeWriterTest {
  
  private JsonTreeWriter jsonTreeWriter;
  
  @Before
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }
  
  @Test
  public void testGet() {
    JsonElement jsonElement = jsonTreeWriter.get();
    assertEquals(JsonNull.INSTANCE, jsonElement);
  }
  
  @Test
  public void testValueString() {
    jsonTreeWriter.value("test");
    JsonElement jsonElement = jsonTreeWriter.get();
    assertTrue(jsonElement.isJsonPrimitive());
    assertEquals("test", jsonElement.getAsString());
  }
  
  @Test
  public void testValueNull() {
    jsonTreeWriter.nullValue();
    JsonElement jsonElement = jsonTreeWriter.get();
    assertTrue(jsonElement.isJsonNull());
  }
  
  @Test
  public void testValueBoolean() {
    jsonTreeWriter.value(true);
    JsonElement jsonElement = jsonTreeWriter.get();
    assertTrue(jsonElement.isJsonPrimitive());
    assertEquals(true, jsonElement.getAsBoolean());
  }
  
  @Test
  public void testValueDouble() {
    jsonTreeWriter.value(10.5);
    JsonElement jsonElement = jsonTreeWriter.get();
    assertTrue(jsonElement.isJsonPrimitive());
    assertEquals(10.5, jsonElement.getAsDouble(), 0);
  }
  
  @Test
  public void testValueLong() {
    jsonTreeWriter.value(100);
    JsonElement jsonElement = jsonTreeWriter.get();
    assertTrue(jsonElement.isJsonPrimitive());
    assertEquals(100, jsonElement.getAsLong());
  }
  
  @Test
  public void testValueNumber() {
    jsonTreeWriter.value(10.5);
    JsonElement jsonElement = jsonTreeWriter.get();
    assertTrue(jsonElement.isJsonPrimitive());
    assertEquals(10.5, jsonElement.getAsDouble(), 0);
  }
  
  @Test
  public void testBeginArray() {
    jsonTreeWriter.beginArray();
    JsonElement jsonElement = jsonTreeWriter.get();
    assertTrue(jsonElement.isJsonArray());
  }
  
  @Test
  public void testEndArray() {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
    JsonElement jsonElement = jsonTreeWriter.get();
    assertTrue(jsonElement.isJsonNull());
  }
  
  @Test
  public void testBeginObject() {
    jsonTreeWriter.beginObject();
    JsonElement jsonElement = jsonTreeWriter.get();
    assertTrue(jsonElement.isJsonObject());
  }
  
  @Test
  public void testEndObject() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    JsonElement jsonElement = jsonTreeWriter.get();
    assertTrue(jsonElement.isJsonObject());
  }
  
  @Test
  public void testName() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    JsonObject jsonObject = (JsonObject) jsonTreeWriter.get();
    assertTrue(jsonObject.has("key"));
  }
}