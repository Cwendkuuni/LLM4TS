import org.junit.Assert;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonTreeReaderTest {

  @Test
  public void testEmptyJson() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
    Assert.assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testSimpleObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{\"name\": \"John\", \"age\": 30}"));
    Assert.assertEquals("name", reader.nextName());
    Assert.assertEquals("John", reader.nextString());
    reader.nextName(); // "age"
    Assert.assertEquals(30, reader.nextInt());
    reader.nextName(); // null
    reader.nextNull();
    Assert.assertEquals("age", reader.nextName());
    reader.close();
  }

  @Test
  public void testEmptyArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[]"));
    Assert.assertFalse(reader.hasNext());
    reader.close();
  }

  @Test
  public void testSimpleArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[1, 2, 3]")); {
      reader.beginArray();
      Assert.assertEquals(1, reader.nextInt());
      Assert.assertEquals(2, reader.nextInt());
      Assert.assertEquals(3, reader.nextInt());
      reader.endArray();
    }
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testClosedReader() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{}"));
    reader.close();
    reader.nextInt();
  }

  @Test
  public void testNestedObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{" +
        "\"name\": {" +
        "\"first\": \"John\"," +
        "\"last\": \"Doe\"" +
        "}," +
        "\"age\": 30" +
        "}")); {
      reader.beginObject();
      reader.nextName(); // "name"
      reader.beginObject(); // begins name object
      reader.nextName(); // "first"
      Assert.assertEquals("John", reader.nextString());
      reader.nextName(); // "last"
      Assert.assertEquals("Doe", reader.nextString());
      reader.endObject(); // ends name object
      reader.nextName(); // "age"
      Assert.assertEquals(30, reader.nextInt());
      reader.endObject(); // ends main object
    }
    reader.close();
  }

  @Test
  public void testNestedArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("[" +
        "[1, 2, 3]," +
        "[4, 5]," +
        "[]]")); {
      reader.beginArray(); // begins outer array
      reader.beginArray(); // begins first inner array
      reader.beginArray(); // begins first inner array's first array
      Assert.assertEquals(1, reader.nextInt());
      Assert.assertEquals(2, reader.nextInt());
      Assert.assertEquals(3, reader.nextInt());
      reader.endArray(); // ends first inner array's first array
      reader.nextInt(); // 4
      reader.nextInt(); // 5
      reader.endArray(); // ends second inner array
      reader.beginArray(); // begins empty inner array
      reader.endArray(); // ends empty inner array
      reader.endArray(); // ends outer array
    }
    reader.close();
  }

  @Test
  public void testNullValues() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{" +
        "\"name\": null," +
        "\"age\": null" +
        "}")); {
      reader.beginObject();
      reader.nextName(); // "name"
      reader.nextNull();
      reader.nextName(); // "age"
      reader.nextNull();
      reader.endObject();
    }
    reader.close();
  }

  @Test
  public void testStringValues() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{" +
        "\"name\": \"John\"" +
        "}")); {
      reader.beginObject();
      reader.nextName(); // "name"
      Assert.assertEquals("John", reader.nextString());
      reader.endObject();
    }
    reader.close();
  }

  @Test
  public void testBooleanValues() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{" +
        "\"name\": false" +
        "}")); {
      reader.beginObject();
      reader.nextName(); // "name"
      Assert.assertFalse(reader.nextBoolean());
      reader.endObject();
    }
    reader.close();
  }

  @Test
  public void testNumberValues() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(JsonElement.parse("{" +
        "\"name\": 123" +
        "}")); {
      reader.beginObject();
      reader.nextName(); // "name"
      Assert.assertEquals(123, reader.nextInt());
      reader.endObject();
    }
    reader.close();

    reader = new JsonTreeReader(JsonElement.parse("{" +
        "\"name\": 123.45" +
        "}")); {
      reader.beginObject();
      reader.nextName(); // "name"
      Assert.assertEquals(123.45, reader.nextDouble(), 0.001);
      reader.endObject();
    }
    reader.close();
  }
}