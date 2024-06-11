package com.example.junit;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class CollectionLikeTypeTest {

 private static final Class<CollectionLikeType> CLASS = CollectionLikeType.class;
 private static final JavaType ELEMENT_TYPE = JavaType.construct(int[].class);

 @Test
 public void testConstructorWithClassAndBindings() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, ELEMENT_TYPE);
 assertEquals(Collection.class, collectionLikeType.getRawClass());
 assertEquals(ELEMENT_TYPE, collectionLikeType.getElementType());
 }

 @Test
 public void testConstructorWithBaseAndElementType() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(TypeBase.class, ELEMENT_TYPE);
 assertEquals(TypeBase.class, collectionLikeType.getRawClass());
 assertEquals(ELEMENT_TYPE, collectionLikeType.getElementType());
 }

 @Test
 public void testUpgradeFrom() {
 JavaType baseType = JavaType.construct(ArrayList.class);
 JavaType elementType = JavaType.construct(int[].class);
 CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);
 assertEquals(ArrayList.class, collectionLikeType.getRawClass());
 assertEquals(elementType, collectionLikeType.getElementType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromInvalidBaseType() {
 JavaType baseType = JavaType.construct(String.class);
 JavaType elementType = JavaType.construct(int[].class);
 CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);
 fail("Should not be able to upgrade from a String type");
 }

 @Test
 public void testIsCollectionLikeType() {
 assertTrue(CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, ELEMENT_TYPE).isCollectionLikeType());
 }

 @Test
 public void testIsTrueCollectionType() {
 assertTrue(CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, ELEMENT_TYPE).isTrueCollectionType());
 }

 @Test
 public void testGetContentType() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, ELEMENT_TYPE);
 assertEquals(ELEMENT_TYPE, collectionLikeType.getContentType());
 }

 @Test
 public void testGetContentValueHandler() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, ELEMENT_Type);
 assertEquals(ELEMENT_TYPE.getValueHandler(), collectionLikeType.getContentValueHandler());
 }

 @Test
 public void testHasHandlers() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, ELEMENT_TYPE);
 assertTrue(collectionLikeType.hasHandlers());
 }

 @Test
 public void testToString() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, ELEMENT_TYPE);
 assertEquals("[collection-like type; class Collection, contains class [I]]", collectionLikeType.toString());
 }

 @Test
 public void testEquals() {
 CollectionLikeType collectionLikeType1 = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, ELEMENT_TYPE);
 CollectionLikeType collectionLikeType2 = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, ELEMENT_TYPE);
 CollectionLikeType collectionLikeType3 = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, ELEMENT_TYPE);

 assertTrue(collectionLikeType1.equals(collectionLikeType1));
 assertTrue(collectionLikeType1.equals(collectionLikeType2));
 assertFalse(collectionLikeType1.equals(collectionLikeType3));
 }

}