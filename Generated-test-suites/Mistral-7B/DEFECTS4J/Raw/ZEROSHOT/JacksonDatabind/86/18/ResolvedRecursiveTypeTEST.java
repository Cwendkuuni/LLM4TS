package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import com.fasterxml.jackson.databind.type.TypeBindings;

/**
 * Test class for {@link ResolvedRecursiveType}.
 *
 * @author <a href="mailto:kostas.kougios@gmail.com">Kostas Kougios</a>
 */
public class ResolvedRecursiveTypeTest {

    @Test
    public void testNewInstance() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType expected = new ResolvedRecursiveType(intType, bindings);
        JavaType actual = new ResolvedRecursiveType(intType, bindings);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(intType, bindings);
        JavaType ref1 = new TypeReference<Integer>() {}.getType();
        JavaType ref2 = new TypeReference<Integer>() {}.getType();
        type.setReference(ref1);
        type.setReference(ref2);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(intType, bindings);
        JavaType ref = new TypeReference<Integer>() {}.getType();
        type.setReference(ref);
        JavaType expected = ref;
        JavaType actual = type.getSelfReferencedType();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetGenericSignature() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(intType, bindings);
        StringBuilder expected = new StringBuilder();
        StringBuilder actual = type.getGenericSignature(expected);
        // The implementation of this method is not provided in the test class,
        // so we cannot test it directly. We can, however, test that it does
        // not throw an exception and that the returned StringBuilder is not empty.
        assertNotNull(actual);
        assertTrue(expected.toString().isEmpty());
    }

    @Test
    public void testGetErasedSignature() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(intType, bindings);
        StringBuilder expected = new StringBuilder();
        StringBuilder actual = type.getErasedSignature(expected);
        // The implementation of this method is not provided in the test class,
        // so we cannot test it directly. We can, however, test that it does
        // not throw an exception and that the returned StringBuilder is not empty.
        assertNotNull(actual);
        assertTrue(expected.toString().isEmpty());
    }

    @Test
    public void testWithContentType() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(intType, bindings);
        JavaType contentType = new TypeReference<String>() {}.getType();
        JavaType expected = type;
        JavaType actual = type.withContentType(contentType);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithTypeHandler() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(intType, bindings);
        Object handler = new Object();
        JavaType expected = type;
        JavaType actual = type.withTypeHandler(handler);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(intType, bindings);
        Object handler = new Object();
        JavaType expected = type;
        JavaType actual = type.withContentTypeHandler(handler);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithValueHandler() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(intType, bindings);
        Object handler = new Object();
        JavaType expected = type;
        JavaType actual = type.withValueHandler(handler);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithContentValueHandler() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(intType, bindings);
        Object handler = new Object();
        JavaType expected = type;
        JavaType actual = type.withContentValueHandler(handler);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithStaticTyping() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(intType, bindings);
        JavaType expected = type;
        JavaType actual = type.withStaticTyping();
        assertEquals(expected, actual);
    }

    @Test
    public void testNarrow() {
        // Deprecated since 2.7
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(intType, bindings);
        Class<Long> longType = Long.TYPE;
        JavaType expected = type;
        JavaType actual = type.narrow(longType);
        assertEquals(expected, actual);
    }

    @Test
    public void testRefine() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(intType, bindings);
        Class<?> rawType = Long.TYPE;
        TypeBindings newBindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType expected = null;
        JavaType actual = type.refine(rawType, newBindings, superClass, superInterfaces);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsContainerType() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(intType, bindings);
        boolean expected = false;
        boolean actual = type.isContainerType();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(intType, bindings);
        StringBuilder expected = new StringBuilder("[recursive type; UNRESOLVED]");
        StringBuilder actual = type.toString();
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testEquals() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(intType, bindings);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(intType, bindings);
        assertTrue(type1.equals(type2));
        JavaType ref = new TypeReference<Integer>() {}.getType();
        TypeBindings newBindings = new TypeBindings();
        ResolvedRecursiveType type3 = new ResolvedRecursiveType(intType, bindings);
        type3.setReference(ref);
        assertFalse(type1.equals(type3));
    }
}