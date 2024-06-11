
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        String fieldName = "name";
        int result = context.writeFieldName(fieldName);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        assertEquals(fieldName, context.getCurrentName());
    }

    @Test
    public void testWriteValue_ObjectContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
    }

    @Test
    public void testWriteValue_ArrayContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.createChildArrayContext();
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
    }

    @Test
    public void testWriteValue_RootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
    }

    @Test
    public void testGetCurrentValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        Object value = new Object();
        context.setCurrentValue(value);
        assertEquals(value, context.getCurrentValue());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext child = parent.createChildArrayContext();
        assertNotNull(child);
        assertEquals(JsonWriteContext.TYPE_ARRAY, child.getType());
        assertEquals(parent, child.getParent());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext child = parent.createChildObjectContext();
        assertNotNull(child);
        assertEquals(JsonWriteContext.TYPE_OBJECT, child.getType());
        assertEquals(parent, child.getParent());
    }

    @Test
    public void testGetParent() {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext child = parent.createChildArrayContext();
        assertEquals(parent, child.getParent());
    }

    @Test
    public void testGetCurrentName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        String fieldName = "name";
        context.writeFieldName(fieldName);
        assertEquals(fieldName, context.getCurrentName());
    }

    @Test
    public void testGetDupDetector() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        DupDetector dups = new DupDetector(null);
        context.withDupDetector(dups);
        assertEquals(dups, context.getDupDetector());
    }
}

