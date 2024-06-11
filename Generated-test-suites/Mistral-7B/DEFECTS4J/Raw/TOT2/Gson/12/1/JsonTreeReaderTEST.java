import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;

public class JsonTreeReaderTest {

  @Test
  public void testEmptyJsonObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    JsonElement element = reader.peek();
    reader.beginObject();
    assertEquals("null", reader.nextName());
    reader.nextNull();
    reader.endObject();
    reader.close();
  }

  @Test
  public void testSimpleJsonObject() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject().addProperty("name", "John Doe"));
    reader.beginObject();
    assertEquals("name", reader.nextName());
    assertEquals("John Doe", reader.nextString());
    reader.endObject();
    reader.close();
  }

  @Test
  public void testJsonObjectWithArray() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject().add("age", new JsonArray().add(25).add(30)));
    reader.beginObject();
    assertEquals("age", reader.nextName());
    reader.beginArray();
    assertEquals(25, reader.nextInt());
    reader.nextInt();
    reader.endArray();
    reader.endObject();
    reader.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testJsonReaderClosed() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonObject());
    reader.close();
    reader.nextString();
  }

  @Test(expected = AssertionError.class)
  public void testInvalidJsonReader() throws IOException {
    JsonTreeReader reader = new JsonTreeReader(new JsonElement());
    reader.nextInt();
  }
}