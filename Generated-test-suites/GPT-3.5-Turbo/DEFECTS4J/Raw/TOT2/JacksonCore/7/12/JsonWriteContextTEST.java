import static org.junit.Assert.*;
import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("test"));
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
    }

    @Test
    public void testCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNotNull(context);
        assertNull(context.getParent());
        assertNull(context.getDupDetector());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildArrayContext();
        assertNotNull(childContext);
        assertEquals(parentContext, childContext.getParent());
        assertNull(childContext.getDupDetector());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        assertNotNull(childContext);
        assertEquals(parentContext, childContext.getParent());
        assertNull(childContext.getDupDetector());
    }

    @Test
    public void testReset() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.writeFieldName("test");
        context.writeValue();
        context.reset(JsonWriteContext.TYPE_ARRAY);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
    }

    @Test
    public void testAppendDesc() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        StringBuilder sb = new StringBuilder();
        context.appendDesc(sb);
        assertEquals("{}", sb.toString());
    }

}