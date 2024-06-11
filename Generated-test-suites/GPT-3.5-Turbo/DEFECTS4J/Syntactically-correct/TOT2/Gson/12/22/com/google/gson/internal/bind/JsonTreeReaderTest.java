import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JsonTreeReaderTest {

    private JsonTreeReader jsonTreeReader;

    @Before
    public void setup() {
        JsonElement element = new JsonObject();
        jsonTreeReader = new JsonTreeReader(element);
    }

    @Test
    public void testBeginArray() throws IOException {
        jsonTreeReader.beginArray();
        assertTrue(jsonTreeReader.hasNext());
    }

    @Test
    public void testEndArray() throws IOException {
        jsonTreeReader.beginArray();
        jsonTreeReader.endArray();
        assertFalse(jsonTreeReader.hasNext());
    }

    @Test
    public void testBeginObject() throws IOException {
        jsonTreeReader.beginObject();
        assertTrue(jsonTreeReader.hasNext());
    }

    @Test
    public void testEndObject() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.endObject();
        assertFalse(jsonTreeReader.hasNext());
    }

    @Test
    public void testHasNext() throws IOException {
        assertFalse(jsonTreeReader.hasNext());
    }

    @Test
    public void testPeek() throws IOException {
        assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
    }

    @Test
    public void testNextName() throws IOException {
        String name = "testName";
        ((JsonObject)jsonTreeReader.peekStack()).add(name, new JsonArray());
        assertEquals(name, jsonTreeReader.nextName());
    }

    @Test
    public void testNextString() throws IOException {
        String value = "testValue";
        ((JsonObject)jsonTreeReader.peekStack()).addProperty("key", value);
        assertEquals(value, jsonTreeReader.nextString());
    }

    @Test
    public void testNextBoolean() throws IOException {
        boolean value = true;
        ((JsonObject)jsonTreeReader.peekStack()).addProperty("key", value);
        assertTrue(jsonTreeReader.nextBoolean());
    }

    @Test
    public void testNextNull() throws IOException {
        ((JsonObject)jsonTreeReader.peekStack()).addNull("key");
        jsonTreeReader.nextNull();
        assertFalse(jsonTreeReader.hasNext());
    }

    @Test
    public void testNextDouble() throws IOException {
        double value = 10.5;
        ((JsonObject)jsonTreeReader.peekStack()).addProperty("key", value);
        assertEquals(value, jsonTreeReader.nextDouble(), 0);
    }

    @Test
    public void testNextLong() throws IOException {
        long value = 100;
        ((JsonObject)jsonTreeReader.peekStack()).addProperty("key", value);
        assertEquals(value, jsonTreeReader.nextLong());
    }

    @Test
    public void testNextInt() throws IOException {
        int value = 5;
        ((JsonObject)jsonTreeReader.peekStack()).addProperty("key", value);
        assertEquals(value, jsonTreeReader.nextInt());
    }

    @Test
    public void testClose() throws IOException {
        jsonTreeReader.close();
        assertThrows(IllegalStateException.class, () -> jsonTreeReader.peek());
    }

    @Test
    public void testSkipValue() throws IOException {
        ((JsonObject)jsonTreeReader.peekStack()).add("key", new JsonObject());
        jsonTreeReader.promoteNameToValue();
        assertTrue(jsonTreeReader.hasNext());
    }

    @Test
    public void testGetPath() throws IOException {
        ((JsonObject)jsonTreeReader.peekStack()).add("key", new JsonObject());
        jsonTreeReader.nextName();
        assertEquals("$.key", jsonTreeReader.getPath());
    }
}