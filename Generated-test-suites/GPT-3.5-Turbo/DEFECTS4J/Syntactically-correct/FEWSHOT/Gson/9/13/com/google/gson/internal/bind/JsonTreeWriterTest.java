import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import org.junit.Test;

import java.io.IOException;

public class JsonTreeWriterTest {

    @Test
    public void testGet() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonElement expected = JsonNull.INSTANCE;
        JsonElement actual = writer.get();
        assertEquals(expected, actual);

        writer.beginObject();
        expected = new JsonObject();
        actual = writer.get();
        assertEquals(expected, actual);

        writer.endObject();
        expected = JsonNull.INSTANCE;
        actual = writer.get();
        assertEquals(expected, actual);
    }

    @Test
    public void testBeginArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonObject object = new JsonObject();
        writer.put(object);
        writer.beginArray();
        JsonElement expected = new JsonArray();
        JsonElement actual = writer.peek();
        assertEquals(expected, actual);
    }

    @Test
    public void testEndArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonObject object = new JsonObject();
        writer.put(object);
        writer.beginArray();
        writer.endArray();
        JsonElement expected = object;
        JsonElement actual = writer.peek();
        assertEquals(expected, actual);
    }

    @Test
    public void testBeginObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonObject object = new JsonObject();
        writer.put(object);
        writer.beginObject();
        JsonElement expected = new JsonObject();
        JsonElement actual = writer.peek();
        assertEquals(expected, actual);
    }

    @Test
    public void testEndObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonObject parent = new JsonObject();
        writer.put(parent);
        writer.beginObject();
        writer.endObject();
        JsonElement expected = parent;
        JsonElement actual = writer.peek();
        assertEquals(expected, actual);
    }

    @Test
    public void testName() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonObject object = new JsonObject();
        writer.put(object);
        writer.beginObject();
        writer.name("name");
        String expected = "name";
        String actual = writer.pendingName;
        assertEquals(expected, actual);
    }

    @Test
    public void testValueString() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonObject object = new JsonObject();
        writer.put(object);
        writer.value("value");
        JsonPrimitive expected = new JsonPrimitive("value");
        JsonPrimitive actual = (JsonPrimitive) writer.peek().getAsJsonArray().get(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testNullValue() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonObject object = new JsonObject();
        writer.put(object);
        writer.nullValue();
        JsonNull expected = JsonNull.INSTANCE;
        JsonNull actual = (JsonNull) writer.peek().getAsJsonArray().get(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testValueBoolean() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonObject object = new JsonObject();
        writer.put(object);
        writer.value(true);
        JsonPrimitive expected = new JsonPrimitive(true);
        JsonPrimitive actual = (JsonPrimitive) writer.peek().getAsJsonArray().get(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testValueDouble() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonObject object = new JsonObject();
        writer.put(object);
        writer.value(1.234);
        JsonPrimitive expected = new JsonPrimitive(1.234);
        JsonPrimitive actual = (JsonPrimitive) writer.peek().getAsJsonArray().get(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testValueLong() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonObject object = new JsonObject();
        writer.put(object);
        writer.value(1234L);
        JsonPrimitive expected = new JsonPrimitive(1234L);
        JsonPrimitive actual = (JsonPrimitive) writer.peek().getAsJsonArray().get(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testValueNumber() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonObject object = new JsonObject();
        writer.put(object);
        writer.value(1234);
        JsonPrimitive expected = new JsonPrimitive(1234);
        JsonPrimitive actual = (JsonPrimitive) writer.peek().getAsJsonArray().get(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testFlush() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Test that no exception is thrown
    }

    @Test
    public void testClose() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Test that no exception is thrown
    }
}