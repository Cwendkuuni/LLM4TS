import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;

public class CollectionLikeTypeTest {

	@Test
	public void testConstruct() {
		JavaType elemT = new JavaType(String.class);
		CollectionLikeType collectionType = CollectionLikeType.construct(Collection.class, elemT);
		assertEquals(Collection.class, collectionType.getRawClass());
		assertEquals(elemT, collectionType.getElementType());
		assertFalse(collectionType.isTrueCollectionType());
		assertNull(collectionType.getValueHandler());
		assertNull(collectionType.getTypeHandler());
		assertFalse(collectionType.isStaticTyping());
	}

	@Test
	public void testUpgradeFrom() {
		JavaType baseType = new JavaType(List.class);
		JavaType elemT = new JavaType(Integer.class);
		CollectionLikeType collectionType = CollectionLikeType.upgradeFrom(baseType, elemT);
		assertEquals(List.class, collectionType.getRawClass());
		assertEquals(elemT, collectionType.getElementType());
		assertFalse(collectionType.isTrueCollectionType());
		assertNull(collectionType.getValueHandler());
		assertNull(collectionType.getTypeHandler());
		assertFalse(collectionType.isStaticTyping());
	}

	@Test
	public void testWithContentType() {
		JavaType elemT = new JavaType(String.class);
		CollectionLikeType collectionType = CollectionLikeType.construct(Collection.class, elemT);
		JavaType newElemT = new JavaType(Integer.class);
		CollectionLikeType newCollectionType = collectionType.withContentType(newElemT);
		assertEquals(Collection.class, newCollectionType.getRawClass());
		assertEquals(newElemT, newCollectionType.getElementType());
		assertFalse(newCollectionType.isTrueCollectionType());
		assertNull(newCollectionType.getValueHandler());
		assertNull(newCollectionType.getTypeHandler());
		assertFalse(newCollectionType.isStaticTyping());
	}

	@Test
	public void testWithTypeHandler() {
		JavaType elemT = new JavaType(String.class);
		CollectionLikeType collectionType = CollectionLikeType.construct(Collection.class, elemT);
		Object typeHandler = new Object();
		CollectionLikeType newCollectionType = collectionType.withTypeHandler(typeHandler);
		assertEquals(Collection.class, newCollectionType.getRawClass());
		assertEquals(elemT, newCollectionType.getElementType());
		assertFalse(newCollectionType.isTrueCollectionType());
		assertNull(newCollectionType.getValueHandler());
		assertEquals(typeHandler, newCollectionType.getTypeHandler());
		assertFalse(newCollectionType.isStaticTyping());
	}

	@Test
	public void testWithContentTypeHandler() {
		JavaType elemT = new JavaType(String.class);
		CollectionLikeType collectionType = CollectionLikeType.construct(Collection.class, elemT);
		Object typeHandler = new Object();
		CollectionLikeType newCollectionType = collectionType.withContentTypeHandler(typeHandler);
		assertEquals(Collection.class, newCollectionType.getRawClass());
		assertEquals(elemT, newCollectionType.getElementType());
		assertFalse(newCollectionType.isTrueCollectionType());
		assertNull(newCollectionType.getValueHandler());
		assertNull(newCollectionType.getTypeHandler());
		assertFalse(newCollectionType.isStaticTyping());
		assertEquals(typeHandler, newCollectionType.getElementType().getTypeHandler());
	}

	@Test
	public void testWithStaticTyping() {
		JavaType elemT = new JavaType(Integer.class);
		CollectionLikeType collectionType = CollectionLikeType.construct(Collection.class, elemT);
		CollectionLikeType newCollectionType = collectionType.withStaticTyping();
		assertEquals(Collection.class, newCollectionType.getRawClass());
		assertEquals(elemT, newCollectionType.getElementType());
		assertFalse(newCollectionType.isTrueCollectionType());
		assertNull(newCollectionType.getValueHandler());
		assertNull(newCollectionType.getTypeHandler());
		assertTrue(newCollectionType.isStaticTyping());
	}

	@Test
	public void testRefine() {
		JavaType elemT = new JavaType(String.class);
		CollectionLikeType collectionType = CollectionLikeType.construct(Collection.class, elemT);
		JavaType superClass = new JavaType(Object.class);
		JavaType[] superInterfaces = new JavaType[2];
		CollectionLikeType newCollectionType = collectionType.refine(List.class, TypeBindings.emptyBindings(), superClass, superInterfaces);
		assertEquals(List.class, newCollectionType.getRawClass());
		assertEquals(elemT, newCollectionType.getElementType());
		assertFalse(newCollectionType.isTrueCollectionType());
		assertNull(newCollectionType.getValueHandler());
		assertNull(newCollectionType.getTypeHandler());
		assertFalse(newCollectionType.isStaticTyping());
	}
  
  @Test
	public void testIsTrueCollectionType() {
		JavaType elemT = new JavaType(String.class);
		CollectionLikeType collectionType = CollectionLikeType.construct(Collection.class, elemT);
		assertTrue(collectionType.isTrueCollectionType());
	}

	@Test
	public void testEquals() {
		JavaType elemT1 = new JavaType(String.class);
		JavaType elemT2 = new JavaType(Integer.class);
		CollectionLikeType collectionType1 = CollectionLikeType.construct(Collection.class, elemT1);
		CollectionLikeType collectionType2 = CollectionLikeType.construct(Collection.class, elemT1);
		CollectionLikeType collectionType3 = CollectionLikeType.construct(Set.class, elemT1);
		CollectionLikeType collectionType4 = CollectionLikeType.construct(Collection.class, elemT2);
		assertTrue(collectionType1.equals(collectionType2));
		assertFalse(collectionType1.equals(collectionType3));
		assertFalse(collectionType1.equals(collectionType4));
	}

	@Test
	public void testToString() {
		JavaType elemT = new JavaType(String.class);
		CollectionLikeType collectionType = CollectionLikeType.construct(Collection.class, elemT);
		assertEquals("[collection-like type; class java.util.Collection, contains " + elemT + "]", collectionType.toString());
	}

}