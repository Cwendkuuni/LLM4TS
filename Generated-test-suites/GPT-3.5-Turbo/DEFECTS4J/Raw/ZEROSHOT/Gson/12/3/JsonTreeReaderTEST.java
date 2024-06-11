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
    public void setUp() {
        JsonElement element = new JsonObject();
        jsonTreeReader = new JsonTreeReader(element);
    }

    @Test
    public void testBeginArray() throws IOException {
        jsonTreeReader.beginArray();
        assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
    }

    @Test
    public void testEndArray() throws IOException {
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
        jsonTreeReader.endObject();
        assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
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
        String name = jsonTreeReader.nextName();
        assertSame("", name);
    }

    @Test
    public void testNextString() throws IOException {
        String value = jsonTreeReader.nextString();
        assertSame("", value);
    }

    @Test
    public void testNextBoolean() throws IOException {
        boolean value = jsonTreeReader.nextBoolean();
        assertFalse(value);
    }

    @Test
    public void testNextNull() throws IOException {
        jsonTreeReader.nextNull();
        assertEquals(JsonToken.NULL, jsonTreeReader.peek());
    }

    @Test
    public void testNextDouble() throws IOException {
        double value = jsonTreeReader.nextDouble();
        assertEquals(0.0, value, 0.0);
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
}