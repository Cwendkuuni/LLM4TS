import static org.junit.Assert.*;
import static com.google.gson.stream.CoercingJsonWriter.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;

import org.junit.Test;

public class JsonTreeWriterTest {

  @Test
  public void testArrayWriting() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginArray();
    writer.value(1);
    writer.value(2);
    writer.value(3.5);
    writer.endArray();

    JsonElement result = writer.get();
    JsonArray expected = new JsonArray();
    expected.add(new JsonPrimitive(1));
    expected.add(new JsonPrimitive(2));
    expected.add(new JsonPrimitive(3.5));

    assertEquals(expected, result);
  }

  @Test
  public void testObjectWriting() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(30);
    writer.name("isStudent").value(false);
    writer.endObject();

    JsonElement result = writer.get();
    JsonObject expected = new JsonObject();
    expected.addProperty("name", "John Doe");
    expected.addProperty("age", 30);
    expected.addProperty("isStudent", false);

    assertEquals(expected, result);
  }

  @Test
  public void testNestedObjectsAndArrays() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.name("person").beginObject();
    writer.name("name").value("John Doe");
    writer.name("age").value(30);
    writer.name("children").beginArray();
    writer.beginObject();
    writer.name("name").value("Jane Doe");
    writer.name("age").value(25);
    writer.endObject();
    writer.endArray();
    writer.endObject();
    writer.endObject();

    JsonElement result = writer.get();
    JsonObject expected = new JsonObject();
    JsonObject person = new JsonObject();
    person.addProperty("name", "John Doe");
    person.addProperty("age", 30);
    JsonArray children = new JsonArray();
    children.add(new JsonObject());
    children.getAsJsonObject().addProperty("name", "Jane Doe");
    children.getAsJsonObject().addProperty("age", 25);
    expected.add("person", person);
    expected.add("children", children);

    assertEquals(expected, result);
  }

  @Test
  public void testClosingWriter() {
    JsonTreeWriter writer = new JsonTreeWriter();
    writer.close();

    try {
      writer.value("test");
    } catch (IllegalStateException e) {
      // expected
    }
  }

  @Test(expected = IOException.class)
  public void testWritingToUnwritableWriter() throws IOException {
    JsonTreeWriter writer = new JsonTreeWriter();
    Writer unwritableWriter = new Writer() {
      @Override public void write(char[] buffer, int offset, int counter) {
        throw new AssertionError();
      }
      @Override public void flush() throws IOException {
        throw new AssertionError();
      }
      @Override public void close() throws IOException {
        throw new AssertionError();
      }
    };
    writer.setWriter(unwritableWriter);
    writer.beginObject();
  }

  @Test
  public void testSerializedNullValues() {
    JsonTreeWriter writer = new JsonTreeWriter(new GsonBuilder().serializeNulls().create());
    writer.value(null);
    JsonElement result = writer.get();
    JsonNull expected = JsonNull.INSTANCE;
    assertEquals(expected, result);
  }
}