
import org.junit.Assert;
import org.junit.Test;
import com.google.gson.JsonObject;

public class JsonTreeReaderTest {
  
  @Test
  public void testBeginArray() {
    JsonObject json = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(json);
    try {
      reader.beginArray();
      Assert.assertEquals("Expected BEGIN_ARRAY token", JsonToken.BEGIN_ARRAY, reader.peek());
    } catch (IOException e) {
      Assert.fail("Unexpected exception thrown");
    }
  }

  @Test
  public void testEndArray() {
    JsonObject json = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(json);
    try {
      reader.endArray();
      Assert.assertEquals("Expected END_ARRAY token", JsonToken.END_ARRAY, reader.peek());
    } catch (IOException e) {
      Assert.fail("Unexpected exception thrown");
    }
  }

  @Test
  public void testBeginObject() {
    JsonObject json = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(json);
    try {
      reader.beginObject();
      Assert.assertEquals("Expected BEGIN_OBJECT token", JsonToken.BEGIN_OBJECT, reader.peek());
    } catch (IOException e) {
      Assert.fail("Unexpected exception thrown");
    }
  }

  @Test
  public void testEndObject() {
    JsonObject json = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(json);
    try {
      reader.endObject();
      Assert.assertEquals("Expected END_OBJECT token", JsonToken.END_OBJECT, reader.peek());
    } catch (IOException e) {
      Assert.fail("Unexpected exception thrown");
    }
  }

  @Test
  public void testHasNext() {
    JsonObject json = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(json);
    try {
      boolean hasNext = reader.hasNext();
      Assert.assertFalse("Expected false", hasNext);
    } catch (IOException e) {
      Assert.fail("Unexpected exception thrown");
    }
  }

  @Test
  public void testNextName() {
    JsonObject json = new JsonObject();
    json.addProperty("key", "value");
    JsonTreeReader reader = new JsonTreeReader(json);
    try {
      String name = reader.nextName();
      Assert.assertEquals("Expected 'key'", "key", name);
    } catch (IOException e) {
      Assert.fail("Unexpected exception thrown");
    }
  }

  @Test
  public void testNextString() {
    JsonObject json = new JsonObject();
    json.addProperty("key", "value");
    JsonTreeReader reader = new JsonTreeReader(json);
    try {
      reader.nextName();
      String value = reader.nextString();
      Assert.assertEquals("Expected 'value'", "value", value);
    } catch (IOException e) {
      Assert.fail("Unexpected exception thrown");
    }
  }

  @Test
  public void testNextBoolean() {
    JsonObject json = new JsonObject();
    json.addProperty("key", true);
    JsonTreeReader reader = new JsonTreeReader(json);
    try {
      reader.nextName();
      boolean value = reader.nextBoolean();
      Assert.assertTrue("Expected true", value);
    } catch (IOException e) {
      Assert.fail("Unexpected exception thrown");
    }
  }

  @Test
  public void testNextNull() {
    JsonObject json = new JsonObject();
    json.addNull("key");
    JsonTreeReader reader = new JsonTreeReader(json);
    try {
      reader.nextName();
      reader.nextNull();
      Assert.assertEquals("Expected NULL token", JsonToken.NULL, reader.peek());
    } catch (IOException e) {
      Assert.fail("Unexpected exception thrown");
    }
  }

  @Test
  public void testNextDouble() {
    JsonObject json = new JsonObject();
    json.addProperty("key", 1.23);
    JsonTreeReader reader = new JsonTreeReader(json);
    try {
      reader.nextName();
      double value = reader.nextDouble();
      Assert.assertEquals("Expected 1.23", 1.23, value, 0.001);
    } catch (IOException e) {
      Assert.fail("Unexpected exception thrown");
    }
  }

  @Test
  public void testNextLong() {
    JsonObject json = new JsonObject();
    json.addProperty("key", 123L);
    JsonTreeReader reader = new JsonTreeReader(json);
    try {
      reader.nextName();
      long value = reader.nextLong();
      Assert.assertEquals("Expected 123", 123L, value);
    } catch (IOException e) {
      Assert.fail("Unexpected exception thrown");
    }
  }

  @Test
  public void testNextInt() {
    JsonObject json = new JsonObject();
    json.addProperty("key", 123);
    JsonTreeReader reader = new JsonTreeReader(json);
    try {
      reader.nextName();
      int value = reader.nextInt();
      Assert.assertEquals("Expected 123", 123, value);
    } catch (IOException e) {
      Assert.fail("Unexpected exception thrown");
    }
  }

  @Test
  public void testClose() {
    JsonObject json = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(json);
    try {
      reader.close();
      Assert.assertEquals("Expected END_DOCUMENT token", JsonToken.END_DOCUMENT, reader.peek());
    } catch (IOException e) {
      Assert.fail("Unexpected exception thrown");
    }
  }

  @Test
  public void testSkipValue() {
    JsonObject json = new JsonObject();
    json.addProperty("key", 123);
    JsonTreeReader reader = new JsonTreeReader(json);
    try {
      reader.skipValue();
      Assert.assertEquals("Expected NULL token", JsonToken.NULL, reader.peek());
    } catch (IOException e) {
      Assert.fail("Unexpected exception thrown");
    }
  }

  @Test
  public void testPromoteNameToValue() {
    JsonObject json = new JsonObject();
    json.addProperty("key", "value");
    JsonTreeReader reader = new JsonTreeReader(json);
    try {
      reader.nextName();
      reader.promoteNameToValue();
      Assert.assertEquals("Expected STRING token", JsonToken.STRING, reader.peek());
      String name = reader.nextString();
      Assert.assertEquals("Expected 'key'", "key", name);
    } catch (IOException e) {
      Assert.fail("Unexpected exception thrown");
    }
  }

  @Test
  public void testGetPath() {
    JsonObject json = new JsonObject();
    json.addProperty("key1", "value1");
    JsonTreeReader reader = new JsonTreeReader(json);
    try {
      String path = reader.getPath();
      Assert.assertEquals("Expected '$'", "$", path);
      reader.beginObject();
      path = reader.getPath();
      Assert.assertEquals("Expected '$.'", "$.", path);
      reader.nextName();
      path = reader.getPath();
      Assert.assertEquals("Expected '$.key1'", "$.key1", path);
    } catch (IOException e) {
      Assert.fail("Unexpected exception thrown");
    }
  }
  
}
