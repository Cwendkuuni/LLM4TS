import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JsonTreeReaderTest {

    private JsonTreeReader jsonTreeReader;

    @Before
    public void setup() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "value");

        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive("item"));

        jsonObject.add("array", jsonArray);
        jsonObject.add("nullValue", JsonNull.INSTANCE);

        JsonParser jsonParser = new JsonParser();
        String jsonString = jsonObject.toString();
        jsonTreeReader = new JsonTreeReader(jsonParser.parse(new StringReader(jsonString)));
    }

    @Test
    public void testBeginArray_validArray_startsArray() throws IOException {
        jsonTreeReader.beginArray();
        assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testBeginArray_invalidArray_throwsException() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.beginArray();
    }

    @Test
    public void testEndArray_validArray_endsArray() throws IOException {
        jsonTreeReader.beginArray();
        jsonTreeReader.endArray();
        assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArray_invalidArray_throwsException() throws IOException {
        jsonTreeReader.endArray();
    }

    @Test
    public void testBeginObject_validObject_startsObject() throws IOException {
        jsonTreeReader.beginObject();
        assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testBeginObject_invalidObject_throwsException() throws IOException {
        jsonTreeReader.beginArray();
        jsonTreeReader.beginObject();
    }

    @Test
    public void testEndObject_validObject_endsObject() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.endObject();
        assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObject_invalidObject_throwsException() throws IOException {
        jsonTreeReader.endObject();
    }

    @Test
    public void testHasNext_validObject_returnsTrue() throws IOException {
        assertTrue(jsonTreeReader.hasNext());
    }

    @Test
    public void testHasNext_validArray_returnsTrue() throws IOException {
        jsonTreeReader.beginArray();
        assertTrue(jsonTreeReader.hasNext());
    }

    @Test
    public void testHasNext_endArray_returnsFalse() throws IOException {
        jsonTreeReader.beginArray();
        jsonTreeReader.endArray();
        assertFalse(jsonTreeReader.hasNext());
    }

    @Test
    public void testPeek_validObject_returnsBeginObject() throws IOException {
        assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    }

    @Test
    public void testPeek_validArray_returnsBeginArray() throws IOException {
        jsonTreeReader.beginArray();
        assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
    }

    @Test
    public void testPeek_emptyArray_returnsEndArray() throws IOException {
        jsonTreeReader.beginArray();
        jsonTreeReader.endArray();
        assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
    }

    @Test
    public void testNextName_validObject_returnsExpectedName() throws IOException {
        jsonTreeReader.beginObject();
        assertEquals("key", jsonTreeReader.nextName());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextName_invalidObject_throwsException() throws IOException {
        jsonTreeReader.nextName();
    }

    @Test
    public void testNextString_validString_returnsExpectedString() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();
        assertEquals("value", jsonTreeReader.nextString());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextString_invalidString_throwsException() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextString();
    }

    @Test
    public void testNextBoolean_validBoolean_returnsExpectedBoolean() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();
        jsonTreeReader.nextString();
        assertTrue(jsonTreeReader.nextBoolean());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextBoolean_invalidBoolean_throwsException() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextBoolean();
    }

    @Test
    public void testNextNull_validNull_returnsNull() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();
        jsonTreeReader.nextString();
        jsonTreeReader.nextBoolean();
        jsonTreeReader.nextNull();
        assertNull(jsonTreeReader.peek());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextNull_invalidNull_throwsException() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextNull();
    }

    @Test
    public void testNextDouble_validDouble_returnsExpectedDouble() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();
        jsonTreeReader.nextString();
        jsonTreeReader.nextBoolean();
        jsonTreeReader.nextNull();
        assertEquals(0.0, jsonTreeReader.nextDouble(), 0.0);
    }

    @Test(expected = IllegalStateException.class)
    public void testNextDouble_invalidDouble_throwsException() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextDouble();
    }

    @Test
    public void testNextLong_validLong_returnsExpectedLong() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();
        jsonTreeReader.nextString();
        jsonTreeReader.nextBoolean();
        jsonTreeReader.nextNull();
        jsonTreeReader.nextDouble();
        assertEquals(0, jsonTreeReader.nextLong());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextLong_invalidLong_throwsException() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextLong();
    }

    @Test
    public void testNextInt_validInt_returnsExpectedInt() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();
        jsonTreeReader.nextString();
        jsonTreeReader.nextBoolean();
        jsonTreeReader.nextNull();
        jsonTreeReader.nextDouble();
        jsonTreeReader.nextLong();
        assertEquals(0, jsonTreeReader.nextInt());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextInt_invalidInt_throwsException() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextInt();
    }

    @Test
    public void testClose_readerClosed() throws IOException {
        jsonTreeReader.close();
        assertTrue(jsonTreeReader.isClosed());
    }

    @Test
    public void testSkipValue_skipValueSuccessfully() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.skipValue();
        assertEquals(JsonToken.PRIMITIVE, jsonTreeReader.peek());
        assertEquals("null", jsonTreeReader.nextString());
    }

    @Test(expected = IllegalStateException.class)
    public void testSkipValue_invalidSkip_throwsException() throws IOException {
        jsonTreeReader.skipValue();
    }
}