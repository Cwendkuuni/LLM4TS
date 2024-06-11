package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setUp() {
        Class<?> erasedType = null; // Fill in with appropriate value
        TypeBindings bindings = null; // Fill in with appropriate value

        resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
    }

    @Test
    public void testSetReference() {
        JavaType ref = null; // Fill in with appropriate value

        resolvedRecursiveType.setReference(ref);

        // Assert statements
    }

    @Test
    public void testGetSelfReferencedType() {
        JavaType expectedResult = null; // Fill in with appropriate value

        JavaType result = resolvedRecursiveType.getSelfReferencedType();

        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = null; // Fill in with appropriate value
        StringBuilder expectedResult = null; // Fill in with appropriate value

        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = null; // Fill in with appropriate value
        StringBuilder expectedResult = null; // Fill in with appropriate value

        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = null; // Fill in with appropriate value
        JavaType expectedResult = null; // Fill in with appropriate value

        JavaType result = resolvedRecursiveType.withContentType(contentType);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null; // Fill in with appropriate value
        JavaType expectedResult = null; // Fill in with appropriate value

        JavaType result = resolvedRecursiveType.withTypeHandler(h);

        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object h = null; // Fill in with appropriate value
        JavaType expectedResult = null; // Fill in with appropriate value

        JavaType result = resolvedRecursiveType.withContentTypeHandler(h);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testWithValueHandler() {
        Object h = null; // Fill in with appropriate value
        JavaType expectedResult = null; // Fill in with appropriate value

        JavaType result = resolvedRecursiveType.withValueHandler(h);

        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object h = null; // Fill in with appropriate value
        JavaType expectedResult = null; // Fill in with appropriate value

        JavaType result = resolvedRecursiveType.withContentValueHandler(h);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType expectedResult = null; // Fill in with appropriate value

        JavaType result = resolvedRecursiveType.withStaticTyping();

        assertEquals(expectedResult, result);
    }

    @Test
    public void test_narrow() {
        Class<?> subclass = null; // Fill in with appropriate value
        JavaType expectedResult = null; // Fill in with appropriate value

        JavaType result = resolvedRecursiveType._narrow(subclass);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = null; // Fill in with appropriate value
        TypeBindings bindings = null; // Fill in with appropriate value
        JavaType superClass = null; // Fill in with appropriate value
        JavaType[] superInterfaces = null; // Fill in with appropriate value
        JavaType expectedResult = null; // Fill in with appropriate value

        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testIsContainerType() {
        boolean expectedResult = false; // Fill in with appropriate value

        boolean result = resolvedRecursiveType.isContainerType();

        assertEquals(expectedResult, result);
    }

    @Test
    public void testToString() {
        String expectedResult = null; // Fill in with appropriate value

        String result = resolvedRecursiveType.toString();

        assertEquals(expectedResult, result);
    }

    @Test
    public void testEquals() {
        Object o = null; // Fill in with appropriate value
        boolean expectedResult = false; // Fill in with appropriate value

        boolean result = resolvedRecursiveType.equals(o);

        assertEquals(expectedResult, result);
    }
}