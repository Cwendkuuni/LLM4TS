import com.google.gson.*;
import com.google.gson.internal.bind.JsonTreeReader;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JsonTreeReaderTest {

    private JsonTreeReader jsonTreeReader;

    @Before
    public void setUp() {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive("value1"));
        jsonArray.add(new JsonPrimitive("value2"));
        jsonArray.add(new JsonPrimitive("value3"));

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key1", "value1");
        jsonObject.addProperty("key2", "value2");
        jsonObject.addProperty("key3", "value3");

        JsonObject jsonElement = new JsonObject();
        jsonElement.add("array", jsonArray);
        jsonElement.add("object", jsonObject);

        jsonTreeReader = new JsonTreeReader(jsonElement);
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
        jsonTreeReader.beginArray();
        assertTrue(jsonTreeReader.hasNext());
        jsonTreeReader.peek();
        assertTrue(jsonTreeReader.hasNext());
        jsonTreeReader.nextString();
        assertTrue(jsonTreeReader.hasNext());
        jsonTreeReader.endArray();
        assertFalse(jsonTreeReader.hasNext());
    }

    @Test
    public void testPeek() throws IOException {
        assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());

        jsonTreeReader.beginArray();
        assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());

        jsonTreeReader.nextString();
        assertEquals(JsonToken.STRING, jsonTreeReader.peek());

        jsonTreeReader.endArray();
        assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
    }

    @Test
    public void testNextName() throws IOException {
        jsonTreeReader.beginObject();
        assertEquals("array", jsonTreeReader.nextName());
        assertEquals("object", jsonTreeReader.nextName());
        jsonTreeReader.endObject();
        assertNull(jsonTreeReader.nextName());
    }

    @Test
    public void testNextString() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();
        assertEquals("value1", jsonTreeReader.nextString());
    }

    @Test
    public void testNextBoolean() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();
        assertFalse(jsonTreeReader.nextBoolean());
    }

    @Test
    public void testNextNull() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();
        jsonTreeReader.skipValue();
        jsonTreeReader.nextNull();
        assertNull(jsonTreeReader.peek());
    }

    @Test
    public void testNextDouble() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();
        assertEquals(1.23, jsonTreeReader.nextDouble(), 0);
    }

    @Test
    public void testNextLong() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();
        assertEquals(123, jsonTreeReader.nextLong());
    }

    @Test
    public void testNextInt() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();
        assertEquals(123, jsonTreeReader.nextInt());
    }

    @Test
    public void testClose() throws IOException {
        jsonTreeReader.close();
        assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
    }

    @Test
    public void testSkipValue() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.skipValue();
        jsonTreeReader.nextNull();
        assertNull(jsonTreeReader.peek());
    }

    @Test
    public void testGetPath() throws IOException {
        assertEquals("$", jsonTreeReader.getPath());

        jsonTreeReader.beginArray();
        assertEquals("$[0]", jsonTreeReader.getPath());

        jsonTreeReader.nextString();
        assertEquals("$[1]", jsonTreeReader.getPath());

        jsonTreeReader.endArray();
        assertEquals("$[1]", jsonTreeReader.getPath());

        jsonTreeReader.endObject();
        assertEquals("$", jsonTreeReader.getPath());
    }

    @Test
    public void testPromoteNameToValue() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();
        jsonTreeReader.promoteNameToValue();
        assertEquals("key1", jsonTreeReader.nextString());
    }
}