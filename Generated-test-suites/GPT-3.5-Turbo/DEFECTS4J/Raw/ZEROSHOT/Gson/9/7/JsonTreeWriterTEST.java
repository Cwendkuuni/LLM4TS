import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.StringWriter;
import static org.junit.jupiter.api.Assertions.*;

public class JsonTreeWriterTest {

    @Test
    public void testGet() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonElement expected = JsonNull.INSTANCE;
        JsonElement actual = writer.get();
        assertEquals(expected, actual);
    }

    @Test
    public void testBeginArray() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        JsonArray expected = new JsonArray();
        JsonArray actual = (JsonArray) writer.get();
        assertEquals(expected, actual);
    }

    @Test
    public void testEndArray() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.endArray();
        JsonElement expected = JsonNull.INSTANCE;
        JsonElement actual = writer.get();
        assertEquals(expected, actual);
    }

    @Test
    public void testBeginObject() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        JsonObject expected = new JsonObject();
        JsonObject actual = (JsonObject) writer.get();
        assertEquals(expected, actual);
    }

    @Test
    public void testEndObject() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.endObject();
        JsonElement expected = JsonNull.INSTANCE;
        JsonElement actual = writer.get();
        assertEquals(expected, actual);
    }

    @Test
    public void testName() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("testName");
        JsonObject jsonObject = (JsonObject) writer.get();
        assertTrue(jsonObject.has("testName"));
    }

    @Test
    public void testValueString() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value("testValueString");
        JsonPrimitive expected = new JsonPrimitive("testValueString");
        JsonPrimitive actual = (JsonPrimitive) writer.get();
        assertEquals(expected, actual);
    }

    @Test
    public void testNullValue() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.nullValue();
        JsonElement expected = JsonNull.INSTANCE;
        JsonElement actual = writer.get();
        assertEquals(expected, actual);
    }

    @Test
    public void testValueBoolean() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(true);
        JsonPrimitive expected = new JsonPrimitive(true);
        JsonPrimitive actual = (JsonPrimitive) writer.get();
        assertEquals(expected, actual);
    }

    @Test
    public void testValueDouble() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(10.5);
        JsonPrimitive expected = new JsonPrimitive(10.5);
        JsonPrimitive actual = (JsonPrimitive) writer.get();
        assertEquals(expected, actual);
    }

    @Test
    public void testValueLong() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(100L);
        JsonPrimitive expected = new JsonPrimitive(100L);
        JsonPrimitive actual = (JsonPrimitive) writer.get();
        assertEquals(expected, actual);
    }

    @Test
    public void testValueNumber() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(10);
        JsonPrimitive expected = new JsonPrimitive(10);
        JsonPrimitive actual = (JsonPrimitive) writer.get();
        assertEquals(expected, actual);
    }

    @Test
    public void testFlush() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        assertDoesNotThrow(() -> writer.flush());
    }

    @Test
    public void testClose() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        assertThrows(IOException.class, () -> writer.close());
    }
}