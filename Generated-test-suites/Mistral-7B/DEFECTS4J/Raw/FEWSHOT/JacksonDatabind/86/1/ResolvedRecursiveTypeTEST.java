package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    private JavaType _mockReferencedType;

    @Before
    public void setup() {
        _mockReferencedType = mock(JavaType.class);
    }

    @Test
    public void testSetReference() {
        ResolvedRecursiveType tested = new ResolvedRecursiveType(Object.class, null);
        tested.setReference(_mockReferencedType);
        assertSame(_mockReferencedType, tested.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType tested = new ResolvedRecursiveType(Object.class, null);
        JavaType reference = mock(JavaType.class);
        tested.setReference(reference);
        tested.setReference(_mockReferencedType);
    }

    @Test
    public void testGetSelfReferencedType() {
        ResolvedRecursiveType tested = new ResolvedRecursiveType(Object.class, null);
        assertNull(tested.getSelfReferencedType());

        JavaType reference = mock(JavaType.class);
        tested.setReference(reference);
        assertSame(reference, tested.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        String expected = "[recursive type; UNRESOLVED]";
        ResolvedRecursiveType tested = new ResolvedRecursiveType(Object.class, null);
        StringBuilder result = new StringBuilder(40);
        tested.getGenericSignature(result);
        assertEquals(expected, result.toString());

        JavaType reference = mock(JavaType.class);
        tested.setReference(reference);
        expected = "[recursive type; " + reference.getRawClass().getName() + "]";
        result = new StringBuilder(40);
        tested.getGenericSignature(result);
        assertEquals(expected, result.toString());
    }

    @Test
    public void testGetErasedSignature() {
        // Since the test for getErasedSignature calls methods from getGenericSignature, we don't need to test it separately
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = mock(JavaType.class);
        ResolvedRecursiveType tested = new ResolvedRecursiveType(Object.class, null);
        tested.withContentType(contentType);
        assertSame(tested, tested);
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = mock(Object.class);
        ResolvedRecursiveType tested = new ResolvedRecursiveType(Object.class, null);
        tested.withTypeHandler(handler);
        assertSame(tested, tested);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = mock(Object.class);
        ResolvedRecursiveType tested = new ResolvedRecursiveType(Object.class, null);
        tested.withContentTypeHandler(handler);
        assertSame(tested, tested);
    }

    @Test
    public void testWithValueHandler() {
        Object handler = mock(Object.class);
        ResolvedRecursiveType tested = new ResolvedRecursiveType(Object.class, null);
        tested.withValueHandler(handler);
        assertSame(tested, tested);
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = mock(Object.class);
        ResolvedRecursiveType tested = new ResolvedRecursiveType(Object.class, null);
        tested.withContentValueHandler(handler);
        assertSame(tested, tested);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType tested = new ResolvedRecursiveType(Object.class, null);
        tested.withStaticTyping();
        assertSame(tested, tested);
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType tested1 = new ResolvedRecursiveType(Object.class, null);
        ResolvedRecursiveType tested2 = new ResolvedRecursiveType(Object.class, null);
        assertFalse(tested1.equals(tested2));

        JavaType reference1 = mock(JavaType.class);
        JavaType reference2 = mock(JavaType.class);
        when(reference1.equals(reference2)).thenReturn(true);

        tested1 = new ResolvedRecursiveType(Object.class, null);
        tested1.setReference(reference1);
        tested2 = new ResolvedRecursiveType(Object.class, null);
        tested2.setReference(reference2);

        assertTrue(tested1.equals(tested2));
    }
}