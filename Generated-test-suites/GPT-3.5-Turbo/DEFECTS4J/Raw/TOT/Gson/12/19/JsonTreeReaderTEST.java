import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import static org.junit.Assert.*;

public class JsonTreeReaderTest {

    private JsonTreeReader jsonTreeReader;

    @Before
    public void setup() {
        JsonElement element = createJsonElement();
        jsonTreeReader = new JsonTreeReader(element);
    }

    private JsonElement createJsonElement() {
        JsonObject object = new JsonObject();
        object.addProperty("name", "John");
        object.addProperty("age", 25);

        JsonArray array = new JsonArray();
        array.add(new JsonPrimitive("apple"));
        array.add(new JsonPrimitive("banana"));
        array.add(new JsonPrimitive("cherry"));

        object.add("fruits", array);

        return object;
    }

    @Test
    public void testBeginArray() throws Exception {
        jsonTreeReader.beginArray();
        assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
    }

    @Test
    public void testEndArray() throws Exception {
        jsonTreeReader.endArray();
        assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
    }

    @Test
    public void testBeginObject() throws Exception {
        jsonTreeReader.beginObject();
        assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    }

    @Test
    public void testEndObject() throws Exception {
        jsonTreeReader.endObject();
        assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
    }

    @Test
    public void testHasNext() throws Exception {
        assertTrue(jsonTreeReader.hasNext());
    }

    @Test
    public void testPeek() throws Exception {
        assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    }

    @Test
    public void testNextName() throws Exception {
        assertEquals("name", jsonTreeReader.nextName());
    }

    @Test
    public void testNextString() throws Exception {
        assertEquals("John", jsonTreeReader.nextString());
    }

    @Test
    public void testNextBoolean() throws Exception {
        assertTrue(jsonTreeReader.nextBoolean());
    }

    @Test
    public void testNextNull() throws Exception {
        jsonTreeReader.nextNull();
        assertEquals(JsonToken.NULL, jsonTreeReader.peek());
    }

    @Test
    public void testNextDouble() throws Exception {
        assertEquals(25.0, jsonTreeReader.nextDouble(), 0.0);
    }

    @Test
    public void testNextLong() throws Exception {
        assertEquals(25, jsonTreeReader.nextLong());
    }

    @Test
    public void testNextInt() throws Exception {
        assertEquals(25, jsonTreeReader.nextInt());
    }

    @Test
    public void testClose() throws Exception {
        jsonTreeReader.close();
        assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
    }

    @Test
    public void testSkipValue() throws Exception {
        jsonTreeReader.skipValue();
        assertEquals(JsonToken.NAME, jsonTreeReader.peek());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("JsonTreeReader", jsonTreeReader.toString());
    }

    @Test
    public void testGetPath() throws Exception {
        assertEquals("$.name", jsonTreeReader.getPath());
    }

    @Test
    public void testPromoteNameToValue() throws Exception {
        jsonTreeReader.promoteNameToValue();
        assertEquals(JsonToken.STRING, jsonTreeReader.peek());
    }
}