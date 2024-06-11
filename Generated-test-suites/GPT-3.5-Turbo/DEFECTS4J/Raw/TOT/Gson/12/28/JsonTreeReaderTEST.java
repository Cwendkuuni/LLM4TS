import com.google.gson.*;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.stream.JsonToken;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.*;

public class JsonTreeReaderTest {

    private JsonTreeReader jsonTreeReader;
    private Gson gson;
    private JsonArray jsonArray;
    private JsonObject jsonObject;

    @Before
    public void setUp() {
        JsonParser parser = new JsonParser();
        String jsonArrayString = "[1, 2, 3, 4, 5]";
        jsonArray = parser.parse(jsonArrayString).getAsJsonArray();

        String jsonObjectString = "{\"name\":\"John\", \"age\":30, \"married\":true}";
        jsonObject = parser.parse(jsonObjectString).getAsJsonObject();

        jsonTreeReader = new JsonTreeReader(jsonArray);
        gson = new Gson();
    }

    @Test
    public void testBeginArray() throws Exception {
        jsonTreeReader.beginArray();
        assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
    }

    @Test
    public void testEndArray() throws Exception {
        jsonTreeReader.beginArray();
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
        jsonTreeReader.beginObject();
        jsonTreeReader.endObject();
        assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
    }

    @Test
    public void testHasNext() throws Exception {
        assertTrue(jsonTreeReader.hasNext());
        jsonTreeReader.beginArray();
        for (int i = 0; i < 5; i++) {
            assertTrue(jsonTreeReader.hasNext());
            jsonTreeReader.peek();
        }
        assertFalse(jsonTreeReader.hasNext());
    }

    @Test
    public void testPeek() throws Exception {
        assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
    }

    @Test
    public void testNextName() throws Exception {
        jsonTreeReader.beginObject();
        assertEquals("name", jsonTreeReader.nextName());
        assertEquals(JsonToken.STRING, jsonTreeReader.peek());
    }

    @Test
    public void testNextString() throws Exception {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();
        assertEquals("John", jsonTreeReader.nextString());
        assertEquals(JsonToken.NAME, jsonTreeReader.peek());
    }

    @Test
    public void testNextBoolean() throws Exception {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();
        jsonTreeReader.nextString();
        assertEquals(true, jsonTreeReader.nextBoolean());
        assertEquals(JsonToken.NAME, jsonTreeReader.peek());
    }

    @Test
    public void testNextNull() throws Exception {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();
        jsonTreeReader.nextString();
        jsonTreeReader.nextBoolean();
        jsonTreeReader.nextNull();
        assertEquals(JsonToken.NAME, jsonTreeReader.peek());
    }

    @Test
    public void testNextDouble() throws Exception {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();
        jsonTreeReader.nextString();
        jsonTreeReader.nextBoolean();
        jsonTreeReader.nextNull();
        assertEquals(3.5, jsonTreeReader.nextDouble(), 0.0);
        assertEquals(JsonToken.NAME, jsonTreeReader.peek());
    }

    @Test
    public void testNextLong() throws Exception {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();
        jsonTreeReader.nextString();
        jsonTreeReader.nextBoolean();
        jsonTreeReader.nextNull();
        jsonTreeReader.nextDouble();
        assertEquals(1000L, jsonTreeReader.nextLong());
        assertEquals(JsonToken.NAME, jsonTreeReader.peek());
    }

    @Test
    public void testNextInt() throws Exception {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();
        jsonTreeReader.nextString();
        jsonTreeReader.nextBoolean();
        jsonTreeReader.nextNull();
        jsonTreeReader.nextDouble();
        jsonTreeReader.nextLong();
        assertEquals(10, jsonTreeReader.nextInt());
        assertEquals(JsonToken.NAME, jsonTreeReader.peek());
    }

    @Test
    public void testClose() throws Exception {
        jsonTreeReader.close();
        assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
    }

    @Test
    public void testSkipValue() throws Exception {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();
        jsonTreeReader.nextString();
        jsonTreeReader.nextBoolean();
        jsonTreeReader.nextNull();
        jsonTreeReader.nextDouble();
        jsonTreeReader.nextLong();
        jsonTreeReader.nextInt();
        jsonTreeReader.skipValue();
        assertEquals(JsonToken.NAME, jsonTreeReader.peek());
    }

    @Test
    public void testGetPath() throws Exception {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();
        jsonTreeReader.nextString();
        jsonTreeReader.nextBoolean();
        jsonTreeReader.nextNull();
        jsonTreeReader.nextDouble();
        jsonTreeReader.nextLong();
        jsonTreeReader.nextInt();
        jsonTreeReader.skipValue();
        assertEquals("$[0]", jsonTreeReader.getPath());
    }

    @Test
    public void testPromoteNameToValue() throws Exception {
        jsonTreeReader.beginObject();
        jsonTreeReader.promoteNameToValue();
        jsonTreeReader.promoteNameToValue();
        assertEquals(JsonToken.NAME, jsonTreeReader.peek());
    }

}