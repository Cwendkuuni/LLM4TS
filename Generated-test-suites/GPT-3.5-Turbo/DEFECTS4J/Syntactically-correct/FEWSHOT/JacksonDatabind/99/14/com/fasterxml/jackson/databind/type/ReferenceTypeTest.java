package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ReferenceTypeTest {
    @Test
    public void testConstruct() {
        // Test with non-null values
        ReferenceType refType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, null, null);
        assertEquals(String.class, refType.getRawClass());
        assertTrue(refType.hasContentType());

        // Test with null values
        ReferenceType refType2 = ReferenceType.construct(null, null);
        assertEquals(null, refType2.getRawClass());
        assertFalse(refType2.hasContentType());
    }

    @Test
    public void testUpgradeFrom() {
        // Test with valid baseType and refdType
        JavaType baseType = SimpleType.construct(String.class);
        JavaType refdType = SimpleType.construct(Integer.class);
        ReferenceType upgradedType = ReferenceType.upgradeFrom(baseType, refdType);
        assertEquals(baseType, upgradedType);
        assertEquals(refdType, upgradedType.getContentType());

        // Test with null refdType
        try {
            ReferenceType.upgradeFrom(baseType, null);
        } catch (IllegalArgumentException e) {
            assertEquals("Missing referencedType", e.getMessage());
        }

        // Test with invalid baseType
        JavaType invalidBaseType = new JavaType() {
            @Override
            public boolean hasContentType() {
                return false;
            }
        };
        try {
            ReferenceType.upgradeFrom(invalidBaseType, refdType);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not upgrade from an instance of class com.fasterxml.jackson.databind.type.JavaType", e.getMessage());
        }
    }

    @Test
    public void testWithContentType() {
        JavaType baseType = SimpleType.construct(String.class);
        JavaType refdType = SimpleType.construct(Integer.class);
        ReferenceType refType = ReferenceType.upgradeFrom(baseType, refdType);
        JavaType contentType = SimpleType.construct(Long.class);
        ReferenceType upgradedType = refType.withContentType(contentType);
        assertEquals(baseType, upgradedType);
        assertEquals(contentType, upgradedType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType baseType = SimpleType.construct(String.class);
        JavaType refdType = SimpleType.construct(Integer.class);
        ReferenceType refType = ReferenceType.upgradeFrom(baseType, refdType);

        Object handler = new Object();
        ReferenceType upgradedType = refType.withTypeHandler(handler);
        assertEquals(baseType, upgradedType);
        assertEquals(handler, upgradedType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType baseType = SimpleType.construct(String.class);
        JavaType refdType = SimpleType.construct(Integer.class);
        ReferenceType refType = ReferenceType.upgradeFrom(baseType, refdType);

        Object handler = new Object();
        ReferenceType upgradedType = refType.withContentTypeHandler(handler);
        assertEquals(baseType, upgradedType);
        assertEquals(handler, upgradedType.getContentType().getTypeHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType baseType = SimpleType.construct(String.class);
        JavaType refdType = SimpleType.construct(Integer.class);
        ReferenceType refType = ReferenceType.upgradeFrom(baseType, refdType);

        Object handler = new Object();
        ReferenceType upgradedType = refType.withContentValueHandler(handler);
        assertEquals(baseType, upgradedType);
        assertEquals(handler, upgradedType.getContentType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType baseType = SimpleType.construct(String.class);
        JavaType refdType = SimpleType.construct(Integer.class);
        ReferenceType refType = ReferenceType.upgradeFrom(baseType, refdType);

        ReferenceType upgradedType = refType.withStaticTyping();
        assertEquals(baseType, upgradedType);
        assertTrue(upgradedType.isStatic());
    }

    @Test
    public void testRefine() {
        ReferenceType refType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, null, null);
        JavaType superClass = SimpleType.construct(Object.class);
        JavaType[] superInterfaces = new JavaType[] { SimpleType.construct(Comparable.class) };

        ReferenceType refinedType = refType.refine(Object.class, TypeBindings.emptyBindings(), superClass, superInterfaces);
        assertEquals(Object.class, refinedType.getRawClass());
        assertEquals(superClass, refinedType.getSuperClass());
        assertArrayEquals(superInterfaces, refinedType.getInterfaces());
    }

    @Test
    public void testGetAnchorType() {
        JavaType baseType = SimpleType.construct(String.class);
        JavaType refdType = SimpleType.construct(Integer.class);
        ReferenceType refType = ReferenceType.upgradeFrom(baseType, refdType);

        assertEquals(refType, refType.getAnchorType());
        assertTrue(refType.isAnchorType());
        assertFalse(refdType.isAnchorType());
    }

    @Test
    public void testToString() {
        ReferenceType refType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, null, null);
        assertEquals("[reference type, class java.lang.String]", refType.toString());
    }

    @Test
    public void testEquals() {
        ReferenceType refType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, null, null);
        ReferenceType refType2 = ReferenceType.construct(String.class, TypeBindings.emptyBindings(), null, null, null);
        ReferenceType refType3 = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(), null, null, null);

        assertTrue(refType.equals(refType));
        assertTrue(refType.equals(refType2));
        assertFalse(refType.equals(refType3));
    }
}