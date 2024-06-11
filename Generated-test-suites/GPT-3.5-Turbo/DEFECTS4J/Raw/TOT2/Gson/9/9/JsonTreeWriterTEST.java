
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
    public void testBeginArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonWriter jsonWriter = writer.beginArray();
        assertTrue(writer.peek() instanceof JsonArray);
    }

    @Test
    public void testEndArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        JsonWriter jsonWriter = writer.endArray();
        assertTrue(writer.peek() instanceof JsonElement);
    }

    @Test
    public void testBeginObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonWriter jsonWriter = writer.beginObject();
        assertTrue(writer.peek() instanceof JsonObject);
    }

    @Test
    public void testEndObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        JsonWriter jsonWriter = writer.endObject();
        assertTrue(writer.peek() instanceof JsonElement);
    }

    @Test
    public void testName() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        JsonWriter jsonWriter = writer.name("testName");
        assertEquals("testName", writer.pendingName);
    }

    @Test
    public void testValueString() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonWriter jsonWriter = writer.value("testValue");
        assertTrue(writer.peek() instanceof JsonPrimitive);
    }

    @Test
    public void testNullValue() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonWriter jsonWriter = writer.nullValue();
        assertEquals(JsonNull.INSTANCE, writer.peek());
    }

    @Test
    public void testValueBoolean() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonWriter jsonWriter = writer.value(true);
        assertTrue(writer.peek() instanceof JsonPrimitive);
    }

    @Test
    public void testValueDouble() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonWriter jsonWriter = writer.value(3.14);
        assertTrue(writer.peek() instanceof JsonPrimitive);
    }

    @Test
    public void testValueLong() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonWriter jsonWriter = writer.value(100L);
        assertTrue(writer.peek() instanceof JsonPrimitive);
    }

    @Test
    public void testValueNumber() {
        JsonTreeWriter writer = new JsonTreeWriter();
        JsonWriter jsonWriter = writer.value(42);
        assertTrue(writer.peek() instanceof JsonPrimitive);
    }

    @Test(expected = AssertionError.class)
    public void testUnwritableWriter() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.close();
    }

    @Test(expected = IOException.class)
    public void testCloseIncompleteDocument() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.close();
    }
}
