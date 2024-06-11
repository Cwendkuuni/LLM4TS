package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ReferenceTypeTest {

    @Test
    public void testConstructorWithRefType() {
        JavaType refType = new SimpleType();
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, refType, null, null, null, false);
        assertEquals(Object.class, referenceType.getRawClass());
        assertNull(referenceType.getBindings());
        assertNull(referenceType.getSuperClass());
        assertNull(referenceType.getSuperInterfaces());
        assertEquals(refType.hashCode(), referenceType.hashCode());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertFalse(referenceType.isStatic());
        assertEquals(refType, referenceType.getContentType());
        assertEquals(refType, referenceType.getReferencedType());
        assertTrue(referenceType.hasContentType());
        assertTrue(referenceType.isReferenceType());
        assertNotNull(referenceType.getErasedSignature(new StringBuilder()));
        assertNotNull(referenceType.getGenericSignature(new StringBuilder()));
        assertEquals(refType, referenceType.getAnchorType());
        assertTrue(referenceType.isAnchorType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullRefType() {
        new ReferenceType(Object.class, null, null, null, null, null, null, null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidBaseTypeInstance() {
        new ReferenceType(new JavaType() {}, new SimpleType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(Long.class);
        JavaType refdType = new SimpleType(String.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refdType);
        assertEquals(Long.class, referenceType.getRawClass());
        assertNull(referenceType.getBindings());
        assertNull(referenceType.getSuperClass());
        assertNull(referenceType.getSuperInterfaces());
        assertEquals(refdType.hashCode(), referenceType.hashCode());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertFalse(referenceType.isStatic());
        assertEquals(refdType, referenceType.getContentType());
        assertEquals(refdType, referenceType.getReferencedType());
        assertTrue(referenceType.hasContentType());
        assertTrue(referenceType.isReferenceType());
        assertNotNull(referenceType.getErasedSignature(new StringBuilder()));
        assertNotNull(referenceType.getGenericSignature(new StringBuilder()));
        assertNotNull(referenceType.getAnchorType());
        assertTrue(referenceType.isAnchorType());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithNullRefdType() {
        JavaType baseType = new SimpleType(Long.class);
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test
    public void testConstruct() {
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = ReferenceType.construct(ReferenceType.class, null, null, null, refType);
        assertEquals(ReferenceType.class, referenceType.getRawClass());
        assertNull(referenceType.getBindings());
        assertNull(referenceType.getSuperClass());
        assertNull(referenceType.getSuperInterfaces());
        assertEquals(refType.hashCode(), referenceType.hashCode());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertFalse(referenceType.isStatic());
        assertEquals(refType, referenceType.getContentType());
        assertEquals(refType, referenceType.getReferencedType());
        assertTrue(referenceType.hasContentType());
        assertTrue(referenceType.isReferenceType());
        assertNotNull(referenceType.getErasedSignature(new StringBuilder()));
        assertNotNull(referenceType.getGenericSignature(new StringBuilder()));
        assertNotNull(referenceType.getAnchorType());
        assertTrue(referenceType.isAnchorType());
    }

    @Test
    public void testConstructWithDeprecatedMethod() {
        JavaType refType = new SimpleType(String.class);
        ReferenceType referenceType = ReferenceType.construct(ReferenceType.class, refType);
        assertEquals(ReferenceType.class, referenceType.getRawClass());
        assertNull(referenceType.getBindings());
        assertNull(referenceType.getSuperClass());
        assertNull(referenceType.getSuperInterfaces());
        assertEquals(refType.hashCode(), referenceType.hashCode());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertFalse(referenceType.isStatic());
        assertEquals(refType, referenceType.getContentType());
        assertEquals(refType, referenceType.getReferencedType());
        assertTrue(referenceType.hasContentType());
        assertTrue(referenceType.isReferenceType());
        assertNotNull(referenceType.getErasedSignature(new StringBuilder()));
        assertNotNull(referenceType.getGenericSignature(new StringBuilder()));
        assertNotNull(referenceType.getAnchorType());
        assertTrue(referenceType.isAnchorType());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new SimpleType(String.class);
        ReferenceType referenceType = createReferenceType();
        ReferenceType result = referenceType.withContentType(contentType);
        assertNotSame(referenceType, result);
        assertEquals(String.class, result.getContentType().getRawClass());
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        ReferenceType referenceType = createReferenceType();
        ReferenceType result = referenceType.withTypeHandler(handler);
        assertNotSame(referenceType, result);
        assertSame(handler, result.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        ReferenceType referenceType = createReferenceType();
        ReferenceType result = referenceType.withContentTypeHandler(handler);
        assertNotSame(referenceType, result);
        assertSame(handler, result.getContentType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        ReferenceType referenceType = createReferenceType();
        ReferenceType result = referenceType.withValueHandler(handler);
        assertNotSame(referenceType, result);
        assertSame(handler, result.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        ReferenceType referenceType = createReferenceType();
        ReferenceType result = referenceType.withContentValueHandler(handler);
        assertNotSame(referenceType, result);
        assertSame(handler, result.getContentType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = createReferenceType();
        ReferenceType result = referenceType.withStaticTyping();
        assertNotSame(referenceType, result);
        assertTrue(result.isStatic());
        assertTrue(result.getContentType().isStatic());
    }

    @Test
    public void testRefine() {
        ReferenceType referenceType = createReferenceType();
        ReferenceType result = referenceType.refine(Object.class, null, null, null);
        assertNotSame(referenceType, result);
        assertEquals(Object.class, result.getRawClass());
    }

    @Test
    public void testToString() {
        ReferenceType referenceType = createReferenceType();
        assertNotNull(referenceType.toString());
    }

    @Test
    public void testEqualsWithSameObject() {
        ReferenceType referenceType = createReferenceType();
        assertTrue(referenceType.equals(referenceType));
    }

    @Test
    public void testEqualsWithEqualObjects() {
        ReferenceType referenceType1 = createReferenceType();
        ReferenceType referenceType2 = createReferenceType();
        assertTrue(referenceType1.equals(referenceType2));
        assertTrue(referenceType2.equals(referenceType1));
    }

    @Test
    public void testEqualsWithDifferentObjects() {
        ReferenceType referenceType1 = createReferenceType();
        ReferenceType referenceType2 = new ReferenceType(SimpleType.class, null, null, null,
                new SimpleType(Integer.class), null, null, null, false);
        assertFalse(referenceType1.equals(referenceType2));
        assertFalse(referenceType2.equals(referenceType1));
    }

    private ReferenceType createReferenceType() {
        JavaType refType = new SimpleType(String.class);
        return new ReferenceType(ReferenceType.class, null, null, null,
                refType, null, null, null, false);
    }
}