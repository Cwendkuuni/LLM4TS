import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonTreeWriterTest {

    @Test
    public void testGet() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonElement element = writer.get();
        assertEquals(JsonNull.INSTANCE, element);
    }

    @Test
    public void testBeginEndArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.endArray();
        JsonElement element = writer.get();
        assertTrue(element.isJsonArray());
    }

    @Test
    public void testBeginEndObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.endObject();
        JsonElement element = writer.get();
        assertTrue(element.isJsonObject());
    }

    @Test
    public void testValueString() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value("test");
        JsonElement element = writer.get();
        assertTrue(element.isJsonPrimitive());
        assertEquals("test", element.getAsString());
    }

    @Test
    public void testValueBoolean() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(true);
        JsonElement element = writer.get();
        assertTrue(element.isJsonPrimitive());
        assertEquals(true, element.getAsBoolean());
    }

    @Test
    public void testValueDouble() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(3.14);
        JsonElement element = writer.get();
        assertTrue(element.isJsonPrimitive());
        assertEquals(3.14, element.getAsDouble(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueDoubleNaN() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(Double.NaN);
    }

    @Test
    public void testValueLong() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(100L);
        JsonElement element = writer.get();
        assertTrue(element.isJsonPrimitive());
        assertEquals(100L, element.getAsLong());
    }

    @Test
    public void testValueNull() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.nullValue();
        JsonElement element = writer.get();
        assertTrue(element.isJsonNull());
    }

    @Test(expected = IOException.class)
    public void testCloseWithoutEndingOperations() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.close();
    }

}