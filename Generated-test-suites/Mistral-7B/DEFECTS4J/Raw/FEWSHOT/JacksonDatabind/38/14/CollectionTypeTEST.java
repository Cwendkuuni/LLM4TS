package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        JavaType elemType = new SimpleType(String.class);
        JavaType collectionType = CollectionType.construct(ArrayList.class, null, null, null, elemType);
        assertEquals(ArrayList.class, collectionType.getRawClass());
        assertEquals(ArrayList.class, collectionType.getRawType());
        assertEquals(ArrayList.class, collectionType.getRawClassWithTypeParameters());
        assertEquals(Collection.class, collectionType.getRawTypeWithTypeParameters());
        assertEquals(elemType, collectionType.getContentType());
        assertEquals(elemType, collectionType.getRawContentType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testConstructWithBindingsAndSuperType() {
        JavaType elemType = new SimpleType(String.class);
        JavaType collectionType = CollectionType.construct(ArrayList.class, null,
                new SimpleType(List.class), null, elemType);
        assertEquals(ArrayList.class, collectionType.getRawClass());
        assertEquals(ArrayList.class, collectionType.getRawType());
        assertEquals(ArrayList.class, collectionType.getRawClassWithTypeParameters());
        assertEquals(Collection.class, collectionType.getRawTypeWithTypeParameters());
        assertEquals(elemType, collectionType.getContentType());
        assertEquals(elemType, collectionType.getRawContentType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
        assertEquals(List.class, collectionType.getSuperClass());
        assertArrayEquals(new Class[0], collectionType.getSuperInterfaces());
    }

    @Test
    public void testConstructWithBindingsAndSuperTypeAndInts() {
        JavaType elemType = new SimpleType(String.class);
        JavaType collectionType = CollectionType.construct(ArrayList.class, null,
                new SimpleType(List.class), new JavaType[] {new SimpleType(Integer.class)}, elemType);
        assertEquals(ArrayList.class, collectionType.getRawClass());
        assertEquals(ArrayList.class, collectionType.getRawType());
        assertEquals(ArrayList.class, collectionType.getRawClassWithTypeParameters());
        assertEquals(Collection.class, collectionType.getRawTypeWithTypeParameters());
        assertEquals(elemType, collectionType.getContentType());
        assertEquals(elemType, collectionType.getRawContentType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
        assertEquals(List.class, collectionType.getSuperClass());
        assertArrayEquals(new Class[] {Integer.class}, collectionType.getSuperInterfaces());
    }

    @Test
    public void testConstructWithBindingsOnly() {
        JavaType elemType = new SimpleType(String.class);
        TypeBindings bindings = new TypeBindings();
        JavaType collectionType = CollectionType.construct(ArrayList.class, bindings, null, null, elemType);
        assertEquals(ArrayList.class, collectionType.getRawClass());
        assertEquals(ArrayList.class, collectionType.getRawType());
        assertEquals(ArrayList.class, collectionType.getRawClassWithTypeParameters());
        assertEquals(Collection.class, collectionType.getRawTypeWithTypeParameters());
        assertEquals(elemType, collectionType.getContentType());
        assertEquals(elemType, collectionType.getRawContentType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
        assertEquals(bindings, collectionType.getBindings());
    }

    @Test
    public void testConstructWithElementTypeOnly() {
        JavaType elemType = new SimpleType(String.class);
        JavaType collectionType = CollectionType.construct(ArrayList.class, null, null, null, elemType);
        assertEquals(ArrayList.class, collectionType.getRawClass());
        assertEquals(ArrayList.class, collectionType.getRawType());
        assertEquals(ArrayList.class, collectionType.getRawClassWithTypeParameters());
        assertEquals(Collection.class, collectionType.getRawTypeWithTypeParameters());
        assertEquals(elemType, collectionType.getContentType());
        assertEquals(elemType, collectionType.getRawContentType());
        assertNull(collectionType.getValueHandler());
        assertNull(collectionType.getTypeHandler());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testNarrow() {
        JavaType elemType = new SimpleType(String.class);
        JavaType collectionType = CollectionType.construct(ArrayList.class, null, null, null, elemType);
        JavaType narrowedType = collectionType._narrow(LinkedList.class);
        assertEquals(LinkedList.class, narrowedType.getRawClass());
        assertEquals(LinkedList.class, narrowedType.getRawType());
        assertEquals(LinkedList.class, narrowedType.getRawClassWithTypeParameters());
        assertEquals(Collection.class, narrowedType.getRawTypeWithTypeParameters());
        assertEquals(elemType, narrowedType.getContentType());
        assertEquals(elemType, narrowedType.getRawContentType());
        assertNull(narrowedType.getValueHandler());
        assertNull(narrowedType.getTypeHandler());
        assertFalse(narrowedType.isStatic());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNarrowWithIncompatibleType() {
        JavaType elemType = new SimpleType(String.class);
        JavaType collectionType = CollectionType.construct(ArrayList.class, null, null, null, elemType);
        collectionType._narrow(HashMap.class);
    }

    @Test
    public void testWithContentType() {
        JavaType elemType = new SimpleType(String.class);
        JavaType collectionType = CollectionType.construct(ArrayList.class, null, null, null, elemType);
        JavaType newContentType = new SimpleType(Integer.class);
        JavaType newCollectionType = collectionType.withContentType(newContentType);
        assertEquals(ArrayList.class, newCollectionType.getRawClass());
        assertEquals(ArrayList.class, newCollectionType.getRawType());
        assertEquals(ArrayList.class, newCollectionType.getRawClassWithTypeParameters());
        assertEquals(Collection.class, newCollectionType.getRawTypeWithTypeParameters());
        assertEquals(newContentType, newCollectionType.getContentType());
        assertEquals(elemType, newCollectionType.getRawContentType());
        assertNull(newCollectionType.getValueHandler());
        assertNull(newCollectionType.getTypeHandler());
        assertFalse(newCollectionType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType elemType = new SimpleType(String.class);
        JavaType collectionType = CollectionType.construct(ArrayList.class, null, null, null, elemType);
        Object handler = new Object();
        JavaType newCollectionType = collectionType.withTypeHandler(handler);
        assertEquals(ArrayList.class, newCollectionType.getRawClass());
        assertEquals(ArrayList.class, newCollectionType.getRawType());
        assertEquals(ArrayList.class, newCollectionType.getRawClassWithTypeParameters());
        assertEquals(Collection.class, newCollectionType.getRawTypeWithTypeParameters());
        assertEquals(elemType, newCollectionType.getContentType());
        assertEquals(elemType, newCollectionType.getRawContentType());
        assertEquals(handler, newCollectionType.getTypeHandler());
        assertNull(newCollectionType.getValueHandler());
        assertFalse(newCollectionType.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType elemType = new SimpleType(String.class);
        JavaType collectionType = CollectionType.construct(ArrayList.class, null, null, null, elemType);
        Object contentHandler = new Object();
        Object typeHandler = new Object();
        JavaType newCollectionType = collectionType.withContentTypeHandler(contentHandler).withTypeHandler(typeHandler);
        assertEquals(ArrayList.class, newCollectionType.getRawClass());
        assertEquals(ArrayList.class, newCollectionType.getRawType());
        assertEquals(ArrayList.class, newCollectionType.getRawClassWithTypeParameters());
        assertEquals(Collection.class, newCollectionType.getRawTypeWithTypeParameters());
        assertEquals(elemType, newCollectionType.getContentType());
        assertEquals(elemType, newCollectionType.getRawContentType());
        assertEquals(contentHandler, newCollectionType.getValueHandler());
        assertEquals(typeHandler, newCollectionType.getTypeHandler());
        assertFalse(newCollectionType.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        JavaType elemType = new SimpleType(String.class);
        JavaType collectionType = CollectionType.construct(ArrayList.class, null, null, null, elemType);
        Object handler = new Object();
        JavaType newCollectionType = collectionType.withValueHandler(handler);
        assertEquals(ArrayList.class, newCollectionType.getRawClass());
        assertEquals(ArrayList.class, newCollectionType.getRawType());
        assertEquals(ArrayList.class, newCollectionType.getRawClassWithTypeParameters());
        assertEquals(Collection.class, newCollectionType.getRawTypeWithTypeParameters());
        assertEquals(elemType, newCollectionType.getContentType());
        assertEquals(elemType, newCollectionType.getRawContentType());
        assertEquals(handler, newCollectionType.getValueHandler());
        assertNull(newCollectionType.getTypeHandler());
        assertFalse(newCollectionType.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType elemType = new SimpleType(String.class);
        JavaType collectionType = CollectionType.construct(ArrayList.class, null, null, null, elemType);
        Object contentHandler = new Object();
        Object valueHandler = new Object();
        JavaType newCollectionType = collectionType.withContentValueHandler(contentHandler).withValueHandler(valueHandler);
        assertEquals(ArrayList.class, newCollectionType.getRawClass());
        assertEquals(ArrayList.class, newCollectionType.getRawType());
        assertEquals(ArrayList.class, newCollectionType.getRawClassWithTypeParameters());
        assertEquals(Collection.class, newCollectionType.getRawTypeWithTypeParameters());
        assertEquals(elemType, newCollectionType.getContentType());
        assertEquals(elemType, newCollectionType.getRawContentType());
        assertEquals(valueHandler, newCollectionType.getValueHandler());
        assertEquals(contentHandler, newCollectionType.getValueHandler());
        assertFalse(newCollectionType.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType elemType = new SimpleType(String.class);
        JavaType collectionType = CollectionType.construct(ArrayList.class, null, null, null, elemType);
        JavaType newCollectionType = collectionType.withStaticTyping();
        assertEquals(ArrayList.class, newCollectionType.getRawClass());
        assertEquals(ArrayList.class, newCollectionType.getRawType());
        assertEquals(ArrayList.class, newCollectionType.getRawClassWithTypeParameters());
        assertEquals(Collection.class, newCollectionType.getRawTypeWithTypeParameters());
        assertEquals(elemType, newCollectionType.getContentType());
        assertEquals(elemType, newCollectionType.getRawContentType());
        assertNull(newCollectionType.getValueHandler());
        assertNull(newCollectionType.getTypeHandler());
        assertTrue(newCollectionType.isStatic());
    }

    @Test
    public void testRefine() {
        JavaType elemType = new SimpleType(String.class);
        JavaType collectionType = CollectionType.construct(ArrayList.class, null, null, null, elemType);
        JavaType newCollectionType = collectionType.refine(LinkedList.class, null, null, null);
        assertEquals(LinkedList.class, newCollectionType.getRawClass());
        assertEquals(LinkedList.class, newCollectionType.getRawType());
        assertEquals(LinkedList.class, newCollectionType.getRawClassWithTypeParameters());
        assertEquals(Collection.class, newCollectionType.getRawTypeWithTypeParameters());
        assertEquals(elemType, newCollectionType.getContentType());
        assertEquals(elemType, newCollectionType.getRawContentType());
        assertNull(newCollectionType.getValueHandler());
        assertNull(newCollectionType.getTypeHandler());
        assertFalse(newCollectionType.isStatic());
    }
}