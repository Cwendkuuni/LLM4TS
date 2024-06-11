package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.TypeSerializer;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        JavaType ref = mock(JavaType.class);
        ResolvedRecursiveType self = new ResolvedRecursiveType(Object.class, null);
        self.setReference(ref);
        assertSame(ref, self.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        JavaType ref1 = mock(JavaType.class);
        JavaType ref2 = mock(JavaType.class);
        ResolvedRecursiveType self = new ResolvedRecursiveType(Object.class, null);
        self.setReference(ref1);
        self.setReference(ref2);
    }

    @Test
    public void testGetSelfReferencedType() {
        JavaType ref = mock(JavaType.class);
        ResolvedRecursiveType self = new ResolvedRecursiveType(Object.class, null);
        self.setReference(ref);
        assertSame(ref, self.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType self = new ResolvedRecursiveType(Object.class, null);
        TypeBindings bindings = mock(TypeBindings.class);
        StringBuilder sb = mock(StringBuilder.class);
        when(sb.append(anyString())).thenAnswer(i -> sb);
        when(self.getSelfReferencedType()).thenReturn(mock(JavaType.class));
        self.getGenericSignature(sb);
        // no assertions here, as the implementation is not important
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType self = new ResolvedRecursiveType(Object.class, null);
        TypeBindings bindings = mock(TypeBindings.class);
        StringBuilder sb = mock(StringBuilder.class);
        when(sb.append(anyString())).thenAnswer(i -> sb);
        when(self.getSelfReferencedType()).thenReturn(mock(JavaType.class));
        self.getErasedSignature(sb);
        // no assertions here, as the implementation is not important
    }

    @Test
    public void testWithContentType() {
        JavaType self = mock(JavaType.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        when(type.getSelfReferencedType()).thenReturn(self);
        JavaType result = type.withContentType(mock(JavaType.class));
        assertSame(type, result);
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = mock(Object.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType referencedType = mock(JavaType.class);
        when(type.getSelfReferencedType()).thenReturn(referencedType);
        JavaType result = type.withTypeHandler(handler);
        assertSame(type, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = mock(Object.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType referencedType = mock(JavaType.class);
        when(type.getSelfReferencedType()).thenReturn(referencedType);
        JavaType result = type.withContentTypeHandler(handler);
        assertSame(type, result);
    }

    @Test
    public void testWithValueHandler() {
        Object handler = mock(Object.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType referencedType = mock(JavaType.class);
        when(type.getSelfReferencedType()).thenReturn(referencedType);
        JavaType result = type.withValueHandler(handler);
        assertSame(type, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = mock(Object.class);
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType referencedType = mock(JavaType.class);
        when(type.getSelfReferencedType()).thenReturn(referencedType);
        JavaType result = type.withContentValueHandler(handler);
        assertSame(type, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType result = type.withStaticTyping();
        assertSame(type, result);
    }

    @Test
    public void testNarrow() {
        // Deprecated method
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        Class<?> subclass = String.class;
        JavaType result = type._narrow(subclass);
        assertSame(type, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType refinedType = mock(JavaType.class);
        when(type.getSelfReferencedType()).thenReturn(null);
        JavaType result = type.refine(Object.class, null, null, new JavaType[0]);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testToString() {
        TypeBindings bindings = mock(TypeBindings.class);
        ResolvedRecursiveType self = new ResolvedRecursiveType(Object.class, bindings);
        TypeSerializer serializer = mock(TypeSerializer.class);
        when(bindings.getSerializer(self)).thenReturn(serializer);
        String result = self.toString();
        String expected = "[recursive type; UNRESOLVED]";
        assertEquals(expected, result);

        JavaType ref = mock(JavaType.class);
        self.setReference(ref);
        result = self.toString();
        expected = "[recursive type; " + ref.getRawClass().getName() + "]";
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        TypeBindings bindings = mock(TypeBindings.class);
        JavaType ref1 = mock(JavaType.class);
        JavaType ref2 = mock(JavaType.class);
        ResolvedRecursiveType self1 = new ResolvedRecursiveType(Object.class, bindings);
        ResolvedRecursiveType self2 = new ResolvedRecursiveType(Object.class, bindings);
        self1.setReference(ref1);
        self2.setReference(ref2);
        assertFalse(self1.equals(self2));

        ResolvedRecursiveType self3 = new ResolvedRecursiveType(Object.class, bindings);
        self3.setReference(ref1);
        assertTrue(self1.equals(self3));

        ResolvedRecursiveType self4 = new ResolvedRecursiveType(Object.class, bindings);
        TypeDeserializer deserializer = mock(TypeDeserializer.class);
        when(bindings.getDeserializer(self1)).thenReturn(deserializer);
        assertTrue(self1.equals(self4));

        ResolvedRecursiveType self5 = new ResolvedRecursiveType(String.class, bindings);
        assertFalse(self1.equals(self5));
    }
}