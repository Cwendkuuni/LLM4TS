import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class JsonTreeReaderTest {

  @Test
  public void testJsonTreeReaderEmptyJson() throws IOException {
    JsonReader jsonTreeReader = new JsonTreeReader(JsonParser.parseReader(""));
    assertFalse("JsonTreeReader should return false when hasNext is called on empty reader", jsonTreeReader.hasNext());
  }

  @Test
  public void testJsonTreeReaderValidJson() throws IOException {
    String jsonString = "{\"name\":\"John\",\"age\":30,\"cars\":[\"Ford\",\"BMW\",\"Fiat\"]}";
    JsonReader jsonTreeReader = new JsonTreeReader(JsonParser.parseReader(jsonString));

    // Test name
    assertEquals("name", jsonTreeReader.nextName());
    // Test string value
    assertEquals("John", jsonTreeReader.nextString());
    // Test age
    assertEquals(30, jsonTreeReader.nextInt());
    // Test cars array
    jsonTreeReader.beginArray();
    String carName = jsonTreeReader.nextString();
    assertEquals("Ford", carName);
    carName = jsonTreeReader.nextString();
    assertEquals("BMW", carName);
    carName = jsonTreeReader.nextString();
    assertEquals("Fiat", carName);
    jsonTreeReader.endArray();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeReaderClosedReader() throws IOException {
    JsonReader jsonTreeReader = new JsonTreeReader(JsonParser.parseReader(""));
    jsonTreeReader.close();
    jsonTreeReader.nextString();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testJsonTreeReaderNullJson() throws IOException {
    JsonReader jsonTreeReader = new JsonTreeReader(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testJsonTreeReaderEmptyJsonElement() throws IOException {
    JsonReader jsonTreeReader = new JsonTreeReader(JsonParser.parseReader(""));;
    jsonTreeReader.nextString();
    jsonTreeReader.nextNull();
    JsonElement jsonElement = jsonTreeReader.peek();
    jsonTreeReader = new JsonTreeReader(jsonElement);
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeReaderEndOfJson() throws IOException {
    String jsonString = "{\"name\":\"John\",\"age\":30,\"cars\":[\"Ford\",\"BMW\",\"Fiat\"]}";
    JsonReader jsonTreeReader = new JsonTreeReader(JsonParser.parseReader(jsonString));

    jsonTree.beginObject(); // Should throw an exception
    jsonTreeReader.nextName();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeReaderEmptyJsonArray() throws IOException {
    String jsonString = "{\"name\":\"John\",\"age\":30}";
    JsonReader jsonTreeReader = new JsonTreeReader(JsonParser.parseReader(jsonString));

    jsonTreeReader.beginArray();
    jsonTreeReader.nextString(); // Should throw an exception
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeReaderEmptyJsonObject() throws IOException {
    String jsonString = "[1, 2, 3]";
    JsonReader jsonTreeReader = new JsonTreeReader(JsonParser.parseReader(jsonString));

    jsonTreeReader.beginObject(); // Should throw an exception
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonTreeReaderInvalidJson() throws IOException {
    String jsonString = "{\"name\":\"John\"age\":30,\"cars\":[\"Ford\",\"BMW\",\"Fiat\"]}";
    JsonReader jsonTreeReader = new JsonTreeReader(JsonParser.parseReader(jsonString));

    jsonTreeReader.nextName();
    jsonTreeReader.nextInt(); // Should throw an exception
  }
}