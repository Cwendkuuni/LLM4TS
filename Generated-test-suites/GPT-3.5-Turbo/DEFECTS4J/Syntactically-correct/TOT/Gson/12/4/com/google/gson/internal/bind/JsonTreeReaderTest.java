import com.google.gson.*;
import com.google.gson.internal.bind.JsonTreeReader;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JsonTreeReaderTest {

    private Gson gson;
    
    @Before
    public void setUp() {
        gson = new Gson();
    }
    
    @Test
    public void testBeginArray() throws IOException {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonObject());
        JsonReader reader = new JsonTreeReader(jsonArray);

        reader.beginArray();

        assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
    }

    @Test
    public void testEndArray() throws IOException {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(new JsonObject());
        JsonReader reader = new JsonTreeReader(jsonArray);

        reader.beginArray();
        reader.endArray();

        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testBeginObject() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "value");
        JsonReader reader = new JsonTreeReader(jsonObject);

        reader.beginObject();

        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    }

    @Test
    public void testEndObject() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "value");
        JsonReader reader = new JsonTreeReader(jsonObject);

        reader.beginObject();
        reader.endObject();

        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testHasNext() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "value");
        JsonReader reader = new JsonTreeReader(jsonObject);

        assertTrue(reader.hasNext());
    }

    @Test
    public void testPeek() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "value");
        JsonReader reader = new JsonTreeReader(jsonObject);

        assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
    }

    @Test
    public void testNextName() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "value");
        JsonReader reader = new JsonTreeReader(jsonObject);

        String name = reader.nextName();

        assertEquals("key", name);
        assertEquals(JsonToken.STRING, reader.peek());
    }

    @Test
    public void testNextString() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "value");
        JsonReader reader = new JsonTreeReader(jsonObject);

        String value = reader.nextString();

        assertEquals("value", value);
        assertEquals(JsonToken.END_OBJECT, reader.peek());
    }

    @Test
    public void testNextBoolean() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", true);
        JsonReader reader = new JsonTreeReader(jsonObject);

        boolean value = reader.nextBoolean();

        assertTrue(value);
        assertEquals(JsonToken.END_OBJECT, reader.peek());
    }

    @Test
    public void testNextNull() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("key", JsonNull.INSTANCE);
        JsonReader reader = new JsonTreeReader(jsonObject);

        reader.nextNull();

        assertEquals(JsonToken.END_OBJECT, reader.peek());
    }

    @Test
    public void testNextDouble() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", 3.14);
        JsonReader reader = new JsonTreeReader(jsonObject);

        double value = reader.nextDouble();

        assertEquals(3.14, value, 0);
        assertEquals(JsonToken.END_OBJECT, reader.peek());
    }

    @Test
    public void testNextLong() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", 1234567890);
        JsonReader reader = new JsonTreeReader(jsonObject);

        long value = reader.nextLong();

        assertEquals(1234567890, value);
        assertEquals(JsonToken.END_OBJECT, reader.peek());
    }

    @Test
    public void testNextInt() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", 42);
        JsonReader reader = new JsonTreeReader(jsonObject);

        int value = reader.nextInt();

        assertEquals(42, value);
        assertEquals(JsonToken.END_OBJECT, reader.peek());
    }

    @Test
    public void testClose() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "value");
        JsonReader reader = new JsonTreeReader(jsonObject);

        reader.close();

        assertEquals(JsonToken.END_DOCUMENT, reader.peek());
    }

    @Test
    public void testSkipValue() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "value");
        JsonReader reader = new JsonTreeReader(jsonObject);

        reader.skipValue();

        assertEquals(JsonToken.END_OBJECT, reader.peek());
    }

    @Test(expected = NumberFormatException.class)
    public void testNextDouble_InvalidNumberFormat() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "invalidNumber");
        JsonReader reader = new JsonTreeReader(jsonObject);

        reader.nextDouble();
    }

    @Test
    public void testPromoteNameToValue() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "value");
        JsonReader reader = new JsonTreeReader(jsonObject);

        reader.promoteNameToValue();

        assertEquals(JsonToken.STRING, reader.peek());
    }

    @Test
    public void testGetPath() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "value");
        JsonReader reader = new JsonTreeReader(jsonObject);

        reader.beginObject();

        assertEquals("$.key", reader.getPath());
    }

    @Test
    public void testGetPath_Nested() throws IOException {
        JsonObject jsonObject = new JsonObject();
        JsonObject nestedObject = new JsonObject();
        nestedObject.addProperty("nestedKey", "nestedValue");
        jsonObject.add("key", nestedObject);
        JsonReader reader = new JsonTreeReader(jsonObject);

        reader.beginObject();
        reader.beginObject();

        assertEquals("$.key.nestedKey", reader.getPath());
    }

}