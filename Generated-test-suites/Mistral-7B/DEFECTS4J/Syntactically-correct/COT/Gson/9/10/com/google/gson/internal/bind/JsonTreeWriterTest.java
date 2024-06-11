import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;

public class JsonTreeWriterTest {

  private JsonTreeWriter writer;

  @Before
  public void setUp() {
    writer = new JsonTreeWriter();
  }

  // Test constructor
  @Test
  public void testJsonTreeWriterConstructor() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertNull(jsonTreeWriter.get());
  }

  // Test get method
  @Test
  public void testGetMethod() {
    writer.beginObject();
    writer.name("name");
    writer.value("value");
    JsonElement jsonElement = writer.get();
    assertNotNull(jsonElement);
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertSame("value", jsonObject.get("name").getAsString());
  }

  // Test beginArray method
  @Test
  public void testBeginArrayMethod() {
    writer.beginArray();
    JsonArray jsonArray = writer.get().getAsJsonArray();
    assertTrue(jsonArray.isEmpty());
  }

  // Test endArray method
  @Test
  public void testEndArrayMethod() {
    writer.beginArray();
    writer.endArray();
    JsonElement jsonElement = writer.get();
    assertSame(JsonArray.class, jsonElement.getClass());
    JsonArray jsonArray = jsonElement.getAsJsonArray();
    assertTrue(jsonArray.isEmpty());
  }

  // Test beginObject method
  @Test
  public void testBeginObjectMethod() {
    writer.beginObject();
    writer.name("name1");
    writer.value("value1");
    writer.name("name2");
    writer.value("value2");
    writer.endObject();
    JsonElement jsonElement = writer.get();
    assertSame(JsonObject.class, jsonElement.getClass());
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertEquals("value1", jsonObject.get("name1").getAsString());
    assertEquals("value2", jsonObject.get("name2").getAsString());
  }

  // Test endObject method
  @Test
  public void testEndObjectMethod() {
    writer.beginObject();
    writer.name("name");
    writer.value("value");
    writer.endObject();
    JsonElement jsonElement = writer.get();
    assertSame(JsonObject.class, jsonElement.getClass());
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertSame("value", jsonObject.get("name").getAsString());
  }

  // Test name method
  @Test
  public void testNameMethod() {
    writer.beginObject();
    writer.name("name");
    writer.endObject();
    JsonElement jsonElement = writer.get();
    assertSame(JsonObject.class, jsonElement.getClass());
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertNull(jsonObject.get("name"));
  }

  // Test value method with String
  @Test
  public void testValueStringMethod() {
    writer.beginObject();
    writer.name("name");
    writer.value("value");
    writer.endObject();
    JsonElement jsonElement = writer.get();
    assertSame(JsonObject.class, jsonElement.getClass());
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertEquals("value", jsonObject.get("name").getAsString());
  }

  // Test value method with boolean
  @Test
  public void testValueBooleanMethod() {
    writer.beginObject();
    writer.name("name");
    writer.value(true);
    writer.endObject();
    JsonElement jsonElement = writer.get();
    assertSame(JsonObject.class, jsonElement.getClass());
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertTrue(jsonObject.get("name").getAsBoolean());
  }

  // Test value method with double
  @Test(expected = IllegalArgumentException.class)
  public void testValueDoubleMethodWithNaN() {
    writer.beginObject();
    writer.name("name");
    writer.value(Double.NaN);
    writer.endObject();
    writer.get();
  }

  // Test value method with double
  @Test(expected = IllegalArgumentException.class)
  public void testValueDoubleMethodWithInfinite() {
    writer.beginObject();
    writer.name("name");
    writer.value(Double.POSITIVE_INFINITY);
    writer.endObject();
    writer.get();
  }

  // Test value method with double
  @Test
  public void testValueDoubleMethod() {
    writer.beginObject();
    writer.name("name");
    writer.value(1.2);
    writer.endObject();
    JsonElement jsonElement = writer.get();
    assertSame(JsonObject.class, jsonElement.getClass());
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertEquals(1.2, jsonObject.get("name").getAsDouble(), 0.0);
  }

  // Test value method with long
  @Test
  public void testValueLongMethod() {
    writer.beginObject();
    writer.name("name");
    writer.value(123L);
    writer.endObject();
    JsonElement jsonElement = writer.get();
    assertSame(JsonObject.class, jsonElement.getClass());
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertEquals(123L, jsonObject.get("name").getAsLong());
  }

  // Test value method with Number
  @Test
  public void testValueNumberMethod() {
    writer.beginObject();
    writer.name("name");
    writer.value(new Long(123));
    writer.endObject();
    JsonElement jsonElement = writer.get();
    assertSame(JsonObject.class, jsonElement.getClass());
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertEquals(123L, jsonObject.get("name").getAsLong());
  }

  // Test nullValue method
  @Test
  public void testNullValueMethod() {
    writer.beginObject();
    writer.name("name");
    writer.nullValue();
    writer.endObject();
    JsonElement jsonElement = writer.get();
    assertSame(JsonObject.class, jsonElement.getClass());
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    assertNull(jsonObject.get("name"));
  }

  // Test flush method
  @Test
  public void testFlushMethod() {
    writer.beginObject();
    writer.name("name");
    writer.value("value");
    writer.endObject();
    writer.flush();
  }

  // Test close method
  @Test(expected = IllegalStateException.class)
  public void testCloseMethodBeforeEndObject() {
    writer.beginObject();
    writer.close();
  }

  // Test close method
  @Test
  public void testCloseMethodAfterEndObject() {
    writer.beginObject();
    writer.endObject();
    writer.close();
  }

  // Test close method with non-empty stack
  @Test(expected = IOException.class)
  public void testCloseMethodWithNonEmptyStack() {
    writer.beginArray();
    writer.close();
  }
}