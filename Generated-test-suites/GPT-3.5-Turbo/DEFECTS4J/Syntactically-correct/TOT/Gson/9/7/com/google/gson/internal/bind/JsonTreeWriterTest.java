import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class JsonTreeWriterTest {

  private JsonTreeWriter jsonTreeWriter;

  @Before
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
  public void testBeginArray() throws IOException {
    jsonTreeWriter.beginArray();
    
    JsonElement result = jsonTreeWriter.get();
    assertTrue(result instanceof JsonArray);
  }

  @Test
  public void testEndArray() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();

    JsonElement result = jsonTreeWriter.get();
    assertNull(result);
  }

  @Test
  public void testBeginObject() throws IOException {
    jsonTreeWriter.beginObject();

    JsonElement result = jsonTreeWriter.get();
    assertTrue(result instanceof JsonObject);
  }

  @Test
  public void testEndObject() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();

    JsonElement result = jsonTreeWriter.get();
    assertNull(result);
  }

  @Test
  public void testName() throws IOException {
    String name = "testName";
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name(name);

    JsonElement result = jsonTreeWriter.get();
    assertTrue(result instanceof JsonObject);
    JsonObject jsonObject = (JsonObject) result;
    assertTrue(jsonObject.has(name));
  }

  @Test
  public void testValue_String() throws IOException {
    String value = "testValue";
    jsonTreeWriter.beginObject();
    jsonTreeWriter.value(value);

    JsonElement result = jsonTreeWriter.get();
    assertTrue(result instanceof JsonObject);
    JsonObject jsonObject = (JsonObject) result;
    assertTrue(jsonObject.has(value));
  }

  @Test
  public void testNullValue() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.nullValue();

    JsonElement result = jsonTreeWriter.get();
    assertTrue(result instanceof JsonObject);
    JsonObject jsonObject = (JsonObject) result;
    assertTrue(jsonObject.has("null"));
    assertTrue(jsonObject.get("null").isJsonNull());
  }

  @Test
  public void testValue_Boolean() throws IOException {
    boolean value = true;
    jsonTreeWriter.beginObject();
    jsonTreeWriter.value(value);

    JsonElement result = jsonTreeWriter.get();
    assertTrue(result instanceof JsonObject);
    JsonObject jsonObject = (JsonObject) result;
    assertTrue(jsonObject.has(String.valueOf(value)));
    assertTrue(jsonObject.get(String.valueOf(value)).getAsBoolean());
  }

  @Test
  public void testValue_Double() throws IOException {
    double value = 3.14;
    jsonTreeWriter.beginObject();
    jsonTreeWriter.value(value);

    JsonElement result = jsonTreeWriter.get();
    assertTrue(result instanceof JsonObject);
    JsonObject jsonObject = (JsonObject) result;
    assertTrue(jsonObject.has(String.valueOf(value)));
    assertEquals(jsonObject.get(String.valueOf(value)).getAsDouble(), value, 0.0);
  }

  @Test
  public void testValue_Long() throws IOException {
    long value = 123456;
    jsonTreeWriter.beginObject();
    jsonTreeWriter.value(value);

    JsonElement result = jsonTreeWriter.get();
    assertTrue(result instanceof JsonObject);
    JsonObject jsonObject = (JsonObject) result;
    assertTrue(jsonObject.has(String.valueOf(value)));
    assertEquals(jsonObject.get(String.valueOf(value)).getAsLong(), value);
  }

  @Test
  public void testValue_Number() throws IOException {
    int value = 42;
    Number number = value;
    jsonTreeWriter.beginObject();
    jsonTreeWriter.value(number);

    JsonElement result = jsonTreeWriter.get();
    assertTrue(result instanceof JsonObject);
    JsonObject jsonObject = (JsonObject) result;
    assertTrue(jsonObject.has(String.valueOf(value)));
    assertEquals(jsonObject.get(String.valueOf(value)).getAsInt(), value);
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalStateException_endArray() throws IOException {
    jsonTreeWriter.endArray();
  }
  
  @Test(expected = IllegalStateException.class)
  public void testIllegalStateException_endObject() throws IOException {
    jsonTreeWriter.endObject();
  }
  
  @Test(expected = IllegalStateException.class)
  public void testIllegalStateException_value_withoutObject() throws IOException {
    jsonTreeWriter.value("testValue");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalArgumentException_value_DoubleWithNaN() throws IOException {
    double value = Double.NaN;
    jsonTreeWriter.beginObject();
    jsonTreeWriter.value(value);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalArgumentException_value_DoubleWithInfinite() throws IOException {
    double value = Double.POSITIVE_INFINITY;
    jsonTreeWriter.beginObject();
    jsonTreeWriter.value(value);
  }

  @Test(expected = IOException.class)
  public void testIOException_closeWithoutEndObject() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.close();
  }

  @Test
  public void testClose() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.value("testValue");
    jsonTreeWriter.close();

    JsonElement result = jsonTreeWriter.get();
    assertNull(result);
  }

}