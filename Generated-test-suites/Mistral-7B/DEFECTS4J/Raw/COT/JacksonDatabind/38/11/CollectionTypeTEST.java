package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class CollectionTypeTest {

    @Test
    public void testCollectionTypeConstructor() {
        Class<List<String>> listClass = List.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(String.class);
        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemT);
        assertEquals(List.class, collectionType.getClass());
        assertEquals(bindings, collectionType._bindings);
        assertEquals(superClass, collectionType._superClass);
        assertEquals(superInts, collectionType._superInterfaces);
        assertEquals(elemT, collectionType._elementType);
        assertNull(collectionType._valueHandler);
        assertNull(collectionType._typeHandler);
        assertFalse(collectionType._asStatic);
    }

    @Test
    public void testCollectionTypeConstructorWithBindings() {
        Class<List<String>> listClass = List.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(String.class);
        CollectionType collectionType = new CollectionType(listClass, bindings, superClass, superInts, elemT);
        assertEquals(List.class, collectionType.getClass());
        assertEquals(bindings, collectionType._bindings);
        assertEquals(superClass, collectionType._superClass);
        assertEquals(superInts, collectionType._superInterfaces);
        assertEquals(elemT, collectionType._elementType);
        assertNull(collectionType._valueHandler);
        assertNull(collectionType._typeHandler);
        assertFalse(collectionType._asStatic);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollectionTypeConstructWithNullRawType() {
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(String.class);
        CollectionType collectionType = CollectionType.construct(null, bindings, superClass, superInts, elemT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollectionTypeConstructWithNullBindings() {
        Class<?> rawType = List.class;
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(String.class);
        CollectionType collectionType = CollectionType.construct(rawType, null, superClass, superInts, elemT);
    }

    @Test
    public void testCollectionTypeConstructWithDeprecated() {
        Class<List<String>> listClass = List.class;
        CollectionType collectionType = CollectionType.construct(listClass, null, null, null, JavaType.construct(String.class));
        assertEquals(List.class, collectionType.getClass());
        assertNotNull(collectionType._bindings);
        assertNull(collectionType._superClass);
        assertNull(collectionType._superInterfaces);
        assertEquals(JavaType.construct(String.class), collectionType._elementType);
        assertNull(collectionType._valueHandler);
        assertNull(collectionType._typeHandler);
        assertFalse(collectionType._asStatic);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCollectionTypeConstructWithNullElementType() {
        Class<?> rawType = List.class;
        CollectionType collectionType = CollectionType.construct(rawType, null, null, null, null);
    }

    @Test
    public void testNarrowMethod() {
        Class<ArrayList<Integer>> arrListClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(Integer.class);
        CollectionType collectionType = new CollectionType(arrListClass, bindings, superClass, superInts, elemT);
        CollectionType narrowedType = collectionType._narrow(LinkedList.class);
        assertNotSame(collectionType, narrowedType);
        assertEquals(LinkedList.class, narrowedType.getClass());
        assertEquals(collectionType._bindings, narrowedType._bindings);
        assertEquals(collectionType._superClass, narrowedType._superClass);
        assertArrayEquals(collectionType._superInterfaces, narrowedType._superInterfaces);
        assertEquals(collectionType._elementType, narrowedType._elementType);
        assertNull(narrowedType._valueHandler);
        assertNull(narrowedType._typeHandler);
        assertFalse(narrowedType._asStatic);
    }

    @Test(expected = ClassCastException.class)
    public void testNarrowMethodWithIncompatibleSubclass() {
        Class<ArrayList<Integer>> arrListClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(Integer.class);
        CollectionType collectionType = new CollectionType(arrListClass, bindings, superClass, superInts, elemT);
        CollectionType narrowedType = collectionType._narrow(String.class);
    }

    @Test
    public void testWithContentTypeMethod() {
        Class<ArrayList<Integer>> arrListClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(Integer.class);
        JavaType contentType = JavaType.construct(Object.class);
        CollectionType collectionType = new CollectionType(arrListClass, bindings, superClass, superInts, elemT);
        CollectionType withContentType = collectionType.withContentType(contentType);
        assertNotSame(collectionType, withContentType);
        assertEquals(arrListClass, withContentType.getClass());
        assertEquals(bindings, withContentType._bindings);
        assertEquals(superClass, withContentType._superClass);
        assertArrayEquals(superInts, withContentType._superInterfaces);
        assertEquals(contentType, withContentType._elementType);
        assertNull(withContentType._valueHandler);
        assertNull(withContentType._typeHandler);
        assertFalse(withContentType._asStatic);
    }

    @Test
    public void testWithTypeHandlerMethod() {
        Class<ArrayList<Integer>> arrListClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(Integer.class);
        Object typeHandler = new Object();
        CollectionType collectionType = new CollectionType(arrListClass, bindings, superClass, superInts, elemT);
        CollectionType withTypeHandler = collectionType.withTypeHandler(typeHandler);
        assertNotSame(collectionType, withTypeHandler);
        assertEquals(arrListClass, withTypeHandler.getClass());
        assertEquals(bindings, withTypeHandler._bindings);
        assertEquals(superClass, withTypeHandler._superClass);
        assertArrayEquals(superInts, withTypeHandler._superInterfaces);
        assertEquals(elemT, withTypeHandler._elementType);
        assertEquals(typeHandler, withTypeHandler._typeHandler);
        assertNull(withTypeHandler._valueHandler);
        assertFalse(withTypeHandler._asStatic);
    }

    @Test
    public void testWithContentTypeHandlerMethod() {
        Class<ArrayList<Integer>> arrListClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(Integer.class);
        Object contentTypeHandler = new Object();
        CollectionType collectionType = new CollectionType(arrListClass, bindings, superClass, superInts, elemT);
        CollectionType withContentTypeHandler = collectionType.withContentTypeHandler(contentTypeHandler);
        assertNotSame(collectionType, withContentTypeHandler);
        assertEquals(arrListClass, withContentTypeHandler.getClass());
        assertEquals(bindings, withContentTypeHandler._bindings);
        assertEquals(superClass, withContentTypeHandler._superClass);
        assertArrayEquals(superInts, withContentTypeHandler._superInterfaces);
        assertEquals(elemT, withContentTypeHandler._elementType);
        assertEquals(contentTypeHandler, withContentTypeHandler._valueHandler);
        assertNull(withContentTypeHandler._typeHandler);
        assertFalse(withContentTypeHandler._asStatic);
    }

    @Test
    public void testWithValueHandlerMethod() {
        Class<ArrayList<Integer>> arrListClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(Integer.class);
        Object valueHandler = new Object();
        CollectionType collectionType = new CollectionType(arrListClass, bindings, superClass, superInts, elemT);
        CollectionType withValueHandler = collectionType.withValueHandler(valueHandler);
        assertNotSame(collectionType, withValueHandler);
        assertEquals(arrListClass, withValueHandler.getClass());
        assertEquals(bindings, withValueHandler._bindings);
        assertEquals(superClass, withValueHandler._superClass);
        assertArrayEquals(superInts, withValueHandler._superInterfaces);
        assertEquals(elemT, withValueHandler._elementType);
        assertNull(withValueHandler._valueHandler);
        assertEquals(valueHandler, withValueHandler._typeHandler);
        assertFalse(withValueHandler._asStatic);
    }

    @Test
    public void testWithContentValueHandlerMethod() {
        Class<ArrayList<Integer>> arrListClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(Integer.class);
        Object contentValueHandler = new Object();
        CollectionType collectionType = new CollectionType(arrListClass, bindings, superClass, superInts, elemT);
        CollectionType withContentValueHandler = collectionType.withContentValueHandler(contentValueHandler);
        assertNotSame(collectionType, withContentValueHandler);
        assertEquals(arrListClass, withContentValueHandler.getClass());
        assertEquals(bindings, withContentValueHandler._bindings);
        assertEquals(superClass, withContentValueHandler._superClass);
        assertArrayEquals(superInts, withContentValueHandler._superInterfaces);
        assertEquals(elemT, withContentValueHandler._elementType);
        assertEquals(contentValueHandler, withContentValueHandler._valueHandler);
        assertEquals(contentValueHandler, withContentValueHandler._typeHandler);
        assertFalse(withContentValueHandler._asStatic);
    }

    @Test
    public void testWithStaticTypingMethod() {
        Class<ArrayList<Integer>> arrListClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(Integer.class);
        CollectionType collectionType = new CollectionType(arrListClass, bindings, superClass, superInts, elemT);
        CollectionType withStaticTyping = collectionType.withStaticTyping();
        assertNotSame(collectionType, withStaticTyping);
        assertEquals(arrListClass, withStaticTyping.getClass());
        assertEquals(bindings, withStaticTyping._bindings);
        assertEquals(superClass, withStaticTyping._superClass);
        assertArrayEquals(superInts, withStaticTyping._superInterfaces);
        assertEquals(elemT, withStaticTyping._elementType);
        assertNull(withStaticTyping._valueHandler);
        assertNull(withStaticTyping._typeHandler);
        assertTrue(withStaticTyping._asStatic);
    }

    @Test
    public void testRefineMethod() {
        Class<ArrayList<Integer>> arrListClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(Integer.class);
        CollectionType collectionType = new CollectionType(arrListClass, bindings, superClass, superInts, elemT);
        JavaType refinedType = collectionType.refine(LinkedList.class, bindings, superClass, superInts);
        assertNotSame(collectionType, refinedType);
        assertEquals(LinkedList.class, refinedType.getClass());
        assertNull(refinedType._bindings);
        assertEquals(superClass, refinedType._superClass);
        assertArrayEquals(superInts, refinedType._superInterfaces);
        assertEquals(elemT, refinedType._elementType);
        assertNull(refinedType._valueHandler);
        assertNull(refinedType._typeHandler);
        assertFalse(refinedType._asStatic);
    }

    @Test
    public void testToStringMethod() {
        Class<ArrayList<Integer>> arrListClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = JavaType.construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.construct(Integer.class);
        CollectionType collectionType = new CollectionType(arrListClass, bindings, superClass, superInts, elemT);
        String toString = collectionType.toString();
        assertTrue(toString.startsWith("[collection type; class " + arrListClass.getName() + ", contains " + elemT +"]"));
    }
}