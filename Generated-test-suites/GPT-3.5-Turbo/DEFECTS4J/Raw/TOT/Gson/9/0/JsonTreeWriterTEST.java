
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonTreeWriterTest {

    private JsonTreeWriter writer;

    @Before
    public void setup() {
        writer = new JsonTreeWriter();
    }

    @Test
    public void testBeginArray() {
        writer.beginArray();
        JsonObject json = new JsonParser().parse(writer.get().toString()).getAsJsonObject();
        assertTrue(json.isJsonArray());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArray_WhenEmptyStack() {
        writer.endArray();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArray_WhenPendingNameNotNull() {
        writer.beginObject().name("test");
        writer.endArray();
    }

    @Test
    public void testEndArray_WhenValidStack() {
        writer.beginArray().value("value").endArray();
        JsonObject json = new JsonParser().parse(writer.get().toString()).getAsJsonObject();
        assertTrue(json.isJsonArray());
        assertTrue(json.getAsJsonArray().get(0).getAsString().equals("value"));
    }

    @Test
    public void testBeginObject() {
        writer.beginObject();
        JsonObject json = new JsonParser().parse(writer.get().toString()).getAsJsonObject();
        assertTrue(json.isJsonObject());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObject_WhenEmptyStack() {
        writer.endObject();
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObject_WhenPendingNameNotNull() {
        writer.beginArray().endObject();
        writer.endObject();
    }

    @Test
    public void testEndObject_WhenValidStack() {
        writer.beginObject().name("key").value("value").endObject();
        JsonObject json = new JsonParser().parse(writer.get().toString()).getAsJsonObject();
        assertTrue(json.isJsonObject());
        assertTrue(json.has("key"));
        assertTrue(json.get("key").getAsString().equals("value"));
    }

    @Test(expected = IllegalStateException.class)
    public void testName_WhenEmptyStack() {
        writer.name("test");
    }

    @Test(expected = IllegalStateException.class)
    public void testName_WhenPendingNameNotNull() {
        writer.beginObject().name("test").name("test2");
    }

    @Test
    public void testName_WhenValidStack() {
        writer.beginObject().name("key");
        JsonObject json = new JsonParser().parse(writer.get().toString()).getAsJsonObject();
        assertTrue(json.has("key"));
    }

    @Test
    public void testValue_String() {
        writer.value("value");
        JsonObject json = new JsonParser().parse(writer.get().toString()).getAsJsonObject();
        assertTrue(json.getAsString().equals("value"));
    }

    @Test
    public void testValue_Null() {
        writer.value((String) null);
        JsonObject json = new JsonParser().parse(writer.get().toString()).getAsJsonObject();
        assertTrue(json.isJsonNull());
    }

    @Test
    public void testValue_Boolean() {
        writer.value(true);
        JsonObject json = new JsonParser().parse(writer.get().toString()).getAsJsonObject();
        assertTrue(json.getAsBoolean());
    }

    @Test
    public void testValue_Double() {
        writer.value(1.234);
        JsonObject json = new JsonParser().parse(writer.get().toString()).getAsJsonObject();
        assertTrue(json.getAsDouble() == 1.234);
    }

    @Test
    public void testValue_Long() {
        writer.value(1234567890L);
        JsonObject json = new JsonParser().parse(writer.get().toString()).getAsJsonObject();
        assertTrue(json.getAsLong() == 1234567890L);
    }

    @Test
    public void testValue_Number() {
        writer.value(123.456);
        JsonObject json = new JsonParser().parse(writer.get().toString()).getAsJsonObject();
        assertTrue(json.getAsDouble() == 123.456);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValue_Double_WhenNaN() {
        writer.value(Double.NaN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValue_Double_WhenPositiveInfinity() {
        writer.value(Double.POSITIVE_INFINITY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValue_Double_WhenNegativeInfinity() {
        writer.value(Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testFlush() {
        // No behavior to test, as the method is empty
    }
    
    @Test(expected = IOException.class)
    public void testClose_WhenIncompleteDocument() throws IOException {
        writer.beginArray().endObject().close();
    }

    @Test
    public void testClose_WhenValidDocument() throws IOException {
        writer.beginArray().endArray().close();
        assertTrue(writer.get() == JsonNull.INSTANCE);
    }
}
