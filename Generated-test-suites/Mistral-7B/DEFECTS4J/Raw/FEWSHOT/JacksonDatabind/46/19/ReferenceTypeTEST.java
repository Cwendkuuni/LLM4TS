package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        JavaType refType = IntType.getInstance();
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null);
        assertNotNull(referenceType);
        assertEquals(cls, referenceType.getType());
        assertEquals(refType.getClass(), referenceType._referencedType.getClass());
        assertEquals(refType, referenceType.getReferencedType());
        assertFalse(referenceType.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = String.class;
        JavaType refType = IntType.getInstance();
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(handler);
        assertNotSame(referenceType, newReferenceType);
        assertEquals(referenceType.getType(), newReferenceType.getType());
        assertEquals(referenceType._referencedType, newReferenceType._referencedType);
        assertNotSame(referenceType._typeHandler, newReferenceType._typeHandler);
        assertEquals(handler, newReferenceType._typeHandler);
        assertFalse(newReferenceType.isStatic());
    }

    @Test
    public void testGetReferencedType() {
        Class<?> cls = String.class;
        JavaType refType = IntType.getInstance();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        Class<?> cls = String.class;
        JavaType refType = IntType.getInstance();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testNarrow() {
        Class<?> cls = String.class;
        JavaType refType = IntType.getInstance();
        JavaType narrowedType = new ReferenceType(String[].class, refType, null, null, false)._narrow(cls);
        assertEquals(cls, narrowedType.getType());
        assertEquals(refType, narrowedType._referencedType);
    }

    @Test
    public void testGetContainedType() {
        Class<?> cls = String.class;
        JavaType refType = IntType.getInstance();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        assertEquals(refType, referenceType.containedType(0));
        assertNull(referenceType.containedType(1));
    }

    @Test
    public void testContainsType() {
        Class<?> cls = String.class;
        JavaType refType = IntType.getInstance();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        assertTrue(referenceType.containsType(refType));
        assertFalse(referenceType.containsType(DoubleType.getInstance()));
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = String.class;
        JavaType refType1 = IntType.getInstance();
        Class<?> cls2 = String.class;
        JavaType refType2 = LongType.getInstance();
        ReferenceType referenceType1 = new ReferenceType(cls1, refType1, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(cls2, refType2, null, null, false);
        assertFalse(referenceType1.equals(referenceType2));

        ReferenceType referenceType3 = new ReferenceType(cls1, refType1, null, null, false);
        assertTrue(referenceType1.equals(referenceType3));
    }
}