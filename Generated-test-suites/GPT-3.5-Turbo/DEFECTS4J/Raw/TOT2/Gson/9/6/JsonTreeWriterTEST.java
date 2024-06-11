import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeWriterTest {

    @Test
    public void testGet() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonElement element = writer.get();
        assertNotNull(element);
        assertTrue(element.isJsonNull());
    }

    @Test
    public void testBeginArray() {
        // Test adding elements to JsonArray
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.value("value1");
        writer.value(123);
        writer.endArray();
        JsonElement element = writer.get();
        assertTrue(element.isJsonArray());
        assertEquals(2, element.getAsJsonArray().size());
    }

    @Test
    public void testBeginObject() {
        // Test adding elements to JsonObject
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("key").value("value");
        writer.endObject();
        JsonElement element = writer.get();
        assertTrue(element.isJsonObject());
        assertTrue(element.getAsJsonObject().has("key"));
    }

    @Test
    public void testValue() {
        // Test adding different types of values
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value("string");
        writer.value(true);
        writer.value(123);
        JsonElement element = writer.get();
        assertTrue(element.isJsonPrimitive());
        assertEquals("string", element.getAsString());

        writer = new JsonTreeWriter();
        writer.value(null);
        element = writer.get();
        assertTrue(element.isJsonNull());
    }

    @Test
    public void testExceptionHandling() {
        // Test exception handling for closing without completing document
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.close();
            fail("Should have thrown an IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testEdgeCases() {
        // Test edge case of empty document
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonElement element = writer.get();
        assertNotNull(element);
        assertTrue(element.isJsonNull());
    }
}