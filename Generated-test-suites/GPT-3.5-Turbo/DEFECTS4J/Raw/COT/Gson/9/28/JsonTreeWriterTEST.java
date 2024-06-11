import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
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
    public void testGet() {
        JsonElement result = writer.get();
        assertEquals(JsonNull.INSTANCE, result);
    }

    @Test
    public void testBeginArray() {
        JsonWriter result = writer.beginArray();
        assertSame(writer, result);
    }

    @Test
    public void testEndArray() {
        try {
            writer.endArray();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Exception expected
        }
    }

    @Test
    public void testBeginObject() {
        JsonWriter result = writer.beginObject();
        assertSame(writer, result);
    }

    @Test
    public void testEndObject() {
        try {
            writer.endObject();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Exception expected
        }
    }

    @Test
    public void testName() {
        try {
            writer.name("test");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Exception expected
        }
    }

    @Test
    public void testValueString() {
        JsonWriter result = writer.value("test");
        assertSame(writer, result);
    }

    @Test
    public void testNullValue() {
        JsonWriter result = writer.nullValue();
        assertSame(writer, result);
    }

    @Test
    public void testValueBoolean() {
        JsonWriter result = writer.value(true);
        assertSame(writer, result);
    }

    @Test
    public void testValueDouble() {
        JsonWriter result = writer.value(1.0);
        assertSame(writer, result);
    }

    @Test
    public void testValueLong() {
        JsonWriter result = writer.value(1L);
        assertSame(writer, result);
    }

    @Test
    public void testValueNumber() {
        JsonWriter result = writer.value(1);
        assertSame(writer, result);
    }

    @Test
    public void testFlush() {
        assertDoesNotThrow(() -> writer.flush());
    }

    @Test
    public void testClose() {
        assertThrows(IOException.class, () -> writer.close());
    }
}