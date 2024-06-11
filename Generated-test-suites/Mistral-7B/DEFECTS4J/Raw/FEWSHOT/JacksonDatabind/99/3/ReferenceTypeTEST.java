package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refdType = TypeFactory.defaultInstance().constructType(Integer.class);

        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refdType);

        assertTrue(referenceType instanceof ReferenceType);
        assertTrue(referenceType.getContentType().equals(refdType));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNullRefdType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNonType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType.upgradeFrom(baseType, new Object());
    }

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);

        ReferenceType referenceType = ReferenceType.construct(cls, bindings, superClass, superInterfaces, refType);

        assertTrue(referenceType instanceof ReferenceType);
        assertTrue(referenceType.getContentType().equals(refType));
    }

    @Test
    public void testWithContentType() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInterfaces, refType, null, null, null, false);

        JavaType newContentType = TypeFactory.defaultInstance().constructType(Long.class);
        ReferenceType newReferenceType = referenceType.withContentType(newContentType);

        assertTrue(newReferenceType instanceof ReferenceType);
        assertTrue(newReferenceType.getContentType().equals(newContentType));
    }

    @Test
    public void testGetContentType() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInterfaces, refType, null, null, null, false);

        JavaType contentType = referenceType.getContentType();

        assertTrue(contentType instanceof JavaType);
        assertTrue(contentType.equals(refType));
    }

    @Test
    public void testIsReferenceType() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInterfaces, refType, null, null, null, false);

        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInterfaces, refType, null, null, null, false);

        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetReferencedType() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInterfaces, refType, null, null, null, false);

        JavaType referencedType = referenceType.getReferencedType();

        assertTrue(referencedType instanceof JavaType);
        assertTrue(referencedType.equals(refType));
    }

    @Test
    public void testGetAnchorType() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInterfaces, refType, null, null, null, false);

        JavaType anchorType = referenceType.getAnchorType();

        assertTrue(anchorType instanceof ReferenceType);
    }

    @Test
    public void testIsAnchorType() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInterfaces, refType, null, null, null, false);

        assertFalse(referenceType.isAnchorType());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);

        ReferenceType referenceType = new ReferenceType(rawType, bindings, superClass, superInterfaces, refType, null, null, null, false);

        JavaType newRefdType = TypeFactory.defaultInstance().constructType(Long.class);
        ReferenceType newReferenceType = referenceType.refine(rawType, bindings, superClass, superInterfaces);

        assertTrue(newReferenceType instanceof ReferenceType);
        assertTrue(newReferenceType.getReferencedType().equals(newRefdType));
    }

    @Test
    public void testEquality() {
        Class<?> cls1 = String.class;
        TypeBindings bindings1 = TypeBindings.emptyBindings();
        JavaType superClass1 = Object.class;
        JavaType[] superInterfaces1 = new JavaType[0];
        JavaType refType1 = TypeFactory.defaultInstance().constructType(Integer.class);

        ReferenceType referenceType1 = new ReferenceType(cls1, bindings1, superClass1, superInterfaces1, refType1, null, null, null, false);

        Class<?> cls2 = String.class;
        TypeBindings bindings2 = TypeBindings.emptyBindings();
        JavaType superClass2 = Object.class;
        JavaType[] superInterfaces2 = new JavaType[0];
        JavaType refType2 = TypeFactory.defaultInstance().constructType(Integer.class);

        ReferenceType referenceType2 = new ReferenceType(cls2, bindings2, superClass2, superInterfaces2, refType2, null, null, null, false);

        ReferenceType referenceType3 = referenceType1;

        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType3));
        assertTrue(referenceType1.equals(referenceType2));

        ReferenceType referenceType4 = new ReferenceType(cls1, bindings1, superClass1, superInterfaces1, refType1, null, null, null, true);

        assertFalse(referenceType1.equals(referenceType4));
    }

    @Test
    public void testToString() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInterfaces, refType, null, null, null, false);

        String toString = referenceType.toString();

        StringBuilder expectedString = new StringBuilder(40)
                .append("[reference type, class ")
                .append(cls.getName())
                .append('<')
                .append(refType)
                .append('>')
                .append(']');

        assertTrue(toString.equals(expectedString.toString()));
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = String.class;
        TypeBindings bindings1 = TypeBindings.emptyBindings();
        JavaType superClass1 = Object.class;
        JavaType[] superInterfaces1 = new JavaType[0];
        JavaType refType1 = TypeFactory.defaultInstance().constructType(Integer.class);

        ReferenceType referenceType1 = new ReferenceType(cls1, bindings1, superClass1, superInterfaces1, refType1, null, null, null, false);

        Class<?> cls2 = String.class;
        TypeBindings bindings2 = TypeBindings.emptyBindings();
        JavaType superClass2 = Object.class;
        JavaType[] superInterfaces2 = new JavaType[0];
        JavaType refType2 = TypeFactory.defaultInstance().constructType(Integer.class);

        ReferenceType referenceType2 = new ReferenceType(cls2, bindings2, superClass2, superInterfaces2, refType2, null, null, null, false);

        ReferenceType referenceType3 = referenceType1;

        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType3));
        assertTrue(referenceType1.equals(referenceType2));

        ReferenceType referenceType4 = new ReferenceType(cls1, bindings1, superClass1, superInterfaces1, refType1, null, null, null, true);

        assertFalse(referenceType1.equals(referenceType4));

        Object other = new Object();

        assertFalse(referenceType1.equals(other));
    }
}