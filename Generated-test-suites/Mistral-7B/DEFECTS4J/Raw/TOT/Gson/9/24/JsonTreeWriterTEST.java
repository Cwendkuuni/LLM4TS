import static org.junit.Assert.*;
import static com.google.gson.stream.CoercingJsonWriter.*;
import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import org.junit.Before;
import org.junit.Test;

public class JsonTreeWriterTest {

  private JsonTreeWriter writer;

  @Before
  public void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
  public void testEmptyJson() {
    writer.beginObject();
    writer.endObject();
    JsonElement result = writer.get();
    assertEquals("{}", result.toString());
  }

  @Test
  public void testSimpleJsonObject() {
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(30);
    writer.endObject();
    JsonElement result = writer.get();
    JsonObject jsonObject = result.getAsJsonObject();
    assertEquals("John Doe", jsonObject.get("name").getAsString());
    assertEquals(30, jsonObject.get("age").getAsInt());
  }

  @Test
  public void testArrayWithSingleElement() {
    writer.beginArray();
    writer.value("Hello");
    writer.endArray();
    JsonElement result = writer.get();
    JsonArray jsonArray = result.getAsJsonArray();
    assertEquals("[\n  \"Hello\"\n]", jsonArray.toString());
  }

  @Test
  public void testArrayWithMultipleElements() {
    writer.beginArray();
    writer.value("Hello");
    writer.value(1);
    writer.value("World");
    writer.endArray();
    JsonElement result = writer.get();
    JsonArray jsonArray = result.getAsJsonArray();
    assertEquals("[\n  \"Hello\"\n, \n  1\n, \n  \"World\"\n]", jsonArray.toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testWriteToClosedWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonElement result = writer.get();
    writer.close();
    writer.value("Hello");
  }

  @Test(expected = IllegalStateException.class)
  public void testWriteToEmptyStack() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.value("Hello");
  }

  @Test(expected = IllegalStateException.class)
  public void testWriteObjectAfterArray() {
    writer.beginArray();
    writer.value("Hello");
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.endObject();
  }

  @Test(expected = IllegalStateException.class)
  public void testWriteArrayAfterObject() {
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.beginArray();
  }

  @Test(expected = AssertionError.class)
  public void testWriteToUnwritableWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    JsonWriter jsonWriter = writer;
    jsonWriter.write(new char[0], 0, 0);
  }
}