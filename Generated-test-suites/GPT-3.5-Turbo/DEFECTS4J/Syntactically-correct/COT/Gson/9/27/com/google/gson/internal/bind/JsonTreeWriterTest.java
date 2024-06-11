
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.*;
import com.google.gson.internal.bind.JsonTreeWriter;
import java.io.IOException;

public class JsonTreeWriterTest {

    @Test
    public void testBeginArray() throws IOException {
        JsonWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.endArray();
        JsonArray jsonArray = (JsonArray) writer.get();
        assertNotNull(jsonArray);
        assertEquals(0, jsonArray.size());
    }

    @Test
    public void testEndArray() throws IOException {
        JsonWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.value("one");
        writer.endArray();
        JsonArray jsonArray = (JsonArray) writer.get();
        assertNotNull(jsonArray);
        assertEquals(1, jsonArray.size());
    }

    @Test
    public void testBeginObject() throws IOException {
        JsonWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.endObject();
        JsonObject jsonObject = (JsonObject) writer.get();
        assertNotNull(jsonObject);
        assertTrue(jsonObject.entrySet().isEmpty());
    }

    @Test
    public void testEndObject() throws IOException {
        JsonWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name").value("value");
        writer.endObject();
        JsonObject jsonObject = (JsonObject) writer.get();
        assertNotNull(jsonObject);
        assertFalse(jsonObject.entrySet().isEmpty());
        assertEquals("value", jsonObject.get("name").getAsString());
    }

    @Test
    public void testName() throws IOException {
        JsonWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name").value("value");
        writer.endObject();
        JsonObject jsonObject = (JsonObject) writer.get();
        assertNotNull(jsonObject);
        assertFalse(jsonObject.entrySet().isEmpty());
        assertEquals("value", jsonObject.get("name").getAsString());
    }

    @Test
    public void testValueString() throws IOException {
        JsonWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name").value("value");
        writer.endObject();
        JsonObject jsonObject = (JsonObject) writer.get();
        assertNotNull(jsonObject);
        assertFalse(jsonObject.entrySet().isEmpty());
        assertEquals("value", jsonObject.get("name").getAsString());
    }

    @Test
    public void testNullValue() throws IOException {
        JsonWriter writer = new JsonTreeWriter();
        writer.value((String) null);
        assertEquals(JsonNull.INSTANCE, writer.get());
    }

    @Test
    public void testValueBoolean() throws IOException {
        JsonWriter writer = new JsonTreeWriter();
        writer.value(true);
        assertEquals(true, ((JsonPrimitive) writer.get()).getAsBoolean());
    }

    @Test
    public void testValueDouble() throws IOException {
        JsonWriter writer = new JsonTreeWriter();
        writer.value(0.2);
        assertEquals(0.2, ((JsonPrimitive) writer.get()).getAsDouble(), 0.0);
    }

    @Test
    public void testValueLong() throws IOException {
        JsonWriter writer = new JsonTreeWriter();
        writer.value(123456789L);
        assertEquals(123456789L, ((JsonPrimitive) writer.get()).getAsLong());
    }

    @Test
    public void testValueNumber() throws IOException {
        JsonWriter writer = new JsonTreeWriter();
        writer.value(123);
        assertEquals(123, ((JsonPrimitive) writer.get()).getAsInt());
    }

    @Test(expected = IllegalStateException.class)
    public void testValueNonFiniteNumber() throws IOException {
        JsonWriter writer = new JsonTreeWriter();
        writer.value(Double.NaN);
    }

    @Test(expected = IllegalStateException.class)
    public void testValueNonFiniteNumberStrict() throws IOException {
        JsonWriter writer = new JsonTreeWriter();
        writer.setLenient(false);
        writer.value(Double.NaN);
    }

    @Test(expected = IllegalStateException.class)
    public void testValueNonFiniteNumberInfinity() throws IOException {
        JsonWriter writer = new JsonTreeWriter();
        writer.value(Double.POSITIVE_INFINITY);
    }

    @Test(expected = IllegalStateException.class)
    public void testValueNonFiniteNumberInfinityStrict() throws IOException {
        JsonWriter writer = new JsonTreeWriter();
        writer.setLenient(false);
        writer.value(Double.POSITIVE_INFINITY);
    }
  
    @Test(expected = IOException.class)
    public void testCloseIncompleteDocument() throws IOException {
        JsonWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name").value("value");
        writer.close();
    }

    @Test(expected = IOException.class)
    public void testFlush() throws IOException {
        JsonWriter writer = new JsonTreeWriter();
        writer.flush();
    }
}
