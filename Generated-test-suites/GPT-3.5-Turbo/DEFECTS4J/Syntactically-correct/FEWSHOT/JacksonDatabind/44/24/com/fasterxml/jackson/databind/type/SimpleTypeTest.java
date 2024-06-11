
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;


public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType st = SimpleType.constructUnsafe(String.class);
        assertNotNull(st);
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType st = SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Expected IllegalArgumentException
        }
        try {
            SimpleType st = SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Expected IllegalArgumentException
        }
        try {
            SimpleType st = SimpleType.construct(Object[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Expected IllegalArgumentException
        }
        SimpleType st = SimpleType.construct(String.class);
        assertNotNull(st);
    }

    @Test
    public void testNarrow() {
        SimpleType st = SimpleType.construct(String.class);
        JavaType narrowedType = st._narrow(StringBuilder.class);
        assertTrue(narrowedType instanceof SimpleType);
        assertEquals(StringBuilder.class, narrowedType.getRawClass());
    }

    @Test
    public void testWithContentType() {
        SimpleType st = SimpleType.construct(String.class);
        try {
            st.withContentType(String.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Expected IllegalArgumentException
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType st = SimpleType.construct(String.class);
        Object handler = new Object();
        SimpleType newType = st.withTypeHandler(handler);
        assertEquals(handler, newType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType st = SimpleType.construct(String.class);
        try {
            st.withContentTypeHandler(new Object());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Expected IllegalArgumentException
        }
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType st = SimpleType.construct(String.class);
        try {
            st.withContentValueHandler(new Object());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Expected IllegalArgumentException
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType st = SimpleType.construct(String.class);
        SimpleType newType = st.withStaticTyping();
        assertTrue(newType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType st = SimpleType.construct(String.class);
        JavaType refinedType = st.refine(StringBuilder.class, null, null, null);
        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType st = SimpleType.construct(String.class);
        assertFalse(st.isContainerType());
    }

    @Test
    public void testToString() {
        SimpleType st = SimpleType.construct(String.class);
        assertEquals("[simple type, class java.lang.String]", st.toString());
    }

    @Test
    public void testEquals() {
        SimpleType st1 = SimpleType.constructUnsafe(String.class);
        SimpleType st2 = SimpleType.constructUnsafe(String.class);
        assertTrue(st1.equals(st2));
        assertFalse(st1.equals(new Object()));
    }
}
