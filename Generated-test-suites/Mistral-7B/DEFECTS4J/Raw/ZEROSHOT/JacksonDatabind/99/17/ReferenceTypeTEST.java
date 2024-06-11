package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        JavaType refType = TypeFactory.defaultInstance().constructValueType(int.class, null);
        JavaType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], refType);
        assertNotNull(referenceType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(int.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType intType = TypeFactory.defaultInstance().constructValueType(int.class, null);
        JavaType listIntType = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, intType);
        JavaType referenceType = ReferenceType.upgradeFrom(intType, listIntType);
        assertNotNull(referenceType);
        assertEquals(int.class, referenceType.getContentType().getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNullRefType() {
        JavaType intType = TypeFactory.defaultInstance().constructValueType(int.class, null);
        JavaType referenceType = ReferenceType.upgradeFrom(intType, null);
    }

    @Test
    public void testGetContentType() {
        JavaType refType = TypeFactory.defaultInstance().constructValueType(int.class, null);
        JavaType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], refType);
        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = TypeFactory.defaultInstance().constructValueType(int.class, null);
        JavaType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], refType);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType refType = TypeFactory.defaultInstance().constructValueType(int.class, null);
        JavaType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], refType);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = TypeFactory.defaultInstance().constructValueType(int.class, null);
        JavaType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], refType);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testToString() {
        JavaType refType = TypeFactory.defaultInstance().constructValueType(int.class, null);
        JavaType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], refType);
        String expectedToString = "[reference type, class java.lang.String<java.lang.Integer>]";
        assertEquals(expectedToString, referenceType.toString());
    }

    @Test
    public void testEquals() {
        JavaType intType1 = TypeFactory.defaultInstance().constructValueType(int.class, null);
        JavaType intType2 = TypeFactory.defaultInstance().constructValueType(int.class, null);
        JavaType referenceType1 = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], intType1);
        JavaType referenceType2 = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], intType2);
        JavaType referenceType3 = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], intType1);

        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType3));
        assertTrue(referenceType1.equals(referenceType3.getContentType()));
        assertTrue(referenceType1.equals(referenceType3.getReferencedType()));
        assertFalse(referenceType1.equals(intType1));
        assertFalse(referenceType1.equals(referenceType2));
    }

    @Test
    public void testGetAnchorType() {
        JavaType intType = TypeFactory.defaultInstance().constructValueType(int.class, null);
        JavaType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], intType);
        JavaType anchorType = referenceType.getAnchorType();
        assertEquals(referenceType, anchorType);
    }

    @Test
    public void testIsAnchorType() {
        JavaType intType = TypeFactory.defaultInstance().constructValueType(int.class, null);
        JavaType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], intType);
        assertTrue(referenceType.isAnchorType());
    }

    @Test
    public void testNarrow() {
        JavaType intType = TypeFactory.defaultInstance().constructValueType(int.class, null);
        JavaType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, new JavaType[0], intType);
        JavaType subclass = TypeFactory.defaultInstance().constructValueType(Integer.TYPE, null);
        JavaType narrowedType = referenceType._narrow(subclass.getRawClass());
        assertNotNull(narrowedType);
        assertEquals(subclass, narrowedType);
    }
}