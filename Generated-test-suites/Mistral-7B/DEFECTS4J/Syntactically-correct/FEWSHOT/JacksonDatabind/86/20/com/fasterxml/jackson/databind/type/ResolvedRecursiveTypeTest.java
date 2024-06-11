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
    private TypeBase typeBase;

    @Before
    public void setUp() {
        _referencedType = mock(JavaType.class);
        typeBase = new ResolvedRecursiveType(null, new TypeBindings()) {
            @Override
            public JavaType getSelfReferencedType() {
                return _referencedType;
            }
        };
    }

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, new TypeBindings()) {
            @Override
            public JavaType getSelfReferencedType() {
                return null;
            }
        };

        JavaType ref = mock(JavaType.class);

        type.setReference(ref);

        assertSame(ref, type.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        JavaType ref1 = mock(JavaType.class);
        JavaType ref2 = mock(JavaType.class);

        ResolvedRecursiveType type = new ResolvedRecursiveType(null, new TypeBindings()) {
            @Override
            public JavaType getSelfReferencedType() {
                return null;
            }
        };

        type.setReference(ref1);
        type.setReference(ref2);
    }

    @Test
    public void testGetSelfReferencedType() {
        when(_referencedType.getRawClass()).thenReturn(Object.class);

        assertNull(typeBase.getSelfReferencedType());

        typeBase.setReference(_referencedType);

        assertSame(Object.class, typeBase.getSelfReferencedType().getRawClass());
    }

    @Test
    public void testGetGenericSignature() {
        when(_referencedType.getGenericSignature(new StringBuilder())).thenReturn(new StringBuilder("List<String>"));

        StringBuilder sb = new StringBuilder();

        assertSame(new StringBuilder("List<String>"), typeBase.getGenericSignature(sb));
    }

    @Test
    public void testGetErasedSignature() {
        when(_referencedType.getErasedSignature(new StringBuilder())).thenReturn(new StringBuilder("List"));

        StringBuilder sb = new StringBuilder();

        assertSame(new StringBuilder("List"), typeBase.getErasedSignature(sb));
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = mock(JavaType.class);

        assertSame(typeBase, typeBase.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = mock(Object.class);

        assertSame(typeBase, typeBase.withTypeHandler(handler));
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = mock(Object.class);

        assertSame(typeBase, typeBase.withContentTypeHandler(handler));
    }

    @Test
    public void testWithValueHandler() {
        Object handler = mock(Object.class);

        assertSame(typeBase, typeBase.withValueHandler(handler));
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = mock(Object.class);

        assertSame(typeBase, typeBase.withContentValueHandler(handler));
    }

    @Test
    public void testWithStaticTyping() {
        assertSame(typeBase, typeBase.withStaticTyping());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(null, new TypeBindings()) {
            @Override
            public JavaType getSelfReferencedType() {
                return _referencedType;
            }
        };

        assertSame(typeBase, typeBase);
        assertNotSame(other, typeBase);

        when(_referencedType.equals(mock(JavaType.class))).thenReturn(true);

        assertSame(typeBase, other);
    }
}