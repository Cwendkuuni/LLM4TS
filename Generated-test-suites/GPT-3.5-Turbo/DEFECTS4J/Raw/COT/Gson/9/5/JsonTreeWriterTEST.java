import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class JsonTreeWriterTest {

  private JsonTreeWriter jsonTreeWriter;

  @Before
  public void setup() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
  public void testBeginArray() throws IOException {
    JsonWriter jsonWriter = jsonTreeWriter.beginArray();
    assertEquals(jsonTreeWriter.peek().getClass(), JsonArray.class);
  }

  @Test
  public void testEndArray() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
    assertTrue(jsonTreeWriter.stack.isEmpty());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWithoutBeginArray() throws IOException {
    jsonTreeWriter.endArray();
  }

  @Test
  public void testBeginObject() throws IOException {
    JsonWriter jsonWriter = jsonTreeWriter.beginObject();
    assertEquals(jsonTreeWriter.peek().getClass(), JsonObject.class);
  }

  @Test
  public void testEndObject() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    assertTrue(jsonTreeWriter.stack.isEmpty());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWithoutBeginObject() throws IOException {
    jsonTreeWriter.endObject();
  }

  @Test
  public void testName() throws IOException {
    jsonTreeWriter.beginObject();
    JsonWriter jsonWriter = jsonTreeWriter.name("test");
    assertEquals(jsonWriter, jsonTreeWriter);
  }

  @Test(expected = IllegalStateException.class)
  public void testNameWithoutBeginObject() throws IOException {
    jsonTreeWriter.name("test");
  }

  @Test(expected = IllegalStateException.class)
  public void testNameCalledTwiceWithoutValue() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("test");
    jsonTreeWriter.name("test2");
  }

  @Test
  public void testValueString() throws IOException {
    jsonTreeWriter.value("test");
    assertEquals(jsonTreeWriter.peek().getClass(), JsonPrimitive.class);
  }

  @Test
  public void testValueNull() throws IOException {
    jsonTreeWriter.nullValue();
    assertTrue(jsonTreeWriter.peek().getClass().equals(JsonNull.class));
  }

  @Test
  public void testValueBoolean() throws IOException {
    jsonTreeWriter.value(true);
    assertEquals(jsonTreeWriter.peek().getClass(), JsonPrimitive.class);
  }

  @Test
  public void testValueDouble() throws IOException {
    jsonTreeWriter.value(1.0);
    assertEquals(jsonTreeWriter.peek().getClass(), JsonPrimitive.class);
  }

  @Test
  public void testValueLong() throws IOException {
    jsonTreeWriter.value(1L);
    assertEquals(jsonTreeWriter.peek().getClass(), JsonPrimitive.class);
  }

  @Test
  public void testValueNumber() throws IOException {
    jsonTreeWriter.value(1);
    assertEquals(jsonTreeWriter.peek().getClass(), JsonPrimitive.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueDoubleNaN() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.value(Double.NaN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueDoubleInfinite() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.value(Double.POSITIVE_INFINITY);
  }

  @Test
  public void testFlush() throws IOException {
    jsonTreeWriter.flush();
    // No assertion needed, just check that no exception is thrown
  }

  @Test(expected = IOException.class)
  public void testCloseIncompleteDocument() throws IOException {
    jsonTreeWriter.close();
  }
}