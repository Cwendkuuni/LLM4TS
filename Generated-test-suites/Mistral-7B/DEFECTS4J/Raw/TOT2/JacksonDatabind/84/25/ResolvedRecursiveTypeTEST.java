
package com.example;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ResolvedRecursiveTypeTest {

    private static final String EXPECTED_TYPE_NAME = "java.util.List";

    @Test
    public void testConstructor() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testSetReference() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = mock(JavaType.class);
        resolvedRecursiveType.setReference(ref);
        assertNotNull(resolvedRecursiveType.getSelfReferencedType());
        assertSame(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testConstructWithNullArguments() {
        assertNull(new ResolvedRecursiveType(null, null));
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = mock(JavaType.class);
        resolvedRecursiveType.setReference(ref);
        resolvedRecursiveType.setReference(ref);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType javaType = mock(JavaType.class);
        resolvedRecursiveType.setReference(javaType);
        assertEquals(javaType, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testConstructWithNullErasedType() {
        TypeBindings bindings = new TypeBindings();
        assertNull(new ResolvedRecursiveType(null, bindings));
    }

    @Test(expected = NullPointerException.class)
    public void testSetReferenceWithNullReference() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(null);
    }

    @Test
    public void testGetGenericSignature() {
        String expectedTypeName = "java.util.List<T>";
        JavaType listType = TypeFactory.defaultInstance().constructType(expectedTypeName);
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(listType);

        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(sb);
        String genericSignature = sb.toString();
        assertEquals("[java.util.List<T>]", genericSignature);
    }

    @Test
    public void testGetErasedSignature() {
        String expectedTypeName = "Ljava.util.List;";
        JavaType listType = TypeFactory.defaultInstance().constructType(EXPECTED_TYPE_NAME);
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(listType);

        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(sb);
        String erasedSignature = sb.toString();
        assertEquals("[Ljava.util.List;", erasedSignature);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = mock(JavaType.class);
        JavaType[] superInterfaces = new JavaType[0];

        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(rawType, bindings);
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testConstructorWithNullBindings() {
        Class<?> erasedType = Object.class;
        TypeBindings nullBindings = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, nullBindings);
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }
}