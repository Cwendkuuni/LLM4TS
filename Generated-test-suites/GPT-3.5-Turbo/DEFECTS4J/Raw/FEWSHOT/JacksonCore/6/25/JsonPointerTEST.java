package com.fasterxml.jackson.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class JsonPointerTest {
	
	private JsonPointer empty;
	private JsonPointer nonEmpty;
	private JsonPointer escaped;
	
	@Before
	public void setup() {
		empty = new JsonPointer();
		nonEmpty = new JsonPointer("test", "node", empty);
		escaped = new JsonPointer("test", "node", nonEmpty);
	}
	
	@Test
	public void testMatches() {
		assertTrue(empty.matches());
		assertFalse(nonEmpty.matches());
		assertFalse(escaped.matches());
	}
	
	@Test
	public void testGetMatchingProperty() {
		assertNull(empty.getMatchingProperty());
		assertEquals("test", nonEmpty.getMatchingProperty());
		assertEquals("test", escaped.getMatchingProperty());
	}
	
	@Test
	public void testGetMatchingIndex() {
		assertEquals(-1, empty.getMatchingIndex());
		assertEquals(-1, nonEmpty.getMatchingIndex());
		assertEquals(-1, escaped.getMatchingIndex());
	}
	
	@Test
	public void testMayMatchProperty() {
		assertFalse(empty.mayMatchProperty());
		assertTrue(nonEmpty.mayMatchProperty());
		assertTrue(escaped.mayMatchProperty());
	}
	
	@Test
	public void testMayMatchElement() {
		assertFalse(empty.mayMatchElement());
		assertFalse(nonEmpty.mayMatchElement());
		assertFalse(escaped.mayMatchElement());
	}
	
	@Test
	public void testMatchProperty() {
		assertNull(empty.matchProperty("test"));
		assertEquals(nonEmpty, nonEmpty.matchProperty("test"));
		assertEquals(escaped, escaped.matchProperty("test"));
	}
	
	@Test
	public void testMatchElement() {
		assertNull(empty.matchElement(0));
		assertNull(nonEmpty.matchElement(0));
		assertNull(escaped.matchElement(0));
	}
	
	@Test
	public void testTail() {
		assertEquals(empty, empty.tail());
		assertEquals(empty, nonEmpty.tail());
		assertEquals(nonEmpty, escaped.tail());
	}
	
	@Test
	public void testToString() {
		assertEquals("", empty.toString());
		assertEquals("/test/node", nonEmpty.toString());
		assertEquals("/test/node/test/node", escaped.toString());
	}
	
	@Test
	public void testHashCode() {
		assertEquals(empty.hashCode(), empty.hashCode());
		assertEquals(nonEmpty.hashCode(), nonEmpty.hashCode());
		assertEquals(escaped.hashCode(), escaped.hashCode());
	}
	
	@Test
	public void testEquals() {
		assertTrue(empty.equals(empty));
		assertTrue(nonEmpty.equals(nonEmpty));
		assertTrue(escaped.equals(escaped));
		
		assertFalse(empty.equals(null));
		assertFalse(empty.equals(new Object()));
		
		assertFalse(empty.equals(nonEmpty));
		assertFalse(nonEmpty.equals(escaped));
		assertFalse(escaped.equals(empty));
	}
}