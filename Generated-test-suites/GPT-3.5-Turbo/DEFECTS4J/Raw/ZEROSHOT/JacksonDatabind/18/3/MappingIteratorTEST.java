package com.fasterxml.jackson.databind;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MappingIteratorTest {

    private MappingIterator mappingIterator;
    private JsonParser dummyParser;
    
    @Before
    public void setUp() {
        mappingIterator = new MappingIterator(null, dummyParser, null, null, false, null);
    }
    
    @After
    public void tearDown() {
        mappingIterator = null;
    }
    
    @Test
    public void testHasNext_noInput_returnFalse() {
        assertFalse(mappingIterator.hasNext());
    }
    
    @Test
    public void testHasNext_inputHasNext_returnTrue() {
        mappingIterator._parser = dummyParser;
        dummyParser.setCurrentToken(JsonToken.START_ARRAY);
        assertTrue(mappingIterator.hasNext());
    }
    
    @Test
    public void testHasNext_inputHasNoNext_returnFalse() {
        mappingIterator._parser = dummyParser;
        dummyParser.setCurrentToken(null);
        assertFalse(mappingIterator.hasNext());
    }
    
    @Test
    public void testNext_valueDeserializedReturnException_throwRuntimeJsonMappingException() {
        mappingIterator._parser = dummyParser;
        mappingIterator._deserializer = null;
        mappingIterator._context = null;
        
        try {
            mappingIterator.next();
            fail("Expected RuntimeJsonMappingException not thrown");
        } catch (RuntimeJsonMappingException e) {
            assertNotNull(e.getMessage());
        } catch (IOException e) {
            fail("Expected RuntimeJsonMappingException not thrown");
        }
    }
    
    @Test
    public void testNext_mapHasNextCheckedFalse_throwNoSuchElementException() {
        mappingIterator._hasNextChecked = false;
        
        try {
            mappingIterator.next();
            fail("Expected NoSuchElementException not thrown");
        } catch (NoSuchElementException e) {
            assertNotNull(e.getMessage());
        } catch (IOException e) {
            fail("Expected NoSuchElementException not thrown");
        }
    }
    
    @Test
    public void testNext_mapHasNextCheckedTrue_returnNextValue() {
        mappingIterator._hasNextChecked = true;
        mappingIterator._parser = dummyParser;
        mappingIterator._deserializer = null;
        mappingIterator._context = null;
        
        try {
            Object result = mappingIterator.next();
            assertNull(result);
        } catch (NoSuchElementException e) {
            fail("Expected RuntimeException not thrown");
        } catch (IOException e) {
            fail("Expected RuntimeException not thrown");
        }
    }
    
    @Test
    public void testRemove_unsupportedOperationException() {
        try {
            mappingIterator.remove();
            fail("Expected UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            assertNotNull(e.getMessage());
        }
    }
    
    @Test
    public void testClose_dummyParserNotNull_closeParser() {
        mappingIterator._parser = dummyParser;
        
        try {
            mappingIterator.close();
        } catch (IOException e) {
            fail("Expected IOException not thrown");
        }
    }
    
    @Test
    public void testReadAll_hasNextValueTrue_returnList() {
        dummyParser.setCurrentToken(JsonToken.START_ARRAY);
        mappingIterator._parser = dummyParser;
        
        List<Object> resultList = new ArrayList<>();
        try {
            List<Object> result = mappingIterator.readAll(resultList);
            assertNotNull(result);
            assertEquals(resultList, result);
        } catch (IOException e) {
            fail("Expected IOException not thrown");
        }
    }
    
    @Test
    public void testReadAll_hasNextValueFalse_returnList() {
        dummyParser.setCurrentToken(null);
        mappingIterator._parser = dummyParser;
        
        List<Object> resultList = new ArrayList<>();
        try {
            List<Object> result = mappingIterator.readAll(resultList);
            assertNotNull(result);
            assertEquals(resultList, result);
        } catch (IOException e) {
            fail("Expected IOException not thrown");
        }
    }
    
    @Test
    public void testReadAll_hasNextValueFalse_returnCollection() {
        dummyParser.setCurrentToken(null);
        mappingIterator._parser = dummyParser;
        
        List<Object> results = new ArrayList<>();
        try {
            List<Object> result = mappingIterator.readAll(results);
            assertNotNull(result);
            assertEquals(results, result);
        } catch (IOException e) {
            fail("Expected IOException not thrown");
        }
    }
    
    @Test
    public void testGetParser_returnParser() {
        mappingIterator._parser = dummyParser;
        
        JsonParser result = mappingIterator.getParser();
        assertNotNull(result);
        assertEquals(dummyParser, result);
    }
    
    @Test
    public void testGetParserSchema_returnSchema() {
        mappingIterator._parser = dummyParser;
        
        dummyParser.setSchema(null);
        
        FormatSchema result = mappingIterator.getParserSchema();
        assertNull(result);
    }
    
    @Test
    public void testGetParserSchema_noSchema_returnSchema() {
        mappingIterator._parser = dummyParser;
        
        FormatSchema result = mappingIterator.getParserSchema();
        assertNotNull(result);
    }
    
    @Test
    public void testGetCurrentLocation_returnLocation() {
        mappingIterator._parser = dummyParser;
        assertTrue(mappingIterator.getCurrentLocation() instanceof JsonLocation);
    }
}