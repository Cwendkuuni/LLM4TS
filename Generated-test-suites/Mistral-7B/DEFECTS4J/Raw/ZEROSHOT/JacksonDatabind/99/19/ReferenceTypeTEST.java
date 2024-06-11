package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstructor() {
        JavaType refType = JavaType.construct(String.class);
        ReferenceType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, new JavaType[0], refType);
        assertEquals(Integer.class, referenceType.getRawClass());
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = JavaType.construct(String.class);
        JavaType refType = JavaType.construct(List.class, JavaType.construct(Integer.class));
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(List.class, referenceType.getRawClass());
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testGetContentType() {
        JavaType refType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.TYPE, TypeBindings.emptyBindings(), null, new JavaType[0], null, referenceType, null, null, false);
        referenceType.setReferencedType(refType);
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.TYPE, TypeBindings.emptyBindings(), null, new JavaType[0], null, referenceType, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType refType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.TYPE, TypeBindings.emptyBindings(), null, new JavaType[0], null, referenceType, null, null, false);
        referenceType.setReferencedType(refType);
        assertTrue(referenceType.hasContentType());

        JavaType baseType = JavaType.construct(Integer.class);
        ReferenceType referenceType2 = new ReferenceType(baseType, null, null, null, null, null, null, null, false);
        assertFalse(referenceType2.hasContentType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.TYPE, TypeBindings.emptyBindings(), null, new JavaType[0], null, referenceType, null, null, false);
        referenceType.setReferencedType(refType);

        StringBuilder sb = new StringBuilder();
        referenceType.getErasedSignature(sb);
        assertEquals("java.lang.Integer", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.TYPE, TypeBindings.emptyBindings(), null, new JavaType[0], null, referenceType, null, null, false);
        referenceType.setReferencedType(refType);

        StringBuilder sb = new StringBuilder();
        referenceType.getGenericSignature(sb);
        assertEquals("[Ljava.lang.Object;<T:Ljava.lang.String;>;", sb.toString());
    }

    @Test
    public void testToString() {
        JavaType refType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.TYPE, TypeBindings.emptyBindings(), null, new JavaType[0], null, referenceType, null, null, false);
        referenceType.setReferencedType(refType);

        String actual = referenceType.toString();
        String expected = "[reference type, class [Ljava.lang.Object;<T:Ljava.lang.String;>;]";
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNullRefType() {
        JavaType baseType = JavaType.construct(String.class);
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructNullBaseType() {
        JavaType refType = JavaType.construct(String.class);
        ReferenceType.construct(null, TypeBindings.emptyBindings(), null, new JavaType[0], refType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructNullRefType() {
        JavaType baseType = JavaType.construct(Integer.class);
        ReferenceType.construct(baseType.getRawClass(), TypeBindings.emptyBindings(), null, new JavaType[0], null);
    }

    @Test
    public void testGetAnchorType() {
        JavaType refType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.TYPE, TypeBindings.emptyBindings(), null, new JavaType[0], null, referenceType, null, null, false);
        referenceType.setReferencedType(refType);

        ReferenceType anchorType = referenceType;
        ReferenceType actual = anchorType.getAnchorType();
        assertEquals(anchorType, actual);

        anchorType = referenceType.getSuperClass();
        actual = anchorType.getAnchorType();
        assertSame(referenceType, actual);
    }

    @Test
    public void testIsAnchorType() {
        JavaType refType = JavaType.construct(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.TYPE, TypeBindings.emptyBindings(), null, new JavaType[0], null, referenceType, null, null, false);
        referenceType.setReferencedType(refType);

        assertTrue(referenceType.isAnchorType());

        ReferenceType superType = referenceType.getSuperClass();
        assertFalse(superType.isAnchorType());
    }
}