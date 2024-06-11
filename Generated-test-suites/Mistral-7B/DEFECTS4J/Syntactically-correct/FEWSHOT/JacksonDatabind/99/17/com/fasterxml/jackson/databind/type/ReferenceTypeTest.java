package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(Long.class, TypeBindings.emptyBindings(), superClass, new JavaType[]{}, refType);
        assertEquals(Long.class, referenceType.getRawClass());
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType simpleType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(simpleType, refType);
        assertEquals(Integer.class, referenceType.getRawClass());
        assertEquals(String.class, referenceType.getReferencedType().getRawClass());
    }

    @Test
    public void testGetContentType() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = new ReferenceType(Long.class, TypeBindings.emptyBindings(), superClass, new JavaType[]{}, refType);
        assertEquals(Integer.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testIsReferenceType() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = new ReferenceType(Long.class, TypeBindings.emptyBindings(), superClass, new JavaType[]{}, refType);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = new ReferenceType(Long.class, TypeBindings.emptyBindings(), superClass, new JavaType[]{}, refType);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = new ReferenceType(Long.class, TypeBindings.emptyBindings(), superClass, new JavaType[]{}, refType);
        StringBuilder expected = new StringBuilder("Ljava/lang/Long;");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = new ReferenceType(Long.class, TypeBindings.emptyBindings(), superClass, new JavaType[]{}, refType);
        StringBuilder expected = new StringBuilder("Ljava/lang/Long;<T extends java.lang.Comparable<T>>");
        StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetAnchorType() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType1 = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType refType2 = TypeFactory.defaultInstance().constructType(Double.class);
        ReferenceType referenceType1 = new ReferenceType(Long.class, TypeBindings.emptyBindings(), superClass, new JavaType[]{}, refType1);
        ReferenceType referenceType2 = new ReferenceType(Long.class, TypeBindings.emptyBindings(), superClass, new JavaType[]{}, refType2);
        assertEquals(referenceType1, referenceType1.getAnchorType());
        assertSame(referenceType1, referenceType2.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType superClass = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType1 = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType refType2 = TypeFactory.defaultInstance().constructType(Double.class);
        ReferenceType referenceType1 = new ReferenceType(Long.class, TypeBindings.emptyBindings(), superClass, new JavaType[]{}, refType1);
        ReferenceType referenceType2 = new ReferenceType(Long.class, TypeBindings.emptyBindings(), superClass, new JavaType[]{}, refType2);
        assertTrue(referenceType1.isAnchorType());
        assertTrue(referenceType2.isAnchorType());
        assertFalse(referenceType1.isAnchorType
                    (new ReferenceType(Long.class, TypeBindings.emptyBindings(), superClass, new JavaType[]{}, refType1)));
    }
}