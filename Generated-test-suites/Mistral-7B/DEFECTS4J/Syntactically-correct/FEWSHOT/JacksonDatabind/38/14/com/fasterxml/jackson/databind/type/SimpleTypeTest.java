package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertSame(String.class, simpleType.getClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testConstruct() {
        // Deprecated since 2.7, do not use
        throw new IllegalArgumentException("Method 'construct' is deprecated since 2.7.");
    }

    @Test
    public void testNarrow() {
        SimpleType parentType = new SimpleType(Number.class, TypeBindings.emptyBindings(), null, null);
        SimpleType childType = new SimpleType(Integer.class, TypeBindings.emptyBindings(), null, null);

        SimpleType narrowedType = parentType.narrow(Integer.class);
        assertSame(childType, narrowedType);
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        Object handler = new Object();
        SimpleType newSimpleType = simpleType.withTypeHandler(handler);
        assertSame(simpleType, newSimpleType); // no change, handler is not set yet

        SimpleType newSimpleType2 = newSimpleType.withTypeHandler(handler);
        assertNotSame(simpleType, newSimpleType2);
        assertTrue(newSimpleType2.getTypeHandler() == handler);
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(String[].class, TypeBindings.emptyBindings(), null, null);
        String canonicalName = simpleType.buildCanonicalName();
        assertTrue(canonicalName.equals("[[Ljava.lang.String;]"));
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertTrue(simpleType1.equals(simpleType2));

        SimpleType simpleType3 = new SimpleType(Integer.class, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType1.equals(simpleType3));
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String[].class, TypeBindings.emptyBindings(), null, null);
        StringBuilder erasedSignature = new StringBuilder();
        simpleType.getErasedSignature(erasedSignature);
        assertTrue(erasedSignature.toString().equals("[Ljava.lang.String;"));
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(List.class, TypeBindings.emptyBindings(), null, null);
        StringBuilder genericSignature = new StringBuilder();
        simpleType.getGenericSignature(genericSignature);
        assertTrue(genericSignature.toString().equals("java.util.List<T>"));
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[simple type, class ");
        stringBuilder.append(simpleType.buildCanonicalName());
        stringBuilder.append(']');
        assertTrue(simpleType.toString().equals(stringBuilder.toString()));
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        Class<?> rawType = Integer.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = new JavaType[0];
        JavaType refinedType = simpleType.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(refinedType);
    }
}