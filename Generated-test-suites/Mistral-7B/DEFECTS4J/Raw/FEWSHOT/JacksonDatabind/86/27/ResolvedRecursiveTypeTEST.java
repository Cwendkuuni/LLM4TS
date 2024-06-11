package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeBinding;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.TypeHandler;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        Class<?> erasedType = String[].class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        type.setReference(ref);
        JavaType expected = ref;
        assertSame(expected, type.getSelfReferencedType());

        try {
            type.setReference(ref);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = String[].class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        type.setReference(ref);

        JavaType expected = ref;
        assertSame(expected, type.getSelfReferencedType());

        assertNull(new ResolvedRecursiveType(String.class, bindings).getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> erasedType = String[].class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);
        StringBuilder sb = mock(StringBuilder.class);

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        type.setReference(ref);

        when(ref.getGenericSignature(sb)).thenReturn(sb);

        type.getGenericSignature(sb);
        verify(ref).getGenericSignature(sb);
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> erasedType = String[].class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);
        StringBuilder sb = mock(StringBuilder.class);

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        type.setReference(ref);

        when(ref.getErasedSignature(sb)).thenReturn(sb);

        type.getErasedSignature(sb);
        verify(ref).getErasedSignature(sb);
    }

    @Test
    public void testWithContentType() {
        Class<?> erasedType = String[].class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);
        JavaType contentType = mock(JavaType.class);

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        type.setReference(ref);

        type.withContentType(contentType);
        JavaType expected = type;
        assertSame(expected, type);

        verify(ref, never()).withContentType(contentType);
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> erasedType = String[].class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);
        Object handler = mock(Object.class);

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        type.setReference(ref);

        type.withTypeHandler(handler);
        JavaType expected = type;
        assertSame(expected, type);

        verify(ref, never()).withTypeHandler(handler);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> erasedType = String[].class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);
        Object handler = mock(Object.class);

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        type.setReference(ref);

        type.withContentTypeHandler(handler);
        JavaType expected = type;
        assertSame(expected, type);

        verify(ref, never()).withContentTypeHandler(handler);
    }

    @Test
    public void testWithValueHandler() {
        Class<?> erasedType = String[].class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);
        Object handler = mock(Object.class);

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        type.setReference(ref);

        type.withValueHandler(handler);
        JavaType expected = type;
        assertSame(expected, type);

        verify(ref, never()).withValueHandler(handler);
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> erasedType = String[].class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);
        Object handler = mock(Object.class);

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        type.setReference(ref);

        type.withContentValueHandler(handler);
        JavaType expected = type;
        assertSame(expected, type);

        verify(ref, never()).withContentValueHandler(handler);
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> erasedType = String[].class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        type.setReference(ref);

        type.withStaticTyping();
        JavaType expected = type;
        assertSame(expected, type);

        verify(ref, never()).withStaticTyping();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNarrow() {
        Class<?> erasedType = String[].class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);
        Class<?> subclass = String[].class;

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        type.setReference(ref);

        type._narrow(subclass);
        fail("UnsupportedOperationException expected");
    }

    @Test
    public void testRefine() {
        Class<?> erasedType = String[].class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);
        Class<?> rawType = String[].class;
        JavaType superClass = mock(JavaType.class);
        JavaType[] superInterfaces = new JavaType[0];

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        type.setReference(ref);

        assertNull(type.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        Class<?> erasedType = String[].class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        type.setReference(ref);

        assertFalse(type.isContainerType());
    }

    @Test
    public void testToString() {
        Class<?> erasedType = String[].class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);
        type.setReference(ref);

        String expected = "[recursive type; UNRESOLVED]";
        String result = type.toString();
        assertEquals(expected, result);

        when(ref.getRawClass()).thenReturn(String[].class);
        expected = "[recursive type; [" + String[].class.getName() + "]]";
        result = type.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        Class<?> erasedType = String[].class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref1 = mock(JavaType.class);
        JavaType ref2 = mock(JavaType.class);

        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, bindings);
        type1.setReference(ref1);

        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType, bindings);
        type2.setReference(ref2);

        assertFalse(type1.equals(type2));

        JavaType sameRef = ref1;
        type2.setReference(sameRef);

        assertTrue(type1.equals(type2));
    }
}