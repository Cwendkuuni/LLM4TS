import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeFieldName("field1"));
        assertEquals("field1", context.getCurrentName());
        
        context = context.createChildObjectContext();
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeFieldName("field2"));
        assertEquals("field2", context.getCurrentName());
    }

    @Test
    public void testWriteValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
        
        context = context.createChildObjectContext();
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
        
        context = context.createChildArrayContext();
        
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
    }

    @Test
    public void testAppendDesc() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        StringBuilder sb = new StringBuilder();
        
        context.appendDesc(sb);
        assertEquals("/", sb.toString());
        
        context = context.createChildObjectContext();
        sb = new StringBuilder();
        
        context.appendDesc(sb);
        assertEquals("/{?}", sb.toString());
        
        context = context.createChildObjectContext();
        sb = new StringBuilder();
        
        context.appendDesc(sb);
        assertEquals("/{?/{?}}", sb.toString());
    }
}