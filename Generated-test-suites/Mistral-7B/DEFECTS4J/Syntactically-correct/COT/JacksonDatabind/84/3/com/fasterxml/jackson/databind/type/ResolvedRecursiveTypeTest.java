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
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(type);
        assertEquals(erasedType, type.getRawClass());
        assertNull(type.getSelfReferencedType());
    }

    /**
     * Test method setReference.
     */
    @Test
    public void testSetReference() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        JavaType ref = new MapType(Integer.class, JavaType.constructCollectionType(Map.class, Integer.class));
        type.setReference(ref);
        assertNotNull(type.getSelfReferencedType());
        assertEquals(ref, type.getSelfReferencedType());

        try {
            type.setReference(ref); // should not be called multiple times
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            assertEquals("Trying to re-set self reference; old value = " + ref, e.getMessage());
        }
    }

    /**
     * Test method getSelfReferencedType.
     */
    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        JavaType ref = new MapType(Integer.class, JavaType.constructCollectionType(Map.class, Integer.class));
        type.setReference(ref);
        assertEquals(ref, type.getSelfReferencedType());

        ref = new ArrayListType(JavaType.constructCollectionType(ArrayList.class, Integer.class));
        type.setReference(ref);
        assertEquals(ref, type.getSelfReferencedType());
    }

    /**
     * Test method getGenericSignature and getErasedSignature.
     */
    @Test
    public void testGetSignatures() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        JavaType ref = new MapType(Integer.class, JavaType.constructCollectionType(Map.class, Integer.class));
        type.setReference(ref);

        StringBuilder sb = new StringBuilder(50);
        String expectedTypeSignature = "Map<Integer, List<Integer>>";
        String expectedErasedSignature = "Map<Integer, List<Integer>>";

        type.getGenericSignature(sb);
        String actualTypeSignature = sb.toString();
        assertEquals(expectedTypeSignature, actualTypeSignature);

        sb.setLength(0);
        type.getErasedSignature(sb);
        String actualErasedSignature = sb.toString();
        assertEquals(expectedErasedSignature, actualErasedSignature);
    }

    /**
     * Test method equals.
     */
    @Test
    public void testEquals() {
        Class<?> erasedType1 = Integer.class;
        TypeBindings bindings1 = new TypeBindings();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType1, bindings1);

        Class<?> erasedType2 = Integer.class;
        TypeBindings bindings2 = new TypeBindings();
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType2, bindings2);

        JavaType ref = new MapType(Integer.class, JavaType.constructCollectionType(Map.class, Integer.class));
        type1.setReference(ref);
        type2.setReference(ref);

        assertFalse(type1.equals(type2));

        JavaType ref2 = new ArrayListType(JavaType.constructCollectionType(ArrayList.class, Integer.class));
        type1.setReference(ref);
        type2.setReference(ref2);

        assertFalse(type1.equals(type2));
    }

    /**
     * Test method equals with same instance.
     */
    @Test
    public void testEqualsSelf() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        assertTrue(type.equals(type));
    }

    /**
     * Test method isContainerType.
     */
    @Test
    public void testIsContainerType() {
        assertTrue(new ArrayListType(JavaType.constructCollectionType(ArrayList.class, Integer.class)).isContainerType());
        assertTrue(new MapType(Integer.class, JavaType.constructCollectionType(Map.class, Integer.class)).isContainerType());
        assertFalse(new ResolvedRecursiveType(Integer.class, new TypeBindings()).isContainerType());
    }

    /**
     * Test method toString.
     */
    @Test
    public void testToString() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        JavaType ref = new MapType(Integer.class, JavaType.constructCollectionType(Map.class, Integer.class));
        type.setReference(ref);

        String expectedString = "[recursive type; Map<Integer, List<Integer>>]";
        assertEquals(expectedString, type.toString());
    }
}