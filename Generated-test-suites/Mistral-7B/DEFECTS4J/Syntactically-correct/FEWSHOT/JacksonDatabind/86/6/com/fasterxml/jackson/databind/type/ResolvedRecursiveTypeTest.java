package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.json.TypeIdResolver;

import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    private JavaType _referencedType;

    @Before
    public void setup() {
        _referencedType = mock(JavaType.class);
    }

    @Test
    public void testSetReference() {
        ResolvedRecursiveType sut = new ResolvedRecursiveType(Object.class, null);
        sut.setReference(_referencedType);

        assertSame(sut._referencedType, _referencedType);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType sut = new ResolvedRecursiveType(Object.class, null);
        sut.setReference(_referencedType);
        sut.setReference(new JavaType() {
            // empty implementation
        });
    }

    @Test
    public void testGetSelfReferencedType() {
        ResolvedRecursiveType sut = new ResolvedRecursiveType(Object.class, null);
        assertNull(sut.getSelfReferencedType());

        sut.setReference(_referencedType);
        assertSame(sut.getSelfReferencedType(), _referencedType);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType sut = new ResolvedRecursiveType(Object.class, null);
        StringBuilder sb = new StringBuilder();
        sut.getGenericSignature(sb);
        assertNull(sb.toString());

        sut.setReference(_referencedType);
        when(_referencedType.getGenericSignature(mock(StringBuilder.class))).thenReturn(sb);
        sb.setLength(0);
        sut.getGenericSignature(sb);
        assertEquals("[]", sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType sut = new ResolvedRecursiveType(Object.class, null);
        StringBuilder sb = new StringBuilder();
        sut.getErasedSignature(sb);
        assertNull(sb.toString());

        sut.setReference(_referencedType);
        when(_referencedType.getErasedSignature(mock(StringBuilder.class))).thenReturn(sb);
        sb.setLength(0);
        sut.getErasedSignature(sb);
        assertEquals("class java.lang.Object", sb.toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType sut = new ResolvedRecursiveType(Object.class, null);
        JavaType contentType = mock(JavaType.class);
        assertSame(sut, sut.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType sut = new ResolvedRecursiveType(Object.class, null);
        Object handler = mock(Object.class);
        assertSame(sut, sut.withTypeHandler(handler));
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType sut = new ResolvedRecursiveType(Object.class, null);
        Object handler = mock(Object.class);
        assertSame(sut, sut.withContentTypeHandler(handler));
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType sut = new ResolvedRecursiveType(Object.class, null);
        Object handler = mock(Object.class);
        assertSame(sut, sut.withValueHandler(handler));
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType sut = new ResolvedRecursiveType(Object.class, null);
        Object handler = mock(Object.class);
        assertSame(sut, sut.withContentValueHandler(handler));
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType sut = new ResolvedRecursiveType(Object.class, null);
        assertSame(sut, sut.withStaticTyping());
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType sut = new ResolvedRecursiveType(Object.class, null);
        Class<?> subclass = String.class;
        assertSame(sut, sut.withStaticTyping());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRefine() {
        ResolvedRecursiveType sut = new ResolvedRecursiveType(Object.class, null);
        Class<?> rawType = Object.class;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType superClass = mock(JavaType.class);
        JavaType[] superInterfaces = new JavaType[0];
        sut.refine(rawType, bindings, superClass, superInterfaces);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType sut = new ResolvedRecursiveType(Object.class, null);
        assertFalse(sut.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType sut = new ResolvedRecursiveType(Object.class, null);
        assertEquals("[recursive type; UNRESOLVED]", sut.toString());

        sut.setReference(_referencedType);
        when(_referencedType.getRawClass().getName()).thenReturn("MyClass");
        assertEquals("[recursive type; MyClass]", sut.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType sut1 = new ResolvedRecursiveType(Object.class, null);
        ResolvedRecursiveType sut2 = new ResolvedRecursiveType(Object.class, null);
        assertFalse(sut1.equals(sut2));

        JavaType ref = mock(JavaType.class);
        ResolvedRecursiveType sut3 = new ResolvedRecursiveType(Object.class, null);
        sut3.setReference(ref);
        ResolvedRecursiveType sut4 = new ResolvedRecursiveType(Object.class, null);
        sut4.setReference(ref);
        assertTrue(sut3.equals(sut4));
    }

    @Test
    public void testEqualsWithNull() {
        ResolvedRecursiveType sut = new ResolvedRecursiveType(Object.class, null);
        assertFalse(sut.equals(null));
    }
}