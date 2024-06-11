import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;

public class JsonTreeReaderTest {

    private JsonTreeReader reader;

    @Test
    public void testBeginArray() throws IOException {
        JsonArray jsonArray = new JsonArray();
        reader = new JsonTreeReader(jsonArray);
        reader.beginArray();
        Assert.assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
        Assert.assertEquals(0, reader.stackSize);
    }

    @Test
    public void testEndArray() throws IOException {
        JsonArray jsonArray = new JsonArray();
        reader = new JsonTreeReader(jsonArray);
        reader.beginArray();
        reader.endArray();
        Assert.assertEquals(JsonToken.END_ARRAY, reader.peek());
        Assert.assertEquals(0, reader.stackSize);
    }

    @Test
    public void testBeginObject() throws IOException {
        JsonObject jsonObject = new JsonObject();
        reader = new JsonTreeReader(jsonObject);
        reader.beginObject();
        Assert.assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        Assert.assertEquals(0, reader.stackSize);
    }

    @Test
    public void testEndObject() throws IOException {
        JsonObject jsonObject = new JsonObject();
        reader = new JsonTreeReader(jsonObject);
        reader.beginObject();
        reader.endObject();
        Assert.assertEquals(JsonToken.END_OBJECT, reader.peek());
        Assert.assertEquals(0, reader.stackSize);
    }

    @Test
    public void testHasNext() throws IOException {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(1);
        jsonArray.add(2);
        reader = new JsonTreeReader(jsonArray);
        reader.beginArray();
        Assert.assertTrue(reader.hasNext());
        reader.skipValue();
        Assert.assertTrue(reader.hasNext());
        reader.skipValue();
        Assert.assertFalse(reader.hasNext());
    }

    @Test
    public void testPeek() throws IOException {
        JsonArray jsonArray = new JsonArray();
        reader = new JsonTreeReader(jsonArray);
        Assert.assertEquals(JsonToken.END_DOCUMENT, reader.peek());

        jsonArray.add(1);
        reader = new JsonTreeReader(jsonArray);
        reader.beginArray();
        Assert.assertEquals(JsonToken.NUMBER, reader.peek());

        JsonObject jsonObject = new JsonObject();
        reader = new JsonTreeReader(jsonObject);
        reader.beginObject();
        Assert.assertEquals(JsonToken.END_OBJECT, reader.peek());
    }

    @Test
    public void testNextName() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name1", "value1");
        jsonObject.addProperty("name2", "value2");
        reader = new JsonTreeReader(jsonObject);
        reader.beginObject();
        Assert.assertEquals("name1", reader.nextName());
        Assert.assertEquals("name2", reader.nextName());
    }

    @Test
    public void testNextString() throws IOException {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("string1");
        jsonArray.add("string2");
        reader = new JsonTreeReader(jsonArray);
        reader.beginArray();
        Assert.assertEquals("string1", reader.nextString());
        Assert.assertEquals("string2", reader.nextString());
    }

    @Test
    public void testNextBoolean() throws IOException {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(true);
        jsonArray.add(false);
        reader = new JsonTreeReader(jsonArray);
        reader.beginArray();
        Assert.assertTrue(reader.nextBoolean());
        Assert.assertFalse(reader.nextBoolean());
    }

    @Test
    public void testNextNull() throws IOException {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(JsonNull.INSTANCE);
        jsonArray.add(JsonNull.INSTANCE);
        reader = new JsonTreeReader(jsonArray);
        reader.beginArray();
        reader.nextNull();
        reader.nextNull();
    }

    @Test
    public void testNextDouble() throws IOException {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(3.14);
        jsonArray.add(2.71);
        reader = new JsonTreeReader(jsonArray);
        reader.beginArray();
        Assert.assertEquals(3.14, reader.nextDouble(), 0.001);
        Assert.assertEquals(2.71, reader.nextDouble(), 0.001);
    }

    @Test
    public void testNextLong() throws IOException {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(100L);
        jsonArray.add(200L);
        reader = new JsonTreeReader(jsonArray);
        reader.beginArray();
        Assert.assertEquals(100L, reader.nextLong());
        Assert.assertEquals(200L, reader.nextLong());
    }

    @Test
    public void testNextInt() throws IOException {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(1);
        jsonArray.add(2);
        reader = new JsonTreeReader(jsonArray);
        reader.beginArray();
        Assert.assertEquals(1, reader.nextInt());
        Assert.assertEquals(2, reader.nextInt());
    }

    @Test
    public void testClose() throws IOException {
        JsonObject jsonObject = new JsonObject();
        reader = new JsonTreeReader(jsonObject);
        reader.close();
        Assert.assertEquals(JsonToken.END_DOCUMENT, reader.peek());
        Assert.assertEquals(1, reader.stackSize);
    }

    @Test
    public void testSkipValue() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name1", "value1");
        jsonObject.addProperty("name2", "value2");
        reader = new JsonTreeReader(jsonObject);
        reader.beginObject();
        reader.skipValue();
        reader.skipValue();
    }

    @Test
    public void testPromoteNameToValue() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "value");
        reader = new JsonTreeReader(jsonObject);
        reader.beginObject();
        reader.promoteNameToValue();
        Assert.assertEquals(JsonToken.STRING, reader.peek());
    }

    @Test
    public void testGetPath() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "value");
        reader = new JsonTreeReader(jsonObject);
        reader.beginObject();
        reader.nextName();
        Assert.assertEquals("$['name']", reader.getPath());
    }
}