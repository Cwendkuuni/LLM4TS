import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testJsonTreeWriter_Constructor() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    assertNull(jsonTreeWriter.get());
  }

  @Test
  public void testBeginArray() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    JsonArray array = jsonTreeWriter.get();
    assertNotNull(array);
    jsonTreeWriter.endArray();
    assertNull(jsonTreeWriter.get());
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    JsonObject object = jsonTreeWriter.get();
    assertNotNull(object);
    jsonTreeWriter.endObject();
    assertNull(jsonTreeWriter.get());
  }

  @Test
  public void testName() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("test");
    JsonObject object = jsonTreeWriter.get();
    assertNotNull(object);
    assertEquals("test", object.name("test").getAsString());
    jsonTreeWriter.endObject();
    assertNull(jsonTreeWriter.get());
  }

  @Test
  public void testValueString() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("test").value("testString");
    JsonObject object = jsonTreeWriter.get();
    assertNotNull(object);
    assertEquals("testString", object.get("test").getAsString());
    jsonTreeWriter.endObject();
    assertNull(jsonTreeWriter.get());
  }

  @Test
  public void testValueBoolean() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("test").value(true);
    JsonObject object = jsonTreeWriter.get();
    assertNotNull(object);
    assertTrue(object.get("test").getAsBoolean());
    jsonTreeWriter.endObject();
    assertNull(jsonTreeWriter.get());
  }

  @Test
  public void testValueNumber() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("test").value(123);
    JsonObject object = jsonTreeWriter.get();
    assertNotNull(object);
    assertEquals(123, object.get("test").getAsInt());
    jsonTreeWriter.endObject();
    assertNull(jsonTreeWriter.get());
  }

  @Test
  public void testValueNull() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.name("test").nullValue();
    JsonObject object = jsonTreeWriter.get();
    assertNotNull(object);
    assertNull(object.get("test"));
    jsonTreeWriter.endObject();
    assertNull(jsonTreeWriter.get());
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayAfterBeginObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectAfterBeginArray() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginArray();
    jsonTreeWriter.beginObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testNameAfterEndObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    jsonTreeWriter.name("test");
  }

  @Test(expected = IllegalStateException.class)
  public void testValueAfterEndObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    jsonTreeWriter.value("test");
  }

  @Test(expected = IllegalStateException.class)
  public void testCloseWriterBeforeEndObject() {
    JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();jsonTreeWriter.beginObject();jsonTreeWriter.close();
  }
}