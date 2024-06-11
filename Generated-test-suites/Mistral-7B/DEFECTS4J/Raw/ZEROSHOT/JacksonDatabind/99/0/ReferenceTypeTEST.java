package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ReferenceTypeTest {

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(int[].class);
        JavaType refdType = new SimpleType(String.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refdType);
        assertEquals(String.class, referenceType.getContentType().getClass());
        assertEquals(int[].class, referenceType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithNullRefdType() {
        JavaType baseType = new SimpleType(int[].class);
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test
    public void testConstruct() {
        JavaType referencedType = new SimpleType(String.class);
        ReferenceType referenceType = ReferenceType.construct(int[].class, null, null, null, referencedType);
        assertEquals(String.class, referenceType.getContentType().getClass());
        assertEquals(int[].class, referenceType.getRawClass());
    }

    @Test
    public void testGetContentType() {
        JavaType referencedType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(int[].class, null, null, null, referencedType, null, null, null, false);
        assertEquals(referencedType, referenceType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType referencedType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(int[].class, null, null, null, referencedType, null, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType referencedType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(int[].class, null, null, null, referencedType, null, null, null, false);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType referencedType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(int[].class, null, null, null, referencedType, null, null, null, false);
        StringBuilder expected = new StringBuilder("[I");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType referencedType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(int[].class, null, null, null, referencedType, null, null, null, false);
        StringBuilder expected = new StringBuilder("[Ljava/lang/String;>");
        StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetAnchorType() {
        JavaType referencedType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(int[].class, null, null, null, referencedType, null, null, null, false);
        assertEquals(referenceType, referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType referencedType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(int[].class, null, null, null, referencedType, null, null, null, false);
        assertTrue(referenceType.isAnchorType());
    }

    @Test
    public void testToString() {
        JavaType referencedType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(int[].class, null, null, null, referencedType, null, null, null, false);
        String expected = "[reference type, class [I, class java.lang.String]";
        String actual = referenceType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        JavaType referencedType1 = new SimpleType(String.class);
        JavaType referencedType2 = new SimpleType(String.class);
        ReferenceType referenceType1 = new ReferenceType(int[].class, null, null, null, referencedType1, null, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(int[].class, null, null, null, referencedType2, null, null, null, false);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType2));
        JavaType referencedType3 = new SimpleType(Integer.class);
        ReferenceType referenceType3 = new ReferenceType(int[].class, null, null, null, referencedType3, null, null, null, false);
        assertFalse(referenceType1.equals(referenceType3));
    }
}