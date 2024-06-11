import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.*;

public class JsonTreeReaderTest {

    private JsonTreeReader jsonTreeReader;

    @Before
    public void setUp() {
        String json = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
        JsonElement jsonElement = new JsonParser().parse(json);
        jsonTreeReader = new JsonTreeReader(jsonElement);
    }

    @Test
    public void testBeginArray() throws IOException {
        jsonTreeReader.beginArray();
        assertEquals("$[0]", jsonTreeReader.getPath());
    }

    @Test
    public void testEndArray() throws IOException {
        jsonTreeReader.beginArray();
        jsonTreeReader.endArray();
        assertEquals("$", jsonTreeReader.getPath());
    }

    @Test
    public void testBeginObject() throws IOException {
        jsonTreeReader.beginObject();
        assertEquals("$.name", jsonTreeReader.getPath());
    }

    @Test
    public void testEndObject() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.endObject();
        assertEquals("$", jsonTreeReader.getPath());
    }

    @Test
    public void testHasNext() throws IOException {
        assertTrue(jsonTreeReader.hasNext());
        jsonTreeReader.beginObject(); // enter the object
        assertTrue(jsonTreeReader.hasNext());
        jsonTreeReader.nextName(); // move to the next name
        assertTrue(jsonTreeReader.hasNext());
        jsonTreeReader.nextString(); // move to the value
        assertTrue(jsonTreeReader.hasNext());
        jsonTreeReader.nextName(); // move to the next name
        assertTrue(jsonTreeReader.hasNext());
        jsonTreeReader.nextLong(); // move to the value
        assertTrue(jsonTreeReader.hasNext());
        jsonTreeReader.nextName(); // move to the next name
        assertTrue(jsonTreeReader.hasNext());
        jsonTreeReader.nextString(); // move to the value
        assertFalse(jsonTreeReader.hasNext());
    }

    @Test
    public void testPeek() throws IOException {
        assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
        jsonTreeReader.beginObject(); // enter the object
        assertEquals(JsonToken.NAME, jsonTreeReader.peek());
        jsonTreeReader.nextName(); // move to the next name
        assertEquals(JsonToken.STRING, jsonTreeReader.peek());
        jsonTreeReader.nextString(); // move to the value
        assertEquals(JsonToken.NAME, jsonTreeReader.peek());
        jsonTreeReader.nextName(); // move to the next name
        assertEquals(JsonToken.NUMBER, jsonTreeReader.peek());
        jsonTreeReader.nextLong(); // move to the value
        assertEquals(JsonToken.NAME, jsonTreeReader.peek());
        jsonTreeReader.nextName(); // move to the next name
        assertEquals(JsonToken.STRING, jsonTreeReader.peek());
        jsonTreeReader.nextString(); // move to the value
        assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
    }

    @Test
    public void testNextName() throws IOException {
        String name = jsonTreeReader.nextName();
        assertEquals("name", name);
        assertEquals("$.age", jsonTreeReader.getPath());
    }

    @Test
    public void testNextString() throws IOException {
        jsonTreeReader.nextName();
        String value = jsonTreeReader.nextString();
        assertEquals("John", value);
        assertEquals("$.age", jsonTreeReader.getPath());
    }

    @Test
    public void testNextBoolean() throws IOException {
        jsonTreeReader.beginObject(); // enter the object
        jsonTreeReader.nextString(); // move to the value
        boolean value = jsonTreeReader.nextBoolean();
        assertTrue(value);
        assertEquals("$.age", jsonTreeReader.getPath());
    }

    @Test
    public void testNextNull() throws IOException {
        jsonTreeReader.beginObject(); // enter the object
        jsonTreeReader.nextString(); // move to the value
        jsonTreeReader.nextBoolean(); // move to the next value
        jsonTreeReader.nextNull(); // move to the value
        assertEquals("$.city", jsonTreeReader.getPath());
    }

    @Test
    public void testNextDouble() throws IOException {
        jsonTreeReader.beginObject(); // enter the object
        jsonTreeReader.nextString(); // move to the value
        jsonTreeReader.nextBoolean(); // move to the next value
        jsonTreeReader.nextNull(); // move to the next value
        double value = jsonTreeReader.nextDouble();
        assertEquals(0.0, value, 0.0);
        assertEquals("$", jsonTreeReader.getPath());
    }

    @Test
    public void testNextLong() throws IOException {
        jsonTreeReader.beginObject(); // enter the object
        jsonTreeReader.nextString(); // move to the value
        jsonTreeReader.nextBoolean(); // move to the next value
        jsonTreeReader.nextNull(); // move to the next value
        jsonTreeReader.nextDouble(); // move to the next value
        long value = jsonTreeReader.nextLong();
        assertEquals(0, value);
        assertEquals("$", jsonTreeReader.getPath());
    }

    @Test
    public void testNextInt() throws IOException {
        jsonTreeReader.beginObject(); // enter the object
        jsonTreeReader.nextString(); // move to the value
        jsonTreeReader.nextBoolean(); // move to the next value
        jsonTreeReader.nextNull(); // move to the next value
        jsonTreeReader.nextDouble(); // move to the next value
        jsonTreeReader.nextLong(); // move to the next value
        int value = jsonTreeReader.nextInt();
        assertEquals(0, value);
        assertEquals("$", jsonTreeReader.getPath());
    }

    @Test
    public void testClose() throws IOException {
        jsonTreeReader.close();
        assertEquals("JsonReader is closed", jsonTreeReader.getPath());
    }

    @Test
    public void testSkipValue() throws IOException {
        jsonTreeReader.skipValue();
        assertEquals("$.age", jsonTreeReader.getPath());
    }

    @Test
    public void testPromoteNameToValue() throws IOException {
        jsonTreeReader.promoteNameToValue();
        assertEquals("$.city", jsonTreeReader.getPath());
    }

}