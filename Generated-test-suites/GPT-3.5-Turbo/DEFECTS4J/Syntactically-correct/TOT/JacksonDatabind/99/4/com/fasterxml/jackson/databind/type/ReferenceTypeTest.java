package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReferenceTypeTest {

    private ReferenceType referenceType;

    @Before
    public void setUp() {
        referenceType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(),
                null, null, null, JavaTypeBuilder.create(SimpleType.class).build(), null, null, false);
    }

    @Test
    public void testConstructorWithNullRefType() {
        try {
            ReferenceType.construct(String.class, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Missing referencedType", e.getMessage());
        }
    }

    @Test
    public void testConstructorWithNullBaseType() {
        try {
            ReferenceType.upgradeFrom(null, JavaTypeBuilder.create(SimpleType.class).build());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not upgrade from an instance of class com.fasterxml.jackson.databind.JavaTypeBuilder$SimpleTypeBuilder", e.getMessage());
        }
    }

    @Test
    public void testWithContentType() {
        JavaType dataType = JavaTypeBuilder.create(SimpleType.class).build();
        ReferenceType newRefType = referenceType.withContentType(dataType);
        assertNotSame(referenceType, newRefType);
        assertEquals(dataType, newRefType.getContentType());
        assertEquals(referenceType.getReferencedType(), newRefType.getReferencedType());
        assertEquals(referenceType.getAnchorType(), newRefType.getAnchorType());
        assertEquals(referenceType.getClass(), newRefType.getClass());
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        ReferenceType newRefType = referenceType.withTypeHandler(typeHandler);
        assertNotSame(referenceType, newRefType);
        assertEquals(referenceType.getContentType(), newRefType.getContentType());
        assertEquals(referenceType.getReferencedType(), newRefType.getReferencedType());
        assertNotSame(referenceType.getTypeHandler(), newRefType.getTypeHandler());
        assertEquals(typeHandler, newRefType.getTypeHandler());
        assertEquals(referenceType.getAnchorType(), newRefType.getAnchorType());
        assertEquals(referenceType.getClass(), newRefType.getClass());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object contentTypeHandler = new Object();
        ReferenceType newRefType = referenceType.withContentTypeHandler(contentTypeHandler);
        assertNotSame(referenceType, newRefType);
        assertNotSame(referenceType.getContentType().getTypeHandler(),
                newRefType.getContentType().getTypeHandler());
        assertEquals(contentTypeHandler, newRefType.getContentType().getTypeHandler());
        assertEquals(referenceType.getReferencedType(), newRefType.getReferencedType());
        assertEquals(referenceType.getTypeHandler(), newRefType.getTypeHandler());
        assertEquals(referenceType.getAnchorType(), newRefType.getAnchorType());
        assertEquals(referenceType.getClass(), newRefType.getClass());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType newRefType = referenceType.withStaticTyping();
        assertNotSame(referenceType, newRefType);
        assertEquals(referenceType.getContentType().getRawClass(),
                newRefType.getContentType().getRawClass());
        assertTrue(newRefType.isStaticTyping());
        assertEquals(referenceType.getReferencedType(), newRefType.getReferencedType());
        assertEquals(referenceType.getTypeHandler(), newRefType.getTypeHandler());
        assertEquals(referenceType.getAnchorType(), newRefType.getAnchorType());
        assertEquals(referenceType.getClass(), newRefType.getClass());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Object.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        ReferenceType newRefType = referenceType.refine(rawType, bindings, null, null);
        assertNotSame(referenceType, newRefType);
        assertEquals(rawType, newRefType.getRawClass());
        assertEquals(bindings, newRefType.getBindings());
        assertEquals(referenceType.getContentType(), newRefType.getContentType());
        assertEquals(referenceType.getReferencedType(), newRefType.getReferencedType());
        assertEquals(referenceType.getTypeHandler(), newRefType.getTypeHandler());
        assertEquals(referenceType.getAnchorType(), newRefType.getAnchorType());
        assertEquals(referenceType.getClass(), newRefType.getClass());
    }

    @Test
    public void testGetContentType() {
        assertNotNull(referenceType.getContentType());
        assertEquals(SimpleType.class, referenceType.getContentType().getClass());
    }

    @Test
    public void testGetReferencedType() {
        JavaType referencedType = JavaTypeBuilder.create(SimpleType.class).build();
        assertNotNull(referenceType.getReferencedType());
        assertEquals(referencedType, referenceType.getReferencedType());
    }

    @Test
    public void testHasContentType() {
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testIsReferenceType() {
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testGetErasedSignature() {
        String expectedSignature = "com.fasterxml.jackson.databind.type.ReferenceType<java.lang.String>";
        assertEquals(expectedSignature, referenceType.getErasedSignature().toString());
    }

    @Test
    public void testGetGenericSignature() {
        String expectedSignature = "com.fasterxml.jackson.databind.type.ReferenceType<Lcom/fasterxml/jackson/databind/type/SimpleType;>;";
        assertEquals(expectedSignature, referenceType.getGenericSignature().toString());
    }

    @Test
    public void testGetAnchorType() {
        assertNotNull(referenceType.getAnchorType());
        assertSame(referenceType, referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        assertTrue(referenceType.isAnchorType());
    }

    @Test
    public void testToString() {
        String expectedString = "[reference type, class com.fasterxml.jackson.databind.type.ReferenceType<com.fasterxml.jackson.databind.type.SimpleType>]";
        assertEquals(expectedString, referenceType.toString());
    }

    @Test
    public void testEqualsWithSameReferenceType() {
        ReferenceType sameRefType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(),
                null, null, null, JavaTypeBuilder.create(SimpleType.class).build(), null, null, false);
        assertTrue(referenceType.equals(sameRefType));
    }

    @Test
    public void testEqualsWithDifferentReferenceType() {
        ReferenceType diffRefType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(),
                null, null, null, JavaTypeBuilder.create(SimpleType.class).build(), null, null, false);
        assertFalse(referenceType.equals(diffRefType));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        assertFalse(referenceType.equals(new Object()));
    }

    @Test
    public void testEqualsWithNull() {
        assertFalse(referenceType.equals(null));
    }

    @Test
    public void testEqualsAndHashCodeWithSameReferenceType() {
        ReferenceType sameRefType = ReferenceType.construct(String.class, TypeBindings.emptyBindings(),
                null, null, null, JavaTypeBuilder.create(SimpleType.class).build(), null, null, false);
        assertTrue(referenceType.equals(sameRefType));
        assertEquals(referenceType.hashCode(), sameRefType.hashCode());
    }

    @Test
    public void testEqualsAndHashCodeWithDifferentReferenceType() {
        ReferenceType diffRefType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(),
                null, null, null, JavaTypeBuilder.create(SimpleType.class).build(), null, null, false);
        assertFalse(referenceType.equals(diffRefType));
        assertNotEquals(referenceType.hashCode(), diffRefType.hashCode());
    }
}