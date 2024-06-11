import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Before;
import org.junit.Test;

public class JsonTreeWriterTest {
    private JsonTreeWriter writer;

    @Before
    public void setup() {
        writer = new JsonTreeWriter();
    }

    @Test
    public void testBeginArray() {
        writer.beginArray();
        JsonElement element = writer.get();
        assertTrue(element instanceof JsonArray);
        assertTrue(element.getAsJsonArray().isEmpty());
    }

    @Test
    public void testEndArray() {
        writer.beginArray();
        writer.endArray();
        JsonElement element = writer.get();
        assertTrue(element instanceof JsonArray);
        assertTrue(element.getAsJsonArray().isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArrayWithoutBeginArray() {
        writer.endArray();
    }

    @Test
    public void testBeginObject() {
        writer.beginObject();
        JsonElement element = writer.get();
        assertTrue(element instanceof JsonObject);
        assertTrue(element.getAsJsonObject().entrySet().isEmpty());
    }

    @Test
    public void testEndObject() {
        writer.beginObject();
        writer.endObject();
        JsonElement element = writer.get();
        assertTrue(element instanceof JsonObject);
        assertTrue(element.getAsJsonObject().entrySet().isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObjectWithoutBeginObject() {
        writer.endObject();
    }

    @Test(expected = IllegalStateException.class)
    public void testNameWithoutBeginObject() {
        writer.name("test");
    }

    @Test
    public void testName() {
        writer.beginObject();
        writer.name("test");
        JsonElement element = writer.get();
        assertTrue(element instanceof JsonObject);
        assertTrue(element.getAsJsonObject().has("test"));
    }

    @Test(expected = IllegalStateException.class)
    public void testValueWithoutName() {
        writer.beginObject();
        writer.value("value");
    }

    @Test
    public void testStringValue() {
        writer.beginObject();
        writer.name("string");
        writer.value("value");
        JsonElement element = writer.get();
        assertTrue(element instanceof JsonObject);
        assertEquals("value", element.getAsJsonObject().get("string").getAsString());
    }

    @Test
    public void testNullValue() {
        writer.beginObject();
        writer.name("null");
        writer.nullValue();
        JsonElement element = writer.get();
        assertTrue(element instanceof JsonObject);
        assertTrue(element.getAsJsonObject().get("null").isJsonNull());
    }

    @Test
    public void testBooleanValue() {
        writer.beginObject();
        writer.name("boolean");
        writer.value(true);
        JsonElement element = writer.get();
        assertTrue(element instanceof JsonObject);
        assertEquals(true, element.getAsJsonObject().get("boolean").getAsBoolean());
    }

    @Test
    public void testDoubleValue() {
        writer.beginObject();
        writer.name("double");
        writer.value(3.14);
        JsonElement element = writer.get();
        assertTrue(element instanceof JsonObject);
        assertEquals(3.14, element.getAsJsonObject().get("double").getAsDouble(), 0.0001);
    }

    @Test
    public void testLongValue() {
        writer.beginObject();
        writer.name("long");
        writer.value(100L);
        JsonElement element = writer.get();
        assertTrue(element instanceof JsonObject);
        assertEquals(100L, element.getAsJsonObject().get("long").getAsLong());
    }

    @Test
    public void testNumberValue() {
        writer.beginObject();
        writer.name("number");
        writer.value(1.23);
        JsonElement element = writer.get();
        assertTrue(element instanceof JsonObject);
        assertEquals(1.23, element.getAsJsonObject().get("number").getAsDouble(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNumberValueWithNonLenientMode() {
        writer.setLenient(false);
        writer.beginObject();
        writer.name("number");
        writer.value(Double.NaN);
    }

    @Test
    public void testFlush() throws Exception {
        writer.beginObject();
        writer.flush(); // Flush should do nothing
    }

    @Test(expected = IOException.class)
    public void testCloseWithoutEndObject() throws Exception {
        writer.close();
    }

}