package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        assertNotNull(simpleType);
        assertEquals(Integer.class, simpleType.getRawClass());

        boolean exceptionThrown = false;
        try {
            SimpleType.construct(Map.class);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            SimpleType.construct(Collection.class);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            SimpleType.construct(Object[].class);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType1 = SimpleType.construct(Number.class);
        SimpleType simpleType2 = simpleType1.narrow(Integer.class);
        assertEquals(Integer.class, simpleType2.getRawClass());
        assertNotSame(simpleType1, simpleType2);

        SimpleType simpleType3 = SimpleType.construct(Double.class);
        SimpleType simpleType4 = simpleType3.narrow(Number.class);
        assertEquals(Number.class, simpleType4.getRawClass());
        assertNotSame(simpleType3, simpleType4);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType1 = SimpleType.construct(Float.class);
        boolean exceptionThrown = false;
        try {
            simpleType1.withContentType(SimpleType.construct(Float.class));
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.construct(Long.class);
        simpleType.withContentTypeHandler(new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType simpleType = SimpleType.construct(String.class);
        simpleType.withContentValueHandler(new Object());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType1 = SimpleType.construct(Boolean.class);
        SimpleType simpleType2 = simpleType1.withStaticTyping();
        assertTrue(simpleType2.isStatic());
        assertNotSame(simpleType1, simpleType2);

        SimpleType simpleType3 = SimpleType.construct(Byte.class);
        SimpleType simpleType4 = simpleType3.withStaticTyping();
        assertTrue(simpleType4.isStatic());
        assertNotSame(simpleType3, simpleType4);
    }

    @Test
    public void testRefine() {
        SimpleType simpleType1 = SimpleType.construct(Short.class);
        JavaType javaType = simpleType1.refine(Integer.class, null, null, new JavaType[] {});
        assertNull(javaType);
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.construct(Character.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("Character", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType1 = SimpleType.construct(Enum.class);
        StringBuilder sb = new StringBuilder();
        simpleType1.getGenericSignature(sb);
        assertEquals("Ljava/lang/Enum<*>;", sb.toString());

        SimpleType simpleType2 = SimpleType.construct(CharSequence.class);
        StringBuilder sb2 = new StringBuilder();
        simpleType2.getGenericSignature(sb2);
        assertEquals("Ljava/lang/CharSequence;", sb2.toString());
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType1 = SimpleType.construct(Object.class);
        assertFalse(simpleType1.isContainerType());

        SimpleType simpleType2 = SimpleType.construct(Collection.class);
        assertFalse(simpleType2.isContainerType());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.construct(Double.class);
        SimpleType simpleType2 = SimpleType.construct(Double.class);
        assertTrue(simpleType1.equals(simpleType2));

        SimpleType simpleType3 = SimpleType.construct(Float.class);
        assertFalse(simpleType1.equals(simpleType3));

        SimpleType simpleType4 = SimpleType.construct(Boolean.class);
        assertFalse(simpleType1.equals(simpleType4));
    }
}