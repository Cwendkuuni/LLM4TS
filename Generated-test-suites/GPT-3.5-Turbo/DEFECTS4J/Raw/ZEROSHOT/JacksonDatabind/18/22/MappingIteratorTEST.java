package com.fasterxml.jackson.databind;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.*;

public class MappingIteratorTest {

    private MappingIterator<Integer> mappingIterator;
    private List<Integer> data;

    @Before
    public void setUp() {
        data = Arrays.asList(1, 2, 3);
        mappingIterator = new MappingIterator<>(null, null, null, null, false, null);
    }

    @Test
    public void testHasNext() {
        Assert.assertTrue(mappingIterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNext() {
        Assert.assertEquals(Integer.valueOf(1), mappingIterator.next());
        Assert.assertEquals(Integer.valueOf(2), mappingIterator.next());
        Assert.assertEquals(Integer.valueOf(3), mappingIterator.next());
        mappingIterator.next();
    }

    @Test
    public void testRemove() {
        try {
            mappingIterator.remove();
            Assert.fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testClose() throws IOException {
        mappingIterator.close();   
        // test passes if no exception is thrown
    }

    @Test
    public void testHasNextValue() throws IOException {
        Assert.assertTrue(mappingIterator.hasNextValue());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextValue() throws IOException {
        Assert.assertEquals(Integer.valueOf(1), mappingIterator.nextValue());
        Assert.assertEquals(Integer.valueOf(2), mappingIterator.nextValue());
        Assert.assertEquals(Integer.valueOf(3), mappingIterator.nextValue());
        mappingIterator.nextValue();
    }

    @Test
    public void testReadAll() throws IOException {
        List<Integer> resultList = mappingIterator.readAll(data);
        Assert.assertEquals(data, resultList);
    }

    @Test
    public void testReadAllWithCollection() throws IOException {
        Collection<Integer> results = new ArrayList<>();
        Collection<Integer> resultList = mappingIterator.readAll(results);
        Assert.assertEquals(data, resultList);
    }

    @Test
    public void testGetParser() {
        Assert.assertNull(mappingIterator.getParser());
    }

    @Test
    public void testGetParserSchema() {
        Assert.assertNull(mappingIterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation() {
        Assert.assertNull(mappingIterator.getCurrentLocation());
    }
}