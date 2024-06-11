package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

 private JavaType elementType;
 private JavaType collectionType;

 @Before
 public void setUp() {
 elementType = TypeFactory.defaultInstance().constructType(int[].class);
 collectionType = CollectionLikeType.construct(List.class, TypeBindings.defaultBindings(), null, null, elementType);
 }

 @Test
 public void testIsContainerType() {
 assertTrue(collectionType.isContainerType());
 }

 @Test
 public void testIsCollectionLikeType() {
 assertTrue(collectionType.isCollectionLikeType());
 }

 @Test
 public void testGetContentType() {
 assertEquals(elementType, collectionType.getContentType());
 }

 @Test
 public void testGetContentValueHandler() {
 assertNull(collectionType.getContentValueHandler());
 }

 @Test
 public void testGetContentTypeHandler() {
 assertNull(collectionType.getContentTypeHandler());
 }

 @Test
 public void testHasHandlers() {
 assertFalse(collectionType.hasHandlers());
 }

 @Test
 public void testIsTrueCollectionType() {
 assertFalse(collectionType.isTrueCollectionType());
 }

 @Test(expected = IllegalStateException.class)
 public void testConstructWithNullElementType() {
 CollectionLikeType nullElementTypeType = CollectionLikeType.construct(List.class, TypeBindings.defaultBindings(), null, null, null);
 }

 @Test
 public void testUpgradeFrom() {
 JavaType javaType = TypeFactory.defaultInstance().constructType(ArrayList.class);
 CollectionLikeType upgradedType = CollectionLikeType.upgradeFrom(javaType, elementType);
 assertEquals(collectionType, upgradedType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpgradeFromInvalidType() {
 JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
 CollectionLikeType upgradedType = CollectionLikeType.upgradeFrom(javaType, elementType);
 }

 @Test
 public void testNarrowType() {
 JavaType subClass = TypeFactory.defaultInstance().constructType(ArrayList.class);
 CollectionLikeType narrowedType = collectionType.narrow(subClass.getClass());
 assertEquals(new CollectionLikeType(subClass.getClass(), TypeBindings.defaultBindings(), null, null, elementType), narrowedType);
 }

 @Test(expected = DeprecationWarning.class)
 public void testDeprecatedNarrowType() {
 JavaType subClass = TypeFactory.defaultInstance().constructType(ArrayList.class);
 CollectionLikeType narrowedType = collectionType._narrow(subClass.getClass());
 }

 @Test
 public void testWithContentType() {
 JavaType contentType = TypeFactory.defaultInstance().constructType(String.class);
 CollectionLikeType withContentType = collectionType.withContentType(contentType);
 assertEquals(new CollectionLikeType(collectionType._class, collectionType._bindings, collectionType._superClass, collectionType._superInterfaces, contentType), withContentType);
 }

 @Test
 public void testWithTypeHandler() {
 Object handler = new Object() {};
 CollectionLikeType withTypeHandler = collectionType.withTypeHandler(handler);
 assertEquals(new CollectionLikeType(collectionType._class, collectionType._bindings, collectionType._superClass, collectionType._superInterfaces, elementType, handler, null, collectionType._asStatic), withTypeHandler);
 }

 @Test
 public void testWithContentTypeHandler() {
 Object handler = new Object() {};
 CollectionLikeType withContentTypeHandler = collectionType.withContentTypeHandler(handler);
 assertEquals(new CollectionLikeType(collectionType._class, collectionType._bindings, collectionType._superClass, collectionType._superInterfaces, elementType, null, handler, collectionType._asStatic), withContentTypeHandler);
 }

 @Test
 public void testWithValueHandler() {
 Object handler = new Object() {};
 CollectionLikeType withValueHandler = collectionType.withValueHandler(handler);
 assertEquals(new CollectionLikeType(collectionType._class, collectionType._bindings, collectionType._superClass, collectionType._superInterfaces, elementType, handler, null, collectionType._asStatic), withValueHandler);
 }

 @Test
 public void testWithContentValueHandler() {
 Object handler = new Object() {};
 CollectionLikeType withContentValueHandler = collectionType.withContentValueHandler(handler);
 assertEquals(new CollectionLikeType(collectionType._class, collectionType._bindings, collectionType._superClass, collectionType._superInterfaces, elementType, null, null, handler), withContentValueHandler);
 }

 @Test
 public void testWithStaticTyping() {
 CollectionLikeType staticTyping = collectionType.withStaticTyping();
 assertTrue(staticTyping._asStatic);
 }

 @Test
 public void testRefine() {
 JavaType refinedType = collectionType.refine(ArrayList.class, TypeBindings.defaultBindings(), null, null);
 assertEquals(new CollectionLikeType(ArrayList.class, TypeBindings.defaultBindings(), null, null, elementType), refinedType);
 }

}