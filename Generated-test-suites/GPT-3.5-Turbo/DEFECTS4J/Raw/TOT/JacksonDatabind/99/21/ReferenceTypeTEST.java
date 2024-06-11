package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstructor1() {
        ReferenceType referenceType = ReferenceType.construct(Object.class, null);
        Assert.assertEquals(Object.class, referenceType.getRawClass());
        Assert.assertNull(referenceType.getContentType());
        Assert.assertNull(referenceType.getReferencedType());
        Assert.assertFalse(referenceType.hasContentType());
        Assert.assertTrue(referenceType.isReferenceType());
        Assert.assertNull(referenceType.getAnchorType());
        Assert.assertTrue(referenceType.isAnchorType());
        Assert.assertEquals("com.fasterxml.jackson.databind.type.ReferenceTypeTest$1", referenceType.toString());
        Assert.assertEquals(false, referenceType.equals(null));
        Assert.assertEquals(false, referenceType.equals(this));
        Assert.assertEquals(false, referenceType.equals(new ReferenceType(Object.class, null, null, null, null, null, null, null, false)));
        Assert.assertEquals(true, referenceType.equals(referenceType));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor1WithNullClass() {
        ReferenceType.construct(null, null);
    }

    @Test
    public void testConstructor2() {
        ReferenceType referenceType = ReferenceType.construct(MyClass.class, null);
        Assert.assertEquals(MyClass.class, referenceType.getRawClass());
        Assert.assertNull(referenceType.getContentType());
        Assert.assertNull(referenceType.getReferencedType());
        Assert.assertFalse(referenceType.hasContentType());
        Assert.assertTrue(referenceType.isReferenceType());
        Assert.assertNull(referenceType.getAnchorType());
        Assert.assertTrue(referenceType.isAnchorType());
        Assert.assertEquals("com.fasterxml.jackson.databind.type.ReferenceTypeTest$MyClass", referenceType.toString());
    }

    @Test
    public void testConstructor2WithEmptyBindings() {
        ReferenceType referenceType = ReferenceType.construct(MyClass.class, TypeBindings.emptyBindings());
        Assert.assertEquals(MyClass.class, referenceType.getRawClass());
        Assert.assertNull(referenceType.getContentType());
        Assert.assertNull(referenceType.getReferencedType());
        Assert.assertFalse(referenceType.hasContentType());
        Assert.assertTrue(referenceType.isReferenceType());
        Assert.assertNull(referenceType.getAnchorType());
        Assert.assertTrue(referenceType.isAnchorType());
        Assert.assertEquals("com.fasterxml.jackson.databind.type.ReferenceTypeTest$MyClass", referenceType.toString());
    }

    @Test
    public void testWithContentType() {
        ReferenceType referenceType1 = ReferenceType.construct(MyClass.class, TypeBindings.emptyBindings());
        ReferenceType referenceType2 = referenceType1.withContentType(JavaType.INT);
        Assert.assertEquals(MyClass.class, referenceType2.getRawClass());
        Assert.assertEquals(JavaType.INT, referenceType2.getContentType());
        Assert.assertEquals(JavaType.INT, referenceType2.getReferencedType());
        Assert.assertTrue(referenceType2.hasContentType());
        Assert.assertTrue(referenceType2.isReferenceType());
        Assert.assertEquals("com.fasterxml.jackson.databind.type.ReferenceTypeTest$MyClass<INT>", referenceType2.toString());
    }

    class MyClass {}

}