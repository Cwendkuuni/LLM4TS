package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveTypeConstructor() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType actual = new ResolvedRecursiveType(erasedType, bindings);
        ResolvedRecursiveType expected = new ResolvedRecursiveType(erasedType, bindings);
        assertEquals(expected, actual);
    }

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        JavaType ref = new ClassType(String.class);
        resolvedRecursiveType.setReference(ref);
        JavaType actual = resolvedRecursiveType.getSelfReferencedType();
        JavaType expected = ref;
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceIllegalStateException() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        JavaType ref = new ClassType(String.class);
        resolvedRecursiveType.setReference(ref);
        resolvedRecursiveType.setReference(new ClassType(Integer.class));
    }

    @Test
    public void testGetSelfReferencedTypeNull() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(sb);
        String expected = "[recursive type; UNRESOLVED]";
        String actual = sb.toString();
        assertEquals(expected, actual);

        JavaType ref = new ClassType(String.class);
        resolvedRecursiveType.setReference(ref);
        StringBuilder sb2 = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(sb2);
        String actual2 = sb2.toString();
        String expected2 = "[recursive type; " + ref.getRawClass().getName() + "]";
        assertEquals(expected2, actual2);
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(sb);
        String expected = "[recursive type; UNRESOLVED]";
        String actual = sb.toString();
        assertEquals(expected, actual);

        JavaType ref = new ClassType(String.class);
        resolvedRecursiveType.setReference(ref);
        StringBuilder sb2 = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(sb2);
        String actual2 = sb2.toString();
        String expected2 = ref.getRawClass().getName();
        assertEquals(expected2, actual2);
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        JavaType contentType = new ArrayType(IntegerType.Instance, 5);
        JavaType actual = resolvedRecursiveType.withContentType(contentType);
        JavaType expected = resolvedRecursiveType;
        assertEquals(expected, actual);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        Object h = new Object();
        JavaType actual = resolvedRecursiveType.withTypeHandler(h);
        JavaType expected = resolvedRecursiveType;
        assertEquals(expected, actual);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        Object h = new Object();
        JavaType actual = resolvedRecursiveType.withContentTypeHandler(h);
        JavaType expected = resolvedRecursiveType;
        assertEquals(expected, actual);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        Object h = new Object();
        JavaType actual = resolvedRecursiveType.withValueHandler(h);
        JavaType expected = resolvedRecursiveType;
        assertEquals(expected, actual);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        Object h = new Object();
        JavaType actual = resolvedRecursiveType.withContentValueHandler(h);
        JavaType expected = resolvedRecursiveType;
        assertEquals(expected, actual);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        JavaType actual = resolvedRecursiveType.withStaticTyping();
        JavaType expected = resolvedRecursiveType;
        assertEquals(expected, actual);
    }

    @Test
    @Deprecated
    public void testNarrow() {
        // Not testing as method is deprecated
    }

    @Test
    public void testRefineNull() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        assertNull(resolvedRecursiveType.refine(null, new TypeBindings(), null, new JavaType[0]));
    }

    @Test
    public void testRefineWithNullTypeBindings() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        assertNull(resolvedRecursiveType.refine(ResolvedRecursiveType.class, null, null, new JavaType[0]));
    }

    @Test
    public void testRefineWithNullSuperClass() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        assertNull(resolvedRecursiveType.refine(ResolvedRecursiveType.class, new TypeBindings(), null, new JavaType[0]));
    }

    @Test
    public void testRefineWithNullSuperInterfaces() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        assertNull(resolvedRecursiveType.refine(ResolvedRecursiveType.class, new TypeBindings(), null, new JavaType[0], (JavaType[]) new Object[0]));
    }

    @Test
    public void testRefineWithEmptySuperInterfaces() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        JavaType[] superInterfaces = new JavaType[0];
        assertNull(resolvedRecursiveType.refine(ResolvedRecursiveType.class, new TypeBindings(), null, null, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToStringNullReferencedType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        String expected = "[recursive type; UNRESOLVED]";
        String actual = resolvedRecursiveType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringWithReferencedType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        JavaType ref = new ClassType(String.class);
        resolvedRecursiveType.setReference(ref);
        String expected = "[recursive type; " + ref.getRawClass().getName() + "]";
        String actual = resolvedRecursiveType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEqualsNullObject() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        assertFalse(resolvedRecursiveType.equals(null));
    }

    @Test
    public void testEqualsSameInstance() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType2 = resolvedRecursiveType1;
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }

    @Test
    public void testEqualsDifferentTypes() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        Object obj = new Object();
        assertFalse(resolvedRecursiveType1.equals(obj));
    }

    @Test
    public void testEqualsDifferentInstances() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }

    @Test
    public void testEqualsSameReferencedTypes() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        JavaType ref1 = new ClassType(String.class);
        resolvedRecursiveType1.setReference(ref1);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        JavaType ref2 = new ClassType(String.class);
        resolvedRecursiveType2.setReference(ref2);
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }

    @Test
    public void testEqualsDifferentReferencedTypes() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        JavaType ref1 = new ClassType(String.class);
        resolvedRecursiveType1.setReference(ref1);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        JavaType ref2 = new ClassType(Integer.class);
        resolvedRecursiveType2.setReference(ref2);
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }
}