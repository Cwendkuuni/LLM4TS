package com.fasterxml.jackson.databind;

import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class MappingIteratorTest {
    
    @Test
    public void testHasNext() throws IOException {
        // Create a mock parser for testing
        JsonParser parser = new MockJsonParser();
        
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(null, parser, null, null, true, null);
        
        // Test when parser has next token
        boolean result = iterator.hasNext();
        Assert.assertTrue(result);
        
        // Test when parser does not have next token
        ((MockJsonParser) parser).setHasNext(false);
        result = iterator.hasNext();
        Assert.assertFalse(result);
    }
    
    @Test
    public void testNext() throws IOException {
        // Create a mock parser for testing
        JsonParser parser = new MockJsonParser();
        
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(null, parser, null, null, true, null);
        
        // Test when parser has next token
        int result = iterator.next();
        Assert.assertEquals(1, result);
        
        // Test when parser does not have next token
        ((MockJsonParser) parser).setHasNext(false);
        try {
            iterator.next();
            Assert.fail("NoSuchElementException was not thrown");
        } catch (NoSuchElementException e) {
            // Expected exception
        }
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws IOException {
        // Create a mock parser for testing
        JsonParser parser = new MockJsonParser();
        
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(null, parser, null, null, true, null);
        
        iterator.remove();
    }
    
    @Test
    public void testClose() throws IOException {
        // Create a mock parser for testing
        JsonParser parser = new MockJsonParser();
        
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(null, parser, null, null, true, null);
        
        iterator.close();
        
        Assert.assertNull(iterator.getParser());
    }
    
    @Test
    public void testHasNextValue() throws IOException {
        // Create a mock parser for testing
        JsonParser parser = new MockJsonParser();
        
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(null, parser, null, null, true, null);
        
        // Test when parser has next token
        boolean result = iterator.hasNextValue();
        Assert.assertTrue(result);
        
        // Test when parser does not have next token
        ((MockJsonParser) parser).setHasNext(false);
        result = iterator.hasNextValue();
        Assert.assertFalse(result);
    }
    
    @Test
    public void testNextValue() throws IOException {
        // Create a mock parser for testing
        JsonParser parser = new MockJsonParser();
        
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(null, parser, null, null, true, null);
        
        // Test when parser has next token
        int result = iterator.nextValue();
        Assert.assertEquals(1, result);
        
        // Test when parser does not have next token
        ((MockJsonParser) parser).setHasNext(false);
        try {
            iterator.nextValue();
            Assert.fail("NoSuchElementException was not thrown");
        } catch (NoSuchElementException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testReadAll() throws IOException {
        // Create a mock parser for testing
        JsonParser parser = new MockJsonParser();
        
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(null, parser, null, null, true, null);
        
        List<Integer> resultList = iterator.readAll();
        
        // Check that all values were read
        Assert.assertEquals(3, resultList.size());
        Assert.assertEquals(1, (int) resultList.get(0));
        Assert.assertEquals(2, (int) resultList.get(1));
        Assert.assertEquals(3, (int) resultList.get(2));
    }
    
    @Test
    public void testReadAllWithList() throws IOException {
        // Create a mock parser for testing
        JsonParser parser = new MockJsonParser();
        
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(null, parser, null, null, true, null);
        
        List<Integer> resultList = new ArrayList<Integer>();
        iterator.readAll(resultList);
        
        // Check that all values were read
        Assert.assertEquals(3, resultList.size());
        Assert.assertEquals(1, (int) resultList.get(0));
        Assert.assertEquals(2, (int) resultList.get(1));
        Assert.assertEquals(3, (int) resultList.get(2));
    }
    
    @Test
    public void testReadAllWithCollection() throws IOException {
        // Create a mock parser for testing
        JsonParser parser = new MockJsonParser();
        
        MappingIterator<Integer> iterator = new MappingIterator<Integer>(null, parser, null, null, true, null);
        
        List<Integer> resultList = new ArrayList<Integer>();
        iterator.readAll(resultList);
        
        // Check that all values were read
        Assert.assertEquals(3, resultList.size());
        Assert.assertEquals(1, (int) resultList.get(0));
        Assert.assertEquals(2, (int) resultList.get(1));
        Assert.assertEquals(3, (int) resultList.get(2));
    }
}