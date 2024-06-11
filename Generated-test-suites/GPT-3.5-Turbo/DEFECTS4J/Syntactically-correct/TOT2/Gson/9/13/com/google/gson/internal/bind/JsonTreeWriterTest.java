import static org.junit.Assert.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Before;
import org.junit.Test;

public class JsonTreeWriterTest {

    private JsonTreeWriter jsonTreeWriter;

    @Before
    public void setUp() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    public void testGet() {
        JsonElement expected = JsonNull.INSTANCE;
        JsonElement actual = jsonTreeWriter.get();
        assertEquals(expected, actual);
    }

    @Test
    public void testBeginArray() {
        try {
            jsonTreeWriter.beginArray();
        } catch (Exception e) {
            fail("Exception thrown when beginning array: " + e.getMessage());
        }
    }

    @Test
    public void testEndArray() {
        try {
            jsonTreeWriter.beginArray();
            jsonTreeWriter.endArray();
        } catch (Exception e) {
            fail("Exception thrown when ending array: " + e.getMessage());
        }
    }

    @Test
    public void testBeginObject() {
        try {
            jsonTreeWriter.beginObject();
        } catch (Exception e) {
            fail("Exception thrown when beginning object: " + e.getMessage());
        }
    }

    @Test
    public void testEndObject() {
        try {
            jsonTreeWriter.beginObject();
            jsonTreeWriter.endObject();
        } catch (Exception e) {
            fail("Exception thrown when ending object: " + e.getMessage());
        }
    }

    @Test
    public void testName() {
        try {
            jsonTreeWriter.beginObject();
            jsonTreeWriter.name("name");
        } catch (Exception e) {
            fail("Exception thrown when setting name: " + e.getMessage());
        }
    }

    @Test
    public void testValueString() {
        try {
            jsonTreeWriter.value("test");
        } catch (Exception e) {
            fail("Exception thrown when setting string value: " + e.getMessage());
        }
    }

    @Test
    public void testNullValue() {
        try {
            jsonTreeWriter.nullValue();
        } catch (Exception e) {
            fail("Exception thrown when setting null value: " + e.getMessage());
        }
    }

    @Test
    public void testValueBoolean() {
        try {
            jsonTreeWriter.value(true);
        } catch (Exception e) {
            fail("Exception thrown when setting boolean value: " + e.getMessage());
        }
    }

    @Test
    public void testValueDouble() {
        try {
            jsonTreeWriter.value(3.14);
        } catch (Exception e) {
            fail("Exception thrown when setting double value: " + e.getMessage());
        }
    }

    @Test
    public void testValueLong() {
        try {
            jsonTreeWriter.value(100L);
        } catch (Exception e) {
            fail("Exception thrown when setting long value: " + e.getMessage());
        }
    }

    @Test
    public void testValueNumber() {
        try {
            jsonTreeWriter.value(42);
        } catch (Exception e) {
            fail("Exception thrown when setting number value: " + e.getMessage());
        }
    }

    @Test
    public void testFlush() {
        try {
            jsonTreeWriter.flush();
        } catch (Exception e) {
            fail("Exception thrown when flushing: " + e.getMessage());
        }
    }

    @Test
    public void testClose() {
        try {
            jsonTreeWriter.close();
        } catch (Exception e) {
            fail("Exception thrown when closing: " + e.getMessage());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testIncompleteDocument() {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.close();
        jsonTreeWriter.get();
    }
}