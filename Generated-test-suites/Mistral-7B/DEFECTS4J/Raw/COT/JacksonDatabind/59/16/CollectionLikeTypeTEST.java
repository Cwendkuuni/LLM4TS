package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.TypeHandler;
import com.fasterxml.jackson.databind.type.TypeBase;

public class CollectionLikeTypeTest {

 // Basic test cases for constructors
 @Test
 public void testConstructors() {
 CollectionLikeType type1 = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, null);
 CollectionLikeType type2 = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, null);
 CollectionLikeType type3 = CollectionLikeType.construct(LinkedList.class, TypeBindings.emptyBindings(), null, null, null);

 assertEquals("java.util.Collection", type1.getClassName());
 assertEquals("java.util.ArrayList", type2.getClassName());
 assertEquals("java.util.LinkedList", type3.getClassName());
 }

 // Test for upgradeFrom method
 @Test
 public void testUpgradeFrom() {
 JavaType javaListType = new TypeFactory().constructType(ArrayList.class, TypeBindings.emptyBindings(), null, null);
 JavaType javaCollectionType = CollectionLikeType.upgradeFrom(javaListType, String.class);

 assertEquals(javaCollectionType.getClass(), CollectionLikeType.class);
 assertEquals(String.class, javaCollectionType.getContentType().getRawClass());
 }

 // Test for getClass, getSuperClass, getSuperInterfaces methods
 @Test
 public void testGetClassSuperClassSuperInterfaces() {
 JavaType listType = new TypeFactory().constructType(ArrayList.class, TypeBindings.emptyBindings(), null, null);
 JavaType collectionType = CollectionLikeType.construct(listType.getRawClass(), TypeBindings.emptyBindings(), null, null, null);

 assertEquals(ArrayList.class, collectionType.getRawClass());
 assertEquals(Collection.class, collectionType.getSuperClass());
 assertArrayEquals(new Class[]{}, collectionType.getSuperInterfaces());
 }

 // Test for isContainerType and isCollectionLikeType methods
 @Test
 public void testIsContainerTypeIsCollectionLikeType() {
 JavaType listType = new TypeFactory().constructType(ArrayList.class, TypeBindings.emptyBindings(), null, null);
 JavaType collectionType = CollectionLikeType.construct(listType.getRawClass(), TypeBindings.emptyBindings(), null, null, null);

 assertTrue(collectionType.isContainerType());
 assertTrue(collectionType.isCollectionLikeType());
 }

 // Test for getContentType, getContentValueHandler, getContentTypeHandler methods
 @Test
 public void testGetContentTypeContentValueHandlerContentTypeHandler() {
 JavaType listType = new TypeFactory().constructType(ArrayList.class, TypeBindings.emptyBindings(), null, null);
 JavaType collectionType = CollectionLikeType.construct(listType.getRawClass(), TypeBindings.emptyBindings(), null, null, null);
 JavaType elementType = new TypeFactory().constructType(String.class, TypeBindings.emptyBindings(), null, null);

 assertEquals(elementType, collectionType.getContentType());
 assertNull(collectionType.getContentValueHandler());
 assertNull(collectionType.getContentTypeHandler());
 }

 // Test for hasHandlers method
 @Test
 public void testHasHandlers() {
 JavaType listType = new TypeFactory().constructType(ArrayList.class, TypeBindings.emptyBindings(), null, null);
 JavaType collectionType = CollectionLikeType.construct(listType.getRawClass(), TypeBindings.emptyBindings(), null, null, null);
 JavaType elementType = new TypeFactory().constructType(String.class, TypeBindings.emptyBindings(), null, null);
 TypeHandler valueHandler = new StringValueHandler();
 TypeHandler typeHandler = new StringTypeDeserializer();

 collectionType = collectionType.withContentTypeHandler(typeHandler).withValueHandler(valueHandler);

 assertTrue(collectionType.hasHandlers());
 }

 // Test for getErasedSignature, getGenericSignature, buildCanonicalName methods
 @Test
 public void testGetErasedSignatureGenericSignatureCanonicalName() {
 JavaType listType = new TypeFactory().constructType(ArrayList.class, TypeBindings.emptyBindings(), null, null);
 JavaType collectionType = CollectionLikeType.construct(listType.getRawClass(), TypeBindings.emptyBindings(), null, null, null);
 StringBuilder sb = new StringBuilder();

 assertEquals("[collection-like type; class java.util.ArrayList, contains java.lang.String]", collectionType.getErasedSignature(sb).toString());
 assertEquals("java.util.Collection<java.lang.String>", collectionType.getGenericSignature(sb).toString());
 assertEquals("java.util.Collection<java.lang.String>", collectionType.buildCanonicalName());
 }

 // Test for isTrueCollectionType method
 @Test
 public void testIsTrueCollectionType() {
 JavaType listType = new TypeFactory().constructType(ArrayList.class, TypeBindings.emptyBindings(), null, null);
 JavaType collectionType = CollectionLikeType.construct(listType.getRawClass(), TypeBindings.emptyBindings(), null, null, null);

 assertTrue(collectionType.isTrueCollectionType());
 }

 // Test for equals method
 @Test
 public void testEquals() {
 JavaType listType1 = new TypeFactory().constructType(ArrayList.class, TypeBindings.emptyBindings(), null, null);
 JavaType listType2 = new TypeFactory().constructType(ArrayList.class, TypeBindings.emptyBindings(), null, null);
 JavaType collectionType1 = CollectionLikeType.construct(listType1.getRawClass(), TypeBindings.emptyBindings(), null, null, null);
 JavaType collectionType2 = CollectionLikeType.construct(listType2.getRawClass(), TypeBindings.emptyBindings(), null, null, null);

 assertEquals(collectionType1, collectionType1);
 assertEquals(collectionType1, collectionType2);
 assertNotEquals(collectionType1, new Object());
 }

 // Test for toString method
 @Test
 public void testToString() {
 JavaType listType = new TypeFactory().constructType(ArrayList.class, TypeBindings.emptyBindings(), null, null);
 JavaType collectionType = CollectionLikeType.construct(listType.getRawClass(), TypeBindings.emptyBindings(), null, null, null);

 assertEquals("[collection-like type; class java.util.ArrayList, contains java.lang.Object]", collectionType.toString());
 }

 // Edge cases and exception handling tests
 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromInvalidBaseType() {
 JavaType intType = new TypeFactory().constructType(int.class, TypeBindings.emptyBindings(), null, null);
 JavaType collectionType = CollectionLikeType.upgradeFrom(intType, String.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithInvalidRawType() {
 try {
 CollectionLikeType.construct(Object.class, TypeBindings.emptyBindings(), null, null, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithNullElemType() {
 try {
 CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithNullBindings() {
 try {
 CollectionLikeType.construct(Collection.class, null, null, null, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithNullSuperClass() {
 try {
 CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithNullSuperInterfaces() {
 try {
 CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, null, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithNullElemTValueHandler() {
 try {
 CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, true);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithNullTypeHandler() {
 try {
 CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, new StringTypeDeserializer(), true);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithNullContentTypeHandler() {
 try {
 CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, true).withContentTypeHandler(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithNullValueHandler() {
 try {
 CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, true).withValueHandler(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithNullContentValueHandler() {
 try {
 CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, true).withContentValueHandler(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithInvalidContentType() {
 try {
 CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, new TypeFactory().constructType(Object.class, TypeBindings.emptyBindings(), null, null), null, null, true);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}