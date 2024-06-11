import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.*;

public class JsonTreeReaderTest {

    @Test
    public void testBeginArray() throws IOException {
        JsonArray jsonArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(jsonArray);

        reader.beginArray();

        assertTrue(reader.hasNext());
        assertEquals(JsonToken.END_ARRAY, reader.peek());
    }

    @Test
    public void testEndArray() throws IOException {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("item1");
        jsonArray.add("item2");
        JsonTreeReader reader = new JsonTreeReader(jsonArray);

        reader.beginArray();
        reader.endArray();

        assertFalse(reader.hasNext());
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testBeginObject() throws IOException {
        JsonObject jsonObject = new JsonObject();
        JsonTreeReader reader = new JsonTreeReader(jsonObject);

        reader.beginObject();

        assertTrue(reader.hasNext());
        assertEquals(JsonToken.END_OBJECT, reader.peek());
    }

    @Test
    public void testEndObject() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key1", "value1");
        jsonObject.addProperty("key2", "value2");
        JsonTreeReader reader = new JsonTreeReader(jsonObject);

        reader.beginObject();
        reader.endObject();

        assertFalse(reader.hasNext());
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testHasNext() throws IOException {
        JsonArray jsonArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(jsonArray);

        assertFalse(reader.hasNext());
    }

    @Test
    public void testPeek() throws IOException {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("item1");
        JsonTreeReader reader = new JsonTreeReader(jsonArray);

        reader.beginArray();

        assertEquals(JsonToken.STRING, reader.peek());
        assertEquals("item1", reader.nextString());
        assertEquals(JsonToken.END_ARRAY, reader.peek());
    }

    @Test
    public void testNextName() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key1", "value1");
        JsonTreeReader reader = new JsonTreeReader(jsonObject);

        reader.beginObject();
        String name = reader.nextName();

        assertEquals("key1", name);
        assertEquals(JsonToken.STRING, reader.peek());
    }

    @Test
    public void testNextString() throws IOException {
        JsonPrimitive jsonPrimitive = new JsonPrimitive("string");
        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

        assertEquals("string", reader.nextString());
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testNextBoolean() throws IOException {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(true);
        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

        assertTrue(reader.nextBoolean());
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testNextNull() throws IOException {
        JsonNull jsonNull = JsonNull.INSTANCE;
        JsonTreeReader reader = new JsonTreeReader(jsonNull);

        reader.nextNull();

        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testNextDouble() throws IOException {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(3.14);
        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

        assertEquals(3.14, reader.nextDouble(), 0.001);
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testNextLong() throws IOException {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(100L);
        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

        assertEquals(100L, reader.nextLong());
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testNextInt() throws IOException {
        JsonPrimitive jsonPrimitive = new JsonPrimitive(42);
        JsonTreeReader reader = new JsonTreeReader(jsonPrimitive);

        assertEquals(42, reader.nextInt());
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testClose() throws IOException {
        JsonArray jsonArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(jsonArray);

        reader.close();

        assertFalse(reader.hasNext());
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testSkipValue() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "value");
        JsonTreeReader reader = new JsonTreeReader(jsonObject);

        reader.beginObject();
        reader.skipValue();

        assertEquals(JsonToken.END_OBJECT, reader.peek());
    }

    @Test
    public void testPromoteNameToValue() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("key", new JsonPrimitive("value"));
        JsonTreeReader reader = new JsonTreeReader(jsonObject);

        reader.beginObject();
        reader.promoteNameToValue();

        assertEquals(JsonToken.STRING, reader.peek());
    }

    @Test
    public void testGetPath() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("key", new JsonArray());
        JsonTreeReader reader = new JsonTreeReader(jsonObject);

        reader.beginObject();
        reader.nextName();
        reader.beginArray();

        assertEquals("$.key[0]", reader.getPath());
    }

    @Test
    public void testToString() {
        JsonArray jsonArray = new JsonArray();
        JsonTreeReader reader = new JsonTreeReader(jsonArray);

        assertEquals("JsonTreeReader", reader.toString());
    }
}

