package com.fasterxml.jackson.databind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MappingIteratorTest {
	
	private MappingIterator<Integer> iterator;
	
	@Before
	public void setUp() throws IOException {
		List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
		JsonParser parser = null;
		DeserializationContext context = null;
		JsonDeserializer<Integer> deserializer = null;
		boolean managedParser = false;
		Object valueToUpdate = null;
		JavaType type = null;
		
		iterator = new MappingIterator<>(type, parser, context, deserializer, managedParser, valueToUpdate);
	}
	
	@After
	public void tearDown() throws IOException {
		iterator.close();
	}
	
	@Test
	public void testHasNext() {
		assertTrue(iterator.hasNext());
	}
	
	@Test
	public void testHasNextValue() throws IOException {
		assertTrue(iterator.hasNextValue());
	}
	
	@Test
	public void testNext() {
		assertEquals(Integer.valueOf(1), iterator.next());
		assertEquals(Integer.valueOf(2), iterator.next());
		assertEquals(Integer.valueOf(3), iterator.next());
		assertEquals(Integer.valueOf(4), iterator.next());
		assertEquals(Integer.valueOf(5), iterator.next());
	}
	
	@Test(expected = RuntimeException.class)
	public void testNextWithIOException() throws IOException {
		iterator.next();
		throw new IOException();
	}
	
	@Test
	public void testReadAll() throws IOException {
		List<Integer> resultList = new ArrayList<>();
		iterator.readAll(resultList);
		assertEquals(5, resultList.size());
		assertTrue(resultList.contains(1));
		assertTrue(resultList.contains(2));
		assertTrue(resultList.contains(3));
		assertTrue(resultList.contains(4));
		assertTrue(resultList.contains(5));
	}
	
	@Test
	public void testReadAllCollection() throws IOException {
		List<Integer> resultList = new ArrayList<>();
		iterator.readAll(resultList);
		assertEquals(5, resultList.size());
		assertTrue(resultList.contains(1));
		assertTrue(resultList.contains(2));
		assertTrue(resultList.contains(3));
		assertTrue(resultList.contains(4));
		assertTrue(resultList.contains(5));
	}
	
	@Test
	public void testGetParser() {
		assertTrue(iterator.getParser() == null);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testRemove() {
		iterator.remove();
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testNoSuchElementException() {
		while (true) {
			iterator.next();
		}
	}
	
	@Test(expected = RuntimeException.class)
	public void testHandleMappingException() throws IOException {
		iterator.next();
		throw new JsonMappingException(null, "Test Mapping Exception");
	}
	
	@Test(expected = RuntimeException.class)
	public void testHandleIOException() throws IOException {
		iterator.next();
		throw new IOException("Test IO Exception");
	}
	
	@Test
	public void testClose() throws IOException {
		iterator.close();
		assertTrue(iterator.hasNextValue() == false);
		assertFalse(iterator.hasNext());
	}
}