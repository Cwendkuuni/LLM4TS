import static org.junit.Assert.*;
import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        try {
            assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("test"));
            assertEquals("test", context.getCurrentName());
        } catch (JsonProcessingException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext child = parent.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, child.getType());
        assertEquals(parent, child.getParent());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext child = parent.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, child.getType());
        assertEquals(parent, child.getParent());
    }

    @Test
    public void testWithDupDetector() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        DupDetector dupDetector = new DupDetector(null);
        context.withDupDetector(dupDetector);
        assertEquals(dupDetector, context.getDupDetector());
    }

    @Test
    public void testGetCurrentValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNull(context.getCurrentValue());
    }

    @Test
    public void testSetCurrentValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        Object value = new Object();
        context.setCurrentValue(value);
        assertEquals(value, context.getCurrentValue());
    }
}