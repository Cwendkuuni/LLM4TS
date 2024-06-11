package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<List<String>> listClass = new ArrayList<>() {@SuppressWarnings("unchecked")
            public Class<List<String>> getClass() {
                return (Class<List<String>>) List.class;
            }
        };
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemT = String.class;

        CollectionType collectionType = CollectionType.construct(listClass, bindings, superClass, superInterfaces, elemT);

        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getTypeBindings());
        assertEquals(superClass, collectionType.getSuperClass());
        assertArrayEquals(superInterfaces, collectionType.getSuperInterfaces());
        assertEquals(elemT, collectionType.getContentType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testConstructDeprecated() {
        Class<List<String>> listClass = new ArrayList<>() {@SuppressWarnings("unchecked")
            public Class<List<String>> getClass() {
                return (Class<List<String>>) List.class;
            }
        };
        JavaType elemT = String.class;

        CollectionType collectionType = CollectionType.construct(listClass, elemT);

        assertEquals(List.class, collectionType.getRawClass());
        assertNull(collectionType.getTypeBindings());
        assertNull(collectionType.getSuperClass());
        assertArrayEquals(new JavaType[0], collectionType.getSuperInterfaces());
        assertEquals(elemT, collectionType.getContentType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testRefine() {
        Class<List<String>> listClass = new ArrayList<>() {@SuppressWarnings("unchecked")
            public Class<List<String>> getClass() {
                return (Class<List<String>>) List.class;
            }
        };
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemT = String.class;

        CollectionType collectionType = new CollectionType(listClass, bindings, superClass, superInterfaces, elemT);

        CollectionType refinedCollectionType = collectionType.refine(List.class, null, null, new JavaType[0]);

        assertEquals(List.class, refinedCollectionType.getRawClass());
        assertNull(refinedCollectionType.getTypeBindings());
        assertNull(refinedCollectionType.getSuperClass());
        assertArrayEquals(new JavaType[0], refinedCollectionType.getSuperInterfaces());
        assertEquals(elemT, refinedCollectionType.getContentType());
        assertNull(refinedCollectionType.getValueHandler());
        assertNull(refinedCollectionType.getTypeHandler());
        assertFalse(refinedCollectionType.isStatic());

        CollectionType refinedCollectionType2 = collectionType.refine(ArrayList.class, null, null, new JavaType[0]);

        assertEquals(ArrayList.class, refinedCollectionType2.getRawClass());
        assertNull(refinedCollectionType2.getTypeBindings());
        assertNull(refinedCollectionType2.getSuperClass());
        assertArrayEquals(new JavaType[0], refinedCollectionType2.getSuperInterfaces());
        assertEquals(elemT, refinedCollectionType2.getContentType());
        assertNull(refinedCollectionType2.getValueHandler());
        assertNull(refinedCollectionType2.getTypeHandler());
        assertFalse(refinedCollectionType2.isStatic());
    }

    @Test
    public void testToString() {
        Class<List<String>> listClass = new ArrayList<>() {@SuppressWarnings("unchecked")
            public Class<List<String>> getClass() {
                return (Class<List<String>>) List.class;
            }
        };
        JavaType elemT = String.class;

        CollectionType collectionType = new CollectionType(listClass, null, null, new JavaType[0], elemT);

        assertEquals("[collection type; class java.util.List, contains class java.lang.String]", collectionType.toString());
    }
}