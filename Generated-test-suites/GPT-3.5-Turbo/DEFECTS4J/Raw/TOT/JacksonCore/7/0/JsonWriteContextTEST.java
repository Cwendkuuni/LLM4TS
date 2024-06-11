import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        // Typical use case
        int result = context.writeFieldName("fieldName");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
        
        // Edge case: field name is null
        result = context.writeFieldName(null);
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, result);
        
        // Error scenario: duplicate field name
        DupDetector dups = new DupDetector(null);
        context.withDupDetector(dups);
        dups.addValue("fieldName");
        try {
            context.writeFieldName("fieldName");
            fail("Expected JsonGenerationException");
        } catch (JsonGenerationException e) {
            assertEquals("Duplicate field 'fieldName'", e.getMessage());
        }
    }
    
    @Test
    public void testWriteValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        // Typical use case for object context
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
        
        // Typical use case for array context
        context = context.createChildArrayContext();
        result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        
        // Typical use case for root context
        context = JsonWriteContext.createRootContext();
        result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, result);
        
        // Edge case: only one value in root context
        result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
    }
    
    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        
        // Typical use case
        JsonWriteContext childContext = parentContext.createChildArrayContext();
        assertNotNull(childContext);
        assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getType());
        
        // Edge case: child context already exists
        JsonWriteContext existingChildContext = parentContext.createChildArrayContext();
        assertEquals(childContext, existingChildContext);
        assertEquals(JsonWriteContext.TYPE_ARRAY, existingChildContext.getType());
    }
    
    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        
        // Typical use case
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        assertNotNull(childContext);
        assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());
        
        // Edge case: child context already exists
        JsonWriteContext existingChildContext = parentContext.createChildObjectContext();
        assertEquals(childContext, existingChildContext);
        assertEquals(JsonWriteContext.TYPE_OBJECT, existingChildContext.getType());
    }
    
    @Test
    public void testGetParent() {
        JsonWriteContext parentContext = JsonWriteContext.createRootContext();
        JsonWriteContext childContext = parentContext.createChildObjectContext();
        
        // Typical use case
        JsonWriteContext result = childContext.getParent();
        assertEquals(parentContext, result);
    }
    
    @Test
    public void testGetCurrentName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeFieldName("fieldName");
        
        // Typical use case
        String result = context.getCurrentName();
        assertEquals("fieldName", result);
        
        // Edge case: current name is null
        JsonWriteContext newContext = context.createChildObjectContext();
        result = newContext.getCurrentName();
        assertNull(result);
    }
    
    @Test
    public void testGetDupDetector() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        DupDetector dups = new DupDetector(null);
        context.withDupDetector(dups);
        
        // Typical use case
        DupDetector result = context.getDupDetector();
        assertEquals(dups, result);
    }
}