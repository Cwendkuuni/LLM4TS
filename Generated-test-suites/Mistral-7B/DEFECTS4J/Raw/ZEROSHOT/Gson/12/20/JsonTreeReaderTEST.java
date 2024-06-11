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
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;

import org.junit.Before;
import org.junit.Test;

public class JsonTreeReaderTest {

  private JsonElement jsonElement;
  private JsonTreeReader jsonTreeReader;

  @Before
  public void setUp() {
    JsonParser jsonParser = new JsonParser();
    jsonElement = jsonParser.parse("{\"foo\": [1, \"bar\"]}");
    jsonTreeReader = new JsonTreeReader(jsonElement);
  }

  @Test
  public void testBeginArray() throws IOException {
    jsonTreeReader.beginArray();
    JsonArray expectedArray = new JsonArray();
    JsonArray actualArray = (JsonArray) jsonTreeReader.peekStack();
    assertSame(expectedArray, actualArray);
  }

  @Test
  public void testEndArray() throws IOException {
    jsonTreeReader.beginArray();
    jsonTreeReader.endArray();
    assertNull(jsonTreeReader.peekStack());
  }

  @Test
  public void testBeginObject() throws IOException {
    jsonTreeReader.beginObject();
    JsonObject expectedObject = new JsonObject();
    JsonObject actualObject = (JsonObject) jsonTreeReader.peekStack();
    assertSame(expectedObject, actualObject);
  }

  @Test
  public void testEndObject() throws IOException {
    jsonTreeReader.beginObject();
    jsonTreeReader.endObject();
    assertNull(jsonTreeReader.peekStack());
  }

  @Test
  public void testHasNext() throws IOException {
    jsonTreeReader.beginArray();
    jsonTreeReader.beginArray();
    assertTrue(jsonTreeReader.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testHasNextWhenClosed() throws IOException {
    jsonTreeReader.close();
    jsonTreeReader.hasNext();
  }

  @Test
  public void testPeek() throws IOException {
    jsonTreeReader.beginArray();
    jsonTreeReader.beginArray();
    JsonArray expectedArray = jsonTreeReader.beginArray();
    JsonArray actualArray = (JsonArray) jsonTreeReader.peek();
    assertSame(expectedArray, actualArray);
  }

  @Test
  public void testNextName() throws IOException {
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    String expectedName = "foo";
    String actualName = jsonTreeReader.pathNames[jsonTreeReader.stackSize - 1];
    assertEquals(expectedName, actualName);
  }

  @Test
  public void testNextString() throws IOException {
    jsonTreeReader.beginObject();
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    jsonTreeReader.nextString();
    String expectedString = "bar";
    String actualString = jsonTreeReader.pathNames[jsonTreeReader.stackSize - 2];
    assertEquals(expectedString, actualString);
  }

  @Test
  public void testNextBoolean() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("foo", true);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    jsonTreeReader.nextBoolean();
    boolean expectedBoolean = true;
    boolean actualBoolean = jsonTreeReader.pathIndices[jsonTreeReader.stackSize - 1];
    assertEquals(expectedBoolean, actualBoolean);
  }

  @Test
  public void testNextNull() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("foo", null);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    jsonTreeReader.nextNull();
    assertNull(jsonTreeReader.pathNames[jsonTreeReader.stackSize - 1]);
  }

  @Test
  public void testNextDouble() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("foo", 1.5);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    jsonTreeReader.nextDouble();
    double expectedDouble = 1.5;
    double actualDouble = jsonTreeReader.pathIndices[jsonTreeReader.stackSize - 1];
    assertEquals(expectedDouble, actualDouble, 0.0);
  }

  @Test
  public void testNextLong() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("foo", 1);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    jsonTreeReader.nextLong();
    long expectedLong = 1;
    long actualLong = jsonTreeReader.pathIndices[jsonTreeReader.stackSize - 1];
    assertEquals(expectedLong, actualLong);
  }

  @Test
  public void testNextInt() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("foo", 1);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    jsonTreeReader.nextInt();
    int expectedInt = 1;
    int actualInt = jsonTreeReader.pathIndices[jsonTreeReader.stackSize - 1];
    assertEquals(expectedInt, actualInt);
  }

  @Test
  public void testClose() throws IOException {
    jsonTreeReader.close();
    assertFalse(jsonTreeReader.hasNext());
  }

  @Test
  public void testSkipValue() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("foo", null);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    jsonTreeReader.nextNull();
    jsonTreeReader.skipValue();
    assertEquals("null", jsonTreeReader.pathNames[jsonTreeReader.stackSize - 1]);
  }

  @Test
  public void testPromoteNameToValue() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("foo", "bar");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);
    jsonTreeReader.beginObject();
    jsonTreeReader.promoteNameToValue();
    assertEquals("bar", jsonTreeReader.pathNames[jsonTreeReader.stackSize - 1]);
  }

  @Test
  public void testGetPath() throws IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.add("foo", new JsonArray().add(1).add("bar"));
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonObject);
    jsonTreeReader.beginObject();
    jsonTreeReader.nextName();
    jsonTreeReader.beginArray();
    jsonTreeReader.nextInt();
    jsonTreeReader.nextName();
    jsonTreeReader.nextString();
    jsonTreeReader.endArray();
    jsonTreeReader.endObject();
    String expectedPath = "$." + "foo" + "[" + 0 + "]";
    String actualPath = jsonTreeReader.getPath();
    assertEquals(expectedPath, actualPath);
  }

  @Test
  public void testGetPathWhenEmpty() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(null);
    String expectedPath = "$";
    String actualPath = jsonTreeReader.getPath();
    assertEquals(expectedPath, actualPath);
  }

  @Test
  public void testGetPathWhenClosed() throws IOException {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(mock(JsonElement.class));
    jsonTreeReader.close();
    String expectedPath = "$";
    String actualPath = jsonTreeReader.getPath();
    assertEquals(expectedPath, actualPath);
  }
}