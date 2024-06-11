package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;

public class SimpleTypeTest {

	@Test
	public void testConstructUnsafe() {
		SimpleType type = SimpleType.constructUnsafe(String.class);
		assertEquals(String.class, type.getRawClass());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructUnsafeWithMap() {
		SimpleType.constructUnsafe(Map.class);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructUnsafeWithCollection() {
		SimpleType.constructUnsafe(Collection.class);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructUnsafeWithArray() {
		SimpleType.constructUnsafe(String[].class);
	}

	@Test
	public void testConstruct() {
		SimpleType type = SimpleType.construct(String.class);
		assertEquals(String.class, type.getRawClass());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructWithMap() {
		SimpleType.construct(Map.class);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructWithCollection() {
		SimpleType.construct(Collection.class);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructWithArray() {
		SimpleType.construct(String[].class);
	}
}