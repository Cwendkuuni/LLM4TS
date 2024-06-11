
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;


public class JsonTreeReaderTest {
  
  @Test
  public void testBeginArray() throws IOException {
    JsonElement element = new JsonArray();
    JsonTreeReader reader = new JsonTreeReader(element);
    reader.beginArray();
    
    Assert.assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
  }
  
  @Test
  public void testEndArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonArray());
    reader.endArray();
    
    Assert.assertEquals(JsonToken.END_ARRAY, reader.peek());
  }
  
  @Test
  public void testBeginObject() throws IOException {
    JsonElement element = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(element);
    reader.beginObject();
    
    Assert.assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
  }
  
  @Test
  public void testEndObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.endObject();
    
    Assert.assertEquals(JsonToken.END_OBJECT, reader.peek());
  }
  
  @Test
  public void testHasNext() throws IOException {
    JsonElement element = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(element);
    
    Assert.assertFalse(reader.hasNext());
  }
  
  @Test
  public void testPeek() throws IOException {
    JsonElement element = new JsonArray();
    JsonTreeReader reader = new JsonTreeReader(element);
    
    Assert.assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }
  
  @Test
  public void testNextName() throws IOException {
    JsonObject object = new JsonObject();
    object.addProperty("name", "John");
    JsonTreeReader reader = new JsonTreeReader(object);
    String name = reader.nextName();
    
    Assert.assertEquals("name", name);
    Assert.assertEquals(JsonToken.STRING, reader.peek());
  }
  
  @Test
  public void testNextString() throws IOException {
    JsonPrimitive primitive = new JsonPrimitive("Hello");
    JsonTreeReader reader = new JsonTreeReader(primitive);
    String value = reader.nextString();
    
    Assert.assertEquals("Hello", value);
    Assert.assertEquals(JsonToken.NULL, reader.peek());
  }
  
  @Test
  public void testNextBoolean() throws IOException {
    JsonPrimitive primitive = new JsonPrimitive(true);
    JsonTreeReader reader = new JsonTreeReader(primitive);
    boolean value = reader.nextBoolean();
    
    Assert.assertTrue(value);
    Assert.assertEquals(JsonToken.NULL, reader.peek());
  }
  
  @Test
  public void testNextNull() throws IOException {
    JsonNull jsonNull = JsonNull.INSTANCE;
    JsonTreeReader reader = new JsonTreeReader(jsonNull);
    reader.nextNull();
    
    Assert.assertEquals(JsonToken.NULL, reader.peek());
  }
  
  @Test
  public void testNextDouble() throws IOException {
    JsonPrimitive primitive = new JsonPrimitive(3.14);
    JsonTreeReader reader = new JsonTreeReader(primitive);
    double value = reader.nextDouble();
    
    Assert.assertEquals(3.14, value, 0.001);
    Assert.assertEquals(JsonToken.NULL, reader.peek());
  }
  
  @Test
  public void testNextLong() throws IOException {
    JsonPrimitive primitive = new JsonPrimitive(42L);
    JsonTreeReader reader = new JsonTreeReader(primitive);
    long value = reader.nextLong();
    
    Assert.assertEquals(42L, value);
    Assert.assertEquals(JsonToken.NULL, reader.peek());
  }
  
  @Test
  public void testNextInt() throws IOException {
    JsonPrimitive primitive = new JsonPrimitive(42);
    JsonTreeReader reader = new JsonTreeReader(primitive);
    int value = reader.nextInt();
    
    Assert.assertEquals(42, value);
    Assert.assertEquals(JsonToken.NULL, reader.peek());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testClose() throws IOException {
    JsonPrimitive primitive = new JsonPrimitive(42);
    JsonTreeReader reader = new JsonTreeReader(primitive);
    reader.close();
    
    reader.peek();
  }
  
  @Test
  public void testSkipValue() throws IOException {
    JsonPrimitive primitive = new JsonPrimitive("Hello");
    JsonTreeReader reader = new JsonTreeReader(primitive);
    reader.skipValue();
    
    Assert.assertEquals(JsonToken.END_DOCUMENT, reader.peek());
  }
  
  @Test
  public void testToString() {
    JsonElement element = new JsonArray();
    JsonTreeReader reader = new JsonTreeReader(element);
    
    Assert.assertEquals("JsonTreeReader", reader.toString());
  }
  
  @Test
  public void testPromoteNameToValue() throws IOException {
    JsonObject object = new JsonObject();
    object.addProperty("name", "John");
    
    JsonTreeReader reader = new JsonTreeReader(object);
    reader.beginObject();
    reader.promoteNameToValue();
    
    Assert.assertEquals(JsonToken.STRING, reader.peek());
  }
  
  @Test
  public void testGetPath() throws IOException {
    JsonObject object = new JsonObject();
    object.addProperty("name", "John");
    
    JsonTreeReader reader = new JsonTreeReader(object);
    reader.beginObject();
    reader.nextName();
    String path = reader.getPath();
    
    Assert.assertEquals("$.name", path);
  }
}

