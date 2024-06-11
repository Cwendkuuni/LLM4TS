package com.yourpackagename.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

import org.junit.Test;

/**
 * Test cases for {@link ResolvedRecursiveType} class.
 */
public class ResolvedRecursiveTypeTest {

    @Test
    public void testConstructor() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType actual = new ResolvedRecursiveType(erasedType, bindings);
        assertEquals(erasedType, actual.getRawClass());
        assertNull(actual.getSelfReferencedType());
    }

    @Test
    public void testSetReference() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new JavaType(String.class);
        type.setReference(ref);
        JavaType expected = ref;
        JavaType actual = type.getSelfReferencedType();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref1 = new JavaType(String.class);
        JavaType ref2 = new JavaType(Integer.class);
        type.setReference(ref1);
        type.setReference(ref2);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new JavaType(String.class);
        type.setReference(ref);
        JavaType expected = ref;
        JavaType actual = type.getSelfReferencedType();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        StringBuilder expected = new StringBuilder("<T>");
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new JavaType(String.class);
        type.setReference(ref);
        StringBuilder actual = new StringBuilder();
        type.getGenericSignature(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        StringBuilder expected = new StringBuilder("Ljava/lang/Object;");
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new JavaType(String.class);
        type.setReference(ref);
        StringBuilder actual = new StringBuilder();
        type.getErasedSignature(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testWithContentType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType contentType = new JavaType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType actual = type.withContentType(contentType);
        assertEquals(type, actual);
    }

    @Test
    public void testToString() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType ref = new JavaType(String.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        type.setReference(ref);
        String expected = "[recursive type; java.lang.String]";
        String actual = type.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Class<?> erasedType1 = Object.class;
        Class<?> erasedType2 = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType ref1 = new JavaType(String.class);
        JavaType ref2 = new JavaType(Integer.class);
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType1, bindings);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType1, bindings);
        type1.setReference(ref1);
        type2.setReference(ref2);
        assertFalse(type1.equals(type2));

        JavaType ref3 = new JavaType(String.class);
        type1.setReference(ref3);
        ResolvedRecursiveType type3 = new ResolvedRecursiveType(erasedType1, bindings);
        type3.setReference(ref3);
        assertTrue(type1.equals(type3));

        JavaType ref4 = new JavaType(erasedType2);
        ResolvedRecursiveType type4 = new ResolvedRecursiveType(erasedType2, bindings);
        assertFalse(type1.equals(type4));
    }

}