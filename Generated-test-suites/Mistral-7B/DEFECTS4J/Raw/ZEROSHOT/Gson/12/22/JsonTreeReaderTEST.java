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

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Before;
import org.junit.Test;

public class JsonTreeReaderTest {

  private JsonTreeReader reader;
  private JsonElement jsonElement;

  @Before
  public void setup() {
    jsonElement = createJsonElement();
    reader = new JsonTreeReader(jsonElement);
  }

  @Test
  public void testHasNextReturnsTrueWhenThereIsANextToken() throws IOException {
    // Given

    // When
    boolean hasNext = reader.hasNext();

    // Then
    assertTrue(hasNext);
  }

  @Test
  public void testHasNextReturnsFalseWhenThereIsNoNextToken() throws IOException {
    // Given
    reader.close();

    // When
    boolean hasNext = reader.hasNext();

    // Then
    assertFalse(hasNext);
  }

  @Test
  public void testPeekReturnsExpectedToken() throws IOException {
    // Given

    // When
    JsonToken token = reader.peek();

    // Then
    assertEquals(JsonToken.BEGIN_ARRAY, token);
  }

  @Test
  public void testNextNameReturnsExpectedName() throws IOException {
    // Given

    // When
    String name = reader.nextName();

    // Then
    assertEquals("foo", name);
  }

  @Test
  public void testNextStringReturnsExpectedString() throws IOException {
    // Given

    // When
    String string = reader.nextString();

    // Then
    assertEquals("bar", string);
  }

  @Test
  public void testNextBooleanReturnsExpectedBoolean() throws IOException {
    // Given

    // When
    boolean booleanValue = reader.nextBoolean();

    // Then
    assertTrue(booleanValue);
  }

  @Test
  public void testNextNullReturnsExpectedNull() throws IOException {
    // Given

    // When
    reader.nextNull();

    // Then
    assertNull(reader.peek());
  }

  @Test
  public void testNextDoubleReturnsExpectedDouble() throws IOException {
    // Given

    // When
    double doubleValue = reader.nextDouble();

    // Then
    assertEquals(2.5, doubleValue, 0.001);
  }

  @Test
  public void testNextLongReturnsExpectedLong() throws IOException {
    // Given

    // When
    long longValue = reader.nextLong();

    // Then
    assertEquals(42L, longValue);
  }

  @Test
  public void testNextIntReturnsExpectedInt() throws IOException {
    // Given

    // When
    int intValue = reader.nextInt();

    // Then
    assertEquals(42, intValue);
  }

  @Test
  public void testCloseClosesReader() throws IOException {
    // Given

    // When
    reader.close();

    // Then
    assertFalse(reader.hasNext());
  }

  @Test
  public void testPeekStackReturnsExpectedObject() throws IOException {
    // Given

    // When
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("foo", new JsonPrimitive("bar"));
    JsonTreeReader reader = new JsonTreeReader(jsonObject);
    reader.beginObject();

    // Then
    assertEquals("foo", reader.peekStack().toString());
  }

  @Test
  public void testPopStackReturnsExpectedObject() throws IOException {
    // Given
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("foo", new JsonPrimitive("bar"));
    JsonTreeReader reader = new JsonTreeReader(jsonObject);
    reader.beginObject();

    // When
    Object poppedObject = reader.popStack();

    // Then
    assertEquals("bar", poppedObject.toString());
  }

  @Test
  public void testPushAddsObjectToStack() throws IOException {
    // Given

    // When
    reader.push(new JsonPrimitive(1));

    // Then
    assertEquals(1, reader.stackSize);
  }

  @Test
  public void testBeginArrayStartsReadingArray() throws IOException {
    // Given

    // When
    reader.beginArray();

    // Then
    assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
  }

  @Test
  public void testEndArrayEndsReadingArray() throws IOException {
    // Given

    // When
    reader.beginArray();
    reader.endArray();

    // Then
    assertEquals(JsonToken.END_ARRAY, reader.peek());
  }

  @Test
  public void testBeginObjectStartsReadingObject() throws IOException {
    // Given

    // When
    reader.beginObject();

    // Then
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
  }

  @Test
  public void testEndObjectEndsReadingObject() throws IOException {
    // Given

    // When
    reader.beginObject();
    reader.endObject();

    // Then
    assertEquals(JsonToken.END_OBJECT, reader.peek());
  }

  @Test
  public void testGetPathReturnsExpectedPath() throws IOException {
    // Given

    // When
    reader.beginArray().beginObject().addProperty("foo", new JsonPrimitive("bar"));

    // Then
    assertEquals("/foo", reader.getPath());
  }

  @Test
  public void testSkipValueSkipsNextValue() throws IOException {
    // Given

    // When
    reader.nextName();
    reader.nextString();
    reader.skipValue();

    // Then
    assertEquals("null", reader.peek().toString());
  }

  @Test
  public void testPromoteNameToValuePromotesNameToValue() throws IOException {
    // Given

    // When
    reader.nextName();

    // Then
    assertEquals("foo", reader.peek().toString());

    // When
    reader.promoteNameToValue();

    // Then
    assertEquals("foo", reader.nextString().toString());
  }

  @Test
  public void testReadArrayOfObjectsWithNullValues() throws IOException {
    // Given
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonObject());
    jsonArray.add(null);
    jsonArray.add(new JsonObject());

    // When
    reader = new JsonTreeReader(jsonArray);
    reader.beginArray();

    // Then
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());

    // When
    reader.nextName();

    // Then
    assertEquals("null", reader.peek().toString());

    // When
    reader.nextNull();

    // Then
    assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());

    // When
    reader.nextName();

    // Then
    assertEquals("foo", reader.nextName().toString());

    // When
    reader.endObject();

    // Then
    assertEquals(JsonToken.END_ARRAY, reader.peek());

    // When
    reader.close();
  }

  private JsonElement createJsonElement() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("foo", new JsonPrimitive("bar"));
    jsonObject.addProperty("baz", true);
    jsonObject.add("qux", new JsonArray());
    JsonArray quxArray = jsonObject.getAsJsonArray("qux");
    quxArray.add(new JsonPrimitive(1));
    quxArray.add(new JsonPrimitive(2.5));
    quxArray.add(new JsonPrimitive("three"));
    jsonObject.add("quux", new JsonObject());
    jsonObject.add("quuz", new JsonNull());
    return jsonObject;
  }
}