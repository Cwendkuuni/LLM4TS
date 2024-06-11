import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

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
        assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
        assertFalse(jsonTreeReader.hasNext());
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
        assertFalse(jsonTreeReader.hasNext());
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
    public void testNextName() throws IOException {
        jsonTreeReader.beginObject();
        String name = jsonTreeReader.nextName();
        assertEquals("", name);
        assertEquals(JsonToken.NAME, jsonTreeReader.peek());
    }

    @Test
    public void testNextString() throws IOException {
        StringReader reader = new StringReader("\"test\"");
        jsonTreeReader = new JsonTreeReader(jsonTreeReader.jsonObject);
        jsonTreeReader.reader = new JsonReader(reader);
        String value = jsonTreeReader.nextString();
        assertEquals("test", value);
        assertEquals(JsonToken.STRING, jsonTreeReader.peek());
    }

    @Test
    public void testNextBoolean() throws IOException {
        StringReader reader = new StringReader("true");
        jsonTreeReader = new JsonTreeReader(jsonTreeReader.jsonObject);
        jsonTreeReader.reader = new JsonReader(reader);
        boolean value = jsonTreeReader.nextBoolean();
        assertTrue(value);
        assertEquals(JsonToken.BOOLEAN, jsonTreeReader.peek());
    }

    @Test
    public void testNextNull() throws IOException {
        StringReader reader = new StringReader("null");
        jsonTreeReader = new JsonTreeReader(jsonTreeReader.jsonObject);
        jsonTreeReader.reader = new JsonReader(reader);
        jsonTreeReader.nextNull();
        assertEquals(JsonToken.NULL, jsonTreeReader.peek());
    }

    @Test
    public void testNextDouble() throws IOException {
        StringReader reader = new StringReader("1.23");
        jsonTreeReader = new JsonTreeReader(jsonTreeReader.jsonObject);
        jsonTreeReader.reader = new JsonReader(reader);
        double value = jsonTreeReader.nextDouble();
        assertEquals(1.23, value, 0.001);
        assertEquals(JsonToken.NUMBER, jsonTreeReader.peek());
    }

    @Test
    public void testNextLong() throws IOException {
        StringReader reader = new StringReader("1234567890");
        jsonTreeReader = new JsonTreeReader(jsonTreeReader.jsonObject);
        jsonTreeReader.reader = new JsonReader(reader);
        long value = jsonTreeReader.nextLong();
        assertEquals(1234567890L, value);
        assertEquals(JsonToken.NUMBER, jsonTreeReader.peek());
    }

    @Test
    public void testNextInt() throws IOException {
        StringReader reader = new StringReader("123");
        jsonTreeReader = new JsonTreeReader(jsonTreeReader.jsonObject);
        jsonTreeReader.reader = new JsonReader(reader);
        int value = jsonTreeReader.nextInt();
        assertEquals(123, value);
        assertEquals(JsonToken.NUMBER, jsonTreeReader.peek());
    }

    @Test
    public void testClose() throws IOException {
        jsonTreeReader.close();
        assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
    }

    @Test
    public void testSkipValue() throws IOException {
        jsonTreeReader.skipValue();
        assertEquals("null", jsonTreeReader.getPath());
    }

    @Test
    public void testPromoteNameToValue() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();
        jsonTreeReader.promoteNameToValue();
        assertEquals("null", jsonTreeReader.getPath());
        assertEquals(JsonToken.NULL, jsonTreeReader.peek());
    }
}

