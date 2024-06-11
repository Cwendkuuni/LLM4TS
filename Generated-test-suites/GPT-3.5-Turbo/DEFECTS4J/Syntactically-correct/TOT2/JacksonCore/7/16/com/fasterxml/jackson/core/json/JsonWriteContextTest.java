import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("fieldName"));
        
        context.reset(JsonWriteContext.TYPE_OBJECT);
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, context.writeFieldName("fieldName"));
    }
    
    @Test
    public void testWriteValue() {
        JsonWriteContext contextObject = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext contextArray = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        JsonWriteContext contextRoot = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, contextObject.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, contextObject.writeValue());
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, contextArray.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, contextArray.writeValue());
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, contextRoot.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, contextRoot.writeValue());
    }
    
    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);

        JsonWriteContext childContext = context.createChildObjectContext();
        assertNotNull(childContext);
        assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);

        JsonWriteContext childContext = context.createChildArrayContext();
        assertNotNull(childContext);
        assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getType());
    }

    @Test
    public void testReset() {
        JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        context.setCurrentValue("value");

        context.reset(JsonWriteContext.TYPE_ARRAY);
        assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
        assertNull(context.getCurrentName());
        assertNull(context.getCurrentValue());
        assertFalse(context.hasName());
    }
}