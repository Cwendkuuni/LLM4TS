import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeWriterTest {

    @Test
    public void testBeginArray() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();

        writer.beginArray();

        JsonElement result = writer.get();
        assertTrue(result instanceof JsonArray);
        assertTrue(((JsonArray) result).size() == 0);
    }

    @Test
    public void testEndArray() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();

        writer.beginArray();
        writer.value("value1");
        writer.value("value2");
        writer.endArray();

        JsonElement result = writer.get();
        assertTrue(result instanceof JsonArray);
        JsonArray jsonArray = (JsonArray) result;
        assertEquals("value1", jsonArray.get(0).getAsString());
        assertEquals("value2", jsonArray.get(1).getAsString());
    }

    @Test
    public void testBeginObject() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();

        writer.beginObject();

        JsonElement result = writer.get();
        assertTrue(result instanceof JsonObject);
        assertTrue(((JsonObject) result).size() == 0);
    }

    @Test
    public void testEndObject() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();

        writer.beginObject();
        writer.name("name1");
        writer.value("value1");
        writer.name("name2");
        writer.value("value2");
        writer.endObject();

        JsonElement result = writer.get();
        assertTrue(result instanceof JsonObject);
        JsonObject jsonObject = (JsonObject) result;
        assertEquals("value1", jsonObject.get("name1").getAsString());
        assertEquals("value2", jsonObject.get("name2").getAsString());
    }

    @Test
    public void testName() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();

        writer.beginObject();
        writer.name("name1");

        JsonElement result = writer.get();
        assertTrue(result instanceof JsonObject);
        JsonObject jsonObject = (JsonObject) result;
        assertTrue(jsonObject.contains("name1"));
    }

    @Test
    public void testValueString() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();

        writer.value("testString");
        
        JsonElement result = writer.get();
        assertTrue(result instanceof JsonPrimitive);
        assertEquals("testString", result.getAsString());
    }

    @Test
    public void testValueBoolean() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();

        writer.value(true);
        
        JsonElement result = writer.get();
        assertTrue(result instanceof JsonPrimitive);
        assertEquals(true, result.getAsBoolean());
    }

    @Test
    public void testValueDouble() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();

        writer.value(3.14);
        
        JsonElement result = writer.get();
        assertTrue(result instanceof JsonPrimitive);
        assertEquals(3.14, result.getAsDouble(), 0.01);
    }

    @Test
    public void testValueLong() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();

        writer.value(100L);
        
        JsonElement result = writer.get();
        assertTrue(result instanceof JsonPrimitive);
        assertEquals(100L, result.getAsLong());
    }

    @Test
    public void testValueNull() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();

        writer.nullValue();
        
        JsonElement result = writer.get();
        assertTrue(result instanceof JsonNull);
    }

    @Test
    public void testValueNumber() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();

        writer.value(123);
        
        JsonElement result = writer.get();
        assertTrue(result instanceof JsonPrimitive);
        assertEquals(123, result.getAsInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueNonFiniteDouble() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();

        writer.beginArray();
        writer.value(Double.POSITIVE_INFINITY);
        writer.endArray();
    }

    @Test
    public void testFlush() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();

        writer.beginObject();
        writer.name("name1");
        writer.value("value1");
        writer.flush();

        JsonElement result = writer.get();
        assertTrue(result instanceof JsonObject);
        JsonObject jsonObject = (JsonObject) result;
        assertEquals("value1", jsonObject.get("name1").getAsString());
    }

    @Test(expected = IOException.class)
    public void testCloseIncompleteDocument() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();

        writer.beginArray();
        writer.value("value1");
        writer.close();
    }

    @Test
    public void testClose() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();

        writer.close();

        JsonElement result = writer.get();
        assertTrue(result instanceof JsonPrimitive);
        assertEquals("closed", result.getAsString());
    }

}