package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.databind.JavaType;

public class ReferenceTypeTest {

    @Test
    public void testConstructor() {
        JavaType refType = JavaType.construct(String.class);
        JavaType baseType = JavaType.construct(Integer.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        assertNotNull(referenceType);
        assertEquals(String.class, referenceType.getContentType().getRawClass());
        assertEquals(Integer.class, referenceType.getRawClass());
        assertSame(refType, referenceType.getContentType());
    }

    @Test
    public void testConstruct() {
        JavaType refType = JavaType.construct(String[].class);
        JavaType baseType = JavaType.construct(List.class);
        ReferenceType referenceType = ReferenceType.construct(Integer[].class, new TypeBindings(), baseType, new JavaType[0], refType);
        assertNotNull(referenceType);
        assertEquals(Integer[].class, referenceType.getRawClass());
        assertEquals(List.class, baseType.getRawClass());
        assertEquals(String[].class, referenceType.getContentType().getRawClass());
        assertSame(refType, referenceType.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = JavaType.construct(String.class);
        JavaType referencedType = JavaType.construct(Integer.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, referencedType);
        assertNotNull(referenceType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(Integer.class, referenceType.getContentType().getRawClass());
        assertSame(referencedType, referenceType.getContentType());
    }

    @Test
    public void testWithContentType() {
        JavaType baseType = JavaType.construct(String.class);
        JavaType referencedType = JavaType.construct(Integer.class);
        JavaType contentType = JavaType.construct(Long.class);
        ReferenceType referenceType = new ReferenceType(baseType.getRawClass(), new TypeBindings(), baseType, new JavaType[0], referencedType);
        ReferenceType result = referenceType.withContentType(contentType);
        assertNotNull(result);
        assertEquals(String.class, result.getRawClass());
        assertEquals(Long.class, result.getContentType().getRawClass());
        assertSame(contentType, result.getContentType());
    }

    @Test
    public void testGetContentType() {
        JavaType baseType = JavaType.construct(String.class);
        JavaType referencedType = JavaType.construct(Integer.class);
        ReferenceType referenceType = new ReferenceType(baseType.getRawClass(), new TypeBindings(), baseType, new JavaType[0], referencedType);
        JavaType contentType = referenceType.getContentType();
        assertSame(referencedType, contentType);
    }

    @Test
    public void testIsReferenceType() {
        JavaType baseType = JavaType.construct(String.class);
        JavaType referencedType = JavaType.construct(Integer.class);
        ReferenceType referenceType = new ReferenceType(baseType.getRawClass(), new TypeBindings(), baseType, new JavaType[0], referencedType);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType baseType = JavaType.construct(String.class);
        JavaType referencedType = JavaType.construct(Integer.class);
        ReferenceType referenceType = new ReferenceType(baseType.getRawClass(), new TypeBindings(), baseType, new JavaType[0], referencedType);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType baseType = JavaType.construct(String[].class);
        JavaType referencedType = JavaType.construct(Integer[].class);
        ReferenceType referenceType = new ReferenceType(baseType.getRawClass(), new TypeBindings(), baseType, new JavaType[0], referencedType);
        StringBuilder expected = new StringBuilder("JavaType[]");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType baseType = JavaType.construct(String[].class);
        JavaType referencedType = JavaType.construct(Integer[].class);
        ReferenceType referenceType = new ReferenceType(baseType.getRawClass(), new TypeBindings(), baseType, new JavaType[0], referencedType);
        StringBuilder expected = new StringBuilder("JavaType<JavaType<");
        StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetAnchorType() {
        JavaType baseType = JavaType.construct(String.class);
        JavaType referencedType = JavaType.construct(Integer.class);
        ReferenceType referenceType = new ReferenceType(baseType.getRawClass(), new TypeBindings(), baseType, new JavaType[0], referencedType);
        assertNull(referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType baseType = JavaType.construct(String.class);
        JavaType referencedType = JavaType.construct(Integer.class);
        ReferenceType referenceType = new ReferenceType(baseType.getRawClass(), new TypeBindings(), baseType, new JavaType[0], referencedType);
        assertFalse(referenceType.isAnchorType());
    }
}