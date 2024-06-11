import static org.junit.Assert.*;
import org.junit.Test;

public class JsonTreeReaderTest {

    @Test
    public void testBeginArray() {
        JsonTreeReader reader = new JsonTreeReader(new JsonArray());
        try {
            reader.beginArray();
            assertEquals(JsonToken.BEGIN_ARRAY, reader.peek());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testEndArray() {
        JsonTreeReader reader = new JsonTreeReader(new JsonArray());
        try {
            reader.endArray();
            assertEquals(JsonToken.END_ARRAY, reader.peek());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testBeginObject() {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        try {
            reader.beginObject();
            assertEquals(JsonToken.BEGIN_OBJECT, reader.peek());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testEndObject() {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        try {
            reader.endObject();
            assertEquals(JsonToken.END_OBJECT, reader.peek());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testHasNext() {
        JsonTreeReader reader = new JsonTreeReader(new JsonArray());
        try {
            assertFalse(reader.hasNext());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testPeek() {
        JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("test"));
        try {
            assertEquals(JsonToken.STRING, reader.peek());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testNextName() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John Doe");
        JsonTreeReader reader = new JsonTreeReader(jsonObject);
        try {
            assertEquals("name", reader.nextName());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testNextString() {
        JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive("test"));
        try {
            assertEquals("test", reader.nextString());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testNextBoolean() {
        JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(true));
        try {
            assertTrue(reader.nextBoolean());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testNextNull() {
        JsonTreeReader reader = new JsonTreeReader(JsonNull.INSTANCE);
        try {
            reader.nextNull();
            assertEquals("null", reader.getPath());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testNextDouble() {
        JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(3.14));
        try {
            assertEquals(3.14, reader.nextDouble(), 0.01);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testNextLong() {
        JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(123456789));
        try {
            assertEquals(123456789, reader.nextLong());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testNextInt() {
        JsonTreeReader reader = new JsonTreeReader(new JsonPrimitive(42));
        try {
            assertEquals(42, reader.nextInt());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testClose() {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        try {
            reader.close();
            assertEquals(JsonToken.END_DOCUMENT, reader.peek());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testSkipValue() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("name", new JsonPrimitive("John Doe"));
        JsonTreeReader reader = new JsonTreeReader(jsonObject);
        try {
            reader.skipValue();
            assertEquals("null", reader.getPath());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPromoteNameToValue() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "John Doe");
        JsonTreeReader reader = new JsonTreeReader(jsonObject);
        try {
            reader.promoteNameToValue();
            assertEquals("name", reader.nextString());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetPath() {
        JsonTreeReader reader = new JsonTreeReader(new JsonObject());
        try {
            assertEquals("$", reader.getPath());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}