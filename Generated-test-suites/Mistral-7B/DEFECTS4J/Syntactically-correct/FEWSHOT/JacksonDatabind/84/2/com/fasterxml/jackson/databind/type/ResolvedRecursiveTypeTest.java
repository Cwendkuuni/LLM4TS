package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = TypeBindings.empty();
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(intType, bindings);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);

        selfRefType.setReference(refType);

        JavaType expectedRefType = refType;
        JavaType actualRefType = selfRefType.getSelfReferencedType();

        assertEquals("Reference types should be equal", expectedRefType, actualRefType);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = TypeBindings.empty();
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(intType, bindings);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);

        selfRefType.setReference(refType);

        // This should throw an IllegalStateException
        selfRefType.setReference(refType);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = TypeBindings.empty();
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(intType, bindings);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);

        selfRefType.setReference(refType);

        assertSame("Self-referenced types should be the same instance", refType, selfRefType.getSelfReferencedType());
    }

    @Test
    public void testGetSelfReferencedType_Null() {
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(Object.class, TypeBindings.empty());
        assertNull("Self-referenced type should be null for uninitialized type", selfRefType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = TypeBindings.empty();
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(intType, bindings);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);

        selfRefType.setReference(refType);

        StringBuilder expectedSignature = new StringBuilder("[recursive type; java.lang.String]");
        StringBuilder actualSignature = selfRefType.getGenericSignature(new StringBuilder());

        assertEquals("Generic signatures should be equal", expectedSignature.toString(), actualSignature.toString());
    }

    @Test
    public void testGetErasedSignature() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = TypeBindings.empty();
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(intType, bindings);
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);

        selfRefType.setReference(refType);

        StringBuilder expectedSignature = new StringBuilder("java.lang.String");
        StringBuilder actualSignature = selfRefType.getErasedSignature(new StringBuilder());

        assertEquals("Erased signatures should be equal", expectedSignature.toString(), actualSignature.toString());
    }

    @Test
    public void testWithContentType() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = TypeBindings.empty();
        JavaType contentType = TypeFactory.defaultInstance().constructType(String.class);
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(intType, bindings);

        JavaType expectedSelfRefType = selfRefType;
        JavaType actualSelfRefType = selfRefType.withContentType(contentType);

        assertEquals("WithContentType should return the same instance", expectedSelfRefType, actualSelfRefType);
    }

    @Test
    public void testWithTypeHandler() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = TypeBindings.empty();
        Object handler = new Object();
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(intType, bindings);

        JavaType expectedSelfRefType = selfRefType;
        JavaType actualSelfRefType = selfRefType.withTypeHandler(handler);

        assertEquals("WithTypeHandler should return the same instance", expectedSelfRefType, actualSelfRefType);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = TypeBindings.empty();
        Object handler = new Object();
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(intType, bindings);

        JavaType expectedSelfRefType = selfRefType;
        JavaType actualSelfRefType = selfRefType.withContentTypeHandler(handler);

        assertEquals("WithContentTypeHandler should return the same instance", expectedSelfRefType, actualSelfRefType);
    }

    @Test
    public void testWithValueHandler() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = TypeBindings.empty();
        Object handler = new Object();
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(intType, bindings);

        JavaType expectedSelfRefType = selfRefType;
        JavaType actualSelfRefType = selfRefType.withValueHandler(handler);

        assertEquals("WithValueHandler should return the same instance", expectedSelfRefType, actualSelfRefType);
    }

    @Test
    public void testWithContentValueHandler() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = TypeBindings.empty();
        Object handler = new Object();
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(intType, bindings);

        JavaType expectedSelfRefType = selfRefType;
        JavaType actualSelfRefType = selfRefType.withContentValueHandler(handler);

        assertEquals("WithContentValueHandler should return the same instance", expectedSelfRefType, actualSelfRefType);
    }

    @Test
    public void testWithStaticTyping() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = TypeBindings.empty();
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(intType, bindings);

        JavaType expectedSelfRefType = selfRefType;
        JavaType actualSelfRefType = selfRefType.withStaticTyping();

        assertEquals("WithStaticTyping should return the same instance", expectedSelfRefType, actualSelfRefType);
    }

    @Test
    public void testToString() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = TypeBindings.empty();
        ResolvedRecursiveType selfRefType = new ResolvedRecursiveType(intType, bindings);

        StringBuilder expectedString = new StringBuilder("[recursive type; java.lang.Integer]");
        StringBuilder actualString = new StringBuilder(selfRefType.toString());

        assertEquals("ToString should produce the same string representation", expectedString.toString(), actualString.toString());

        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        selfRefType.setReference(stringType);

        expectedString = new StringBuilder("[recursive type; java.lang.String]");
        actualString = new StringBuilder(selfRefType.toString());

        assertEquals("ToString should update string representation when reference changes", expectedString.toString(), actualString.toString());
    }

    @Test
    public void testEquals() {
        Class<Integer> intType = Integer.TYPE;
        TypeBindings bindings = TypeBindings.empty();
        ResolvedRecursiveType selfRefType1 = new ResolvedRecursiveType(intType, bindings);
        ResolvedRecursiveType selfRefType2 = new ResolvedRecursiveType(intType, bindings);

        assertSame("Self-referenced types with the same reference should be equal", selfRefType1, selfRefType2);

        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        selfRefType1.setReference(refType);
        selfRefType2.setReference(refType);

        assertSame("Self-referenced types with the same reference should be equal", selfRefType1, selfRefType2);

        ResolvedRecursiveType otherRefType = new ResolvedRecursiveType(String.class, bindings);
        assertFalse("Self-referenced types with different references should not be equal", selfRefType1.equals(otherRefType));

        assertFalse("Null types should not be equal to self-referenced types", selfRefType1.equals(null));

        ResolvedRecursiveType selfRefType3 = new ResolvedRecursiveType(intType, bindings);
        assertFalse("Self-referenced types with uninitialized references should not be equal", selfRefType1.equals(selfRefType3));
    }
}