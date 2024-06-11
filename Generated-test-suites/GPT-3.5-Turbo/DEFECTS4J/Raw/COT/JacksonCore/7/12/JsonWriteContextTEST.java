import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("name"));
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildArrayContext();
        assertEquals(parentContext, childContext.getParent());
        assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getType());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        assertEquals(parentContext, childContext.getParent());
        assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());
    }

    @Test
    public void testGetCurrentName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeFieldName("name");
        assertEquals("name", context.getCurrentName());
    }

    @Test
    public void testGetDupDetector() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNull(context.getDupDetector());
    }

}