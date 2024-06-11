import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class JsonTreeReaderTest {

    private JsonTreeReader jsonTreeReader;

    @Before
    public void setup() {
        JsonElement jsonElement = new JsonObject();
        jsonTreeReader = new JsonTreeReader(jsonElement);
    }

    @Test
    public void testBeginArray() throws IOException {
        jsonTreeReader.beginArray();
        JsonToken token = jsonTreeReader.peek();
        assertEquals(JsonToken.BEGIN_ARRAY, token);
    }

    @Test
    public void testEndArray() throws IOException {
        jsonTreeReader.endArray();
        JsonToken token = jsonTreeReader.peek();
        assertEquals(JsonToken.END_ARRAY, token);
    }

    @Test
    public void testBeginObject() throws IOException {
        jsonTreeReader.beginObject();
        JsonToken token = jsonTreeReader.peek();
        assertEquals(JsonToken.BEGIN_OBJECT, token);
    }

    @Test
    public void testEndObject() throws IOException {
        jsonTreeReader.endObject();
        JsonToken token = jsonTreeReader.peek();
        assertEquals(JsonToken.END_OBJECT, token);
    }

    @Test
    public void testHasNext() throws IOException {
        boolean hasNext = jsonTreeReader.hasNext();
        assertTrue(hasNext);
    }

    @Test
    public void testPeek() throws IOException {
        JsonToken token = jsonTreeReader.peek();
        assertEquals(JsonToken.BEGIN_OBJECT, token);
    }

    @Test
    public void testNextName() throws IOException {
        String name = jsonTreeReader.nextName();
        assertNull(name);
    }

    @Test
    public void testNextString() throws IOException {
        String string = jsonTreeReader.nextString();
        assertNull(string);
    }

    @Test
    public void testNextBoolean() throws IOException {
        boolean bool = jsonTreeReader.nextBoolean();
        assertFalse(bool);
    }

    @Test
    public void testNextNull() throws IOException {
        jsonTreeReader.nextNull();
        JsonToken token = jsonTreeReader.peek();
        assertEquals(JsonToken.NULL, token);
    }

    @Test
    public void testNextDouble() throws IOException {
        double d = jsonTreeReader.nextDouble();
        assertEquals(0.0, d, 0);
    }

    @Test
    public void testNextLong() throws IOException {
        long l = jsonTreeReader.nextLong();
        assertEquals(0L, l);
    }

    @Test
    public void testNextInt() throws IOException {
        int i = jsonTreeReader.nextInt();
        assertEquals(0, i);
    }

    @Test
    public void testClose() throws IOException {
        jsonTreeReader.close();
        JsonToken token = jsonTreeReader.peek();
        assertEquals(JsonToken.END_DOCUMENT, token);
    }

    @Test
    public void testSkipValue() throws IOException {
        jsonTreeReader.skipValue();
        JsonToken token = jsonTreeReader.peek();
        assertEquals(JsonToken.NULL, token);
    }

    @Test
    public void testToString() {
        String result = jsonTreeReader.toString();
        String expected = "JsonTreeReader";
        assertEquals(expected, result);
    }

    @Test
    public void testPromoteNameToValue() throws IOException {
        jsonTreeReader.promoteNameToValue();
        JsonToken token = jsonTreeReader.peek();
        assertEquals(JsonToken.NULL, token);
    }

    @Test
    public void testGetPath() {
        String path = jsonTreeReader.getPath();
        String expected = "$";
        assertEquals(expected, path);
    }

    @Test
    public void testInvalidPathIndices() {
        jsonTreeReader.promoteNameToValue();
        jsonTreeReader.beginArray();
        jsonTreeReader.endArray();
        String path = jsonTreeReader.getPath();
        String expected = "$[0]";
        assertEquals(expected, path);
    }
}