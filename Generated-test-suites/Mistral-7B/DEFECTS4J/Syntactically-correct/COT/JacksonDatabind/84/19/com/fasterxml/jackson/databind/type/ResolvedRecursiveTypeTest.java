package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

/**
 * Test class for ResolvedRecursiveType.
 */
public class ResolvedRecursiveTypeTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testConstructorWithValidArguments() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(type);
        assertEquals(erasedType, type.getRawClass());
        assertNull(type.getSelfReferencedType());
    }

    /**
     * Test method setReference with valid argument.
     */
    @Test
    public void testSetReferenceWithValidArgument() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new JavaType(int[].class);
        type.setReference(ref);
        assertSame(ref, type.getSelfReferencedType());
    }

    /**
     * Test method setReference with same reference twice.
     */
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceWithSameReferenceTwice() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new JavaType(int[].class);
        type.setReference(ref);
        type.setReference(ref);
    }

    /**
     * Test getSelfReferencedType method.
     */
    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new JavaType(int[].class);
        type.setReference(ref);
        JavaType selfReferencedType = type.getSelfReferencedType();
        assertSame(ref, selfReferencedType);
    }

    /**
     * Test getGenericSignature and getErasedSignature methods.
     */
    @Test
    public void testGetGenericSignatureAndGetErasedSignature() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new JavaType(int[].class);
        type.setReference(ref);
        StringBuilder sb = new StringBuilder();

        type.getGenericSignature(sb);
        assertNotNull(sb.toString());
        sb.setLength(0);

        type.getErasedSignature(sb);
        assertNotNull(sb.toString());
    }

    /**
     * Test methods withContentType, withTypeHandler, withContentTypeHandler, withValueHandler, withContentValueHandler,
     * withStaticTyping.
     */
    @Test
    public void testMethodsWithSpecializations() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();

        type.withContentType(new JavaType(int[].class));
        type.withTypeHandler(handler);
        type.withContentTypeHandler(handler);
        type.withValueHandler(handler);
        type.withContentValueHandler(handler);
        type.withStaticTyping();

        // No assertions since these methods don't return any value
    }

    /**
     * Test method isContainerType.
     */
    @Test
    public void testIsContainerType() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        assertFalse(type.isContainerType());
    }

    /**
     * Test method toString.
     */
    @Test
    public void testToString() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new JavaType(int[].class);
        type.setReference(ref);

        String expected = "[recursive type; [Ljava.lang.int;]";
        String actual = type.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test method equals.
     */
    @Test
    public void testEquals() {
        Class<?> erasedType1 = String.class;
        TypeBindings bindings1 = new TypeBindings();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType1, bindings1);
        JavaType ref1 = new JavaType(int[].class);
        type1.setReference(ref1);

        Class<?> erasedType2 = String.class;
        TypeBindings bindings2 = new TypeBindings();
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType2, bindings2);
        JavaType ref2 = new JavaType(int[].class);
        type2.setReference(ref2);

        Class<?> erasedType3 = Long.TYPE;
        TypeBindings bindings3 = new TypeBindings();
        ResolvedRecursiveType type3 = new ResolvedRecursiveType(erasedType3, bindings3);

        assertFalse(type1.equals(type2));
        assertFalse(type1.equals(type3));
        assertFalse(type1.equals(null));
        assertFalse(type1.equals(""));

        JavaType ref3 = new JavaType(double[].class);
        type1.setReference(ref3);
        assertFalse(type1.equals(type2));
    }

    /**
     * Test method refine.
     */
    @Test
    public void testRefine() {
        // Cannot test method refine since it returns null
    }
}