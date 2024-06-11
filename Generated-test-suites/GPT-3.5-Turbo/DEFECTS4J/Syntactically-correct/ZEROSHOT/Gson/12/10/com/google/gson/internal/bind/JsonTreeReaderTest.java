import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.*;

public class JsonTreeReaderTest {

    private JsonTreeReader jsonTreeReader;

    @Before
    public void setUp() {
        String json = "{'name':'John','age':30,'city':'New York'}";
        JsonElement element = new JsonPrimitive(json);
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
        assertTrue(jsonTreeReader.hasNext());
    }

    @Test
    public void testPeek() throws IOException {
        assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    }

    @Test
    public void testNextName() throws IOException {
        String name = jsonTreeReader.nextName();
        assertEquals("name", name);
    }

    @Test
    public void testNextString() throws IOException {
        String value = jsonTreeReader.nextString();
        assertEquals("John", value);
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
        assertEquals(30.0, value, 0.01);
    }

    @Test
    public void testNextLong() throws IOException {
        long value = jsonTreeReader.nextLong();
        assertEquals(30, value);
    }

    @Test
    public void testNextInt() throws IOException {
        int value = jsonTreeReader.nextInt();
        assertEquals(30, value);
    }

    @Test
    public void testClose() throws IOException {
        jsonTreeReader.close();
        assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
    }

    @Test
    public void testSkipValue() throws IOException {
        jsonTreeReader.skipValue();
        assertEquals(JsonToken.NAME, jsonTreeReader.peek());
    }

    @Test
    public void testGetPath() {
        String path = jsonTreeReader.getPath();
        assertEquals("$", path);
    }

    @Test
    public void testPromoteNameToValue() throws IOException {
        jsonTreeReader.promoteNameToValue();
        assertEquals(JsonToken.STRING, jsonTreeReader.peek());
    }
}