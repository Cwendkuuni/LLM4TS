import org.junit.Test;

public class CollectionTypeTest {
    
    	@Test
	public void testConstruct() {
		Class<?> rawType = Class.class;
		TypeBindings bindings = new TypeBindings();
		JavaType superClass = new JavaType();
		JavaType[] superInts = new JavaType[2];
		JavaType elemT = new JavaType();
		CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
		assertEquals(rawType, collectionType.getRawType());
		assertEquals(bindings, collectionType.getBindings());
		assertEquals(superClass, collectionType.getSuperClass());
		assertEquals(superInts, collectionType.getSuperInterfaces());
		assertEquals(elemT, collectionType.getElementType());
		assertNull(collectionType.getValueHandler());
		assertNull(collectionType.getTypeHandler());
		assertFalse(collectionType.isStatic());
	}
	
	@Test
	public void testWithTypeHandler() {
		Object h = new Object();
		CollectionType collectionType = new CollectionType();
		CollectionType newCollectionType = collectionType.withTypeHandler(h);
		assertEquals(collectionType.getClass(), newCollectionType.getClass());
		assertEquals(collectionType.getRawType(), newCollectionType.getRawType());
		assertEquals(collectionType.getBindings(), newCollectionType.getBindings());
		assertEquals(collectionType.getSuperClass(), newCollectionType.getSuperClass());
		assertEquals(collectionType.getSuperInterfaces(), newCollectionType.getSuperInterfaces());
		assertEquals(collectionType.getElementType(), newCollectionType.getElementType());
		assertEquals(collectionType.getValueHandler(), newCollectionType.getValueHandler());
		assertEquals(h, newCollectionType.getTypeHandler());
		assertEquals(collectionType.isStatic(), newCollectionType.isStatic());
	}
	
	@Test
	public void testWithContentTypeHandler() {
		Object h = new Object();
		CollectionType collectionType = new CollectionType();
		CollectionType newCollectionType = collectionType.withContentTypeHandler(h);
		assertEquals(collectionType.getClass(), newCollectionType.getClass());
		assertEquals(collectionType.getRawType(), newCollectionType.getRawType());
		assertEquals(collectionType.getBindings(), newCollectionType.getBindings());
		assertEquals(collectionType.getSuperClass(), newCollectionType.getSuperClass());
		assertEquals(collectionType.getSuperInterfaces(), newCollectionType.getSuperInterfaces());
		assertEquals(collectionType.getElementType().withTypeHandler(h), newCollectionType.getElementType());
		assertEquals(collectionType.getValueHandler(), newCollectionType.getValueHandler());
		assertEquals(collectionType.getTypeHandler(), newCollectionType.getTypeHandler());
		assertEquals(collectionType.isStatic(), newCollectionType.isStatic());
	}
	
	@Test
	public void testWithStaticTyping() {
		CollectionType collectionType = new CollectionType();
		CollectionType newCollectionType = collectionType.withStaticTyping();
		assertEquals(collectionType.getClass(), newCollectionType.getClass());
		assertEquals(collectionType.getRawType(), newCollectionType.getRawType());
		assertEquals(collectionType.getBindings(), newCollectionType.getBindings());
		assertEquals(collectionType.getSuperClass(), newCollectionType.getSuperClass());
		assertEquals(collectionType.getSuperInterfaces(), newCollectionType.getSuperInterfaces());
		assertEquals(collectionType.getElementType().withStaticTyping(), newCollectionType.getElementType());
		assertEquals(collectionType.getValueHandler(), newCollectionType.getValueHandler());
		assertEquals(collectionType.getTypeHandler(), newCollectionType.getTypeHandler());
		assertTrue(newCollectionType.isStatic());
	}
	
	@Test
	public void testRefine() {
		Class<?> rawType = Class.class;
		TypeBindings bindings = new TypeBindings();
		JavaType superClass = new JavaType();
		JavaType[] superInterfaces = new JavaType[2];
		JavaType elemT = new JavaType();
		CollectionType collectionType = new CollectionType();
		JavaType refinedType = collectionType.refine(rawType, bindings, superClass, superInterfaces);
		assertEquals(rawType, refinedType.getRawType());
		assertEquals(bindings, refinedType.getBindings());
		assertEquals(superClass, refinedType.getSuperClass());
		assertEquals(superInterfaces, refinedType.getSuperInterfaces());
		assertEquals(collectionType.getElementType(), refinedType.getElementType());
		assertEquals(collectionType.getValueHandler(), refinedType.getValueHandler());
		assertEquals(collectionType.getTypeHandler(), refinedType.getTypeHandler());
		assertEquals(collectionType.isStatic(), refinedType.isStatic());
	}

}