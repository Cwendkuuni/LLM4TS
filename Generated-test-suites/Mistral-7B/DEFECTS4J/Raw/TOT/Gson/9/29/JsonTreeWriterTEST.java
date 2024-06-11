import static org.junit.Assert.*;
import static com.google.gson.internal.bind.JsonTreeWriter.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testEmptyJsonWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement element = writer.get();
    assertEquals(JsonNull.INSTANCE, element);
  }

  @Test
  public void testJsonWriterArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value(1);
    writer.value(2);
    JsonArray array = writer.get();
    JsonElement firstElement = array.get(0);
    JsonElement secondElement = array.get(1);
    assertEquals(JsonPrimitive.createNumber(1), firstElement);
    assertEquals(JsonPrimitive.createNumber(2), secondElement);
  }

  @Test
  public void testJsonWriterNestedArray() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.beginArray();
    writer.value(1);
    writer.value(2);
    writer.endArray();
    writer.value(3);
    writer.endArray();
    JsonArray topLevelArray = writer.get();
    JsonArray nestedArray = (JsonArray) topLevelArray.get(0);
    JsonElement firstElement = nestedArray.get(0);
    JsonElement secondElement = nestedArray.get(1);
    assertEquals(JsonPrimitive.createNumber(1), firstElement);
    assertEquals(JsonPrimitive.createNumber(2), secondElement);
    JsonElement thirdElement = topLevelArray.get(1);
    assertEquals(JsonPrimitive.createNumber(3), thirdElement);
  }

  @Test
  public void testJsonWriterObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(30);
    writer.name("city").value("New York");
    writer.endObject();
    JsonObject object = writer.get();
    JsonElement name = object.get("name");
    JsonElement age = object.get("age");
    JsonElement city = object.get("city");
    assertEquals(JsonPrimitive.createString("John Doe"), name);
    assertEquals(JsonPrimitive.createNumber(30), age);
    assertEquals(JsonPrimitive.createString("New York"), city);
  }

  @Test
  public void testJsonWriterNestedObject() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("person").beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(30);
    writer.endObject();
    writer.name("city").value("New York");
    writer.endObject();
    JsonObject topLevelObject = writer.get();
    JsonObject nestedObject = (JsonObject) topLevelObject.get("person");
    JsonElement name = nestedObject.get("name");
    JsonElement age = nestedObject.get("age");
    assertEquals(JsonPrimitive.createString("John Doe"), name);
    assertEquals(JsonPrimitive.createNumber(30), age);
    JsonElement city = topLevelObject.get("city");
    assertEquals(JsonPrimitive.createString("New York"), city);
  }

  @Test
  public void testJsonWriterErrorCases() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("array");
    writer.beginArray();
    writer.value("test"); // expected JsonPrimitive but got JsonPrimitive with incorrect type
    writer.endArray();
    writer.endObject();
    JsonElement element = writer.get();
    assertTrue(element instanceof JsonObject);
    JsonObject jsonObject = (JsonObject) element;
    JsonElement array = jsonObject.get("array");
    assertTrue(array instanceof JsonArray);
    JsonArray jsonArray = (JsonArray) array;
    JsonElement test = jsonArray.get(0);
    assertFalse(test instanceof JsonPrimitive);
    assertFalse(test.isJsonNull());
  }
}