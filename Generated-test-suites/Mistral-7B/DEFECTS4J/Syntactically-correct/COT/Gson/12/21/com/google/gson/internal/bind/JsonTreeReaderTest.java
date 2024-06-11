import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JsonTreeReaderTest {
  private JsonTreeReader jsonTreeReader;
  private JsonElement jsonElement;

  @Before
  public void setUp() {
    jsonElement = mock(JsonElement.class);
    jsonTreeReader = new JsonTreeReader(jsonElement);
  }

  @After
  public void tearDown() {
    jsonTreeReader.close();
  }

  

  // Test constructor
  @Test
  public void testJsonTreeReaderConstructor() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
    assertNotNull(jsonTreeReader);
  }

  // Test beginArray
  @Test
  public void testBeginArrayFunctionality() throws IOException {
    jsonTreeReader.beginArray();
    expect(JsonToken.BEGIN_ARRAY);
    jsonTreeReader.endArray();
    expect(JsonToken.END_ARRAY);
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayWithoutEndArray() throws IOException {
    jsonTreeReader.beginArray();
  }

  @Test
  public void testBeginArrayWithEmptyArray() throws IOException {
    when(jsonElement.isJsonArray()).thenReturn(true);
    when(jsonElement.iterator()).thenReturn(new JsonArray().iterator());
    jsonTreeReader.beginArray();
    expect(JsonToken.BEGIN_ARRAY);
    jsonTreeReader.endArray();
    expect(JsonToken.END_ARRAY);
  }

  @Test
  public void testBeginArrayWithNonEmptyArray() throws IOException {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive("hello"));
    when(jsonElement.isJsonArray()).thenReturn(true);
    when(jsonElement.iterator()).thenReturn(jsonArray.iterator());
    jsonTreeReader.beginArray();
    expect(JsonToken.BEGIN_ARRAY);
    jsonTreeReader.endArray();
    expect(JsonToken.END_ARRAY);
    jsonTreeReader.nextName(); // "hello"
    jsonTreeReader.nextNull();
    jsonTreeReader.endArray(); // empty object
  }

  // Test endArray
  @Test
  public void testEndArrayFunctionality() throws IOException {
    jsonTreeReader.beginArray();
    jsonTreeReader.beginArray();
    jsonTreeReader.endArray();
    jsonTreeReader.endArray();
    expect(JsonToken.END_ARRAY);
  }

  @Test
  public void testEndArrayWithoutBeginArray() throws IOException {
    jsonTreeReader.endArray();
  }

  // Test beginObject
  @Test
  public void testBeginObjectFunctionality() throws IOException {
    jsonTreeReader.beginObject();
    expect(JsonToken.BEGIN_OBJECT);
    jsonTreeReader.endObject();
    expect(JsonToken.END_OBJECT);
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectWithoutEndObject() throws IOException {
    jsonTreeReader.beginObject();
  }

  @Test
  public void testBeginObjectWithEmptyObject() throws IOException {
    JsonObject jsonObject = new JsonObject();
    when(jsonElement.isJsonObject()).thenReturn(true);
    when(jsonElement.entrySet()).thenReturn(jsonObject.entrySet().iterator());
    jsonTreeReader.beginObject();
    expect(JsonToken.BEGIN_OBJECT);
    jsonTreeReader.endObject();
    expect(JsonToken.END_OBJECT);
  }

  @Test
  public void testBeginObjectWithNonEmptyObject() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name", "John");
    when(jsonElement.isJsonObject()).thenReturn(true);
    when(jsonElement.entrySet()).thenReturn(jsonObject.entrySet().iterator());
    jsonTreeReader.beginObject();
    expect(JsonToken.BEGIN_OBJECT);
    jsonTreeReader.nextName(); // "name"
    assertEquals("John", jsonTreeReader.nextString()); // "John"
    jsonTreeReader.nextNull();
    jsonTreeReader.endObject(); // empty object
    jsonTreeReader.endObject(); // end of document
  }

  // Test hasNext
  @Test
  public void testHasNextFunctionality() throws IOException {
    jsonTreeReader.beginArray();
    jsonTreeReader.beginArray();
    jsonTreeReader.endArray();
    jsonTreeReader.endArray();
    jsonTreeReader.beginObject();
    jsonTreeReader.beginObject();
    jsonTreeReader.endObject();
    jsonTreeReader.endObject();

    assertTrue(jsonTreeReader.hasNext());
    expect(JsonToken.BEGIN_ARRAY);

    jsonTreeReader.nextName(); // "null"
    jsonTreeReader.nextNull();
    jsonTreeReader.nextNull();

    assertFalse(jsonTreeReader.hasNext());
  }

  // Test peek
  @Test
  public void testPeekFunctionality() throws IOException {
    jsonTreeReader.beginArray();
    jsonTreeReader.beginArray();
    jsonTreeReader.endArray();
    jsonTreeReader.endArray();
    jsonTreeReader.beginObject();
    jsonTreeReader.beginObject();
    jsonTreeReader.endObject();
    jsonTreeReader.endObject();
    jsonTreeReader.beginObject();
    jsonTreeReader.beginObject();
    jsonTreeReader.endObject();
    jsonTreeReader.endObject();

    JsonToken expected = JsonToken.BEGIN_ARRAY;
    JsonToken actual;

    actual = jsonTreeReader.peek();
    assertEquals(expected, actual);

    jsonTreeReader.nextName();
    expected = JsonToken.NAME;
    actual = jsonTreeReader.peek();
    assertEquals(expected, actual);

    jsonTreeReader.nextNull();
    expected = JsonToken.NULL;
    actual = jsonTreeReader.peek();
    assertEquals(expected, actual);

    jsonTreeReader.nextNull(); // empty object
    expected = JsonToken.END_OBJECT;
    actual = jsonTreeReader.peek();
    assertEquals(expected, actual);

    jsonTreeReader.nextNull(); // end of document
    expected = JsonToken.END_DOCUMENT;
    actual = jsonTreeReader.peek();
    assertEquals(expected, actual);
  }

  // Test expect
  @Test(expected = IllegalStateException.class)
  public void testExpectJsonToken() throws IOException {
    jsonTreeReader.nextNull();
    jsonTreeReader.expect(JsonToken.NULL);
  }

  @Test(expected = IllegalStateException.class)
  public void testExpectJsonTokenWithDifferentToken() throws IOException {
    jsonTreeReader.nextNull();
    jsonTreeReader.expect(JsonToken.BOOLEAN);
  }

  @Test(expected = IllegalStateException.class)
  public void testExpectJsonTokenWithEndOfDocument() throws IOException {
    jsonTreeReader.nextNull();
    jsonTreeReader.nextNull();
    jsonTreeReader.expect(JsonToken.BEGIN_ARRAY);
  }

  // Test popStack and peekStack
  @Test
  public void testPopStackAndPeekStackFunctionality() throws IOException {
    jsonTreeReader.beginArray();
    jsonTreeReader.beginArray();
    jsonTreeReader.endArray();
    jsonTreeReader.endArray();
    jsonTreeReader.beginObject();
    jsonTreeReader.beginObject();
    jsonTreeReader.endObject();
    jsonTreeReader.endObject();
    jsonTreeReader.beginObject();
    jsonTreeReader.beginObject();
    jsonTreeReader.endObject();
    jsonTreeReader.endObject();

    Object expected = new JsonArray();
    Object actual;

    actual = jsonTreeReader.peekStack();
    assertEquals(expected, actual);

    actual = jsonTreeReader.popStack();
    assertEquals(expected, actual);

    jsonTreeReader.nextNull(); // empty object
    expected = "null";
    String actualString = (String) jsonTreeReader.popStack();
    assertEquals(expected, actualString);

    jsonTreeReader.nextNull(); // empty object
    expected = null;
    actual = jsonTreeReader.popStack();
    assertEquals(expected, actual);
  }

  // Test close
  @Test
  public void testCloseFunctionality() throws IOException {
    jsonTreeReader.beginArray();
    jsonTreeReader.beginArray();
    jsonTreeReader.endArray();
    jsonTreeReader.endArray();
    jsonTreeReader.beginObject();
    jsonTreeReader.beginObject();
    jsonTreeReader.endObject();
    jsonTreeReader.endObject();
    jsonTreeReader.beginObject();
    jsonTreeReader.beginObject();
    jsonTreeReader.endObject();
    jsonTreeReader.endObject();

    jsonTreeReader.close();

    assertSame(SENTINEL_CLOSED, jsonTreeReader.peekStack());
  }

  
}