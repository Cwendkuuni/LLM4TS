import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class JsonTreeReaderTest {

    private JsonTreeReader jsonTreeReader;

    @Before
    public void setup() {
        JsonElement jsonElement = new JsonObject();
        jsonTreeReader = new JsonTreeReader(jsonElement);
    }

    @Test
    public void testBeginArray() {
        try {
            jsonTreeReader.beginArray();
            assertEquals(JsonToken.BEGIN_ARRAY, jsonTreeReader.peek());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testEndArray() {
        try {
            jsonTreeReader.beginArray();
            jsonTreeReader.endArray();
            assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testBeginObject() {
        try {
            jsonTreeReader.beginObject();
            assertEquals(JsonToken.BEGIN_OBJECT, jsonTreeReader.peek());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testEndObject() {
        try {
            jsonTreeReader.beginObject();
            jsonTreeReader.endObject();
            assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testHasNext() {
        try {
            assertFalse(jsonTreeReader.hasNext());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testPeek() {
        try {
            assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testNextName() {
        try {
            jsonTreeReader.beginObject();
            jsonTreeReader.nextName();
            assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testNextString() {
        try {
            jsonTreeReader.beginArray();
            jsonTreeReader.nextString();
            assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testNextBoolean() {
        try {
            jsonTreeReader.beginObject();
            jsonTreeReader.nextBoolean();
            assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testNextNull() {
        try {
            jsonTreeReader.beginArray();
            jsonTreeReader.nextNull();
            assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testNextDouble() {
        try {
            jsonTreeReader.beginObject();
            jsonTreeReader.nextDouble();
            assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testNextLong() {
        try {
            jsonTreeReader.beginArray();
            jsonTreeReader.nextLong();
            assertEquals(JsonToken.END_ARRAY, jsonTreeReader.peek());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testNextInt() {
        try {
            jsonTreeReader.beginObject();
            jsonTreeReader.nextInt();
            assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testClose() {
        try {
            jsonTreeReader.close();
            assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testSkipValue() {
        try {
            jsonTreeReader.skipValue();
            assertEquals(JsonToken.END_DOCUMENT, jsonTreeReader.peek());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testGetPath() {
        assertEquals("$", jsonTreeReader.getPath());
    }

    @Test
    public void testPromoteNameToValue() {
        try {
            jsonTreeReader.beginObject();
            jsonTreeReader.promoteNameToValue();
            assertEquals(JsonToken.NAME, jsonTreeReader.peek());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
}