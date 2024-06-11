
import com.google.gson.*;
import com.google.gson.internal.bind.JsonTreeReader;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class JsonTreeReaderTest {

    private JsonTreeReader reader;

    @Before
    public void setUp() {
        JsonElement element = new JsonObject();
        reader = new JsonTreeReader(element);
    }

    @Test
    public void testBeginArray() {
        try {
            reader.beginArray();
            assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testEndArray() {
        try {
            reader.endArray();
            assertEquals(JsonToken.END_ARRAY, reader.peek());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testBeginObject() {
        try {
            reader.beginObject();
            assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testEndObject() {
        try {
            reader.endObject();
            assertEquals(JsonToken.END_OBJECT, reader.peek());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testHasNext() {
        try {
            assertFalse(reader.hasNext());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPeek() {
        try {
            assertEquals(JsonToken.END_DOCUMENT, reader.peek());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testNextName() {
        try {
            assertThrows(IllegalStateException.class, reader::nextName);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testNextString() {
        try {
            assertThrows(IllegalStateException.class, reader::nextString);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testNextBoolean() {
        try {
            assertThrows(IllegalStateException.class, reader::nextBoolean);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testNextNull() {
        try {
            assertThrows(IllegalStateException.class, reader::nextNull);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testNextDouble() {
        try {
            assertThrows(IllegalStateException.class, reader::nextDouble);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testNextLong() {
        try {
            assertThrows(IllegalStateException.class, reader::nextLong);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testNextInt() {
        try {
            assertThrows(IllegalStateException.class, reader::nextInt);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testClose() {
        try {
            reader.close();
            assertEquals(JsonToken.END_DOCUMENT, reader.peek());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testSkipValue() {
        try {
            reader.skipValue();
            assertEquals(JsonToken.END_DOCUMENT, reader.peek());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testToString() {
        assertEquals("JsonTreeReader", reader.toString());
    }

    @Test
    public void testPromoteNameToValue() {
        try {
            assertThrows(IllegalStateException.class, reader::promoteNameToValue);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetPath() {
        assertEquals("$", reader.getPath());
    }

    @Test
    public void testLocationString() {
        assertEquals(" at path $", reader.locationString());
    }
}
