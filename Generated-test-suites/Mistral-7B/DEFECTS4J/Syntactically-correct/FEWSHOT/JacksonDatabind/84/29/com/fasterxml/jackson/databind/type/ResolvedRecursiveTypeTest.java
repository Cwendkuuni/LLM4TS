package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    private JavaType _referencedType;
    private ResolvedRecursiveType _sut;

    @Before
    public void setup() {
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
        JavaType ref = mock(JavaType.class);
        _sut.setReference(ref);
        assertSame(ref, _sut.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        JavaType ref1 = mock(JavaType.class);
        _sut.setReference(ref1);
        JavaType ref2 = mock(JavaType.class);
        _sut.setReference(ref2);
    }

    @Test
    public void testGetSelfReferencedType() {
        assertNull(_sut.getSelfReferencedType());
        _sut.setReference(_referencedType);
        assertSame(_referencedType, _sut.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        String expected = "_ referencedType: ";
        StringBuilder sb = new StringBuilder();
        _sut.getGenericSignature(sb);
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        String expected = "[recursive type; UNRESOLVED]";
        StringBuilder sb = new StringBuilder();
        _sut.getErasedSignature(sb);
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = mock(JavaType.class);
        _sut = new ResolvedRecursiveType(Object.class, new TypeBindings()) {
            @Override
            public JavaType getSelfReferencedType() {
                return _referencedType;
            }
        };
        assertSame(_sut, _sut.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = mock(Object.class);
        assertSame(_sut, _sut.withTypeHandler(handler));
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = mock(Object.class);
        assertSame(_sut, _sut.withContentTypeHandler(handler));
    }

    @Test
    public void testWithValueHandler() {
        Object handler = mock(Object.class);
        assertSame(_sut, _sut.withValueHandler(handler));
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = mock(Object.class);
        assertSame(_sut, _sut.withContentValueHandler(handler));
    }

    @Test
    public void testWithStaticTyping() {
        assertSame(_sut, _sut.withStaticTyping());
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = String.class;
        assertSame(_sut, _sut._narrow(subclass));
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = mock(JavaType.class);
        JavaType[] superInterfaces = new JavaType[0];
        assertNull(_sut.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        assertFalse(_sut.isContainerType());
    }

    @Test
    public void testToString() {
        StringBuilder sb = new StringBuilder(40)
                .append("[recursive type; ");
        if (_referencedType == null) {
            sb.append("UNRESOLVED");
        } else {
            sb.append(_referencedType.getRawClass().getName());
        }
        sb.append(']');
        String expected = sb.toString();
        assertEquals(expected, _sut.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType another = new ResolvedRecursiveType(Object.class, new TypeBindings()) {
            @Override
            public JavaType getSelfReferencedType() {
                return _referencedType;
            }
        };
        assertSame(_sut, _sut);
        assertNotSame(_sut, new Object());
        assertNotSame(_sut, another);
        assertNotSame(_sut, another); // verify that equals is consistent
        _sut.setReference(_referencedType);
        another.setReference(_referencedType);
        assertSame(_sut, another);
    }
}