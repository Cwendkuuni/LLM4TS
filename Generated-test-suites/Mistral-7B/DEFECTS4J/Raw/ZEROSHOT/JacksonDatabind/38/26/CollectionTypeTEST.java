package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings.TypeSerializerBindings;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CollectionTypeTest {

 @Test
 public void testConstructWithTypeBindingsAndElementType() {
 TypeBindings bindings = TypeBindings.defaultBindings();
 TypeSerializerBindings serializerBindings = bindings.findValueSerializer(String.class);
 JavaType elemType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType collectionType = CollectionType.construct(List.class, bindings, null, null, elemType);
 assertEquals(List.class, collectionType.getRawClass());
 assertEquals(elemType, collectionType.getContentType());
 assertEquals(serializerBindings, collectionType.getTypeHandler());
 assertEquals(TypeFactory.defaultInstance().constructType(List.class), collectionType.getRawType());
 }

 @Test
 public void testConstructWithRawTypeAndElementType() {
 JavaType elemType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType collectionType = CollectionType.construct(List.class, null, null, null, elemType);
 assertEquals(List.class, collectionType.getRawClass());
 assertEquals(elemType, collectionType.getContentType());
 assertNull(collectionType.getTypeHandler());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructWithRawTypeAndNullElementTypeThrowsException() {
 CollectionType.construct(List.class, null, null, null, null);
 }

 @Test
 public void testNarrow() {
 JavaType arrayType = TypeFactory.defaultInstance().constructType(String[].class);
 JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
 JavaType narrowedType = new CollectionType(ArrayList.class, null, null, null, null)._narrow(ArrayList.class);
 assertEquals(ArrayList.class, narrowedType.getRawClass());
 assertEquals(listType.getContentType(), narrowedType.getContentType());
 }

 @Test
 public void testWithContentType() {
 JavaType elemType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
 JavaType newListType = listType.withContentType(Integer.TYPE);
 assertEquals(listType.getRawClass(), newListType.getRawClass());
 assertEquals(Integer.TYPE, newListType.getContentType());
 }

 @Test
 public void testWithTypeHandler() {
 JavaType elemType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
 Object handler = new Object() {};
 JavaType newListType = listType.withTypeHandler(handler);
 assertEquals(listType.getRawClass(), newListType.getRawClass());
 assertEquals(elemType, newListType.getContentType());
 assertEquals(handler, newListType.getTypeHandler());
 }

 @Test
 public void testWithContentTypeHandler() {
 JavaType elemType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType contentTypeHandler = new Object() {};
 JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
 JavaType newListType = listType.withContentTypeHandler(contentTypeHandler);
 assertEquals(listType.getRawClass(), newListType.getRawClass());
 assertEquals(elemType, newListType.getContentType());
 assertEquals(contentTypeHandler, newListType.getContentTypeHandler());
 }

 @Test
 public void testWithValueHandler() {
 JavaType elemType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
 Object handler = new Object() {};
 JavaType newListType = listType.withValueHandler(handler);
 assertEquals(listType.getRawClass(), newListType.getRawClass());
 assertEquals(elemType, newListType.getContentType());
 assertEquals(handler, newListType.getValueHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 JavaType elemType = TypeFactory.defaultInstance().constructType(String.class);
 Object contentValueHandler = new Object() {};
 JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
 JavaType newListType = listType.withContentValueHandler(contentValueHandler);
 assertEquals(listType.getRawClass(), newListType.getRawClass());
 assertEquals(elemType, newListType.getContentType());
 assertEquals(contentValueHandler, newListType.getContentValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 JavaType elemType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
 JavaType staticListType = listType.withStaticTyping();
 assertEquals(listType.getRawClass(), staticListType.getRawClass());
 assertEquals(elemType, staticListType.getContentType());
 assertTrue(staticListType.isStatic());
 }

 @Test
 public void testRefine() {
 JavaType rawType = new TypeBase() {}.getType(ArrayList.class);
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType elemType = TypeFactory.defaultInstance().constructType(String.class);
 JavaType newCollectionType = new CollectionType(rawType, null).refine(ArrayList.class, bindings, null, null);
 assertEquals(ArrayList.class, newCollectionType.getRawClass());
 assertEquals(elemType, newCollectionType.getContentType());
 }

 @Test
 public void testToString() {
 JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
 assertEquals("[collection type; class java.util.List, contains class java.lang.String]", listType.toString());
 }
}