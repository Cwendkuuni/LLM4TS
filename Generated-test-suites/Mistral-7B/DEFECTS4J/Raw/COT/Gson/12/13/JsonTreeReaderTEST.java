import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;

public class JsonTreeReaderTest {

  @Test
  public void testJsonTreeReaderWithEmptyJsonElement() {
    JsonElement emptyJsonElement = JsonParser.parseString("{}");
    JsonTreeReader jsonTreeReader = new JsonTreeReader(emptyJsonElement);
    assertFalse(jsonTreeReader.hasNext());
    jsonTreeReader.close();
  }

  @Test
  public void testJsonTreeReaderWithNullJsonElement() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(null);
    assertFalse(jsonTreeReader.hasNext());
    jsonTreeReader.close();
  }

  @Test
  public void testJsonTreeReaderWithSimpleJsonObject() {
    String jsonString = "{\"name\": \"John\", \"age\": 30, \"city\": \"New York\"}";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    assertEquals("name", jsonTreeReader.nextName());
    assertEquals("John", jsonTreeReader.nextString());
    jsonTreeReader.nextBoolean(); // expected but not present, should not throw an exception
    jsonTreeReader.nextNull(); // expected but not present, should not throw an exception
    jsonTreeReader.nextInt(); // expected but not present, should not throw an exception

    jsonTreeReader.expect(JsonToken.END_OBJECT);
    jsonTreeReader.close();
  }

  @Test
  public void testJsonTreeReaderWithSimpleJsonArray() {
    String jsonString = "[1, 2, 3]";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.expect(JsonToken.BEGIN_ARRAY);
    JsonElement element = jsonTreeReader.nextElement();
    assertEquals(1, element.getAsInt());
    element = jsonTreeReader.nextElement();
    assertEquals(2, element.getAsInt());
    element = jsonTreeReader.nextElement();
    assertEquals(3, element.getAsInt());

    jsonTreeReader.expect(JsonToken.END_ARRAY);
    jsonTreeReader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeReaderWithClosedReader() {
    JsonTreeReader jsonTreeReader = new JsonTreeReader(new JsonElement());
    jsonTreeReader.close();
    jsonTreeReader.nextName(); // should throw an exception
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeReaderWithNestedJsonArray() {
    String jsonString = "[1, [2, 3, 4], 5]]";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.expect(JsonToken.BEGIN_ARRAY);
    JsonElement array = jsonTreeReader.nextElement();
    assertEquals(1, array.getAsInt());

    jsonTreeReader.expect(JsonToken.BEGIN_ARRAY);
    JsonElement nestedArray = jsonTreeReader.nextElement();
    JsonTreeReader nestedJsonTreeReader = new JsonTreeReader(nestedArray);

    JsonToken nestedToken = nestedJsonTreeReader.peek();
    JsonElement nestedElement = null;
    switch (nestedToken) {
      case BEGIN_ARRAY:
        nestedElement = nestedJsonTreeReader.nextElement();
        break;
      case BEGIN_OBJECT:
        nestedElement = nestedJsonTreeReader.nextName();
        jsonTreeReader.promoteNameToValue();
        break;
      case STRING:
      case NUMBER:
      case BOOLEAN:
        nestedElement = nestedJsonTreeReader.nextString();
        break;
      case NULL:
        nestedElement = JsonNull.INSTANCE;
        break;
      case END_ARRAY:
      case END_OBJECT:
        nestedJsonTreeReader.close();
        jsonTreeReader.nextElement();
        break;
      default:
        fail("Unexpected token: " + nestedToken);
    }

    jsonTreeReader.expect(JsonToken.END_ARRAY);
    jsonTreeReader.close();

    assertEquals(2, nestedElement.getAsJsonArray().size());
  }
}