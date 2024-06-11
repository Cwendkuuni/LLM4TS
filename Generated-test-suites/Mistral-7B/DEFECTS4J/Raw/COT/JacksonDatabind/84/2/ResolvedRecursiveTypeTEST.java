package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

import org.junit.Test;

/**
 * Unit tests for {@link ResolvedRecursiveType} class.
 */
public class ResolvedRecursiveTypeTest {

    // ---------------------------------------------------------------------------------------------
    // Basic tests for all public methods
    // ---------------------------------------------------------------------------------------------

    @Test
    public void testResolvedRecursiveType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertNull(type.getSelfReferencedType());
    }

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new SimpleModule().simpleType(Integer.class);
        type.setReference(ref);
        assertNotNull(type.getSelfReferencedType());
        assertEquals(ref.getClass(), type.getSelfReferencedType().getClass());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref1 = new SimpleModule().simpleType(Integer.class);
        JavaType ref2 = new SimpleModule().simpleType(Long.class);
        type.setReference(ref1);
        type.setReference(ref2);
    }

    @Test
    public void testGetSelfReferencedType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new SimpleModule().simpleType(Integer.class);
        type.setReference(ref);
        JavaType result = type.getSelfReferencedType();
        assertNotNull(result);
        assertEquals(ref.getClass(), result.getClass());
    }

    // ---------------------------------------------------------------------------------------------
    // Testing getGenericSignature and getErasedSignature methods
    // ---------------------------------------------------------------------------------------------

    @Test
    public void testGetGenericSignature() {
        JavaType type = new SimpleModule()
                .typeFactory()
                .constructType(Map.class, String.class, new TypeFactory().constructType(Number.class, Integer.class));
        StringBuilder sb = new StringBuilder();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Map.class, new TypeBindings());
        resolvedType.setReference(type);
        resolvedType.getGenericSignature(sb);
        String expected = "[Map<java.lang.String, ResolvedRecursiveType<java.util.Map<java.lang.String, Integer>>>;]";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType type = new SimpleModule()
                .typeFactory()
                .constructType(Map.class, String.class, new TypeFactory().constructType(Number.class, Integer.class));
        StringBuilder sb = new StringBuilder();
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Map.class, new TypeBindings());
        resolvedType.setReference(type);
        resolvedType.getErasedSignature(sb);
        String expected = "[Map<String, Number>]";
        assertEquals(expected, sb.toString());
    }

    // ---------------------------------------------------------------------------------------------
    // Testing overridden methods
    // ---------------------------------------------------------------------------------------------

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType contentType = new SimpleModule().simpleType(Integer.class);
        JavaType result = type.withContentType(contentType);
        assertSame(type, result);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        JavaType result = type.withTypeHandler(handler);
        assertSame(type, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        JavaType result = type.withContentTypeHandler(handler);
        assertSame(type, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        JavaType result = type.withValueHandler(handler);
        assertSame(type, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        JavaType result = type.withContentValueHandler(handler);
        assertSame(type, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType result = type.withStaticTyping();
        assertSame(type, result);
    }

    @Test
    @Deprecated
    public void testNarrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Class<?> subclass = Integer.class;
        JavaType result = type._narrow(subclass);
        assertSame(type, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Class<?> rawType = List.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleModule().simpleType(Collection.class);
        JavaType[] superInterfaces = new JavaType[] { new SimpleModule().simpleType(Iterable.class) };
        JavaType result = type.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

    // ---------------------------------------------------------------------------------------------
    // Testing isContainerType method
    // ---------------------------------------------------------------------------------------------

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertFalse(type.isContainerType());
    }

    // ---------------------------------------------------------------------------------------------
    // Testing toString method
    // ---------------------------------------------------------------------------------------------

    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new SimpleModule().simpleType(Integer.class);
        type.setReference(ref);
        String expected = "[recursive type; " + ref.getRawClass().getName() + "]";
        assertEquals(expected, type.toString());
    }

    // ---------------------------------------------------------------------------------------------
    // Testing equals method
    // ---------------------------------------------------------------------------------------------

    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref1 = new SimpleModule().simpleType(Integer.class);
        type1.setReference(ref1);

        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref2 = new SimpleModule().simpleType(Long.class);
        type2.setReference(ref2);

        ResolvedRecursiveType type3 = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref3 = new SimpleModule().simpleType(Integer.class);
        type3.setReference(ref3);

        ResolvedRecursiveType type4 = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertTrue(type1.equals(type1));
        assertFalse(type1.equals(type2));
        assertFalse(type1.equals(type3));
        assertFalse(type1.equals(type4));
        assertFalse(type1.equals(new Object()));
    }

    // ---------------------------------------------------------------------------------------------
    // Testing edge cases and exception handling scenarios
    // ---------------------------------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        type.setReference(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetReferenceDifferentTypes() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref1 = new SimpleModule().simpleType(Integer.class);
        JavaType ref2 = new SimpleModule().simpleType(Long.class);
        type.setReference(ref1);
        type.setReference(ref2);
    }

    @Test
    public void testEqualsWithNull() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new SimpleModule().simpleType(Integer.class);
        type1.setReference(ref);

        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertFalse(type1.equals(null));
        assertFalse(type1.equals(type2));
    }

    @Test
    public void testEqualsWithSameClassButDifferentTypeBindings() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new SimpleModule().simpleType(Integer.class);
        type1.setReference(ref);

        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref2 = new SimpleModule().simpleType(Integer.class);
        type2.setReference(ref2);

        assertFalse(type1.equals(type2));
    }

    @Test
    public void testEqualsWithSameClassAndSameTypeBindingsButDifferentReferences() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref1 = new SimpleModule().simpleType(Integer.class);
        type1.setReference(ref1);

        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref2 = new SimpleModule().simpleType(Long.class);
        type2.setReference(ref2);

        assertFalse(type1.equals(type2));
    }

    @Test
    public void testEqualsWithSameClassAndSameTypeBindingsAndSameReference() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new SimpleModule().simpleType(Integer.class);
        type1.setReference(ref);

        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        type2.setReference(ref);

        assertTrue(type1.equals(type2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelfReferencedTypeWithNull() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        type.getSelfReferencedType();
    }
}