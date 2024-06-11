import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
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
        assertTrue(jsonTreeReader.hasNext());
        assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
    }

    @Test
    public void testEndArray() throws IOException {
        jsonTreeReader.beginArray();
        jsonTreeReader.endArray();
        assertFalse(jsonTreeReader.hasNext());
        assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
    }

    @Test
    public void testBeginObject() throws IOException {
        jsonTreeReader.beginObject();
        assertTrue(jsonTreeReader.hasNext());
        assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    }

    @Test
    public void testEndObject() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.endObject();
        assertFalse(jsonTreeReader.hasNext());
        assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
    }

    @Test
    public void testHasNext() throws IOException {
        assertFalse(jsonTreeReader.hasNext());
    }

    @Test
    public void testPeek() throws IOException {
        assertEquals(JsonToken.STRING, jsonTreeReader.peek());
    }

    @Test
    public void testNextName() throws IOException {
        jsonTreeReader.beginObject();
        String name = jsonTreeReader.nextName();
        assertEquals("key", name);
        assertEquals(JsonToken.VALUE, jsonTreeReader.peek());
    }

    @Test
    public void testNextString() throws IOException {
        jsonTreeReader.beginArray();
        String value = jsonTreeReader.nextString();
        assertEquals("value", value);
        assertFalse(jsonTreeReader.hasNext());
    }

    @Test
    public void testNextBoolean() throws IOException {
        jsonTreeReader.beginObject();
        boolean value = jsonTreeReader.nextBoolean();
        assertFalse(value);
        assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
    }

    @Test
    public void testNextNull() throws IOException {
        jsonTreeReader.nextNull();
        assertFalse(jsonTreeReader.hasNext());
    }

    @Test
    public void testNextDouble() throws IOException {
        jsonTreeReader.beginArray();
        double value = jsonTreeReader.nextDouble();
        assertEquals(10.5, value, 0);
        assertFalse(jsonTreeReader.hasNext());
    }

    @Test
    public void testNextLong() throws IOException {
        jsonTreeReader.beginObject();
        long value = jsonTreeReader.nextLong();
        assertEquals(100L, value);
        assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
    }

    @Test
    public void testNextInt() throws IOException {
        jsonTreeReader.beginArray();
        int value = jsonTreeReader.nextInt();
        assertEquals(5, value);
        assertFalse(jsonTreeReader.hasNext());
    }

    @Test
    public void testSkipValue() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.skipValue();
        assertFalse(jsonTreeReader.hasNext());
    }

    @Test
    public void testGetPath() throws IOException {
        jsonTreeReader.beginObject();
        assertEquals("$.key", jsonTreeReader.getPath());
    }

    @Test
    public void testPromoteNameToValue() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.promoteNameToValue();
        assertEquals(JsonToken.STRING, jsonTreeReader.peek());
        assertEquals("key", jsonTreeReader.nextString());
    }

    @Test
    public void testClose() throws IOException {
        jsonTreeReader.close();
        assertFalse(jsonTreeReader.hasNext());
    }

}