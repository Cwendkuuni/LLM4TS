package com.fasterxml.jackson.databind.type;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;
    private JavaType referencedType;

    @Before
    public void setUp() {
        Class<?> erasedType = Class.class;
        TypeBindings bindings = new TypeBindings();
        resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        referencedType = JavaType.defaultInstance();
    }

    @After
    public void tearDown() {
        resolvedRecursiveType = null;
        referencedType = null;
    }

    @Test
    public void testSetReference_Success() {
        assertNull(resolvedRecursiveType.getSelfReferencedType());

        // Set the reference
        resolvedRecursiveType.setReference(referencedType);

        // Assert the reference is set correctly
        assertEquals(referencedType, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReference_MultipleTimes_ExceptionThrown() {
        assertNull(resolvedRecursiveType.getSelfReferencedType());

        // Set the reference the first time
        resolvedRecursiveType.setReference(referencedType);

        // Assert the reference is set correctly
        assertEquals(referencedType, resolvedRecursiveType.getSelfReferencedType());

        // Set the reference again, which should throw an exception
        resolvedRecursiveType.setReference(referencedType);
    }

    @Test
    public void testGetGenericSignature_Success() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expectedResult = new StringBuilder();

        // Invoke the method
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);

        // Assert the result is the same as the expected result
        assertEquals(expectedResult.toString(), result.toString());
    }

    @Test
    public void testGetErasedSignature_Success() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expectedResult = new StringBuilder();

        // Invoke the method
        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);

        // Assert the result is the same as the expected result
        assertEquals(expectedResult.toString(), result.toString());
    }

    @Test
    public void testWithContentType_Success() {
        JavaType contentType = JavaType.defaultInstance();

        // Invoke the method
        JavaType result = resolvedRecursiveType.withContentType(contentType);

        // Assert the result is the same as the input
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler_Success() {
        Object handler = new Object();

        // Invoke the method
        JavaType result = resolvedRecursiveType.withTypeHandler(handler);

        // Assert the result is the same as the input
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler_Success() {
        Object handler = new Object();

        // Invoke the method
        JavaType result = resolvedRecursiveType.withContentTypeHandler(handler);

        // Assert the result is the same as the input
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler_Success() {
        Object handler = new Object();

        // Invoke the method
        JavaType result = resolvedRecursiveType.withValueHandler(handler);

        // Assert the result is the same as the input
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler_Success() {
        Object handler = new Object();

        // Invoke the method
        JavaType result = resolvedRecursiveType.withContentValueHandler(handler);

        // Assert the result is the same as the input
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping_Success() {
        // Invoke the method
        JavaType result = resolvedRecursiveType.withStaticTyping();

        // Assert the result is the same as the input
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testNarrow_Success() {
        Class<?> subclass = Class.class;

        // Invoke the method
        JavaType result = resolvedRecursiveType._narrow(subclass);

        // Assert the result is the same as the input
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine_Success() {
        Class<?> rawType = Class.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.defaultInstance();
        JavaType[] superInterfaces = new JavaType[0];

        // Invoke the method
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);

        // Assert the result is null
        assertNull(result);
    }

    @Test
    public void testIsContainerType_Success() {
        // Invoke the method
        boolean result = resolvedRecursiveType.isContainerType();

        // Assert the result is false
        assertFalse(result);
    }

    @Test
    public void testToString_ReferencedTypeIsNull_Success() {
        resolvedRecursiveType.setReference(null);

        // Invoke the method
        String result = resolvedRecursiveType.toString();

        // Assert the result contains "UNRESOLVED"
        assertTrue(result.contains("UNRESOLVED"));
    }

    @Test
    public void testToString_ReferencedTypeIsNotNull_Success() {
        resolvedRecursiveType.setReference(referencedType);

        // Get the name of the raw class
        String rawClassName = referencedType.getRawClass().getName();

        // Invoke the method
        String result = resolvedRecursiveType.toString();

        // Assert the result contains the name of the raw class
        assertTrue(result.contains(rawClassName));
    }

    @Test
    public void testEquals_ReferenceIsNull_ReturnsFalse() {
        Object obj = null;

        // Invoke the method
        boolean result = resolvedRecursiveType.equals(obj);

        // Assert the result is false
        assertFalse(result);
    }

    @Test
    public void testEquals_ReferenceIsNotInstanceOfSameClass_ReturnsFalse() {
        Object obj = new Object();

        // Invoke the method
        boolean result = resolvedRecursiveType.equals(obj);

        // Assert the result is false
        assertFalse(result);
    }

    @Test
    public void testEquals_ReferenceIsInstanceOfSameClassButReferencedTypeIsNull_ReturnsFalse() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(null, null);

        // Invoke the method
        boolean result = resolvedRecursiveType.equals(other);

        // Assert the result is false
        assertFalse(result);
    }

    @Test
    public void testEquals_ReferenceIsInstanceOfSameClassAndReferencedTypeIsSame_ReturnsTrue() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(null, null);
        resolvedRecursiveType.setReference(referencedType);
        other.setReference(referencedType);

        // Invoke the method
        boolean result = resolvedRecursiveType.equals(other);

        // Assert the result is true
        assertTrue(result);
    }

    @Test
    public void testEquals_ReferenceIsInstanceOfSameClassAndReferencedTypeIsDifferent_ReturnsFalse() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(null, null);
        resolvedRecursiveType.setReference(referencedType);
        other.setReference(JavaType.defaultInstance());

        // Invoke the method
        boolean result = resolvedRecursiveType.equals(other);

        // Assert the result is false
        assertFalse(result);
    }

}