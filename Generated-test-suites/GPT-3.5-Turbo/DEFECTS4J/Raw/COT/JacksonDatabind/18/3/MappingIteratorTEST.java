
package com.fasterxml.jackson.databind;

import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class MappingIteratorTest {

    @Test
    public void testHasNext() {
        MappingIterator<Integer> iterator = MappingIterator.emptyIterator();
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testNext() {
        MappingIterator<Integer> iterator = MappingIterator.emptyIterator();
        try {
            iterator.next();
            fail("NoSuchElementException should be thrown");
        } catch (NoSuchElementException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testRemove() {
        MappingIterator<Integer> iterator = MappingIterator.emptyIterator();
        try {
            iterator.remove();
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testClose() {
        MappingIterator<Integer> iterator = MappingIterator.emptyIterator();
        try {
            iterator.close();
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testHasNextValue() {
        MappingIterator<Integer> iterator = MappingIterator.emptyIterator();
        try {
            assertFalse(iterator.hasNextValue());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testNextValue() {
        MappingIterator<Integer> iterator = MappingIterator.emptyIterator();
        try {
            iterator.nextValue();
            fail("NoSuchElementException should be thrown");
        } catch (NoSuchElementException e) {
            // Expected exception
        } catch (Exception e) {
            fail("NoSuchElementException should be thrown");
        }
    }
    
    @Test
    public void testReadAll() {
        MappingIterator<Integer> iterator = MappingIterator.emptyIterator();
        try {
            List<Integer> result = iterator.readAll();
            assertNotNull(result);
            assertEquals(0, result.size());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testReadAllWithList() {
        MappingIterator<Integer> iterator = MappingIterator.emptyIterator();
        try {
            List<Integer> result = iterator.readAll(new ArrayList<Integer>());
            assertNotNull(result);
            assertEquals(0, result.size());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testReadAllWithCollection() {
        MappingIterator<Integer> iterator = MappingIterator.emptyIterator();
        try {
            Collection<Integer> result = iterator.readAll(new HashSet<Integer>());
            assertNotNull(result);
            assertEquals(0, result.size());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testGetParser() {
        MappingIterator<Integer> iterator = MappingIterator.emptyIterator();
        assertNull(iterator.getParser());
    }
    
    @Test
    public void testGetParserSchema() {
        MappingIterator<Integer> iterator = MappingIterator.emptyIterator();
        assertNull(iterator.getParserSchema());
    }
    
    @Test
    public void testGetCurrentLocation() {
        MappingIterator<Integer> iterator = MappingIterator.emptyIterator();
        assertNull(iterator.getCurrentLocation());
    }
}
