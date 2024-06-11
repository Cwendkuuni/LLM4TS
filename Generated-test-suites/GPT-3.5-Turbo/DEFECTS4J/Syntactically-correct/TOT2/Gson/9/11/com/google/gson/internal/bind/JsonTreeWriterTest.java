import com.google.gson.JsonArray;
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
        assertEquals(JsonPrimitive.class, element.getClass());
        assertEquals(JsonNull.INSTANCE, element);
    }

    @Test
    public void testBeginAndEndArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.endArray();
        JsonElement element = writer.get();
        assertTrue(element instanceof JsonArray);
    }

    @Test
    public void testBeginAndEndObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.endObject();
        JsonElement element = writer.get();
        assertTrue(element instanceof JsonObject);
    }

    @Test
    public void testValueString() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value("test");
        JsonElement element = writer.get();
        assertEquals("test", element.getAsString());
    }

    @Test
    public void testNullValue() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.nullValue();
        JsonElement element = writer.get();
        assertEquals(JsonNull.INSTANCE, element);
    }

    @Test
    public void testValueBoolean() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(true);
        JsonElement element = writer.get();
        assertEquals(true, element.getAsBoolean());
    }

    @Test
    public void testValueDouble() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(3.14);
        JsonElement element = writer.get();
        assertEquals(3.14, element.getAsDouble(), 0);
    }

    @Test
    public void testValueLong() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(100L);
        JsonElement element = writer.get();
        assertEquals(100L, element.getAsLong());
    }

    @Test
    public void testValueNumber() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(123);
        JsonElement element = writer.get();
        assertEquals(123, element.getAsInt());
    }

    @Test
    public void testClose() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.close();
        try {
            writer.get();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // test passed
        }
    }

    @Test(expected = IOException.class)
    public void testIncompleteDocument() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.close();
    }
}