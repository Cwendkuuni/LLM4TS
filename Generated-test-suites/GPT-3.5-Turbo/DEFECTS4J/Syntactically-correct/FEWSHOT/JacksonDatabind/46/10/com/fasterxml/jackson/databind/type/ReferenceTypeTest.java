package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        ReferenceType referenceType = ReferenceType.construct(String.class, JavaType.INT, new Object(), new Object());
        assertTrue(referenceType instanceof ReferenceType);
        assertEquals(referenceType.getClass(), ReferenceType.class);
    }

    @Test
    public void testWithContentTypeHandler() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.INT, new Object(), new Object(), false).withContentTypeHandler(new Object());
        assertTrue(referenceType instanceof ReferenceType);
        assertEquals(referenceType.getClass(), ReferenceType.class);
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.INT, new Object(), new Object(), false).withStaticTyping();
        assertTrue(referenceType instanceof ReferenceType);
        assertEquals(referenceType.getClass(), ReferenceType.class);
    }

    @Test
    public void testBuildCanonicalName() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.INT, new Object(), new Object(), false);
        assertEquals("java.lang.String<int>", referenceType.buildCanonicalName());
    }

    @Test
    public void testGetReferencedType() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.INT, new Object(), new Object(), false);
        JavaType referencedType = referenceType.getReferencedType();
        assertTrue(referencedType instanceof JavaType);
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.INT, new Object(), new Object(), false);
        assertTrue(referenceType.isReferenceType());
    }
}