package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertEquals(cls, simpleType.getType());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertEquals(cls, simpleType.getType());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());

        try {
            SimpleType simpleType2 = SimpleType.construct(List.class);
            fail("Should not be able to construct SimpleType for a Collection");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNarrow() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        Class<?> subclass = Integer.class;
        SimpleType narrowedType = simpleType._narrow(subclass);
        assertEquals(subclass, narrowedType.getType());
        assertNotNull(narrowedType.getSuperClass());
        assertArrayEquals(new Class[] {}, narrowedType.getSuperInterfaces());
        assertFalse(narrowedType.isContainerType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = String.class;
        Object handler = new Object();
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        SimpleType withHandler = simpleType.withTypeHandler(handler);
        assertEquals(cls, withHandler.getType());
        assertEquals(handler, withHandler.getTypeHandler());
        assertNull(withHandler.getSuperClass());
        assertNull(withHandler.getSuperInterfaces());
        assertFalse(withHandler.isContainerType());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = List.class;
        TypeBindings bindings = new TypeBindings();
        SimpleType simpleType = new SimpleType(cls, bindings, null, null, null, null, false);
        String canonicalName = simpleType.buildCanonicalName();
        assertEquals("java.util.List", canonicalName);
    }

    @Test
    public void testGetErasedSignatureAndGetGenericSignature() {
        Class<?> cls = List.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null, null, null, false);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("java.util.List", sb.toString());
        simpleType.getGenericSignature(sb);
        assertEquals("<java.lang.Object>", sb.toString());
    }
}