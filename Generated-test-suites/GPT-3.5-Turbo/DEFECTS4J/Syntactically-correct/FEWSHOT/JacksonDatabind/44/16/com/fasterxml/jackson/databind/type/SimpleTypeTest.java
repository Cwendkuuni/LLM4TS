package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class SimpleTypeTest {
    private SimpleType simpleType;

    @Before
    public void setup() {
        simpleType = new SimpleType(String.class);
    }

    @Test
    public void testConstructor() {
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getContentType());
        assertNull(simpleType.getKeyType());
    }

    @Test
    public void testIsContainerType() {
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature_withStringBuilder() {
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);

        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignature_withStringBuilder() {
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);

        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testToString() {
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }

    @Test
    public void testEquals_withSameInstance() {
        assertTrue(simpleType.equals(simpleType));
    }

    @Test
    public void testEquals_withEqualSimpleTypes() {
        SimpleType other = new SimpleType(String.class);

        assertTrue(simpleType.equals(other));
    }

    @Test
    public void testEquals_withNull() {
        assertFalse(simpleType.equals(null));
    }

    @Test
    public void testEquals_withDifferentClass() {
        SimpleType other = new SimpleType(Integer.class);

        assertFalse(simpleType.equals(other));
    }

    @Test
    public void testEquals_withDifferentSuperClass() {
        SimpleType other = new SimpleType(CharSequence.class);

        assertFalse(simpleType.equals(other));
    }

    @Test
    public void testEquals_withDifferentBindings() {
        SimpleType other = new SimpleType(String.class, TypeBindings.emptyBindings().withBoundType(0, Integer.class), null, null);

        assertFalse(simpleType.equals(other));
    }
}