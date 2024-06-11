package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeBase;
import com.fasterxml.jackson.databind.type.CollectionLikeType;

public class CollectionLikeTypeTest {

 // List of all the public methods with their signatures

 @Test
 public void testConstruct() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(),
         _bogusSuperClass(Collection.class), null, JavaType.constructRawListType(Integer.TYPE));
 assertEquals(Collection.class, collectionLikeType.getClass());
 assertEquals(JavaType.constructRawListType(Integer.TYPE), collectionLikeType.getContentType());
 }

 @Test
 public void testConstructWithBindings() {
 TypeVariable<?>[] vars = Collection.class.getTypeParameters();
 TypeBindings bindings;
 if ((vars == null) || (vars.length != 1)) {
 bindings = TypeBindings.emptyBindings();
 } else {
 bindings = TypeBindings.create(Collection.class, JavaType.constructRawType(ArrayList.class));
 }
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, bindings,
         _bogusSuperClass(Collection.class), null, JavaType.constructRawListType(Integer.TYPE));
 assertEquals(Collection.class, collectionLikeType.getClass());
 assertEquals(JavaType.constructRawListType(Integer.TYPE), collectionLikeType.getContentType());
 }

 @Test
 public void testUpgradeFrom() {
 JavaType baseType = JavaType.constructRawType(ArrayList.class);
 JavaType elementType = JavaType.constructRawType(Integer.TYPE);
 CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);
 assertEquals(CollectionLikeType.class, collectionLikeType.getClass());
 assertEquals(baseType.getClass(), collectionLikeType.getRawClass());
 assertEquals(elementType, collectionLikeType.getContentType());
 }

 @Test
 public void testIsContainerType() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(),
         _bogusSuperClass(Collection.class), null, JavaType.constructRawListType(Integer.TYPE), null, null, false);
 assertTrue(collectionLikeType.isContainerType());
 }

 @Test
 public void testIsCollectionLikeType() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(),
         _bogusSuperClass(Collection.class), null, JavaType.constructRawListType(Integer.TYPE), null, null, false);
 assertTrue(collectionLikeType.isCollectionLikeType());
 }

 @Test
 public void testGetContentType() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(),
         _bogusSuperClass(Collection.class), null, JavaType.constructRawListType(Integer.TYPE), null, null, false);
 assertEquals(JavaType.constructRawListType(Integer.TYPE), collectionLikeType.getContentType());
 }

 @Test
 public void testGetContentValueHandler() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(),
         _bogusSuperClass(Collection.class), null, JavaType.constructRawListType(Integer.TYPE), null, null, false);
 assertEquals(null, collectionLikeType.getContentValueHandler());
 }

 @Test
 public void testHasHandlers() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(),
         _bogusSuperClass(Collection.class), null, JavaType.constructRawListType(Integer.TYPE), null, null, false);
 assertFalse(collectionLikeType.hasHandlers());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithInvalidBaseType() {
 JavaType elemT = JavaType.constructRawType(ArrayList.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(String.class, elemT);
 fail("Should throw IllegalArgumentException");
 }

 // Edge cases and exception handling

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithNullBindings() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, null,
         _bogusSuperClass(Collection.class), null, JavaType.constructRawListType(Integer.TYPE));
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithNullSuperClass() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(),
         null, null, JavaType.constructRawListType(Integer.TYPE));
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithNullElementType() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(),
         _bogusSuperClass(Collection.class), null, null);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testConstructWithInvalidElementType() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(),
         _bogusSuperClass(Collection.class), null, JavaType.constructRawType(Object.class));
 assertFalse(collectionLikeType.isTrueCollectionType());
 }

 @Test
 public void testUpgradeFromWithInvalidBaseType() {
 JavaType baseType = JavaType.constructRawType(String.class);
 JavaType elementType = JavaType.constructRawType(Integer.TYPE);
 try {
 CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testIsTrueCollectionType() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(),
         _bogusSuperClass(Collection.class), null, JavaType.constructRawListType(Integer.TYPE), null, null, false);
 assertTrue(collectionLikeType.isTrueCollectionType());
 }

 @Test
 public void testIsTrueCollectionTypeWithNonCollectionType() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(String.class, TypeBindings.emptyBindings(),
         _bogusSuperClass(Collection.class), null, JavaType.constructRawListType(Integer.TYPE), null, null, false);
 assertFalse(collectionLikeType.isTrueCollectionType());
 }

}