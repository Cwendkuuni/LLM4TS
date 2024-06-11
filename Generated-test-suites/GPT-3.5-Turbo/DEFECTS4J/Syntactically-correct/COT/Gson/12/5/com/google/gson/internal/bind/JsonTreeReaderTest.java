import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonTreeReaderTest {

    private JsonTreeReader reader;

    @Before
    public void setUp() {
        // Create a JsonTreeReader with a test JsonElement
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John");
        jsonObject.addProperty("age", 30);
        jsonObject.addProperty("married", true);
        jsonObject.add("children", new JsonArray());
        reader = new JsonTreeReader(jsonObject);
    }

    @Test
    public void testBeginArray() throws IOException {
        reader.beginArray();
        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    }

    @Test
    public void testEndArray() throws IOException {
        reader.beginArray();
        reader.endArray();
        assertEquals(JsonToken.END_ARRAY, reader.peek());
    }

    @Test
    public void testBeginObject() throws IOException {
        reader.beginObject();
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    }

    @Test
    public void testEndObject() throws IOException {
        reader.beginObject();
        reader.endObject();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
    }

    @Test
    public void testHasNext() throws IOException {
        assertTrue(reader.hasNext());
    }

    @Test
    public void testPeek() throws IOException {
        assertEquals(JsonToken.NAME, reader.peek());
    }

    @Test
    public void testNextName() throws IOException {
        assertEquals("name", reader.nextName());
        assertEquals(JsonToken.STRING, reader.peek());
    }

    @Test
    public void testNextString() throws IOException {
        // Test normal string value
        reader.nextName();
        assertEquals("John", reader.nextString());

        // Test null value
        reader.nextName();
        reader.nextNull();
        assertEquals(JsonToken.NULL, reader.peek());
    }

    @Test
    public void testNextBoolean() throws IOException {
        reader.nextName();
        reader.nextString();
        assertEquals(true, reader.nextBoolean());
        assertEquals(JsonToken.BOOLEAN, reader.peek());
    }

    @Test
    public void testNextNull() throws IOException {
        reader.nextName();
        reader.nextString();
        reader.nextBoolean();
        reader.nextNull();
        assertEquals(JsonToken.NULL, reader.peek());
    }

    @Test
    public void testNextDouble() throws IOException {
        reader.nextName();
        reader.nextString();
        reader.nextBoolean();
        reader.nextNull();
        reader.nextDouble();
        assertEquals(JsonToken.NUMBER, reader.peek());
    }

    @Test
    public void testNextLong() throws IOException {
        reader.nextName();
        reader.nextString();
        reader.nextBoolean();
        reader.nextNull();
        reader.nextLong();
        assertEquals(JsonToken.NUMBER, reader.peek());
    }

    @Test
    public void testNextInt() throws IOException {
        reader.nextName();
        reader.nextString();
        reader.nextBoolean();
        reader.nextNull();
        reader.nextInt();
        assertEquals(JsonToken.NUMBER, reader.peek());
    }

    @Test
    public void testClose() throws IOException {
        reader.close();
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testSkipValue() throws IOException {
        reader.skipValue();
        assertEquals(JsonToken.STRING, reader.peek());
    }

    @Test
    public void testPromoteNameToValue() throws IOException {
        reader.promoteNameToValue();
        assertEquals(JsonToken.STRING, reader.peek());
    }

    @Test
    public void testGetPath() throws IOException {
        reader.promoteNameToValue();
        assertEquals("$.name", reader.getPath());
    }

    @Test
    public void testMultipleOperations() throws IOException {
        reader.beginArray();
        reader.beginObject();
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());

        reader.nextName();
        assertEquals(JsonToken.STRING, reader.peek());
        assertEquals("name", reader.nextString());

        reader.nextName();
        assertEquals(JsonToken.NUMBER, reader.peek());
        assertEquals(30, reader.nextInt());

        reader.nextName();
        assertEquals(JsonToken.BOOLEAN, reader.peek());
        assertEquals(true, reader.nextBoolean());

        reader.nextName();
        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());

        reader.endArray();
        assertEquals(JsonToken.END_ARRAY, reader.peek());

        reader.endObject();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
    }
}