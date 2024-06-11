package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {
    
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        
        assertEquals(String.class, simpleType.getRawClass());
    }
    
    @Test
    public void testConstruct() {
        try {
            SimpleType simpleType = SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }
        
        try {
            SimpleType simpleType = SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }
        
        try {
            SimpleType simpleType = SimpleType.construct(Object[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.Object;)", e.getMessage());
        }
        
        SimpleType simpleType = SimpleType.construct(String.class);
        
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void test_narrow() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class).narrow(String.class);

        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void test_withContentType() {
        try {
            SimpleType simpleType = SimpleType.construct(String.class).withContentType(SimpleType.constructUnsafe(String.class));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void test_withTypeHandler() {
        Object dummyHandler1 = new Object();
        Object dummyHandler2 = new Object();
        SimpleType simpleType = SimpleType.constructUnsafe(String.class).withTypeHandler(dummyHandler1);

        assertEquals(dummyHandler1, simpleType.getTypeHandler());

        SimpleType simpleType2 = simpleType.withTypeHandler(dummyHandler2);

        assertEquals(dummyHandler2, simpleType2.getTypeHandler());
    }

    @Test
    public void test_withContentTypeHandler() {
        try {
            SimpleType simpleType = SimpleType.constructUnsafe(String.class).withContentTypeHandler(new Object());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void test_withValueHandler() {
        Object dummyHandler1 = new Object();
        Object dummyHandler2 = new Object();
        SimpleType simpleType = SimpleType.constructUnsafe(String.class).withValueHandler(dummyHandler1);

        assertEquals(dummyHandler1, simpleType.getValueHandler());

        SimpleType simpleType2 = simpleType.withValueHandler(dummyHandler2);

        assertEquals(dummyHandler2, simpleType2.getValueHandler());
    }

    @Test
    public void testT_withContentValueHandler() {
        try {
            SimpleType simpleType = SimpleType.constructUnsafe(String.class).withContentValueHandler(new Object());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void test_withStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class).withStaticTyping();

        assertTrue(simpleType.isStatic());
    }

    @Test
    public void test_refine() {
        JavaType refinedType = SimpleType.constructUnsafe(String.class).refine(String.class, null, null, null);

        assertNull(refinedType);
    }

    @Test
    public void test_buildCanonicalName() {
        SimpleType simpleType = SimpleType.construct(Map.class);

        assertEquals("java.util.Map", simpleType.buildCanonicalName());
    }

    @Test
    public void test_isContainerType() {
        SimpleType simpleType = SimpleType.construct(Map.class);

        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void test_getErasedSignature() {
        SimpleType simpleType = SimpleType.construct(Map.class);
        StringBuilder sb = new StringBuilder();

        StringBuilder erasedSignature = simpleType.getErasedSignature(sb);

        assertEquals("[simple type, class java.util.Map]", erasedSignature.toString());
    }

    @Test
    public void test_getGenericSignature() {
        SimpleType simpleType = SimpleType.construct(Map.class);
        StringBuilder sb = new StringBuilder();

        StringBuilder genericSignature = simpleType.getGenericSignature(sb);

        assertEquals("[simple type, class java.util.Map]", genericSignature.toString());
    }

    @Test
    public void test_equals() {
        SimpleType simpleType1 = SimpleType.construct(Map.class);
        SimpleType simpleType2 = SimpleType.construct(Map.class);
        SimpleType simpleType3 = SimpleType.construct(Collection.class);

        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }
    
    @Test
    public void test_toString() {
        SimpleType simpleType = SimpleType.construct(Map.class);
        
        assertEquals("[simple type, class java.util.Map]", simpleType.toString());
    }
}