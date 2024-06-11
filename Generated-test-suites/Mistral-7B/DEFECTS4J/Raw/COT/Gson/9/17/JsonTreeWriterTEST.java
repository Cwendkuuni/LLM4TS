import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testGet() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("value");
    JsonElement result = writer.get();
    JsonObject expected = new JsonObject();
    expected.add("name", new JsonPrimitive("value"));
    assertEquals(expected, result);
  }

  @Test
  public void testBeginArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value("element1");
    writer.endArray();
    JsonArray result = (JsonArray) writer.get();
    JsonArray expected = new JsonArray();
    expected.add(new JsonPrimitive("element1"));
    assertEquals(expected, result);
  }

  @Test
  public void testBeginObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name1").value("value1");
    writer.name("name2").value("value2");
    writer.endObject();
    JsonObject result = (JsonObject) writer.get();
    JsonObject expected = new JsonObject();
    expected.add("name1", new JsonPrimitive("value1"));
    expected.add("name2", new JsonPrimitive("value2"));
    assertEquals(expected, result);
  }

  @Test
  public void testEndArrayWhenNotStarted() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.endArray();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // expected
    }
  }

  @Test
  public void testEndObjectWhenNotStarted() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.endObject();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // expected
    }
  }

  @Test
  public void testNameWhenNotObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    try {
      writer.beginArray();
      writer.name("name");
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // expected
    }
  }

  @Test
  public void testNullValue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.nullValue();
    JsonElement result = writer.get();
    assertEquals(JsonNull.INSTANCE, result);
  }

  @Test
  public void testValueString() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("stringValue");
    JsonElement result = writer.get();
    assertEquals(new JsonPrimitive("stringValue"), result);
  }

  @Test
  public void testValueBooleanTrue() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(true);
    JsonElement result = writer.get();
    assertEquals(new JsonPrimitive(true), result);
  }

  @Test
  public void testValueBooleanFalse() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(false);
    JsonElement result = writer.get();
    assertEquals(new JsonPrimitive(false), result);
  }

  @Test
  public void testValueNumberInt() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(42);
    JsonElement result = writer.get();
    assertEquals(new JsonPrimitive(42), result);
  }

  @Test
  public void testValueNumberFloat() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(3.14159265f);
    JsonElement result = writer.get();
    assertEquals(new JsonPrimitive(3.14159265f), result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueNaN() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(Double.NaN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValueInfinity() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value(Double.POSITIVE_INFINITY);
  }

  @Test(expected = IllegalStateException.class)
  public void testCloseWhenNotEmpty() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.close();
  }
}