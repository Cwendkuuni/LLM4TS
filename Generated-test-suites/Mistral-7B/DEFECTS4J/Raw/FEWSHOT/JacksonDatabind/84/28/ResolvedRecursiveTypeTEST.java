package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeBindings.TypeSerializerBindings;

import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    private JavaType _mockType;
    private TypeBindings _mockBindings;

    @Before
    public void setup() {
        _mockType = mock(JavaType.class);
        _mockBindings = new TypeBindings();
    }

    @Test
    public void testNullSelfReference() {
        ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, _mockBindings);
        assertNull(actual.getSelfReferencedType());
    }

    @Test
    public void testSetSelfReference() {
        JavaType ref = mock(JavaType.class);
        ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, _mockBindings);
        actual.setReference(ref);
        assertSame(ref, actual.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetSelfReferenceTwice() {
        JavaType ref = mock(JavaType.class);
        ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, _mockBindings);
        actual.setReference(ref);
        actual.setReference(_mockType);
    }

    @Test
    public void testGetGenericSignature() {
        when(_mockType.getGenericSignature(Mockito.any(StringBuilder.class))).thenReturn(new StringBuilder("[]"));
        ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, _mockBindings);
        actual.setReference(_mockType);

        StringBuilder expected = new StringBuilder("[recursive type; [Ljava.lang.Object;]");
        StringBuilder result = actual.getGenericSignature(new StringBuilder());

        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetErasedSignature() {
        when(_mockType.getErasedSignature(Mockito.any(StringBuilder.class))).thenReturn(new StringBuilder("Object"));
        ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, _mockBindings);
        actual.setReference(_mockType);

        StringBuilder expected = new StringBuilder("[Object]");
        StringBuilder result = actual.getErasedSignature(new StringBuilder());

        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, _mockBindings);
        actual.setReference(_mockType);

        JavaType expected = _mockType;
        JavaType result = actual.withContentType(null);

        assertEquals(expected, result);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, _mockBindings);
        actual.setReference(_mockType);

        Object expected = null;
        Object result = actual.withTypeHandler(expected);

        assertEquals(expected, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, _mockBindings);
        actual.setReference(_mockType);

        Object expected = null;
        Object result = actual.withContentTypeHandler(expected);

        assertEquals(expected, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, _mockBindings);
        actual.setReference(_mockType);

        Object expected = null;
        Object result = actual.withValueHandler(expected);

        assertEquals(expected, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, _mockBindings);
        actual.setReference(_mockType);

        Object expected = null;
        Object result = actual.withContentValueHandler(expected);

        assertEquals(expected, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, _mockBindings);
        actual.setReference(_mockType);

        JavaType expected = actual;
        JavaType result = actual.withStaticTyping();

        assertEquals(expected, result);
    }

    @Test
    public void testToString() {
        JavaType ref = mock(JavaType.class);
        when(ref.getRawClass().getName()).thenReturn("java.util.List");

        ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, _mockBindings);
        actual.setReference(ref);

        StringBuilder expected = new StringBuilder("[recursive type; java.util.List]");
        StringBuilder result = new StringBuilder(actual.toString());

        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testEquals() {
        JavaType ref1 = mock(JavaType.class);
        JavaType ref2 = mock(JavaType.class);
        ResolvedRecursiveType actual1 = new ResolvedRecursiveType(Object.class, _mockBindings);
        ResolvedRecursiveType actual2 = new ResolvedRecursiveType(Object.class, _mockBindings);

        when(ref1.getRawClass().getName()).thenReturn("java.util.List");
        when(ref2.getRawClass().getName()).thenReturn("java.util.Map");

        actual1.setReference(ref1);
        actual2.setReference(ref2);

        assertFalse(actual1.equals(actual2));
    }

    @Test
    public void testEqualsSameInstance() {
        JavaType ref = mock(JavaType.class);
        ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, _mockBindings);

        when(ref.getRawClass().getName()).thenReturn("java.util.List");

        actual.setReference(ref);
        actual.setReference(ref);

        assertTrue(actual.equals(actual));
    }

    @Test
    public void testEqualsSelfReference() {
        JavaType ref = mock(JavaType.class);
        ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, _mockBindings);

        when(ref.getRawClass().getName()).thenReturn("java.util.List");

        actual.setReference(ref);

        assertTrue(actual.equals(actual));
    }
}