import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {
    
    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        try {
            assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("fieldName"));
            assertEquals("fieldName", context.getCurrentName());
        } catch (JsonProcessingException e) {
            fail("Should not throw an exception");
        }
    }
    
    @Test
    public void testWriteValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = context.createChildArrayContext();
        assertNotNull(childContext);
        assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getType());
        assertEquals(context, childContext.getParent());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = context.createChildObjectContext();
        assertNotNull(childContext);
        assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());
        assertEquals(context, childContext.getParent());
    }

    @Test
    public void testGetCurrentValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNull(context.getCurrentValue());
    }

    @Test
    public void testSetGetCurrentValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.setCurrentValue("test");
        assertEquals("test", context.getCurrentValue());
    }
}