package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class ReferenceTypeTest {

    private ReferenceType referenceType;

    @Before
    public void setUp() {
        referenceType = new ReferenceType(null, null, null, null, null, null, null, null, false);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new SimpleType();
        JavaType result = referenceType.withContentType(contentType);
        assertEquals(contentType, result.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        JavaType result = referenceType.withTypeHandler(typeHandler);
        assertEquals(typeHandler, result.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object typeHandler = new Object();
        JavaType result = referenceType.withContentTypeHandler(typeHandler);
        assertEquals(typeHandler, result.getContentType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        JavaType result = referenceType.withValueHandler(valueHandler);
        assertEquals(valueHandler, result.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object valueHandler = new Object();
        JavaType result = referenceType.withContentValueHandler(valueHandler);
        assertEquals(valueHandler, result.getContentType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType result = referenceType.withStaticTyping();
        assertEquals(true, result.isStaticTyping());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType result = referenceType.refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(true, result instanceof ReferenceType);
    }

    @Test
    public void testGetContentType() {
        JavaType result = referenceType.getContentType();
        assertEquals(null, result);
    }

    @Test
    public void testGetReferencedType() {
        JavaType result = referenceType.getReferencedType();
        assertEquals(null, result);
    }

    @Test
    public void testHasContentType() {
        boolean result = referenceType.hasContentType();
        assertEquals(true, result);
    }

    @Test
    public void testIsReferenceType() {
        boolean result = referenceType.isReferenceType();
        assertEquals(true, result);
    }

    @Test
    public void testGetAnchorType() {
        JavaType result = referenceType.getAnchorType();
        assertEquals(null, result);
    }

}