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

    private JavaType _mockReferencedType;

    @Before
    public void setUp() {
        _mockReferencedType = mock(JavaType.class);
    }

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        type.setReference(_mockReferencedType);
        assertSame(_mockReferencedType, type.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        type.setReference(_mockReferencedType);
        type.setReference(new JavaType() {});
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        when(type.getSelfReferencedType()).thenReturn(null);
        assertEquals("[recursive type; UNRESOLVED]", type.toString());

        type.setReference(_mockReferencedType);
        when(type.getSelfReferencedType()).thenReturn(_mockReferencedType);
        assertEquals("[recursive type; " + _mockReferencedType.getRawClass().getName() + "]", type.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertFalse(type1.equals(type2));

        JavaType refType = mock(JavaType.class);
        type1.setReference(refType);
        type2.setReference(refType);
        assertTrue(type1.equals(type2));

        type2.setReference(new JavaType() {});
        assertFalse(type1.equals(type2));
    }

    @Test
    public void testGetSelfReferencedType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertNull(type.getSelfReferencedType());

        JavaType refType = mock(JavaType.class);
        type.setReference(refType);
        assertSame(refType, type.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        assertEquals("", sb.toString());

        JavaType refType = mock(JavaType.class);
        type.setReference(refType);
        when(refType.getGenericSignature(sb)).thenReturn(sb.append("T").toString());
        assertEquals("T", type.getGenericSignature(sb).toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        assertEquals("", sb.toString());

        JavaType refType = mock(JavaType.class);
        type.setReference(refType);
        when(refType.getErasedSignature(sb)).thenReturn(sb.append("String").toString());
        assertEquals("String", type.getErasedSignature(sb).toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType contentType = new JavaType() {};
        assertEquals(type, type.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object typeHandler = new Object() {};
        assertEquals(type, type.withTypeHandler(typeHandler));
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object contentTypeHandler = new Object() {};
        assertEquals(type, type.withContentTypeHandler(contentTypeHandler));
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object valueHandler = new Object() {};
        assertEquals(type, type.withValueHandler(valueHandler));
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object contentValueHandler = new Object() {};
        assertEquals(type, type.withContentValueHandler(contentValueHandler));
    }
}