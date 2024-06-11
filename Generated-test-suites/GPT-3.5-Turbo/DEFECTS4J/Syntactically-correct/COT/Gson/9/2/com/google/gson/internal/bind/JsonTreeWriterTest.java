
import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class JsonTreeWriterTest {
  
  private JsonTreeWriter jsonTreeWriter;
  
  @Before
  public void setup() {
    jsonTreeWriter = new JsonTreeWriter();
  }
  
  @Test
  public void testBeginArray() {
    jsonTreeWriter.beginArray();
    JsonElement result = jsonTreeWriter.get();
    
    assertTrue(result instanceof JsonArray);
  }
  
  @Test
  public void testEndArray() {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
    JsonElement result = jsonTreeWriter.get();
    
    assertTrue(result instanceof JsonNull);
  }
  
  @Test
  public void testBeginObject() {
    jsonTreeWriter.beginObject();
    JsonElement result = jsonTreeWriter.get();
    
    assertTrue(result instanceof JsonObject);
  }
  
  @Test
  public void testEndObject() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    JsonElement result = jsonTreeWriter.get();
    
    assertTrue(result instanceof JsonNull);
  }
  
  @Test
  public void testName() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    jsonTreeWriter.value("value");
    jsonTreeWriter.endObject();
    JsonElement result = jsonTreeWriter.get();
    
    assertTrue(result instanceof JsonObject);
    assertEquals(((JsonObject) result).get("key").getAsString(), "value");
  }
  
  @Test
  public void testValueString() {
    jsonTreeWriter.value("string");
    JsonElement result = jsonTreeWriter.get();
    
    assertTrue(result instanceof JsonPrimitive);
    assertEquals(((JsonPrimitive) result).getAsString(), "string");
  }
  
  @Test
  public void testNullValue() {
    jsonTreeWriter.nullValue();
    JsonElement result = jsonTreeWriter.get();
    
    assertTrue(result instanceof JsonNull);
  }
  
  @Test
  public void testValueBoolean() {
    jsonTreeWriter.value(true);
    JsonElement result = jsonTreeWriter.get();
    
    assertTrue(result instanceof JsonPrimitive);
    assertEquals(((JsonPrimitive) result).getAsBoolean(), true);
  }
  
  @Test
  public void testValueDouble() {
    jsonTreeWriter.value(1.5);
    JsonElement result = jsonTreeWriter.get();
    
    assertTrue(result instanceof JsonPrimitive);
    assertEquals(((JsonPrimitive) result).getAsDouble(), 1.5, 0.001);
  }
  
  @Test
  public void testValueLong() {
    jsonTreeWriter.value(123456789L);
    JsonElement result = jsonTreeWriter.get();
    
    assertTrue(result instanceof JsonPrimitive);
    assertEquals(((JsonPrimitive) result).getAsLong(), 123456789L);
  }
  
  @Test
  public void testValueNumber() {
    jsonTreeWriter.value(100);
    JsonElement result = jsonTreeWriter.get();
    
    assertTrue(result instanceof JsonPrimitive);
    assertEquals(((JsonPrimitive) result).getAsNumber(), 100);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testIncompleteDocument() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.flush();
    jsonTreeWriter.close();
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testForbidsNaN() {
    jsonTreeWriter.value(Double.NaN);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testForbidsInfinity() {
    jsonTreeWriter.value(Double.POSITIVE_INFINITY);
  }
}