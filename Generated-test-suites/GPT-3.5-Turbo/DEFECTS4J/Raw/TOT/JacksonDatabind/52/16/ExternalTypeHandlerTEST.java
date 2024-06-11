import static org.junit.Assert.*;
import org.junit.Test;

public class ExternalTypeHandlerTest {

    @Test
    public void testStart() {
        ExternalTypeHandler handler = new ExternalTypeHandler.Builder().build();
        ExternalTypeHandler newHandler = handler.start();
        assertNotNull(newHandler);
    }
    
    @Test
    public void testHandleTypePropertyValue() {
        ExternalTypeHandler handler = new ExternalTypeHandler.Builder().build();
        // Test when property does not exist 
        boolean result = handler.handleTypePropertyValue(null, null, "propName", null);
        assertFalse(result);
        
        // Test when property exists but does not have type property name
        handler.addExternal(null, null);
        result = handler.handleTypePropertyValue(null, null, "propName", null);
        assertFalse(result);
        
        // Test when everything is valid
        // TODO: Complete this test case
        handler.addExternal(null, null);
        result = handler.handleTypePropertyValue(null, null, "typePropertyName", null);
        assertTrue(result);
    }
    
    @Test
    public void testHandlePropertyValue() {
        ExternalTypeHandler handler = new ExternalTypeHandler.Builder().build();
        // Test when property does not exist 
        boolean result = handler.handlePropertyValue(null, null, "propName", null);
        assertFalse(result);
        
        // Test when property exists but does not have type property name
        handler.addExternal(null, null);
        result = handler.handlePropertyValue(null, null, "propName", null);
        assertFalse(result);
        
        // Test when everything is valid
        // TODO: Complete this test case
        handler.addExternal(null, null);
        result = handler.handlePropertyValue(null, null, "typePropertyName", null);
        assertTrue(result);
    }
    
    @Test
    public void testComplete() {
        ExternalTypeHandler handler = new ExternalTypeHandler.Builder().build();
        // TODO: Complete this test case
    }
    
    @Test
    public void testCompleteWithBuffer() {
        ExternalTypeHandler handler = new ExternalTypeHandler.Builder().build();
        // TODO: Complete this test case
    }
    
}