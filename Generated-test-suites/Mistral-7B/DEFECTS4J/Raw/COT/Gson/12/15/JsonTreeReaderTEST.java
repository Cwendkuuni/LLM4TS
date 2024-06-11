import static org.junit.Assert.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;

public class JsonTreeReaderTest {
  

  @Test
  public void testJsonTreeReader_EmptyJsonElement() {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseString("{}"));
    assertFalse(reader.hasNext());
    reader.close();
  }

  @Test(expected = AssertionError.class)
  public void testJsonTreeReader_NullReader() {
    new JsonTreeReader(null);
  }

  @Test
  public void testJsonTreeReader_BeginArray() {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseString("[\"apple\", \"banana\"]"));
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getAsInt());
    reader.beginArray();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek().getAsInt());
    assertEquals("apple", reader.nextString());
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek().getAsInt());
    assertEquals("banana", reader.nextString());
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.END_ARRAY, reader.peek().getAsInt());
    reader.endArray();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.END_OBJECT, reader.peek().getAsInt());
    reader.close();
  }

  @Test
  public void testJsonTreeReader_BeginObject() {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseString("{\"name\": \"John\", \"age\": 30}"));
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().getAsInt());
    reader.beginObject();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek().getAsInt());
    assertEquals("name", reader.nextName());
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().getAsInt());
    reader.promoteNameToValue();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek().getAsInt());
    assertEquals("John", reader.nextString());
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek().getAsInt());
    assertEquals("age", reader.nextName());
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NUMBER, reader.peek().getAsInt());
    assertEquals(30, reader.nextInt());
    reader.endObject();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.END_OBJECT, reader.peek().getAsInt());
    reader.close();
  }

  @Test
  public void testJsonTreeReader_EmptyArray() {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseString("[]"));
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek().getAsInt());
    reader.beginArray();
    assertFalse(reader.hasNext());
    reader.endArray();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.END_ARRAY, reader.peek().getAsInt());
    reader.endArray();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.END_OBJECT, reader.peek().getAsInt());
    reader.close();
  }

  @Test
  public void testJsonTreeReader_EmptyObject() {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseString("{}"));
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().getAsInt());
    reader.beginObject();
    assertFalse(reader.hasNext());
    reader.endObject();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.END_OBJECT, reader.peek().getAsInt());
    reader.endObject();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.END_DOCUMENT, reader.peek().getAsInt());
    reader.close();
  }

  @Test
  public void testJsonTreeReader_ComplexJson() {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseString("{\"name\": { \"first\": \"John\", \"last\": \"Doe\" }, \"age\": 30, \"hobbies\": [\"reading\", \"swimming\"] }"));
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek().getAsInt());
    reader.beginObject();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek().getAsInt());
    assertEquals("name", reader.nextName());
    reader.beginObject();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek().getAsInt());
    assertEquals("first", reader.nextName());
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek().getAsInt());
    assertEquals("John", reader.nextString());
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek().getAsInt());
    assertEquals("last", reader.nextName());
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek().getAsInt());
    assertEquals("Doe", reader.nextString());
    reader.endObject();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek().getAsInt());
    assertEquals("age", reader.nextName());
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NUMBER, reader.peek().getAsInt());
    assertEquals(30, reader.nextInt());
    reader.beginArray();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.NAME, reader.peek().getAsInt());
    assertEquals("hobbies", reader.nextName());
    reader.beginArray();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.STRING, reader.peek().getAsInt());
    assertEquals("reading", reader.nextString());
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.STRING, reader.peek().getAsInt());
    assertEquals("swimming", reader.nextString());
    reader.endArray();
    reader.endArray();
    assertTrue(reader.hasNext());
    assertEquals(JsonToken.END_OBJECT, reader.peek().getAsInt());
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeReader_Closed() {
    JsonTreeReader reader = new JsonTreeReader(JsonParser.parseString("{}"));
    reader.close();
    reader.nextInt();
  }

  
}