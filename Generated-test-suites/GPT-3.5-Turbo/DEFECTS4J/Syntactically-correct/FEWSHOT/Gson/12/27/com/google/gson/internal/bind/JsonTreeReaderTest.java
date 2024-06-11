import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

public class JsonTreeReaderTest {
    private JsonTreeReader reader;

    @Before
    public void setup() {
        JsonElement jsonElement = new JsonPrimitive("test");
        reader = new JsonTreeReader(jsonElement);
    }

    @Test
    public void testBeginArray() throws IOException {
        reader.beginArray();
        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testBeginArrayIllegalState() throws IOException {
        reader.beginObject();
        reader.beginArray();
    }

    @Test
    public void testEndArray() throws IOException {
        reader.endArray();
        assertEquals(JsonToken.END_ARRAY, reader.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArrayIllegalState() throws IOException {
        reader.endObject();
        reader.endArray();
    }

    @Test
    public void testBeginObject() throws IOException {
        reader.beginObject();
        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testBeginObjectIllegalState() throws IOException {
        reader.beginArray();
        reader.beginObject();
    }

    @Test
    public void testEndObject() throws IOException {
        reader.endObject();
        assertEquals(JsonToken.END_OBJECT, reader.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObjectIllegalState() throws IOException {
        reader.endArray();
        reader.endObject();
    }

    @Test
    public void testHasNext() throws IOException {
        assertTrue(reader.hasNext());
        reader.beginObject();
        assertTrue(reader.hasNext());
        reader.endObject();
        assertFalse(reader.hasNext());
    }

    @Test
    public void testPeek() throws IOException {
        assertEquals(JsonToken.STRING, reader.peek());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextNameNoSuchElement() throws IOException {
        reader.nextName();
    }

    @Test
    public void testNextName() throws IOException {
        reader.beginObject();
        assertEquals(JsonToken.NAME, reader.peek());
        assertEquals("", reader.nextName());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextNameIllegalState() throws IOException {
        reader.beginArray();
        reader.nextName();
    }

    @Test(expected = IllegalStateException.class)
    public void testNextStringIllegalState() throws IOException {
        reader.endArray();
        reader.nextString();
    }

    @Test(expected = IllegalStateException.class)
    public void testNextBooleanIllegalState() throws IOException {
        reader.nextBoolean();
    }

    @Test(expected = IllegalStateException.class)
    public void testNextNullIllegalState() throws IOException {
        reader.nextNull();
    }

    @Test(expected = IllegalStateException.class)
    public void testNextDoubleIllegalState() throws IOException {
        reader.nextDouble();
    }

    @Test(expected = IllegalStateException.class)
    public void testNextLongIllegalState() throws IOException {
        reader.nextLong();
    }

    @Test(expected = IllegalStateException.class)
    public void testNextIntIllegalState() throws IOException {
        reader.nextInt();
    }

    @Test
    public void testClose() throws IOException {
        reader.close();
        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testSkipValueIllegalState() throws IOException {
        reader.skipValue();
    }

    @Test
    public void testToString() {
        assertEquals("JsonTreeReader", reader.toString());
    }

    @Test
    public void testGetPath() throws IOException {
        assertEquals("$", reader.getPath());

        reader.beginArray();
        assertEquals("$[0]", reader.getPath());

        reader.beginObject();
        assertEquals("$[0].", reader.getPath());

        reader.nextName();
        assertEquals("$[0].null", reader.getPath());

        reader.beginArray();
        assertEquals("$[0].null[0]", reader.getPath());

        reader.endArray();
        assertEquals("$[0].null[1]", reader.getPath());

        reader.endObject();
        assertEquals("$[1]", reader.getPath());

        reader.endArray();
        assertEquals("$", reader.getPath());
    }

    @Test
    public void testPromoteNameToValue() throws IOException {
        reader.beginObject();
        reader.nextName();
        reader.promoteNameToValue();

        assertEquals(JsonToken.STRING, reader.peek());
        assertEquals("test", reader.nextString());

        assertEquals(JsonToken.NAME, reader.peek());
        assertEquals("", reader.nextName());

        assertEquals(JsonToken.NULL, reader.peek());
    }
}