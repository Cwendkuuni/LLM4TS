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
    public void testBeginAndEndArray() {
        JsonTreeWriter writer = new JsonTreeWriter();

        try {
            writer.beginArray();
            writer.endArray();
        } catch (Exception e) {
            fail("Exception thrown for valid operation: " + e.getMessage());
        }
    }

    @Test
    public void testBeginAndEndObject() {
        JsonTreeWriter writer = new JsonTreeWriter();

        try {
            writer.beginObject();
            writer.endObject();
        } catch (Exception e) {
            fail("Exception thrown for valid operation: " + e.getMessage());
        }
    }

    @Test
    public void testName() {
        JsonTreeWriter writer = new JsonTreeWriter();
        String name = "testName";

        try {
            writer.beginObject();
            writer.name(name);
            writer.endObject();
        } catch (Exception e) {
            fail("Exception thrown for valid operation: " + e.getMessage());
        }
    }

    @Test
    public void testValue() {
        JsonTreeWriter writer = new JsonTreeWriter();

        try {
            writer.value("testValue");
            writer.value(true);
            writer.value(10.5);
            writer.value(100L);
            writer.value(new JsonPrimitive("testPrimitive"));
        } catch (Exception e) {
            fail("Exception thrown for valid operation: " + e.getMessage());
        }
    }

    @Test
    public void testNullValue() {
        JsonTreeWriter writer = new JsonTreeWriter();

        try {
            writer.nullValue();
        } catch (Exception e) {
            fail("Exception thrown for valid operation: " + e.getMessage());
        }
    }
}