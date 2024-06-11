
import com.google.gson.internal.bind.JsonTreeWriter;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JsonTreeWriterTest {

    private JsonTreeWriter jsonTreeWriter;

    @Before
    public void setUp() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    public void testBeginArray() throws IOException {
        // Test method: beginArray()
        JsonArray jsonArray = new JsonArray();
        jsonTreeWriter.beginArray();
        jsonTreeWriter.endArray();
        JsonElement result = jsonTreeWriter.get();
        assertEquals(jsonArray, result);
    }

    @Test
    public void testEndArray() throws IOException {
        // Test method: endArray()
        jsonTreeWriter.beginArray();
        jsonTreeWriter.endArray();
        JsonElement result = jsonTreeWriter.get();
        assertTrue(result.isJsonArray());
    }

    @Test
    public void testBeginObject() throws IOException {
        // Test method: beginObject()
        JsonObject jsonObject = new JsonObject();
        jsonTreeWriter.beginObject();
        jsonTreeWriter.endObject();
        JsonElement result = jsonTreeWriter.get();
        assertEquals(jsonObject, result);
    }

    @Test
    public void testEndObject() throws IOException {
        // Test method: endObject()
        jsonTreeWriter.beginObject();
        jsonTreeWriter.endObject();
        JsonElement result = jsonTreeWriter.get();
        assertTrue(result.isJsonObject());
    }

    @Test
    public void testName() throws IOException {
        // Test method: name(String name)
        JsonObject jsonObject = new JsonObject();
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("key");
        jsonTreeWriter.value("value");
        jsonTreeWriter.endObject();
        JsonElement result = jsonTreeWriter.get();
        jsonObject.addProperty("key", "value");
        assertEquals(jsonObject, result);
    }

    @Test
    public void testValueString() throws IOException {
        // Test method: value(String value)
        jsonTreeWriter.value("value");
        JsonElement result = jsonTreeWriter.get();
        assertTrue(result.isJsonPrimitive());
        assertTrue(result.getAsJsonPrimitive().isString());
        assertEquals("value", result.getAsString());
    }

    @Test
    public void testNullValue() throws IOException {
        // Test method: nullValue()
        jsonTreeWriter.nullValue();
        JsonElement result = jsonTreeWriter.get();
        assertTrue(result.isJsonNull());
    }

    @Test
    public void testValueBoolean() throws IOException {
        // Test method: value(boolean value)
        jsonTreeWriter.value(true);
        JsonElement result = jsonTreeWriter.get();
        assertTrue(result.isJsonPrimitive());
        assertTrue(result.getAsJsonPrimitive().isBoolean());
        assertTrue(result.getAsBoolean());
    }

    @Test
    public void testValueDouble() throws IOException {
        // Test method: value(double value)
        double value = 1.23;
        jsonTreeWriter.value(value);
        JsonElement result = jsonTreeWriter.get();
        assertTrue(result.isJsonPrimitive());
        assertTrue(result.getAsJsonPrimitive().isNumber());
        assertEquals(value, result.getAsDouble(), 0.0);
    }

    @Test
    public void testValueLong() throws IOException {
        // Test method: value(long value)
        long value = 123;
        jsonTreeWriter.value(value);
        JsonElement result = jsonTreeWriter.get();
        assertTrue(result.isJsonPrimitive());
        assertTrue(result.getAsJsonPrimitive().isNumber());
        assertEquals(value, result.getAsLong());
    }

    @Test
    public void testValueNumber() throws IOException {
        // Test method: value(Number value)
        Number value = 1.23;
        jsonTreeWriter.value(value);
        JsonElement result = jsonTreeWriter.get();
        assertTrue(result.isJsonPrimitive());
        assertTrue(result.getAsJsonPrimitive().isNumber());
        assertEquals(value, result.getAsNumber());
    }

    @Test(expected = IllegalStateException.class)
    public void testFlush() throws IOException {
        // Test method: flush()
        jsonTreeWriter.flush();
    }

    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        // Test method: close()
        jsonTreeWriter.close();
        jsonTreeWriter.beginArray();
    }
}