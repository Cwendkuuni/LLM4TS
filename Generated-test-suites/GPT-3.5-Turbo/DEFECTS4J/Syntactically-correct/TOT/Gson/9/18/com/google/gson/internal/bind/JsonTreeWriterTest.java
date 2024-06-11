import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;

public class JsonTreeWriterTest {

  @Test
  public void testValue_Null() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value((String) null);
    JsonObject json = writer.get().getAsJsonObject();
    assertNotNull(json);
    assertNull(json.get("value"));
  }

  @Test
  public void testValue_String() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("testString");
    JsonObject json = writer.get().getAsJsonObject();
    assertNotNull(json);
    assertEquals("testString", json.get("value").getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testName_OutsideObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.name("testName");
  }

  @Test
  public void testName_InsideObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("testName");
    JsonObject json = writer.get().getAsJsonObject();
    assertNotNull(json);
    assertNotNull(json.get("testName"));
  }

  @Test(expected = IllegalStateException.class)
  public void testName_NestedObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("object1");
    writer.beginObject();
    writer.name("object2");
  }

  @Test(expected = IllegalStateException.class)
  public void testName_MultipleNames() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("testName1");
    writer.name("testName2");
  }

  @Test(expected = IllegalStateException.class)
  public void testValue_OutsideObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("testValue");
  }

  @Test
  public void testValue_InsideObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("testValue");
    writer.value("value1");
    JsonObject json = writer.get().getAsJsonObject();
    assertNotNull(json.get("testValue"));
    assertEquals("value1", json.get("testValue").getAsString());
  }

  @Test(expected = IllegalStateException.class)
  public void testValue_MultipleValues() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("testValue");
    writer.value("value1");
    writer.value("value2");
  }

  @Test
  public void testBeginObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    JsonObject json = writer.get().getAsJsonObject();
    assertNotNull(json);
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObject_OutsideObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObject_InsideValue() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("testName");
    writer.value("testValue");
    writer.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndObject_MultipleEnd() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.endObject();
    writer.endObject();
  }

  @Test
  public void testBeginArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    JsonArray array = writer.get().getAsJsonArray();
    assertNotNull(array);
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArray_OutsideArray() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArray_InsideValue() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("testValue");
    writer.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testEndArray_MultipleEnd() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.endArray();
    writer.endArray();
  }

  @Test
  public void testNullValue() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.nullValue();
    JsonObject json = writer.get().getAsJsonObject();
    assertNotNull(json);
    assertEquals(JsonPrimitive.Null, json.get("null"));
  }

  @Test
  public void testValue_Boolean() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(true);
    JsonObject json = writer.get().getAsJsonObject();
    assertNotNull(json);
    assertTrue(json.get("value").getAsBoolean());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValue_Double_NaN() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(Double.NaN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValue_Double_Infinity() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(Double.POSITIVE_INFINITY);
  }

  @Test(expected = IllegalStateException.class)
  public void testValue_Long_OutsideObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(1234L);
  }

  @Test(expected = IllegalStateException.class)
  public void testValue_Long_NestedObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("testName");
    writer.value(1234L);
  }

  @Test
  public void testValue_Long_InsideObject() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("testValue");
    writer.value(1234L);
    JsonObject json = writer.get().getAsJsonObject();
    assertNotNull(json.get("testValue"));
    assertEquals(1234L, json.get("testValue").getAsLong());
  }

  @Test
  public void testValue_Number() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(1234);
    JsonObject json = writer.get().getAsJsonObject();
    assertNotNull(json);
    assertEquals(1234, json.get("value").getAsInt());
  }

  @Test(expected = IllegalStateException.class)
  public void testFlush() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.flush();
  }

  @Test(expected = IOException.class)
  public void testClose_IncompleteDocument() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.close();
  }

  @Test
  public void testClose_CompleteDocument() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name1").value("value1");
    writer.endObject();
    writer.close();
    JsonObject json = writer.get().getAsJsonObject();
    assertNotNull(json);
    assertNotNull(json.get("name1"));
    assertEquals("value1", json.get("name1").getAsString());
  }

}