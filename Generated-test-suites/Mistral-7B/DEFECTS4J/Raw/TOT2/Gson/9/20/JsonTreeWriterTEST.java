import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.junit.Test;

public class JsonTreeWriterTest {
  @Test
  public void testJsonTreeWriter_BasicUsage() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginObject();
    writer.name("name");
    writer.value("John Doe");
    writer.name("age");
    writer.value(30);
    writer.endObject();

    JsonElement jsonElement = writer.get();
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    assertEquals("name", jsonObject.keySet().iterator().next());
    assertEquals("John Doe", jsonObject.get("name").getAsString());
    assertEquals(30, jsonObject.get("age").getAsInt());

    assertNull(jsonObject.get("unknownKey"));
  }

  @Test
  public void testJsonTreeWriter_ArrayUsage() {
    JsonTreeWriter writer = new JsonTreeWriter();

    writer.beginArray();
    writer.beginArray();
    writer.value(1);
    writer.value(2);
    writer.endArray();
    writer.value(3);
    writer.endArray();

    JsonElement jsonElement = writer.get();
    JsonArray jsonArray = jsonElement.getAsJsonArray();
    JsonArray innerArray = jsonArray.get(0).getAsJsonArray();

    assertEquals(2, innerArray.size());
    assertEquals(1, innerArray.get(0).getAsInt());
    assertEquals(2, innerArray.get(1).getAsInt());
    assertEquals(3, jsonArray.get(1).getAsInt());
  }
}