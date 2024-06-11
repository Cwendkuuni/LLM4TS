package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SimpleTypeTest {
    @Test
    public void testConstructor() {
        SimpleType simpleType = new SimpleType(String.class);

        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(0, simpleType.getBindings().size());
        assertEquals(null, simpleType.getSuperClass());
        assertEquals(0, simpleType.getInterfaces().size());
        assertEquals(null, simpleType.getValueHandler());
        assertEquals(null, simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testCopyConstructor() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(simpleType1);

        assertEquals(simpleType1.getRawClass(), simpleType2.getRawClass());
        assertEquals(simpleType1.getBindings().size(), simpleType2.getBindings().size());
        assertEquals(simpleType1.getSuperClass(), simpleType2.getSuperClass());
        assertEquals(simpleType1.getInterfaces().size(), simpleType2.getInterfaces().size());
        assertEquals(simpleType1.getValueHandler(), simpleType2.getValueHandler());
        assertEquals(simpleType1.getTypeHandler(), simpleType2.getTypeHandler());
        assertEquals(simpleType1.isStatic(), simpleType2.isStatic());
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType1 = new SimpleType(StringBuffer.class);
        SimpleType simpleType2 = simpleType1._narrow(String.class);

        assertEquals(String.class, simpleType2.getRawClass());
        assertEquals(simpleType1.getBindings().size(), simpleType2.getBindings().size());
        assertEquals(simpleType1.getSuperClass(), simpleType2.getSuperClass());
        assertEquals(simpleType1.getInterfaces().size(), simpleType2.getInterfaces().size());
        assertEquals(simpleType1.getValueHandler(), simpleType2.getValueHandler());
        assertEquals(simpleType1.getTypeHandler(), simpleType2.getTypeHandler());
        assertEquals(simpleType1.isStatic(), simpleType2.isStatic());
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);

        try {
            simpleType.withContentType(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);

        try {
            simpleType.withContentTypeHandler(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object valueHandler = new Object();

        SimpleType newSimpleType = simpleType.withValueHandler(valueHandler);

        assertEquals(String.class, newSimpleType.getRawClass());
        assertEquals(simpleType.getBindings().size(), newSimpleType.getBindings().size());
        assertEquals(simpleType.getSuperClass(), newSimpleType.getSuperClass());
        assertEquals(simpleType.getInterfaces().size(), newSimpleType.getInterfaces().size());
        assertEquals(valueHandler, newSimpleType.getValueHandler());
        assertEquals(simpleType.getTypeHandler(), newSimpleType.getTypeHandler());
        assertEquals(simpleType.isStatic(), newSimpleType.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);

        try {
            simpleType.withContentValueHandler(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);

        SimpleType newSimpleType = simpleType.withStaticTyping();

        assertEquals(String.class, newSimpleType.getRawClass());
        assertEquals(simpleType.getBindings().size(), newSimpleType.getBindings().size());
        assertEquals(simpleType.getSuperClass(), newSimpleType.getSuperClass());
        assertEquals(simpleType.getInterfaces().size(), newSimpleType.getInterfaces().size());
        assertEquals(simpleType.getValueHandler(), newSimpleType.getValueHandler());
        assertEquals(simpleType.getTypeHandler(), newSimpleType.getTypeHandler());
        assertTrue(newSimpleType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);

        assertEquals(null, simpleType.refine(StringBuffer.class, null, null, null));
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class);

        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);

        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String.class);

        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);

        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);

        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        SimpleType simpleType3 = new SimpleType(Integer.class);

        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
        assertFalse(simpleType1.equals(null));
        assertFalse(simpleType1.equals(new Object()));
    }
}