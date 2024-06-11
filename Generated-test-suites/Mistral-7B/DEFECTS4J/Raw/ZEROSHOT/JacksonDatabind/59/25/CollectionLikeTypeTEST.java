package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

 @Test
 public void testConstructorWithClassAndTypeBindings() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(
 JavaType.getTypeFactory().constructType(Collection.class, String.class),
 TypeBindings.emptyBindings(),
 JavaType.constructType(Collection.class, Object.class),
 null,
 JavaType.constructType(String.class),
 null,
 null,
 false
 );
 assertNotNull(collectionLikeType);
 assertSame(Collection.class, collectionLikeType.getRawClass());
 assertSame(String.class, collectionLikeType.getElementType());
 }

 @Test
 public void testConstructorWithBaseAndElementType() {
 TypeBase base = TypeBase.construct(JavaType.constructType(Collection.class, Object.class), null, null, null, null, null, null, false);
 CollectionLikeType collectionLikeType = new CollectionLikeType(base, JavaType.constructType(String.class));
 assertNotNull(collectionLikeType);
 assertSame(Collection.class, collectionLikeType.getRawClass());
 assertSame(String.class, collectionLikeType.getElementType());
 }

 @Test
 public void testUpgradeFromSimpleType() {
 JavaType javaType = JavaType.constructType(ArrayList.class, Integer.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, Integer.class);
 assertNotNull(collectionLikeType);
 assertSame(ArrayList.class, collectionLikeType.getRawClass());
 assertSame(Integer.class, collectionLikeType.getElementType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromInvalidBaseType() {
 JavaType javaType = JavaType.constructType(String.class);
 CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, Integer.class);
 fail("Should not be able to upgrade from a non-Collection type");
 }

 @Test
 public void testNarrow() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(
 JavaType.getTypeFactory().constructType(ArrayList.class, String.class),
 TypeBindings.emptyBindings(),
 JavaType.constructType(Collection.class, Object.class),
 null,
 JavaType.constructType(String.class),
 null,
 null,
 false
 );
 JavaType narrowType = collectionLikeType.narrow(ArrayList.class);
 assertNotNull(narrowType);
 assertSame(ArrayList.class, narrowType.getRawClass());
 assertSame(String.class, narrowType.getElementType());
 }

 @Test
 public void testIsContainerType() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(
 JavaType.getTypeFactory().constructType(ArrayList.class, String.class),
 TypeBindings.emptyBindings(),
 JavaType.constructType(Collection.class, Object.class),
 null,
 JavaType.constructType(String.class),
 null,
 null,
 false
 );
 assertTrue(collectionLikeType.isContainerType());
 }

 @Test
 public void testIsCollectionLikeType() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(
 JavaType.getTypeFactory().constructType(ArrayList.class, String.class),
 TypeBindings.emptyBindings(),
 JavaType.constructType(Collection.class, Object.class),
 null,
 JavaType.constructType(String.class),
 null,
 null,
 false
 );
 assertTrue(collectionLikeType.isCollectionLikeType());
 }

 @Test
 public void testGetContentType() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(
 JavaType.getTypeFactory().constructType(ArrayList.class, String.class),
 TypeBindings.emptyBindings(),
 JavaType.constructType(Collection.class, Object.class),
 null,
 JavaType.constructType(String.class),
 null,
 null,
 false
 );
 assertSame(String.class, collectionLikeType.getContentType());
 }

 @Test
 public void testGetContentValueHandler() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(
 JavaType.getTypeFactory().constructType(ArrayList.class, String.class),
 TypeBindings.emptyBindings(),
 JavaType.constructType(Collection.class, Object.class),
 null,
 JavaType.constructType(String.class),
 null,
 null,
 false
 );
 assertNull(collectionLikeType.getContentValueHandler());
 }

 @Test
 public void testGetContentTypeHandler() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(
 JavaType.getTypeFactory().constructType(ArrayList.class, String.class),
 TypeBindings.emptyBindings(),
 JavaType.constructType(Collection.class, Object.class),
 null,
 JavaType.constructType(String.class),
 null,
 null,
 false
 );
 assertNull(collectionLikeType.getContentTypeHandler());
 }

 @Test
 public void testHasHandlers() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(
 JavaType.getTypeFactory().constructType(ArrayList.class, String.class),
 TypeBindings.emptyBindings(),
 JavaType.constructType(Collection.class, Object.class),
 null,
 JavaType.constructType(String.class),
 null,
 null,
 false
 );
 assertFalse(collectionLikeType.hasHandlers());
 }

 @Test
 public void testGetErasedSignature() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(
 JavaType.getTypeFactory().constructType(ArrayList.class, String.class),
 TypeBindings.emptyBindings(),
 JavaType.constructType(Collection.class, Object.class),
 null,
 JavaType.constructType(String.class),
 null,
 null,
 false
 );
 StringBuilder sb = new StringBuilder();
 collectionLikeType.getErasedSignature(sb);
 assertEquals("java.util.Collection", sb.toString());
 }

 @Test
 public void testGetGenericSignature() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(
 JavaType.getTypeFactory().constructType(ArrayList.class, String.class),
 TypeBindings.emptyBindings(),
 JavaType.constructType(Collection.class, Object.class),
 null,
 JavaType.constructType(String.class),
 null,
 null,
 false
 );
 StringBuilder sb = new StringBuilder();
 collectionLikeType.getGenericSignature(sb);
 assertEquals("java.util.Collection<java.lang.String>", sb.toString());
 }

 @Test
 public void testBuildCanonicalName() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(
 JavaType.getTypeFactory().constructType(ArrayList.class, String.class),
 TypeBindings.emptyBindings(),
 JavaType.constructType(Collection.class, Object.class),
 null,
 JavaType.constructType(String.class),
 null,
 null,
 false
 );
 assertEquals("java.util.Collection<java.lang.String>", collectionLikeType.buildCanonicalName());
 }

 @Test
 public void testIsTrueCollectionType() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(
 JavaType.getTypeFactory().constructType(ArrayList.class, String.class),
 TypeBindings.emptyBindings(),
 JavaType.constructType(Collection.class, Object.class),
 null,
 JavaType.constructType(String.class),
 null,
 null,
 false
 );
 assertTrue(collectionLikeType.isTrueCollectionType());
 }

 @Test
 public void testEquals() {
 CollectionLikeType collectionLikeType1 = CollectionLikeType.construct(
 JavaType.getTypeFactory().constructType(ArrayList.class, String.class),
 TypeBindings.emptyBindings(),
 JavaType.constructType(Collection.class, Object.class),
 null,
 JavaType.constructType(String.class),
 null,
 null,
 false
 );
 CollectionLikeType collectionLikeType2 = CollectionLikeType.construct(
 JavaType.getTypeFactory().constructType(ArrayList.class, String.class),
 TypeBindings.emptyBindings(),
 JavaType.constructType(Collection.class, Object.class),
 null,
 JavaType.constructType(String.class),
 null,
 null,
 false
 );
 CollectionLikeType collectionLikeType3 = CollectionLikeType.construct(
 JavaType.getTypeFactory().constructType(LinkedList.class, String.class),
 TypeBindings.emptyBindings(),
 JavaType.constructType(Collection.class, Object.class),
 null,
 JavaType.constructType(String.class),
 null,
 null,
 false
 );
 CollectionLikeType collectionLikeType4 = collectionLikeType1;
 assertTrue(collectionLikeType1.equals(collectionLikeType4));
 assertTrue(collectionLikeType4.equals(collectionLikeType1));
 assertFalse(collectionLikeType1.equals(collectionLikeType2));
 assertFalse(collectionLikeType2.equals(collectionLikeType1));
 assertFalse(collectionLikeType1.equals(collectionLikeType3));
 assertFalse(collectionLikeType3.equals(collectionLikeType1));
 }

 @Test
 public void testToString() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(
 JavaType.getTypeFactory().constructType(ArrayList.class, String.class),
 TypeBindings.emptyBindings(),
 JavaType.constructType(Collection.class, Object.class),
 null,
 JavaType.constructType(String.class),
 null,
 null,
 false
 );
 assertEquals("[collection-like type; class java.util.ArrayList<java.lang.String>, contains java.lang.String]", collectionLikeType.toString());
 }

}