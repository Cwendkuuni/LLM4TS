package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class CollectionTypeTest {

	@Test
	public void testConstruct() {
		Class<?> rawType = null;
		TypeBindings bindings = null;
		JavaType superClass = null;
		JavaType[] superInts = null;
		JavaType elemT = null;
		CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
		assertNotNull(collectionType);
	}

	@Test
	public void testConstruct() {
		Class<?> rawType = null;
		JavaType elemT = null;
		CollectionType collectionType = CollectionType.construct(rawType, elemT);
		assertNotNull(collectionType);
	}

	@Test
	public void test_narrow() {
		Class<?> subclass = null;
		CollectionType collectionType = new CollectionType();
		JavaType result = collectionType._narrow(subclass);
		assertNotNull(result);
	}

	@Test
	public void testWithContentType() {
		JavaType contentType = null;
		CollectionType collectionType = new CollectionType();
		JavaType result = collectionType.withContentType(contentType);
		assertNotNull(result);
	}

	@Test
	public void testWithTypeHandler() {
		Object h = null;
		CollectionType collectionType = new CollectionType();
		CollectionType result = collectionType.withTypeHandler(h);
		assertNotNull(result);
	}

	@Test
	public void testWithContentTypeHandler() {
		Object h = null;
		CollectionType collectionType = new CollectionType();
		CollectionType result = collectionType.withContentTypeHandler(h);
		assertNotNull(result);
	}

	@Test
	public void testWithValueHandler() {
		Object h = null;
		CollectionType collectionType = new CollectionType();
		CollectionType result = collectionType.withValueHandler(h);
		assertNotNull(result);
	}

	@Test
	public void testWithContentValueHandler() {
		Object h = null;
		CollectionType collectionType = new CollectionType();
		CollectionType result = collectionType.withContentValueHandler(h);
		assertNotNull(result);
	}

	@Test
	public void testWithStaticTyping() {
		CollectionType collectionType = new CollectionType();
		CollectionType result = collectionType.withStaticTyping();
		assertNotNull(result);
	}

	@Test
	public void testRefine() {
		Class<?> rawType = null;
		TypeBindings bindings = null;
		JavaType superClass = null;
		JavaType[] superInterfaces = null;
		JavaType elementType = null;
		Object valueHandler = null;
		Object typeHandler = null;
		boolean asStatic = false;
		CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInterfaces, elementType, valueHandler, typeHandler, asStatic);
		assertNotNull(collectionType.refine(rawType, bindings, superClass, superInterfaces));
	}

	@Test
	public void testToString() {
		CollectionType collectionType = new CollectionType();
		String result = collectionType.toString();
		assertNotNull(result);
	}
}