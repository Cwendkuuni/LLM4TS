import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

	@Test
	public void testConstruct() {
		SimpleType simpleType = SimpleType.construct(Object.class);
		assertNotNull(simpleType);
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
		SimpleType.construct(Array.class);
	}

	@Test
	public void testConstructUnsafe() {
		SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
		assertNotNull(simpleType);
	}

	@Test
	public void testNarrow() {
		SimpleType simpleType = new SimpleType(Object.class);
		JavaType narrowedType = simpleType._narrow(String.class);
		assertNotNull(narrowedType);
	}

	@Test
	public void testWithContentType() {
		SimpleType simpleType = new SimpleType(Object.class);
		JavaType contentType = new SimpleType(String.class);
		assertThrows(IllegalArgumentException.class, () -> simpleType.withContentType(contentType));
	}

	@Test
	public void testWithTypeHandler() {
		SimpleType simpleType = new SimpleType(Object.class);
		Object typeHandler = new Object();
		SimpleType newSimpleType = simpleType.withTypeHandler(typeHandler);
		assertNotEquals(simpleType, newSimpleType);
		assertEquals(typeHandler, newSimpleType._typeHandler);
	}

	@Test
	public void testWithContentTypeHandler() {
		SimpleType simpleType = new SimpleType(Object.class);
		Object contentTypeHandler = new Object();
		assertThrows(IllegalArgumentException.class, () -> simpleType.withContentTypeHandler(contentTypeHandler));
	}

	@Test
	public void testWithValueHandler() {
		SimpleType simpleType = new SimpleType(Object.class);
		Object valueHandler = new Object();
		SimpleType newSimpleType = simpleType.withValueHandler(valueHandler);
		assertNotEquals(simpleType, newSimpleType);
		assertEquals(valueHandler, newSimpleType._valueHandler);
	}

	@Test
	public void testWithContentValueHandler() {
		SimpleType simpleType = new SimpleType(Object.class);
		Object contentValueHandler = new Object();
		assertThrows(IllegalArgumentException.class, () -> simpleType.withContentValueHandler(contentValueHandler));
	}

	@Test
	public void testWithStaticTyping() {
		SimpleType simpleType = new SimpleType(Object.class);
		SimpleType newSimpleType = simpleType.withStaticTyping();
		assertTrue(newSimpleType._asStatic);
	}

	@Test
	public void testRefine() {
		SimpleType simpleType = new SimpleType(Object.class);
		JavaType refinedType = simpleType.refine(String.class, null, null, null);
		assertNull(refinedType);
	}

	@Test
	public void testBuildCanonicalName() {
		SimpleType simpleType = new SimpleType(Object.class);
		String canonicalName = simpleType.buildCanonicalName();
		assertEquals("java.lang.Object", canonicalName);
	}

	@Test
	public void testIsContainerType() {
		SimpleType simpleType = new SimpleType(Object.class);
		assertFalse(simpleType.isContainerType());
	}

	@Test
	public void testGetErasedSignature() {
		SimpleType simpleType = new SimpleType(Object.class);
		StringBuilder signature = simpleType.getErasedSignature(new StringBuilder());
		assertEquals("Ljava/lang/Object;", signature.toString());
	}

	@Test
	public void testGetGenericSignature() {
		SimpleType simpleType = new SimpleType(Object.class);
		StringBuilder signature = simpleType.getGenericSignature(new StringBuilder());
		assertEquals("Ljava/lang/Object;", signature.toString());
	}

	@Test
	public void testToString() {
		SimpleType simpleType = new SimpleType(Object.class);
		String string = simpleType.toString();
		assertEquals("[simple type, class java.lang.Object]", string);
	}

	@Test
	public void testEquals() {
		SimpleType simpleType1 = new SimpleType(Object.class);
		SimpleType simpleType2 = new SimpleType(Object.class);
		assertTrue(simpleType1.equals(simpleType2));
	}

}