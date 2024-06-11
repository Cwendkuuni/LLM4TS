import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.json.JsonWriteContext;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() throws JsonGenerationException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("fieldName"));
    }

    @Test
    public void testWriteFieldNameWithDuplicate() throws JsonGenerationException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.withDupDetector(new DupDetector());
        context.writeFieldName("fieldName");
        try {
            context.writeFieldName("fieldName");
            fail("Expected JsonGenerationException to be thrown");
        } catch (JsonGenerationException e) {
            assertEquals("Duplicate field 'fieldName'", e.getMessage());
        }
    }

    @Test
    public void testWriteValueInObjectContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.createChildObjectContext();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
    }

    @Test
    public void testWriteValueInObjectContextWithExistingFields() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.createChildObjectContext();
        context.writeFieldName("fieldName1");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
    }

    @Test
    public void testWriteValueInArrayContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.createChildArrayContext();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
    }

    @Test
    public void testWriteValueInRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
    }

    @Test
    public void testWriteValueInRootContextWithExistingValues() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.writeValue());
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
    public void testGetCurrentName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNull(context.getCurrentName());
    }

    @Test
    public void testGetDupDetector() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNull(context.getDupDetector());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = context.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getType());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = context.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());
    }

    @Test
    public void testGetParent() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildArrayContext();
        assertEquals(parentContext, childContext.getParent());
    }

    @Test
    public void testToString() {
        JsonWriteContext context1 = JsonWriteContext.createRootContext();
        assertEquals("/", context1.toString());

        JsonWriteContext context2 = context1.createChildArrayContext();
        assertEquals("[0]", context2.toString());

        JsonWriteContext context3 = context2.createChildObjectContext();
        assertEquals("[0]/{?}", context3.toString());

        context3.writeFieldName("fieldName");
        assertEquals("[0]/fieldName", context3.toString());
    }
}