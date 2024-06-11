import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testGetReturnsTopLevelJsonElement() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement jsonElement = writer.beginObject().name("name").value("John").endObject().get();
    assertEquals(JsonPrimitive.createString("John"), jsonElement);
  }

  @Test
  public void testBeginArrayAddElementAndEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonArray jsonArray = writer.beginArray().value(1).value(2).endArray();
    assertEquals(2, jsonArray.size());
    assertEquals(JsonPrimitive.createNumber(1), jsonArray.get(0));
    assertEquals(JsonPrimitive.createNumber(2), jsonArray.get(1));
  }

  @Test
  public void testBeginObjectAddNameValuePairAndEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonObject jsonObject = writer.beginObject().name("name").value("John").name("age").value(30).endObject();
    assertEquals("name", jsonObject.keySet().iterator().next());
    assertEquals("John", jsonObject.get("name"));
    assertEquals(JsonPrimitive.createNumber(30), jsonObject.get("age"));
  }

  @Test
  public void testBeginObjectWithPendingNameAndEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().name("name");
    JsonObject jsonObject = writer.endObject();
    assertNull(jsonObject.get("name"));
  }

  @Test
  public void testValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement jsonElement = writer.beginObject().name("name").value("John").endObject();
    assertEquals(JsonPrimitive.createString("John"), jsonElement.get("name"));
  }

  @Test
  public void testValueBoolean() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement jsonElement = writer.beginObject().name("isLoggedIn").value(true).endObject();
    assertEquals(JsonPrimitive.createBoolean(true), jsonElement.get("isLoggedIn"));
  }

  @Test
  public void testValueNumber() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement jsonElement = writer.beginObject().name("age").value(30).endObject();
    assertEquals(JsonPrimitive.createNumber(30), jsonElement.get("age"));
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginArrayAfterEndArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray().endArray().beginArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArrayAfterBeginArrayButBeforeName() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray().beginObject().endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testBeginObjectAfterEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().endObject().beginObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testNameAfterEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().endObject().name("name");
  }

  @Test(expected = IllegalStateException.class)
  public void testValueAfterEndObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject().endObject().value("name");
  }

  @Test
  public void testValueNull() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonObject jsonObject = writer.beginObject().name("isLoggedIn").value(null).endObject();
    assertNull(jsonObject.get("isLoggedIn"));
  }

  @Test
  public void testCloseWriter() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement jsonElement = writer.beginObject().name("name").value("John").endObject().get();
    writer.close();
    assertThrows(IOException.class, () -> writer.get());
  }

  @Test
  public void testCloseWriterWithContent() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement jsonElement = writer.beginObject().name("name").value("John").endObject().get();
    writer.close();
    assertEquals(JsonPrimitive.createString("John"), jsonElement);
  }
}