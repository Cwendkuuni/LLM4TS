package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

 @Test
 public void testConstructor() {
 Class<?> listClass = ArrayList.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType superClass = Collection.class;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = IntegerType.INSTANCE;
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemT);
 assertNotNull(collectionType);
 assertEquals(listClass, collectionType.getRawClass());
 assertEquals(bindings, collectionType.getTypeBindings());
 assertEquals(superClass, collectionType.getSuperClass());
 assertEquals(superInts, collectionType.getSuperInterfaces());
 assertEquals(elemT, collectionType.getElementType());
 }

 @Test
 public void testDeprecatedConstructor() {
 Class<?> listClass = ArrayList.class;
 JavaType elemT = IntegerType.INSTANCE;
 CollectionType collectionType = CollectionType.construct(listClass, elemT);
 assertNotNull(collectionType);
 assertEquals(listClass, collectionType.getRawClass());
 // The rest of the checks are not possible without knowing the bindings
 }

 @Test
 public void testNarrow() {
 Class<?> listClass = ArrayList.class;
 Class<?> subListClass = ArrayListSubClass.class;
 CollectionType collectionType = CollectionType.construct(listClass, null, null, null, IntegerType.INSTANCE);
 CollectionType narrowedType = collectionType._narrow(subListClass);
 assertNotNull(narrowedType);
 assertEquals(subListClass, narrowedType.getRawClass());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNarrowUnsupported() {
 Class<?> listClass = ArrayList.class;
 Class<?> subListClass = LinkedList.class;
 CollectionType collectionType = CollectionType.construct(listClass, null, null, null, IntegerType.INSTANCE);
 collectionType._narrow(subListClass);
 }

 @Test
 public void testWithContentType() {
 Class<?> listClass = ArrayList.class;
 Class<?> stringClass = String.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType superClass = Collection.class;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = IntegerType.INSTANCE;
 JavaType contentType = StringType.INSTANCE;
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemT);
 CollectionType result = collectionType.withContentType(contentType);
 assertNotNull(result);
 assertEquals(listClass, result.getRawClass());
 assertEquals(bindings, result.getTypeBindings());
 assertEquals(superClass, result.getSuperClass());
 assertEquals(superInts, result.getSuperInterfaces());
 assertEquals(contentType, result.getElementType());
 }

 @Test
 public void testWithContentTypeHandler() {
 Class<?> listClass = ArrayList.class;
 Class<?> stringClass = String.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType superClass = Collection.class;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = IntegerType.INSTANCE;
 JavaType contentType = StringType.INSTANCE;
 Object stringHandler = new StringTypeHandler();
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemT);
 CollectionType result = collectionType.withContentTypeHandler(stringHandler);
 assertNotNull(result);
 assertEquals(listClass, result.getRawClass());
 assertEquals(bindings, result.getTypeBindings());
 assertEquals(superClass, result.getSuperClass());
 assertEquals(superInts, result.getSuperInterfaces());
 assertEquals(elemT, result.getElementType());
 assertEquals(stringHandler, result.getContentTypeHandler());
 }

 @Test
 public void testWithTypeHandler() {
 Class<?> listClass = ArrayList.class;
 Class<?> stringClass = String.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType superClass = Collection.class;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = IntegerType.INSTANCE;
 Object stringHandler = new StringTypeHandler();
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemT);
 CollectionType result = collectionType.withTypeHandler(stringHandler);
 assertNotNull(result);
 assertEquals(listClass, result.getRawClass());
 assertEquals(bindings, result.getTypeBindings());
 assertEquals(superClass, result.getSuperClass());
 assertEquals(superInts, result.getSuperInterfaces());
 assertEquals(elemT, result.getElementType());
 assertEquals(stringHandler, result.getTypeHandler());
 }

 @Test
 public void testWithContentValueHandler() {
 Class<?> listClass = ArrayList.class;
 Class<?> stringClass = String.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType superClass = Collection.class;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = IntegerType.INSTANCE;
 Object stringHandler = new StringTypeHandler();
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemT);
 CollectionType result = collectionType.withContentValueHandler(stringHandler);
 assertNotNull(result);
 assertEquals(listClass, result.getRawClass());
 assertEquals(bindings, result.getTypeBindings());
 assertEquals(superClass, result.getSuperClass());
 assertEquals(superInts, result.getSuperInterfaces());
 assertEquals(elemT, result.getElementType());
 assertEquals(stringHandler, result.getContentValueHandler());
 }

 @Test
 public void testWithStaticTyping() {
 Class<?> listClass = ArrayList.class;
 CollectionType collectionType = CollectionType.construct(listClass, null, null, null, IntegerType.INSTANCE);
 CollectionType staticTyping = collectionType.withStaticTyping();
 assertNotNull(staticTyping);
 assertEquals(listClass, staticTyping.getRawClass());
 assertEquals(false, staticTyping._asStatic);
 CollectionType staticCollectionType = CollectionType.construct(listClass, null, null, null, IntegerType.INSTANCE).withStaticTyping();
 assertEquals(staticCollectionType, staticTyping);
 }

 @Test
 public void testRefine() {
 Class<?> listClass = ArrayList.class;
 Class<?> subListClass = ArrayListSubClass.class;
 TypeBindings bindings = TypeBindings.defaultBindings();
 JavaType superClass = Collection.class;
 JavaType[] superInts = new JavaType[0];
 JavaType elemT = IntegerType.INSTANCE;
 CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemT);
 CollectionType refinedType = collectionType.refine(subListClass, bindings, null, null);
 assertNotNull(refinedType);
 assertEquals(subListClass, refinedType.getRawClass());
 }
}