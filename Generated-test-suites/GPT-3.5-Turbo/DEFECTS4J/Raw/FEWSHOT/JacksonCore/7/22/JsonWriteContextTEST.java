
package com.fasterxml.jackson.core.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class JsonWriteContextTest {
    
    private JsonWriteContext jsonContext;
    
    @Before
    public void setUp() {
        jsonContext = JsonWriteContext.createRootContext();
    }
    
    @Test
    public void testCreateRootContext() {
        assertNull(jsonContext.getParent());
        assertEquals(JsonWriteContext.TYPE_ROOT, jsonContext.type);
        assertNull(jsonContext.getCurrentName());
        assertNull(jsonContext.getDupDetector());
    }
    
    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext childContext = jsonContext.createChildArrayContext();
        assertEquals(jsonContext, childContext.getParent());
        assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.type);
        assertFalse(childContext._gotName);
        assertEquals(0, childContext.getCurrentIndex());
        assertNull(childContext.getDupDetector());
    }
    
    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext childContext = jsonContext.createChildObjectContext();
        assertEquals(jsonContext, childContext.getParent());
        assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.type);
        assertFalse(childContext._gotName);
        assertNull(childContext.getCurrentName());
        assertNull(childContext.getDupDetector());
    }
    
    @Test
    public void testWriteFieldName() {
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, jsonContext.writeFieldName("field1"));
        assertTrue(jsonContext._gotName);
        assertEquals("field1", jsonContext.getCurrentName());
    }
    
    @Test
    public void testWriteFieldNameWithDuplicate() {
        JsonWriteContext childContext = jsonContext.createChildObjectContext().withDupDetector(new DupDetector(null));
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, childContext.writeFieldName("field1"));
        assertTrue(childContext._gotName);
        
        try {
            childContext.writeFieldName("field1");
        } catch (JsonProcessingException e) {
            assertEquals("Duplicate field 'field1'", e.getMessage());
        }
    }
    
    @Test
    public void testWriteFieldNameAfterValue() {
        JsonWriteContext childContext = jsonContext.createChildObjectContext();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, childContext.writeFieldName("field1"));
        assertTrue(childContext._gotName);
        assertEquals("field1", childContext.getCurrentName());
        
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, childContext.writeFieldName("field2"));
        assertTrue(childContext._gotName);
        assertEquals("field2", childContext.getCurrentName());
    }
    
    @Test
    public void testWriteValueForObject() {
        jsonContext._type = JsonWriteContext.TYPE_OBJECT;
        jsonContext._gotName = true;
        jsonContext._index = 0;
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, jsonContext.writeValue());
        assertFalse(jsonContext._gotName);
        assertEquals(1, jsonContext.getCurrentIndex());
    }
    
    @Test
    public void testWriteValueForArray() {
        jsonContext._type = JsonWriteContext.TYPE_ARRAY;
        jsonContext._index = 0;
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, jsonContext.writeValue());
        assertEquals(1, jsonContext.getCurrentIndex());
    }
    
    @Test
    public void testWriteValueForRoot() {
        jsonContext._index = 0;
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, jsonContext.writeValue());
        assertEquals(1, jsonContext.getCurrentIndex());
    }
    
    @Test
    public void testGetCurrentValue() {
        assertNull(jsonContext.getCurrentValue());
    }
    
    @Test
    public void testSetCurrentValue() {
        jsonContext.setCurrentValue("value1");
        assertEquals("value1", jsonContext.getCurrentValue());
    }
    
    @Test
    public void testToString() {
        assertEquals("/", jsonContext.toString());
    }
}
