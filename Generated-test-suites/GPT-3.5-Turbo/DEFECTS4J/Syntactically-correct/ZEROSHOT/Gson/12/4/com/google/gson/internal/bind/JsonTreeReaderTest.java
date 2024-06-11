import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

public class JsonTreeReaderTest {

  @Test
  public void testBeginArray() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive("value1"));
    jsonArray.add(new JsonPrimitive("value2"));
    
    JsonTreeReader reader = new JsonTreeReader(jsonArray);
    try {
      reader.beginArray();
      assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
  
  @Test
  public void testEndArray() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive("value1"));
    jsonArray.add(new JsonPrimitive("value2"));
    
    JsonTreeReader reader = new JsonTreeReader(jsonArray);
    try {
      reader.endArray();
      assertEquals(JsonToken.END_ARRAY, reader.peek());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
  
  @Test
  public void testBeginObject() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name1", "value1");
    jsonObject.addProperty("name2", "value2");
    
    JsonTreeReader reader = new JsonTreeReader(jsonObject);
    try {
      reader.beginObject();
      assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
  
  @Test
  public void testEndObject() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name1", "value1");
    jsonObject.addProperty("name2", "value2");
    
    JsonTreeReader reader = new JsonTreeReader(jsonObject);
    try {
      reader.endObject();
      assertEquals(JsonToken.END_OBJECT, reader.peek());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
  
  @Test
  public void testHasNext() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name1", "value1");
    jsonObject.addProperty("name2", "value2");
    
    JsonTreeReader reader = new JsonTreeReader(jsonObject);
    try {
      assertTrue(reader.hasNext());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
  
  @Test
  public void testPeek() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive("value1"));
    
    JsonTreeReader reader = new JsonTreeReader(jsonArray);
    try {
      assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
  
  @Test
  public void testNextName() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name1", "value1");
    jsonObject.addProperty("name2", "value2");
    
    JsonTreeReader reader = new JsonTreeReader(jsonObject);
    try {
      assertEquals("name1", reader.nextName());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
  
  @Test
  public void testNextString() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("value1");
    
    JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);
    try {
      assertEquals("value1", reader.nextString());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
  
  @Test
  public void testNextBoolean() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(true);
    
    JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);
    try {
      assertTrue(reader.nextBoolean());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
  
  @Test
  public void testNextNull() {
    JsonNull jsonNull = JsonNull.INSTANCE;
    
    JsonTreeReader reader = new JsonTreeReader(jsonNull);
    try {
      reader.nextNull();
      assertEquals(JsonToken.NULL, reader.peek());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
  
  @Test
  public void testNextDouble() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(10.5);
    
    JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);
    try {
      double expected = 10.5;
      double actual = reader.nextDouble();
      assertEquals(expected, actual, 0.01);
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
  
  @Test
  public void testNextLong() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(10L);
    
    JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);
    try {
      long expected = 10L;
      long actual = reader.nextLong();
      assertEquals(expected, actual);
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
  
  @Test
  public void testNextInt() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(10);
    
    JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);
    try {
      int expected = 10;
      int actual = reader.nextInt();
      assertEquals(expected, actual);
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
  
  @Test
  public void testClose() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(10);
    
    JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);
    try {
      reader.close();
      assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
  
  @Test
  public void testSkipValue() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("value1");
    
    JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);
    try {
      reader.skipValue();
      assertEquals(JsonToken.NULL, reader.peek());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
  
  @Test
  public void testPromoteNameToValue() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", "value");
    
    JsonTreeReader reader = new JsonTreeReader(jsonObject);
    try {
      reader.promoteNameToValue();
      assertEquals(JsonToken.STRING, reader.peek());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
  
  @Test
  public void testGetPath() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", "value");
    
    JsonTreeReader reader = new JsonTreeReader(jsonObject);
    try {
      reader.beginObject();
      reader.nextName();
      String expected = "$.name";
      String actual = reader.getPath();
      assertEquals(expected, actual);
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

}