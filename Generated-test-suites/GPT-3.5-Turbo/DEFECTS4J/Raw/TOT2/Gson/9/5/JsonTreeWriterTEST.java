import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testGet() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("key", "value");
    
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    jsonTreeWriter.value("value");
    jsonTreeWriter.endObject();
    
    assertEquals(jsonObject, jsonTreeWriter.get());
  }

  @Test
  public void testValueString() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    String value = "testString";
    
    jsonTreeWriter.value(value);
    
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("value", value);
    
    assertEquals(jsonObject, jsonTreeWriter.get());
  }

  @Test
  public void testValueNull() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    
    jsonTreeWriter.nullValue();
    
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("nullValue", null);
    
    assertEquals(jsonObject, jsonTreeWriter.get());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueInvalidDouble() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    double value = Double.NaN;
    
    jsonTreeWriter.value(value);
  }

  @Test
  public void testValueDouble() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    double value = 10.5;
    
    jsonTreeWriter.value(value);
    
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("value", value);
    
    assertEquals(jsonObject, jsonTreeWriter.get());
  }

  @Test
  public void testValueLong() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    long value = 100L;
    
    jsonTreeWriter.value(value);
    
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("value", value);
    
    assertEquals(jsonObject, jsonTreeWriter.get());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueInvalidNumber() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    Number value = Double.NaN;
    
    jsonTreeWriter.value(value);
  }

  @Test
  public void testBeginArray() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("value1");
    jsonTreeWriter.value("value2");
    jsonTreeWriter.endArray();
    
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive("value1"));
    jsonArray.add(new JsonPrimitive("value2"));
    
    assertEquals(jsonArray, jsonTreeWriter.get());
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    jsonTreeWriter.value("value");
    jsonTreeWriter.endObject();
    
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("key", "value");
    
    assertEquals(jsonObject, jsonTreeWriter.get());
  }
}