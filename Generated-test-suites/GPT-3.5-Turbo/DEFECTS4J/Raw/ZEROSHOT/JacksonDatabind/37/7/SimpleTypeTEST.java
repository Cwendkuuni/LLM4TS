package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;

public class SimpleTypeTest {

	@Test
	public void testConstructUnsafe() {
		SimpleType type = SimpleType.constructUnsafe(Map.class);
		assertNull(type.getSuperClass());
		assertNull(type.getSuperInterfaces());
		assertFalse(type.isContainerType());
		assertEquals(Map.class, type.getRawClass());
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
		SimpleType.constructUnsafe(int[].class);
	}

	@Test
	public void testConstruct() {
		SimpleType type = SimpleType.construct(Map.class);
		assertNull(type.getSuperClass());
		assertNull(type.getSuperInterfaces());
		assertFalse(type.isContainerType());
		assertEquals(Map.class, type.getRawClass());
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
		SimpleType.construct(int[].class);
	}

	@Test
	public void test_narrow() {
		SimpleType baseType = SimpleType.construct(Map.class);
		SimpleType newType = (SimpleType) baseType._narrow(Object.class);
		assertEquals(Object.class, newType.getRawClass());
		assertNull(newType.getSuperClass());
		assertNull(newType.getSuperInterfaces());
		assertFalse(newType.isContainerType());
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_narrowWithIncompatibleClass() {
		SimpleType baseType = SimpleType.construct(Map.class);
		baseType._narrow(Collection.class);
	}

	@Test
	public void test_withContentType() {
		SimpleType baseType = SimpleType.construct(Map.class);
		try {
			baseType.withContentType(null);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
		}
	}

	@Test
	public void test_withTypeHandler() {
		SimpleType baseType = SimpleType.construct(Map.class);
		Object typeHandler = new Object();
		SimpleType newType = baseType.withTypeHandler(typeHandler);
		assertEquals(Map.class, newType.getRawClass());
		assertEquals(typeHandler, newType.getTypeHandler());
	}

	@Test
	public void test_withContentTypeHandler() {
		SimpleType baseType = SimpleType.construct(Map.class);
		try {
			baseType.withContentTypeHandler(new Object());
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
		}
	}

	@Test
	public void test_withValueHandler() {
		SimpleType baseType = SimpleType.construct(Map.class);
		Object valueHandler = new Object();
		SimpleType newType = baseType.withValueHandler(valueHandler);
		assertEquals(Map.class, newType.getRawClass());
		assertEquals(valueHandler, newType.getValueHandler());
	}

	@Test
	public void test_withContentValueHandler() {
		SimpleType baseType = SimpleType.construct(Map.class);
		try {
			baseType.withContentValueHandler(new Object());
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
		}
	}

	@Test
	public void test_withStaticTyping() {
		SimpleType baseType = SimpleType.construct(Map.class);
		SimpleType newType = baseType.withStaticTyping();
		assertEquals(Map.class, newType.getRawClass());
		assertTrue(newType.isStaticTyping());
	}

	@Test
	public void test_refine() {
		SimpleType baseType = SimpleType.construct(Map.class);
		JavaType refinedType = baseType.refine(null, null, null, null);
		assertNull(refinedType);
	}

	@Test
	public void testGetErasedSignature() {
		SimpleType type = SimpleType.construct(Map.class);
		StringBuilder sb = new StringBuilder();
		String signature = type.getErasedSignature(sb).toString();
		assertEquals("Ljava/util/Map;", signature);
	}

	@Test
	public void testGetGenericSignature() {
		SimpleType type = SimpleType.construct(Map.class);
		StringBuilder sb = new StringBuilder();
		String signature = type.getGenericSignature(sb).toString();
		assertEquals("Ljava/util/Map<*>;", signature);
	}

	@Test
	public void testToString() {
		SimpleType type = SimpleType.construct(Map.class);
		assertEquals("[simple type, class java.util.Map]", type.toString());
	}

	@Test
	public void testEquals() {
		SimpleType type1 = SimpleType.construct(Map.class);
		SimpleType type2 = SimpleType.construct(Map.class);
		assertTrue(type1.equals(type2));
	}

}