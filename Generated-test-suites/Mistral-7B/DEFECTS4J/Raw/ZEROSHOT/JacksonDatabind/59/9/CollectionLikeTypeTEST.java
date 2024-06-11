package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CollectionLikeTypeTest {

 @Test
 public void testCollectionLikeTypeCreation() {
 // Test constructors
 JavaType javaIntType = TypeFactory.defaultInstance().constructType(int.class);
 JavaType collectionType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(),
 null, new JavaType[]{}, javaIntType);
 CollectionLikeType collectionLikeType = (CollectionLikeType) collectionType;
 assertEquals(ArrayList.class, collectionLikeType.getRawClass());
 assertEquals(TypeBindings.emptyBindings(), collectionLikeType.getTypeBindings());
 assertEquals(JavaType.construct(int.class), collectionLikeType.getElementType());
 assertFalse(collectionLikeType.isTrueCollectionType());

 // Test upgradeFrom
 JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType collectionStringType = CollectionLikeType.upgradeFrom(JavaType.construct(ArrayList.class), javaStringType);
 CollectionLikeType collectionLikeStringType = (CollectionLikeType) collectionStringType;
 assertEquals(ArrayList.class, collectionLikeStringType.getRawClass());
 assertEquals(TypeBindings.emptyBindings(), collectionLikeStringType.getTypeBindings());
 assertEquals(JavaType.construct(String.class), collectionLikeStringType.getElementType());
 assertTrue(collectionLikeStringType.isTrueCollectionType());
 }

 @Test
 public void testCollectionLikeTypeMethods() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(),
 null, new JavaType[]{}, JavaType.construct(int.class));

 JavaType contentType = collectionLikeType.getContentType();
 assertEquals(JavaType.construct(int.class), contentType);

 Object contentValueHandler = collectionLikeType.getContentValueHandler();
 assertNull(contentValueHandler);

 Object contentTypeHandler = collectionLikeType.getContentTypeHandler();
 assertNull(contentTypeHandler);

 boolean hasHandlers = collectionLikeType.hasHandlers();
 assertFalse(hasHandlers);

 // Test with handlers
 JavaType javaStringType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType valueHandler = TypeFactory.defaultInstance().constructType(StringHandler.class);
 JavaType typeHandler = TypeFactory.defaultInstance().constructType(StringTypeHandler.class);
 CollectionLikeType collectionLikeStringType = (CollectionLikeType) collectionLikeType.withContentTypeHandler(typeHandler)
 .withValueHandler(valueHandler);

 contentType = collectionLikeStringType.getContentType();
 assertEquals(JavaType.construct(String.class), contentType);

 contentValueHandler = collectionLikeStringType.getContentValueHandler();
 assertNotNull(contentValueHandler);

 contentTypeHandler = collectionLikeStringType.getContentTypeHandler();
 assertNotNull(contentTypeHandler);

 hasHandlers = collectionLikeStringType.hasHandlers();
 assertTrue(hasHandlers);
 }

 @Test
 public void testCollectionLikeTypeEquality() {
 CollectionLikeType collectionLikeType1 = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(),
 null, new JavaType[]{}, JavaType.construct(int.class));
 CollectionLikeType collectionLikeType2 = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(),
 null, new JavaType[]{}, JavaType.construct(int.class));
 CollectionLikeType collectionLikeType3 = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(),
 null, new JavaType[]{}, JavaType.construct(long.class));

 assertTrue(collectionLikeType1.equals(collectionLikeType2));
 assertFalse(collectionLikeType1.equals(collectionLikeType3));
 }

 @Test
 public void testCollectionLikeTypeToString() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(),
 null, new JavaType[]{}, JavaType.construct(int.class));
 assertEquals("[collection-like type; class java.util.ArrayList, contains class java.lang.Integer]",
 collectionLikeType.toString());
 }

 @Test
 public void testCollectionLikeTypeIsContainerType() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(),
 null, new JavaType[]{}, JavaType.construct(int.class));
 assertTrue(collectionLikeType.isContainerType());
 }

 @Test
 public void testCollectionLikeTypeIsCollectionLikeType() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(),
 null, new JavaType[]{}, JavaType.construct(int.class));
 assertTrue(collectionLikeType.isCollectionLikeType());
 }

 @Test
 public void testCollectionLikeTypeWithStaticTyping() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(),
 null, new JavaType[]{}, JavaType.construct(int.class));
 CollectionLikeType collectionLikeTypeWithStaticTyping = collectionLikeType.withStaticTyping();
 assertEquals(collectionLikeType.getRawClass(), collectionLikeTypeWithStaticTyping.getRawClass());
 assertTrue(collectionLikeTypeWithStaticTyping.isStatic());
 }

 @Test
 public void testCollectionLikeTypeNarrowing() {
 CollectionLikeType collectionLikeType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(),
 null, new JavaType[]{}, JavaType.construct(int.class));
 JavaType javaArrayListSubclass = TypeFactory.defaultInstance().constructType(ArrayListSubclass.class);
 CollectionLikeType narrowedCollectionLikeType = (CollectionLikeType) collectionLikeType.narrow(JavaType.construct(ArrayListSubclass.class));
 assertEquals(javaArrayListSubclass, narrowedCollectionLikeType.getRawClass());
 }
}