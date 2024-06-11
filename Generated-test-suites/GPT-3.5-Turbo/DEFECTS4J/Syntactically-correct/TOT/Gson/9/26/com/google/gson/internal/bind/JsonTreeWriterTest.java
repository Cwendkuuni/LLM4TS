import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeWriterTest {

    private JsonTreeWriter jsonTreeWriter;

    @Before
    public void setUp() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    public void testBeginArray() {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.value(1);
        jsonTreeWriter.value(true);
        jsonTreeWriter.value("test");
        jsonTreeWriter.endArray();

        JsonElement result = jsonTreeWriter.get();
        assertTrue(result instanceof JsonArray);
        JsonArray jsonArray = (JsonArray) result;
        assertEquals(1, jsonArray.get(0).getAsInt());
        assertEquals(true, jsonArray.get(1).getAsBoolean());
        assertEquals("test", jsonArray.get(2).getAsString());
    }

    @Test
    public void testEndArray() {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.value(1);
        jsonTreeWriter.value(true);
        jsonTreeWriter.value("test");
        jsonTreeWriter.endArray();
        jsonTreeWriter.endArray();

        JsonElement result = jsonTreeWriter.get();
        assertTrue(result instanceof JsonArray);
        JsonArray jsonArray = (JsonArray) result;
        assertEquals(1, jsonArray.get(0).getAsInt());
        assertEquals(true, jsonArray.get(1).getAsBoolean());
        assertEquals("test", jsonArray.get(2).getAsString());

        assertFalse(jsonTreeWriter.beginArray());
    }

    @Test
    public void testBeginObject() {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("name");
        jsonTreeWriter.value("John");
        jsonTreeWriter.name("age");
        jsonTreeWriter.value(25);
        jsonTreeWriter.endObject();

        JsonElement result = jsonTreeWriter.get();
        assertTrue(result instanceof JsonObject);
        JsonObject jsonObject = (JsonObject) result;
        assertEquals("John", jsonObject.get("name").getAsString());
        assertEquals(25, jsonObject.get("age").getAsInt());
    }

    @Test
    public void testEndObject() {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("name");
        jsonTreeWriter.value("John");
        jsonTreeWriter.name("age");
        jsonTreeWriter.value(25);
        jsonTreeWriter.endObject();
        jsonTreeWriter.endObject();

        JsonElement result = jsonTreeWriter.get();
        assertTrue(result instanceof JsonObject);
        JsonObject jsonObject = (JsonObject) result;
        assertEquals("John", jsonObject.get("name").getAsString());
        assertEquals(25, jsonObject.get("age").getAsInt());

        assertFalse(jsonTreeWriter.beginObject());
    }

    @Test
    public void testName() {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("name");
        jsonTreeWriter.value("John");

        JsonElement result = jsonTreeWriter.get();
        assertTrue(result instanceof JsonObject);
        JsonObject jsonObject = (JsonObject) result;
        assertEquals("John", jsonObject.get("name").getAsString());
        assertNull(jsonTreeWriter.name("age"));
    }

    @Test
    public void testValueString() {
        jsonTreeWriter.value("test");

        JsonElement result = jsonTreeWriter.get();
        assertEquals("test", result.getAsString());
    }

    @Test
    public void testValueNull() {
        jsonTreeWriter.nullValue();

        JsonElement result = jsonTreeWriter.get();
        assertEquals(JsonNull.INSTANCE, result);
    }

    @Test
    public void testValueBoolean() {
        jsonTreeWriter.value(true);

        JsonElement result = jsonTreeWriter.get();
        assertEquals(true, result.getAsBoolean());
    }

    @Test
    public void testValueDouble() {
        jsonTreeWriter.value(2.5);

        JsonElement result = jsonTreeWriter.get();
        assertEquals(2.5, result.getAsDouble(), 0.001);
    }

    @Test
    public void testValueLong() {
        jsonTreeWriter.value(10L);

        JsonElement result = jsonTreeWriter.get();
        assertEquals(10L, result.getAsLong());
    }

    @Test
    public void testValueNumber() {
        jsonTreeWriter.value(1.5);

        JsonElement result = jsonTreeWriter.get();
        assertEquals(1.5, result.getAsDouble(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueDoubleWithNaN() {
        jsonTreeWriter.value(Double.NaN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueDoubleWithInfinity() {
        jsonTreeWriter.value(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testFlush() {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("name");
        jsonTreeWriter.value("John");
        jsonTreeWriter.flush();
        jsonTreeWriter.name("age");
        jsonTreeWriter.value(25);
        jsonTreeWriter.endObject();

        JsonElement result = jsonTreeWriter.get();
        assertTrue(result instanceof JsonObject);
        JsonObject jsonObject = (JsonObject) result;
        assertEquals("John", jsonObject.get("name").getAsString());
        assertEquals(25, jsonObject.get("age").getAsInt());
    }

    @Test(expected = IOException.class)
    public void testCloseWithIncompleteDocument() throws IOException {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("name");
        jsonTreeWriter.value("John");
        jsonTreeWriter.close();
    }

    @Test(expected = AssertionError.class)
    public void testCloseWithAssertionError() {
        jsonTreeWriter.close();
        jsonTreeWriter.beginObject();
    }

    @Test
    public void testGet() {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("name");
        jsonTreeWriter.value("John");
        jsonTreeWriter.endObject();

        JsonElement result = jsonTreeWriter.get();
        assertTrue(result instanceof JsonObject);
        JsonObject jsonObject = (JsonObject) result;
        assertEquals("John", jsonObject.get("name").getAsString());
    }

}