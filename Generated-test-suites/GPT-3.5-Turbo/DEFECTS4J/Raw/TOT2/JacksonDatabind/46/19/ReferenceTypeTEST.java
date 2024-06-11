package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        assertNotNull(ReferenceType.construct(String.class, null, null, null));
    }

    @Test
    public void testWithTypeHandler() {
        ReferenceType rt = new ReferenceType(String.class, null, null, null, false);
        assertEquals(rt, rt.withTypeHandler(null));
    }

    @Test
    public void testWithContentTypeHandler() {
        ReferenceType rt = new ReferenceType(String.class, null, null, null, false);
        assertEquals(rt, rt.withContentTypeHandler(null));
    }

    @Test
    public void testWithValueHandler() {
        ReferenceType rt = new ReferenceType(String.class, null, null, null, false);
        assertEquals(rt, rt.withValueHandler(null));
    }

    @Test
    public void testWithContentValueHandler() {
        ReferenceType rt = new ReferenceType(String.class, null, null, null, false);
        assertEquals(rt, rt.withContentValueHandler(null));
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType rt = new ReferenceType(String.class, null, null, null, false);
        assertEquals(rt, rt.withStaticTyping());
    }

    @Test
    public void testBuildCanonicalName() {
        ReferenceType rt = new ReferenceType(String.class, null, null, null, false);
        assertNotNull(rt.buildCanonicalName());
    }

    @Test
    public void testNarrow() {
        ReferenceType rt = new ReferenceType(String.class, null, null, null, false);
        assertEquals(rt, rt._narrow(Object.class));
    }

    @Test
    public void testGetReferencedType() {
        ReferenceType rt = new ReferenceType(String.class, null, null, null, false);
        assertNotNull(rt.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType rt = new ReferenceType(String.class, null, null, null, false);
        assertTrue(rt.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        ReferenceType rt = new ReferenceType(String.class, null, null, null, false);
        assertEquals(1, rt.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        ReferenceType rt = new ReferenceType(String.class, null, null, null, false);
        assertNotNull(rt.containedType(0));
        assertNull(rt.containedType(1));
    }

    @Test
    public void testContainedTypeName() {
        ReferenceType rt = new ReferenceType(String.class, null, null, null, false);
        assertNotNull(rt.containedTypeName(0));
        assertNull(rt.containedTypeName(1));
    }

    @Test
    public void testGetParameterSource() {
        ReferenceType rt = new ReferenceType(String.class, null, null, null, false);
        assertEquals(String.class, rt.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        ReferenceType rt = new ReferenceType(String.class, null, null, null, false);
        assertNotNull(rt.getErasedSignature(new StringBuilder()));
    }

    @Test
    public void testGetGenericSignature() {
        ReferenceType rt = new ReferenceType(String.class, null, null, null, false);
        assertNotNull(rt.getGenericSignature(new StringBuilder()));
    }

    @Test
    public void testToString() {
        ReferenceType rt = new ReferenceType(String.class, null, null, null, false);
        assertNotNull(rt.toString());
    }

    @Test
    public void testEquals() {
        ReferenceType rt1 = new ReferenceType(String.class, null, null, null, false);
        ReferenceType rt2 = new ReferenceType(String.class, null, null, null, false);
        assertTrue(rt1.equals(rt2));
    }
}