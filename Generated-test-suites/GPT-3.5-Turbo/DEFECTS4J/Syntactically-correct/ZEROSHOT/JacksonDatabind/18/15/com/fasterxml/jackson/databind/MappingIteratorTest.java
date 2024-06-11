package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class MappingIteratorTest
{
    MappingIterator<Integer> iterator;
    ObjectMapper objectMapper;
    
    @Before
    public void setup() throws IOException {
        objectMapper = new ObjectMapper();
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        iterator = new MappingIterator<Integer>(objectMapper.getTypeFactory().constructType(Integer.class), objectMapper.treeAsTokens(objectMapper.valueToTree(list)), objectMapper.getDeserializationContext(), objectMapper.getDeserializerProvider().findValueDeserializer(objectMapper.getDeserializationContext().getConfig(), objectMapper.constructType(Integer.class)),true, null);
    }

    @Test
    public void testHasNext() {
        assertTrue(iterator.hasNext());
    }
    
    @Test
    public void testHasNextWithEmptyList() throws IOException {
        List<Integer> list = new ArrayList<Integer>();
        iterator = new MappingIterator<Integer>(objectMapper.getTypeFactory().constructType(Integer.class), objectMapper.treeAsTokens(objectMapper.valueToTree(list)), objectMapper.getDeserializationContext(), objectMapper.getDeserializerProvider().findValueDeserializer(objectMapper.getDeserializationContext().getConfig(), objectMapper.constructType(Integer.class)),true, null);
        assertFalse(iterator.hasNext());
    }
    
    @Test(expected = RuntimeException.class)
    public void testHasNextWithError() throws IOException {
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(null);
        list.add(3);
        iterator = new MappingIterator<Integer>(objectMapper.getTypeFactory().constructType(Integer.class), objectMapper.treeAsTokens(objectMapper.valueToTree(list)), objectMapper.getDeserializationContext(), objectMapper.getDeserializerProvider().findValueDeserializer(objectMapper.getDeserializationContext().getConfig(), objectMapper.constructType(Integer.class)),true, null);
        iterator.hasNext();
    }
    
    @Test
    public void testNext() {
        assertEquals(new Integer(1), iterator.next());
        assertEquals(new Integer(2), iterator.next());
        assertEquals(new Integer(3), iterator.next());
    }
    
    @Test
    public void testNextWithEmptyList() throws IOException {
        List<Integer> list = new ArrayList<Integer>();
        iterator = new MappingIterator<Integer>(objectMapper.getTypeFactory().constructType(Integer.class), objectMapper.treeAsTokens(objectMapper.valueToTree(list)), objectMapper.getDeserializationContext(), objectMapper.getDeserializerProvider().findValueDeserializer(objectMapper.getDeserializationContext().getConfig(), objectMapper.constructType(Integer.class)),true, null);
        assertFalse(iterator.hasNext());
        try {
            iterator.next();
            fail("Expected NoSuchElementException to be thrown");
        } catch (NoSuchElementException e) {
            // expected
        }
    }
    
    @Test(expected = RuntimeException.class)
    public void testNextWithError() throws IOException {
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(null);
        list.add(3);
        iterator = new MappingIterator<Integer>(objectMapper.getTypeFactory().constructType(Integer.class), objectMapper.treeAsTokens(objectMapper.valueToTree(list)), objectMapper.getDeserializationContext(), objectMapper.getDeserializerProvider().findValueDeserializer(objectMapper.getDeserializationContext().getConfig(), objectMapper.constructType(Integer.class)),true, null);
        iterator.next();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        iterator.remove();
    }
    
    @Test
    public void testClose() {
        try {
            iterator.close();
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test(expected = IOException.class)
    public void testCloseWithError() throws IOException {
    	objectMapper = null;
        iterator = new MappingIterator<Integer>(objectMapper.getTypeFactory().constructType(Integer.class), objectMapper.treeAsTokens(objectMapper.valueToTree(new ArrayList<Integer>())), objectMapper.getDeserializationContext(), objectMapper.getDeserializerProvider().findValueDeserializer(objectMapper.getDeserializationContext().getConfig(), objectMapper.constructType(Integer.class)),true, null);
        iterator.close();
    }
    
    @Test
    public void testReadAll() throws IOException {
        List<Integer> resultList = iterator.readAll();
        assertEquals(3, resultList.size());
        assertEquals(new Integer(1), resultList.get(0));
        assertEquals(new Integer(2), resultList.get(1));
        assertEquals(new Integer(3), resultList.get(2));
    }
    
    @Test
    public void testReadAllWithEmptyList() throws IOException {
        List<Integer> list = new ArrayList<Integer>();
        iterator = new MappingIterator<Integer>(objectMapper.getTypeFactory().constructType(Integer.class), objectMapper.treeAsTokens(objectMapper.valueToTree(list)), objectMapper.getDeserializationContext(), objectMapper.getDeserializerProvider().findValueDeserializer(objectMapper.getDeserializationContext().getConfig(), objectMapper.constructType(Integer.class)),true, null);
        List<Integer> resultList = iterator.readAll();
        assertEquals(0, resultList.size());
    }
    
    @Test(expected = RuntimeException.class)
    public void testReadAllWithError() throws IOException {
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(null);
        list.add(3);
        iterator = new MappingIterator<Integer>(objectMapper.getTypeFactory().constructType(Integer.class), objectMapper.treeAsTokens(objectMapper.valueToTree(list)), objectMapper.getDeserializationContext(), objectMapper.getDeserializerProvider().findValueDeserializer(objectMapper.getDeserializationContext().getConfig(), objectMapper.constructType(Integer.class)),true, null);
        iterator.readAll();
    }
    
    @Test
    public void testReadAllWithResults() throws IOException {
        List<Integer> resultList = new ArrayList<Integer>();
        iterator.readAll(resultList);
        assertEquals(3, resultList.size());
        assertEquals(new Integer(1), resultList.get(0));
        assertEquals(new Integer(2), resultList.get(1));
        assertEquals(new Integer(3), resultList.get(2));
    }
    
    @Test
    public void testReadAllWithResultsAndEmptyList() throws IOException {
        List<Integer> list = new ArrayList<Integer>();
        iterator = new MappingIterator<Integer>(objectMapper.getTypeFactory().constructType(Integer.class), objectMapper.treeAsTokens(objectMapper.valueToTree(list)), objectMapper.getDeserializationContext(), objectMapper.getDeserializerProvider().findValueDeserializer(objectMapper.getDeserializationContext().getConfig(), objectMapper.constructType(Integer.class)),true, null);
        List<Integer> resultList = new ArrayList<Integer>();
        iterator.readAll(resultList);
        assertEquals(0, resultList.size());
    }
    
    @Test(expected = RuntimeException.class)
    public void testReadAllWithResultsWithError() throws IOException {
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(null);
        list.add(3);
        iterator = new MappingIterator<Integer>(objectMapper.getTypeFactory().constructType(Integer.class), objectMapper.treeAsTokens(objectMapper.valueToTree(list)), objectMapper.getDeserializationContext(), objectMapper.getDeserializerProvider().findValueDeserializer(objectMapper.getDeserializationContext().getConfig(), objectMapper.constructType(Integer.class)),true, null);
        iterator.readAll(new ArrayList<Integer>());
    }
    
    @Test
    public void testReadAllWithCollection() throws IOException {
        List<Integer> resultList = iterator.readAll(new ArrayList<Integer>());
        assertEquals(3, resultList.size());
        assertEquals(new Integer(1), resultList.get(0));
        assertEquals(new Integer(2), resultList.get(1));
        assertEquals(new Integer(3), resultList.get(2));
    }
    
    @Test
    public void testReadAllWithCollectionAndEmptyList() throws IOException {
        List<Integer> list = new ArrayList<Integer>();
        iterator = new MappingIterator<Integer>(objectMapper.getTypeFactory().constructType(Integer.class), objectMapper.treeAsTokens(objectMapper.valueToTree(list)), objectMapper.getDeserializationContext(), objectMapper.getDeserializerProvider().findValueDeserializer(objectMapper.getDeserializationContext().getConfig(), objectMapper.constructType(Integer.class)),true, null);
        List<Integer> resultList = iterator.readAll(new ArrayList<Integer>());
        assertEquals(0, resultList.size());
    }
    
    @Test(expected = RuntimeException.class)
    public void testReadAllWithCollectionWithError() throws IOException {
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(null);
        list.add(3);
        iterator = new MappingIterator<Integer>(objectMapper.getTypeFactory().constructType(Integer.class), objectMapper.treeAsTokens(objectMapper.valueToTree(list)), objectMapper.getDeserializationContext(), objectMapper.getDeserializerProvider().findValueDeserializer(objectMapper.getDeserializationContext().getConfig(), objectMapper.constructType(Integer.class)),true, null);
        iterator.readAll(new ArrayList<Integer>());
    }
    
    @Test
    public void testGetParser() {
        assertNotNull(iterator.getParser());
    }
    
    @Test
    public void testGetParserSchema() {
        assertNull(iterator.getParserSchema());
    }
    
    @Test
    public void testGetCurrentLocation() {
        assertNotNull(iterator.getCurrentLocation());
    }
}