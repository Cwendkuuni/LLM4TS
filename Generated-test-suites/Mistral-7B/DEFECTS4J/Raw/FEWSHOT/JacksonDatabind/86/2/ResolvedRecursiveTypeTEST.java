package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);
        ResolvedRecursiveType self = new ResolvedRecursiveType(intType, bindings);

        self.setReference(ref);

        assertSame(ref, self.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref1 = mock(JavaType.class);
        JavaType ref2 = mock(JavaType.class);
        ResolvedRecursiveType self = new ResolvedRecursiveType(intType, bindings);

        self.setReference(ref1);
        self.setReference(ref2);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);
        ResolvedRecursiveType self = new ResolvedRecursiveType(intType, bindings);

        self.setReference(ref);

        assertSame(ref, self.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);
        ResolvedRecursiveType self = new ResolvedRecursiveType(intType, bindings);
        StringBuilder sb = mock(StringBuilder.class);

        self.setReference(ref);
        when(ref.getGenericSignature(sb)).thenReturn(sb);

        self.getGenericSignature(sb);

        // verify ref's getGenericSignature was called once
        verify(ref, times(1)).getGenericSignature(sb);
    }

    @Test
    public void testGetErasedSignature() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);
        ResolvedRecursiveType self = new ResolvedRecursiveType(intType, bindings);
        StringBuilder sb = mock(StringBuilder.class);

        self.setReference(ref);
        when(ref.getErasedSignature(sb)).thenReturn(sb);

        self.getErasedSignature(sb);

        // verify ref's getErasedSignature was called once
        verify(ref, times(1)).getErasedSignature(sb);
    }

    @Test
    public void testEquals() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref1 = mock(JavaType.class);
        JavaType ref2 = mock(JavaType.class);
        JavaType ref3 = mock(JavaType.class);
        ResolvedRecursiveType self = new ResolvedRecursiveType(intType, bindings);
        ResolvedRecursiveType other = new ResolvedRecursiveType(intType, bindings);

        self.setReference(ref1);
        other.setReference(ref2);

        assertFalse(self.equals(other));

        self.setReference(ref2);
        assertTrue(self.equals(other));

        // test with reference to itself
        self.setReference(self);
        assertTrue(self.equals(self));

        // test with null
        assertFalse(self.equals(null));

        // test with different classes
        assertFalse(self.equals(new Object()));

        // test with unresolved self-reference
        ResolvedRecursiveType unresolvedSelf = new ResolvedRecursiveType(intType, bindings);
        assertFalse(self.equals(unresolvedSelf));

        // test with same reference but different self-references
        JavaType ref4 = mock(JavaType.class);
        self.setReference(ref1);
        ResolvedRecursiveType other2 = new ResolvedRecursiveType(intType, bindings);
        other2.setReference(ref1);
        other2.setReference(ref4);
        assertFalse(self.equals(other2));
    }

    @Test
    public void testToString() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref = mock(JavaType.class);
        ResolvedRecursiveType self = new ResolvedRecursiveType(intType, bindings);

        self.setReference(ref);

        String expected = "[recursive type; " + ref.getRawClass().getName() + "]";

        String result = self.toString();

        assertEquals(expected, result);
    }
}