import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
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
    public void setUp() {
        JsonElement element = new JsonPrimitive("test");
        jsonTreeReader = new JsonTreeReader(element);
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
        assertEquals(JsonToken.STRING, token);
    }

    @Test
    public void testNextName() throws IOException {
        String name = jsonTreeReader.nextName();
        assertEquals("null", name);
    }

    @Test
    public void testNextString() throws IOException {
        String value = jsonTreeReader.nextString();
        assertEquals("test", value);
    }

    @Test
    public void testNextBoolean() throws IOException {
        boolean value = jsonTreeReader.nextBoolean();
        assertFalse(value);
    }

    @Test
    public void testNextNull() throws IOException {
        jsonTreeReader.nextNull();
        JsonToken token = jsonTreeReader.peek();
        assertEquals(JsonToken.NULL, token);
    }

    @Test
    public void testNextDouble() throws IOException {
        double value = jsonTreeReader.nextDouble();
        assertEquals(0.0, value, 0);
    }

    @Test
    public void testNextLong() throws IOException {
        long value = jsonTreeReader.nextLong();
        assertEquals(0, value);
    }

    @Test
    public void testNextInt() throws IOException {
        int value = jsonTreeReader.nextInt();
        assertEquals(0, value);
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
    public void testPromoteNameToValue() throws IOException {
        jsonTreeReader.promoteNameToValue();
        JsonToken token = jsonTreeReader.peek();
        assertEquals(JsonToken.STRING, token);
    }

    @Test
    public void testGetPath() throws IOException {
        String path = jsonTreeReader.getPath();
        assertEquals("$.null", path);
    }

    @Test
    public void testToString() {
        String result = jsonTreeReader.toString();
        assertEquals("JsonTreeReader", result);
    }
}