import static org.junit.Assert.*;
import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testBeginArray() {
    JsonElement element = new JsonArray();
    JsonTreeReader reader = new JsonTreeReader(element);

    try {
      reader.beginArray();
      assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testEndArray() {
    JsonElement element = new JsonArray();
    JsonTreeReader reader = new JsonTreeReader(element);

    try {
      reader.endArray();
      assertEquals(JsonToken.END_ARRAY, reader.peek());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testBeginObject() {
    JsonElement element = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(element);

    try {
      reader.beginObject();
      assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testEndObject() {
    JsonElement element = new JsonObject();
    JsonTreeReader reader = new JsonTreeReader(element);

    try {
      reader.endObject();
      assertEquals(JsonToken.END_OBJECT, reader.peek());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testHasNext() {
    JsonElement element = new JsonArray();
    JsonTreeReader reader = new JsonTreeReader(element);

    try {
      assertFalse(reader.hasNext());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testPeek() {
    JsonElement element = new JsonPrimitive("test");
    JsonTreeReader reader = new JsonTreeReader(element);

    try {
      assertEquals(JsonToken.STRING, reader.peek());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testNextName() {
    JsonObject object = new JsonObject();
    object.addProperty("test", "value");
    JsonTreeReader reader = new JsonTreeReader(object);

    try {
      reader.beginObject();
      String name = reader.nextName();
      assertEquals("test", name);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testNextString() {
    JsonElement element = new JsonPrimitive("test");
    JsonTreeReader reader = new JsonTreeReader(element);

    try {
      assertEquals("test", reader.nextString());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testNextBoolean() {
    JsonElement element = new JsonPrimitive(true);
    JsonTreeReader reader = new JsonTreeReader(element);

    try {
      assertTrue(reader.nextBoolean());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testNextNull() {
    JsonElement element = JsonNull.INSTANCE;
    JsonTreeReader reader = new JsonTreeReader(element);

    try {
      reader.nextNull();
      assertEquals(JsonToken.NULL, reader.peek());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testNextDouble() {
    JsonElement element = new JsonPrimitive(3.14);
    JsonTreeReader reader = new JsonTreeReader(element);

    try {
      assertEquals(3.14, reader.nextDouble(), 0.001);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testNextLong() {
    JsonElement element = new JsonPrimitive(100000000000L);
    JsonTreeReader reader = new JsonTreeReader(element);

    try {
      assertEquals(100000000000L, reader.nextLong());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testNextInt() {
    JsonElement element = new JsonPrimitive(42);
    JsonTreeReader reader = new JsonTreeReader(element);

    try {
      assertEquals(42, reader.nextInt());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testClose() {
    JsonElement element = new JsonPrimitive("test");
    JsonTreeReader reader = new JsonTreeReader(element);

    try {
      reader.close();
      assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testSkipValue() {
    JsonObject object = new JsonObject();
    object.addProperty("test", "value");
    JsonTreeReader reader = new JsonTreeReader(object);

    try {
      reader.beginObject();
      reader.skipValue();
      assertEquals(JsonToken.END_OBJECT, reader.peek());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
}