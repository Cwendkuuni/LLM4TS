import static org.junit.Assert.*;
import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        try {
            assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeFieldName("test"));
            assertEquals("test", context.getCurrentName());
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        try {
            assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeFieldName("test"));
            assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = context.createChildArrayContext();
        assertNotNull(childContext);
        assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getType());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = context.createChildObjectContext();
        assertNotNull(childContext);
        assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());
    }

    @Test
    public void testReset() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeFieldName("test");
        context.writeValue();
        context.reset(JsonWriteContext.TYPE_ARRAY);
        assertNull(context.getCurrentName());
        assertEquals(-1, context.getIndex());
    }

    @Test
    public void testAppendDesc() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        StringBuilder sb = new StringBuilder();
        context.appendDesc(sb);
        assertEquals("/{}", sb.toString());
    }

}