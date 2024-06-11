import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

    private JsonWriteContext context;

    @Before
    public void setup() {
        context = JsonWriteContext.createRootContext();
    }

    @Test
    public void testWriteFieldName() {
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("fieldName"));
        assertEquals("fieldName", context.getCurrentName());
    }

    @Test
    public void testWriteFieldNameWithDuplicate() {
        context = JsonWriteContext.createRootContext().withDupDetector(new DupDetector());
        context.writeFieldName("fieldName");
        try {
            context.writeFieldName("fieldName");
            fail("Expected JsonGenerationException");
        } catch (JsonProcessingException e) {
            assertEquals("Duplicate field 'fieldName'", e.getMessage());
        }
    }

    @Test
    public void testWriteValueInObjectContext() {
        context = JsonWriteContext.createRootContext().createChildObjectContext();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
    }

    @Test
    public void testWriteValueInArrayContext() {
        context = JsonWriteContext.createRootContext().createChildArrayContext();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
    }

    @Test
    public void testWriteValueInRootContext() {
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.writeValue());
    }

    @Test
    public void testGetParent() {
        assertNull(context.getParent());
        context = JsonWriteContext.createRootContext().createChildObjectContext();
        assertNotNull(context.getParent());
    }

    @Test
    public void testGetCurrentName() {
        assertNull(context.getCurrentName());
        context.writeFieldName("fieldName");
        assertEquals("fieldName", context.getCurrentName());
    }

    @Test
    public void testGetDupDetector() {
        assertNull(context.getDupDetector());
        context = JsonWriteContext.createRootContext().withDupDetector(new DupDetector());
        assertNotNull(context.getDupDetector());
    }

    @Test
    public void testGetCurrentValue() {
        assertNull(context.getCurrentValue());
        Object value = new Object();
        context.setCurrentValue(value);
        assertEquals(value, context.getCurrentValue());
    }
}