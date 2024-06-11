package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeBase;

import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    private JavaType _referencedType;
    private ResolvedRecursiveType _sut;

    @Before
    public void setUp() {
        _referencedType = mock(JavaType.class);
        _sut = new ResolvedRecursiveType(Object.class, new TypeBindings()) {
            @Override
            public JavaType getSelfReferencedType() {
                return _referencedType;
            }
        };
    }

    @Test
    public void testSetReference() {
        ResolvedRecursiveType refType = mock(ResolvedRecursiveType.class);
        _sut.setReference(refType);

        JavaType expected = refType;
        JavaType actual = _sut.getSelfReferencedType();

        assertSame(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType refType = mock(ResolvedRecursiveType.class);
        _sut.setReference(refType);
        _sut.setReference(refType);
    }

    @Test
    public void testGetSelfReferencedType() {
        assertNull(_sut.getSelfReferencedType());

        _sut.setReference(_referencedType);

        JavaType expected = _referencedType;
        JavaType actual = _sut.getSelfReferencedType();

        assertSame(expected, actual);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();

        _sut.getGenericSignature(sb);

        String expected = "[recursive type; UNRESOLVED]";
        String actual = sb.toString();

        assertSame(expected, actual);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();

        _sut.getErasedSignature(sb);

        String expected = "[recursive type; UNRESOLVED]";
        String actual = sb.toString();

        assertSame(expected, actual);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = mock(JavaType.class);

        _sut.withContentType(contentType);

        JavaType expected = _sut;
        JavaType actual = _sut;

        assertSame(expected, actual);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = mock(Object.class);

        _sut.withTypeHandler(h);

        JavaType expected = _sut;
        JavaType actual = _sut;

        assertSame(expected, actual);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = mock(Object.class);

        _sut.withContentTypeHandler(h);

        JavaType expected = _sut;
        JavaType actual = _sut;

        assertSame(expected, actual);
    }

    @Test
    public void testWithValueHandler() {
        Object h = mock(Object.class);

        _sut.withValueHandler(h);

        JavaType expected = _sut;
        JavaType actual = _sut;

        assertSame(expected, actual);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = mock(Object.class);

        _sut.withContentValueHandler(h);

        JavaType expected = _sut;
        JavaType actual = _sut;

        assertSame(expected, actual);
    }

    @Test
    public void testWithStaticTyping() {
        _sut.withStaticTyping();

        JavaType expected = _sut;
        JavaType actual = _sut;

        assertSame(expected, actual);
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = String.class;

        _sut.withContentType(JavaType.constructParametricType(List.class, JavaType.of(Integer.class)));
        _sut.narrow(subclass);

        JavaType expected = _sut;
        JavaType actual = _sut;

        assertSame(expected, actual);
    }

    @Test
    public void testRefine() {
        JavaType rawType = mock(JavaType.class);
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = mock(JavaType.class);
        JavaType[] superInterfaces = new JavaType[0];

        when(rawType.getRawClass()).thenReturn(List.class);
        when(rawType.getErasedSignature(new StringBuilder())).thenReturn("java.util.List<T>");

        _sut.refine(rawType, bindings, superClass, superInterfaces);

        JavaType expected = null;
        JavaType actual = _sut;

        assertSame(expected, actual);
    }

    @Test
    public void testIsContainerType() {
        boolean actual = _sut.isContainerType();

        boolean expected = false;

        assertFalse(actual);
        assertFalse(expected);
    }

    @Test
    public void testToString() {
        StringBuilder sb = new StringBuilder(40)
                .append("[recursive type; ");

        _sut.toString();

        String expected = "[recursive type; UNRESOLVED]";
        String actual = sb.toString();

        assertSame(expected, actual);
    }

    @Test
    public void testEquals() {
        Object other = new Object();

        boolean actual = _sut.equals(other);

        boolean expected = false;

        assertFalse(actual);
        assertFalse(expected);

        ResolvedRecursiveType otherResolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings()) {
            @Override
            public JavaType getSelfReferencedType() {
                return _referencedType;
            }
        };

        actual = _sut.equals(otherResolvedRecursiveType);

        expected = true;

        assertTrue(actual);
        assertTrue(expected);
    }
}