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
    public void setup() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    public void testGet() {
        JsonElement jsonElement = jsonTreeWriter.get();
        assertNotNull(jsonElement);
        assertTrue(jsonElement instanceof JsonNull);
    }

    @Test
    public void testBeginArray() {
        try {
            jsonTreeWriter.beginArray();
            JsonElement jsonElement = jsonTreeWriter.get();
            assertNotNull(jsonElement);
            assertTrue(jsonElement instanceof JsonArray);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testBeginObject() {
        try {
            jsonTreeWriter.beginObject();
            JsonElement jsonElement = jsonTreeWriter.get();
            assertNotNull(jsonElement);
            assertTrue(jsonElement instanceof JsonObject);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testValueString() {
        try {
            jsonTreeWriter.value("test");
            JsonElement jsonElement = jsonTreeWriter.get();
            assertNotNull(jsonElement);
            assertTrue(jsonElement instanceof JsonPrimitive);
            assertEquals("test", jsonElement.getAsString());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testValueNull() {
        try {
            jsonTreeWriter.nullValue();
            JsonElement jsonElement = jsonTreeWriter.get();
            assertNotNull(jsonElement);
            assertTrue(jsonElement instanceof JsonNull);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testValueBoolean() {
        try {
            jsonTreeWriter.value(true);
            JsonElement jsonElement = jsonTreeWriter.get();
            assertNotNull(jsonElement);
            assertTrue(jsonElement instanceof JsonPrimitive);
            assertTrue(jsonElement.getAsBoolean());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testValueDouble() {
        try {
            jsonTreeWriter.value(10.5);
            JsonElement jsonElement = jsonTreeWriter.get();
            assertNotNull(jsonElement);
            assertTrue(jsonElement instanceof JsonPrimitive);
            assertEquals(10.5, jsonElement.getAsDouble(), 0.0);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testValueLong() {
        try {
            jsonTreeWriter.value(100L);
            JsonElement jsonElement = jsonTreeWriter.get();
            assertNotNull(jsonElement);
            assertTrue(jsonElement instanceof JsonPrimitive);
            assertEquals(100L, jsonElement.getAsLong());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testValueNumber() {
        try {
            jsonTreeWriter.value(20.5);
            JsonElement jsonElement = jsonTreeWriter.get();
            assertNotNull(jsonElement);
            assertTrue(jsonElement instanceof JsonPrimitive);
            assertEquals(20.5, jsonElement.getAsDouble(), 0.0);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testEndArray() {
        try {
            jsonTreeWriter.beginArray();
            jsonTreeWriter.endArray();
            JsonElement jsonElement = jsonTreeWriter.get();
            assertNotNull(jsonElement);
            assertTrue(jsonElement instanceof JsonArray);
            assertEquals(0, ((JsonArray) jsonElement).size());

        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testEndObject() {
        try {
            jsonTreeWriter.beginObject();
            jsonTreeWriter.endObject();
            JsonElement jsonElement = jsonTreeWriter.get();
            assertNotNull(jsonElement);
            assertTrue(jsonElement instanceof JsonObject);
            assertEquals(0, ((JsonObject) jsonElement).size());

        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateException() {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.endArray();
    }

}