package com.fasterxml.jackson.databind.type.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link ResolvedRecursiveType} class.
 */
public class ResolvedRecursiveTypeTest {

    private JavaType javaType;

    @Before
    public void setUp() {
        Class<List<String>> listOfStrings = List.class.getTypeParameters()[0].getType();
        JavaType javaTypeRef = new TypeReference<List<String>>() {}.getType();
        javaType = new ResolvedRecursiveType(listOfStrings, new TypeBindings());
    }

    @Test
    public void testGetSelfReferencedType() {
        assertNull(javaType.getSelfReferencedType());

        JavaType refType = new TypeReference<List<String>>() {}.getType();
        javaType.setReference(refType);
        assertEquals(refType, javaType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        JavaType refType = new TypeReference<List<String>>() {}.getType();
        javaType.setReference(refType);
        javaType.setReference(new TypeReference<List<String>>() {}.getType());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        javaType.getGenericSignature(sb);
        String signature = sb.toString();
        String expected = "[Ljava.util.List<Ljava.lang.String;>;";
        assertEquals(expected, signature);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        javaType.getErasedSignature(sb);
        String signature = sb.toString();
        String expected = "Ljava.util.List;";
        assertEquals(expected, signature);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new TypeReference<String>() {}.getType();
        JavaType result = javaType.withContentType(contentType);
        assertEquals(javaType, result);
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        JavaType result = javaType.withTypeHandler(handler);
        assertEquals(javaType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        JavaType result = javaType.withContentTypeHandler(handler);
        assertEquals(javaType, result);
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        JavaType result = javaType.withValueHandler(handler);
        assertEquals(javaType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        JavaType result = javaType.withContentValueHandler(handler);
        assertEquals(javaType, result);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType result = javaType.withStaticTyping();
        assertEquals(javaType, result);
    }

    @Test
    public void testNarrow() {
        Class<List<Number>> listOfNumbers = List.class.getTypeParameters()[0].getType();
        JavaType javaTypeNarrow = javaType.narrow(listOfNumbers);
        assertNull(javaTypeNarrow);
    }

    @Test
    public void testRefine() {
        Class<List<String>> listOfStrings = List.class.getTypeParameters()[0].getType();
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType result = javaType.refine(listOfStrings, new TypeBindings(), superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        assertFalse(javaType.isContainerType());
    }

    @Test
    public void testToString() {
        String expected = "[recursive type; UNRESOLVED]";
        assertEquals(expected, javaType.toString());

        JavaType refType = new TypeReference<List<String>>() {}.getType();
        javaType.setReference(refType);
        expected = "[recursive type; " + refType.getRawClass().getName() + "]";
        assertEquals(expected, javaType.toString());
    }

    @Test
    public void testEquals() {
        JavaType javaTypeRef = new ResolvedRecursiveType(javaType.getRawClass(), new TypeBindings());
        assertFalse(javaType.equals(javaTypeRef));

        JavaType refType = new TypeReference<List<String>>() {}.getType();
        javaType.setReference(refType);
        JavaType javaTypeRefWithRef = new ResolvedRecursiveType(javaType.getRawClass(), new TypeBindings());
        javaTypeRefWithRef.setReference(refType);
        assertTrue(javaType.equals(javaTypeRefWithRef));
    }
}