import org.junit.*;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int status = context.writeFieldName("name");
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
        assertEquals("name", context.getCurrentName());
    }

    @Test
    public void testWriteFieldName_duplicates() {
        DupDetector dups = new DupDetector();
        JsonWriteContext context = JsonWriteContext.createRootContext(dups);
        int status = context.writeFieldName("name");
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
        assertEquals("name", context.getCurrentName());

        try {
            status = context.writeFieldName("name");
            fail("Expected JsonGenerationException");
        } catch (JsonGenerationException e) {
            assertEquals("Duplicate field 'name'", e.getMessage());
        }
    }

    @Test
    public void testWriteValue_object() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
    }

    @Test
    public void testWriteValue_array() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.createChildArrayContext();
        int status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
    }

    @Test
    public void testWriteValue_root() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
    }

    @Test
    public void testGetCurrentValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNull(context.getCurrentValue());
    }

    @Test
    public void testSetCurrentValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.setCurrentValue("value");
        assertEquals("value", context.getCurrentValue());
    }

    @Test
    public void testReset() {
        DupDetector dups = new DupDetector();
        JsonWriteContext context = JsonWriteContext.createRootContext(dups);
        context.writeFieldName("name");
        context.writeValue();
        context.reset(JsonWriteContext.TYPE_ARRAY);
        assertNull(context.getCurrentName());
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
    }

    @Test
    public void testWithDupDetector() {
        DupDetector dups = new DupDetector();
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.withDupDetector(dups);
        assertEquals(dups, context.getDupDetector());
    }

    @Test
    public void testCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        assertNull(context.getParent());
    }

    @Test
    public void testCreateRootContext_duplicates() {
        DupDetector dups = new DupDetector();
        JsonWriteContext context = JsonWriteContext.createRootContext(dups);
        assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        assertNull(context.getParent());
        assertEquals(dups, context.getDupDetector());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext context = parent.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
        assertEquals(parent, context.getParent());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext context = parent.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, context.getType());
        assertEquals(parent, context.getParent());
    }

    @Test
    public void testGetParent() {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext context = parent.createChildObjectContext();
        assertEquals(parent, context.getParent());
    }

    @Test
    public void testGetCurrentName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNull(context.getCurrentName());
    }

    @Test
    public void testGetDupDetector() {
        DupDetector dups = new DupDetector();
        JsonWriteContext context = JsonWriteContext.createRootContext(dups);
        assertEquals(dups, context.getDupDetector());
    }

    @Test
    public void testToString() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals("/", context.toString());

        context.createChildObjectContext();
        assertEquals("/{}", context.toString());

        context.createChildArrayContext();
        assertEquals("/{}[]", context.toString());
    }
}