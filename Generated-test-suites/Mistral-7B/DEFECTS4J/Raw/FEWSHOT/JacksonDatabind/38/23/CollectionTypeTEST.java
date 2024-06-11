package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        JavaType elemType = new SimpleType(String.class);
        Type superclass = ArrayList.class;
        JavaType superInterfaces = new ArrayType(List.class, Long.TYPE);
        CollectionType collectionType = CollectionType.construct((Class<?>) ArrayList.class, null, superclass, superInterfaces, elemType);
        assertEquals(ArrayList.class, collectionType.getRawClass());
        assertEquals(elemType, collectionType.getContentType());
        assertNotNull(collectionType.getBindings());
        assertEquals(superclass, collectionType.getSuperClass());
        assertArrayEquals(new JavaType[] { superInterfaces }, collectionType.getSuperInterfaces());
    }

    @Test
    public void testConstructWithBindings() {
        JavaType elemType = new SimpleType(String.class);
        Type superclass = ArrayList.class;
        JavaType superInterfaces = new ArrayType(List.class, Long.TYPE);
        TypeBindings bindings = new TypeBindings();
        CollectionType collectionType = CollectionType.construct((Class<?>) ArrayList.class, bindings, superclass, superInterfaces, elemType);
        assertEquals(ArrayList.class, collectionType.getRawClass());
        assertEquals(elemType, collectionType.getContentType());
        assertNotNull(collectionType.getBindings());
        assertEquals(superclass, collectionType.getSuperClass());
        assertArrayEquals(new JavaType[] { superInterfaces }, collectionType.getSuperInterfaces());
    }

    @Test
    public void testConstructWithBogusSuperClass() {
        JavaType elemType = new SimpleType(String.class);
        CollectionType collectionType = CollectionType.construct((Class<?>) ArrayList.class, null, _bogusSuperClass(ArrayList.class), null, elemType);
        assertEquals(ArrayList.class, collectionType.getRawClass());
        assertEquals(elemType, collectionType.getContentType());
        assertNotNull(collectionType.getBindings());
        assertNull(collectionType.getSuperClass());
        assertArrayEquals(new JavaType[] {}, collectionType.getSuperInterfaces());
    }

    @Test
    public void testNarrow() {
        JavaType listType = CollectionType.construct((Class<?>) ArrayList.class, null, ArrayList.class, null, new SimpleType(Integer.class));
        CollectionType arrayListType = (CollectionType) listType;
        JavaType linkedListType = CollectionType.construct((Class<?>) LinkedList.class, null, ArrayList.class, null, new SimpleType(Integer.class));
        CollectionType linkedListNarrowed = arrayListType._narrow(LinkedList.class);
        assertEquals(linkedListType, linkedListNarrowed);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNarrowDeprecated() {
        JavaType listType = CollectionType.construct((Class<?>) ArrayList.class, null, ArrayList.class, null, new SimpleType(Integer.class));
        ((CollectionType) listType)._narrow(LinkedList.class);
    }

    @Test
    public void testWithContentType() {
        JavaType listType = CollectionType.construct((Class<?>) ArrayList.class, null, ArrayList.class, null, new SimpleType(Integer.class));
        JavaType listWithStringContentType = listType.withContentType(new SimpleType(String.class));
        assertEquals(new SimpleType(ArrayList.class), listType.getRawType());
        assertEquals(new SimpleType(Integer.class), listType.getContentType());
        assertEquals(new SimpleType(String.class), listWithStringContentType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType listType = CollectionType.construct((Class<?>) ArrayList.class, null, ArrayList.class, null, new SimpleType(Integer.class));
        Object handler = new Object();
        CollectionType listWithHandler = listType.withTypeHandler(handler);
        assertEquals(listType, listType);
        assertEquals(handler, listWithHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType listType = CollectionType.construct((Class<?>) ArrayList.class, null, ArrayList.class, null, new SimpleType(Integer.class));
        Object handler = new Object();
        JavaType stringType = new SimpleType(String.class);
        CollectionType listWithHandlers = listType.withContentTypeHandler(handler).withTypeHandler(handler).withContentType(stringType);
        assertEquals(stringType, listWithHandlers.getContentType());
    }

    @Test
    public void testWithValueHandler() {
        JavaType listType = CollectionType.construct((Class<?>) ArrayList.class, null, ArrayList.class, null, new SimpleType(Integer.class));
        Object handler = new Object();
        CollectionType listWithHandler = listType.withValueHandler(handler);
        assertEquals(listType, listType);
        assertEquals(handler, listWithHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType listType = CollectionType.construct((Class<?>) ArrayList.class, null, ArrayList.class, null, new SimpleType(Integer.class));
        Object handler = new Object();
        JavaType stringType = new SimpleType(String.class);
        CollectionType listWithHandlers = listType.withContentValueHandler(handler).withValueHandler(handler).withContentType(stringType);
        assertEquals(stringType, listWithHandlers.getContentType());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType listType = CollectionType.construct((Class<?>) ArrayList.class, null, ArrayList.class, null, new SimpleType(Integer.class));
        CollectionType listWithStaticTyping = listType.withStaticTyping();
        assertEquals(listType, listType);
        assertTrue(listWithStaticTyping._asStatic);
    }

    @Test
    public void testRefine() {
        JavaType listType = CollectionType.construct((Class<?>) ArrayList.class, null, ArrayList.class, null, new SimpleType(Integer.class));
        JavaType listWithSubclass = CollectionType.construct((Class<?>) LinkedList.class, null, ArrayList.class, null, new SimpleType(Integer.class));
        CollectionType refinedList = listType.refine(LinkedList.class, null, null, null);
        assertEquals(listWithSubclass, refinedList);
    }

    private static Class<?> _bogusSuperClass(Class<?> rawType) {
        throw new UnsupportedOperationException("This method should not be called");
    }

    private static void assertArrayEquals(JavaType[] expected, JavaType[] actual) {
        if (!expected.length == actual.length) {
            throw new AssertionError("Array lengths don't match: expected=" + expected.length + ", actual=" + actual.length);
        }
        for (int i = 0; i < expected.length; i++) {
            if (!expected[i].equals(actual[i])) {
                throw new AssertionError("Array elements don't match at index " + i + ": expected=" + expected[i] + ", actual=" + actual[i]);
            }
        }
    }
}