package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        // construct(Class<?> rawType, TypeBindings bindings, JavaType superClass, JavaType[] superInts, JavaType elemT)
        Class<?> rawType = Collection.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = {};
        JavaType elemT = null;
        CollectionLikeType type = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
        assertNotNull(type);
        assertEquals(rawType, type.getRawClass());
        assertEquals(bindings, type.getBindings());
        assertEquals(superClass, type.getSuperClass());
        assertArrayEquals(superInts, type.getInterfaces().toArray());
        assertEquals(elemT, type.getContentType());

        // test long parameters
        rawType = StringBuilder.class;
        superClass = null;
        elemT = null;
        type = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
        assertNotNull(type);
        assertEquals(rawType, type.getRawClass());
        assertEquals(bindings, type.getBindings());
        assertEquals(superClass, type.getSuperClass());
        assertArrayEquals(superInts, type.getInterfaces().toArray());
        assertEquals(elemT, type.getContentType());

        // test empty bindings
        rawType = Collection.class;
        bindings = TypeBindings.emptyBindings();
        superClass = null;
        superInts = new JavaType[]{};
        elemT = null;
        type = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
        assertNotNull(type);
        assertEquals(rawType, type.getRawClass());
        assertEquals(bindings, type.getBindings());
        assertEquals(superClass, type.getSuperClass());
        assertArrayEquals(superInts, type.getInterfaces().toArray());
        assertEquals(elemT, type.getContentType());
    }

    @Test
    public void testConstruct_Dependency() {
        // TODO: implement test case
        fail("Not implemented yet");
    }

    @Test
    public void testConstruct_ContentElemT() {
        // TODO: implement test case
        fail("Not implemented yet");
    }

    @Test
    public void testUpgradeFrom() {
        // TODO: implement test case
        fail("Not implemented yet");
    }

    @Test
    public void test_contentType() {
        // TODO: implement test case
        fail("Not implemented yet");
    }

    @Test
    public void test_isTrueCollectionType() {
        // TODO: implement test case
        fail("Not implemented yet");
    }

    @Test
    public void test_equals() {
        // TODO: implement test case
        fail("Not implemented yet");
    }

    @Test
    public void test_toString() {
        // TODO: implement test case
        fail("Not implemented yet");
    }

}