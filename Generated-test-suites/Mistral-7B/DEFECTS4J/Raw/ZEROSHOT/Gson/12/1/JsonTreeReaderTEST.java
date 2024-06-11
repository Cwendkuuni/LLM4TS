import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JsonTreeReaderTest {

  private JsonElement jsonElement;
  private JsonTreeReader jsonTreeReader;

  @Before
  public void setUp() {
    jsonElement = new JsonObject()
        .add("array", new JsonArray()
            .add(new JsonPrimitive("element1"))
            .add(new JsonPrimitive("element2")))
        .add("object", new JsonObject()
            .add("name1", new JsonPrimitive("value1"))
            .add("name2", new JsonPrimitive("value2"))
            .add("name3", JsonNull.INSTANCE));
    jsonTreeReader = new JsonTreeReader(jsonElement);
  }

  @Test
  public void testBeginArray() throws IOException {
    jsonTreeReader.beginArray();
    assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayTwice() throws IOException {
    jsonTreeReader.beginArray();
    jsonTreeReader.beginArray();
  }

  @Test
  public void testEndArray() throws IOException {
    jsonTreeReader.beginArray();
    jsonTreeReader.endArray();
    assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayBeforeBeginArray() throws IOException {
    jsonTreeReader.endArray();
    jsonTreeReader.nextToken();
  }

  @Test
  public void testHasNext() throws IOException {
    jsonTreeReader.nextToken();
    jsonTreeReader.nextToken();
    assertTrue(jsonTreeReader.hasNext());
  }

  @Test
  public void testPeek() throws IOException {
    jsonTreeReader.nextToken();
    assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
  }

  @Test
  public void testNextName() throws IOException {
    jsonTreeReader.nextName();
    assertEquals("array", jsonTreeReader.nextName());
    assertNull(jsonTreeReader.nextName());
  }

  @Test
  public void testNextString() throws IOException {
    jsonTreeReader.nextString();
    assertEquals("element1", jsonTreeReader.nextString());
    assertEquals("value1", jsonTreeReader.nextString());
    assertNull(jsonTreeReader.nextString());
  }

  @Test
  public void testNextBoolean() throws IOException {
    jsonTreeReader.nextBoolean();
    jsonTreeReader.nextBoolean();
    assertFalse(jsonTreeReader.nextBoolean());
    assertTrue(jsonTreeReader.nextBoolean());
    assertNull(jsonTreeReader.nextBoolean());
  }

  @Test
  public void testNextNull() throws IOException {
    jsonTreeReader.nextNull();
    jsonTreeReader.nextNull();
    assertNull(jsonTreeReader.nextNull());
  }

  @Test
  public void testNextDouble() throws IOException {
    jsonTreeReader.nextDouble();
    jsonTreeReader.nextDouble();
    assertEquals(1.2, jsonTreeReader.nextDouble(), 0.001);
    assertNull(jsonTreeReader.nextDouble());
  }

  @Test
  public void testNextLong() throws IOException {
    jsonTreeReader.nextLong();
    jsonTreeReader.nextLong();
    assertEquals(42L, jsonTreeReader.nextLong());
    assertNull(jsonTreeReader.nextLong());
  }

  @Test
  public void testNextInt() throws IOException {
    jsonTreeReader.nextInt();
    jsonTreeReader.nextInt();
    assertEquals(42, jsonTreeReader.nextInt());
    assertNull(jsonTreeReader.nextInt());
  }

  @Test
  public void testClose() throws IOException {
    jsonTreeReader.close();
    assertNull(jsonTreeReader.peek());
  }

  @Test(expected = IllegalStateException.class)
  public void testPeekAfterClose() throws IOException {
    jsonTreeReader.close();
    jsonTreeReader.peek();
  }

  @Test
  public void testEndObject() throws IOException {
    jsonTreeReader.nextToken();
    jsonTreeReader.beginObject();
    jsonTreeReader.endObject();
    assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectBeforeBeginObject() throws IOException {
    jsonTreeReader.endObject();
    jsonTreeReader.nextToken();
  }

  @Test
  public void testSkipValue() throws IOException {
    jsonTreeReader.nextName();
    jsonTreeReader.nextName();
    jsonTreeReader.nextName();
    jsonTreeReader.skipValue();
    jsonTreeReader.nextName();
    jsonTreeReader.nextString();
    assertEquals(3, jsonTreeReader.pathIndices[jsonTreeReader.stackSize - 1]);
  }

  @Test
  public void testPromoteNameToValue() throws IOException {
    jsonTreeReader.nextName();
    jsonTreeReader.promoteNameToValue();
    assertEquals("value1", jsonTreeReader.nextString());
  }

  @Test
  public void testGetPath() throws IOException {
    jsonTreeReader.nextToken();
    jsonTreeReader.nextName();
    jsonTreeReader.nextName();
    jsonTreeReader.nextToken();
    jsonTreeReader.nextName();
    jsonTreeReader.nextString();
    jsonTreeReader.nextToken();
    jsonTreeReader.nextName();
    jsonTreeReader.nextNull();
    jsonTreeReader.nextToken();
    jsonTreeReader.nextName();
    jsonTreeReader.nextString();
    jsonTreeReader.close();
    assertEquals("$array[0]$name1$value1.$object.name1$value2.$object.name3$null.$", jsonTreeReader.getPath());
  }
}