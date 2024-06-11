
package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        // Typical use case
        CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        assertNotNull(collectionType);
        // Edge case
        collectionType = CollectionType.construct(ArrayList.class, null);
        assertNotNull(collectionType);
    }

    @Test
    public void testNarrow() {
        // Typical use case
        CollectionType collectionType = new CollectionType(ArrayList.class, new TypeBindings(), null, null, String.class, null, null, false);
        JavaType narrowedType = collectionType._narrow(LinkedList.class);
        assertNotNull(narrowedType);
        assertEquals(LinkedList.class, narrowedType.getRawClass());
    }

    @Test
    public void testWithContentType() {
        // Typical use case
        CollectionType collectionType = new CollectionType(ArrayList.class, new TypeBindings(), null, null, String.class, null, null, false);
        JavaType modifiedType = collectionType.withContentType(Integer.class);
        assertNotNull(modifiedType);
        assertEquals(ArrayList.class, modifiedType.getRawClass());
        assertEquals(Integer.class, modifiedType.getContentType().getRawClass());
    }

    @Test
    public void testWithTypeHandler() {
        // Typical use case
        CollectionType collectionType = new CollectionType(ArrayList.class, new TypeBindings(), null, null, String.class, null, null, false);
        CollectionType modifiedType = collectionType.withTypeHandler(new CustomTypeHandler());
        assertNotNull(modifiedType);
        assertEquals(ArrayList.class, modifiedType.getRawClass());
        assertNotNull(modifiedType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        // Typical use case
        CollectionType collectionType = new CollectionType(ArrayList.class, new TypeBindings(), null, null, String.class, null, null, false);
        CollectionType modifiedType = collectionType.withContentTypeHandler(new CustomTypeHandler());
        assertNotNull(modifiedType);
        assertEquals(ArrayList.class, modifiedType.getRawClass());
        assertNotNull(modifiedType.getContentType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        // Typical use case
        CollectionType collectionType = new CollectionType(ArrayList.class, new TypeBindings(), null, null, String.class, null, null, false);
        CollectionType modifiedType = collectionType.withValueHandler(new CustomValueHandler());
        assertNotNull(modifiedType);
        assertEquals(ArrayList.class, modifiedType.getRawClass());
        assertNotNull(modifiedType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        // Typical use case
        CollectionType collectionType = new CollectionType(ArrayList.class, new TypeBindings(), null, null, String.class, null, null, false);
        CollectionType modifiedType = collectionType.withContentValueHandler(new CustomValueHandler());
        assertNotNull(modifiedType);
        assertEquals(ArrayList.class, modifiedType.getRawClass());
        assertNotNull(modifiedType.getContentType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        // Typical use case
        CollectionType collectionType = new CollectionType(ArrayList.class, new TypeBindings(), null, null, String.class, null, null, false);
        CollectionType modifiedType = collectionType.withStaticTyping();
        assertNotNull(modifiedType);
        assertEquals(ArrayList.class, modifiedType.getRawClass());
        assertTrue(modifiedType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        // Typical use case
        CollectionType collectionType = new CollectionType(ArrayList.class, new TypeBindings(), null, null, String.class, null, null, false);
        JavaType refinedType = collectionType.refine(LinkedList.class, new TypeBindings(), null, null);
        assertNotNull(refinedType);
        assertEquals(LinkedList.class, refinedType.getRawClass());
    }

    @Test
    public void testToString() {
        CollectionType collectionType = new CollectionType(ArrayList.class, new TypeBindings(), null, null, String.class, null, null, false);
        String toString = collectionType.toString();
        assertNotNull(toString);
        assertTrue(toString.contains(ArrayList.class.getName()));
        assertTrue(toString.contains(String.class.getName()));
    }

}

class CustomTypeHandler {}

class CustomValueHandler {}

