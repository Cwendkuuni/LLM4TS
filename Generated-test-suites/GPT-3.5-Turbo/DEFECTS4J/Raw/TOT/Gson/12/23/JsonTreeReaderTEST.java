import com.google.gson.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class JsonTreeReaderTest {

    @Test
    void testBeginArray() throws IOException {
        JsonElement element = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(element);
        reader.beginArray();
        assertTrue(reader.hasNext());
        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    }

    @Test
    void testEndArray() throws IOException {
        JsonElement element = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(element);
        reader.endArray();
        assertFalse(reader.hasNext());
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    void testBeginObject() throws IOException {
        JsonElement element = new JsonObject();
        JsonTreeReader reader = new JsonTreeReader(element);
        reader.beginObject();
        assertTrue(reader.hasNext());
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    }

    @Test
    void testEndObject() throws IOException {
        JsonElement element = new JsonObject();
        JsonTreeReader reader = new JsonTreeReader(element);
        reader.endObject();
        assertFalse(reader.hasNext());
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    void testHasNext() throws IOException {
        JsonElement element = new JsonPrimitive("test");
        JsonTreeReader reader = new JsonTreeReader(element);
        assertFalse(reader.hasNext());
        reader.beginArray();
        assertTrue(reader.hasNext());
    }

    @Test
    void testPeek() throws IOException {
        JsonElement element = new JsonPrimitive("test");
        JsonTreeReader reader = new JsonTreeReader(element);
        assertEquals(JsonToken.STRING, reader.peek());
        reader.beginArray();
        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    }

    @Test
    void testNextName() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "value");
        JsonTreeReader reader = new JsonTreeReader(jsonObject);
        reader.beginObject();
        assertEquals("name", reader.nextName());
    }

    @Test
    void testNextString() throws IOException {
        JsonElement element = new JsonPrimitive("test");
        JsonTreeReader reader = new JsonTreeReader(element);
        assertEquals("test", reader.nextString());
    }

    @Test
    void testNextBoolean() throws IOException {
        JsonElement element = new JsonPrimitive(true);
        JsonTreeReader reader = new JsonTreeReader(element);
        assertTrue(reader.nextBoolean());
    }

    @Test
    void testNextNull() throws IOException {
        JsonElement element = JsonNull.INSTANCE;
        JsonTreeReader reader = new JsonTreeReader(element);
        reader.nextNull();
        assertFalse(reader.hasNext());
    }

    @Test
    void testNextDouble() throws IOException {
        JsonElement element = new JsonPrimitive(3.14);
        JsonTreeReader reader = new JsonTreeReader(element);
        assertEquals(3.14, reader.nextDouble());
    }

    @Test
    void testNextLong() throws IOException {
        JsonElement element = new JsonPrimitive(100);
        JsonTreeReader reader = new JsonTreeReader(element);
        assertEquals(100, reader.nextLong());
    }

    @Test
    void testNextInt() throws IOException {
        JsonElement element = new JsonPrimitive(42);
        JsonTreeReader reader = new JsonTreeReader(element);
        assertEquals(42, reader.nextInt());
    }

    @Test
    void testClose() throws IOException {
        JsonElement element = new JsonPrimitive("test");
        JsonTreeReader reader = new JsonTreeReader(element);
        reader.close();
        assertFalse(reader.hasNext());
    }

    @Test
    void testSkipValue() throws IOException {
        JsonElement element = new JsonObject();
        element.getAsJsonObject().addProperty("name", "value");
        JsonTreeReader reader = new JsonTreeReader(element);
        reader.beginObject();
        reader.nextName();
        reader.skipValue();
        assertFalse(reader.hasNext());
    }

    @Test
    void testPromoteNameToValue() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "value");
        JsonElement element = jsonObject.entrySet().iterator().next().getValue();
        JsonTreeReader reader = new JsonTreeReader(element);
        reader.promoteNameToValue();
        assertEquals("name", reader.nextString());
        assertEquals("value", reader.nextString());
    }

    @Test
    void testGetPath() throws IOException {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonObject());
        jsonArray.get(0).getAsJsonObject().addProperty("name", "value");
        JsonTreeReader reader = new JsonTreeReader(jsonArray);
        reader.beginArray();
        reader.beginObject();
        reader.nextName();
        assertEquals("$[0].name", reader.getPath());
    }

    @Test
    void testGetPath_EmptyArray() throws IOException {
        JsonArray jsonArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(jsonArray);
        assertEquals("$", reader.getPath());
    }

    @Test
    void testGetPath_EmptyObject() throws IOException {
        JsonObject jsonObject = new JsonObject();
        JsonTreeReader reader = new JsonTreeReader(jsonObject);
        assertEquals("$", reader.getPath());
    }

    @Test
    void testToString() {
        JsonElement element = new JsonObject();
        JsonTreeReader reader = new JsonTreeReader(element);
        assertEquals("JsonTreeReader", reader.toString());
    }
}