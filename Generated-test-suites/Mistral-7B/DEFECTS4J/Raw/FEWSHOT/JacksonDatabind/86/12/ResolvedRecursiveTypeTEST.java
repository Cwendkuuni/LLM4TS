package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings()) {
            @Override
            public JavaType getSelfReferencedType() {
                return null;
            }
        };

        type.setReference(_referencedType);
        assertSame(_referencedType, type.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings()) {
            @Override
            public JavaType getSelfReferencedType() {
                return null;
            }
        };

        JavaType ref1 = mock(JavaType.class);
        JavaType ref2 = mock(JavaType.class);

        type.setReference(ref1);
        type.setReference(ref2);
    }

    @Test
    public void testGetSelfReferencedType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings()) {
            @Override
            public JavaType getSelfReferencedType() {
                return null;
            }
        };
        assertNull(type.getSelfReferencedType());

        type.setReference(_referencedType);
        assertSame(_referencedType, type.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        when(_referencedType.getGenericSignature(new StringBuilder())).thenReturn(new StringBuilder(""));

        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings()) {
            @Override
            public JavaType getSelfReferencedType() {
                return _referencedType;
            }
        };

        StringBuilder sb = new StringBuilder(40);
        type.getGenericSignature(sb);
        assertSame("", sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        when(_referencedType.getErasedSignature(new StringBuilder())).thenReturn(new StringBuilder(""));

        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings()) {
            @Override
            public JavaType getSelfReferencedType() {
                return _referencedType;
            }
        };

        StringBuilder sb = new StringBuilder(40);
        type.getErasedSignature(sb);
        assertSame("", sb.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, new TypeBindings()) {
            @Override
            public JavaType getSelfReferencedType() {
                return null;
            }
        };
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, new TypeBindings()) {
            @Override
            public JavaType getSelfReferencedType() {
                return null;
            }
        };

        assertFalse(type1.equals(type2));

        JavaType ref = mock(JavaType.class);

        type1.setReference(ref);
        type2.setReference(ref);

        assertTrue(type1.equals(type2));
    }
}