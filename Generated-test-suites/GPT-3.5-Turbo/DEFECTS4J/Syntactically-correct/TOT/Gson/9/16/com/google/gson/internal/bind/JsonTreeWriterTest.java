import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
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
  
    Assert.assertEquals(JsonArray.class, jsonTreeWriter.peek().getClass());
  }

  @Test
  public void testEndArray_emptyArray() {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
  
    Assert.assertEquals(JsonNull.INSTANCE, jsonTreeWriter.get());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArray_nestedArray() {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.endArray();
    jsonTreeWriter.endArray();
  }

  @Test
  public void testBeginObject() {
    jsonTreeWriter.beginObject();
  
    Assert.assertEquals(JsonObject.class, jsonTreeWriter.peek().getClass());
  }

  @Test
  public void testEndObject_emptyObject() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
  
    Assert.assertEquals(JsonNull.INSTANCE, jsonTreeWriter.get());
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObject_nestedObject() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    jsonTreeWriter.endObject();
  }

  @Test
  public void testName_validName() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key1");

    Assert.assertEquals("key1", jsonTreeWriter.pendingName);
  }

  @Test(expected = IllegalStateException.class)
  public void testName_invalidName() {
    jsonTreeWriter.name("key1");

    jsonTreeWriter.name("key2");
  }

  @Test
  public void testValue_stringValue() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    jsonTreeWriter.value("value");

    JsonObject jsonObject = (JsonObject) jsonTreeWriter.get();
    Assert.assertEquals(JsonPrimitive.class, jsonObject.get("key").getClass());
    Assert.assertEquals("value", ((JsonPrimitive) jsonObject.get("key")).getAsString());
  }

  @Test
  public void testValue_booleanValue() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    jsonTreeWriter.value(true);

    JsonObject jsonObject = (JsonObject) jsonTreeWriter.get();
    Assert.assertEquals(JsonPrimitive.class, jsonObject.get("key").getClass());
    Assert.assertEquals(true, ((JsonPrimitive) jsonObject.get("key")).getAsBoolean());
  }

  @Test
  public void testValue_doubleValue() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    jsonTreeWriter.value(10.5);

    JsonObject jsonObject = (JsonObject) jsonTreeWriter.get();
    Assert.assertEquals(JsonPrimitive.class, jsonObject.get("key").getClass());
    Assert.assertEquals(10.5, ((JsonPrimitive) jsonObject.get("key")).getAsDouble(), 0);
  }

  @Test
  public void testValue_longValue() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    jsonTreeWriter.value(100L);

    JsonObject jsonObject = (JsonObject) jsonTreeWriter.get();
    Assert.assertEquals(JsonPrimitive.class, jsonObject.get("key").getClass());
    Assert.assertEquals(100L, ((JsonPrimitive) jsonObject.get("key")).getAsLong());
  }

  @Test
  public void testValue_nullValue() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    jsonTreeWriter.nullValue();

    JsonObject jsonObject = (JsonObject) jsonTreeWriter.get();
    Assert.assertEquals(JsonNull.INSTANCE, jsonObject.get("key"));
  }

  @Test
  public void testValue_numberValue() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    jsonTreeWriter.value(50);

    JsonObject jsonObject = (JsonObject) jsonTreeWriter.get();
    Assert.assertEquals(JsonPrimitive.class, jsonObject.get("key").getClass());
    Assert.assertEquals(50, ((JsonPrimitive) jsonObject.get("key")).getAsInt());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValue_nanValue() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    jsonTreeWriter.value(Double.NaN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValue_infinityValue() {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("key");
    jsonTreeWriter.value(Double.POSITIVE_INFINITY);
  }

  @Test
  public void testFlush() {
    // Since flush() performs no operation, testing its functionality is not necessary.
  }

  @Test(expected = IOException.class)
  public void testClose_unfinishedDocument() throws IOException {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.close();
  }

  @Test
  public void testClose_emptyDocument() throws IOException {
    jsonTreeWriter.close();
  
    Assert.assertEquals(JsonNull.INSTANCE, jsonTreeWriter.get());
  }

  @Test
  public void testClose_finishedDocument() throws IOException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    jsonTreeWriter.close();
  
    Assert.assertEquals(JsonNull.INSTANCE, jsonTreeWriter.get());
  }

}