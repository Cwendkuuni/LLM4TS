package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstructWithClassesAndBindingsAndSuperClassAndSuperInterfacesAndRefType() {
        JavaType referencedType = new SimpleType(int[].class, TypeBindings.emptyBindings(), null, null);
        ReferenceType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, null, referencedType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), referenceType.getBindings());
        assertEquals(null, referenceType.getSuperClass());
        assertNull(referenceType.getSuperInterfaces());
        assertEquals(referencedType, referenceType.getContentType());
    }

    @Test
    public void testConstructWithClassesAndBindingsAndRefType() {
        JavaType referencedType = new SimpleType(int[].class, TypeBindings.emptyBindings(), null, null);
        ReferenceType referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, referencedType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), referenceType.getBindings());
        assertEquals(null, referenceType.getSuperClass());
        assertEquals(referencedType, referenceType.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new ClassType(int[].class);
        JavaType referencedType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, referencedType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), referenceType.getBindings());
        assertEquals(null, referenceType.getSuperClass());
        assertEquals(referencedType, referenceType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithNullRefType() {
        JavaType baseType = new ClassType(int[].class);
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithNonReferenceTypeBase() {
        JavaType baseType = new SimpleType(String.class);
        JavaType referencedType = new SimpleType(int[].class, TypeBindings.emptyBindings(), null, null);
        ReferenceType.upgradeFrom(baseType, referencedType);
    }

    @Test
    public void testWithContentType() {
        JavaType baseType = new ClassType(int[].class);
        JavaType referencedType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        JavaType newContentType = new SimpleType(int.class, TypeBindings.emptyBindings(), null, null);
        ReferenceType referenceType = new ReferenceType(baseType, referencedType);
        ReferenceType newReferenceType = referenceType.withContentType(newContentType);
        assertEquals(int.class, newReferenceType.getContentType().getRawClass());
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(referenceType.getBindings(), newReferenceType.getBindings());
        assertEquals(referenceType.getSuperClass(), newReferenceType.getSuperClass());
        assertEquals(referenceType.getSuperInterfaces(), newReferenceType.getSuperInterfaces());
        assertNotSame(referencedType, newReferenceType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType baseType = new ClassType(int[].class);
        JavaType referencedType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        ReferenceType referenceType = new ReferenceType(baseType, referencedType);
        Object handler = new Object();
        ReferenceType newReferenceType = referenceType.withTypeHandler(handler);
        assertNotSame(referenceType.getTypeHandler(), newReferenceType.getTypeHandler());
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(referenceType.getBindings(), newReferenceType.getBindings());
        assertEquals(referenceType.getSuperClass(), newReferenceType.getSuperClass());
        assertEquals(referenceType.getSuperInterfaces(), newReferenceType.getSuperInterfaces());
        assertEquals(referenceType.getContentType(), newReferenceType.getContentType());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType baseType = new ClassType(int[].class);
        JavaType referencedType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        ReferenceType referenceType = new ReferenceType(baseType, referencedType);
        Object contentValueHandler = new Object();
        JavaType newContentType = new SimpleType(int.class, TypeBindings.emptyBindings(), null, null);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(contentValueHandler).withContentType(newContentType);
        assertEquals(int.class, newReferenceType.getContentType().getRawClass());
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(referenceType.getBindings(), newReferenceType.getBindings());
        assertEquals(referenceType.getSuperClass(), newReferenceType.getSuperClass());
        assertEquals(referenceType.getSuperInterfaces(), newReferenceType.getSuperInterfaces());
        assertNotSame(referencedType, newReferenceType.getContentType());
        assertNotSame(contentValueHandler, newReferenceType.getValueHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType baseType = new ClassType(int[].class);
        JavaType referencedType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        ReferenceType referenceType = new ReferenceType(baseType, referencedType);
        Object handler = new Object();
        ReferenceType newReferenceType = referenceType.withValueHandler(handler);
        assertNotSame(referenceType.getValueHandler(), newReferenceType.getValueHandler());
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(referenceType.getBindings(), newReferenceType.getBindings());
        assertEquals(referenceType.getSuperClass(), newReferenceType.getSuperClass());
        assertEquals(referenceType.getSuperInterfaces(), newReferenceType.getSuperInterfaces());
        assertEquals(referenceType.getContentType(), newReferenceType.getContentType());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType baseType = new ClassType(int[].class);
        JavaType referencedType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        ReferenceType referenceType = new ReferenceType(baseType, referencedType);
        Object contentValueHandler = new Object();
        JavaType newContentType = new SimpleType(int.class, TypeBindings.emptyBindings(), null, null);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(contentValueHandler).withContentType(newContentType);
        assertEquals(int.class, newReferenceType.getContentType().getRawClass());
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(referenceType.getBindings(), newReferenceType.getBindings());
        assertEquals(referenceType.getSuperClass(), newReferenceType.getSuperClass());
        assertEquals(referenceType.getSuperInterfaces(), newReferenceType.getSuperInterfaces());
        assertNotSame(referencedType, newReferenceType.getContentType());
        assertNotSame(contentValueHandler, newReferenceType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType baseType = new ClassType(int[].class);
        JavaType referencedType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        ReferenceType referenceType = new ReferenceType(baseType, referencedType);
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertTrue(newReferenceType.isStaticTyping());
        assertEquals(referenceType.getRawClass(), newReferenceType.getRawClass());
        assertEquals(referenceType.getBindings(), newReferenceType.getBindings());
        assertEquals(referenceType.getSuperClass(), newReferenceType.getSuperClass());
        assertEquals(referenceType.getSuperInterfaces(), newReferenceType.getSuperInterfaces());
        assertEquals(referenceType.getContentType(), newReferenceType.getContentType());
    }

    @Test
    public void testRefine() {
        JavaType baseType = new ClassType(int[].class);
        JavaType referencedType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        ReferenceType referenceType = new ReferenceType(baseType, referencedType);
        JavaType newBaseType = new ClassType(int[].class, null, String[].class, null);
        JavaType newReferencedType = new SimpleType(String.class, null, null, null);
        JavaType newReferenceType = referenceType.refine(newBaseType, TypeBindings.emptyBindings(), null, newReferencedType);
        assertEquals(newBaseType, newReferenceType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), newReferenceType.getBindings());
        assertEquals(newReferencedType, newReferenceType.getContentType());
        assertNull(newReferenceType.getSuperClass());
        assertNull(newReferenceType.getSuperInterfaces());
    }

    @Test
    public void testGetContentType() {
        JavaType baseType = new ClassType(int[].class);
        JavaType referencedType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        ReferenceType referenceType = new ReferenceType(baseType, referencedType);
        assertEquals(referencedType, referenceType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType baseType = new ClassType(int[].class);
        JavaType referencedType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        ReferenceType referenceType = new ReferenceType(baseType, referencedType);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testGetAnchortType() {
        JavaType baseType = new ClassType(int[].class);
        JavaType referencedType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        ReferenceType referenceType = new ReferenceType(baseType, referencedType);
        assertEquals(referenceType, referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType baseType = new ClassType(int[].class);
        JavaType referencedType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        ReferenceType referenceType = new ReferenceType(baseType, referencedType);
        assertFalse(referenceType.isAnchorType());
    }
}