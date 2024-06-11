package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        // Test construct method with valid arguments
        JavaType elemType = null; // add a valid JavaType argument here
        Class<?> collectionType = CollectionType.class;
        TypeBindings bindings = null;
        JavaType superClass = null; // add a valid JavaType argument here
        JavaType[] superInts = null; // add valid JavaType[] arguments here
        CollectionType expected = new CollectionType(collectionType, bindings, superClass, superInts, elemType);

        CollectionType actual = CollectionType.construct(collectionType, bindings, superClass, superInts, elemType);

        assertEquals(expected, actual);

        // Test construct method with null arguments
        elemType = null;
        collectionType = null;
        bindings = null;
        superClass = null;
        superInts = null;

        expected = null;

        actual = CollectionType.construct(collectionType, bindings, superClass, superInts, elemType);

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructDeprecatedNullElemType() {
        // Test construct method with null elemType and valid arguments other than null
        JavaType superClass = null; // add a valid JavaType argument here
        JavaType[] superInts = null; // add valid JavaType[] arguments here
        Class<?> collectionType = CollectionType.class;
        TypeBindings bindings = null;

        CollectionType.construct(collectionType, bindings, superClass, superInts, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructDeprecatedNullCollectionType() {
        // Test construct method with null collectionType and valid arguments other than null
        JavaType elemType = null;
        JavaType[] superInts = null; // add valid JavaType[] arguments here
        TypeBindings bindings = null;
        JavaType superClass = null; // add a valid JavaType argument here

        CollectionType.construct(null, bindings, superClass, superInts, elemType);
    }

    @Test
    public void testWithContentType() {
        // Test withContentType method with valid arguments
        JavaType elemType = null;
        JavaType collectionType = CollectionType.class;
        JavaType contentType = null; // add a valid JavaType argument here
        TypeBindings bindings = null;
        JavaType[] superInterfaces = null; // add valid JavaType[] arguments here
        JavaType superClass = null; // add a valid JavaType argument here

        CollectionType expected = new CollectionType(collectionType, bindings, superClass, superInterfaces, elemType);
        CollectionType actual = expected.withContentType(contentType);

        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected.withContentType(contentType)._elementType, actual._elementType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeNull() {
        // Test withContentType method with null JavaType argument
        JavaType elemType = null;
        JavaType collectionType = CollectionType.class;
        TypeBindings bindings = null;
        JavaType[] superInterfaces = null; // add valid JavaType[] arguments here
        JavaType superClass = null; // add a valid JavaType argument here

        CollectionType actual = new CollectionType(collectionType, bindings, superClass, superInterfaces, elemType);

        actual = actual.withContentType(null);
    }

    @Test
    public void testWithTypeHandler() {
        // Test withTypeHandler method with valid arguments
        JavaType elemType = null;
        JavaType collectionType = CollectionType.class;
        Object typeHandler = null; // add a valid Object argument here
        TypeBindings bindings = null;
        JavaType[] superInterfaces = null; // add valid JavaType[] arguments here
        JavaType superClass = null; // add a valid JavaType argument here

        CollectionType expected = new CollectionType(collectionType, bindings, superClass, superInterfaces, elemType);
        CollectionType actual = expected.withTypeHandler(typeHandler);

        assertEquals(expected.getClass(), actual.getClass());
        assertNotSame(expected._typeHandler, actual._typeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        // Test withContentTypeHandler method with valid arguments
        JavaType elemType = null;
        JavaType collectionType = CollectionType.class;
        Object contentTypeHandler = null; // add a valid Object argument here
        TypeBindings bindings = null;
        JavaType[] superInterfaces = null; // add valid JavaType[] arguments here
        JavaType superClass = null; // add a valid JavaType argument here

        CollectionType expected = new CollectionType(collectionType, bindings, superClass, superInterfaces, elemType);
        CollectionType actual = expected.withContentTypeHandler(contentTypeHandler);

        assertEquals(expected.getClass(), actual.getClass());
        assertNotSame(expected._typeHandler, actual._typeHandler);
        assertEquals(expected._elementType.getClass(), actual._elementType.getClass());
        assertNotSame(expected._elementType._typeHandler, actual._elementType._typeHandler);
    }

    @Test
    public void testWithValueHandler() {
        // Test withValueHandler method with valid arguments
        JavaType elemType = null;
        JavaType collectionType = CollectionType.class;
        Object valueHandler = null; // add a valid Object argument here
        TypeBindings bindings = null;
        JavaType[] superInterfaces = null; // add valid JavaType[] arguments here
        JavaType superClass = null; // add a valid JavaType argument here

        CollectionType expected = new CollectionType(collectionType, bindings, superClass, superInterfaces, elemType);
        CollectionType actual = expected.withValueHandler(valueHandler);

        assertEquals(expected.getClass(), actual.getClass());
        assertNotSame(expected._valueHandler, actual._valueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        // Test withContentValueHandler method with valid arguments
        JavaType elemType = null;
        JavaType collectionType = CollectionType.class;
        Object contentValueHandler = null; // add a valid Object argument here
        TypeBindings bindings = null;
        JavaType[] superInterfaces = null; // add valid JavaType[] arguments here
        JavaType superClass = null; // add a valid JavaType argument here

        CollectionType expected = new CollectionType(collectionType, bindings, superClass, superInterfaces, elemType);
        CollectionType actual = expected.withContentValueHandler(contentValueHandler);

        assertEquals(expected.getClass(), actual.getClass());
        assertNotSame(expected._valueHandler, actual._valueHandler);
        assertEquals(expected._elementType.getClass(), actual._elementType.getClass());
        assertNotSame(expected._elementType._valueHandler, actual._elementType._valueHandler);
    }

    @Test
    public void testWithStaticTyping() {
        // Test withStaticTyping method with valid arguments
        JavaType elemType = null;
        JavaType collectionType = CollectionType.class;
        TypeBindings bindings = null;
        JavaType[] superInterfaces = null; // add valid JavaType[] arguments here
        JavaType superClass = null; // add a valid JavaType argument here

        CollectionType expected = new CollectionType(collectionType, bindings, superClass, superInterfaces, elemType);
        CollectionType actual = expected.withStaticTyping();

        assertEquals(expected.getClass(), actual.getClass());
        assertTrue(expected._asStatic != actual._asStatic);
    }

    @Test
    public void testRefine() {
        // Test refine method with valid arguments
        JavaType elemType = null;
        JavaType collectionType = CollectionType.class;
        TypeBindings bindings = null;
        JavaType superClass = null; // add a valid JavaType argument here
        JavaType[] superInterfaces = null; // add valid JavaType[] arguments here

        CollectionType expected = new CollectionType(collectionType, bindings, superClass, superInterfaces, elemType);
        CollectionType actual = expected.refine(CollectionType.class, bindings, superClass, superInterfaces);

        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected._class, actual._class);
        assertEquals(expected._bindings, actual._bindings);
        assertEquals(expected._superInterfaces, actual._superInterfaces);
        assertEquals(expected._superClass, actual._superClass);
        assertEquals(expected._elementType, actual._elementType);
        assertEquals(expected._valueHandler, actual._valueHandler);
        assertEquals(expected._typeHandler, actual._typeHandler);
        assertEquals(expected._asStatic, actual._asStatic);
    }

    @Test
    public void testToString() {
        // Test toString method
        JavaType elemType = Integer.TYPE;
        JavaType collectionType = CollectionType.class;
        TypeBindings bindings = null;
        JavaType[] superInterfaces = null; // add valid JavaType[] arguments here
        JavaType superClass = null; // add a valid JavaType argument here

        CollectionType ct = new CollectionType(collectionType, bindings, superClass, superInterfaces, elemType);

        String expected = "[collection type; class " + collectionType.getName() + ", contains " + elemType + "]";

        String actual = ct.toString();

        assertEquals(expected, actual);
    }
}