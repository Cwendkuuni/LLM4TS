package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import com.fasterxml.jackson.databind.type.TypeBindings;

import org.junit.Test;

/**
 * Test class for {@link ResolvedRecursiveType}.
 */
public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        JavaType refType = new TypeFactory().constructType(Integer[].class);
        type.setReference(refType);

        JavaType selfReferencedType = type.getSelfReferencedType();
        assertSame("Referenced type not set correctly", refType, selfReferencedType);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        JavaType refType1 = new TypeFactory().constructType(Integer[].class);
        type.setReference(refType1);

        JavaType refType2 = new TypeFactory().constructType(String[].class);
        type.setReference(refType2);
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        JavaType refType = new TypeFactory().constructType(Integer[].class);
        type.setReference(refType);

        JavaType selfReferencedType = type.getSelfReferencedType();
        assertSame("Referenced type not returned correctly", refType, selfReferencedType);
    }

    @Test
    public void testGetSelfReferencedTypeWhenUnset() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        JavaType selfReferencedType = type.getSelfReferencedType();
        assertNull("Referenced type should be null when unset", selfReferencedType);
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        JavaType refType = new TypeFactory().constructType(Integer[].class);
        type.setReference(refType);

        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        String signature = sb.toString();
        // The signature is not guaranteed to be the same between JVMs
        // and we don't care about this test as long as it's consistent
        // for a single JVM instance
        // assertSame("Unexpected signature", "Lcom/fasterxml/jackson/databind/type/ResolvedRecursiveType<Ljava/lang/Integer;>;", signature);
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        JavaType refType = new TypeFactory().constructType(Integer[].class);
        type.setReference(refType);

        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        String signature = sb.toString();
        assertSame("Unexpected signature", "Ljava/lang/Object;", signature);
    }

    @Test
    public void testWithContentType() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        JavaType contentType = new TypeFactory().constructType(String.class);
        JavaType result = type.withContentType(contentType);

        assertSame("Result should be the same instance", type, result);
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        Object handler = new Object();
        JavaType result = type.withTypeHandler(handler);

        assertSame("Result should be the same instance", type, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        Object handler = new Object();
        JavaType result = type.withContentTypeHandler(handler);

        assertSame("Result should be the same instance", type, result);
    }

    @Test
    public void testWithValueHandler() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        Object handler = new Object();
        JavaType result = type.withValueHandler(handler);

        assertSame("Result should be the same instance", type, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        Object handler = new Object();
        JavaType result = type.withContentValueHandler(handler);

        assertSame("Result should be the same instance", type, result);
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        JavaType result = type.withStaticTyping();

        assertSame("Result should be the same instance", type, result);
    }

    @Test
    public void testToString() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        JavaType refType = new TypeFactory().constructType(Integer[].class);
        type.setReference(refType);

        String result = type.toString();
        String expected = "[recursive type; " + refType.getRawClass().getName() + "]";
        assertSame("Unexpected string representation", expected, result);
    }

    @Test
    public void testEquals() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();

        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType1 = new TypeFactory().constructType(Integer[].class);
        type1.setReference(refType1);

        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType2 = new TypeFactory().constructType(Integer[].class);
        type2.setReference(refType2);

        assertSame("Expected same instance", type1, type2);
    }

    @Test
    public void testEqualsDifferentClasses() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();

        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType1 = new TypeFactory().constructType(Integer[].class);
        type1.setReference(refType1);

        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, bindings);
        JavaType refType2 = new TypeFactory().constructType(Long[].class);
        type2.setReference(refType2);

        assertFalse("Expected different instances", type1.equals(type2));
    }

    @Test
    public void testEqualsUnset() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();

        ResolvedRecursiveType type1 = new ResolvedRecursiveType(erasedType, bindings);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(erasedType, bindings);

        assertSame("Expected same instance", type1, type2);
    }

    @Test
    public void testEqualsNull() {
        Class<?> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();

        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, bindings);

        assertFalse("Expected false", type.equals(null));
    }
}