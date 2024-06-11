import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonTreeWriterTest {

    private JsonTreeWriter jsonTreeWriter;

    @Before
    public void setup() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    public void testBeginArray() {
        jsonTreeWriter.beginArray();
        assertTrue(jsonTreeWriter.peek() instanceof JsonArray);
    }

    @Test
    public void testEndArray() {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.endArray();
        assertTrue(jsonTreeWriter.peek() instanceof JsonObject);
    }

    @Test
    public void testBeginObject() {
        jsonTreeWriter.beginObject();
        assertTrue(jsonTreeWriter.peek() instanceof JsonObject);
    }

    @Test
    public void testEndObject() {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.endObject();
        assertTrue(jsonTreeWriter.peek() instanceof JsonArray);
    }

    @Test
    public void testName() {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("key");
        assertEquals("key", jsonTreeWriter.pendingName);
    }

    // Additional edge cases and exception handling scenarios test cases should be added for other methods

}