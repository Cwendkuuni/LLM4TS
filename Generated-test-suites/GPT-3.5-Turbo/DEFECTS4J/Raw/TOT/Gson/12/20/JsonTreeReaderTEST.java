import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.*;

public class JsonTreeReaderTest {

    private JsonTreeReader jsonTreeReader;

    @Before
    public void setup() {
        JsonElement element = new JsonObject();
        jsonTreeReader = new JsonTreeReader(element);
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
            jsonTreeReader.endObject();
            assertEquals(JsonToken.END_OBJECT, jsonTreeReader.peek());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testHasNext() {
        try {
            jsonTreeReader.beginArray();
            assertTrue(jsonTreeReader.hasNext());
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
            jsonTreeReader.promoteNameToValue();
            assertEquals(JsonToken.NAME, jsonTreeReader.peek());
            assertEquals("name", jsonTreeReader.nextName());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testNextString() {
        try {
            jsonTreeReader.promoteNameToValue();
            jsonTreeReader.push(new JsonPrimitive("value"));
            assertEquals(JsonToken.STRING, jsonTreeReader.peek());
            assertEquals("value", jsonTreeReader.nextString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testNextBoolean() {
        try {
            jsonTreeReader.promoteNameToValue();
            jsonTreeReader.push(new JsonPrimitive(true));
            assertEquals(JsonToken.BOOLEAN, jsonTreeReader.peek());
            assertTrue(jsonTreeReader.nextBoolean());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testNextNull() {
        try {
            jsonTreeReader.nextNull();
            assertEquals(JsonToken.NULL, jsonTreeReader.peek());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testNextDouble() {
        try {
            jsonTreeReader.promoteNameToValue();
            jsonTreeReader.push(new JsonPrimitive(3.14));
            assertEquals(JsonToken.NUMBER, jsonTreeReader.peek());
            assertEquals(3.14, jsonTreeReader.nextDouble(), 0.0001);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testNextLong() {
        try {
            jsonTreeReader.promoteNameToValue();
            jsonTreeReader.push(new JsonPrimitive(100L));
            assertEquals(JsonToken.NUMBER, jsonTreeReader.peek());
            assertEquals(100L, jsonTreeReader.nextLong());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testNextInt() {
        try {
            jsonTreeReader.promoteNameToValue();
            jsonTreeReader.push(new JsonPrimitive(42));
            assertEquals(JsonToken.NUMBER, jsonTreeReader.peek());
            assertEquals(42, jsonTreeReader.nextInt());
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
            jsonTreeReader.promoteNameToValue();
            jsonTreeReader.push(new JsonPrimitive("value"));
            jsonTreeReader.skipValue();
            assertEquals(JsonToken.NULL, jsonTreeReader.peek());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testGetPath() {
        try {
            jsonTreeReader.beginObject();
            jsonTreeReader.promoteNameToValue();
            jsonTreeReader.push(new JsonPrimitive("value"));
            assertEquals("$.name", jsonTreeReader.getPath());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testToString() {
        assertEquals("JsonTreeReader", jsonTreeReader.toString());
    }

    @Test
    public void testPromoteNameToValue() {
        try {
            jsonTreeReader.beginObject();
            jsonTreeReader.nextName();
            jsonTreeReader.promoteNameToValue();
            assertEquals(JsonToken.STRING, jsonTreeReader.peek());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

}