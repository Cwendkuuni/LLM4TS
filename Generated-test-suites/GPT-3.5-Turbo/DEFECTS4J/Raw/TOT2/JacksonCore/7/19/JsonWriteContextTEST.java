import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {
   
    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("fieldName"));
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("fieldName2"));
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        
        context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
    }

    @Test
    public void testCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNotNull(context);
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildArrayContext();
        assertNotNull(childContext);
        assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getContextType());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        assertNotNull(childContext);
        assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getContextType());
    }

    @Test
    public void testWithDupDetector() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        assertNotNull(context.withDupDetector(null));
    }
}