import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JsonTreeWriterTest {

    @Test
    public void testGet() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonElement element = writer.get();
        assertEquals(JsonNull.INSTANCE, element);
    }

    @Test
    public void testBeginArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.beginArray();
            JsonElement element = writer.get();
            assertTrue(element instanceof JsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEndArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.beginArray();
            writer.endArray();
            JsonElement element = writer.get();
            assertEquals(JsonNull.INSTANCE, element);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBeginObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.beginObject();
            JsonElement element = writer.get();
            assertTrue(element instanceof JsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEndObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.beginObject();
            writer.endObject();
            JsonElement element = writer.get();
            assertEquals(JsonNull.INSTANCE, element);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Additional test cases for other public methods can be added here

}