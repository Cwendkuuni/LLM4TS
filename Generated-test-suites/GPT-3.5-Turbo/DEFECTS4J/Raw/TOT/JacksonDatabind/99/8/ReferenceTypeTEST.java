package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstructor() {
        ReferenceType referenceType = new ReferenceType(String.class, new TypeBindings(), null, null, new SimpleType());
        assertNotNull(referenceType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(String.class.getName(), referenceType.toCanonical());
    }

    @Test
    public void testWithContentType() {
        ReferenceType referenceType = new ReferenceType(String.class, new TypeBindings(), null, null, new SimpleType());
        JavaType contentType = new SimpleType();
        ReferenceType result = referenceType.withContentType(contentType);
        assertNotSame(referenceType, result);
        assertEquals(contentType, result.getContentType());
    }

    @Test
    public void testWithContentType_SameContentType() {
        ReferenceType referenceType = new ReferenceType(String.class, new TypeBindings(), null, null, new SimpleType());
        JavaType contentType = new SimpleType();
        ReferenceType result = referenceType.withContentType(contentType);
        assertNotSame(referenceType, result);
        assertEquals(contentType, result.getContentType());

        ReferenceType sameResult = result.withContentType(contentType);
        assertSame(result, sameResult);
    }

    @Test
    public void testWithTypeHandler() {
        ReferenceType referenceType = new ReferenceType(String.class, new TypeBindings(), null, null, new SimpleType());
        Object typeHandler = new Object();
        ReferenceType result = referenceType.withTypeHandler(typeHandler);
        assertNotSame(referenceType, result);
        assertEquals(typeHandler, result.getTypeHandler());
    }

    @Test
    public void testWithTypeHandler_SameTypeHandler() {
        ReferenceType referenceType = new ReferenceType(String.class, new TypeBindings(), null, null, new SimpleType());
        Object typeHandler = new Object();
        ReferenceType result = referenceType.withTypeHandler(typeHandler);
        assertNotSame(referenceType, result);
        assertEquals(typeHandler, result.getTypeHandler());

        ReferenceType sameResult = result.withTypeHandler(typeHandler);
        assertSame(result, sameResult);
    }

    @Test
    public void testWithContentTypeHandler() {
        ReferenceType referenceType = new ReferenceType(String.class, new TypeBindings(), null, null, new SimpleType());
        Object contentTypeHandler = new Object();
        ReferenceType result = referenceType.withContentTypeHandler(contentTypeHandler);
        assertNotSame(referenceType, result);
        assertEquals(contentTypeHandler, result.getContentType().getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler_SameContentTypeHandler() {
        ReferenceType referenceType = new ReferenceType(String.class, new TypeBindings(), null, null, new SimpleType());
        Object contentTypeHandler = new Object();
        ReferenceType result = referenceType.withContentTypeHandler(contentTypeHandler);
        assertNotSame(referenceType, result);
        assertEquals(contentTypeHandler, result.getContentType().getTypeHandler());

        ReferenceType sameResult = result.withContentTypeHandler(contentTypeHandler);
        assertSame(result, sameResult);
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType(String.class, new TypeBindings(), null, null, new SimpleType());
        ReferenceType result = referenceType.withStaticTyping();
        assertNotSame(referenceType, result);
        assertTrue(result.isStaticTyping());
    }
    
    @Test
    public void testRefine() {
        ReferenceType referenceType = new ReferenceType(String.class, new TypeBindings(), null, null, new SimpleType());
        ReferenceType result = referenceType.refine(Object.class, new TypeBindings(), null, null);
        assertNotSame(referenceType, result);
        assertEquals(Object.class, result.getRawClass());
    }
    
    @Test
    public void testRefine_SameClass() {
        ReferenceType referenceType = new ReferenceType(String.class, new TypeBindings(), null, null, new SimpleType());
        ReferenceType result = referenceType.refine(String.class, new TypeBindings(), null, null);
        assertNotSame(referenceType, result);
        assertEquals(String.class, result.getRawClass());
    }

    @Test
    public void testGetContentType() {
        ReferenceType referenceType = new ReferenceType(String.class, new TypeBindings(), null, null, new SimpleType());
        JavaType contentType = referenceType.getContentType();
        assertNotNull(contentType);
        assertTrue(contentType instanceof SimpleType);
    }

    @Test
    public void testGetReferencedType() {
        ReferenceType referenceType = new ReferenceType(String.class, new TypeBindings(), null, null, new SimpleType());
        JavaType referencedType = referenceType.getReferencedType();
        assertNotNull(referencedType);
        assertTrue(referencedType instanceof SimpleType);
        assertEquals(referenceType.getContentType(), referencedType);
    }

    @Test
    public void testHasContentType() {
        ReferenceType referenceType = new ReferenceType(String.class, new TypeBindings(), null, null, new SimpleType());
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType(String.class, new TypeBindings(), null, null, new SimpleType());
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testGetAnchorType() {
        ReferenceType referenceType = new ReferenceType(String.class, new TypeBindings(), null, null, new SimpleType());
        JavaType anchorType = referenceType.getAnchorType();
        assertNotSame(referenceType, anchorType);
        assertEquals(referenceType.getRawClass(), anchorType.getRawClass());
    }

    @Test
    public void testIsAnchorType() {
        ReferenceType referenceType = new ReferenceType(String.class, new TypeBindings(), null, null, new SimpleType());
        assertTrue(referenceType.isAnchorType());
    }

    @Test
    public void testToString() {
        ReferenceType referenceType = new ReferenceType(String.class, new TypeBindings(), null, null, new SimpleType());
        String expected = "[reference type, class java.lang.String<java.lang.String>]";
        assertEquals(expected, referenceType.toString());
    }

    @Test
    public void testEquals() {
        ReferenceType referenceType = new ReferenceType(String.class, new TypeBindings(), null, null, new SimpleType());
        ReferenceType sameType = new ReferenceType(String.class, new TypeBindings(), null, null, new SimpleType());
        assertTrue(referenceType.equals(referenceType));
        assertTrue(referenceType.equals(sameType));
        assertFalse(referenceType.equals(null));
        assertFalse(referenceType.equals(""));
    }

}