package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType refType = null; // Use a mock or a concrete implementation
        Class<?> cls = String.class;

        ReferenceType referenceType1 = ReferenceType.construct(cls, refType, null);
        ReferenceType referenceType2 = ReferenceType.construct(cls, refType, null);

        assertEquals(referenceType1, referenceType2);
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = null; // Use a mock or a concrete implementation
        Class<?> cls = String.class;

        ReferenceType referenceType = new ReferenceType(cls, refType, null);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        Class<?> cls = String.class;
        JavaType refType = null; // Use a mock or a concrete implementation

        ReferenceType referenceType = new ReferenceType(cls, refType, null);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testToString() {
        Class<?> cls = String.class;
        JavaType refType = null; // Use a mock or a concrete implementation

        ReferenceType referenceType = new ReferenceType(cls, refType, null);
        String expected = "[reference type, class " + cls.getName() + "<T>" +
                "]";
        String actual = referenceType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = String.class;
        JavaType refType1 = null; // Use a mock or a concrete implementation
        ReferenceType referenceType1 = new ReferenceType(cls1, refType1, null);

        Class<?> cls2 = Integer.class;
        JavaType refType2 = null; // Use a mock or a concrete implementation
        ReferenceType referenceType2 = new ReferenceType(cls2, refType2, null);

        assertFalse(referenceType1.equals(referenceType2));

        ReferenceType referenceType3 = new ReferenceType(cls1, refType1, null);
        assertTrue(referenceType1.equals(referenceType3));

        ReferenceType referenceType4 = new ReferenceType(cls1, refType1, null);
        assertTrue(referenceType1.equals(referenceType4));

        ReferenceType referenceType5 = new ReferenceType(cls1, refType1, null);
        referenceType5.withTypeHandler(null);
        assertFalse(referenceType1.equals(referenceType5));
    }

    @Test
    public void testNarrow() {
        Class<?> cls = String.class;
        JavaType refType = null; // Use a mock or a concrete implementation
        JavaType subclassType = Integer.class;

        ReferenceType referenceType = new ReferenceType(cls, refType, null);
        JavaType narrowedType = referenceType._narrow(subclassType);
        assertEquals(new ReferenceType(subclassType, refType, null), narrowedType);
    }

    @Test
    public void testGetContainedTypeCount() {
        Class<?> cls = String.class;
        JavaType refType = null; // Use a mock or a concrete implementation

        ReferenceType referenceType = new ReferenceType(cls, refType, null);
        int containedTypeCount = referenceType.containedTypeCount();
        assertEquals(1, containedTypeCount);
    }

    @Test
    public void testGetContainedType() {
        Class<?> cls = String.class;
        JavaType refType = null; // Use a mock or a concrete implementation

        ReferenceType referenceType = new ReferenceType(cls, refType, null);
        JavaType containedType = referenceType.containedType(0);
        assertEquals(refType, containedType);
    }

    @Test
    public void testGetParameterSource() {
        Class<?> cls = String.class;
        JavaType refType = null; // Use a mock or a concrete implementation

        ReferenceType referenceType = new ReferenceType(cls, refType, null);
        assertEquals(cls, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        JavaType refType = null; // Use a mock or a concrete implementation

        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType(cls, refType, null);
        referenceType.getErasedSignature(sb);
        String expected = "L" + cls.getName() + ";";
        String actual = sb.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = String.class;
        JavaType refType = null; // Use a mock or a concrete implementation

        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType(cls, refType, null);
        referenceType.getGenericSignature(sb);
        String expected = "L" + cls.getName() + '<T>' + ";";
        String actual = sb.toString();
        assertEquals(expected, actual);
    }
}