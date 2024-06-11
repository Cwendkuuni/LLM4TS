package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        // Typical use case
        CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        assertNotNull(collectionType);

        // Edge case: rawType is null
        try {
            collectionType = CollectionType.construct(null, new TypeBindings(), null, null, String.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("rawType cannot be null", e.getMessage());
        }

        // Error scenario: elemT is null
        try {
            collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("elemT cannot be null", e.getMessage());
        }
    }

    @Test
    public void testConstruct_Deprecated() {
        // Typical use case
        CollectionType collectionType = CollectionType.construct(ArrayList.class, String.class);
        assertNotNull(collectionType);

        // Edge case: rawType is null
        try {
            collectionType = CollectionType.construct(null, String.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("rawType cannot be null", e.getMessage());
        }

        // Error scenario: elemT is null
        try {
            collectionType = CollectionType.construct(ArrayList.class, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("elemT cannot be null", e.getMessage());
        }
    }

    @Test
    public void test_narrow() {
        // Typical use case
        CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        CollectionType narrowedType = collectionType._narrow(LinkedList.class);
        assertNotNull(narrowedType);
        assertEquals(LinkedList.class, narrowedType.getRawClass());

        // Edge case: subclass is null
        try {
            narrowedType = collectionType._narrow(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("subclass cannot be null", e.getMessage());
        }
    }

    @Test
    public void test_withContentType() {
        // Typical use case
        CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        JavaType contentType = new SimpleType(Object.class);
        CollectionType modifiedType = collectionType.withContentType(contentType);
        assertNotNull(modifiedType);
        assertEquals(contentType, modifiedType.getContentType());

        // Edge case: new contentType is the same as current
        modifiedType = collectionType.withContentType(collectionType.getContentType());
        assertSame(collectionType, modifiedType);
    }

    @Test
    public void test_withTypeHandler() {
        // Typical use case
        CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        Object typeHandler = new TypeHandler();
        CollectionType modifiedType = collectionType.withTypeHandler(typeHandler);
        assertNotNull(modifiedType);
        assertEquals(typeHandler, modifiedType.getTypeHandler());
    }

    @Test
    public void test_withContentTypeHandler() {
        // Typical use case
        CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        Object contentTypeHandler = new TypeHandler();
        CollectionType modifiedType = collectionType.withContentTypeHandler(contentTypeHandler);
        assertNotNull(modifiedType);
        assertEquals(contentTypeHandler, modifiedType.getContentType().getTypeHandler());
    }

    @Test
    public void test_withValueHandler() {
        // Typical use case
        CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        Object valueHandler = new ValueHandler();
        CollectionType modifiedType = collectionType.withValueHandler(valueHandler);
        assertNotNull(modifiedType);
        assertEquals(valueHandler, modifiedType.getValueHandler());
    }

    @Test
    public void test_withContentValueHandler() {
        // Typical use case
        CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        Object contentValueHandler = new ValueHandler();
        CollectionType modifiedType = collectionType.withContentValueHandler(contentValueHandler);
        assertNotNull(modifiedType);
        assertEquals(contentValueHandler, modifiedType.getContentType().getValueHandler());
    }

    @Test
    public void test_withStaticTyping() {
        // Typical use case: current asStatic is false
        CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        CollectionType modifiedType = collectionType.withStaticTyping();
        assertNotNull(modifiedType);
        assertTrue(modifiedType.isStaticTyping());

        // Edge case: current asStatic is true
        modifiedType = modifiedType.withStaticTyping();
        assertSame(collectionType, modifiedType);
    }

    @Test
    public void test_refine() {
        // Typical use case
        CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        JavaType superClass = new SimpleType(AbstractList.class);
        JavaType[] superInterfaces = new JavaType[] { new SimpleType(List.class) };
        CollectionType modifiedType = collectionType.refine(LinkedList.class, null, superClass, superInterfaces);
        assertNotNull(modifiedType);
        assertEquals(LinkedList.class, modifiedType.getRawClass());
        assertEquals(superClass, modifiedType.getSuperClass());
        assertArrayEquals(superInterfaces, modifiedType.getInterfaces());

        // Edge case: rawType is null
        try {
            modifiedType = collectionType.refine(null, null, superClass, superInterfaces);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("rawType cannot be null", e.getMessage());
        }
    }

    @Test
    public void test_toString() {
        CollectionType collectionType = CollectionType.construct(ArrayList.class, new TypeBindings(), null, null, String.class);
        assertEquals("[collection type; class java.util.ArrayList, contains class java.lang.String]", collectionType.toString());
    }

}