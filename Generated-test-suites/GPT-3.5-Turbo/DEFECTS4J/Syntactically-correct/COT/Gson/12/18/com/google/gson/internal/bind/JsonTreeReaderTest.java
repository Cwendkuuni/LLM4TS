import org.junit.Assert;
import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testBeginArray() {
    JsonArray array = new JsonArray();
    JsonTreeReader reader = new JsonTreeReader(array);
    try {
      reader.beginArray();
      Assert.assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    } catch (IOException e) {
      Assert.fail(e.getMessage());
    }
  }
  
  @Test
  public void testEndArray() {
    JsonArray array = new JsonArray();
    JsonTreeReader reader = new JsonTreeReader(array);
    try {
      reader.endArray();
      Assert.assertEquals(JsonToken.END_ARRAY, reader.peek());
    } catch (IOException e) {
      Assert.fail(e.getMessage());
    }
  }
  
  @Test
  public void testBeginObject() {
    JsonObject object = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(object);
    try {
      reader.beginObject();
      Assert.assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    } catch (IOException e) {
      Assert.fail(e.getMessage());
    }
  }
  
  @Test
  public void testEndObject() {
    JsonObject object = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(object);
    try {
      reader.endObject();
      Assert.assertEquals(JsonToken.END_OBJECT, reader.peek());
    } catch (IOException e) {
      Assert.fail(e.getMessage());
    }
  }
  
  @Test
  public void testHasNext() {
    JsonObject object = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(object);
    try {
      Assert.assertFalse(reader.hasNext());
    } catch (IOException e) {
      Assert.fail(e.getMessage());
    }
  }
  
  @Test
  public void testPeek() {
    JsonObject object = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(object);
    try {
      Assert.assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    } catch (IOException e) {
      Assert.fail(e.getMessage());
    }
  }
  
  @Test
  public void testNextName() {
    JsonObject object = new JsonObject();
    object.addProperty("name", "John");
    JsonTreeReader reader = new JsonTreeReader(object);
    try {
      String name = reader.nextName();
      Assert.assertEquals("name", name);
    } catch (IOException e) {
      Assert.fail(e.getMessage());
    }
  }
  
  @Test
  public void testNextString() {
    JsonPrimitive primitive = new JsonPrimitive("Hello");
    JsonTreeReader reader = new JsonTreeReader(primitive);
    try {
      String value = reader.nextString();
      Assert.assertEquals("Hello", value);
    } catch (IOException e) {
      Assert.fail(e.getMessage());
    }
  }
  
  @Test
  public void testNextBoolean() {
    JsonPrimitive primitive = new JsonPrimitive(true);
    JsonTreeReader reader = new JsonTreeReader(primitive);
    try {
      boolean value = reader.nextBoolean();
      Assert.assertEquals(true, value);
    } catch (IOException e) {
      Assert.fail(e.getMessage());
    }
  }
  
  @Test
  public void testNextNull() {
    JsonNull nullValue = JsonNull.INSTANCE;
    JsonTreeReader reader = new JsonTreeReader(nullValue);
    try {
      reader.nextNull();
      Assert.assertEquals(JsonToken.NULL, reader.peek());
    } catch (IOException e) {
      Assert.fail(e.getMessage());
    }
  }
  
  @Test
  public void testNextDouble() {
    JsonPrimitive primitive = new JsonPrimitive(3.14);
    JsonTreeReader reader = new JsonTreeReader(primitive);
    try {
      double value = reader.nextDouble();
      Assert.assertEquals(3.14, value, 0.001);
    } catch (IOException e) {
      Assert.fail(e.getMessage());
    }
  }
  
  @Test
  public void testNextLong() {
    JsonPrimitive primitive = new JsonPrimitive(100L);
    JsonTreeReader reader = new JsonTreeReader(primitive);
    try {
      long value = reader.nextLong();
      Assert.assertEquals(100L, value);
    } catch (IOException e) {
      Assert.fail(e.getMessage());
    }
  }
  
  @Test
  public void testNextInt() {
    JsonPrimitive primitive = new JsonPrimitive(42);
    JsonTreeReader reader = new JsonTreeReader(primitive);
    try {
      int value = reader.nextInt();
      Assert.assertEquals(42, value);
    } catch (IOException e) {
      Assert.fail(e.getMessage());
    }
  }
  
  @Test
  public void testClose() {
    JsonArray array = new JsonArray();
    JsonTreeReader reader = new JsonTreeReader(array);
    try {
      reader.close();
      Assert.assertEquals("JsonReader is closed", reader.peek());
    } catch (IOException e) {
      Assert.fail(e.getMessage());
    }
  }
  
  @Test
  public void testSkipValue() {
    JsonObject object = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(object);
    try {
      reader.skipValue();
      Assert.assertEquals(JsonToken.NAME, reader.peek());
    } catch (IOException e) {
      Assert.fail(e.getMessage());
    }
  }
  
  @Test
  public void testPromoteNameToValue() {
    JsonObject object = new JsonObject();
    object.addProperty("name", "John");
    JsonTreeReader reader = new JsonTreeReader(object);
    try {
      reader.promoteNameToValue();
      Assert.assertEquals(JsonToken.STRING, reader.peek());
    } catch (IOException e) {
      Assert.fail(e.getMessage());
    }
  }
  
  @Test
  public void testGetPath() {
    JsonObject object = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(object);
    try {
      String path = reader.getPath();
      Assert.assertEquals("$", path);
    } catch (IOException e) {
      Assert.fail(e.getMessage());
    }
  }
  
  @Test
  public void testToString() {
    JsonObject object = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(object);
    String className = reader.toString();
    Assert.assertEquals("JsonTreeReader", className);
  }
}