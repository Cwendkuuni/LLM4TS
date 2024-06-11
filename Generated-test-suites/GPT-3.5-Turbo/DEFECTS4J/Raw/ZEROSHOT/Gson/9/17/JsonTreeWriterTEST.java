import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class JsonTreeWriterTest {
  
  private JsonTreeWriter jsonTreeWriter;

  @Before
  public void setUp() throws IOException {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
  public void testGet() {
    JsonElement expected = JsonNull.INSTANCE;
    JsonElement actual = jsonTreeWriter.get();
    Assert.assertEquals(expected, actual);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetWithNonEmptyStack() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.get();
  }

  @Test
  public void testPeek() {
    JsonElement expected = JsonNull.INSTANCE;
    JsonElement actual = jsonTreeWriter.peek();
    Assert.assertEquals(expected, actual);
  }

  @Test(expected = IllegalStateException.class)
  public void testPeekWithEmptyStack() throws IOException {
    jsonTreeWriter.peek();
  }

  @Test
  public void testPut() {
    JsonNull nullValue = JsonNull.INSTANCE;
    jsonTreeWriter.put(nullValue);
    JsonElement expected = nullValue;
    JsonElement actual = jsonTreeWriter.peek();
    Assert.assertEquals(expected, actual);
  }

  @Test(expected = IllegalStateException.class)
  public void testPutWithPendingName() throws IOException {
    jsonTreeWriter.name("name");
    jsonTreeWriter.put(new JsonPrimitive("value"));
  }

  @Test
  public void testBeginArray() throws IOException {
    jsonTreeWriter.beginArray();
    JsonElement expected = new JsonArray();
    JsonElement actual = jsonTreeWriter.peek();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testEndArray() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
    JsonElement expected = JsonNull.INSTANCE;
    JsonElement actual = jsonTreeWriter.peek();
    Assert.assertEquals(expected, actual);
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWithEmptyStack() throws IOException {
    jsonTreeWriter.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWithPendingName() throws IOException {
    jsonTreeWriter.name("name");
    jsonTreeWriter.endArray();
  }

  @Test
  public void testBeginObject() throws IOException {
    jsonTreeWriter.beginObject();
    JsonElement expected = new JsonObject();
    JsonElement actual = jsonTreeWriter.peek();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testEndObject() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    JsonElement expected = JsonNull.INSTANCE;
    JsonElement actual = jsonTreeWriter.peek();
    Assert.assertEquals(expected, actual);
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWithEmptyStack() throws IOException {
    jsonTreeWriter.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWithPendingName() throws IOException {
    jsonTreeWriter.name("name");
    jsonTreeWriter.endObject();
  }

  @Test
  public void testName() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    String expected = "name";
    String actual = jsonTreeWriter.pendingName;
    Assert.assertEquals(expected, actual);
  }

  @Test(expected = IllegalStateException.class)
  public void testNameWithEmptyStack() throws IOException {
    jsonTreeWriter.name("name");
  }

  @Test(expected = IllegalStateException.class)
  public void testNameWithPendingName() throws IOException {
    jsonTreeWriter.name("name1");
    jsonTreeWriter.name("name2");
  }

  @Test(expected = IllegalStateException.class)
  public void testNameWithNonObjectElement() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.name("name");
  }

  @Test
  public void testValueString() throws IOException {
    String value = "value";
    jsonTreeWriter.value(value);
    JsonElement expected = new JsonPrimitive(value);
    JsonElement actual = jsonTreeWriter.peek();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testValueStringNull() throws IOException {
    jsonTreeWriter.value((String) null);
    JsonElement expected = JsonNull.INSTANCE;
    JsonElement actual = jsonTreeWriter.peek();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testNullValue() throws IOException {
    jsonTreeWriter.nullValue();
    JsonElement expected = JsonNull.INSTANCE;
    JsonElement actual = jsonTreeWriter.peek();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testValueBoolean() throws IOException {
    boolean value = true;
    jsonTreeWriter.value(value);
    JsonElement expected = new JsonPrimitive(value);
    JsonElement actual = jsonTreeWriter.peek();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testValueDouble() throws IOException {
    double value = 3.14;
    jsonTreeWriter.value(value);
    JsonElement expected = new JsonPrimitive(value);
    JsonElement actual = jsonTreeWriter.peek();
    Assert.assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueDoubleWithInvalidValue() throws IOException {
    jsonTreeWriter.value(Double.NaN);
  }

  @Test
  public void testValueLong() throws IOException {
    long value = 123;
    jsonTreeWriter.value(value);
    JsonElement expected = new JsonPrimitive(value);
    JsonElement actual = jsonTreeWriter.peek();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testValueNumber() throws IOException {
    Number value = 3.14;
    jsonTreeWriter.value(value);
    JsonElement expected = new JsonPrimitive(value);
    JsonElement actual = jsonTreeWriter.peek();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testValueNumberNull() throws IOException {
    jsonTreeWriter.value((Number) null);
    JsonElement expected = JsonNull.INSTANCE;
    JsonElement actual = jsonTreeWriter.peek();
    Assert.assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueNumberWithInvalidValue() throws IOException {
    jsonTreeWriter.value(Double.NaN);
  }

  @Test
  public void testFlush() throws IOException {
    // Checking that flush does not throw an exception
    jsonTreeWriter.flush();
  }

  @Test(expected = IOException.class)
  public void testCloseWithNonEmptyStack() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.close();
  }

  @Test
  public void testClose() throws IOException {
    jsonTreeWriter.close();
    JsonElement expected = new JsonPrimitive("closed");
    JsonElement actual = jsonTreeWriter.peek();
    Assert.assertEquals(expected, actual);
  }
}