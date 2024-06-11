
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JsonTreeWriterTest {
  
  private JsonTreeWriter jsonTreeWriter;
  
  @Before
  public void setup() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
  public void testBeginArray() {
    jsonTreeWriter.beginArray();

    JsonElement result = jsonTreeWriter.get();
    Assert.assertTrue(result instanceof JsonArray);
  }

  @Test
  public void testEndArray() {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();

    JsonElement result = jsonTreeWriter.get();
    Assert.assertTrue(result.isJsonNull());
  }

  @Test
  public void testBeginObject() {
    jsonTreeWriter.beginObject();

    JsonElement result = jsonTreeWriter.get();
    Assert.assertTrue(result instanceof JsonObject);
  }

  @Test
  public void testEndObject() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();

    JsonElement result = jsonTreeWriter.get();
    Assert.assertTrue(result.isJsonNull());
  }

  @Test
  public void testName() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("testName");

    JsonElement result = jsonTreeWriter.get();
    Assert.assertTrue(result.isJsonNull());
  }

  @Test
  public void testValue_string() {
    String value = "testValue";
    jsonTreeWriter.value(value);

    JsonElement result = jsonTreeWriter.get();
    Assert.assertEquals(value, result.getAsString());
  }

  @Test
  public void testNullValue() {
    jsonTreeWriter.nullValue();

    JsonElement result = jsonTreeWriter.get();
    Assert.assertTrue(result.isJsonNull());
  }

  @Test
  public void testValue_boolean() {
    boolean value = true;
    jsonTreeWriter.value(value);

    JsonElement result = jsonTreeWriter.get();
    Assert.assertEquals(value, result.getAsBoolean());
  }

  @Test
  public void testValue_double() {
    double value = 3.14;
    jsonTreeWriter.value(value);

    JsonElement result = jsonTreeWriter.get();
    Assert.assertEquals(value, result.getAsDouble(), 0.0001);
  }

  @Test
  public void testValue_long() {
    long value = 12345;
    jsonTreeWriter.value(value);

    JsonElement result = jsonTreeWriter.get();
    Assert.assertEquals(value, result.getAsLong());
  }

  @Test
  public void testValue_Number() {
    Number value = 3.14;
    jsonTreeWriter.value(value);

    JsonElement result = jsonTreeWriter.get();
    Assert.assertEquals(value, result.getAsNumber());
  }
}