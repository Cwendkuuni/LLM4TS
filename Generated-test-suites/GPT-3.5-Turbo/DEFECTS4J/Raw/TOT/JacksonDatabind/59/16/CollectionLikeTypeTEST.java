package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class CollectionLikeTypeTest {
	
	@Test
	public void testConstruct() {
		JavaType elemT = JavaTypeFactory.constructType(String.class);
		CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, elemT);
	}
	
	@Test
	public void testUpgradeFrom() {
		JavaType baseType = JavaTypeFactory.constructType(String.class);
		JavaType elementType = JavaTypeFactory.constructType(Integer.class);
		CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);
	}
	
	@Test
	public void testNarrow() {
		JavaType elemT = JavaTypeFactory.constructType(String.class);
		CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, elemT);
		JavaType narrowedType = collectionLikeType._narrow(ArrayList.class);
	}
	
	@Test
	public void testWithContentType() {
		JavaType elemT = JavaTypeFactory.constructType(String.class);
		CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, elemT);
		JavaType contentType = JavaTypeFactory.constructType(Integer.class);
		JavaType withContentTypeType = collectionLikeType.withContentType(contentType);
	}
	
	@Test
	public void testWithTypeHandler() {
		JavaType elemT = JavaTypeFactory.constructType(String.class);
		CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, elemT);
		CollectionLikeType withTypeHandlerType = collectionLikeType.withTypeHandler("typeHandler");
	}
	
	@Test
	public void testWithContentTypeHandler() {
		JavaType elemT = JavaTypeFactory.constructType(String.class);
		CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, elemT);
		CollectionLikeType withContentTypeHandlerType = collectionLikeType.withContentTypeHandler("typeHandler");
	}
	
	@Test
	public void testWithValueHandler() {
		JavaType elemT = JavaTypeFactory.constructType(String.class);
		CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, elemT);
		CollectionLikeType withValueHandlerType = collectionLikeType.withValueHandler("valueHandler");
	}
	
	@Test
	public void testWithContentValueHandler() {
		JavaType elemT = JavaTypeFactory.constructType(String.class);
		CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, elemT);
		CollectionLikeType withContentValueHandlerType = collectionLikeType.withContentValueHandler("valueHandler");
	}
	
	@Test
	public void testWithStaticTyping() {
		JavaType elemT = JavaTypeFactory.constructType(String.class);
		CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, elemT);
		CollectionLikeType withStaticTypingType = collectionLikeType.withStaticTyping();
	}
	
	@Test
	public void testRefine() {
		JavaType elemT = JavaTypeFactory.constructType(String.class);
		CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, elemT);
		JavaType superclass = JavaTypeFactory.constructType(Integer.class);
		JavaType[] superinterfaces = {JavaTypeFactory.constructType(Double.class)};
		JavaType refinedType = collectionLikeType.refine(List.class, TypeBindings, superclass, superinterfaces);
	}
	
	@Test
	public void testIsContainerType() {
		JavaType elemT = JavaTypeFactory.constructType(String.class);
		CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, elemT);
		boolean isContainerType = collectionLikeType.isContainerType();
		assertTrue(isContainerType);
	}
	
	@Test
	public void testIsCollectionLikeType() {
		JavaType elemT = JavaTypeFactory.constructType(String.class);
		CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, elemT);
		boolean isCollectionLikeType = collectionLikeType.isCollectionLikeType();
		assertTrue(isCollectionLikeType);
	}
	
	@Test
	public void testGetContentType() {
		JavaType elemT = JavaTypeFactory.constructType(String.class);
		CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, elemT);
		JavaType contentType = collectionLikeType.getContentType();
		assertEquals(elemT, contentType);
	}
	
	@Test
	public void testGetContentValueHandler() {
		JavaType elemT = JavaTypeFactory.constructType(String.class);
		Object valueHandler = "valueHandler";
		elemT.setValueHandler(valueHandler);
		
		CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, elemT);
		Object contentValueHandler = collectionLikeType.getContentValueHandler();
		assertEquals(valueHandler, contentValueHandler);
	}
	
	@Test
	public void testGetContentTypeHandler() {
		JavaType elemT = JavaTypeFactory.constructType(String.class);
		Object typeHandler = "typeHandler";
		elemT.setTypeHandler(typeHandler);
		
		CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, elemT);
		Object contentTypeHandler = collectionLikeType.getContentTypeHandler();
		assertEquals(typeHandler, contentTypeHandler);
	}
	
	@Test
	public void testHasHandlers() {
		JavaType elemT = JavaTypeFactory.constructType(String.class);
		Object valueHandler = "valueHandler";
		Object typeHandler = "typeHandler";
		elemT.setValueHandler(valueHandler);
		elemT.setTypeHandler(typeHandler);
		
		CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, elemT);
		boolean hasHandlers = collectionLikeType.hasHandlers();
		assertTrue(hasHandlers);
	}
	
	@Test
	public void testGetErasedSignature() {
		JavaType elemT = JavaTypeFactory.constructType(String.class);
		CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, elemT);
		String erasedSignature = collectionLikeType.getErasedSignature(new StringBuilder()).toString();
	}
	
	@Test
	public void testGetGenericSignature() {
		JavaType elemT = JavaTypeFactory.constructType(String.class);
		CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, elemT);
		String genericSignature = collectionLikeType.getGenericSignature(new StringBuilder()).toString();
	}
	
	@Test
	public void testBuildCanonicalName() {
		JavaType elemT = JavaTypeFactory.constructType(String.class);
		CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, elemT);
		String canonicalName = collectionLikeType.buildCanonicalName();
	}
	
	@Test
	public void testIsTrueCollectionType() {
		JavaType elemT = JavaTypeFactory.constructType(String.class);
		CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, elemT);
		boolean isTrueCollectionType = collectionLikeType.isTrueCollectionType();
		assertTrue(isTrueCollectionType);
	}
	
	@Test
	public void testEquals() {
		JavaType elemT1 = JavaTypeFactory.constructType(String.class);
		JavaType elemT2 = JavaTypeFactory.constructType(Integer.class);
		CollectionLikeType collectionLikeType1 = CollectionLikeType.construct(Collection.class, elemT1);
		CollectionLikeType collectionLikeType2 = CollectionLikeType.construct(List.class, elemT2);
		
		boolean equals = collectionLikeType1.equals(collectionLikeType2);
		assertFalse(equals);
	}
	
	@Test
	public void testToString() {
		JavaType elemT = JavaTypeFactory.constructType(String.class);
		CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, elemT);
		String string = collectionLikeType.toString();
		assertEquals("[collection-like type; class "+Collection.class.getName()+", contains "+elemT+"]", string);
	}
}