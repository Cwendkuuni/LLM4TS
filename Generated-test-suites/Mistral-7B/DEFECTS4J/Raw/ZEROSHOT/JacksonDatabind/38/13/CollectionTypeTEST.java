package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

 @Test
 public void testConstruct() {
 Class<List<Integer>> listIntType = new ArrayList<Integer>().getType();
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = null;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = JavaType.constructCollectionType(List.class, Integer.TYPE);
 CollectionType collectionType = CollectionType.construct(listIntType, bindings, superClass, superInts, elemT);
 assertEquals(List.class, collectionType.getRawClass());
 assertEquals(bindings, collectionType.getTypeBindings());
 assertEquals(superClass, collectionType.getSuperClass());
 assertEquals(superInts, collectionType.getSuperInterfaces());
 assertEquals(elemT, collectionType.getContentType());
 }

 @Test
 public void testConstructorWithNullBindings() {
 Class<List<Integer>> listIntType = new ArrayList<Integer>().getType();
 JavaType elemT = JavaType.constructCollectionType(List.class, Integer.TYPE);
 CollectionType collectionType = new CollectionType(listIntType, null, null, null, elemT);
 assertEquals(List.class, collectionType.getRawClass());
 assertNull(collectionType.getTypeBindings());
 }

 @Test
 public void testConstructDeprecated() {
 Class<List<Integer>> listIntType = new ArrayList<Integer>().getType();
 JavaType elemT = JavaType.constructCollectionType(List.class, Integer.TYPE);
 CollectionType collectionType = new CollectionType(listIntType, elemT);
 assertEquals(List.class, collectionType.getRawClass());
 assertNull(collectionType.getTypeBindings());
 }

 @Test
 public void testNarrow() {
 Class<ArrayList<String>> stringArrayListType = ArrayList.class;
 Class<List<String>> listStringType = new ArrayList<String>().getType();
 CollectionType collectionType = new CollectionType(listStringType.getComponentType(), null, null, null, String.class);
 ArrayListType stringArrayListTypeInstance = (ArrayListType) collectionType.narrow(ArrayList.class);
 assertEquals(stringArrayListType.getComponentType(), stringArrayListTypeInstance.getComponentType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNarrowInvalidClass() {
 Class<CollectionType> collectionTypeClass = CollectionType.class;
 CollectionType collectionType = new CollectionType(List.class, null, null, null, Integer.TYPE);
 try {
 collectionType.narrow(collectionTypeClass);
 } catch (IllegalArgumentException e) {
 assertEquals("Class is not a collection type", e.getMessage());
 throw e;
 }
 }

 @Test
 public void testWithContentType() {
 Class<List<Integer>> listIntType = new ArrayList<Integer>().getType();
 JavaType elemT = JavaType.constructCollectionType(List.class, Integer.class);
 CollectionType collectionType = new CollectionType(listIntType, null, null, null, Integer.TYPE);
 JavaType newCollectionType = collectionType.withContentType(elemT);
 assertEquals(listIntType.getComponentType(), collectionType.getContentType().getRawClass());
 assertEquals(elemT, newCollectionType.getContentType());
 }

 @Test
 public void testWithTypeHandler() {
 Class<List<Integer>> listIntType = new ArrayList<Integer>().getType();
 CollectionType collectionType = new CollectionType(listIntType, null, null, null, Integer.TYPE);
 Object handler = new Object();
 CollectionType newCollectionType = collectionType.withTypeHandler(handler);
 assertNotSame(collectionType, newCollectionType);
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<List<Integer>> listIntType = new ArrayList<Integer>().getType();
 JavaType elemT = JavaType.constructCollectionType(List.class, Integer.class);
 CollectionType collectionType = new CollectionType(listIntType, null, null, null, Integer.TYPE);
 Object handler = new Object();
 JavaType newCollectionType = collectionType.withContentTypeHandler(handler);
 assertEquals(elemT, newCollectionType.getContentType());
 assertEquals(handler, newCollectionType.getTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 Class<List<Integer>> listIntType = new ArrayList<Integer>().getType();
 CollectionType collectionType = new CollectionType(listIntType, null, null, null, Integer.TYPE);
 Object handler = new Object();
 CollectionType newCollectionType = collectionType.withValueHandler(handler);
 assertNotSame(collectionType, newCollectionType);
 }

 @Test
 public void testWithContentValueHandler() {
 Class<List<Integer>> listIntType = new ArrayList<Integer>().getType();
 JavaType elemT = JavaType.constructCollectionType(List.class, Integer.class);
 CollectionType collectionType = new CollectionType(listIntType, null, null, null, Integer.TYPE);
 Object handler = new Object();
 JavaType newCollectionType = collectionType.withContentValueHandler(handler);
 assertEquals(elemT, newCollectionType.getContentType());
 assertEquals(handler, newCollectionType.getValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 Class<List<Integer>> listIntType = new ArrayList<Integer>().getType();
 CollectionType collectionType = new CollectionType(listIntType, null, null, null, Integer.TYPE);
 CollectionType newCollectionType = collectionType.withStaticTyping();
 assertTrue(newCollectionType.isStatic());
 }

 @Test
 public void testRefine() {
 Class<ArrayList<Integer>> stringArrayListType = ArrayList.class;
 JavaType elemT = JavaType.constructCollectionType(List.class, Integer.class);
 CollectionType collectionType = new CollectionType(ArrayList.class, null, null, null, elemT);
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = null;
 JavaType[] superInterfaces = new JavaType[0];
 CollectionType newCollectionType = collectionType.refine(ArrayList.class, bindings, superClass, superInterfaces);
 assertEquals(ArrayList.class, newCollectionType.getRawClass());
 assertEquals(bindings, newCollectionType.getTypeBindings());
 }

 @Test
 public void testToString() {
 Class<List<Integer>> listIntType = new ArrayList<Integer>().getType();
 CollectionType collectionType = new CollectionType(listIntType, null, null, null, Integer.TYPE);
 assertEquals("{collection type; class class com.fasterxml.jackson.core.type.CollectionType, contains class java.lang.Integer}", collectionType.toString());
 }
}