package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;
import java.io.IOException;
import java.util.*;
import com.fasterxml.jackson.core.*;
import org.junit.Test;

public class MappingIteratorTest {
    
    @SuppressWarnings("unchecked")
    @Test
    public void testHasNextValue_noValue() throws IOException {
        MappingIterator<Object> it = MappingIterator.emptyIterator();
        assertFalse(it.hasNextValue());
        it.close();
    }
    
    @Test
    public void testHasNextValue_withValue() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("[1, 2, 3]");
        MappingIterator<Integer> it = new MappingIterator<Integer>(null, parser, null, null, true, null);
        
        assertTrue(it.hasNextValue());
        it.close();
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testHasNextValue_exception() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("[1, 2, 3]");
        MappingIterator<Integer> it = new MappingIterator<Integer>(null, parser, null, null, true, null);
        
        try {
            it.hasNextValue();
            fail("Should throw IOException");
        } catch (IOException ex) {
            // expected
        }
        it.close();
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testNextValue_noValue() throws IOException {
        MappingIterator<Object> it = MappingIterator.emptyIterator();
        try {
            it.nextValue();
            fail("Should throw NoSuchElementException");
        } catch (NoSuchElementException ex) {
            // expected
        }
        it.close();
    }
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void testNextValue_withValue() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("[1, 2, 3]");
        MappingIterator<Integer> it = new MappingIterator<Integer>(null, parser, null, null, true, null);
        
        assertEquals(new Integer(1), it.nextValue());
        assertEquals(new Integer(2), it.nextValue());
        assertEquals(new Integer(3), it.nextValue());
        
        assertFalse(it.hasNextValue());
        
        it.close();
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testNextValue_exception() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("[1, 2, 3]");
        MappingIterator<Integer> it = new MappingIterator<Integer>(null, parser, null, null, true, null);
        
        try {
            it.nextValue();
            fail("Should throw IOException");
        } catch (IOException ex) {
            // expected
        }
        it.close();
    }
    
    @Test
    public void testClose() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("[1, 2, 3]");
        MappingIterator<Integer> it = new MappingIterator<Integer>(null, parser, null, null, true, null);
        
        it.close();
        assertFalse(it.hasNextValue());
    }
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void testReadAll() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("[1, 2, 3]");
        MappingIterator<Integer> it = new MappingIterator<Integer>(null, parser, null, null, true, null);
        
        List<Integer> list = it.readAll();
        assertEquals(Arrays.asList(1, 2, 3), list);
        
        it.close();
    }
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void testReadAll_withResultList() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("[1, 2, 3]");
        MappingIterator<Integer> it = new MappingIterator<Integer>(null, parser, null, null, true, null);
        
        List<Integer> existingList = new ArrayList<Integer>();
        List<Integer> list = it.readAll(existingList);
        assertSame(existingList, list);
        assertEquals(Arrays.asList(1, 2, 3), list);
        
        it.close();
    }
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void testReadAll_withResultCollection() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("[1, 2, 3]");
        MappingIterator<Integer> it = new MappingIterator<Integer>(null, parser, null, null, true, null);
        
        Set<Integer> existingSet = new HashSet<Integer>();
        Set<Integer> set = it.readAll(existingSet);
        assertSame(existingSet, set);
        assertEquals(new HashSet<Integer>(Arrays.asList(1, 2, 3)), set);
        
        it.close();
    }
    
    @Test
    public void testGetParser() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("[1, 2, 3]");
        MappingIterator<Integer> it = new MappingIterator<Integer>(null, parser, null, null, true, null);
        
        assertSame(parser, it.getParser());
        
        it.close();
    }

    @Test
    public void testGetParserSchema() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("[1, 2, 3]");
        MappingIterator<Integer> it = new MappingIterator<Integer>(null, parser, null, null, true, null);
        
        assertNull(it.getParserSchema());
        
        it.close();
    }
    
    @Test
    public void testGetCurrentLocation() throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("[1, 2, 3]");
        MappingIterator<Integer> it = new MappingIterator<Integer>(null, parser, null, null, true, null);
        
        assertNotNull(it.getCurrentLocation());
        
        it.close();
    }
    
}