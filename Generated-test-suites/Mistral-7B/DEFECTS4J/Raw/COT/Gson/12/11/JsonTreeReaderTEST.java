import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Before;
import org.junit.Test;

public class JsonTreeReaderTest {

  private JsonTreeReader reader;
  private JsonElement jsonElement;

  @Before
  public void setup() {
    jsonElement = new JsonObject() {{
      add("array", new JsonArray() {{
        add(new JsonPrimitive(1));
        add(new JsonPrimitive(2));
      }});
      add("object", new JsonObject() {{
        addProperty("name", "John");
        addProperty("age", 30);
      }});
    }};
    reader = new JsonTreeReader(jsonElement);
  }

  

  @Test
  public void testBeginArray() throws IOException {
    reader.beginArray();
    assertTrue(reader.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayTwice() throws IOException {
    reader.beginArray();
    reader.beginArray();
  }

  @Test
  public void testEndArray() throws IOException {
    reader.beginArray();
    reader.endArray();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testPeekArray() throws IOException {
    reader.beginArray();
    JsonElement actual = reader.peek();
    assertEquals(JsonToken.BEGIN_ARRAY, actual.getAsJson().getType());
  }

  @Test
  public void testNextStringInArray() throws IOException {
    reader.beginArray();
    reader.nextString();
    assertEquals("1", reader.nextString());
  }

  @Test
  public void testNextNullInArray() throws IOException {
    JsonArray jsonArray = new JsonArray() {{
      add(new JsonPrimitive(1));
      add(JsonNull.INSTANCE);
      add(new JsonPrimitive(3));
    }};
    reader = new JsonTreeReader(jsonArray);
    reader.beginArray();
    reader.nextString();
    reader.nextNull();
    reader.nextString();
    assertEquals("3", reader.nextString());
  }

  @Test
  public void testBeginObject() throws IOException {
    reader.beginObject();
    assertTrue(reader.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectTwice() throws IOException {
    reader.beginObject();
    reader.beginObject();
  }

  @Test
  public void testEndObject() throws IOException {
    reader.beginObject();
    reader.endObject();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testPeekObject() throws IOException {
    reader.beginObject();
    JsonElement actual = reader.peek();
    assertEquals(JsonToken.BEGIN_OBJECT, actual.getAsJson().getType());
  }

  @Test
  public void testNextStringInObject() throws IOException {
    reader.beginObject();
    reader.nextName();
    reader.nextString();
    assertEquals("name", reader.nextName());
    assertEquals("John", reader.nextString());
  }

  @Test
  public void testNextBooleanInObject() throws IOException {
    reader.beginObject();
    reader.nextName();
    reader.nextBoolean();
    assertEquals("age", reader.nextName());
    assertTrue(reader.nextBoolean());
  }

  @Test
  public void testNextNullInObject() throws IOException {
    reader.beginObject();
    reader.nextName();
    reader.nextNull();
    assertEquals("null", reader.nextName());
    assertTrue(reader.nextBoolean());
  }

  @Test
  public void testNextIntInObject() throws IOException {
    reader.beginObject();
    reader.nextName();
    reader.nextInt();
    assertEquals("age", reader.nextName());
    assertEquals(30, reader.nextInt());
  }

  @Test
  public void testHasNext() throws IOException {
    reader.beginObject();
    reader.endObject();
    reader.beginArray();
    reader.endArray();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testPeek() throws IOException {
    reader.beginObject();
    reader.nextName();
    reader.nextString();
    JsonToken actual = reader.peek();
    assertEquals(JsonToken.BEGIN_ARRAY, actual);
  }

  @Test
  public void testNextNameWithEmptyObject() throws IOException {
    JsonObject jsonObject = new JsonObject();
    reader = new JsonTreeReader(jsonObject);
    reader.beginObject();
    assertEquals("", reader.nextName());
    assertFalse(reader.hasNext());
  }

  @Test
  public void testNextNameWithNullObject() throws IOException {
    JsonObject jsonObject = null;
    reader = new JsonTreeReader(jsonObject);
    reader.beginObject();
    assertNull(reader.nextName());
    assertFalse(reader.hasNext());
  }

  @Test
  public void testNextNameWithEmptyArray() throws IOException {
    JsonArray jsonArray = new JsonArray();
    reader = new JsonTreeReader(jsonArray);
    reader.beginArray();
    assertEquals("", reader.nextName());
    assertFalse(reader.hasNext());
  }

  @Test
  public void testNextNameWithNullArray() throws IOException {
    JsonArray jsonArray = null;
    reader = new JsonTreeReader(jsonArray);
    reader.beginArray();
    assertNull(reader.nextName());
    assertFalse(reader.hasNext());
  }

  @Test
  public void testNextNameWithEmptyJson() throws IOException {
    JsonElement jsonElement = JsonNull.INSTANCE;
    reader = new JsonTreeReader(jsonElement);
    reader.beginObject();
    assertFalse(reader.hasNext());
  }

  @Test
  public void testClose() throws IOException {
    reader.close();
    assertFalse(reader.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testReadAfterClose() throws IOException {
    reader.close();
    reader.nextString();
  }

  @Test
  public void testPromoteNameToValue() throws IOException {
    reader.promoteNameToValue();
    assertEquals("name", reader.nextString());
    assertEquals("John", reader.nextString());
  }

  @Test
  public void testToString() throws IOException {
    assertEquals("JsonTreeReader", reader.toString());
  }

  @Test
  public void testGetPath() throws IOException {
    reader.beginArray();
    reader.nextString();
    reader.beginObject();
    reader.nextName();
    reader.nextString();
    reader.nextBoolean();
    reader.nextName();
    reader.nextInt();
    reader.endObject();
    reader.endArray();
    assertEquals("$[0].object.name", reader.getPath());
  }

  @Test
  public void testLocationString() throws IOException {
    reader.beginArray();
    reader.nextString();
    reader.beginObject();
    reader.nextName();
    reader.nextString();
    reader.nextBoolean();
    reader.nextName();
    reader.nextInt();
    reader.endObject();
    reader.endArray();
    assertEquals(" at path $[0].object.name", reader.locationString());
  }

  
}