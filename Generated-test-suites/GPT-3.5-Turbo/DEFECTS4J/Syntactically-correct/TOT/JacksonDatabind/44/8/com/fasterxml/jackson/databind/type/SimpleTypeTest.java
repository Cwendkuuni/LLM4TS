
package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        assertEquals(Integer.class, simpleType.getRawClass());
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException is not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }

        try {
            SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException is not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }

        try {
            SimpleType.construct(String[].class);
            fail("Expected IllegalArgumentException is not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.String;)", e.getMessage());
        }

        SimpleType simpleType = SimpleType.construct(Integer.class);
        assertEquals(Integer.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getInterfaces());
        assertEquals(0, simpleType.containedTypeCount());
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        JavaType narrowedType = simpleType.narrow(Number.class);
        assertEquals(Number.class, narrowedType.getRawClass());
        assertEquals(simpleType.getBindings(), narrowedType.getBindings());
        assertEquals(simpleType.getSuperClass(), narrowedType.getSuperClass());
        assertArrayEquals(simpleType.getInterfaces(), narrowedType.getInterfaces());
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        try {
            simpleType.withContentType(SimpleType.constructUnsafe(Long.class));
            fail("Expected IllegalArgumentException is not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        Object typeHandler = new Object();
        SimpleType typeWithHandler = simpleType.withTypeHandler(typeHandler);
        assertSame(typeHandler, typeWithHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        try {
            simpleType.withContentTypeHandler(new Object());
            fail("Expected IllegalArgumentException is not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithvalueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        Object valueHandler = new Object();
        SimpleType typeWithValueHandler = simpleType.withValueHandler(valueHandler);
        assertSame(valueHandler, typeWithValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        try {
            simpleType.withContentValueHandler(new Object());
            fail("Expected IllegalArgumentException is not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        assertFalse(simpleType.isStaticTyping());
        SimpleType staticType = simpleType.withStaticTyping();
        assertTrue(staticType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        JavaType refinedType = simpleType.refine(Double.class, TypeBindings.emptyBindings(), simpleType, null);
        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder signature = simpleType.getErasedSignature(sb);
        assertNotNull(signature);
        assertEquals("int", signature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder signature = simpleType.getGenericSignature(sb);
        assertNotNull(signature);
        assertEquals("int;", signature.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        assertEquals("[simple type, class java.lang.Integer]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(Integer.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(Integer.class);
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(null));
        assertFalse(simpleType1.equals(new Object()));
        assertFalse(simpleType1.equals(SimpleType.constructUnsafe(String.class)));
    }
}

