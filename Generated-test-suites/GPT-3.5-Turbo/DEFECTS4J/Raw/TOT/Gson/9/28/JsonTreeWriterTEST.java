import com.google.gson.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonTreeWriterTest {

  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    
    try {
      writer.beginArray();
      JsonObject obj = (JsonObject) writer.get();
      assertEquals(JsonArray.class, obj.getClass());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    
    try {
      writer.beginArray();
      writer.endArray();
      JsonObject obj = (JsonObject) writer.get();
      assertEquals(JsonNull.class, obj.getClass());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    
    try {
      writer.beginObject();
      JsonObject obj = (JsonObject) writer.get();
      assertEquals(JsonObject.class, obj.getClass());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    
    try {
      writer.beginObject();
      writer.endObject();
      JsonObject obj = (JsonObject) writer.get();
      assertEquals(JsonNull.class, obj.getClass());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    
    try {
      writer.beginObject();
      writer.name("test");
      JsonObject obj = (JsonObject) writer.get();
      assertNotNull(obj.get("test"));
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    
    try {
      writer.value("test");
      JsonObject obj = (JsonObject) writer.get();
      assertEquals(JsonPrimitive.class, obj.getClass());
      assertEquals("test", obj.getAsJsonPrimitive().getAsString());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testNullValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    
    try {
      writer.nullValue();
      JsonObject obj = (JsonObject) writer.get();
      assertEquals(JsonNull.class, obj.getClass());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testValueBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    
    try {
      writer.value(true);
      JsonObject obj = (JsonObject) writer.get();
      assertEquals(JsonPrimitive.class, obj.getClass());
      assertEquals(true, obj.getAsJsonPrimitive().getAsBoolean());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testValueDouble() {
    JsonTreeWriter writer = new JsonTreeWriter();
    
    try {
      writer.value(3.14);
      JsonObject obj = (JsonObject) writer.get();
      assertEquals(JsonPrimitive.class, obj.getClass());
      assertEquals(3.14, obj.getAsJsonPrimitive().getAsDouble(), 0.01);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testValueLong() {
    JsonTreeWriter writer = new JsonTreeWriter();
    
    try {
      writer.value(10L);
      JsonObject obj = (JsonObject) writer.get();
      assertEquals(JsonPrimitive.class, obj.getClass());
      assertEquals(10L, obj.getAsJsonPrimitive().getAsLong());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testValueNumber() {
    JsonTreeWriter writer = new JsonTreeWriter();
    
    try {
      writer.value(5);
      JsonObject obj = (JsonObject) writer.get();
      assertEquals(JsonPrimitive.class, obj.getClass());
      assertEquals(5, obj.getAsJsonPrimitive().getAsInt());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
}