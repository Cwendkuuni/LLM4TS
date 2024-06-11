
package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
    }

    @Test
    public void testSetReference_ValidInput_Success() {
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        Assert.assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReference_AlreadySet_ThrowsException() {
        JavaType ref1 = new JavaType();
        resolvedRecursiveType.setReference(ref1);

        JavaType ref2 = new JavaType();
        resolvedRecursiveType.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature_ReturnsExpectedSignature() {
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);

        StringBuilder sb = new StringBuilder();
        StringBuilder expected = ref.getGenericSignature(sb);

        StringBuilder actual = resolvedRecursiveType.getGenericSignature(new StringBuilder());

        Assert.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetErasedSignature_ReturnsExpectedSignature() {
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);

        StringBuilder sb = new StringBuilder();
        StringBuilder expected = ref.getErasedSignature(sb);

        StringBuilder actual = resolvedRecursiveType.getErasedSignature(new StringBuilder());

        Assert.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testWithContentType_ReturnsThis() {
        JavaType contentType = new JavaType();
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler_ReturnsThis() {
        Object h = new Object();
        JavaType result = resolvedRecursiveType.withTypeHandler(h);
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler_ReturnsThis() {
        Object h = new Object();
        JavaType result = resolvedRecursiveType.withContentTypeHandler(h);
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler_ReturnsThis() {
        Object h = new Object();
        JavaType result = resolvedRecursiveType.withValueHandler(h);
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler_ReturnsThis(){
        Object h = new Object();
        JavaType result = resolvedRecursiveType.withContentValueHandler(h);
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping_ReturnsThis() {
        JavaType result = resolvedRecursiveType.withStaticTyping();
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testDeprecatedNarrow_ReturnsThis() {
        Class<?> subclass = ResolvedRecursiveType.class;
        JavaType result = resolvedRecursiveType._narrow(subclass);
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine_ReturnsNull() {
        Class<?> rawType = ResolvedRecursiveType.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;

        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        Assert.assertNull(result);
    }

    @Test
    public void testIsContainerType_ReturnsFalse() {
        boolean result = resolvedRecursiveType.isContainerType();
        Assert.assertFalse(result);
    }

    @Test
    public void testToString_UnresolvedReference_ReturnsExpectedString() {
        String actual = resolvedRecursiveType.toString();
        String expected = "[recursive type; UNRESOLVED]";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToString_ResolvedReference_ReturnsExpectedString() {
        JavaType ref = new JavaType(ResolvedRecursiveType.class);
        resolvedRecursiveType.setReference(ref);

        String actual = resolvedRecursiveType.toString();
        String expected = "[recursive type; com.fasterxml.jackson.databind.type.ResolvedRecursiveType]";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEquals_SameObject_ReturnsTrue() {
        boolean result = resolvedRecursiveType.equals(resolvedRecursiveType);
        Assert.assertTrue(result);
    }

    @Test
    public void testEquals_NullObject_ReturnsFalse() {
        boolean result = resolvedRecursiveType.equals(null);
        Assert.assertFalse(result);
    }

    @Test
    public void testEquals_InstanceOfDifferentClass_ReturnsFalse() {
        Object obj = new Object();
        boolean result = resolvedRecursiveType.equals(obj);
        Assert.assertFalse(result);
    }

    @Test
    public void testEquals_UnresolvedReference_ReturnsFalse() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        boolean result = resolvedRecursiveType.equals(other);
        Assert.assertFalse(result);
    }

    @Test
    public void testEquals_ResolvedReference_ReturnsTrue() {
        JavaType ref = new JavaType(ResolvedRecursiveType.class);
        resolvedRecursiveType.setReference(ref);

        ResolvedRecursiveType other = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        other.setReference(ref);

        boolean result = resolvedRecursiveType.equals(other);
        Assert.assertTrue(result);
    }
}
