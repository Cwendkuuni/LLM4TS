import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.junit.Before;
import org.junit.Test;

public class JsonTreeWriterTest {

  private JsonTreeWriter jsonWriter;
  private JsonObject jsonObject;
  
  @Before
  public void setUp() {
    jsonWriter = new JsonTreeWriter();
    jsonObject = new JsonObject();
  }

  // Test for beginArray method
  @Test
  public void testBeginArray() {
    try {
      jsonWriter.beginArray();
      JsonElement result = jsonWriter.get();
      assertNotNull(result);
      assertTrue(result instanceof JsonArray);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  // Test for endArray method
  @Test
  public void testEndArray() {
    try {
      jsonWriter.beginArray();
      jsonWriter.endArray();
      JsonElement result = jsonWriter.get();
      assertEquals(JsonNull.INSTANCE, result);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  // Test for beginObject method
  @Test
  public void testBeginObject() {
    try {
      jsonWriter.beginObject();
      JsonElement result = jsonWriter.get();
      assertNotNull(result);
      assertTrue(result instanceof JsonObject);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  // Test for endObject method
  @Test
  public void testEndObject() {
    try {
      jsonWriter.beginObject();
      jsonWriter.endObject();
      JsonElement result = jsonWriter.get();
      assertEquals(JsonNull.INSTANCE, result);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  // Test for name method
  @Test
  public void testName() {
    try {
      jsonWriter.beginObject();
      jsonWriter.name("key");
      JsonElement result = jsonWriter.get();
      assertTrue(result instanceof JsonObject);
      JsonObject object = (JsonObject) result;
      assertTrue(object.has("key"));
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  // Test for value method (String)
  @Test
  public void testValue_String() {
    try {
      jsonWriter.value("value");
      JsonElement result = jsonWriter.get();
      assertEquals(new JsonPrimitive("value"), result);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  // Test for nullValue method
  @Test
  public void testNullValue() {
    try {
      jsonWriter.nullValue();
      JsonElement result = jsonWriter.get();
      assertEquals(JsonNull.INSTANCE, result);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  // Test for value method (boolean)
  @Test
  public void testValue_Boolean() {
    try {
      jsonWriter.value(true);
      JsonElement result = jsonWriter.get();
      assertEquals(new JsonPrimitive(true), result);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  // Test for value method (double)
  @Test
  public void testValue_Double() {
    try {
      jsonWriter.value(3.14);
      JsonElement result = jsonWriter.get();
      assertEquals(new JsonPrimitive(3.14), result);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  // Test for value method (long)
  @Test
  public void testValue_Long() {
    try {
      jsonWriter.value(12345L);
      JsonElement result = jsonWriter.get();
      assertEquals(new JsonPrimitive(12345L), result);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  // Test for value method (Number)
  @Test
  public void testValue_Number() {
    try {
      jsonWriter.value(3.14);
      JsonElement result = jsonWriter.get();
      assertEquals(new JsonPrimitive(3.14), result);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
  
  // Test for illegal argument exception in value method (double)
  @Test(expected = IllegalArgumentException.class)
  public void testValue_Double_IllegalArgument() {
    jsonWriter.setLenient(false);
    jsonWriter.value(Double.POSITIVE_INFINITY);
  }
  
  // Test for illegal argument exception in value method (Number)
  @Test(expected = IllegalArgumentException.class)
  public void testValue_Number_IllegalArgument() {
    jsonWriter.setLenient(false);
    jsonWriter.value(Double.NEGATIVE_INFINITY);
  }

}