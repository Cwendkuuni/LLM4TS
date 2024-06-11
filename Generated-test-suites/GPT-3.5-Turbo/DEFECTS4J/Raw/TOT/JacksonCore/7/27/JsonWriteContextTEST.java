import static org.junit.Assert.*;

import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        // Typical use case - write a field name
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("fieldName"));
        
        // Edge case - write an empty field name
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName(""));
        
        // Error scenario - write a duplicate field name
        try {
            JsonWriteContext contextWithDupDetector = JsonWriteContext.createRootContext(new DupDetector());
            contextWithDupDetector.writeFieldName("fieldName");
            contextWithDupDetector.writeFieldName("fieldName");
            fail("Expected JsonGenerationException to be thrown");
        } catch (JsonGenerationException e) {
            assertEquals("Duplicate field 'fieldName'", e.getMessage());
        }
    }
    
    @Test
    public void testWriteValue() {
        JsonWriteContext arrayContext = JsonWriteContext.createRootContext().createChildArrayContext();
        JsonWriteContext objectContext = JsonWriteContext.createRootContext().createChildObjectContext();
        
        // Typical use case - write a value in an array
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, arrayContext.writeValue());
        
        // Typical use case - write a value in an object
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, objectContext.writeValue());
        
        // Edge case - write a value as the first element in an array
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, arrayContext.writeValue());
        
        // Edge case - write a value as the first element in an object
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, objectContext.writeValue());
        
        // Error scenario - write a value in the root context
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, rootContext.writeValue());
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, rootContext.writeValue());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        
        // Typical use case - create a child array context
        JsonWriteContext childArrayContext = rootContext.createChildArrayContext();
        assertNotNull(childArrayContext);
        assertEquals(JsonWriteContext.TYPE_ARRAY, childArrayContext.getType());
        assertEquals(rootContext, childArrayContext.getParent());
        
        // Edge case - create multiple child array contexts
        JsonWriteContext childArrayContext2 = rootContext.createChildArrayContext();
        assertNotNull(childArrayContext2);
        assertEquals(JsonWriteContext.TYPE_ARRAY, childArrayContext2.getType());
        assertEquals(rootContext, childArrayContext2.getParent());
        
        // Error scenario - create a child array without a parent
        try {
            JsonWriteContext childArrayContext3 = new JsonWriteContext(TYPE_ARRAY, null, null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }
    
    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        
        // Typical use case - create a child object context
        JsonWriteContext childObjectContext = rootContext.createChildObjectContext();
        assertNotNull(childObjectContext);
        assertEquals(JsonWriteContext.TYPE_OBJECT, childObjectContext.getType());
        assertEquals(rootContext, childObjectContext.getParent());
        
        // Edge case - create multiple child object contexts
        JsonWriteContext childObjectContext2 = rootContext.createChildObjectContext();
        assertNotNull(childObjectContext);
        assertEquals(JsonWriteContext.TYPE_OBJECT, childObjectContext2.getType());
        assertEquals(rootContext, childObjectContext2.getParent());
        
        // Error scenario - create a child object without a parent
        try {
            JsonWriteContext childObjectContext3 = new JsonWriteContext(TYPE_OBJECT, null, null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }
    
}