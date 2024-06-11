package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new ClassType(String.class);

        type.setReference(ref);
        JavaType expected = ref;
        JavaType actual = type.getSelfReferencedType();

        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref1 = new ClassType(String.class);
        JavaType ref2 = new ClassType(Integer.class);

        type.setReference(ref1);
        type.setReference(ref2);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new ClassType(String.class);
        type.setReference(ref);

        JavaType expected = ref;
        JavaType actual = type.getSelfReferencedType();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetGenericSignature() {
        // Test with referenced type
        Class<?> erasedType = List.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType typeRef = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new ParameterizedType(new ClassType(ArrayList.class), new TypeReference<Integer>(){});
        typeRef.setReference(ref);

        StringBuilder expected = new StringBuilder();
        StringBuilder actual = typeRef.getGenericSignature(new StringBuilder());

        assertEquals("(Ljava/util/ArrayList;<Ljava/lang/Integer;>)", actual.toString());

        // Test without referenced type
        erasedType = Integer.class;
        typeRef = new ResolvedRecursiveType(erasedType, bindings);

        expected = new StringBuilder();
        actual = typeRef.getGenericSignature(new StringBuilder());

        assertEquals("", actual.toString());
    }

    @Test
    public void testGetErasedSignature() {
        // Test with referenced type
        Class<?> erasedType = List.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType typeRef = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new ParameterizedType(new ClassType(ArrayList.class), new TypeReference<Integer>(){});
        typeRef.setReference(ref);

        StringBuilder expected = new StringBuilder();
        StringBuilder actual = typeRef.getErasedSignature(new StringBuilder());

        assertEquals("Ljava/util/List;", actual.toString());

        // Test without referenced type
        erasedType = Integer.class;
        typeRef = new ResolvedRecursiveType(erasedType, bindings);

        expected = new StringBuilder();
        actual = typeRef.getErasedSignature(new StringBuilder());

        assertEquals("I", actual.toString());
    }

    @Test
    public void testIsContainerType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        assertFalse(type.isContainerType());
    }

    @Test
    public void testToString() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new ClassType(String.class);
        type.setReference(ref);

        String expected = "[recursive type; java.lang.String]";
        String actual = type.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Class<?> erasedType1 = Object.class;
        TypeBindings bindings1 = new TypeBindings();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType1, bindings1);
        JavaType ref1 = new ClassType(String.class);
        type1.setReference(ref1);

        Class<?> erasedType2 = Object.class;
        TypeBindings bindings2 = new TypeBindings();
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType2, bindings2);
        JavaType ref2 = new ClassType(String.class);
        type2.setReference(ref2);

        assertTrue(type1.equals(type1));
        assertTrue(type1.equals(type1));
        assertTrue(type1.equals(type2));
        assertTrue(type2.equals(type1));
        assertTrue(type1.equals(type2));
        assertTrue(type2.equals(type1));

        assertEquals(type1.hashCode(), type2.hashCode());

        JavaType ref3 = new ClassType(Integer.class);
        type1.setReference(ref3);
        assertFalse(type1.equals(type2));
    }
}