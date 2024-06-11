package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

import org.junit.Test;

/**
 * Test cases for the ResolvedRecursiveType class.
 */
public class ResolvedRecursiveTypeTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testResolvedRecursiveTypeConstructor() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType instance = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(instance);
    }

    /**
     * Test method setReference with valid argument.
     */
    @Test
    public void testSetReference() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType instance = new ResolvedRecursiveType(erasedType, bindings);

        JavaType ref = new SimpleType(int[].class);
        instance.setReference(ref);

        JavaType expected = ref;
        JavaType actual = instance.getSelfReferencedType();
        assertEquals(expected, actual);
    }

    /**
     * Test method setReference with same argument twice.
     */
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceWithSameArgumentTwice() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType instance = new ResolvedRecursiveType(erasedType, bindings);

        JavaType ref = new SimpleType(int[].class);
        instance.setReference(ref);

        try {
            instance.setReference(ref);
        } catch (IllegalStateException e) {
            assertEquals("Trying to re-set self reference; old value = " + ref + ", new = " + ref, e.getMessage());
            throw e;
        }
    }

    /**
     * Test method getSelfReferencedType with uninitialized instance.
     */
    @Test
    public void testGetSelfReferencedTypeWithUninitializedInstance() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType instance = new ResolvedRecursiveType(erasedType, bindings);

        JavaType actual = instance.getSelfReferencedType();
        assertNull(actual);
    }

    /**
     * Test method getSelfReferencedType with initialized instance.
     */
    @Test
    public void testGetSelfReferencedTypeWithInitializedInstance() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType instance = new ResolvedRecursiveType(erasedType, bindings);

        JavaType ref = new SimpleType(int[].class);
        instance.setReference(ref);

        JavaType expected = ref;
        JavaType actual = instance.getSelfReferencedType();
        assertEquals(expected, actual);
    }

    /**
     * Test method getGenericSignature with valid argument.
     */
    @Test
    public void testGetGenericSignature() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType instance = new ResolvedRecursiveType(erasedType, bindings);

        StringBuilder expected = new StringBuilder("[recursive type; ");
        StringBuilder refSb = new StringBuilder("<java.lang.int[]>");
        instance.setReference(new SimpleType(int[].class));

        StringBuilder actual = new StringBuilder();
        instance.getGenericSignature(actual);

        assertEquals(expected.toString(), actual.toString());
    }

    /**
     * Test method getErasedSignature with valid argument.
     */
    @Test
    public void testGetErasedSignature() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType instance = new ResolvedRecursiveType(erasedType, bindings);

        StringBuilder expected = new StringBuilder("[I");
        StringBuilder actual = new StringBuilder();
        instance.getErasedSignature(actual);

        assertEquals(expected.toString(), actual.toString());
    }

    /**
     * Test methods withContentType, withTypeHandler, withContentTypeHandler,
     * withValueHandler, withContentValueHandler, withStaticTyping.
     */
    @Test
    public void testMethodsWithNoEffect() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType instance = new ResolvedRecursiveType(erasedType, bindings);

        Object handler = new Object();

        instance.withContentType(new SimpleType(int[].class));
        instance.withTypeHandler(handler);
        instance.withContentTypeHandler(handler);
        instance.withValueHandler(handler);
        instance.withContentValueHandler(handler);
        instance.withStaticTyping();

        JavaType actual = instance;
        JavaType expected = instance;
        assertEquals(expected, actual);
    }

    /**
     * Test method _narrow with deprecated usage.
     * @deprecated since 2.7
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testNarrowDeprecated() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType instance = new ResolvedRecursiveType(erasedType, bindings);

        Class<String> subclass = String.class;
        JavaType actual = instance._narrow(subclass);

        fail("_narrow method should not be invoked");
    }

    /**
     * Test method refine.
     */
    @Test
    public void testRefine() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType instance = new ResolvedRecursiveType(erasedType, bindings);

        JavaType expected = null;
        JavaType actual = instance.refine(null, bindings, null, new JavaType[0]);
        assertEquals(expected, actual);
    }

    /**
     * Test method isContainerType.
     */
    @Test
    public void testIsContainerType() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType instance = new ResolvedRecursiveType(erasedType, bindings);

        assertFalse(instance.isContainerType());
    }

    /**
     * Test method toString.
     */
    @Test
    public void testToString() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();

        // [UNRESOLVED]
        ResolvedRecursiveType instance1 = new ResolvedRecursiveType(erasedType, bindings);
        assertEquals("[recursive type; UNRESOLVED]", instance1.toString());

        // [recursive type; java.lang.Integer]
        JavaType ref = new SimpleType(Integer.class);
        ResolvedRecursiveType instance2 = new ResolvedRecursiveType(erasedType, bindings);
        instance2.setReference(ref);
        assertEquals("[recursive type; java.lang.Integer]", instance2.toString());
    }

    /**
     * Test method equals.
     */
    @Test
    public void testEquals() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();

        ResolvedRecursiveType instance1 = new ResolvedRecursiveType(erasedType, bindings);
        ResolvedRecursiveType instance2 = new ResolvedRecursiveType(erasedType, bindings);

        assertTrue(instance1.equals(instance2));

        JavaType ref = new SimpleType(int[].class);
        instance1.setReference(ref);
        instance2.setReference(ref);

        assertTrue(instance1.equals(instance2));

        instance1.setReference(new SimpleType(String.class));
        assertFalse(instance1.equals(instance2));
    }
}