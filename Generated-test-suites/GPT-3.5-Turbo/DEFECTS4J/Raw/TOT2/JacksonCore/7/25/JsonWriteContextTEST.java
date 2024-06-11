import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

    // Test for writeFieldName method
    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = new JsonWriteContext(JsonStreamContext.TYPE_OBJECT, null, null);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("fieldName"));
        assertEquals("fieldName", context.getCurrentName());
    }

    // Test for writeValue method
    @Test
    public void testWriteValue() {
        JsonWriteContext context = new JsonWriteContext(JsonStreamContext.TYPE_OBJECT, null, null);
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());

        context = new JsonWriteContext(JsonStreamContext.TYPE_ARRAY, null, null);
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());

        context = new JsonWriteContext(JsonStreamContext.TYPE_ROOT, null, null);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
    }

    // Test for createChildArrayContext method
    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonStreamContext.TYPE_OBJECT, null, null);
        JsonWriteContext context = parentContext.createChildArrayContext();
        assertNotNull(context.getParent());
        assertEquals(JsonStreamContext.TYPE_ARRAY, context.getType());
    }

    // Test for createChildObjectContext method
    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = new JsonWriteContext(JsonStreamContext.TYPE_ARRAY, null, null);
        JsonWriteContext context = parentContext.createChildObjectContext();
        assertNotNull(context.getParent());
        assertEquals(JsonStreamContext.TYPE_OBJECT, context.getType());
    }
}