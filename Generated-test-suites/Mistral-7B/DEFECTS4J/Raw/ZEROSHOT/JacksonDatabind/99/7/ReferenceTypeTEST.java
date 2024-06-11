package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, new JavaType[0], refType);
        assertEquals(Integer.class, referenceType.getRawClass());
        assertEquals(String.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.upgradeFrom(stringType, intType);
        assertEquals(Integer.class, referenceType.getRawClass());
        assertEquals(String.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testGetContentType() {
        JavaType referenceType = TypeFactory.defaultInstance().constructType(ReferenceType.class);
        JavaType contentType = TypeFactory.defaultInstance().constructType(String.class);
        referenceType = new ReferenceType(ReferenceType.class, TypeBindings.emptyBindings(), null, new JavaType[0], contentType, null, null, null, false);
        assertEquals(String.class, referenceType.getContentType().getRawClass());
    }

    @Test
    public void testIsReferenceType() {
        JavaType referenceType = TypeFactory.defaultInstance().constructType(ReferenceType.class);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testNarrow() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType integerType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.upgradeFrom(stringType, integerType);
        JavaType narrowedType = referenceType.narrow(Integer.class);
        assertEquals(Integer.class, narrowedType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowInvalidSubclass() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType integerType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.upgradeFrom(stringType, integerType);
        referenceType = new ReferenceType(Integer.class, TypeBindings.emptyBindings(), null, new JavaType[0], referenceType, null, null, null, false);
        referenceType.narrow(Long.class);
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType integerType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.upgradeFrom(stringType, integerType);
        assertEquals("class java.lang.Integer<java.lang.String>", referenceType.buildCanonicalName());
    }

    @Test
    public void testWithContentType() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType integerType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.upgradeFrom(stringType, integerType);
        JavaType newContentType = TypeFactory.defaultInstance().constructType(Double.class);
        JavaType newReferenceType = referenceType.withContentType(newContentType);
        assertEquals(Integer.class, referenceType.getRawClass());
        assertEquals(Double.class, newReferenceType.getContentType().getRawClass());
    }

    @Test
    public void testGetReferencedType() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType integerType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.upgradeFrom(stringType, integerType);
        assertEquals(integerType, referenceType.getReferencedType());
    }

    @Test
    public void testHasContentType() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType integerType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.upgradeFrom(stringType, integerType);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetAnchorType() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType integerType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.upgradeFrom(stringType, integerType);
        JavaType anchorType = referenceType.getAnchorType();
        assertEquals(referenceType, anchorType);
    }

    @Test
    public void testIsAnchorType() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType integerType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.upgradeFrom(stringType, integerType);
        assertTrue(referenceType.isAnchorType());
    }

    @Test
    public void testToString() {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType integerType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType = ReferenceType.upgradeFrom(stringType, integerType);
        assertEquals("[reference type, class java.lang.Integer<java.lang.String>]", referenceType.toString());
    }

    @Test
    public void testEquals() {
        JavaType stringType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType stringType2 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType integerType1 = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType integerType2 = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType referenceType1 = ReferenceType.upgradeFrom(stringType1, integerType1);
        JavaType referenceType2 = ReferenceType.upgradeFrom(stringType2, integerType2);
        JavaType identicalReferenceType = new ReferenceType(referenceType1.getClass(), referenceType1._bindings, referenceType1._superClass, referenceType1._superInterfaces, referenceType1._referencedType, referenceType1._anchorType, referenceType1._valueHandler, referenceType1._typeHandler, referenceType1._asStatic);
        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(identicalReferenceType));
        assertFalse(referenceType1.equals(stringType1));
        assertFalse(referenceType1.equals(integerType1));
        assertFalse(referenceType1.equals(null));
        assertFalse(referenceType1.equals(""));
        assertFalse(referenceType1.equals(Boolean.TRUE));
        JavaType differentReferenceType = new ReferenceType(referenceType1.getClass(), referenceType1._bindings, referenceType1._superClass, referenceType1._superInterfaces, new JavaType(int.class), referenceType1._anchorType, referenceType1._valueHandler, referenceType1._typeHandler, referenceType1._asStatic);
        assertFalse(referenceType1.equals(differentReferenceType));
    }
}