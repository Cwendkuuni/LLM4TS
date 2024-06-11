package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = Integer.class;
        JavaType refType = BooleanType.Instance;
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null);
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(refType.getClass(), referenceType._referencedType.getClass());
    }

    @Test
    public void testGetReferencedType() {
        Class<?> cls = Integer.class;
        JavaType refType = BooleanType.Instance;
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        Class<?> cls = Integer.class;
        JavaType refType = BooleanType.Instance;
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = Integer.class;
        JavaType refType = BooleanType.Instance;
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);
        assertNotEquals(referenceType, newReferenceType);
        assertEquals(typeHandler, newReferenceType._typeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> cls = Integer.class;
        JavaType refType = BooleanType.Instance;
        Object contentTypeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        JavaType newContentType = referenceType.withContentTypeHandler(contentTypeHandler);
        assertNotEquals(refType, newContentType);
        assertEquals(contentTypeHandler, newContentType.<Object>getTypeHandler());
        ReferenceType newReferenceType = new ReferenceType(_class, newContentType, null, null, false);
        assertEquals(newContentType, newReferenceType.getReferencedType());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> cls = Integer.class;
        JavaType refType = BooleanType.Instance;
        Object valueHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);
        assertNotEquals(referenceType, newReferenceType);
        assertEquals(valueHandler, newReferenceType._valueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> cls = Integer.class;
        JavaType refType = BooleanType.Instance;
        Object contentValueHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        JavaType newContentType = referenceType.withContentValueHandler(contentValueHandler);
        assertNotEquals(refType, newContentType);
        assertEquals(contentValueHandler, newContentType.<Object>getValueHandler());
        ReferenceType newReferenceType = new ReferenceType(_class, newContentType, null, null, false);
        assertEquals(newContentType, newReferenceType.getReferencedType());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> cls = Integer.class;
        JavaType refType = BooleanType.Instance;
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertNotEquals(referenceType, newReferenceType);
        assertTrue(newReferenceType._asStatic);
    }

    @Test
    public void testNarrow() {
        Class<?> cls = Integer.class;
        JavaType refType = BooleanType.Instance;
        JavaType newRefType = cls.getSuperclass();
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        JavaType narrowType = referenceType._narrow(newRefType.getClass());
        assertEquals(newRefType, narrowType.getReferencedType());
    }

    @Test
    public void testToString() {
        Class<?> cls = Integer.class;
        JavaType refType = BooleanType.Instance;
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        String toString = referenceType.toString();
        assertTrue(toString.contains("[reference type"));
        assertTrue(toString.contains(cls.getName()));
        assertTrue(toString.contains(refType.toString()));
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = Integer.class;
        JavaType refType1 = BooleanType.Instance;
        Class<?> cls2 = Integer.class;
        JavaType refType2 = BooleanType.Instance;
        ReferenceType referenceType1 = new ReferenceType(cls1, refType1, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(cls2, refType2, null, null, false);
        assertFalse(referenceType1.equals(referenceType2));
        ReferenceType newReferenceType1 = new ReferenceType(cls1, refType1, null, null, false);
        ReferenceType newReferenceType2 = new ReferenceType(cls2, refType2, null, null, false);
        assertFalse(referenceType1.equals(newReferenceType2));
        ReferenceType sameReferenceType = new ReferenceType(cls1, refType1, null, null, false);
        assertTrue(referenceType1.equals(sameReferenceType));
    }
}