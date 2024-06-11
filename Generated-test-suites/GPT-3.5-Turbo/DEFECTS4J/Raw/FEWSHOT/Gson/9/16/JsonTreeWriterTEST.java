import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonTreeWriterTest {
    @Test
    public void testBeginArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        assertEquals("Expected one JSON element in the stack", 1, writer.stack.size());
        assertEquals("Expected a JsonArray at the top of the stack", JsonArray.class, writer.stack.get(0).getClass());
        writer.endArray();
        assertEquals("Expected an empty stack after ending array", 0, writer.stack.size());
    }
    
    @Test
    public void testEndArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.beginObject();
        writer.endArray();
        assertEquals("Expected one JSON element in the stack", 1, writer.stack.size());
        assertEquals("Expected a JsonObject at the top of the stack", JsonObject.class, writer.stack.get(0).getClass());
    }
    
    @Test
    public void testBeginObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        assertEquals("Expected one JSON element in the stack", 1, writer.stack.size());
        assertEquals("Expected a JsonObject at the top of the stack", JsonObject.class, writer.stack.get(0).getClass());
        writer.endObject();
        assertEquals("Expected an empty stack after ending object", 0, writer.stack.size());
    }
    
    @Test
    public void testEndObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.beginArray();
        writer.endObject();
        assertEquals("Expected one JSON element in the stack", 1, writer.stack.size());
        assertEquals("Expected a JsonArray at the top of the stack", JsonArray.class, writer.stack.get(0).getClass());
    }
    
    @Test
    public void testName() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name");
        assertEquals("name", writer.pendingName);
        writer.endObject();
        assertNull(writer.pendingName);
    }
    
    @Test
    public void testValueString() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.value("value");
        assertEquals(JsonPrimitive.class, writer.peek().getClass());
        writer.endObject();
        assertEquals("value", ((JsonObject) writer.product).get("value").getAsString());
    }
    
    @Test
    public void testValueNull() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.nullValue();
        assertEquals(JsonNull.class, writer.peek().getClass());
        writer.endObject();
        assertEquals(JsonNull.INSTANCE, ((JsonObject) writer.product).get("null"));
    }
    
    @Test
    public void testValueBoolean() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.value(true);
        assertEquals(JsonPrimitive.class, writer.peek().getClass());
        writer.endObject();
        assertEquals(true, ((JsonObject) writer.product).get("true").getAsBoolean());
    }
    
    @Test
    public void testValueDouble() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.value(1.0);
        assertEquals(JsonPrimitive.class, writer.peek().getClass());
        writer.endObject();
        assertEquals(1.0, ((JsonObject) writer.product).get("1.0").getAsDouble(), 0.0);
    }
    
    @Test
    public void testValueLong() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.value(10L);
        assertEquals(JsonPrimitive.class, writer.peek().getClass());
        writer.endObject();
        assertEquals(10L, ((JsonObject) writer.product).get("10").getAsLong());
    }
    
    @Test
    public void testValueNumber() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.value(10.5);
        assertEquals(JsonPrimitive.class, writer.peek().getClass());
        writer.endObject();
        assertEquals(10.5, ((JsonObject) writer.product).get("10.5").getAsDouble(), 0.0);
    }
    
    @Test
    public void testFlush() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.flush(); // No assertion, just check if an exception is thrown
    }
    
    @Test(expected = IOException.class)
    public void testCloseIncompleteDocument() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.close();
    }
}