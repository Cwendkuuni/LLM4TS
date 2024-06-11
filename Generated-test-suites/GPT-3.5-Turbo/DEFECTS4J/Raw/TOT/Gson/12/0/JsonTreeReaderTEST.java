import com.google.gson.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import static org.junit.Assert.*;

public class JsonTreeReaderTest {

    private JsonElement getJsonElement() {
        String json = "{\n" +
                "    \"name\": \"John\",\n" +
                "    \"age\": 30,\n" +
                "    \"address\": {\n" +
                "        \"street\": \"123 ABC Street\",\n" +
                "        \"city\": \"XYZ\",\n" +
                "        \"state\": \"ABC\"\n" +
                "    },\n" +
                "    \"phoneNumbers\": [\n" +
                "        \"1111111111\",\n" +
                "        \"2222222222\",\n" +
                "        \"3333333333\"\n" +
                "    ],\n" +
                "    \"isActive\": true,\n" +
                "    \"balance\": 100.50,\n" +
                "    \"nullValue\": null\n" +
                "}";
        JsonParser parser = new JsonParser();
        return parser.parse(json);
    }

    @Test
    public void testBeginArray() throws IOException {
        JsonElement element = getJsonElement();
        JsonTreeReader reader = new JsonTreeReader(element);
        reader.beginArray();

        assertTrue(reader.hasNext());
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    }

    @Test
    public void testEndArray() throws IOException {
        JsonElement element = getJsonElement();
        JsonTreeReader reader = new JsonTreeReader(element);
        reader.beginArray();
        reader.endArray();

        assertFalse(reader.hasNext());
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testBeginObject() throws IOException {
        JsonElement element = getJsonElement();
        JsonTreeReader reader = new JsonTreeReader(element);
        reader.beginObject();

        assertTrue(reader.hasNext());
        assertEquals(JsonToken.NAME, reader.peek());
    }

    @Test
    public void testEndObject() throws IOException {
        JsonElement element = getJsonElement();
        JsonTreeReader reader = new JsonTreeReader(element);
        reader.beginObject();
        reader.endObject();

        assertFalse(reader.hasNext());
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testHasNext() throws IOException {
        JsonElement element = getJsonElement();
        JsonTreeReader reader = new JsonTreeReader(element);

        assertTrue(reader.hasNext());
        reader.beginObject();
        assertTrue(reader.hasNext());
        reader.nextName();
        assertTrue(reader.hasNext());
        reader.nextString();
        assertTrue(reader.hasNext());
        reader.nextName();
        assertTrue(reader.hasNext());
        reader.beginObject();
        assertTrue(reader.hasNext());
        reader.endObject();
        assertTrue(reader.hasNext());
        reader.nextName();
        assertTrue(reader.hasNext());
        reader.beginArray();
        assertTrue(reader.hasNext());
        reader.endArray();
        assertTrue(reader.hasNext());
        reader.nextName();
        assertTrue(reader.hasNext());
        reader.nextBoolean();
        assertTrue(reader.hasNext());
        reader.nextName();
        assertTrue(reader.hasNext());
        reader.nextDouble();
        assertTrue(reader.hasNext());
        reader.nextName();
        assertTrue(reader.hasNext());
        reader.nextNull();

        assertFalse(reader.hasNext());
    }

    @Test
    public void testPeek() throws IOException {
        JsonElement element = getJsonElement();
        JsonTreeReader reader = new JsonTreeReader(element);

        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        reader.beginObject();
        assertEquals(JsonToken.NAME, reader.peek());
        reader.nextName();
        assertEquals(JsonToken.STRING, reader.peek());
        reader.nextString();
        assertEquals(JsonToken.NAME, reader.peek());
        reader.nextName();
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        reader.beginObject();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.endObject();
        assertEquals(JsonToken.NAME, reader.peek());
        reader.nextName();
        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
        reader.beginArray();
        assertEquals(JsonToken.END_ARRAY, reader.peek());
        reader.endArray();
        assertEquals(JsonToken.NAME, reader.peek());
        reader.nextName();
        assertEquals(JsonToken.BOOLEAN, reader.peek());
        reader.nextBoolean();
        assertEquals(JsonToken.NAME, reader.peek());
        reader.nextName();
        assertEquals(JsonToken.NUMBER, reader.peek());
        reader.nextDouble();
        assertEquals(JsonToken.NAME, reader.peek());
        reader.nextName();
        assertEquals(JsonToken.NULL, reader.peek());
        reader.nextNull();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
        reader.endObject();
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testNextName() throws IOException {
        JsonElement element = getJsonElement();
        JsonTreeReader reader = new JsonTreeReader(element);
        reader.beginObject();

        assertEquals("name", reader.nextName());
    }

    @Test
    public void testNextString() throws IOException {
        JsonElement element = getJsonElement();
        JsonTreeReader reader = new JsonTreeReader(element);
        reader.beginObject();
        reader.nextName();

        assertEquals("John", reader.nextString());
    }

    @Test
    public void testNextBoolean() throws IOException {
        JsonElement element = getJsonElement();
        JsonTreeReader reader = new JsonTreeReader(element);
        reader.beginObject();
        reader.nextName();
        reader.nextString();
        reader.nextName();

        assertTrue(reader.nextBoolean());
    }

    @Test
    public void testNextNull() throws IOException {
        JsonElement element = getJsonElement();
        JsonTreeReader reader = new JsonTreeReader(element);
        reader.beginObject();
        reader.nextName();
        reader.nextString();
        reader.nextName();
        reader.nextBoolean();
        reader.nextName();

        reader.nextNull();
        assertNull(reader.peekStack());
    }

    @Test
    public void testNextDouble() throws IOException {
        JsonElement element = getJsonElement();
        JsonTreeReader reader = new JsonTreeReader(element);
        reader.beginObject();
        reader.nextName();
        reader.nextString();
        reader.nextName();
        reader.nextBoolean();
        reader.nextName();
        reader.nextNull();
        reader.nextName();

        assertEquals(100.50, reader.nextDouble(), 0.0);
    }

    @Test
    public void testNextLong() throws IOException {
        JsonElement element = getJsonElement();
        JsonTreeReader reader = new JsonTreeReader(element);
        reader.beginObject();
        reader.nextName();
        reader.nextString();
        reader.nextName();
        reader.nextBoolean();
        reader.nextName();
        reader.nextNull();
        reader.nextName();
        reader.nextDouble();
        reader.nextName();

        assertEquals(30, reader.nextLong());
    }

    @Test
    public void testNextInt() throws IOException {
        JsonElement element = getJsonElement();
        JsonTreeReader reader = new JsonTreeReader(element);
        reader.beginObject();
        reader.nextName();
        reader.nextString();
        reader.nextName();
        reader.nextBoolean();
        reader.nextName();
        reader.nextNull();
        reader.nextName();
        reader.nextDouble();
        reader.nextName();
        reader.nextLong();
        reader.nextName();

        assertEquals(0, reader.nextInt()); // The value is not an integer, returns 0
    }

    @Test
    public void testClose() throws IOException {
        JsonElement element = getJsonElement();
        JsonTreeReader reader = new JsonTreeReader(element);
        reader.close();

        assertEquals("JsonReader is closed", reader.toString());
    }

    @Test
    public void testSkipValue() throws IOException {
        JsonElement element = getJsonElement();
        JsonTreeReader reader = new JsonTreeReader(element);
        reader.beginObject();
        reader.skipValue();

        assertEquals(JsonToken.NAME, reader.peek());
        assertEquals("name", reader.nextName());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}