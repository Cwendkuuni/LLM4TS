import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

public class JsonTreeReaderTest {

    private JsonElement element;
    private JsonTreeReader jsonTreeReader;

    @Before
    public void setUp() {
        element = createJsonElement();
        jsonTreeReader = new JsonTreeReader(element);
    }

    @Test
    public void testBeginArray() throws IOException {
        jsonTreeReader.beginArray();
        Assert.assertTrue(jsonTreeReader.hasNext());
        Assert.assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
        Assert.assertEquals(0, jsonTreeReader.getPathIndices().length);
    }

    @Test
    public void testEndArray() throws IOException {
        jsonTreeReader.beginArray();
        jsonTreeReader.endArray();
        Assert.assertFalse(jsonTreeReader.hasNext());
        Assert.assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
        Assert.assertEquals(0, jsonTreeReader.getPathIndices().length);
    }

    @Test
    public void testBeginObject() throws IOException {
        jsonTreeReader.beginObject();
        Assert.assertTrue(jsonTreeReader.hasNext());
        Assert.assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
        Assert.assertEquals(0, jsonTreeReader.getPathIndices().length);
    }

    @Test
    public void testEndObject() throws IOException {
        jsonTreeReader.beginObject();
        jsonTreeReader.endObject();
        Assert.assertFalse(jsonTreeReader.hasNext());
        Assert.assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
        Assert.assertEquals(0, jsonTreeReader.getPathIndices().length);
    }

    @Test
    public void testHasNext() throws IOException {
        Assert.assertTrue(jsonTreeReader.hasNext());
        jsonTreeReader.nextString();
        Assert.assertTrue(jsonTreeReader.hasNext());
        jsonTreeReader.nextString();
        Assert.assertFalse(jsonTreeReader.hasNext());
    }

    @Test
    public void testPeek() throws IOException {
        Assert.assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
        jsonTreeReader.nextName();
        Assert.assertEquals(JsonToken.STRING, jsonTreeReader.peek());
        jsonTreeReader.nextString();
        Assert.assertEquals(JsonToken.NAME, jsonTreeReader.peek());
        jsonTreeReader.nextName();
        Assert.assertEquals(JsonToken.STRING, jsonTreeReader.peek());
        jsonTreeReader.nextString();
        Assert.assertEquals(JsonToken.BOOLEAN, jsonTreeReader.peek());
        jsonTreeReader.nextBoolean();
        Assert.assertEquals(JsonToken.NAME, jsonTreeReader.peek());
        jsonTreeReader.nextName();
        Assert.assertEquals(JsonToken.STRING, jsonTreeReader.peek());
        jsonTreeReader.nextString();
        Assert.assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
        jsonTreeReader.endObject();
        Assert.assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
    }

    @Test
    public void testNextName() throws IOException {
        Assert.assertEquals("name", jsonTreeReader.nextName());
        Assert.assertEquals("value", jsonTreeReader.getPathNames()[jsonTreeReader.getPathIndices().length - 1]);
    }

    @Test
    public void testNextString() throws IOException {
        Assert.assertEquals("value", jsonTreeReader.nextString());
        Assert.assertEquals("value1", jsonTreeReader.nextString("default"));
    }

    @Test
    public void testNextBoolean() throws IOException {
        Assert.assertTrue(jsonTreeReader.nextBoolean());
        Assert.assertFalse(jsonTreeReader.nextBoolean());
    }

    @Test
    public void testNextNull() throws IOException {
        jsonTreeReader.nextNull();
        //TODO: Check if null is read correctly
    }

    @Test
    public void testNextDouble() throws IOException {
        Assert.assertEquals(1.23456789012, jsonTreeReader.nextDouble(), 0.0);
    }

    @Test
    public void testNextLong() throws IOException {
        Assert.assertEquals(999999999999999999L, jsonTreeReader.nextLong());
    }

    @Test
    public void testNextInt() throws IOException {
        Assert.assertEquals(1234567890, jsonTreeReader.nextInt());
    }

    @Test
    public void testClose() throws IOException {
        jsonTreeReader.close();
        Assert.assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
    }

    @Test
    public void testSkipValue() throws IOException {
        jsonTreeReader.skipValue();
        //TODO: Check if value is skipped correctly
    }

    @Test
    public void testGetPath() throws IOException {
        Assert.assertEquals("$.name", jsonTreeReader.nextName());
        Assert.assertEquals("$.name", jsonTreeReader.getPath());
        Assert.assertEquals("value", jsonTreeReader.nextString());
        Assert.assertEquals("$.name", jsonTreeReader.getPath());
    }

    private JsonElement createJsonElement() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "value");
        jsonObject.addProperty("bool", true);
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonPrimitive(1));
        jsonArray.add(new JsonPrimitive(2));
        jsonObject.add("array", jsonArray);

        JsonObject innerObject = new JsonObject();
        innerObject.addProperty("innerName", "innerValue");
        jsonObject.add("object", innerObject);

        return jsonObject;
    }
}