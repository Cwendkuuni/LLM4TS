package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.*;

@Category(IntegrationTests.class)
public class CollectionTypeTest {

    // Basic test cases for all methods

    @Test
    public void testConstruct() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = null;
        JavaType superClass = _bogusSuperClass(rawType);
        JavaType[] superInterfaces = null;
        JavaType elemT = String.class;
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInterfaces, elemT);
        assertNotNull(collectionType);
    }

    @Test
    public void testConstructDeprecated() {
        Class<?> rawType = ArrayList.class;
        JavaType elemT = String.class;
        CollectionType collectionType = CollectionType.construct(rawType, elemT);
        assertNotNull(collectionType);
    }

    @Test
    public void testNarrow() {
        Class<?> rawType = ArrayList.class;
        Class<?> subclass = LinkedList.class;
        CollectionType collectionType = new CollectionType(rawType, null, _bogusSuperClass(rawType), null, String.class);
        CollectionType narrowedType = collectionType._narrow(subclass);
        assertNotNull(narrowedType);
        assertTrue(narrowedType.getClass() == LinkedListType.class);
    }

    @Test
    public void testWithContentType() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = null;
        JavaType superClass = _bogusSuperClass(rawType);
        JavaType[] superInterfaces = null;
        JavaType elemT = String.class;
        JavaType contentType = Integer.class;
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInterfaces, elemT, null, null, false);
        CollectionType withContentType = collectionType.withContentType(contentType);
        assertEquals(collectionType.getClass(), withContentType.getClass());
        assertEquals(contentType, withContentType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = null;
        JavaType superClass = _bogusSuperClass(rawType);
        JavaType[] superInterfaces = null;
        JavaType elemT = String.class;
        Object typeHandler = new Object();
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInterfaces, elemT, null, typeHandler, false);
        CollectionType withTypeHandler = collectionType.withTypeHandler(typeHandler);
        assertEquals(collectionType.getClass(), withTypeHandler.getClass());
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = null;
        JavaType superClass = _bogusSuperClass(rawType);
        JavaType[] superInterfaces = null;
        JavaType elemT = String.class;
        Object contentTypeHandler = new Object();
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInterfaces, elemT, null, null, false);
        CollectionType withContentTypeHandler = collectionType.withContentTypeHandler(contentTypeHandler);
        assertEquals(collectionType.getClass(), withContentTypeHandler.getClass());
        assertEquals(contentTypeHandler, withContentTypeHandler.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = null;
        JavaType superClass = _bogusSuperClass(rawType);
        JavaType[] superInterfaces = null;
        JavaType elemT = String.class;
        Object valueHandler = new Object();
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInterfaces, elemT, valueHandler, null, false);
        CollectionType withValueHandler = collectionType.withValueHandler(valueHandler);
        assertEquals(collectionType.getClass(), withValueHandler.getClass());
        assertEquals(valueHandler, withValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = null;
        JavaType superClass = _bogusSuperClass(rawType);
        JavaType[] superInterfaces = null;
        JavaType elemT = String.class;
        Object contentValueHandler = new Object();
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInterfaces, elemT, null, null, false);
        CollectionType withContentValueHandler = collectionType.withContentValueHandler(contentValueHandler);
        assertEquals(collectionType.getClass(), withContentValueHandler.getClass());
        assertEquals(contentValueHandler, withContentValueHandler.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = null;
        JavaType superClass = _bogusSuperClass(rawType);
        JavaType[] superInterfaces = null;
        JavaType elemT = String.class;
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInterfaces, elemT, null, null, false);
        CollectionType withStaticTyping = collectionType.withStaticTyping();
        assertEquals(collectionType.getClass(), withStaticTyping.getClass());
        assertTrue(withStaticTyping.isAsStatic());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = null;
        JavaType superClass = _bogusSuperClass(rawType);
        JavaType[] superInterfaces = null;
        JavaType elemT = String.class;
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInterfaces, elemT, null, null, false);
        JavaType refinedType = collectionType.refine(LinkedList.class, bindings, superClass, superInterfaces);
        assertNotNull(refinedType);
        assertTrue(refinedType.isInstance(new LinkedListType()));
    }

    // Edge cases and exception handling tests

    @Test(expected = IllegalArgumentException.class)
    public void testConstructNullRawType() {
        TypeBindings bindings = null;
        JavaType superClass = _bogusSuperClass(null);
        JavaType[] superInterfaces = null;
        JavaType elemT = String.class;
        CollectionType.construct(null, bindings, superClass, superInterfaces, elemT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructNullBindings() {
        Class<?> rawType = ArrayList.class;
        JavaType superClass = _bogusSuperClass(rawType);
        JavaType[] superInterfaces = null;
        JavaType elemT = String.class;
        CollectionType.construct(rawType, null, superClass, superInterfaces, elemT);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructNullSuperClass() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = null;
        JavaType[] superInterfaces = null;
        JavaType elemT = String.class;
        CollectionType.construct(rawType, bindings, null, superInterfaces, elemT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructNullSuperInterfaces() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = null;
        JavaType superClass = _bogusSuperClass(rawType);
        JavaType elemT = String.class;
        CollectionType.construct(rawType, bindings, superClass, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructNullElemT() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = null;
        JavaType superClass = _bogusSuperClass(rawType);
        JavaType[] superInterfaces = null;
        CollectionType.construct(rawType, bindings, superClass, superInterfaces);
    }

    @Test(expected = NullPointerException.class)
    public void testNarrowNullSubclass() {
        Class<?> rawType = ArrayList.class;
        CollectionType collectionType = new CollectionType(rawType, null, _bogusSuperClass(rawType), null, String.class, null, null, false);
        collectionType._narrow(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeNullContentType() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = null;
        JavaType superClass = _bogusSuperClass(rawType);
        JavaType[] superInterfaces = null;
        JavaType elemT = String.class;
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInterfaces, elemT, null, null, false);
        collectionType.withContentType(null);
    }

    @Test(expected = NullPointerException.class)
    public void testWithTypeHandlerNullHandler() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = null;
        JavaType superClass = _bogusSuperClass(rawType);
        JavaType[] superInterfaces = null;
        JavaType elemT = String.class;
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInterfaces, elemT, null, null, false);
        collectionType.withTypeHandler(null);
    }

    @Test(expected = NullPointerException.class)
    public void testWithContentTypeHandlerNullHandler() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = null;
        JavaType superClass = _bogusSuperClass(rawType);
        JavaType[] superInterfaces = null;
        JavaType elemT = String.class;
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInterfaces, elemT, null, null, false);
        collectionType.withContentTypeHandler(null);
    }

    @Test(expected = NullPointerException.class)
    public void testWithValueHandlerNullHandler() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = null;
        JavaType superClass = _bogusSuperClass(rawType);
        JavaType[] superInterfaces = null;
        JavaType elemT = String.class;
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInterfaces, elemT, null, null, false);
        collectionType.withValueHandler(null);
    }

    @Test(expected = NullPointerException.class)
    public void testWithContentValueHandlerNullHandler() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = null;
        JavaType superClass = _bogusSuperClass(rawType);
        JavaType[] superInterfaces = null;
        JavaType elemT = String.class;
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInterfaces, elemT, null, null, false);
        collectionType.withContentValueHandler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithStaticTypingInvalidState() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = null;
        JavaType superClass = _bogusSuperClass(rawType);
        JavaType[] superInterfaces = null;
        JavaType elemT = String.class;
        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInterfaces, elemT, null, null, false);
        collectionType.withStaticTyping();
        collectionType.withStaticTyping();
    }
}