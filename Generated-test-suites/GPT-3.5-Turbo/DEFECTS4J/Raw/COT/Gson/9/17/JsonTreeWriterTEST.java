import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class JsonTreeWriterTest {

    @Test
    public void testBeginArray() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        JsonElement expected = new JsonArray();
        assertEquals(expected, writer.get());
    }

    @Test
    public void testEndArray() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.endArray();
        JsonElement expected = new JsonArray();
        assertEquals(expected, writer.get());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndArrayWithoutBeginArray() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.endArray();
    }

    @Test
    public void testBeginObject() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        JsonElement expected = new JsonObject();
        assertEquals(expected, writer.get());
    }

    @Test
    public void testEndObject() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.endObject();
        JsonElement expected = new JsonObject();
        assertEquals(expected, writer.get());
    }

    @Test(expected = IllegalStateException.class)
    public void testEndObjectWithoutBeginObject() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.endObject();
    }

    @Test
    public void testName() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("key");
        JsonElement expected = new JsonObject();
        expected.getAsJsonObject().add("key", JsonNull.INSTANCE);
        assertEquals(expected, writer.get());
    }

    @Test(expected = IllegalStateException.class)
    public void testNameWithoutBeginObject() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.name("key");
    }

    @Test
    public void testValueString() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value("value");
        JsonElement expected = new JsonPrimitive("value");
        assertEquals(expected, writer.get());
    }

    @Test
    public void testValueNull() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.nullValue();
        JsonElement expected = JsonNull.INSTANCE;
        assertEquals(expected, writer.get());
    }

    @Test
    public void testValueBoolean() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(true);
        JsonElement expected = new JsonPrimitive(true);
        assertEquals(expected, writer.get());
    }

    @Test
    public void testValueDouble() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(3.14159);
        JsonElement expected = new JsonPrimitive(3.14159);
        assertEquals(expected, writer.get());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueDoubleException() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.setLenient(false);
        writer.value(Double.NaN);
    }

    @Test
    public void testValueLong() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(1234567890L);
        JsonElement expected = new JsonPrimitive(1234567890L);
        assertEquals(expected, writer.get());
    }

    @Test
    public void testValueNumber() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(123.456);
        JsonElement expected = new JsonPrimitive(123.456);
        assertEquals(expected, writer.get());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueNumberException() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.setLenient(false);
        writer.value(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testFlush() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.flush();
    }

    @Test(expected = IOException.class)
    public void testCloseWithIncompleteDocument() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.close();
    }

    @Test
    public void testClose() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.close();
        JsonElement expected = JsonNull.INSTANCE;
        assertEquals(expected, writer.get());
    }

}