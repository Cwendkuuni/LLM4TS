package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class SimpleTypeTest {
	
	private SimpleType simpleType;
	
	@Before
	public void setup() {
		simpleType = new SimpleType(Object.class);
	}
	
	@Test
	public void testConstructUnsafe() {
		SimpleType constructedType = SimpleType.constructUnsafe(Object.class);
		assertEquals(Object.class, constructedType.getRawClass());
	}
	
	@Test
	public void testConstruct() {
		SimpleType constructedType = SimpleType.construct(Object.class);
		assertEquals(Object.class, constructedType.getRawClass());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructWithMap() {
		SimpleType.construct(Map.class);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructWithCollection() {
		SimpleType.construct(Collection.class);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructWithArray() {
		SimpleType.construct(Array.class);
	}
	
	@Test
	public void testWithContentType() {
		JavaType contentType = simpleType.withContentType(Object.class);
		assertEquals(Object.class, contentType.getRawClass());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWithContentTypeException() {
		simpleType.withContentType(Object.class);
	}
	
	@Test
	public void testWithTypeHandler() {
		Object handler = new Object();
		SimpleType type = simpleType.withTypeHandler(handler);
		assertEquals(handler, type.getTypeHandler());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWithContentTypeHandler() {
		simpleType.withContentTypeHandler(new Object());
	}
	
	@Test
	public void testWithValueHandler() {
		Object handler = new Object();
		SimpleType type = simpleType.withValueHandler(handler);
		assertEquals(handler, type.getValueHandler());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWithContentValueHandler() {
		simpleType.withContentValueHandler(new Object());
	}
	
	@Test
	public void testWithStaticTyping() {
		SimpleType newType = simpleType.withStaticTyping();
		assertEquals(true, newType.isStatic());
	}
	
	@Test
	public void testRefine() {
		JavaType refinedType = simpleType.refine(Object.class, new TypeBindings(), null, null);
		assertEquals(null, refinedType);
	}
	
	@Test
	public void testIsContainerType() {
		assertEquals(false, simpleType.isContainerType());
	}
	
	@Test
	public void testGetErasedSignature() {
		StringBuilder sb = new StringBuilder();
		StringBuilder signature = simpleType.getErasedSignature(sb);
		assertEquals("Ljava/lang/Object;", signature.toString());
	}
	
	@Test
	public void testGetGenericSignature() {
		StringBuilder sb = new StringBuilder();
		StringBuilder signature = simpleType.getGenericSignature(sb);
		assertEquals("Ljava/lang/Object;;", signature.toString());
	}
	
	@Test
	public void testToString() {
		String expected = "[simple type, class java.lang.Object]";
		assertEquals(expected, simpleType.toString());
	}
	
	@Test
	public void testEquals() {
		SimpleType type = new SimpleType(Object.class);
		assertEquals(true, simpleType.equals(type));
	}
}