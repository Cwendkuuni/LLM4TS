package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MappingIteratorTest {
    
    private MappingIterator<Object> mappingIterator;
    
    @Before
    public void setUp() throws Exception {
        mappingIterator = new MappingIterator<Object>(null, null, null, null, false, null);
    }

    @After
    public void tearDown() throws Exception {
        mappingIterator = null;
    }
    
    @Test
    public void testHasNextValue_WhenNoNextValue_ReturnsFalse() throws IOException {
        assertFalse(mappingIterator.hasNextValue());
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testNextValue_WhenNoNextValue_ThrowsNoSuchElementException() throws IOException {
        mappingIterator.nextValue();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove_ThrowsUnsupportedOperationException() {
        mappingIterator.remove();
    }
    
    @Test
    public void testClose_WhenParserNotNull_CallsCloseOnParser() throws IOException {
        JsonParser parser = new JsonParser();
        mappingIterator = new MappingIterator<Object>(null, parser, null, null, true, null);
        
        mappingIterator.close();
        
        assertTrue(parser.isCloseCalled());
    }
    
    @Test
    public void testClose_WhenParserNull_DoesNotCallCloseOnParser() throws IOException {
        JsonParser parser = new JsonParser();
        mappingIterator = new MappingIterator<Object>(null, null, null, null, true, null);
        
        mappingIterator.close();
        
        assertFalse(parser.isCloseCalled());
    }
    
    @Test
    public void testReadAll_WhenHasNextValue_ReturnsListWithAllValues() throws IOException {
        List<Object> resultList = new ArrayList<>();
        mappingIterator = new MappingIterator<Object>(null, null, null, null, true, null) {
            private boolean hasFirstValue = true;
            
            @Override
            public boolean hasNextValue() throws IOException {
                if (hasFirstValue) {
                    hasFirstValue = false;
                    return true;
                }
                return super.hasNextValue();
            }
            
            @Override
            public Object nextValue() throws IOException {
                return "value";
            }
        };
        
        mappingIterator.readAll(resultList);
        
        assertEquals(1, resultList.size());
        assertEquals("value", resultList.get(0));
    }

}