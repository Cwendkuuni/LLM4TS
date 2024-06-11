
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;

import static org.junit.Assert.*;

public class JsonWriteContextTest {
    
    private JsonWriteContext context;
    
    @Before
    public void setUp() {
        context = JsonWriteContext.createRootContext();
    }
    
    @Test
    public void testWriteFieldName() throws JsonGenerationException {
        int result = context.writeFieldName("fieldName");
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        
        String currentName = context.getCurrentName();
        assertEquals("fieldName", currentName);
    }
    
    @Test
    public void testWriteValueForObjectContext() {
        context = context.createChildObjectContext();
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        
        Object currentValue = context.getCurrentValue();
        assertNull(currentValue);
    }
    
    @Test
    public void testWriteValueForArrayContext() {
        context = context.createChildArrayContext();
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        
        Object currentValue = context.getCurrentValue();
        assertNull(currentValue);
    }
    
    @Test
    public void testWriteValueForRootContext() {
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        
        Object currentValue = context.getCurrentValue();
        assertNull(currentValue);
    }
    
    @Test
    public void testReset() {
        context.writeFieldName("fieldName");
        context.writeValue();
        
        context.reset(JsonWriteContext.TYPE_ARRAY);
        
        assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
        assertEquals(-1, context.getIndex());
        assertNull(context.getCurrentName());
        assertFalse(context.didGetName());
        assertNull(context.getCurrentValue());
        assertNull(context.getDupDetector());
    }
    
    @Test
    public void testWithDupDetector() {
        DupDetector dupDetector = new DupDetector();
        context.withDupDetector(dupDetector);
        
        assertEquals(dupDetector, context.getDupDetector());
    }
    
    @Test
    public void testGetCurrentValue() {
        Object currentValue = context.getCurrentValue();
        assertNull(currentValue);
    }
    
    @Test
    public void testSetCurrentValue() {
        Object value = new Object();
        context.setCurrentValue(value);
        
        Object currentValue = context.getCurrentValue();
        assertEquals(value, currentValue);
    }
    
    @Test
    public void testCreateRootContext() {
        context = JsonWriteContext.createRootContext();
        
        assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        assertNull(context.getParent());
        assertEquals(-1, context.getIndex());
        assertNull(context.getCurrentName());
        assertFalse(context.didGetName());
        assertNull(context.getCurrentValue());
        assertNull(context.getDupDetector());
    }
    
    @Test
    public void testCreateChildArrayContext() {
        context = context.createChildArrayContext();
        
        assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
        assertEquals(context, context.getParent());
        assertEquals(0, context.getIndex());
        assertNull(context.getCurrentName());
        assertFalse(context.didGetName());
        assertNull(context.getCurrentValue());
        assertNotNull(context.getDupDetector());
    }
    
    @Test
    public void testCreateChildObjectContext() {
        context = context.createChildObjectContext();
        
        assertEquals(JsonWriteContext.TYPE_OBJECT, context.getType());
        assertEquals(context, context.getParent());
        assertEquals(0, context.getIndex());
        assertNull(context.getCurrentName());
        assertFalse(context.didGetName());
        assertNull(context.getCurrentValue());
        assertNotNull(context.getDupDetector());
    }
}
