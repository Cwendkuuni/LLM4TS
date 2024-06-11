package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class ReferenceTypeTest {

    private ReferenceType referenceType;

    @Before
    public void setUp() {
        referenceType = new ReferenceType(Object.class, SimpleType.constructUnsafe(Object.class), null, null, false);
    }

    @Test
    public void testConstruct() {
        assertEquals(Object.class, referenceType.getClass());
        assertEquals(SimpleType.constructUnsafe(Object.class), referenceType.getReferencedType());
        assertNull(referenceType.getTypeHandler());
        assertNull(referenceType.getValueHandler());
        assertFalse(referenceType.isStaticTyping());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object typeHandler = new Object();
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(typeHandler);
        assertNotEquals(referenceType, newReferenceType);
        assertEquals(typeHandler, newReferenceType.getContentType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);
        assertNotEquals(referenceType, newReferenceType);
        assertEquals(valueHandler, newReferenceType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertNotEquals(referenceType, newReferenceType);
        assertTrue(newReferenceType.isStaticTyping());
    }

    @Test
    public void testBuildCanonicalName() {
        String expected = Object.class.getName() + "<" + referenceType.getReferencedType().toCanonical() + ">";
        assertEquals(expected, referenceType.buildCanonicalName());
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = String.class;
        ReferenceType newReferenceType = referenceType._narrow(subclass);
        assertNotEquals(referenceType, newReferenceType);
        assertEquals(subclass, newReferenceType.getClass());
    }

    @Test
    public void testGetReferencedType() {
        assertEquals(SimpleType.constructUnsafe(Object.class), referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        assertEquals(referenceType.getReferencedType(), referenceType.containedType(0));
        assertNull(referenceType.containedType(1));
    }

    @Test
    public void testContainedTypeName() {
        assertEquals("T", referenceType.containedTypeName(0));
        assertNull(referenceType.containedTypeName(1));
    }

    @Test
    public void testGetParameterSource() {
        assertEquals(Object.class, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        String expected = "L" + Object.class.getName().replace(".", "/") + ";";
        assertEquals(sb.append(expected).toString(), referenceType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        String expected = "L" + Object.class.getName().replace(".", "/") + "<" + referenceType.getReferencedType().getGenericSignature(new StringBuilder()) + ";";
        assertEquals(sb.append(expected).toString(), referenceType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testToString() {
        String expected = "[reference type, class " + referenceType.buildCanonicalName() + "]";
        assertEquals(expected, referenceType.toString());
    }

    @Test
    public void testEquals() {
        ReferenceType sameReferenceType = referenceType;
        assertTrue(referenceType.equals(sameReferenceType));

        ReferenceType differentReferenceType = new ReferenceType(Object.class, SimpleType.constructUnsafe(Integer.class), null, null, false);
        assertFalse(referenceType.equals(differentReferenceType));
    }
}