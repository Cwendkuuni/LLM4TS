package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);

        assertEquals(Object.class, simpleType.getRawClass());
        assertTrue(simpleType.isTypeOrSubTypeOf(Object.class));
        assertFalse(simpleType.isTypeOrSubTypeOf(String.class));
        assertFalse(simpleType.isContainerType());
        assertNull(simpleType.getContentType());
        assertNull(simpleType.getKeyType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(Object[].class);
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        SimpleType narrowedType = (SimpleType) simpleType._narrow(String.class);

        assertEquals(String.class, narrowedType.getRawClass());
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        SimpleType withContentType = (SimpleType) simpleType.withContentType(SimpleType.constructUnsafe(String.class));

        assertEquals(Object.class, withContentType.getRawClass());
        assertNull(withContentType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        simpleType.withContentTypeHandler(new Object());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        SimpleType withStaticTyping = simpleType.withStaticTyping();

        assertEquals(Object.class, withStaticTyping.getRawClass());
        assertTrue(withStaticTyping.isStaticTyping());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        JavaType refinedType = simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null);

        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);

        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder erasedSignature = simpleType.getErasedSignature(sb);

        assertEquals("Ljava/lang/Object;", sb.toString());
        assertEquals("Ljava/lang/Object;", erasedSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder genericSignature = simpleType.getGenericSignature(sb);

        assertEquals("Ljava/lang/Object;", sb.toString());
        assertEquals("Ljava/lang/Object;", genericSignature.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        String stringRepresentation = simpleType.toString();

        assertEquals("[simple type, class java.lang.Object]", stringRepresentation);
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(Object.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(Object.class);
        SimpleType simpleType3 = SimpleType.constructUnsafe(String.class);

        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
        assertFalse(simpleType1.equals(null));
        assertFalse(simpleType1.equals(new Object()));
    }
}