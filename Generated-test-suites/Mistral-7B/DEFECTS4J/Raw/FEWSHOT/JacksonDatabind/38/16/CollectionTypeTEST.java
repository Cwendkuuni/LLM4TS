package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Type;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = ArrayList.class;
        JavaType elemT = IntegerType.getInstance();
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        assertEquals("java.util.ArrayList", collectionType.getRawClass().getName());
        assertEquals(elemT, collectionType.getContentType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testConstructDeprecated() {
        Class<?> rawType = ArrayList.class;
        JavaType elemT = IntegerType.getInstance();

        CollectionType collectionType = CollectionType.construct(rawType, elemT);
        assertEquals("java.util.ArrayList", collectionType.getRawClass().getName());
        assertEquals(elemT, collectionType.getContentType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testWithContentType() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = IntegerType.getInstance();
        JavaType contentType = StringType.getInstance();

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        CollectionType newCollectionType = collectionType.withContentType(contentType);
        assertEquals(contentType, newCollectionType.getContentType());
        assertEquals(elemT, collectionType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = IntegerType.getInstance();
        Object typeHandler = new Object() {
            // Test handler implementation
        };

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        CollectionType newCollectionType = collectionType.withTypeHandler(typeHandler);
        assertEquals(typeHandler, newCollectionType.getTypeHandler());
        assertNull(collectionType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = IntegerType.getInstance();
        Object contentTypeHandler = new Object() {
            // Test handler implementation
        };
        Object typeHandler = new Object() {
            // Test handler implementation
        };

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        CollectionType newCollectionType = collectionType.withContentTypeHandler(contentTypeHandler).withTypeHandler(typeHandler);
        assertEquals(contentTypeHandler, newCollectionType.getValueHandler());
        assertEquals(typeHandler, newCollectionType.getTypeHandler());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = IntegerType.getInstance();
        Object valueHandler = new Object() {
            // Test handler implementation
        };

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        CollectionType newCollectionType = collectionType.withValueHandler(valueHandler);
        assertEquals(valueHandler, newCollectionType.getValueHandler());
        assertNull(collectionType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = IntegerType.getInstance();
        Object contentValueHandler = new Object() {
            // Test handler implementation
        };
        Object valueHandler = new Object() {
            // Test handler implementation
        };

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        CollectionType newCollectionType = collectionType.withContentValueHandler(contentValueHandler).withValueHandler(valueHandler);
        assertEquals(contentValueHandler, newCollectionType.getValueHandler());
        assertEquals(valueHandler, newCollectionType.getValueHandler());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = IntegerType.getInstance();

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        CollectionType newCollectionType = collectionType.withStaticTyping();
        assertTrue(newCollectionType.isStatic());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = IntegerType.getInstance();

        CollectionType collectionType = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        JavaType refinedType = collectionType.refine(LinkedList.class, bindings, ArrayList.class, new JavaType[0]);
        assertNotNull(refinedType);
        assertEquals(LinkedList.class, refinedType.getRawClass());
        assertEquals(elemT, refinedType.getContentType());
        assertNull(refinedType.getValueHandler());
        assertNull(refinedType.getTypeHandler());
        assertFalse(refinedType.isStatic());
    }
}