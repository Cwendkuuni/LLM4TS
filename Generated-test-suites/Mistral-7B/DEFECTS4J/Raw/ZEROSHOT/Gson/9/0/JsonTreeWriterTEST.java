import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class JsonTreeWriterTest {

  private JsonTreeWriter jsonTreeWriter;
  private Writer writerMock;

  @Before
  public void setUp() {
    writerMock = mock(Writer.class);
    jsonTreeWriter = new JsonTreeWriter() {
      @Override
      Writer getDelegate() {
        return writerMock;
      }
    };
  }

  @After
  public void tearDown() {
    jsonTreeWriter.close();
  }

  @Test
  public void testBeginArray() throws IOException {
    jsonTreeWriter.beginArray();
    JsonArray expectedArray = new JsonArray();
    JsonArray actualArray = (JsonArray) jsonTreeWriter.peek();

    Assert.assertEquals(expectedArray, actualArray);
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayWhenStackIsEmpty() throws IOException {
    jsonTreeWriter.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayAfterEndArray() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
    jsonTreeWriter.beginArray();
  }

  @Test
  public void testBeginObject() throws IOException {
    jsonTreeWriter.beginObject();
    JsonObject expectedObject = new JsonObject();
    JsonObject actualObject = (JsonObject) jsonTreeWriter.peek();

    Assert.assertEquals(expectedObject, actualObject);
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectWhenStackIsEmpty() throws IOException {
    jsonTreeWriter.beginObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectAfterEndObject() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    jsonTreeWriter.beginObject();
  }

  @Test
  public void testEndArray() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();

    JsonElement topOfStack = jsonTreeWriter.peek();
    Assert.assertTrue(topOfStack instanceof JsonArray);

    jsonTreeWriter.endArray();

    topOfStack = jsonTreeWriter.peek();
    Assert.assertEquals(JsonNull.INSTANCE, topOfStack);
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWhenStackIsEmpty() throws IOException {
    jsonTreeWriter.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayAfterBeginObject() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endArray();
  }

  @Test
  public void testEndObject() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();

    JsonElement topOfStack = jsonTreeWriter.peek();
    Assert.assertEquals(JsonNull.INSTANCE, topOfStack);
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWhenStackIsEmpty() throws IOException {
    jsonTreeWriter.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectAfterBeginArray() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
  }

  @Test
  public void testName() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("testName");
    JsonObject expectedObject = new JsonObject();
    String expectedName = "testName";
    expectedObject.addProperty(expectedName, "");

    JsonObject actualObject = (JsonObject) jsonTreeWriter.peek();
    Assert.assertEquals(expectedObject, actualObject);
    String actualName = actualObject.name(expectedName).getAsString();

    Assert.assertEquals(expectedName, actualName);
  }

  @Test(expected = IllegalStateException.class)
  public void testNameWhenStackIsEmpty() throws IOException {
    jsonTreeWriter.name("testName");
  }

  @Test(expected = IllegalStateException.class)
  public void testNameAfterEndObject() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    jsonTreeWriter.name("testName");
  }

  @Test
  public void testValueString() throws IOException {
    jsonTreeWriter.value("testString");

    JsonObject expectedObject = new JsonObject();
    JsonPrimitive expectedPrimitive = new JsonPrimitive("testString");

    JsonObject actualObject = (JsonObject) jsonTreeWriter.peek();
    JsonPrimitive actualPrimitive = (JsonPrimitive) actualObject.get("");

    Assert.assertEquals(expectedObject, actualObject);
    Assert.assertEquals(expectedPrimitive, actualPrimitive);
  }

  @Test
  public void testValueBooleanTrue() throws IOException {
    jsonTreeWriter.value(true);

    JsonObject expectedObject = new JsonObject();
    JsonPrimitive expectedPrimitive = new JsonPrimitive(true);

    JsonObject actualObject = (JsonObject) jsonTreeWriter.peek();
    JsonPrimitive actualPrimitive = (JsonPrimitive) actualObject.get("");

    Assert.assertEquals(expectedObject, actualObject);
    Assert.assertEquals(expectedPrimitive, actualPrimitive);
  }

  @Test
  public void testValueBooleanFalse() throws IOException {
    jsonTreeWriter.value(false);

    JsonObject expectedObject = new JsonObject();
    JsonPrimitive expectedPrimitive = new JsonPrimitive(false);

    JsonObject actualObject = (JsonObject) jsonTreeWriter.peek();
    JsonPrimitive actualPrimitive = (JsonPrimitive) actualObject.get("");

    Assert.assertEquals(expectedObject, actualObject);
    Assert.assertEquals(expectedPrimitive, actualPrimitive);
  }

  @Test
  public void testValueDouble() throws IOException {
    jsonTreeWriter.value(1.23);

    JsonObject expectedObject = new JsonObject();
    JsonPrimitive expectedPrimitive = new JsonPrimitive(1.23);

    JsonObject actualObject = (JsonObject) jsonTreeWriter.peek();
    JsonPrimitive actualPrimitive = (JsonPrimitive) actualObject.get("");

    Assert.assertEquals(expectedObject, actualObject);
    Assert.assertEquals(expectedPrimitive, actualPrimitive);
  }

  @Test
  public void testValueLong() throws IOException {
    jsonTreeWriter.value(123);

    JsonObject expectedObject = new JsonObject();
    JsonPrimitive expectedPrimitive = new JsonPrimitive(123);

    JsonObject actualObject = (JsonObject) jsonTreeWriter.peek();
    JsonPrimitive actualPrimitive = (JsonPrimitive) actualObject.get("");

    Assert.assertEquals(expectedObject, actualObject);
    Assert.assertEquals(expectedPrimitive, actualPrimitive);
  }

  @Test
  public void testValueNumber() throws IOException {
    jsonTreeWriter.value(new Long(123));

    JsonObject expectedObject = new JsonObject();
    JsonPrimitive expectedPrimitive = new JsonPrimitive(123);

    JsonObject actualObject = (JsonObject) jsonTreeWriter.peek();
    JsonPrimitive actualPrimitive = (JsonPrimitive) actualObject.get("");

    Assert.assertEquals(expectedObject, actualObject);
    Assert.assertEquals(expectedPrimitive, actualPrimitive);
  }

  @Test
  public void testFlushAndClose() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.value("testString");
    jsonTreeWriter.endArray();

    jsonTreeWriter.flush();
    jsonTreeWriter.close();

    verify(writerMock).write(SENTINEL_CLOSED.getAsString().getBytes());
    verify(writerMock).close();
  }
}