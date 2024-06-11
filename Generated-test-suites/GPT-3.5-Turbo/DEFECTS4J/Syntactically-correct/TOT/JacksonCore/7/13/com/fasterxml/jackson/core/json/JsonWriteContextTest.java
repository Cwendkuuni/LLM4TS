package com.fasterxml.jackson.core.json;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Test
    public void testWriteFieldName_typical() {
        // Arrange
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        // Act
        int result = context.writeFieldName("name");
        
        // Assert
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        assertEquals("name", context.getCurrentName());
    }
    
    @Test
    public void testWriteFieldName_emptyName() {
        // Arrange
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        // Act
        int result = context.writeFieldName("");
        
        // Assert
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        assertEquals("", context.getCurrentName());
    }
    
    @Test
    public void testWriteFieldName_nullName() {
        // Arrange
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        // Act
        int result = context.writeFieldName(null);
        
        // Assert
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        assertNull(context.getCurrentName());
    }
    
    @Test
    public void testWriteFieldName_duplicateName() {
        // Arrange
        JsonWriteContext context = JsonWriteContext.createRootContext();
        DupDetectorMock detectorMock = new DupDetectorMock();
        context.withDupDetector(detectorMock);
        
        // Act
        int result = context.writeFieldName("name");
        
        // Assert
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        assertEquals("name", context.getCurrentName());
        assertTrue(detectorMock.isDupCalled);
    }
    
    @Test
    public void testWriteValue_typical() {
        // Arrange
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeFieldName("name");
        
        // Act
        int result = context.writeValue();
        
        // Assert
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
    }
    
    @Test
    public void testWriteValue_firstObjectInArray() {
        // Arrange
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.createChildArrayContext();
        
        // Act
        int result = context.writeValue();
        
        // Assert
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
    }
    
    @Test
    public void testWriteValue_subsequentObjectInArray() {
        // Arrange
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.createChildArrayContext();
        context.writeValue();
        context.writeValue();
        
        // Act
        int result = context.writeValue();
        
        // Assert
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
    }
    
    @Test
    public void testWriteValue_firstValueAfterRoot() {
        // Arrange
        JsonWriteContext context = JsonWriteContext.createRootContext();
        
        // Act
        int result = context.writeValue();
        
        // Assert
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
    }
    
    @Test
    public void testWriteValue_subsequentValueAfterRoot() {
        // Arrange
        JsonWriteContext context = JsonWriteContext.createRootContext();
        context.writeValue();
        context.writeValue();
        
        // Act
        int result = context.writeValue();
        
        // Assert
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, result);
    }
}

class DupDetectorMock extends DupDetector {
    boolean isDupCalled = false;
    
    @Override
    public boolean isDup(String name) throws JsonProcessingException {
        isDupCalled = true;
        return false;
    }
}