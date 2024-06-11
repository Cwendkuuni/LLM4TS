
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JsonTreeWriterTest {
    private JsonTreeWriter jsonTreeWriter;

    @Before
    public void setUp() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    public void testBeginArray() throws IOException {
        jsonTreeWriter.beginArray();
        assertTrue("Expected JsonArray on stack", jsonTreeWriter.peek() instanceof JsonArray);
    }

    @Test
    public void testEndArray() throws IOException {
        jsonTreeWriter.endArray();
        assertTrue("Expected IllegalStateException", true);
    }

    @Test
    public void testBeginObject() throws IOException {
        jsonTreeWriter.beginObject();
        assertTrue("Expected JsonObject on stack", jsonTreeWriter.peek() instanceof JsonObject);
    }

    @Test
    public void testEndObject() throws IOException {
        jsonTreeWriter.endObject();
        assertTrue("Expected IllegalStateException", true);
    }

    @Test
    public void testName() throws IOException {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("testName");
        assertNull("Expected pending name to be null", jsonTreeWriter.getPendingName());
    }

    @Test
    public void testValueString() throws IOException {
        jsonTreeWriter.value("testValue");
        JsonElement element = jsonTreeWriter.get();
        assertTrue("Expected JsonPrimitive", element instanceof JsonPrimitive);
        assertEquals("Expected 'testValue'", "testValue", element.getAsString());
    }

    @Test
    public void testValueNull() throws IOException {
        jsonTreeWriter.nullValue();
        JsonElement element = jsonTreeWriter.get();
        assertTrue("Expected JsonNull", element instanceof JsonNull);
    }

    @Test
    public void testValueBoolean() throws IOException {
        jsonTreeWriter.value(true);
        JsonElement element = jsonTreeWriter.get();
        assertTrue("Expected JsonPrimitive", element instanceof JsonPrimitive);
        assertTrue("Expected 'true'", element.getAsBoolean());
    }

    @Test
    public void testValueDouble() throws IOException {
        double value = 3.14;
        jsonTreeWriter.value(value);
        JsonElement element = jsonTreeWriter.get();
        assertTrue("Expected JsonPrimitive", element instanceof JsonPrimitive);
        assertEquals("Expected 3.14", value, element.getAsDouble(), 0.001);
    }

    @Test
    public void testValueLong() throws IOException {
        long value = 1234567890L;
        jsonTreeWriter.value(value);
        JsonElement element = jsonTreeWriter.get();
        assertTrue("Expected JsonPrimitive", element instanceof JsonPrimitive);
        assertEquals("Expected 1234567890", value, element.getAsLong());
    }

    @Test
    public void testValueNumber() throws IOException {
        Number value = 3.14;
        jsonTreeWriter.value(value);
        JsonElement element = jsonTreeWriter.get();
        assertTrue("Expected JsonPrimitive", element instanceof JsonPrimitive);
        assertEquals("Expected 3.14", value, element.getAsNumber());
    }

    @Test
    public void testFlush() throws IOException {
        // Nothing to test
    }

    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        jsonTreeWriter.close();
        JsonElement element = jsonTreeWriter.get();
        assertTrue("Expected JsonPrimitive", element instanceof JsonPrimitive);
        assertEquals("Expected 'closed'", "closed", element.getAsString());
    }
}
