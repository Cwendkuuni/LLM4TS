import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeWriterTest {

  private JsonTreeWriter jsonTreeWriter;

  @Before
  public void setup() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
  public void testBeginArray() throws Exception {
    jsonTreeWriter.beginArray();
    JsonElement element = jsonTreeWriter.get();
    assertTrue(element instanceof JsonArray);
  }

  @Test
  public void testEndArray() throws Exception {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
    JsonElement element = jsonTreeWriter.get();
    assertTrue(element instanceof JsonNull);
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayWhenNotInArray() throws Exception {
    jsonTreeWriter.endArray();
  }

  @Test
  public void testBeginObject() throws Exception {
    jsonTreeWriter.beginObject();
    JsonElement element = jsonTreeWriter.get();
    assertTrue(element instanceof JsonObject);
  }

  @Test
  public void testEndObject() throws Exception {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    JsonElement element = jsonTreeWriter.get();
    assertTrue(element instanceof JsonNull);
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObjectWhenNotInObject() throws Exception {
    jsonTreeWriter.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayWhenInObject() throws Exception {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectWhenInArray() throws Exception {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.beginObject();
  }

  @Test
  public void testName() throws Exception {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    JsonElement element = jsonTreeWriter.get();
    assertTrue(element instanceof JsonObject);
    JsonObject object = (JsonObject) element;
    assertTrue(object.has("name"));
  }

  @Test(expected = IllegalStateException.class)
  public void testNameWhenNotInObject() throws Exception {
    jsonTreeWriter.name("name");
  }

  @Test(expected = IllegalStateException.class)
  public void testNameWhenAlreadyExists() throws Exception {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("name");
    jsonTreeWriter.name("name");
  }

  @Test
  public void testValueString() throws Exception {
    jsonTreeWriter.value("value");
    JsonElement element = jsonTreeWriter.get();
    assertTrue(element instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) element;
    assertEquals("value", primitive.getAsString());
  }

  @Test
  public void testValueStringNull() throws Exception {
    jsonTreeWriter.value((String) null);
    JsonElement element = jsonTreeWriter.get();
    assertTrue(element instanceof JsonNull);
  }

  @Test
  public void testValueBoolean() throws Exception {
    jsonTreeWriter.value(true);
    JsonElement element = jsonTreeWriter.get();
    assertTrue(element instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) element;
    assertTrue(primitive.getAsBoolean());
  }

  @Test
  public void testValueDouble() throws Exception {
    jsonTreeWriter.value(3.14);
    JsonElement element = jsonTreeWriter.get();
    assertTrue(element instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) element;
    assertEquals(3.14, primitive.getAsDouble(), 0.001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueDoubleNaN() throws Exception {
    jsonTreeWriter.value(Double.NaN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueDoublePositiveInfinity() throws Exception {
    jsonTreeWriter.value(Double.POSITIVE_INFINITY);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueDoubleNegativeInfinity() throws Exception {
    jsonTreeWriter.value(Double.NEGATIVE_INFINITY);
  }

  @Test
  public void testValueLong() throws Exception {
    jsonTreeWriter.value(42L);
    JsonElement element = jsonTreeWriter.get();
    assertTrue(element instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) element;
    assertEquals(42L, primitive.getAsLong());
  }

  @Test
  public void testValueNumber() throws Exception {
    jsonTreeWriter.value(3.14f);
    JsonElement element = jsonTreeWriter.get();
    assertTrue(element instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) element;
    assertEquals(3.14f, primitive.getAsFloat(), 0.001);
  }

  @Test
  public void testValueNumberNull() throws Exception {
    jsonTreeWriter.value((Number) null);
    JsonElement element = jsonTreeWriter.get();
    assertTrue(element instanceof JsonNull);
  }
}