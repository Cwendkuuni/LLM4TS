package com.fasterxml.jackson.core.json;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {
    
    private JsonWriteContext context;
    private DupDetector dupDetector;
    
    @Before
    public void setup() {
        dupDetector = new DupDetector();
        context = new JsonWriteContext(0, null, dupDetector);
    }
    
    @Test
    public void testReset() {
        JsonWriteContext resetContext = context.reset(1);
        assertNotNull(resetContext);
        assertEquals(1, resetContext._type);
        assertEquals(-1, resetContext._index);
        assertNull(resetContext._currentName);
        assertFalse(resetContext._gotName);
        assertNull(resetContext._currentValue);
        assertNull(resetContext._dups);
    }
    
    @Test
    public void testWithDupDetector() {
        JsonWriteContext dupContext = context.withDupDetector(dupDetector);
        assertNotNull(dupContext);
        assertEquals(dupDetector, dupContext._dups);
    }
    
    @Test
    public void testGetCurrentValue() {
        context.setCurrentValue("test");
        assertEquals("test", context.getCurrentValue());
    }
    
    @Test
    public void testGetCurrentName() {
        context._currentName = "name";
        assertEquals("name", context.getCurrentName());
    }
    
    @Test
    public void testCreateRootContext() {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext(dupDetector);
        assertNotNull(rootContext);
        assertEquals(JsonWriteContext.TYPE_ROOT, rootContext._type);
        assertNull(rootContext._parent);
        assertEquals(dupDetector, rootContext._dups);
        assertEquals(-1, rootContext._index);
        assertNull(rootContext._currentName);
        assertFalse(rootContext._gotName);
        assertNull(rootContext._currentValue);
    }
    
    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext childArrayContext = context.createChildArrayContext();
        assertNotNull(childArrayContext);
        assertEquals(JsonWriteContext.TYPE_ARRAY, childArrayContext._type);
        assertEquals(context, childArrayContext._parent);
        assertNotNull(childArrayContext._dups);
    }
    
    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext childObjectContext = context.createChildObjectContext();
        assertNotNull(childObjectContext);
        assertEquals(JsonWriteContext.TYPE_OBJECT, childObjectContext._type);
        assertEquals(context, childObjectContext._parent);
        assertNotNull(childObjectContext._dups);
    }
    
    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        int status = context.writeFieldName("fieldName");
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
        assertEquals("fieldName", context._currentName);
        assertTrue(context._gotName);
    }
    
    @Test
    public void testWriteFieldNameExpectValue() throws JsonProcessingException {
        context._gotName = true;
        int status = context.writeFieldName("fieldName");
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, status);
    }

    @Test
    public void testWriteValueInObject() {
        context._type = JsonWriteContext.TYPE_OBJECT;
        int status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);
        assertFalse(context._gotName);
        assertEquals(0, context._index);
    }

    @Test
    public void testWriteValueInArray() {
        context._type = JsonWriteContext.TYPE_ARRAY;
        int status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
        assertEquals(0, context._index);
    }
    
    @Test
    public void testWriteValueInRoot() {
        context._type = JsonWriteContext.TYPE_ROOT;
        int status = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
        assertEquals(0, context._index);
    }
    
    @Test
    public void testAppendDescTypeObject() {
        context._type = JsonWriteContext.TYPE_OBJECT;
        context._currentName = "fieldName";
        StringBuilder sb = new StringBuilder();
        context.appendDesc(sb);
        assertEquals("{\"fieldName\"}", sb.toString());
    }
    
    @Test
    public void testAppendDescTypeArray() {
        context._type = JsonWriteContext.TYPE_ARRAY;
        context._index = 1;
        StringBuilder sb = new StringBuilder();
        context.appendDesc(sb);
        assertEquals("[1]", sb.toString());
    }
    
    @Test
    public void testAppendDescTypeRoot() {
        context._type = JsonWriteContext.TYPE_ROOT;
        StringBuilder sb = new StringBuilder();
        context.appendDesc(sb);
        assertEquals("/", sb.toString());
    }
    
    @Test
    public void testToString() {
        context._type = JsonWriteContext.TYPE_OBJECT;
        context._currentName = "fieldName";
        assertEquals("{\"fieldName\"}", context.toString());
    }
}