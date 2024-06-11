import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JsonTreeWriterTest {

  //Test case for beginArray()
  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.beginArray();
      Assert.assertEquals(JsonArray.class, writer.get().getClass());
    } catch (Exception e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }
  
  //Test case for endArray()
  @Test
  public void testEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.beginArray();
      writer.endArray();
      Assert.assertEquals(JsonNull.INSTANCE, writer.get());
    } catch (Exception e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }
  
  //Test case for beginObject()
  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.beginObject();
      Assert.assertEquals(JsonObject.class, writer.get().getClass());
    } catch (Exception e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }
  
  //Test case for endObject()
  @Test
  public void testEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.beginObject();
      writer.endObject();
      Assert.assertEquals(JsonNull.INSTANCE, writer.get());
    } catch (Exception e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }
  
  //Test case for name(String name)
  @Test
  public void testName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.beginObject();
      writer.name("key");
      writer.value("value");
      writer.endObject();
      
      JsonObject jsonObject = (JsonObject) writer.get();
      Assert.assertTrue(jsonObject.has("key"));
      Assert.assertEquals("value", jsonObject.get("key").getAsString());
    } catch (Exception e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }
  
  //Test case for value(String value)
  @Test
  public void testValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.value("value");
      Assert.assertEquals(JsonPrimitive.class, writer.get().getClass());
      Assert.assertEquals("value", writer.get().getAsString());
    } catch (Exception e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }
  
  //Test case for nullValue()
  @Test
  public void testNullValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.nullValue();
      Assert.assertEquals(JsonNull.INSTANCE, writer.get());
    } catch (Exception e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }
  
  //Test case for value(boolean value)
  @Test
  public void testValueBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.value(true);
      Assert.assertEquals(JsonPrimitive.class, writer.get().getClass());
      Assert.assertEquals(true, writer.get().getAsBoolean());
    } catch (Exception e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }
  
  //Test case for value(double value)
  @Test
  public void testValueDouble() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.value(3.14);
      Assert.assertEquals(JsonPrimitive.class, writer.get().getClass());
      Assert.assertEquals(3.14, writer.get().getAsDouble(), 0.001);
    } catch (Exception e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }
  
  //Test case for value(long value)
  @Test
  public void testValueLong() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.value(1000L);
      Assert.assertEquals(JsonPrimitive.class, writer.get().getClass());
      Assert.assertEquals(1000L, writer.get().getAsLong());
    } catch (Exception e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }
  
  //Test case for value(Number value)
  @Test
  public void testValueNumber() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.value(3.14);
      writer.value(1000L);
      writer.value(null);
      writer.value(Double.POSITIVE_INFINITY);
      writer.value(Double.NEGATIVE_INFINITY);
      JsonObject jsonObject = (JsonObject) writer.get();
      
      List<JsonPrimitive> expectedPrimitives = Arrays.asList(new JsonPrimitive(3.14),
          new JsonPrimitive(1000L), JsonNull.INSTANCE, new JsonPrimitive(Double.POSITIVE_INFINITY),
          new JsonPrimitive(Double.NEGATIVE_INFINITY));
      List<JsonPrimitive> actualPrimitives = Arrays.asList(jsonObject.get("value1").getAsJsonPrimitive(),
          jsonObject.get("value2").getAsJsonPrimitive(),
          jsonObject.get("value3").getAsJsonPrimitive(),
          jsonObject.get("value4").getAsJsonPrimitive(),
          jsonObject.get("value5").getAsJsonPrimitive());
      Assert.assertEquals(expectedPrimitives, actualPrimitives);
    } catch (Exception e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }
  
  //Test case for flush(), no specific tests required as the method is empty.
  
  //Test case for close()
  @Test
  public void testClose() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.close();
      Assert.assertEquals(JsonPrimitive.class, writer.get().getClass());
      Assert.assertEquals("closed", writer.get().getAsString());
    } catch (Exception e) {
      Assert.fail("Unexpected exception: " + e.getMessage());
    }
  }
}