package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;

/**
 * Test cases for ResolvedRecursiveType class.
 */
public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveTypeConstruction() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeBindings.empty();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(type);
        assertEquals(erasedType, type.getRawClass());
        assertNull(type.getSelfReferencedType());
    }

    @Test
    public void testResolvedRecursiveTypeSetReference() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeBindings.empty();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        type1.setReference(refType);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType, bindings);
        type2.setReference(refType);
         // Should not be able to set reference multiple times
         try {
             type1.setReference(refType);
         } catch (IllegalStateException e) {
             assertEquals("Trying to re-set self reference", e.getMessage());
         }
         ResolvedRecursiveType type3 = new ResolvedRecursiveType(erasedType, bindings);
         type3.setReference(null); // Should be able to set to null
         JavaType refType2 = TypeFactory.defaultInstance().constructType(Integer.class);
         type3.setReference(refType2);
         assertEquals(refType2, type3.getSelfReferencedType());
         assertEquals(refType2, type1.getSelfReferencedType());
         assertEquals(refType2, type2.getSelfReferencedType());
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeBindings.empty();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        assertNull(type.getSelfReferencedType());
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        type.setReference(refType);
        assertEquals(refType, type.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeBindings.empty();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder sb = new StringBuilder(100);
        type.getGenericSignature(sb);
        String expected = "[recursive type; UNRESOLVED]";
        assertEquals(expected, sb.toString());
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        type.setReference(refType);
        sb = new StringBuilder(100);
        type.getGenericSignature(sb);
        expected = "[recursive type; [" + refType.getRawClass().getName() + "]]";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeBindings.empty();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder sb = new StringBuilder(100);
        type.getErasedSignature(sb);
        String expected = "[recursive type; UNRESOLVED]";
        assertEquals(expected, sb.toString());
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        type.setReference(refType);
        sb = new StringBuilder(100);
        type.getErasedSignature(sb);
        expected = refType.getRawClass().getName();
        assertEquals(expected, sb.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWithContentType() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeBindings.empty();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        JavaType contentType = TypeFactory.defaultInstance().constructType(String.class);
        type.withContentType(contentType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWithTypeHandler() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeBindings.empty();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();
        type.withTypeHandler(handler);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWithContentTypeHandler() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeBindings.empty();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();
        type.withContentTypeHandler(handler);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWithValueHandler() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeBindings.empty();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();
        type.withValueHandler(handler);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWithContentValueHandler() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeBindings.empty();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();
        type.withContentValueHandler(handler);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWithStaticTyping() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeBindings.empty();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        type.withStaticTyping();
    }

    @Test
    public void testNarrow() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeBindings.empty();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        Class<?> subclass = Object.class;
        assertNull(type._narrow(subclass));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRefine() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeBindings.empty();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        Class<?> rawType = String.class;
        TypeBindings newBindings = TypeBindings.empty();
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInterfaces = new JavaType[0];
        type.refine(rawType, newBindings, superClass, superInterfaces);
    }

    @Test
    public void testIsContainerType() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeBindings.empty();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testToString() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeBindings.empty();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder expected = new StringBuilder(100)
                .append("[recursive type; UNRESOLVED]");
        assertEquals(expected.toString(), type.toString());
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        type.setReference(refType);
        expected = new StringBuilder(100)
                .append("[recursive type; [")
                .append(refType.getRawClass().getName())
                .append("]]");
        assertEquals(expected.toString(), type.toString());
    }

    @Test
    public void testEquals() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = TypeBindings.empty();
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, bindings);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType, bindings);
        assertTrue(type1.equals(type2));

        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        type1.setReference(refType);
        type2.setReference(refType);
        assertTrue(type1.equals(type2));

        ResolvedRecursiveType type3 = new ResolvedRecursiveType(erasedType, bindings);
        assertFalse(type1.equals(type3));

        type3.setReference(refType);
        assertTrue(type1.equals(type3));

        ResolvedRecursiveType type4 = new ResolvedRecursiveType(Integer.class, bindings);
        assertFalse(type1.equals(type4));
    }

}