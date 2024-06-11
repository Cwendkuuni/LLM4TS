import static org.junit.Assert.*;
import static com.google.gson.stream.JsonReader.LengthReader;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testEmptyJson() throws IOException {
    String json = "{}";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {});
    reader.setLenient(true);
    reader.open(new StringReader(json));

    assertTrue(reader.hasNext());
    reader.close();
  }

  @Test
  public void testSimpleObject() throws IOException {
    String json = "{\"name\": \"John\", \"age\": 30}";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John");
        jsonObject.addProperty("age", 30);
        this.value = jsonObject;
      }
    });
    reader.setLenient(true);
    reader.open(new StringReader(json));

    assertEquals("name", reader.nextName());
    assertEquals("John", reader.nextString());
    assertEquals("age", reader.nextName());
    assertEquals(30, reader.nextInt());
    reader.nextNull();
    reader.close();
  }

  @Test
  public void testSimpleArray() throws IOException {
    String json = "[1, 2, 3]";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(1);
        jsonArray.add(2);
        jsonArray.add(3);
        this.value = jsonArray;
      }
    });
    reader.setLenient(true);
    reader.open(new StringReader(json));

    reader.beginArray();
    assertEquals(1, reader.nextInt());
    assertEquals(2, reader.nextInt());
    assertEquals(3, reader.nextInt());
    reader.endArray();
    reader.close();
  }

  @Test
  public void testNestedObjects() throws IOException {
    String json = "{\"name\": {\"first\": \"John\", \"last\": \"Doe\"}}";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      {
        JsonObject jsonObject = new JsonObject();
        JsonObject nestedObject = new JsonObject();
        nestedObject.addProperty("first", "John");
        nestedObject.addProperty("last", "Doe");
        jsonObject.add("name", nestedObject);
        this.value = jsonObject;
      }
    });
    reader.setLenient(true);
    reader.open(new StringReader(json));

    assertEquals("name", reader.nextName());
    reader.beginObject();
    assertEquals("first", reader.nextName());
    assertEquals("John", reader.nextString());
    reader.endObject();
    reader.endObject();
    reader.close();
  }

  @Test
  public void testNestedArrays() throws IOException {
    String json = "[1, [2, [3, [4]]]]";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      {
        JsonArray jsonArray = new JsonArray();
        JsonArray firstArray = new JsonArray();
        JsonArray secondArray = new JsonArray();
        JsonArray thirdArray = new JsonArray();
        thirdArray.add(new JsonPrimitive(4));
        secondArray.add(thirdArray);
        firstArray.add(secondArray);
        jsonArray.add(new JsonPrimitive(1));
        jsonArray.add(firstArray);
        this.value = jsonArray;
      }
    });
    reader.setLenient(true);
    reader.open(new StringReader(json));

    reader.beginArray();
    assertEquals(1, reader.nextInt());
    reader.beginArray();
    reader.beginArray();
    assertEquals(4, reader.nextInt());
    reader.endArray();
    reader.endArray();
    reader.endArray();
    reader.close();
  }

  @Test
  public void testNullValues() throws IOException {
    String json = "{\"name\": null, \"age\": null}";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", null);
        jsonObject.addProperty("age", null);
        this.value = jsonObject;
      }
    });
    reader.setLenient(true);
    reader.open(new StringReader(json));

    assertEquals("name", reader.nextName());
    assertNull(reader.nextString());
    assertEquals("age", reader.nextName());
    assertNull(reader.nextNull());
    reader.close();
  }

  @Test
  public void testEmptyArrays() throws IOException {
    String json = "[{}, {}]";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      {
        JsonArray jsonArray = new JsonArray();
        JsonElement emptyElement = JsonNull.INSTANCE;
        jsonArray.add(emptyElement);
        jsonArray.add(emptyElement);
        this.value = jsonArray;
      }
    });
    reader.setLenient(true);
    reader.open(new StringReader(json));

    reader.beginArray();
    reader.endArray();
    reader.beginArray();
    reader.endArray();
    reader.close();
  }

  @Test(expected = AssertionError.class)
  public void testMalformedJson() throws IOException {
    String json = "{name: 1}"; // missing quotes in key
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {});
    reader.setLenient(true);
    reader.open(new StringReader(json));
    reader.nextName(); // will fail as it encounters 'name' without quotes
  }

  @Test(expected = AssertionError.class)
  public void testMalformedJsonArray() throws IOException {
    String json = "[1, 2[3]"; // missing ']' in second element
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {});
    reader.setLenient(true);
    reader.open(new StringReader(json));
    reader.beginArray();
    reader.nextInt(); // no problem with first element
    reader.beginArray(); // starts parsing second element
    reader.nextInt(); // reads first number
    reader.nextInt(); // fails as it encounters '[' instead of ']'
  }

  @Test
  public void testSkipValue() throws IOException {
    String json = "{\"name\": null}";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", null);
        this.value = jsonObject;
      }
    });
    reader.setLenient(true);
    reader.open(new StringReader(json));

    reader.nextName();
    reader.skipValue();
    reader.nextNull();
    reader.close();
  }

  @Test
  public void testPromoteNameToValue() throws IOException {
    String json = "{\"name\": null, \"age\": 30, \"status\": status}";
    JsonTreeReader reader = new JsonTreeReader(new JsonElement() {
      {
        JsonObject jsonObject = new JsonObject();
        JsonObject nestedObject = new JsonObject();
        nestedObject.addProperty("status", null);
        jsonObject.add("status", nestedObject);
        jsonObject.addProperty("name", null);
        jsonObject.addProperty("age", 30);
        this.value = jsonObject;
      }
    });
    reader.setLenient(true);
    reader.open(new StringReader(json));

    reader.promoteNameToValue();
    assertEquals(null, reader.nextNull());
    reader.nextInt();
    reader.close();
  }
}