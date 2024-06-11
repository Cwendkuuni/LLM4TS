package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

 @Test
 public void testCollectionLikeTypeConstruction() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE);
 assertEquals(java.util.ArrayList.class, collectionLikeType.getRawClass());
 assertEquals(JavaType.construct(Collection.class, JavaType.of(Integer.class)), collectionLikeType.getRawType());
 assertEquals(JavaType.construct(Collection.class, JavaType.of(Integer.class)), collectionLikeType.getType());
 assertEquals(JavaType.of(Integer.TYPE), collectionLikeType.getContentType());
 assertNull(collectionLikeType.getValueHandler());
 assertNull(collectionLikeType.getTypeHandler());
 assertFalse(collectionLikeType.isTrueCollectionType());

 CollectionLikeType collectionLikeTypeList = CollectionLikeType.construct(java.util.List.class, null, null, null, java.lang.Integer.TYPE);
 assertEquals(java.util.List.class, collectionLikeTypeList.getRawClass());
 assertEquals(JavaType.construct(Collection.class, JavaType.of(Integer.class)), collectionLikeTypeList.getRawType());
 assertEquals(JavaType.construct(List.class, JavaType.of(Integer.class)), collectionLikeTypeList.getType());
 assertEquals(JavaType.of(Integer.TYPE), collectionLikeTypeList.getContentType());
 assertNull(collectionLikeTypeList.getValueHandler());
 assertNull(collectionLikeTypeList.getTypeHandler());
 assertTrue(collectionLikeTypeList.isTrueCollectionType());
 }

 @Test
 public void testCollectionLikeTypeUpgradeFrom() {
 JavaType javaType = JavaType.construct(java.util.ArrayList.class, null);
 CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, JavaType.of(Integer.TYPE));
 assertEquals(java.util.ArrayList.class, collectionLikeType.getRawClass());
 assertEquals(JavaType.construct(Collection.class, JavaType.of(Integer.class)), collectionLikeType.getRawType());
 assertEquals(JavaType.construct(ArrayList.class, JavaType.of(Integer.class)), collectionLikeType.getType());
 assertEquals(JavaType.of(Integer.TYPE), collectionLikeType.getContentType());
 assertNull(collectionLikeType.getValueHandler());
 assertNull(collectionLikeType.getTypeHandler());
 assertFalse(collectionLikeType.isTrueCollectionType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCollectionLikeTypeUpgradeFromInvalidBaseType() {
 JavaType javaType = JavaType.construct(java.lang.String.class, null);
 CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, JavaType.of(Integer.TYPE));
 fail("Should not reach this point");
 }

 @Test
 public void testCollectionLikeTypeRefine() {
 JavaType javaType = JavaType.construct(java.util.LinkedHashSet.class, null);
 CollectionLikeType collectionLikeType = new CollectionLikeType((TypeBase) javaType.getInstanceType(), JavaType.of(Double.TYPE));
 JavaType refinedType = collectionLikeType.refine(java.util.HashSet.class, null, null, null, JavaType.of(Integer.TYPE));
 assertEquals(java.util.HashSet.class, refinedType.getRawClass());
 assertEquals(JavaType.construct(Collection.class, JavaType.of(Integer.TYPE)), refinedType.getRawType());
 assertEquals(JavaType.construct(HashSet.class, JavaType.of(Integer.TYPE)), refinedType.getType());
 assertEquals(JavaType.of(Integer.TYPE), refinedType.getContentType());
 assertNull(refinedType.getValueHandler());
 assertNull(refinedType.getTypeHandler());
 assertFalse(refinedType.isTrueCollectionType());
 }

 @Test
 public void testCollectionLikeTypeIsContainerTypeAndIsCollectionLikeType() {
 assertTrue(CollectionLikeType.class.isAssignableFrom(TypeBase.class));
 assertTrue(CollectionLikeType.class.isAssignableFrom(JavaType.class));
 assertTrue(CollectionLikeType.class.isInstance(new CollectionLikeType(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE)));
 assertTrue(CollectionLikeType.class.isInstance(new CollectionLikeType((TypeBase) new JavaType(Integer[].class).getInstanceType(), java.lang.Integer.TYPE)));
 }

 @Test
 public void testCollectionLikeTypeHashCode() {
 CollectionLikeType collectionLikeType1 = new CollectionLikeType(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE);
 CollectionLikeType collectionLikeType2 = new CollectionLikeType(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE);
 CollectionLikeType collectionLikeType3 = new CollectionLikeType(java.util.LinkedList.class, null, null, null, java.lang.Integer.TYPE);
 assertEquals(collectionLikeType1.hashCode(), collectionLikeType1.hashCode());
 assertEquals(collectionLikeType1.hashCode(), collectionLikeType2.hashCode());
 assertNotEquals(collectionLikeType1.hashCode(), collectionLikeType3.hashCode());
 }

 @Test
 public void testCollectionLikeTypeToString() {
 CollectionLikeType collectionLikeType = new CollectionLikeType(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE);
 assertEquals("[collection-like type; class java.util.ArrayList, contains class java.lang.Integer]", collectionLikeType.toString());
 }

 @Test
 public void testCollectionLikeTypeEquals() {
 CollectionLikeType collectionLikeType1 = new CollectionLikeType(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE);
 CollectionLikeType collectionLikeType2 = new CollectionLikeType(java.util.ArrayList.class, null, null, null, java.lang.Integer.TYPE);
 CollectionLikeType collectionLikeType3 = new CollectionLikeType(java.util.ArrayList.class, null, null, null, java.lang.String.class);
 CollectionLikeType collectionLikeType4 = new CollectionLikeType(java.util.LinkedList.class, null, null, null, java.lang.Integer.TYPE);
 assertTrue(collectionLikeType1.equals(collectionLikeType1));
 assertTrue(collectionLikeType1.equals(collectionLikeType2));
 assertFalse(collectionLikeType1.equals(collectionLikeType3));
 assertFalse(collectionLikeType1.equals(collectionLikeType4));
 }
}