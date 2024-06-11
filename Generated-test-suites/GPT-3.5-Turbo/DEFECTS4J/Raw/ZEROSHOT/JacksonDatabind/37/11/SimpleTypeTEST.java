package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType type = SimpleType.constructUnsafe(String.class);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
    }

    @Test
    public void testConstruct() {
        SimpleType type = SimpleType.construct(Integer.class);
        assertNotNull(type);
        assertEquals(Integer.class, type.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMapClass() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollectionClass() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArrayClass() {
        SimpleType.construct(Object[].class);
    }

    @Test
    public void testNarrow() {
        SimpleType type = SimpleType.construct(Number.class)._narrow(Integer.class);
        assertNotNull(type);
        assertEquals(Integer.class, type.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubclass() {
        SimpleType type = SimpleType.construct(Number.class)._narrow(String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType.construct(Number.class).withContentType(Integer.class);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object typeHandler = new Object();
        SimpleType type = SimpleType.construct(Number.class).withContentTypeHandler(typeHandler);
        assertNotNull(type);
        assertEquals(typeHandler, type.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType.construct(Number.class).withContentValueHandler(new Object());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType type = SimpleType.construct(Number.class).withStaticTyping();
        assertNotNull(type);
        assertTrue(type.isStatic());
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType type = SimpleType.construct(Number.class);
        assertEquals("java.lang.Number", type.buildCanonicalName());
        
        type = SimpleType.construct(Map.Entry.class);
        assertEquals("java.util.Map$Entry<?,?>", type.buildCanonicalName());
    }

    @Test
    public void testIsContainerType() {
        SimpleType type = SimpleType.construct(Number.class);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType type = SimpleType.construct(Number.class);
        StringBuilder sb = new StringBuilder();
        assertEquals("Ljava/lang/Number;", type.getErasedSignature(sb).toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType type = SimpleType.construct(Number.class);
        StringBuilder sb = new StringBuilder();
        assertEquals("Ljava/lang/Number;", type.getGenericSignature(sb).toString());
    }

    @Test
    public void testToString() {
        SimpleType type = SimpleType.construct(Number.class);
        assertEquals("[simple type, class java.lang.Number]", type.toString());
    }

    @Test
    public void testEquals() {
        SimpleType type1 = SimpleType.construct(Number.class);
        SimpleType type2 = SimpleType.construct(Number.class);
        assertTrue(type1.equals(type2));
    }
}