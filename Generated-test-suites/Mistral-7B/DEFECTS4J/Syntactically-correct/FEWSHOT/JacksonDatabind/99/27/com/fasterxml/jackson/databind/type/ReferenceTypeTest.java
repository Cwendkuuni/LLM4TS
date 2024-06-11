package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = IntType.Instance;
        ReferenceType referenceType = ReferenceType.construct(cls, bindings, superClass, superInts, refType);
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(bindings, referenceType.getTypeBindings());
        assertEquals(superClass, referenceType.getSuperClass());
        assertEquals(superInts, referenceType.getSuperInterfaces());
        assertEquals(refType, referenceType.getContentType());
        assertEquals(ReferenceType.class, referenceType.getClass());
    }

    @Test
    public void testUpgradeFrom() {
        Class<?> baseClass = String.class;
        JavaType baseType = SimpleType.construct(baseClass, TypeBindings.emptyBindings());
        JavaType refType = IntType.Instance;
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(baseClass, referenceType.getRawClass());
        assertEquals(ReferenceType.class, referenceType.getClass());
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testWithContentType() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = IntType.Instance;
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
        JavaType newContentType = DoubleType.Instance;
        ReferenceType newReferenceType = referenceType.withContentType(newContentType);
        assertEquals(cls, newReferenceType.getRawClass());
        assertEquals(bindings, newReferenceType.getTypeBindings());
        assertEquals(superClass, newReferenceType.getSuperClass());
        assertEquals(superInts, newReferenceType.getSuperInterfaces());
        assertEquals(newContentType, newReferenceType.getContentType());
    }

    @Test
    public void testGetContentType() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = IntType.Instance;
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
        assertEquals(IntType.Instance, referenceType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = IntType.Instance;
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testGetAnchorType() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = IntType.Instance;
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
        assertNull(referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = IntType.Instance;
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
        assertFalse(referenceType.isAnchorType());
    }

    @Test
    public void testToString() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType refType = IntType.Instance;
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
        String actual = referenceType.toString();
        String expected = "[reference type, class " + cls.getName() + "<" + refType + ">]";
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = String.class;
        TypeBindings bindings1 = TypeBindings.emptyBindings();
        JavaType superClass1 = Object.class;
        JavaType[] superInts1 = new JavaType[0];
        JavaType refType1 = IntType.Instance;
        ReferenceType referenceType1 = new ReferenceType(cls1, bindings1, superClass1, superInts1, refType1, null, null, null, false);

        Class<?> cls2 = String.class;
        TypeBindings bindings2 = TypeBindings.emptyBindings();
        JavaType superClass2 = Object.class;
        JavaType[] superInts2 = new JavaType[0];
        JavaType refType2 = IntType.Instance;
        ReferenceType referenceType2 = new ReferenceType(cls2, bindings2, superClass2, superInts2, refType2, null, null, null, false);

        ReferenceType referenceType3 = new ReferenceType(cls1, bindings1, superClass1, superInts1, refType1, null, null, null, false);

        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType3));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(new Object()));
        assertFalse(referenceType1.equals(referenceType2));
    }
}