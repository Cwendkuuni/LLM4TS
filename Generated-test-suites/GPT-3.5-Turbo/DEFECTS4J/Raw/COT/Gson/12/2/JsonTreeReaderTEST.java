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
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.*;

public class JsonTreeReaderTest {

    private JsonTreeReader jsonTreeReader;

    @Before
    public void setup() {
        JsonElement jsonElement = new JsonPrimitive("test");
        jsonTreeReader = new JsonTreeReader(jsonElement);
    }

    @Test
    public void beginArray_basicCase() throws IOException {
        jsonTreeReader.beginArray();
        assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void beginArray_invalidToken() throws IOException {
        jsonTreeReader.nextString();
        jsonTreeReader.beginArray();
    }

    @Test
    public void endArray_basicCase() throws IOException {
        jsonTreeReader.beginArray();
        jsonTreeReader.endArray();
        assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void endArray_invalidToken() throws IOException {
        jsonTreeReader.endArray();
    }

    @Test
    public void beginObject_basicCase() throws IOException {
        jsonTreeReader.beginObject();
        assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void beginObject_invalidToken() throws IOException {
        jsonTreeReader.beginArray();
        jsonTreeReader.beginObject();
    }

    @Test
    public void endObject_basicCase() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.endObject();
        assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void endObject_invalidToken() throws IOException {
        jsonTreeReader.endObject();
    }

    @Test
    public void hasNext_basicCase() throws IOException {
        assertTrue(jsonTreeReader.hasNext());
    }

    @Test
    public void peek_basicCase() throws IOException {
        assertEquals(JsonToken.STRING, jsonTreeReader.peek());
    }

    @Test
    public void peek_endDocument() throws IOException {
        jsonTreeReader.endObject();
        assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
    }

    @Test
    public void nextName_basicCase() throws IOException {
        jsonTreeReader.beginObject();
        assertEquals("test", jsonTreeReader.nextName());
    }

    @Test(expected = IllegalStateException.class)
    public void nextName_invalidToken() throws IOException {
        jsonTreeReader.nextString();
        jsonTreeReader.nextName();
    }

    @Test
    public void nextString_basicCase() throws IOException {
        assertEquals("test", jsonTreeReader.nextString());
    }

    @Test(expected = IllegalStateException.class)
    public void nextString_invalidToken() throws IOException {
        jsonTreeReader.beginArray();
        jsonTreeReader.nextString();
    }

    @Test(expected = IllegalStateException.class)
    public void nextString_invalidNumberToken() throws IOException {
        JsonElement jsonElement = new JsonPrimitive(123);
        jsonTreeReader = new JsonTreeReader(jsonElement);

        jsonTreeReader.nextString();
    }

    @Test
    public void nextBoolean_basicCase() throws IOException {
        JsonElement jsonElement = new JsonPrimitive(true);
        jsonTreeReader = new JsonTreeReader(jsonElement);

        assertEquals(true, jsonTreeReader.nextBoolean());
    }

    @Test(expected = IllegalStateException.class)
    public void nextBoolean_invalidToken() throws IOException {
        jsonTreeReader.nextBoolean();
    }

    @Test
    public void nextNull_basicCase() throws IOException {
        JsonElement jsonElement = JsonNull.INSTANCE;
        jsonTreeReader = new JsonTreeReader(jsonElement);

        jsonTreeReader.nextNull();
    }

    @Test(expected = IllegalStateException.class)
    public void nextNull_invalidToken() throws IOException {
        jsonTreeReader.nextNull();
    }

    @Test
    public void nextDouble_basicCase() throws IOException {
        JsonElement jsonElement = new JsonPrimitive(10.5);
        jsonTreeReader = new JsonTreeReader(jsonElement);

        assertEquals(10.5, jsonTreeReader.nextDouble(), 0.0);
    }

    @Test(expected = IllegalStateException.class)
    public void nextDouble_invalidToken() throws IOException {
        jsonTreeReader.nextDouble();
    }

    @Test(expected = NumberFormatException.class)
    public void nextDouble_notLenient() throws IOException {
        JsonElement jsonElement = new JsonPrimitive(Double.NaN);
        jsonTreeReader = new JsonTreeReader(jsonElement);

        jsonTreeReader.nextDouble();
    }

    @Test
    public void nextLong_basicCase() throws IOException {
        JsonElement jsonElement = new JsonPrimitive(Long.MAX_VALUE);
        jsonTreeReader = new JsonTreeReader(jsonElement);

        assertEquals(Long.MAX_VALUE, jsonTreeReader.nextLong());
    }

    @Test(expected = IllegalStateException.class)
    public void nextLong_invalidToken() throws IOException {
        jsonTreeReader.nextLong();
    }

    @Test
    public void nextInt_basicCase() throws IOException {
        JsonElement jsonElement = new JsonPrimitive(123);
        jsonTreeReader = new JsonTreeReader(jsonElement);

        assertEquals(123, jsonTreeReader.nextInt());
    }

    @Test(expected = IllegalStateException.class)
    public void nextInt_invalidToken() throws IOException {
        jsonTreeReader.nextInt();
    }

    @Test
    public void close_basicCase() throws IOException {
        jsonTreeReader.close();
        assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
    }

    @Test
    public void skipValue_nameToken() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();
        jsonTreeReader.skipValue();
        assertEquals(JsonToken.NAME, jsonTreeReader.peek());
    }

    @Test
    public void skipValue_valueToken() throws IOException {
        jsonTreeReader.beginArray();
        jsonTreeReader.skipValue();
        assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
    }

    @Test
    public void getPath_basicCase() throws IOException {
        assertEquals("$", jsonTreeReader.getPath());
    }

    @Test
    public void toString_basicCase() {
        assertEquals("JsonTreeReader", jsonTreeReader.toString());
    }

    @Test
    public void promoteNameToValue_basicCase() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();
        jsonTreeReader.promoteNameToValue();
        assertEquals(JsonToken.STRING, jsonTreeReader.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void promoteNameToValue_invalidToken() throws IOException {
        jsonTreeReader.promoteNameToValue();
    }

    @Test
    public void push_basicCase() {
        jsonTreeReader.push("test");
        assertEquals("test", jsonTreeReader.peekStack());
    }
}