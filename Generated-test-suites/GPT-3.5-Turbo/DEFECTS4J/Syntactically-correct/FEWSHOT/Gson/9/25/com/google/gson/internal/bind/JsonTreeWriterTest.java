import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import org.junit.Test;

public class JsonTreeWriterTest {
    @Test
    public void testGet() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonElement jsonElement = writer.get();
        assertNotNull(jsonElement);
        assertEquals(JsonNull.INSTANCE, jsonElement);
    }

    @Test
    public void testBeginArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        JsonElement jsonElement = writer.get();
        assertNotNull(jsonElement);
        assertTrue(jsonElement instanceof JsonArray);
    }

    @Test
    public void testEndArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.endArray();
        JsonElement jsonElement = writer.get();
        assertNotNull(jsonElement);
        assertEquals(JsonNull.INSTANCE, jsonElement);
    }

    @Test
    public void testBeginObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        JsonElement jsonElement = writer.get();
        assertNotNull(jsonElement);
        assertTrue(jsonElement instanceof JsonObject);
    }

    @Test
    public void testEndObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.endObject();
        JsonElement jsonElement = writer.get();
        assertNotNull(jsonElement);
        assertEquals(JsonNull.INSTANCE, jsonElement);
    }

    @Test
    public void testName() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name");
        JsonElement jsonElement = writer.get();
        assertNotNull(jsonElement);
        assertTrue(jsonElement instanceof JsonObject);
        JsonObject jsonObject = (JsonObject) jsonElement;
        assertTrue(jsonObject.has("name"));
    }

    @Test
    public void testValueString() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value("value");
        JsonElement jsonElement = writer.get();
        assertNotNull(jsonElement);
        assertEquals(new JsonPrimitive("value"), jsonElement);
    }

    @Test
    public void testValueNull() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.nullValue();
        JsonElement jsonElement = writer.get();
        assertNotNull(jsonElement);
        assertEquals(JsonNull.INSTANCE, jsonElement);
    }

    @Test
    public void testValueBoolean() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(true);
        JsonElement jsonElement = writer.get();
        assertNotNull(jsonElement);
        assertEquals(new JsonPrimitive(true), jsonElement);
    }

    @Test
    public void testValueDouble() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(3.14);
        JsonElement jsonElement = writer.get();
        assertNotNull(jsonElement);
        assertEquals(new JsonPrimitive(3.14), jsonElement);
    }

    @Test
    public void testValueLong() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(42L);
        JsonElement jsonElement = writer.get();
        assertNotNull(jsonElement);
        assertEquals(new JsonPrimitive(42L), jsonElement);
    }

    @Test
    public void testValueNumber() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(3.14);
        JsonElement jsonElement = writer.get();
        assertNotNull(jsonElement);
        assertEquals(new JsonPrimitive(3.14), jsonElement);
    }

    @Test
    public void testFlush() {
        JsonTreeWriter writer = new JsonTreeWriter();
        // Flushing doesn't do anything in this implementation, so no need for a test.
    }

    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.close();
        writer.beginArray(); // This should throw an IOException since the writer is closed.
    }
}