import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;

public class JsonTreeReaderTest {

    private JsonTreeReader jsonTreeReader;

    @Before
    public void setup() {
        JsonElement element = new JsonObject();
        jsonTreeReader = new JsonTreeReader(element);
    }

    @Test
    public void testBeginArray() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.beginArray();
        Assert.assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
    }

    @Test
    public void testEndArray() throws IOException {
        jsonTreeReader.beginArray();
        jsonTreeReader.endArray();
        Assert.assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
    }

    @Test
    public void testBeginObject() throws IOException {
        jsonTreeReader.beginObject();
        Assert.assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
    }

    @Test
    public void testEndObject() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.endObject();
        Assert.assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
    }

    @Test
    public void testHasNext() throws IOException {
        boolean hasNext = jsonTreeReader.hasNext();
        Assert.assertFalse(hasNext);
    }

    @Test
    public void testPeek() throws IOException {
        JsonToken token = jsonTreeReader.peek();
        Assert.assertEquals(JsonToken.END_DOCUMENT, token);
    }

    @Test
    public void testNextName() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.nextName();
        Assert.assertEquals("null", jsonTreeReader.getPath());
    }

    @Test
    public void testNextString() throws IOException {
        jsonTreeReader.beginArray();
        jsonTreeReader.nextString();
        Assert.assertEquals("null", jsonTreeReader.getPath());
    }

    @Test
    public void testNextBoolean() throws IOException {
        jsonTreeReader.beginArray();
        jsonTreeReader.nextBoolean();
        Assert.assertEquals("null", jsonTreeReader.getPath());
    }

    @Test
    public void testNextNull() throws IOException {
        jsonTreeReader.nextNull();
        Assert.assertEquals("null", jsonTreeReader.getPath());
    }

    @Test
    public void testNextDouble() throws IOException {
        jsonTreeReader.beginArray();
        jsonTreeReader.nextDouble();
        Assert.assertEquals("null", jsonTreeReader.getPath());
    }

    @Test
    public void testNextLong() throws IOException {
        jsonTreeReader.beginArray();
        jsonTreeReader.nextLong();
        Assert.assertEquals("null", jsonTreeReader.getPath());
    }

    @Test
    public void testNextInt() throws IOException {
        jsonTreeReader.beginArray();
        jsonTreeReader.nextInt();
        Assert.assertEquals("null", jsonTreeReader.getPath());
    }

    @Test
    public void testClose() throws IOException {
        jsonTreeReader.close();
        Assert.assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
    }

    @Test
    public void testSkipValue() throws IOException {
        jsonTreeReader.beginArray();
        jsonTreeReader.skipValue();
        Assert.assertEquals("null", jsonTreeReader.getPath());
    }

    @Test
    public void testGetPath() throws IOException {
        jsonTreeReader.beginArray();
        String path = jsonTreeReader.getPath();
        Assert.assertEquals("$[0]", path);
    }
}