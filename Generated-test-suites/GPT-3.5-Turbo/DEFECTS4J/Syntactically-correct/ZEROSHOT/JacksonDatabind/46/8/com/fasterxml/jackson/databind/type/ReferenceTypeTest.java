package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = Integer.class;
        JavaType refType = new JavaType();
        Object valueHandler = null;
        Object typeHandler = null;
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        assertNotNull(referenceType);
        assertEquals(cls, referenceType.getClass());
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testWithTypeHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType();
        referenceType.withTypeHandler(h);
        assertEquals(h, referenceType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType();
        referenceType.withContentTypeHandler(h);
        assertEquals(h, referenceType.getReferencedType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType();
        referenceType.withValueHandler(h);
        assertEquals(h, referenceType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType();
        referenceType.withContentValueHandler(h);
        assertEquals(h, referenceType.getReferencedType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType();
        referenceType.withStaticTyping();
        assertTrue(referenceType.isStaticTyping());
    }

    @Test
    public void testBuildCanonicalName() {
        ReferenceType referenceType = new ReferenceType();
        referenceType.buildCanonicalName();
        // TODO: add assertions
    }

    @Test
    public void testToString() {
        ReferenceType referenceType = new ReferenceType();
        String expected = "[reference type, class " + referenceType.buildCanonicalName() +
                "<" + referenceType.getReferencedType() + ">]";
        assertEquals(expected, referenceType.toString());
    }

    @Test
    public void testEquals() {
        Class<?> cls = Integer.class;
        JavaType refType = new JavaType();
        Object valueHandler = null;
        Object typeHandler = null;
        ReferenceType referenceType1 = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        ReferenceType referenceType2 = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        assertTrue(referenceType1.equals(referenceType2));
    }
}