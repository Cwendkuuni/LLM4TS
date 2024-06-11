import com.google.gson.*;
import org.junit.Assert.*;
import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testEmptyJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
    Assert.assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testSimpleJsonObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{" +
        "\"name\": \"John\"," +
        "\"age\": 30," +
        "\"city\": \"New York\"" +
        "}"));

    Assert.assertEquals("name", reader.nextName());
    reader.nextString();
    Assert.assertEquals("John", reader.nextString());

    Assert.assertEquals("age", reader.nextName());
    reader.nextInt();
    Assert.assertEquals(30, reader.nextInt());

    Assert.assertEquals("city", reader.nextName());
    reader.nextString();
    Assert.assertEquals("New York", reader.nextString());

    reader.nextNull();
    reader.nextNull(); // empty object
    reader.popStack();
    reader.popStack();
  }

  @Test
  public void testSimpleJsonArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[1, 2, 3, 4]"));

    Assert.assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getAsInt());
    reader.beginArray();

    reader.nextInt();
    Assert.assertEquals(1, reader.nextInt());

    reader.nextInt();
    Assert.assertEquals(2, reader.nextInt());

    reader.nextInt();
    Assert.assertEquals(3, reader.nextInt());

    reader.nextInt();
    Assert.assertEquals(4, reader.nextInt());

    reader.endArray();
    reader.nextNull(); // empty array
    reader.popStack();
    reader.popStack();
  }

  @Test(expected = IllegalStateException.class)
  public void testClosedJsonReader() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
    reader.close();
    reader.nextName();
  }

  @Test
  public void testJsonWithInvalidType() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{ \"a\": 1 }"));
    reader.promoteNameToValue();

    try {
      reader.nextBoolean();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }

    reader.nextInt();
    Assert.assertEquals(1, reader.nextInt());
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonWithInvalidArrayIndex() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[1, 2, 3]"));
    reader.beginArray();
    reader.nextInt();
    reader.nextInt();
    reader.nextInt();

    try {
      reader.nextInt(); // Array index out of bounds
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }

    reader.endArray();
    reader.nextNull(); // empty array
    reader.popStack();
    reader.popStack();
  }

  @Test
  public void testJsonWithEmptyArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[]"));
    reader.beginArray();
    reader.endArray();
    reader.nextNull(); // empty array
    reader.popStack();
    reader.popStack();
  }

  @Test
  public void testJsonWithEmptyObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
    reader.beginObject();
    reader.endObject();
    reader.nextNull(); // empty object
    reader.popStack();
    reader.popStack();
  }
}