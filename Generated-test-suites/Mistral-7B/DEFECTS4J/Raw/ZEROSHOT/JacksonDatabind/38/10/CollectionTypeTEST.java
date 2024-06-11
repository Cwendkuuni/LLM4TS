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
 Class<List<Integer>> listClass = java.util.List.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = JavaType.construct(Object.class, bindings);
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemT = JavaType.construct(Integer.class, bindings);
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);

 assertEquals(List.class, collectionType.getRawClass());
 assertEquals(bindings, collectionType.getTypeBindings());
 assertEquals(superClass, collectionType.getSuperClass());
 assertEquals(superInterfaces, collectionType.getSuperInterfaces());
 assertEquals(elemT, collectionType.getContentType());
 assertFalse(collectionType.isStatic());
 }

 @Test
 public void testConstructDeprecated() {
 Class<List<Integer>> listClass = java.util.List.class;
 JavaType elemT = JavaType.construct(Integer.class, null);
 CollectionType collectionType = CollectionType.construct(listClass, elemT);

 assertEquals(List.class, collectionType.getRawClass());
 assertNull(collectionType.getTypeBindings());
 assertEquals(null, collectionType.getSuperClass());
 assertArrayEquals(new JavaType[0], collectionType.getSuperInterfaces());
 assertEquals(elemT, collectionType.getContentType());
 assertFalse(collectionType.isStatic());
 }

 @Test
 public void testNarrow() {
 Class<ArrayList<Integer>> arrayListClass = java.util.ArrayList.class;
 CollectionType listType = CollectionType.construct(java.util.List.class, null, null, new JavaType[0], JavaType.construct(Integer.class, null));
 CollectionType arrayListType = listType.narrow(arrayListClass);

 assertEquals(ArrayList.class, arrayListType.getRawClass());
 assertEquals(null, arrayListType.getTypeBindings());
 assertEquals(null, arrayListType.getSuperClass());
 assertArrayEquals(new JavaType[0], arrayListType.getSuperInterfaces());
 assertEquals(JavaType.construct(Integer.class, null), arrayListType.getContentType());
 assertFalse(arrayListType.isStatic());
 }

 @Test
 public void testWithContentType() {
 Class<List<String>> listClass = java.util.List.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = JavaType.construct(Object.class, bindings);
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemT = JavaType.construct(Integer.class, bindings);
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 JavaType stringType = JavaType.construct(String.class, bindings);
 CollectionType newCollectionType = collectionType.withContentType(stringType);

 assertEquals(List.class, newCollectionType.getRawClass());
 assertEquals(bindings, newCollectionType.getTypeBindings());
 assertEquals(superClass, newCollectionType.getSuperClass());
 assertArrayEquals(newJavaType[0], newCollectionType.getSuperInterfaces());
 assertEquals(stringType, newCollectionType.getContentType());
 assertFalse(newCollectionType.isStatic());
 }

 @Test
 public void testWithTypeHandler() {
 Class<List<Integer>> listClass = java.util.List.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = JavaType.construct(Object.class, bindings);
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemT = JavaType.construct(Integer.class, bindings);
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 Object handler = new Object();
 CollectionType newCollectionType = collectionType.withTypeHandler(handler);

 assertEquals(List.class, newCollectionType.getRawClass());
 assertEquals(bindings, newCollectionType.getTypeBindings());
 assertEquals(superClass, newCollectionType.getSuperClass());
 assertArrayEquals(newJavaType[0], newCollectionType.getSuperInterfaces());
 assertEquals(elemT, newCollectionType.getContentType());
 assertEquals(handler, newCollectionType.getTypeHandler());
 assertFalse(newCollectionType.isStatic());
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<List<Integer>> listClass = java.util.List.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = JavaType.construct(Object.class, bindings);
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemT = JavaType.construct(Integer.class, bindings);
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 Object handler = new Object();
 JavaType stringType = JavaType.construct(String.class, bindings);
 CollectionType newCollectionType = collectionType.withContentTypeHandler(handler);

 assertEquals(List.class, newCollectionType.getRawClass());
 assertEquals(bindings, newCollectionType.getTypeBindings());
 assertEquals(superClass, newCollectionType.getSuperClass());
 assertArrayEquals(newJavaType[0], newCollectionType.getSuperInterfaces());
 assertEquals(stringType.withTypeHandler(handler), newCollectionType.getContentType());
 assertEquals(handler, newCollectionType.getContentTypeHandler());
 assertFalse(newCollectionType.isStatic());
 }

 @Test
 public void testWithValueHandler() {
 Class<List<Integer>> listClass = java.util.List.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = JavaType.construct(Object.class, bindings);
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemT = JavaType.construct(Integer.class, bindings);
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 Object handler = new Object();
 CollectionType newCollectionType = collectionType.withValueHandler(handler);

 assertEquals(List.class, newCollectionType.getRawClass());
 assertEquals(bindings, newCollectionType.getTypeBindings());
 assertEquals(superClass, newCollectionType.getSuperClass());
 assertArrayEquals(newJavaType[0], newCollectionType.getSuperInterfaces());
 assertEquals(elemT, newCollectionType.getContentType());
 assertEquals(handler, newCollectionType.getValueHandler());
 assertFalse(newCollectionType.isStatic());
 }

 @Test
 public void testWithContentValueHandler() {
 Class<List<Integer>> listClass = java.util.List.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = JavaType.construct(Object.class, bindings);
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemT = JavaType.construct(Integer.class, bindings);
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 Object handler = new Object();
 JavaType stringType = JavaType.construct(String.class, bindings);
 CollectionType newCollectionType = collectionType.withContentValueHandler(handler);

 assertEquals(List.class, newCollectionType.getRawClass());
 assertEquals(bindings, newCollectionType.getTypeBindings());
 assertEquals(superClass, newCollectionType.getSuperClass());
 assertArrayEquals(newJavaType[0], newCollectionType.getSuperInterfaces());
 assertEquals(elemT.withValueHandler(handler), newCollectionType.getContentType());
 assertEquals(handler, newCollectionType.getContentValueHandler());
 assertFalse(newCollectionType.isStatic());
 }

 @Test
 public void testWithStaticTyping() {
 Class<List<Integer>> listClass = java.util.List.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = JavaType.construct(Object.class, bindings);
 JavaType[] superInterfaces = new JavaType[0];
 JavaType elemT = JavaType.construct(Integer.class, bindings);
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);
 CollectionType newCollectionType = collectionType.withStaticTyping();

 assertEquals(List.class, newCollectionType.getRawClass());
 assertEquals(bindings, newCollectionType.getTypeBindings());
 assertEquals(superClass, newCollectionType.getSuperClass());
 assertArrayEquals(newJavaType[0], newCollectionType.getSuperInterfaces());
 assertEquals(elemT, newCollectionType.getContentType());
 assertTrue(newCollectionType.isStatic());
 }

 @Test
 public void testRefine() {
 Class<List<String>> listClass = java.util.List.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = JavaType.construct(CollectionLikeType.class, bindings);
 JavaType[] superInterfaces = new JavaType[] { JavaType.construct(Collection.class, bindings) };
 JavaType elemT = JavaType.construct(String.class, bindings);
 CollectionType collectionType = CollectionType.construct(listClass, null, null, new JavaType[0], elemT);
 CollectionType newCollectionType = collectionType.refine(ArrayList.class, bindings, superClass, superInterfaces);

 assertEquals(ArrayList.class, newCollectionType.getRawClass());
 assertEquals(bindings, newCollectionType.getTypeBindings());
 assertEquals(superClass, newCollectionType.getSuperClass());
 assertArrayEquals(superInterfaces, newCollectionType.getSuperInterfaces());
 assertEquals(elemT, newCollectionType.getContentType());
 assertFalse(newCollectionType.isStatic());
 }
}