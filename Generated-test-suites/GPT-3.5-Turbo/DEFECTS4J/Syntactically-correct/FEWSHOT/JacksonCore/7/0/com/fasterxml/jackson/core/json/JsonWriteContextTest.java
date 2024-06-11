import static org.junit.Assert.*;
import org.junit.Test;

public class JsonWriteContextTest {
    
    @Test
    public void testReset() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        context.reset(JsonWriteContext.TYPE_OBJECT);
        assertEquals(JsonWriteContext.TYPE_OBJECT, context.getType());
        assertNull(context.getCurrentName());
        assertFalse(context.isGotName());
        assertNull(context.getParent());
        assertNull(context.getDupDetector());
        assertNull(context.getChild());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext childContext = context.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getType());
        assertNull(childContext.getCurrentName());
        assertFalse(childContext.isGotName());
        assertEquals(context, childContext.getParent());
        assertNull(childContext.getDupDetector());
        assertNull(childContext.getChild());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        JsonWriteContext childContext = context.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());
        assertNull(childContext.getCurrentName());
        assertFalse(childContext.isGotName());
        assertEquals(context, childContext.getParent());
        assertNull(childContext.getDupDetector());
        assertNull(childContext.getChild());
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("name"));
        assertEquals("name", context.getCurrentName());
        assertTrue(context.isGotName());
        assertNull(context.getDupDetector());
        context.setGotName(false);
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeFieldName("age"));
        assertEquals("age", context.getCurrentName());
        assertTrue(context.isGotName());
        assertNull(context.getDupDetector());
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        assertEquals(0, context.getCurrentIndex());
        assertFalse(context.isGotName());

        context.reset(JsonWriteContext.TYPE_ARRAY);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        assertEquals(0, context.getCurrentIndex());
        assertFalse(context.isGotName());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
        assertEquals(1, context.getCurrentIndex());

        context.reset(JsonWriteContext.TYPE_ROOT);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        assertEquals(0, context.getCurrentIndex());
        assertFalse(context.isGotName());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.writeValue());
    }

    @Test
    public void testToString() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        assertEquals("{\"?\"}", context.toString());

        context.setCurrentName("name");
        assertEquals("{\"name\"}", context.toString());

        context.reset(JsonWriteContext.TYPE_ARRAY);
        assertEquals("[0]", context.toString());
    }
}