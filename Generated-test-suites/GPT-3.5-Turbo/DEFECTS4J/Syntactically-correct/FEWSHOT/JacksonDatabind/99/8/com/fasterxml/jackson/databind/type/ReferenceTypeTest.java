package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = JavaType.STRING;
        ReferenceType referenceType = ReferenceType.construct(cls, bindings, superClass, superInts, refType);

        assertEquals(cls, referenceType.getRawClass());
        assertEquals(bindings, referenceType.getBindings());
        assertEquals(superClass, referenceType.getSuperClass());
        assertEquals(superInts, referenceType.getInterfaces());
        assertEquals(refType, referenceType.getReferencedType());
        assertNull(referenceType.getAnchorType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = JavaType.STRING;
        JavaType refdType = JavaType.STRING;
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refdType);

        assertEquals(baseType.getRawClass(), referenceType.getRawClass());
        assertEquals(baseType.getBindings(), referenceType.getBindings());
        assertEquals(baseType.getSuperClass(), referenceType.getSuperClass());
        assertEquals(baseType.getInterfaces(), referenceType.getInterfaces());
        assertEquals(refdType, referenceType.getReferencedType());
        assertTrue(referenceType.isAnchorType());

        try {
            ReferenceType.upgradeFrom(baseType, null);
        } catch (IllegalArgumentException e) {
            assertEquals("Missing referencedType", e.getMessage());
        }

        try {
            ReferenceType.upgradeFrom(JavaType.INT, refdType);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not upgrade from an instance of class com.fasterxml.jackson.databind.type.TypeBase", e.getMessage());
        }
    }

    @Test
    public void testWithContentType() {
        JavaType refType = JavaType.STRING;
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);

        JavaType contentType = JavaType.BOOLEAN;
        ReferenceType updatedType = referenceType.withContentType(contentType);

        assertEquals(contentType, updatedType.getReferencedType());
        assertEquals(referenceType, updatedType.getAnchorType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType refType = JavaType.STRING;
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);

        Object handler = new Object();
        ReferenceType updatedType = referenceType.withTypeHandler(handler);

        assertEquals(handler, updatedType.getTypeHandler());
        assertEquals(referenceType.getReferencedType().getTypeHandler(), updatedType.getReferencedType().getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType refType = JavaType.STRING;
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);

        Object handler = new Object();
        ReferenceType updatedType = referenceType.withContentTypeHandler(handler);

        assertEquals(handler, updatedType.getContentType().getTypeHandler());
        assertEquals(referenceType.getReferencedType().getTypeHandler(), updatedType.getReferencedType().getTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType refType = JavaType.STRING;
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);

        ReferenceType updatedType = referenceType.withStaticTyping();

        assertTrue(updatedType.isStatic());
        assertTrue(referenceType.getReferencedType().isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType refType = JavaType.STRING;
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, null, null, null, null, false);

        JavaType updatedType = referenceType.refine(rawType, bindings, superClass, superInterfaces);

        assertEquals(rawType, updatedType.getRawClass());
        assertEquals(bindings, updatedType.getBindings());
        assertEquals(superClass, updatedType.getSuperClass());
        assertEquals(superInterfaces, updatedType.getInterfaces());
        assertEquals(refType, updatedType.getReferencedType());
        assertNull(updatedType.getAnchorType());
    }

    @Test
    public void testGetAnchorType() {
        JavaType anchorType = JavaType.LONG;
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, null, anchorType, null, null, false);

        assertEquals(anchorType, referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        ReferenceType referenceType = new ReferenceType(String.class, null, null, null, null, null, null, null, false);

        assertTrue(referenceType.isAnchorType());

        JavaType anchorType = JavaType.LONG;
        ReferenceType updatedType = new ReferenceType(String.class, null, null, null, null, anchorType, null, null, false);

        assertFalse(updatedType.isAnchorType());
    }
    
    @Test
    public void testToString() {
        JavaType refType = JavaType.STRING;
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(), null, null, refType, null, null, null, false);
        assertEquals("[reference type, class java.lang.String<java.lang.String>]", referenceType.toString());
    }
}