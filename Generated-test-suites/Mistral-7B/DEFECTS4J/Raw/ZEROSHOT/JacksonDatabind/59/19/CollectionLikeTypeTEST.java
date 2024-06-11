package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeBase;
import com.fasterxml.jackson.databind.TypeModifier;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeUtils;

public class CollectionLikeTypeTest {

	@Test
	public void testCollectionLikeTypeConstruction() {
		Class<Collection<String>> collectionClass = Collection.class.getTypeParameters()[0].getType();
		JavaType javaType = TypeUtils.constructType(collectionClass);
		CollectionLikeType collectionLikeType = CollectionLikeType.construct(collectionClass, TypeBindings.emptyBindings(), null, null, javaType);
		assertEquals(collectionClass, collectionLikeType.getRawType());
		assertEquals(JavaType.construct(TypeVariable.where(collectionClass, 0), javaType.getTypeParameters()[0].getType()), collectionLikeType.getType());
		assertEquals(javaType, collectionLikeType.getContentType());
		assertEquals(null, collectionLikeType.getSuperClass());
		assertEquals(null, collectionLikeType.getSuperInterfaces());
		assertEquals(false, collectionLikeType.isTrueCollectionType());
		assertEquals("java.util.Collection<java.lang.String>", collectionLikeType.buildCanonicalName());
	}

	@Test
	public void testCollectionLikeTypeUpgradeFrom() {
		Class<List<String>> listClass = java.util.List.class.getTypeParameters()[0].getType();
		JavaType javaType = TypeUtils.constructType(listClass);
		CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, TypeUtils.constructType(String.class));
		assertEquals(listClass, collectionLikeType.getRawType());
		assertEquals(JavaType.construct(TypeVariable.where(listClass, 0), javaType.getTypeParameters()[0].getType()), collectionLikeType.getType());
		assertEquals(javaType, collectionLikeType.getContentType());
		assertEquals(null, collectionLikeType.getSuperClass());
		assertEquals(null, collectionLikeType.getSuperInterfaces());
		assertEquals(false, collectionLikeType.isTrueCollectionType());
		assertEquals("java.util.List<java.lang.String>", collectionLikeType.buildCanonicalName());
	}

	@Test
	public void testCollectionLikeTypeWithContentType() {
		Class<Collection<String>> collectionClass = Collection.class.getTypeParameters()[0].getType();
		JavaType javaType = TypeUtils.constructType(collectionClass);
		CollectionLikeType collectionLikeType = new CollectionLikeType(javaType);
		JavaType javaTypeWithInt = TypeUtils.constructType(Integer.TYPE);
		CollectionLikeType collectionLikeTypeWithContentType = collectionLikeType.withContentType(javaTypeWithInt);
		assertEquals(javaType.getTypeParameters()[0].getType(), collectionLikeType.getContentType().getTypeParameters()[0].getType());
		assertEquals(javaTypeWithInt, collectionLikeTypeWithContentType.getContentType());
		assertEquals(collectionLikeType, collectionLikeTypeWithContentType.withContentType(javaType));
	}

	@Test
	public void testCollectionLikeTypeWithTypeHandler() {
		Class<Collection<String>> collectionClass = Collection.class.getTypeParameters()[0].getType();
		JavaType javaType = TypeUtils.constructType(collectionClass);
		CollectionLikeType collectionLikeType = new CollectionLikeType(javaType);
		Object handler = new Object() {};
		CollectionLikeType collectionLikeTypeWithTypeHandler = collectionLikeType.withTypeHandler(handler);
		assertEquals(handler, collectionLikeTypeWithTypeHandler.getTypeHandler());
		assertEquals(collectionLikeType, collectionLikeTypeWithTypeHandler.withTypeHandler(null));
	}

	@Test
	public void testCollectionLikeTypeWithContentValueHandler() {
		Class<Collection<String>> collectionClass = Collection.class.getTypeParameters()[0].getType();
		JavaType javaType = TypeUtils.constructType(collectionClass);
		CollectionLikeType collectionLikeType = new CollectionLikeType(javaType);
		Object handler = new Object() {};
		JavaType javaTypeWithInt = TypeUtils.constructType(Integer.TYPE);
		CollectionLikeType collectionLikeTypeWithContentValueHandler = collectionLikeType.withContentValueHandler(handler);
		assertEquals(handler, collectionLikeTypeWithContentValueHandler.getValueHandler());
		assertEquals(javaTypeWithInt, collectionLikeTypeWithContentValueHandler.getContentType());
		assertEquals(collectionLikeType, collectionLikeTypeWithContentValueHandler.withContentValueHandler(null));
	}

	@Test
	public void testCollectionLikeTypeIsContainerType() {
		Class<Collection<String>> collectionClass = Collection.class.getTypeParameters()[0].getType();
		JavaType javaType = TypeUtils.constructType(collectionClass);
		CollectionLikeType collectionLikeType = new CollectionLikeType(javaType);
		assertTrue(collectionLikeType.isContainerType());
		assertTrue(collectionLikeType.isCollectionLikeType());
	}

	@Test
	public void testCollectionLikeTypeRefine() {
		Class<Collection<String>> collectionClass = Collection.class.getTypeParameters()[0].getType();
		JavaType javaType = TypeUtils.constructType(collectionClass);
		CollectionLikeType collectionLikeType = new CollectionLikeType(javaType);
		Class<ArrayList<String>> arrayListClass = ArrayList.class.getTypeParameters()[0].getType();
		TypeBindings bindings = TypeBindings.emptyBindings();
		JavaType javaTypeWithStaticTyping = javaType.withStaticTyping();
		CollectionLikeType collectionLikeTypeRefined = collectionLikeType.refine(arrayListClass, bindings, null, null);
		assertEquals(collectionLikeType, collectionLikeTypeRefined);
		assertEquals(javaTypeWithStaticTyping, collectionLikeTypeRefined.getType());
		assertEquals(javaType, collectionLikeTypeRefined.getContentType());
		assertEquals(ArrayList.class, collectionLikeTypeRefined.getRawType());
		assertEquals(false, collectionLikeTypeRefined.isTrueCollectionType());
	}

	@Test
	public void testCollectionLikeTypeEquals() {
		Class<Collection<String>> collectionClass = Collection.class.getTypeParameters()[0].getType();
		JavaType javaType = TypeUtils.constructType(collectionClass);
		CollectionLikeType collectionLikeType1 = new CollectionLikeType(javaType);
		CollectionLikeType collectionLikeType2 = new CollectionLikeType(javaType);
		CollectionLikeType collectionLikeType3 = new CollectionLikeType(javaType);
		CollectionLikeType collectionLikeType4 = new CollectionLikeType(javaType);
		CollectionLikeType collectionLikeType5 = new CollectionLikeType(javaType, null);
		CollectionLikeType collectionLikeType6 = new CollectionLikeType(Integer.TYPE);
		CollectionLikeType collectionLikeType7 = new CollectionLikeType(javaType, null, null, null, null, null, null, true);
		assertEquals(collectionLikeType1, collectionLikeType1);
		assertEquals(collectionLikeType1, collectionLikeType2);
		assertEquals(collectionLikeType1, collectionLikeType3);
		assertEquals(collectionLikeType1, collectionLikeType4);
		assertNotEquals(collectionLikeType1, collectionLikeType5);
		assertNotEquals(collectionLikeType1, collectionLikeType6);
		assertNotEquals(collectionLikeType1, collectionLikeType7);
		assertNotEquals(collectionLikeType1, null);
		assertNotEquals(new Object() {}, collectionLikeType1);
	}

	@Test
	public void testCollectionLikeTypeToString() {
		Class<Collection<String>> collectionClass = Collection.class.getTypeParameters()[0].getType();
		JavaType javaType = TypeUtils.constructType(collectionClass);
		CollectionLikeType collectionLikeType = new CollectionLikeType(javaType);
		assertEquals("[collection-like type; class java.util.Collection<java.lang.String>, contains java.lang.Class<java.lang.String>]", collectionLikeType.toString());
	}
}