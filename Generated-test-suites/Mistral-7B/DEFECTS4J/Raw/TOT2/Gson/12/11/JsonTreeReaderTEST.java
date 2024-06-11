import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JsonTreeReaderTest {
  @Test
  public void testJsonTreeReader_BeginArray() throws IOException {
    String json = "[\n" +
        "  {\n" +
        "    \"name\": \"John\",\n" +
        "    \"age\": 30,\n" +
        "    \"cars\": [\n" +
        "      {\n" +
        "        \"brand\": \"Ford\",\n" +
        "        \"model\": \"Fiesta\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"brand\": \"Volvo\",\n" +
        "        \"model\": \"V70\"\n" +
        "      }\n" +
        "    ]\n" +
        "  },\n" +
        "  {\n" +
        "    \"name\": \"Jane\",\n" +
        "    \"age\": 28,\n" +
        "    \"cars\": []\n" +
        "  }\n" +
        "]";

    JsonElement jsonElement = JsonParser.parseString(json);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.beginArray();
    assertTrue(jsonTreeReader.hasNext());
    jsonTreeReader.beginObject();
    String name = jsonTreeReader.nextName();
    assertEquals("John", name);
    jsonTreeReader.nextBoolean(); // skip boolean value
    jsonTreeReader.beginArray();

    // Test for empty array
    jsonTreeReader.endArray();
    assertFalse(jsonTreeReader.hasNext());

    jsonTreeReader.beginObject();
    name = jsonTreeReader.nextName();
    assertEquals("Jane", name);
    jsonTreeReader.nextBoolean(); // skip boolean value
    jsonTreeReader.beginArray();

    // Test for non-empty array
    JsonTreeReader arrayIterator = (JsonTreeReader) jsonTreeReader.peekStack();
    JsonElement arrayElement = arrayIterator.next();
    JsonArray array = (JsonArray) arrayElement;
    int elementCount = array.size();
    for (int i = 0; i < elementCount; i++) {
      JsonElement jsonElement = array.get(i);
      JsonTreeReader objectReader = (JsonTreeReader) jsonTreeReader.peekStack();
      String brand = objectReader.nextName();
      assertEquals("Ford" /* or "Volvo" */ , brand);
      String model = objectReader.nextString();
      assertEquals("Fiesta" /* or "V70" */ , model);
      jsonTreeReader.nextBoolean(); // skip boolean value
    }

    jsonTreeReader.endArray();
    jsonTreeReader.endObject(); // End of the first object
    jsonTreeReader.endArray(); // End of the JSON array
    jsonTreeReader.close();
  }

  @Test
  public void testJsonTreeReader_BeginObject() throws IOException {
    String json = "{\n" +
        "  \"name\": \"John\",\n" +
        "  \"age\": 30,\n" +
        "  \"city\": null,\n" +
        "  \"hobbies\": [\n" +
        "    \"swimming\",\n" +
        "    \"reading\",\n" +
        "    null\n" +
        "  ],\n" +
        "  \"address\": {\n" +
        "    \"street\": \"Highway 7\",\n" +
        "    \"city\": \"Anytown\",\n" +
        "    \"state\": \"NY\",\n" +
        "    \"zip\": 12345\n" +
        "  }\n" +
        "}";

    JsonElement jsonElement = JsonParser.parseString(json);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    jsonTreeReader.beginObject();
    String name = jsonTreeReader.nextName();
    assertEquals("name", name);
    String value = jsonTreeReader.nextString();
    assertEquals("John", value);

    name = jsonTreeReader.nextName();
    assertEquals("age", name);
    int age = jsonTreeReader.nextInt();
    assertEquals(30, age);

    name = jsonTreeReader.nextName();
    assertEquals("city", name);
    jsonTreeReader.nextNull();

    name = jsonTreeReader.nextName();
    assertEquals("hobbies", name);
    jsonTreeReader.beginArray();

    // Test for empty array
    jsonTreeReader.endArray();
    assertFalse(jsonTreeReader.hasNext());

    jsonTreeReader.beginObject();
    name = jsonTreeReader.nextName();
    assertEquals("hobbies", name);
    jsonTreeReader.beginArray();

    // Test for non-empty array
    JsonTreeReader arrayIterator = (JsonTreeReader) jsonTreeReader.peekStack();
    JsonElement arrayElement = arrayIterator.next();
    JsonArray array = (JsonArray) arrayElement;
    int elementCount = array.size();
    for (int i = 0; i < elementCount; i++) {
      JsonElement jsonElement = array.get(i);
      String hobby = jsonTreeReader.nextString();
      assertEquals("swimming" /* or "reading" or null */ , hobby);
    }

    jsonTreeReader.endArray();
    jsonTreeReader.endObject(); // End of the object "hobbies"

    jsonTreeReader.beginObject();
    name = jsonTreeReader.nextName();
    assertEquals("address", name);
    jsonTreeReader.beginObject();

    // Test for nested object
    String street = jsonTreeReader.nextString();
    assertEquals("Highway 7", street);
    String city = jsonTreeReader.nextString();
    assertEquals("Anytown", city);
    String state = jsonTreeReader.nextString();
    assertEquals("NY", state);
    int zip = jsonTreeReader.nextInt();
    assertEquals(12345, zip);

    jsonTreeReader.endObject();
    jsonTreeReader.endObject(); // End of the object "address"
    jsonTreeReader.endObject(); // End of the JSON object
    jsonTreeReader.close();
  }

  @Test
  public void testJsonTreeReader_Expectations() throws IOException {
    String json = "{\"name\": \"John\", \"age\": 30, \"city\": \"New York\"}";
    JsonElement jsonElement = JsonParser.parseString(json);
    JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);

    try {
      jsonTreeReader.beginArray();
      fail("Expected BEGIN_OBJECT but was BEGIN_ARRAY");
    } catch (IllegalStateException e) {
      // Expected exception
    }

    try {
      jsonTreeReader.endObject();
      fail("Expected END_ARRAY but was END_OBJECT");
    } catch (IllegalStateException e) {
      // Expected exception
    }

    try {
      jsonTreeReader.beginArray();
      jsonTreeReader.nextString();
      fail("Expected STRING but was BEGIN_ARRAY");
    } catch (IllegalStateException e) {
      // Expected exception
    }

    try {
      jsonTreeReader.endArray();
      jsonTreeReader.nextInt();
      fail("Expected INT but was END_ARRAY");
    } catch (IllegalStateException e) {
      // Expected exception
    }

    try {
      jsonTreeReader.nextInt();
      jsonTreeReader.nextBoolean();
      fail("Expected NAME but was INT");
    } catch (IllegalStateException e) {
      // Expected exception
    }

    try {
      jsonTreeReader.beginObject();
      jsonTreeReader.nextInt();
      fail("Expected NAME but was BEGIN_OBJECT");
    } catch (IllegalStateException e) {
      // Expected exception
    }

    try {
      jsonTreeReader.endObject();
      jsonTreeReader.nextObject();
      fail("Expected END_ARRAY but was END_OBJECT");
    } catch (IllegalStateException e) {
      // Expected exception
    }

    jsonTreeReader.close();
  }
}