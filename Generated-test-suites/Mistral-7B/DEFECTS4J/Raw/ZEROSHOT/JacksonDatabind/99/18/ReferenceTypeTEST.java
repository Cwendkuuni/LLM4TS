package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testReferenceType() {
        JavaType baseType = JavaType.construct(String.class, TypeBindings.emptyBindings());
        JavaType refdType = JavaType.construct(Integer.class, TypeBindings.emptyBindings());
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refdType);

        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(referenceType.getClass(), referenceType.getClass());
        assertEquals(JavaType.construct(String.class, TypeBindings.emptyBindings()), referenceType.getSuperClass());
        assertNull(referenceType.getSuperInterfaces());
        assertNotNull(referenceType.getContentType());
        assertEquals(Integer.class, referenceType.getContentType().getRawClass());
        assertTrue(referenceType.isReferenceType());
        assertFalse(referenceType.isValueType());
        assertFalse(referenceType.isContainerType());
        assertFalse(referenceType.isPrimitive());
        assertFalse(referenceType.isLong());
        assertFalse(referenceType.isInt());
        assertFalse(referenceType.isShort());
        assertFalse(referenceType.isByte());
        assertFalse(referenceType.isBoolean());
        assertFalse(referenceType.isChar());
        assertFalse(referenceType.isNumber());
        assertFalse(referenceType.isCollectionLike());
        assertFalse(referenceType.isMapLike());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertNull(referenceType.getAnchorType());
        assertFalse(referenceType.isAnchorType());
        assertEquals(referenceType, referenceType.withContentType(refdType));
        assertEquals(referenceType, referenceType.withTypeHandler(null));
        assertEquals(referenceType, referenceType.withContentTypeHandler(null));
        assertEquals(referenceType, referenceType.withValueHandler(null));
        assertEquals(referenceType, referenceType.withContentValueHandler(null));
        assertEquals(referenceType, referenceType.withStaticTyping());
        assertEquals(referenceType, referenceType.refine(String.class, TypeBindings.emptyBindings(), null, null));
        assertEquals(ReferenceType.class, referenceType.getClass());
    }

    @Test
    public void testReferenceTypeWithAnchorType() {
        JavaType baseType = JavaType.construct(String.class, TypeBindings.emptyBindings());
        JavaType refdType = JavaType.construct(Integer.class, TypeBindings.emptyBindings());
        JavaType anchorType = JavaType.construct(Long.class, TypeBindings.emptyBindings());
        ReferenceType referenceType = new ReferenceType(baseType, refdType, null, null, null, anchorType, null, null, false);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(referenceType.getClass(), referenceType.getClass());
        assertEquals(JavaType.construct(String.class, TypeBindings.emptyBindings()), referenceType.getSuperClass());
        assertNull(referenceType.getSuperInterfaces());
        assertNotNull(referenceType.getContentType());
        assertEquals(Integer.class, referenceType.getContentType().getRawClass());
        assertTrue(referenceType.isReferenceType());
        assertFalse(referenceType.isValueType());
        assertFalse(referenceType.isContainerType());
        assertFalse(referenceType.isPrimitive());
        assertFalse(referenceType.isLong());
        assertFalse(referenceType.isInt());
        assertFalse(referenceType.isShort());
        assertFalse(referenceType.isByte());
        assertFalse(referenceType.isBoolean());
        assertFalse(referenceType.isChar());
        assertFalse(referenceType.isNumber());
        assertFalse(referenceType.isCollectionLike());
        assertFalse(referenceType.isMapLike());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertEquals(anchorType, referenceType.getAnchorType());
        assertTrue(referenceType.isAnchorType());
    }

    @Test
    public void testReferenceTypeConstructors() {
        JavaType baseType = JavaType.construct(String.class, TypeBindings.emptyBindings());
        JavaType refdType = JavaType.construct(Integer.class, TypeBindings.emptyBindings());
        JavaType refdType2 = JavaType.construct(Long.class, TypeBindings.emptyBindings());
        ReferenceType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, null, refdType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(referenceType.getClass(), referenceType.getClass());
        assertEquals(JavaType.construct(String.class, TypeBindings.emptyBindings()), referenceType.getSuperClass());
        assertNull(referenceType.getSuperInterfaces());
        assertNotNull(referenceType.getContentType());
        assertEquals(Integer.class, referenceType.getContentType().getRawClass());
        assertTrue(referenceType.isReferenceType());
        assertFalse(referenceType.isValueType());
        assertFalse(referenceType.isContainerType());
        assertFalse(referenceType.isPrimitive());
        assertFalse(referenceType.isLong());
        assertFalse(referenceType.isInt());
        assertFalse(referenceType.isShort());
        assertFalse(referenceType.isByte());
        assertFalse(referenceType.isBoolean());
        assertFalse(referenceType.isChar());
        assertFalse(referenceType.isNumber());
        assertFalse(referenceType.isCollectionLike());
        assertFalse(referenceType.isMapLike());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertNull(referenceType.getAnchorType());
        assertFalse(referenceType.isAnchorType());
        ReferenceType referenceType2 = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, null, refdType2);
        assertEquals(String.class, referenceType2.getRawClass());
        assertEquals(referenceType2.getClass(), referenceType2.getClass());
        assertEquals(JavaType.construct(String.class, TypeBindings.emptyBindings()), referenceType2.getSuperClass());
        assertNull(referenceType2.getSuperInterfaces());
        assertNotNull(referenceType2.getContentType());
        assertEquals(Long.class, referenceType2.getContentType().getRawClass());
        assertTrue(referenceType2.isReferenceType());
        assertFalse(referenceType2.isValueType());
        assertFalse(referenceType2.isContainerType());
        assertFalse(referenceType2.isPrimitive());
        assertFalse(referenceType2.isLong());
        assertFalse(referenceType2.isInt());
        assertFalse(referenceType2.isShort());
        assertFalse(referenceType2.isByte());
        assertFalse(referenceType2.isBoolean());
        assertFalse(referenceType2.isChar());
        assertFalse(referenceType2.isNumber());
        assertFalse(referenceType2.isCollectionLike());
        assertFalse(referenceType2.isMapLike());
        assertNull(referenceType2.getValueHandler());
        assertNull(referenceType2.getTypeHandler());
        assertNull(referenceType2.getAnchorType());
        assertFalse(referenceType2.isAnchorType());
        ReferenceType referenceType3 = new ReferenceType(referenceType, refdType);
        assertEquals(String.class, referenceType3.getRawClass());
        assertEquals(referenceType3.getClass(), referenceType3.getClass());
        assertEquals(JavaType.construct(String.class, TypeBindings.emptyBindings()), referenceType3.getSuperClass());
        assertNull(referenceType3.getSuperInterfaces());
        assertNotNull(referenceType3.getContentType());
        assertEquals(Integer.class, referenceType3.getContentType().getRawClass());
        assertTrue(referenceType3.isReferenceType());
        assertFalse(referenceType3.isValueType());
        assertFalse(referenceType3.isContainerType());
        assertFalse(referenceType3.isPrimitive());
        assertFalse(referenceType3.isLong());
        assertFalse(referenceType3.isInt());
        assertFalse(referenceType3.isShort());
        assertFalse(referenceType3.isByte());
        assertFalse(referenceType3.isBoolean());
        assertFalse(referenceType3.isChar());
        assertFalse(referenceType3.isNumber());
        assertFalse(referenceType3.isCollectionLike());
        assertFalse(referenceType3.isMapLike());
        assertNull(referenceType3.getValueHandler());
        assertNull(referenceType3.getTypeHandler());
        assertNull(referenceType3.getAnchorType());
        assertFalse(referenceType3.isAnchorType());
    }

    @Test
    public void testReferenceTypeDeprecatedConstructors() {
        JavaType baseType = JavaType.construct(String.class, TypeBindings.emptyBindings());
        JavaType refdType = JavaType.construct(Integer.class, TypeBindings.emptyBindings());
        ReferenceType referenceType = new ReferenceType(baseType, refdType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(referenceType.getClass(), referenceType.getClass());
        assertEquals(JavaType.construct(String.class, TypeBindings.emptyBindings()), referenceType.getSuperClass());
        assertNull(referenceType.getSuperInterfaces());
        assertNotNull(referenceType.getContentType());
        assertEquals(Integer.class, referenceType.getContentType().getRawClass());
        assertTrue(referenceType.isReferenceType());
        assertFalse(referenceType.isValueType());
        assertFalse(referenceType.isContainerType());
        assertFalse(referenceType.isPrimitive());
        assertFalse(referenceType.isLong());
        assertFalse(referenceType.isInt());
        assertFalse(referenceType.isShort());
        assertFalse(referenceType.isByte());
        assertFalse(referenceType.isBoolean());
        assertFalse(referenceType.isChar());
        assertFalse(referenceType.isNumber());
        assertFalse(referenceType.isCollectionLike());
        assertFalse(referenceType.isMapLike());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertNull(referenceType.getAnchorType());
        assertFalse(referenceType.isAnchorType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidUpgradeFrom() {
        JavaType baseType = JavaType.construct(String.class, TypeBindings.emptyBindings());
        JavaType refdType = JavaType.construct(Integer.class, TypeBindings.emptyBindings());
        JavaType invalidType = JavaType.construct(Double.class, TypeBindings.emptyBindings());
        ReferenceType.upgradeFrom(baseType, refdType, invalidType);
    }
}