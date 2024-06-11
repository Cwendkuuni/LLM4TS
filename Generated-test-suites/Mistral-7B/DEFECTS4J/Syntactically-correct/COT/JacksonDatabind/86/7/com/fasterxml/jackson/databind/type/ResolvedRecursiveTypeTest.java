package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

/**
 * Test cases for ResolvedRecursiveType class
 */
public class ResolvedRecursiveTypeTest {

    // Test constructor
    @Test
    public void testResolvedRecursiveTypeConstructor() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(resolvedRecursiveType);
        assertEquals(erasedType, resolvedRecursiveType.getRawClass());
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    // Test setReference method
    @Test
    public void testSetReference() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = MockJavaType.getMockJavaType();
        resolvedRecursiveType.setReference(ref);
        JavaType expectedRef = ref;
        JavaType actualRef = resolvedRecursiveType.getSelfReferencedType();
        assertEquals(expectedRef, actualRef);
    }

    // Test getSelfReferencedType method
    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertNull(resolvedRecursiveType.getSelfReferencedType());

        JavaType ref = MockJavaType.getMockJavaType();
        resolvedRecursiveType.setReference(ref);
        JavaType expectedRef = ref;
        JavaType actualRef = resolvedRecursiveType.getSelfReferencedType();
        assertEquals(expectedRef, actualRef);
    }

    // Test getGenericSignature method
    @Test
    public void testGetGenericSignature() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(sb);
        String expectedSignature = "[]";
        String actualSignature = sb.toString();
        assertEquals(expectedSignature, actualSignature);

        JavaType ref = MockJavaType.getMockJavaType();
        resolvedRecursiveType.setReference(ref);
        sb = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(sb);
        String referencedTypeSignature = ref.getGenericSignature(sb).toString();
        expectedSignature = "[" + referencedTypeSignature + "]";
        actualSignature = sb.toString();
        assertEquals(expectedSignature, actualSignature);
    }

    // Test getErasedSignature method
    @Test
    public void testGetErasedSignature() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(sb);
        String expectedSignature = erasedType.getName();
        String actualSignature = sb.toString();
        assertEquals(expectedSignature, actualSignature);

        JavaType ref = MockJavaType.getMockJavaType();
        resolvedRecursiveType.setReference(ref);
        StringBuilder referencedTypeErasedSignature = new StringBuilder();
        ref.getErasedSignature(referencedTypeErasedSignature);
        expectedSignature = erasedType.getName() + "<" + referencedTypeErasedSignature.toString() + ">";
        actualSignature = sb.toString();
        assertEquals(expectedSignature, actualSignature);
    }

    // Test overridden methods
    @Test
    public void testOverriddenMethods() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        // test withContentType method
        JavaType contentType = MockJavaType.getMockJavaType();
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withContentType(contentType));

        // test withTypeHandler method
        Object handler = new Object();
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(handler));

        // test withContentTypeHandler method
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(handler));

        // test withValueHandler method
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(handler));

        // test withContentValueHandler method
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(handler));

        // test withStaticTyping method
        assertSame(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }

    // Test deprecated method _narrow
    @Test(expected = UnsupportedOperationException.class)
    public void testNarrowMethod() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Class<?> subclass = String.class;
        resolvedRecursiveType.narrow(subclass);
    }

    // Test refine method
    @Test
    public void testRefineMethod() {
        Class<?> rawType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = MockJavaType.getMockJavaType();
        JavaType[] superInterfaces = new JavaType[] {MockJavaType.getMockJavaType()};
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(rawType, bindings);
        assertNull(resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces));
    }

    // Test isContainerType method
    @Test
    public void testIsContainerType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    // Test toString method
    @Test
    public void testToString() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        String expectedString = "[recursive type; UNRESOLVED]";
        String actualString = resolvedRecursiveType.toString();
        assertEquals(expectedString, actualString);

        JavaType ref = MockJavaType.getMockJavaType();
        resolvedRecursiveType.setReference(ref);
        expectedString = "[recursive type; " + ref.getRawClass().getName() + "]";
        actualString = resolvedRecursiveType.toString();
        assertEquals(expectedString, actualString);
    }

    // Test equals method
    @Test
    public void testEquals() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType, bindings);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType, bindings);
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));

        JavaType ref = MockJavaType.getMockJavaType();
        ResolvedRecursiveType resolvedRecursiveType3 = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType3.setReference(ref);
        ResolvedRecursiveType resolvedRecursiveType4 = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType4.setReference(ref);
        assertTrue(resolvedRecursiveType3.equals(resolvedRecursiveType4));

        JavaType ref1 = MockJavaType.getMockJavaType();
        JavaType ref2 = MockJavaType.getMockJavaType();
        ResolvedRecursiveType resolvedRecursiveType5 = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType5.setReference(ref1);
        ResolvedRecursiveType resolvedRecursiveType6 = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType6.setReference(ref2);
        assertFalse(resolvedRecursiveType5.equals(resolvedRecursiveType6));
    }

    // Test exception handling when trying to set self reference multiple times
    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionWhenSettingSelfReferenceMultipleTimes() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = MockJavaType.getMockJavaType();
        resolvedRecursiveType.setReference(ref);
        resolvedRecursiveType.setReference(ref);
    }
}