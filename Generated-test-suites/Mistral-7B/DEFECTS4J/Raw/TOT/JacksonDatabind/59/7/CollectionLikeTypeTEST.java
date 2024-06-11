package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

 @Test
 public void testConstruct() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(java.util.ArrayList.class, TypeBindings.defaultBindings(), null, null, JavaType.constructRawListType(String.class));
 assertEquals("java.util.ArrayList<java.lang.String>", collectionLikeType.toString());
 }

 @Test
 public void testUpgradeFrom() {
 JavaType listType = JavaType.constructRawListType(String.class);
 JavaType collectionLikeType = CollectionLikeType.upgradeFrom(listType, String.class);
 assertEquals("com.fasterxml.jackson.databind.type.CollectionLikeType<java.lang.String>", collectionLikeType.toString());
 }

 @Test
 public void testIsContainerTypeAndIsCollectionLikeType() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, TypeBindings.defaultBindings(), null, null, JavaType.constructRawListType(String.class), null, null, false);
 assertTrue(collectionLikeType.isContainerType());
 assertTrue(collectionLikeType.isCollectionLikeType());
 }

 @Test
 public void testGetContentType() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, TypeBindings.defaultBindings(), null, null, JavaType.constructRawListType(String.class), null, null, false);
 assertEquals(JavaType.constructRawType(String.class), collectionLikeType.getContentType());
 }

 @Test
 public void testIsTrueCollectionType() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, TypeBindings.defaultBindings(), null, null, JavaType.constructRawListType(String.class), null, null, false);
 assertTrue(collectionLikeType.isTrueCollectionType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromWrongBaseType() {
 JavaType wrongBaseType = JavaType.constructRawType(int[].class);
 JavaType collectionLikeType = CollectionLikeType.upgradeFrom(wrongBaseType, String.class);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testToString() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, TypeBindings.defaultBindings(), null, null, JavaType.constructRawListType(String.class), null, null, false);
 assertEquals("[collection-like type; class java.util.ArrayList<java.lang.String>, contains java.lang.String]", collectionLikeType.toString());
 }
}