import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;

public class JsonTreeReaderTest {

  @Test
  public void testEmptyJsonObject() throws IOException {
    String json = "{}";
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader(new ByteArrayInputStream(json.getBytes())));
    reader.beginObject();
    assertEquals(JsonToken.END_OBJECT, reader.peek());
    reader.endObject();
  }

  @Test
  public void testEmptyJsonArray() throws IOException {
    String json = "[]";
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader(new ByteArrayInputStream(json.getBytes())));
    reader.beginArray();
    assertEquals(JsonToken.END_ARRAY, reader.peek());
    reader.endArray();
  }

  @Test
  public void testJsonArrayWithOneElement() throws IOException {
    String json = "[\"Hello World\"]";
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader(new ByteArrayInputStream(json.getBytes())));
    reader.beginArray();
    String actual = reader.nextString();
    assertEquals("Hello World", actual);
    reader.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonArrayWithMultipleElementsButReaderNotAtBeginningOfArray() throws IOException {
    String json = "[\"Hello World\", 42]";
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader(new ByteArrayInputStream(json.getBytes())));
    reader.nextInt(); // read '42'
    reader.beginArray();
    // This should throw an IllegalStateException as 'reader' is not at the beginning of the array
    reader.nextString();
  }

  @Test
  public void testJsonArrayWithMultipleElements() throws IOException {
    String json = "[\"Hello World\", 42, true]";
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader(new ByteArrayInputStream(json.getBytes())));
    reader.beginArray();
    String actualString = reader.nextString();
    assertEquals("Hello World", actualString);
    reader.nextInt(); // read '42'
    reader.nextBoolean(); // read 'true'
    reader.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonObjectWithMissingBeginObject() throws IOException {
    String json = "{\"name\": \"John\", \"age\": 25}";
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader(new ByteArrayInputStream(json.getBytes())));
    // This should throw an IllegalStateException as 'reader' is not at the beginning of an object
    reader.nextString();
  }

  @Test
  public void testJsonObjectWithKeyValuePair() throws IOException {
    String json = "{\"name\": \"John\", \"age\": 25}";
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader(new ByteArrayInputStream(json.getBytes())));
    reader.beginObject();
    String actualString = reader.nextName();
    assertEquals("name", actualString);
    reader.nextString(); // read 'John'
    actualString = reader.nextName();
    assertEquals("age", actualString);
    int actualInt = reader.nextInt();
    assertEquals(25, actualInt);
    reader.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonObjectWithMissingEndObject() throws IOException {
    String json = "{\"name\": \"John\", \"age\": 25}";
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader(new ByteArrayInputStream(json.getBytes())));
    reader.beginObject();
    String name = reader.nextName();
    reader.nextString(); // read 'John'
    reader.beginArray(); // This should throw an IllegalStateException as 'reader' is not at the beginning of an array
  }

  @Test
  public void testJsonObjectWithMultipleKeyValuePairs() throws IOException {
    String json = "{\"name\": \"John\", \"age\": 25, \"city\": \"New York\"}";
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader(new ByteArrayInputStream(json.getBytes())));
    reader.beginObject();
    String name = reader.nextName();
    reader.nextString(); // read 'John'
    int age = reader.nextInt();
    assertEquals(25, age);
    String city = reader.nextName();
    reader.nextString(); // read 'New York'
    reader.endObject();
  }

  @Test
  public void testJsonNull() throws IOException {
    String json = "null";
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader(new ByteArrayInputStream(json.getBytes())));
    reader.beginObject();
    reader.nextName();
    reader.nextNull();
    reader.endObject();
  }

  @Test
  public void testJsonBooleanTrue() throws IOException {
    String json = "true";
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader(new ByteArrayInputStream(json.getBytes())));
    reader.beginObject();
    reader.nextName();
    reader.nextBoolean();
    reader.endObject();
  }

  @Test
  public void testJsonBooleanFalse() throws IOException {
    String json = "false";
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader(new ByteArrayInputStream(json.getBytes())));
    reader.beginObject();
    reader.nextName();
    reader.nextBoolean();
    reader.endObject();
    assertFalse(reader.nextBoolean());
  }

  @Test
  public void testJsonNumberInteger() throws IOException {
    String json = "123";
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader(new ByteArrayInputStream(json.getBytes())));
    reader.beginObject();
    reader.nextName();
    reader.nextInt();
    reader.endObject();
    int actual = reader.nextInt();
    assertEquals(123, actual);
  }

  @Test
  public void testJsonNumberLong() throws IOException {
    String json = "45678901234567890";
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader(new ByteArrayInputStream(json.getBytes())));
    reader.beginObject();
    reader.nextName();
    reader.nextLong();
    reader.endObject();
    long actual = reader.nextLong();
    assertEquals(45678901234567890L, actual);
  }

  @Test
  public void testJsonNumberFloat() throws IOException {
    String json = "3.14159265358979323846";
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader(new ByteArrayInputStream(json.getBytes())));
    reader.beginObject();
    reader.nextName();
    reader.nextDouble();
    reader.endObject();
    double actual = reader.nextDouble();
    assertEquals(3.14159265358979323846, actual, 0.0);
  }

  @Test
  public void testJsonNumberString() throws IOException {
    String json = "\"3.14159265358979323846\"";
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader(new ByteArrayInputStream(json.getBytes())));
    reader.beginObject();
    reader.nextName();
    reader.nextString();
    reader.nextString();
    reader.endObject();
    assertEquals("3.14159265358979323846", reader.nextString());
    reader.nextDouble(); // This should throw an AssertionError as 'reader' is not at the beginning of a number
  }

  @Test
  public void testJsonString() throws IOException {
    String json = "\"Hello World\"";
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader(new ByteArrayInputStream(json.getBytes())));
    reader.beginObject();
    reader.nextName();
    String actual = reader.nextString();
    assertEquals("Hello World", actual);
    reader.endObject();
  }

  @Test
  public void testJsonStringWithEscapedCharacter() throws IOException {
    String json = "\"Hello World\\\\\"\"";
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader(new ByteArrayInputStream(json.getBytes())));
    reader.beginObject();
    reader.nextName();
    String actual = reader.nextString();
    assertEquals("Hello World\\\"\"", actual);
    reader.endObject();
  }

  @Test
  public void testJsonStringWithEmptyValue() throws IOException {
    String json = "\"\"";
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader(new ByteArrayInputStream(json.getBytes())));
    reader.beginObject();
    reader.nextName();
    String actual = reader.nextString();
    assertEquals("", actual);
    reader.endObject();
  }

  @Test
  public void testJsonPrimitiveArray() throws IOException {
    String json = "[1, \"two\", 3.0, false, null, true]";
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader(new ByteArrayInputStream(json.getBytes())));
    reader.beginArray();
    int actualInt = reader.nextInt();
    assertEquals(1, actualInt);
    reader.nextString(); // read 'two'
    reader.nextDouble(); // read '3.0'
    reader.nextBoolean(); // read 'false'
    reader.nextNull();
    reader.nextBoolean(); // read 'true'
    reader.endArray();
  }

  @Test
  public void testJsonObjectNestedJsonArray() throws IOException {
    String json = "{\"name\": \"John\", \"age\": 25, \"hobbies\": [\"reading\", 42, \"playing video games\"]}";
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader(new ByteArrayInputStream(json.getBytes())));
    reader.beginObject();
    reader.nextName();
    reader.nextString(); // read 'John'
    reader.nextInt(); // read '25'
    reader.nextName();
    reader.beginArray();
    String string1 = reader.nextString();
    assertEquals("reading", string1);
    reader.nextInt(); // read '42'
    String string2 = reader.nextString();
    assertEquals("playing video games", string2);
    reader.endArray();
    reader.endObject();
  }

  @Test
  public void testJsonObjectNestedJsonObject() throws IOException {
    String json = "{\"name\": \"John\", \"address\": {\"street\": \"123 Main St\", \"city\": \"New York\"}}";
    JsonReader reader = new JsonTreeReader(JsonParser.parseReader(new ByteArrayInputStream(json.getBytes())));
    reader.beginObject();
    reader.nextName();
    reader.nextString(); // read 'John'
    reader.nextName();
    reader.beginObject();
    String street = reader.nextString();
    assertEquals("123 Main St", street);
    reader.nextName();
    reader.nextString(); // read 'New York'
    reader.endObject();
    reader.endObject();
  }
}