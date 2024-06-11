package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    private JavaType _referencedType;

    @Before
    public void setUp() {
        _referencedType = mock(JavaType.class);
    }

    @Test
    public void testSetReference() {
        ResolvedRecursiveType self = new ResolvedRecursiveType(Object.class, null);
        self.setReference(_referencedType);
        assertSame(self, self.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType self = new ResolvedRecursiveType(Object.class, null);
        self.setReference(_referencedType);
        self.setReference(_referencedType);
    }

    @Test
    public void testGetSelfReferencedType() {
        ResolvedRecursiveType self = new ResolvedRecursiveType(Object.class, null);
        assertNull(self.getSelfReferencedType());

        self.setReference(_referencedType);
        assertSame(self, self.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType self = new ResolvedRecursiveType(Object.class, null);
        StringBuilder sb = new StringBuilder();
        self.getGenericSignature(sb);
        assertEquals("", sb.toString());

        self.setReference(_referencedType);
        sb.setLength(0);
        self.getGenericSignature(sb);
        assertEquals("Ljava/lang/Object;L" + _referencedType.getRawClass().getName() + ";", sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType self = new ResolvedRecursiveType(Object.class, null);
        StringBuilder sb = new StringBuilder();
        self.getErasedSignature(sb);
        assertEquals("Ljava/lang/Object;", sb.toString());

        self.setReference(_referencedType);
        sb.setLength(0);
        self.getErasedSignature(sb);
        assertEquals("Ljava/lang/Object;L" + _referencedType.getRawClass().getName() + ";", sb.toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType self = new ResolvedRecursiveType(Object.class, null);
        JavaType contentType = mock(JavaType.class);
        self.withContentType(contentType);
        assertSame(self, self);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType self = new ResolvedRecursiveType(Object.class, null);
        Object handler = mock(Object.class);
        self.withTypeHandler(handler);
        assertSame(self, self);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType self = new ResolvedRecursiveType(Object.class, null);
        Object handler = mock(Object.class);
        self.withContentTypeHandler(handler);
        assertSame(self, self);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType self = new ResolvedRecursiveType(Object.class, null);
        Object handler = mock(Object.class);
        self.withValueHandler(handler);
        assertSame(self, self);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType self = new ResolvedRecursiveType(Object.class, null);
        Object handler = mock(Object.class);
        self.withContentValueHandler(handler);
        assertSame(self, self);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType self = new ResolvedRecursiveType(Object.class, null);
        self.withStaticTyping();
        assertSame(self, self);
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType self = new ResolvedRecursiveType(Object.class, null);
        assertNull(self.withNarrow(Object[].class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRefine() {
        ResolvedRecursiveType self = new ResolvedRecursiveType(Object.class, null);
        self.refine(Object.class, null, null, new JavaType[0]);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType self = new ResolvedRecursiveType(Object.class, null);
        assertFalse(self.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType self = new ResolvedRecursiveType(Object.class, null);
        assertEquals("[recursive type; UNRESOLVED]", self.toString());

        self.setReference(_referencedType);
        assertEquals("[recursive type; " + _referencedType.getRawClass().getName() + "]", self.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType self = new ResolvedRecursiveType(Object.class, null);
        ResolvedRecursiveType other = new ResolvedRecursiveType(Object.class, null);
        assertFalse(self.equals(other));

        self.setReference(_referencedType);
        assertFalse(self.equals(other));

        ResolvedRecursiveType otherSelf = new ResolvedRecursiveType(Object.class, null);
        self.setReference(_referencedType);
        otherSelf.setReference(_referencedType);
        assertTrue(self.equals(otherSelf));
    }
}