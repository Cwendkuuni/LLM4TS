package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    private ReferenceType referenceType;

    @Before
    public void setUp() {
        JavaType referencedType = new SimpleType();
        referenceType = new ReferenceType(ReferenceType.class, new TypeBindings(), null, null, referencedType, null, null, null, false);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new SimpleType();
        JavaType result = referenceType.withContentType(contentType);
        assertNotNull(result);
        assertEquals(contentType, result.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        JavaType result = referenceType.withTypeHandler(handler);
        assertNotNull(result);
        assertEquals(handler, result.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        JavaType result = referenceType.withContentTypeHandler(handler);
        assertNotNull(result);
        assertEquals(handler, result.getContentType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        JavaType result = referenceType.withValueHandler(handler);
        assertNotNull(result);
        assertEquals(handler, result.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        JavaType result = referenceType.withContentValueHandler(handler);
        assertNotNull(result);
        assertEquals(handler, result.getContentType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType result = referenceType.withStaticTyping();
        assertTrue(result.isStaticTyping());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType();
        JavaType[] superInterfaces = null;
        JavaType result = referenceType.refine(rawType, bindings, superClass, superInterfaces);
        assertNotNull(result);
        assertEquals(rawType, result.getRawClass());
        assertEquals(bindings, result.getBindings());
        assertEquals(superClass, result.getSuperClass());
        assertArrayEquals(superInterfaces, result.getInterfaces());
    }

    @Test
    public void testGetContentType() {
        JavaType result = referenceType.getContentType();
        assertNotNull(result);
        assertTrue(result instanceof SimpleType);
    }

    @Test
    public void testGetReferencedType() {
        JavaType result = referenceType.getReferencedType();
        assertNotNull(result);
        assertTrue(result instanceof SimpleType);
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
    public void testGetAnchorType() {
        JavaType result = referenceType.getAnchorType();
        assertNotNull(result);
        assertTrue(result instanceof ReferenceType);
    }

    @Test
    public void testIsAnchorType() {
        boolean result = referenceType.isAnchorType();
        assertTrue(result);
    }

    @Test
    public void testToString() {
        String result = referenceType.toString();
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    public void testEquals() {
        boolean result = referenceType.equals(null);
        assertFalse(result);
        result = referenceType.equals(referenceType);
        assertTrue(result);
        result = referenceType.equals(new ReferenceType(ReferenceType.class, new TypeBindings(), null, null, new SimpleType(), null, null, null, false));
        assertTrue(result);
        result = referenceType.equals(new ReferenceType(String.class, new TypeBindings(), null, null, new SimpleType(), null, null, null, false));
        assertFalse(result);
    }
}