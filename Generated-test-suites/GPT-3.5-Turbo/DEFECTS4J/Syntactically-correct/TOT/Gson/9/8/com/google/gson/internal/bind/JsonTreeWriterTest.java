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
        
        assertEquals(1, jsonTreeWriter.peek().getAsJsonArray().size());
    }

    @Test
    public void testEndArray() {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.beginObject();
        jsonTreeWriter.endObject();
        jsonTreeWriter.endArray();
        
        assertTrue(jsonTreeWriter.peek().isJsonNull());
    }

    @Test
    public void testBeginObject() {
        jsonTreeWriter.beginObject();

        assertEquals(1, jsonTreeWriter.peek().getAsJsonObject().entrySet().size());
    }

    @Test
    public void testEndObject() {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("key").value("value");
        jsonTreeWriter.endObject();

        assertTrue(jsonTreeWriter.peek().isJsonNull());
    }

    @Test
    public void testName() {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("key").value("value");

        assertNotNull(jsonTreeWriter.peek().getAsJsonObject().get("key"));
    }

    @Test
    public void testValueString() {
        jsonTreeWriter.value("value");

        assertEquals("value", jsonTreeWriter.peek().getAsString());
    }

    @Test
    public void testNullValue() {
        jsonTreeWriter.nullValue();

        assertTrue(jsonTreeWriter.peek().isJsonNull());
    }

    @Test
    public void testValueBoolean() {
        jsonTreeWriter.value(true);

        assertTrue(jsonTreeWriter.peek().getAsBoolean());
    }

    @Test
    public void testValueDouble() {
        jsonTreeWriter.value(1.5);

        assertEquals(1.5, jsonTreeWriter.peek().getAsDouble(), 0.0001);
    }

    @Test
    public void testValueLong() {
        jsonTreeWriter.value(100L);

        assertEquals(100L, jsonTreeWriter.peek().getAsLong());
    }

    @Test
    public void testValueNumber() {
        jsonTreeWriter.value(10.5f);

        assertEquals(10.5f, jsonTreeWriter.peek().getAsFloat(), 0.0001);
    }

    @Test
    public void testGet() {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("key").value("value");
        jsonTreeWriter.endObject();

        JsonElement jsonElement = jsonTreeWriter.get();

        assertTrue(jsonElement.isJsonObject());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetWithIncompleteDocument() {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("key").value("value");

        jsonTreeWriter.get();
    }
}