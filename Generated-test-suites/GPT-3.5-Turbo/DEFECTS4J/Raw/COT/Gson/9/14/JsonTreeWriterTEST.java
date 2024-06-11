
import static org.junit.Assert.*;
import org.junit.Test;

public class JsonTreeWriterTest {

    @Test
    public void testBeginArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.beginArray();
            // Check the top element of the stack is a JsonArray
            assertTrue(writer.peek() instanceof JsonArray);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testEndArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.beginArray();
            writer.endArray();
            // Check the stack is empty after calling endArray
            assertTrue(writer.stack.isEmpty());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testBeginObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.beginObject();
            // Check the top element of the stack is a JsonObject
            assertTrue(writer.peek() instanceof JsonObject);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testEndObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.beginObject();
            writer.endObject();
            // Check the stack is empty after calling endObject
            assertTrue(writer.stack.isEmpty());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testName() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.beginObject();
            writer.name("key");
            // Check that the pendingName is set correctly
            assertEquals("key", writer.pendingName);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testValueString() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.beginObject();
            writer.name("key");
            writer.value("value");
            JsonObject object = (JsonObject)writer.get();
            // Check that the value is correctly added to the JsonObject
            assertEquals("value", object.get("key").getAsString());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testValueNull() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.beginObject();
            writer.name("key");
            writer.nullValue();
            JsonObject object = (JsonObject)writer.get();
            // Check that a JsonNull object is correctly added to the JsonObject
            assertTrue(object.get("key") instanceof JsonNull);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testValueBoolean() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.beginObject();
            writer.name("key");
            writer.value(true);
            JsonObject object = (JsonObject)writer.get();
            // Check that a JsonPrimitive containing a boolean is correctly added to the JsonObject
            assertEquals(true, object.get("key").getAsBoolean());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testValueDouble() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.beginObject();
            writer.name("key");
            writer.value(3.14);
            JsonObject object = (JsonObject)writer.get();
            // Check that a JsonPrimitive containing a double is correctly added to the JsonObject
            assertEquals(3.14, object.get("key").getAsDouble(), 0.0);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testValueLong() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.beginObject();
            writer.name("key");
            writer.value(123L);
            JsonObject object = (JsonObject)writer.get();
            // Check that a JsonPrimitive containing a long is correctly added to the JsonObject
            assertEquals(123L, object.get("key").getAsLong());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testValueNumber() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.beginObject();
            writer.name("key");
            writer.value(123);
            JsonObject object = (JsonObject)writer.get();
            // Check that a JsonPrimitive containing a number is correctly added to the JsonObject
            assertEquals(123, object.get("key").getAsInt());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFlush() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.flush();
            // No assertions are needed, as flush does not have any side effects in this context
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testClose() {
        JsonTreeWriter writer = new JsonTreeWriter();
        try {
            writer.close();
            // Check that the stack contains SENTINEL_CLOSED after calling close
            assertTrue(writer.stack.contains(JsonTreeWriter.SENTINEL_CLOSED));
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}

