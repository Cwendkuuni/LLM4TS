package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> listClass = ArrayList.class;
        JavaType elemType = String.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];

        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInts, elemType);
        assertEquals(listClass, collectionType.getRawClass());
        assertEquals(elemType, collectionType.getContentType());
        assertEquals(bindings, collectionType.getTypeBindings());
        assertEquals(superClass, collectionType.getRawType().getSuperclass());
        assertArrayEquals(superInts, collectionType.getSuperInterfaces());

        CollectionType collectionType2 = CollectionType.construct(List.class, bindings, superClass, superInts, elemType);
        assertEquals(List.class, collectionType2.getRawClass());
        assertEquals(elemType, collectionType2.getContentType());
        assertEquals(bindings, collectionType2.getTypeBindings());
        assertEquals(superClass, collectionType2.getRawType().getSuperclass());
        assertArrayEquals(superInts, collectionType2.getSuperInterfaces());
    }

    @Test
    public void testConstructDeprecated() {
        Class<?> listClass = ArrayList.class;
        JavaType elemType = String.class;

        CollectionType collectionType = CollectionType.construct(listClass, elemType);
        assertEquals(listClass, collectionType.getRawClass());
        assertEquals(elemType, collectionType.getContentType());
        assertNull(collectionType.getTypeBindings());
        assertEquals(Object.class, collectionType.getRawType().getSuperclass());
        assertArrayEquals(new JavaType[0], collectionType.getSuperInterfaces());
    }

    @Test
    public void testNarrow() {
        Class<?> listClass = ArrayList.class;
        JavaType elemType = String.class;
        CollectionType collectionType = CollectionType.construct(listClass, elemType);
        Class<?> sublistClass = SubList.class;

        CollectionType subCollectionType = collectionType._narrow(sublistClass);
        assertEquals(sublistClass, subCollectionType.getRawClass());
        assertEquals(elemType, subCollectionType.getContentType());
        assertNull(subCollectionType.getTypeBindings());
        assertEquals(Object.class, subCollectionType.getRawType().getSuperclass());
        assertArrayEquals(new JavaType[0], subCollectionType.getSuperInterfaces());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNarrowDeprecated() {
        Class<?> listClass = ArrayList.class;
        JavaType elemType = String.class;
        CollectionType collectionType = CollectionType.construct(listClass, elemType);
        Class<?> sublistClass = SubList.class;

        collectionType._narrow(sublistClass);
    }

    @Test
    public void testWithContentType() {
        Class<?> listClass = ArrayList.class;
        JavaType elemType = String.class;
        JavaType contentType = Integer.class;
        CollectionType collectionType = CollectionType.construct(listClass, elemType);

        CollectionType newCollectionType = collectionType.withContentType(contentType);
        assertEquals(listClass, newCollectionType.getRawClass());
        assertEquals(contentType, newCollectionType.getContentType());
        assertNull(newCollectionType.getTypeBindings());
        assertEquals(Object.class, newCollectionType.getRawType().getSuperclass());
        assertArrayEquals(new JavaType[0], newCollectionType.getSuperInterfaces());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> listClass = ArrayList.class;
        JavaType elemType = String.class;
        Object handler = new Object() {
            // Stub implementation
        };
        CollectionType collectionType = CollectionType.construct(listClass, elemType);

        CollectionType newCollectionType = collectionType.withTypeHandler(handler);
        assertEquals(listClass, newCollectionType.getRawClass());
        assertEquals(elemType, newCollectionType.getContentType());
        assertNull(newCollectionType.getTypeBindings());
        assertEquals(Object.class, newCollectionType.getRawType().getSuperclass());
        assertArrayEquals(new JavaType[0], newCollectionType.getSuperInterfaces());
        assertEquals(handler, newCollectionType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> listClass = ArrayList.class;
        JavaType elemType = String.class;
        Object handler = new Object() {
            // Stub implementation
        };
        JavaType contentHandler = new JavaType() {
            // Stub implementation
        };
        CollectionType collectionType = CollectionType.construct(listClass, elemType);

        CollectionType newCollectionType = collectionType.withContentTypeHandler(handler);
        assertEquals(listClass, newCollectionType.getRawClass());
        assertEquals(elemType, newCollectionType.getContentType());
        assertNull(newCollectionType.getTypeBindings());
        assertEquals(Object.class, newCollectionType.getRawType().getSuperclass());
        assertArrayEquals(new JavaType[0], newCollectionType.getSuperInterfaces());
        assertEquals(handler, newCollectionType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> listClass = ArrayList.class;
        JavaType elemType = String.class;
        Object handler = new Object() {
            // Stub implementation
        };
        CollectionType collectionType = CollectionType.construct(listClass, elemType);

        CollectionType newCollectionType = collectionType.withValueHandler(handler);
        assertEquals(listClass, newCollectionType.getRawClass());
        assertEquals(elemType, newCollectionType.getContentType());
        assertNull(newCollectionType.getTypeBindings());
        assertEquals(Object.class, newCollectionType.getRawType().getSuperclass());
        assertArrayEquals(new JavaType[0], newCollectionType.getSuperInterfaces());
        assertEquals(handler, newCollectionType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> listClass = ArrayList.class;
        JavaType elemType = String.class;
        Object handler = new Object() {
            // Stub implementation
        };
        JavaType valueHandler = new JavaType() {
            // Stub implementation
        };
        CollectionType collectionType = CollectionType.construct(listClass, elemType);

        CollectionType newCollectionType = collectionType.withContentValueHandler(handler);
        assertEquals(listClass, newCollectionType.getRawClass());
        assertEquals(elemType, newCollectionType.getContentType());
        assertNull(newCollectionType.getTypeBindings());
        assertEquals(Object.class, newCollectionType.getRawType().getSuperclass());
        assertArrayEquals(new JavaType[0], newCollectionType.getSuperInterfaces());
        assertEquals(handler, newCollectionType.getContentValueHandler());
        assertEquals(valueHandler, newCollectionType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> listClass = ArrayList.class;
        JavaType elemType = String.class;
        CollectionType collectionType = CollectionType.construct(listClass, elemType);

        CollectionType newCollectionType = collectionType.withStaticTyping();
        assertEquals(listClass, newCollectionType.getRawClass());
        assertEquals(elemType, newCollectionType.getContentType());
        assertNull(newCollectionType.getTypeBindings());
        assertEquals(listClass, newCollectionType.getRawType().getSuperclass());
        assertArrayEquals(new JavaType[0], newCollectionType.getSuperInterfaces());
        assertTrue(newCollectionType._asStatic);
    }

    @Test
    public void testRefine() {
        Class<?> listClass = ArrayList.class;
        JavaType elemType = String.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[0];

        CollectionType collectionType = CollectionType.construct(listClass, elemType);
        TypeBindings newBindings = TypeBindings.empty();
        JavaType newSuperClass = HashSet.class;
        JavaType[] newSuperInterfaces = new JavaType[] { List.class };

        CollectionType newCollectionType = collectionType.refine(Arrays.asList(newListClass, newBindings, newSuperClass, newSuperInterfaces).toArray(new JavaType[0][]));
        assertEquals(newListClass, newCollectionType.getRawClass());
        assertEquals(elemType, newCollectionType.getContentType());
        assertEquals(newBindings, newCollectionType.getTypeBindings());
        assertEquals(newSuperClass, newCollectionType.getRawType().getSuperclass());
        assertArrayEquals(newSuperInterfaces, newCollectionType.getSuperInterfaces());
        assertFalse(newCollectionType._asStatic);
    }

    @Test
    public void testToString() {
        Class<?> listClass = ArrayList.class;
        JavaType elemType = String.class;
        CollectionType collectionType = CollectionType.construct(listClass, elemType);

        String expected = "[collection type; class " + listClass.getName() + ", contains " + elemType + "]";
        String actual = collectionType.toString();
        assertEquals(expected, actual);
    }
}