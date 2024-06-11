import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class JsonTreeWriterTest {
  
  private JsonTreeWriter jsonTreeWriter;
  
  @Before
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }
  
  @Test
  public void testBeginArray() {
    jsonTreeWriter.beginArray();
    JsonElement result = jsonTreeWriter.get();
    Assert.assertTrue(result instanceof JsonArray);
    Assert.assertEquals(1, ((JsonArray) result).size());
  }
  
  @Test
  public void testEndArray() {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
    JsonElement result = jsonTreeWriter.get();
    Assert.assertTrue(result instanceof JsonNull);
  }
  
  @Test
  public void testBeginObject() {
    jsonTreeWriter.beginObject();
    JsonElement result = jsonTreeWriter.get();
    Assert.assertTrue(result instanceof JsonObject);
    Assert.assertEquals(1, ((JsonObject) result).size());
  }
  
  @Test
  public void testEndObject() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    JsonElement result = jsonTreeWriter.get();
    Assert.assertTrue(result instanceof JsonNull);
  }
  
  @Test
  public void testName() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("testName");
    JsonElement result = jsonTreeWriter.get();
    Assert.assertTrue(result instanceof JsonObject);
    Assert.assertEquals("testName", ((JsonObject) result).get("testName").getAsJsonPrimitive().getAsString());
  }
  
  @Test
  public void testValueString() {
    jsonTreeWriter.value("testValue");
    JsonElement result = jsonTreeWriter.get();
    Assert.assertEquals("testValue", result.getAsString());
  }
  
  @Test
  public void testNullValue() {
    jsonTreeWriter.nullValue();
    JsonElement result = jsonTreeWriter.get();
    Assert.assertTrue(result instanceof JsonNull);
  }
  
  @Test
  public void testValueBoolean() {
    jsonTreeWriter.value(true);
    JsonElement result = jsonTreeWriter.get();
    Assert.assertEquals(true, result.getAsBoolean());
  }
  
  @Test
  public void testValueDouble() {
    jsonTreeWriter.value(10.5);
    JsonElement result = jsonTreeWriter.get();
    Assert.assertEquals(10.5, result.getAsDouble(), 0.01);
  }
  
  @Test
  public void testValueLong() {
    jsonTreeWriter.value(100);
    JsonElement result = jsonTreeWriter.get();
    Assert.assertEquals(100, result.getAsLong());
  }
  
  @Test
  public void testValueNumber() {
    jsonTreeWriter.value(15.8);
    JsonElement result = jsonTreeWriter.get();
    Assert.assertEquals(15.8, result.getAsDouble(), 0.01);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testInvalidEndArray() {
    jsonTreeWriter.endArray();
  }
  
  @Test(expected = IllegalStateException.class)
  public void testInvalidEndObject() {
    jsonTreeWriter.endObject();
  }
  
  @Test(expected = IllegalStateException.class)
  public void testInvalidName() {
    jsonTreeWriter.name("testName");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidValueDouble() {
    jsonTreeWriter.value(Double.POSITIVE_INFINITY);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidValueNumber() {
    jsonTreeWriter.value(Double.NaN);
  }
}