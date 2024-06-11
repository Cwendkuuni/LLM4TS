
import static org.junit.Assert.*;
import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        String fieldName = "fieldName";
        int result = context.writeFieldName(fieldName);
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
        assertEquals(fieldName, context.getCurrentName());
    }

    @Test
    public void testWriteFieldNameDuplicate() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        DupDetector dupDetector = new DupDetector(null);
        dupDetector.checkDup("fieldName");
        context.withDupDetector(dupDetector);
        String fieldName = "fieldName";
        try {
            context.writeFieldName(fieldName);
            fail("Expected JsonGenerationException not thrown");
        } catch (JsonGenerationException ex) {
            assertEquals("Duplicate field 'fieldName'", ex.getMessage());
        }
    }

    @Test
    public void testWriteValueInObjectContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.createChildObjectContext();
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
    }

    @Test
    public void testWriteValueInArrayContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.createChildArrayContext();
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
    }

    @Test
    public void testWriteValueInRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, result);
    }

    @Test
    public void testWriteFirstArrayValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.createChildArrayContext();
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
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

    @Test
    public void testGetParent() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext context = parentContext.createChildObjectContext();
        assertEquals(parentContext, context.getParent());
    }

    @Test
    public void testGetCurrentName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeFieldName("fieldName");
        assertEquals("fieldName", context.getCurrentName());
    }

    @Test
    public void testGetDupDetector() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNull(context.getDupDetector());
    }

    @Test
    public void testCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        assertNull(context.getCurrentName());
        assertEquals(-1, context.getCurrentIndex());
        assertNull(context.getDupDetector());
    }

    @Test
    public void testCreateRootContextWithDupDetector() {
        DupDetector dupDetector = new DupDetector(null);
        JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
        assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        assertNull(context.getCurrentName());
        assertEquals(-1, context.getCurrentIndex());
        assertEquals(dupDetector, context.getDupDetector());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext context = parentContext.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
        assertNull(context.getCurrentName());
        assertEquals(-1, context.getCurrentIndex());
        assertNull(context.getDupDetector());
        assertEquals(parentContext, context.getParent());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext context = parentContext.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, context.getType());
        assertNull(context.getCurrentName());
        assertEquals(-1, context.getCurrentIndex());
        assertNull(context.getDupDetector());
        assertEquals(parentContext, context.getParent());
    }

    @Test
    public void testReset() {
        DupDetector dupDetector = new DupDetector(null);
        JsonWriteContext context = JsonWriteContext.createRootContext(dupDetector);
        context.writeFieldName("fieldName");
        context.setCurrentValue(new Object());
        JsonWriteContext resetContext = context.reset(JsonWriteContext.TYPE_OBJECT);
        assertEquals(JsonWriteContext.TYPE_OBJECT, resetContext.getType());
        assertNull(resetContext.getCurrentName());
        assertEquals(-1, resetContext.getCurrentIndex());
        assertNull(resetContext.getDupDetector());
        assertNull(resetContext.getCurrentValue());
    }

    @Test
    public void testToString() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals("/", context.toString());
        context.writeFieldName("fieldName");
        assertEquals("/fieldName", context.toString());
        context.createChildObjectContext();
        assertEquals("/fieldName/0", context.toString());
    }
}

