import org.junit.Test;
import static org.junit.Assert.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonTreeWriterTest {

    @Test
    public void testGet() {
        JsonTreeWriter writer = new JsonTreeWriter();
        assertEquals(JsonNull.INSTANCE, writer.get());
    }

    @Test
    public void testBeginArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        JsonObject object = (JsonObject) writer.get();
        assertFalse(object.isJsonNull());
        assertTrue(object.isJsonArray());
    }

    @Test
    public void testEndArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.endArray();
        JsonObject object = (JsonObject) writer.get();
        assertTrue(object.isJsonNull());
    }

    @Test
    public void testBeginObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        JsonObject object = (JsonObject) writer.get();
        assertFalse(object.isJsonNull());
        assertTrue(object.isJsonObject());
    }

    @Test
    public void testEndObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.endObject();
        JsonObject object = (JsonObject) writer.get();
        assertTrue(object.isJsonNull());
    }

    @Test
    public void testName() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name");
        JsonObject object = (JsonObject) writer.get();
        assertFalse(object.isJsonNull());
        assertTrue(object.has("name"));
    }

    @Test
    public void testValueString() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value("Hello");
        JsonPrimitive primitive = (JsonPrimitive) writer.get();
        assertFalse(primitive.isJsonNull());
        assertTrue(primitive.isString());
        assertEquals("Hello", primitive.getAsString());
    }

    @Test
    public void testNullValue() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.nullValue();
        JsonNull jsonNull = (JsonNull) writer.get();
        assertEquals(JsonNull.INSTANCE, jsonNull);
    }

    @Test
    public void testValueBoolean() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(true);
        JsonPrimitive primitive = (JsonPrimitive) writer.get();
        assertFalse(primitive.isJsonNull());
        assertTrue(primitive.isBoolean());
        assertTrue(primitive.getAsBoolean());
    }

    @Test
    public void testValueDouble() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(3.14);
        JsonPrimitive primitive = (JsonPrimitive) writer.get();
        assertFalse(primitive.isJsonNull());
        assertTrue(primitive.isNumber());
        assertEquals(3.14, primitive.getAsDouble(), 0);
    }

    @Test
    public void testValueLong() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(100L);
        JsonPrimitive primitive = (JsonPrimitive) writer.get();
        assertFalse(primitive.isJsonNull());
        assertTrue(primitive.isNumber());
        assertEquals(100L, primitive.getAsLong());
    }

    @Test
    public void testValueNumber() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(42);
        JsonPrimitive primitive = (JsonPrimitive) writer.get();
        assertFalse(primitive.isJsonNull());
        assertTrue(primitive.isNumber());
        assertEquals(42, primitive.getAsInt());
    }

    @Test
    public void testFlush() {
        JsonTreeWriter writer = new JsonTreeWriter();
        // Nothing to test, flush method does nothing
    }

    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.close();
        writer.beginArray(); // This should throw an IOException
    }
}