import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class CollectionLikeTypeTest {
	
	private CollectionLikeType collectionLikeType;
	
	@Before
	public void setUp() {
		collectionLikeType = CollectionLikeType.construct(Collection.class, JavaType.EMPTY);
	}

	@Test
	public void testIsTrueCollectionType() {
		assertTrue(collectionLikeType.isTrueCollectionType());
	}
	
	@Test
	public void testGetContentType() {
		JavaType contentType = collectionLikeType.getContentType();
		assertEquals(JavaType.EMPTY, contentType);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testUpgradeFromWithInvalidBaseType() {
		JavaType invalidBaseType = null;
		JavaType elementType = JavaType.EMPTY;
		CollectionLikeType.upgradeFrom(invalidBaseType, elementType);
	}
	
	@Test
	public void testWithContentTypeSameContentValueHandler() {
		JavaType contentType = collectionLikeType.getContentType();
		CollectionLikeType updatedType = collectionLikeType.withContentType(contentType);
		assertEquals(collectionLikeType, updatedType);
	}
	
	@Test
	public void testWithContentTypeDifferentContentValueHandler() {
		JavaType newContentType = JavaType.BOGUS;
		CollectionLikeType updatedType = collectionLikeType.withContentType(newContentType);
		assertNotEquals(collectionLikeType, updatedType);
		assertEquals(newContentType, updatedType.getContentType());
	}
	
	@Test
	public void testWithValueHandler() {
		Object valueHandler = new Object();
		CollectionLikeType updatedType = collectionLikeType.withValueHandler(valueHandler);
		assertEquals(valueHandler, updatedType.getValueHandler());
	}
	
	@Test
	public void testWithContentValueHandler() {
		Object contentValueHandler = new Object();
		CollectionLikeType updatedType = collectionLikeType.withContentValueHandler(contentValueHandler);
		assertEquals(contentValueHandler, updatedType.getContentValueHandler());
	}
	
	@Test
	public void testWithStaticTyping() {
		CollectionLikeType updatedType = collectionLikeType.withStaticTyping();
		assertTrue(updatedType.isStaticTyping());
	}
	
	@Test
	public void testRefine() {
		Class<?> rawType = Object.class;
		JavaType superClass = JavaType.BOGUS;
		JavaType[] superInterfaces = new JavaType[] { JavaType.EMPTY };
		JavaType elementType = JavaType.BOGUS;
		CollectionLikeType refinedType = collectionLikeType.refine(rawType, superClass, superInterfaces, elementType);
		assertEquals(rawType, refinedType.getRawClass());
		assertEquals(superClass, refinedType.getSuperClass());
		assertArrayEquals(superInterfaces, refinedType.getInterfaces());
		assertEquals(elementType, refinedType.getContentType());
	}
	
	@Test
	public void testIsContainerType() {
		assertTrue(collectionLikeType.isContainerType());
	}
	
	@Test
	public void testIsCollectionLikeType() {
		assertTrue(collectionLikeType.isCollectionLikeType());
	}
	
	@Test
	public void testGetContentValueHandler() {
		Object contentValueHandler = collectionLikeType.getContentValueHandler();
		assertNull(contentValueHandler);
	}
	
	@Test
	public void testGetContentTypeHandler() {
		Object contentTypeHandler = collectionLikeType.getContentTypeHandler();
		assertNull(contentTypeHandler);
	}
	
	@Test
	public void testHasHandlersNoHandlers() {
		assertFalse(collectionLikeType.hasHandlers());
	}
	
	@Test
	public void testHasHandlersWithValueHandler() {
		Object valueHandler = new Object();
		CollectionLikeType typeWithHandlers = collectionLikeType.withValueHandler(valueHandler);
		assertTrue(typeWithHandlers.hasHandlers());
	}
	
	@Test
	public void testEqualsSameObject() {
		assertTrue(collectionLikeType.equals(collectionLikeType));
	}
	
	@Test
	public void testEqualsSameClassAndElementType() {
		CollectionLikeType equalType = CollectionLikeType.construct(Collection.class, JavaType.EMPTY);
		assertTrue(collectionLikeType.equals(equalType));
	}
	
	@Test
	public void testEqualsDifferentClass() {
		CollectionLikeType differentClassType = CollectionLikeType.construct(List.class, JavaType.STRING);
		assertFalse(collectionLikeType.equals(differentClassType));
	}
	
	@Test
	public void testEqualsDifferentElementType() {
		CollectionLikeType differentElementType = CollectionLikeType.construct(Collection.class, JavaType.STRING);
		assertFalse(collectionLikeType.equals(differentElementType));
	}
	
	@Test
	public void testToString() {
		String expectedString = "[collection-like type; class java.util.Collection, contains EMPTY]";
		assertEquals(expectedString, collectionLikeType.toString());
	}

}