package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

/**
 * Test class for {@link ResolvedRecursiveType}.
 */
public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType type;
    private JavaType referencedType;

    @Before
    public void setUp() {
        referencedType = new ClassType(String.class);
        type = new ResolvedRecursiveType(Object.class, new TypeBindings());
    }

    @Test
    public void testSetReference() {
        type.setReference(referencedType);
        JavaType actual = type.getSelfReferencedType();
        assertSame(referencedType, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        type.setReference(referencedType);
        type.setReference(new ClassType(Integer.class));
    }

    @Test
    public void testGetSelfReferencedType() {
        type.setReference(referencedType);
        JavaType actual = type.getSelfReferencedType();
        assertSame(referencedType, actual);
    }

    @Test
    public void testGetSelfReferencedTypeWhenUninitialized() {
        JavaType actual = type.getSelfReferencedType();
        assertNull(actual);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder expected = new StringBuilder();
        type.getGenericSignature(expected);
        StringBuilder actual = new StringBuilder();
        referencedType.getGenericSignature(actual);
        assertSame(expected.toString(), actual.toString());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder expected = new StringBuilder();
        type.getErasedSignature(expected);
        StringBuilder actual = new StringBuilder();
        referencedType.getErasedSignature(actual);
        assertSame(expected.toString(), actual.toString());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new ClassType(Integer.class);
        JavaType actual = type.withContentType(contentType);
        assertSame(type, actual);
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        JavaType actual = type.withTypeHandler(handler);
        assertSame(type, actual);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        JavaType actual = type.withContentTypeHandler(handler);
        assertSame(type, actual);
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        JavaType actual = type.withValueHandler(handler);
        assertSame(type, actual);
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        JavaType actual = type.withContentValueHandler(handler);
        assertSame(type, actual);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType actual = type.withStaticTyping();
        assertSame(type, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNarrow() {
        Class<?> subclass = String.class;
        JavaType actual = type._narrow(subclass);
        assertNull(actual);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new ClassType(List.class);
        JavaType[] superInterfaces = new JavaType[] { new ClassType(Serializable.class) };
        JavaType actual = type.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(actual);
    }

    @Test
    public void testIsContainerType() {
        boolean actual = type.isContainerType();
        assertFalse(actual);
    }

    @Test
    public void testToString() {
        StringBuilder expected = new StringBuilder("[recursive type; ");
        expected.append(referencedType.getRawClass().getName());
        expected.append("]");
        String actual = type.toString();
        assertSame(expected.toString(), actual);
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(Object.class, new TypeBindings());
        assertFalse(type.equals(other));

        other.setReference(referencedType);
        assertTrue(type.equals(other));
    }
}