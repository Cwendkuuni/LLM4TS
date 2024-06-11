import static org.junit.Assert.*;
import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNotNull(context);
        assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        assertNull(context.getParent());
        assertNull(context.getDupDetector());
    }

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("fieldName"));
    }

    @Test
    public void testWriteValueForObject() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
    }

    @Test
    public void testWriteValueForArray() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.reset(JsonWriteContext.TYPE_ARRAY);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
    }

    @Test
    public void testToString() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals("/", context.toString());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getType());
        assertEquals(parentContext, childContext.getParent());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());
        assertEquals(parentContext, childContext.getParent());
    }
}