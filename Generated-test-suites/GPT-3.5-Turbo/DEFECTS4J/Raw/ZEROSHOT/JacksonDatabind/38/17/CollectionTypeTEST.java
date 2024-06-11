package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class CollectionTypeTest {

    /**
     * Test the constructor with parameters:
     * CollectionType(Class<?> collT, TypeBindings bindings,
            JavaType superClass, JavaType[] superInts, JavaType elemT,
            Object valueHandler, Object typeHandler, boolean asStatic)
     */
    @Test
    public void testConstructorWithParameters() {
        Class<?> collT = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        CollectionType collectionType = new CollectionType(collT, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);

        assertNotNull(collectionType);
    }

    /**
     * Test the protected constructor with parameters:
     * CollectionType(TypeBase base, JavaType elemT)
     */
    @Test
    public void testProtectedConstructorWithParameters() {
        TypeBase base = null;
        JavaType elemT = null;

        CollectionType collectionType = new CollectionType(base, elemT);

        assertNotNull(collectionType);
    }

    /**
     * Test the static method construct(Class<?> rawType, TypeBindings bindings,
            JavaType superClass, JavaType[] superInts, JavaType elemT)
     */
    @Test
    public void testConstruct() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);

        assertNotNull(collectionType);
    }

    /**
     * Test the deprecated static method construct(Class<?> rawType, JavaType elemT)
     */
    @Test
    public void testDeprecatedConstruct() {
        Class<?> rawType = null;
        JavaType elemT = null;

        CollectionType collectionType = CollectionType.construct(rawType, elemT);

        assertNotNull(collectionType);
    }

    /**
     * Test the protected method _narrow(Class<?> subclass)
     */
    @Test
    public void testNarrow() {
        Class<?> subclass = null;

        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        JavaType result = collectionType._narrow(subclass);

        assertNotNull(result);
    }

    /**
     * Test the method withContentType(JavaType contentType)
     */
    @Test
    public void testWithContentType() {
        JavaType contentType = null;

        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        JavaType result = collectionType.withContentType(contentType);

        assertNotNull(result);
    }

    /**
     * Test the method withTypeHandler(Object h)
     */
    @Test
    public void testWithTypeHandler() {
        Object h = null;

        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = collectionType.withTypeHandler(h);

        assertNotNull(result);
    }

    /**
     * Test the method withContentTypeHandler(Object h)
     */
    @Test
    public void testWithContentTypeHandler() {
        Object h = null;

        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = collectionType.withContentTypeHandler(h);

        assertNotNull(result);
    }

    /**
     * Test the method withValueHandler(Object h)
     */
    @Test
    public void testWithValueHandler() {
        Object h = null;

        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = collectionType.withValueHandler(h);

        assertNotNull(result);
    }

    /**
     * Test the method withContentValueHandler(Object h)
     */
    @Test
    public void testWithContentValueHandler() {
        Object h = null;

        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = collectionType.withContentValueHandler(h);

        assertNotNull(result);
    }

    /**
     * Test the method withStaticTyping()
     */
    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        CollectionType result = collectionType.withStaticTyping();

        assertNotNull(result);
    }

    /**
     * Test the method refine(Class<?> rawType, TypeBindings bindings,
            JavaType superClass, JavaType[] superInterfaces)
     */
    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;

        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        JavaType result = collectionType.refine(rawType, bindings, superClass, superInterfaces);

        assertNotNull(result);
    }

    /**
     * Test the method toString()
     */
    @Test
    public void testToString() {
        CollectionType collectionType = new CollectionType(null, null, null, null, null, null, null, false);
        String result = collectionType.toString();

        assertNotNull(result);
    }
}