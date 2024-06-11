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
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.*;

public class JsonTreeReaderTest {

    private JsonTreeReader jsonTreeReader;

    @Before
    public void setup() {
        JsonElement element = new JsonPrimitive("test");
        jsonTreeReader = new JsonTreeReader(element);
    }

    @Test
    public void testBeginArray() throws IOException {
        jsonTreeReader.beginArray();
        assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
    }

    @Test
    public void testEndArray() throws IOException {
        jsonTreeReader.beginArray();
        jsonTreeReader.endArray();
        assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
    }

    @Test
    public void testBeginObject() throws IOException {
        jsonTreeReader.beginObject();
        assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    }

    @Test
    public void testEndObject() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.endObject();
        assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
    }

    @Test
    public void testHasNext() throws IOException {
        assertTrue(jsonTreeReader.hasNext());
    }

    @Test
    public void testPeek() throws IOException {
        assertEquals(JsonToken.STRING, jsonTreeReader.peek());
    }

    @Test
    public void testNextName() throws IOException {
        assertEquals("test", jsonTreeReader.nextName());
    }

    @Test
    public void testNextString() throws IOException {
        assertEquals("test", jsonTreeReader.nextString());
    }

    @Test
    public void testNextBoolean() throws IOException {
        assertFalse(jsonTreeReader.nextBoolean());
    }

    @Test
    public void testNextNull() throws IOException {
        jsonTreeReader.nextNull();
        assertEquals(JsonToken.NULL, jsonTreeReader.peek());
    }

    @Test
    public void testNextDouble() throws IOException {
        double result = jsonTreeReader.nextDouble();
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testNextLong() throws IOException {
        long result = jsonTreeReader.nextLong();
        assertEquals(0L, result);
    }

    @Test
    public void testNextInt() throws IOException {
        int result = jsonTreeReader.nextInt();
        assertEquals(0, result);
    }

    @Test
    public void testClose() throws IOException {
        jsonTreeReader.close();
        assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
    }

    @Test
    public void testSkipValue() throws IOException {
        jsonTreeReader.skipValue();
        assertEquals(JsonToken.NULL, jsonTreeReader.peek());
    }

    @Test
    public void testPromoteNameToValue() throws IOException {
        jsonTreeReader.promoteNameToValue();
        assertEquals(JsonToken.STRING, jsonTreeReader.peek());
    }

    @Test
    public void testGetPath() throws IOException {
        String path = jsonTreeReader.getPath();
        assertEquals("$", path);
    }
}