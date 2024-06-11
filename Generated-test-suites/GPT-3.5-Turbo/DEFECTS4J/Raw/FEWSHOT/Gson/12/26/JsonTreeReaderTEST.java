import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonTreeReaderTest {

    private JsonTreeReader reader;

    @Before
    public void setup() {
        JsonElement element = new JsonObject();
        reader = new JsonTreeReader(element);
    }

    @Test
    public void testBeginArray() throws Exception {
        reader.beginArray();
        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
        assertTrue(reader.hasNext());
        reader.endArray();
        assertEquals(JsonToken.END_ARRAY, reader.peek());
    }

    @Test
    public void testEndArray() throws Exception {
        reader.beginArray();
        reader.endArray();
        assertFalse(reader.hasNext());
    }

    @Test
    public void testBeginObject() throws Exception {
        reader.beginObject();
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        assertTrue(reader.hasNext());
        reader.endObject();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
    }

    @Test
    public void testEndObject() throws Exception {
        reader.beginObject();
        reader.endObject();
        assertFalse(reader.hasNext());
    }

    @Test
    public void testHasNext() throws Exception {
        assertFalse(reader.hasNext());
    }

    @Test
    public void testPeek() throws Exception {
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    }

    @Test
    public void testNextName() throws Exception {
        reader.beginObject();
        assertNull(reader.nextName());
        reader.endObject();
    }

    @Test
    public void testNextString() throws Exception {
        reader.beginObject();
        assertNull(reader.nextString());
        reader.endObject();
    }

    @Test
    public void testNextBoolean() throws Exception {
        reader.beginObject();
        assertFalse(reader.nextBoolean());
        reader.endObject();
    }

    @Test
    public void testNextNull() throws Exception {
        reader.beginObject();
        reader.nextNull();
        reader.endObject();
    }

    @Test
    public void testNextDouble() throws Exception {
        reader.beginObject();
        assertEquals(0.0, reader.nextDouble(), 0);
        reader.endObject();
    }

    @Test
    public void testNextLong() throws Exception {
        reader.beginObject();
        assertEquals(0, reader.nextLong());
        reader.endObject();
    }

    @Test
    public void testNextInt() throws Exception {
        reader.beginObject();
        assertEquals(0, reader.nextInt());
        reader.endObject();
    }

    @Test
    public void testClose() throws Exception {
        reader.close();
        assertFalse(reader.hasNext());
    }

    @Test
    public void testSkipValue() throws Exception {
        reader.beginObject();
        reader.skipValue();
        assertFalse(reader.hasNext());
        reader.endObject();
    }

    @Test
    public void testToString() {
        assertEquals("JsonTreeReader", reader.toString());
    }

    @Test
    public void testPromoteNameToValue() throws Exception {
        reader.beginObject();
        reader.promoteNameToValue();
        assertEquals(JsonToken.STRING, reader.peek());
        reader.endObject();
    }

    @Test
    public void testGetPath() {
        assertEquals("$", reader.getPath());
    }
}