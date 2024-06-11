package com.example.junit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

/**
 * Test class for {@link ResolvedRecursiveType}.
 */
public class ResolvedRecursiveTypeTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(resolvedRecursiveType);
    }

    /**
     * Test setReference method with null argument.
     */
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceWithNullArgument() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Object constructorArg = null;
        Object[] constructorArgs = new Object[] { constructorArg };
        resolvedRecursiveType.getClass().getMethod("setReference", JavaType.class).invoke(resolvedRecursiveType, constructorArgs[0]);
    }

    /**
     * Test setReference method with non-null argument.
     */
    @Test
    public void testSetReference() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new ClassType(String.class);
        resolvedRecursiveType.setReference(ref);
        JavaType expected = ref;
        JavaType actual = resolvedRecursiveType.getSelfReferencedType();
        assertSame(expected, actual);
    }

    /**
     * Test getSelfReferencedType method with uninitialized _referencedType field.
     */
    @Test
    public void testGetSelfReferencedTypeWithUninitializedReferencedType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType expected = null;
        JavaType actual = resolvedRecursiveType.getSelfReferencedType();
        assertSame(expected, actual);
    }

    /**
     * Test getSelfReferencedType method with initialized _referencedType field.
     */
    @Test
    public void testGetSelfReferencedTypeWithInitializedReferencedType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = new ClassType(String.class);
        resolvedRecursiveType.setReference(ref);
        JavaType expected = ref;
        JavaType actual = resolvedRecursiveType.getSelfReferencedType();
        assertSame(expected, actual);
    }

    /**
     * Test getGenericSignature method with empty StringBuilder argument.
     */
    @Test
    public void testGetGenericSignatureWithEmptyStringBuilder() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(sb);
        String expected = "";
        String actual = sb.toString();
        assertSame(expected, actual);
    }

    /**
     * Test getGenericSignature method with non-empty StringBuilder argument.
     */
    @Test
    public void testGetGenericSignatureWithNonEmptyStringBuilder() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder sb = new StringBuilder("Test");
        JavaType ref = new ClassType(String.class);
        resolvedRecursiveType.setReference(ref);
        resolvedRecursiveType.getGenericSignature(sb);
        String expected = "<T>";
        String actual = sb.toString();
        assertSame(expected, actual);
    }

    /**
     * Test getErasedSignature method with empty StringBuilder argument.
     */
    @Test
    public void testGetErasedSignatureWithEmptyStringBuilder() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(sb);
        String expected = "";
        String actual = sb.toString();
        assertSame(expected, actual);
    }

    /**
     * Test getErasedSignature method with non-empty StringBuilder argument.
     */
    @Test
    public void testGetErasedSignatureWithNonEmptyStringBuilder() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        StringBuilder sb = new StringBuilder("Test");
        JavaType ref = new ClassType(String.class);
        resolvedRecursiveType.setReference(ref);
        resolvedRecursiveType.getErasedSignature(sb);
        String expected = "Ljava/lang/String;";
        String actual = sb.toString();
        assertSame(expected, actual);
    }

    /**
     * Test withContentType method.
     */
    @Test
    public void testWithContentType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType contentType = new ClassType(Integer.class);
        ResolvedRecursiveType expected = resolvedRecursiveType;
        ResolvedRecursiveType actual = (ResolvedRecursiveType) resolvedRecursiveType.withContentType(contentType);
        assertSame(expected, actual);
    }

    /**
     * Test withTypeHandler method.
     */
    @Test
    public void testWithTypeHandler() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();
        ResolvedRecursiveType expected = resolvedRecursiveType;
        ResolvedRecursiveType actual = (ResolvedRecursiveType) resolvedRecursiveType.withTypeHandler(handler);
        assertSame(expected, actual);
    }

    /**
     * Test withContentTypeHandler method.
     */
    @Test
    public void testWithContentTypeHandler() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();
        ResolvedRecursiveType expected = resolvedRecursiveType;
        ResolvedRecursiveType actual = (ResolvedRecursiveType) resolvedRecursiveType.withContentTypeHandler(handler);
        assertSame(expected, actual);
    }

    /**
     * Test withValueHandler method.
     */
    @Test
    public void testWithValueHandler() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();
        ResolvedRecursiveType expected = resolvedRecursiveType;
        ResolvedRecursiveType actual = (ResolvedRecursiveType) resolvedRecursiveType.withValueHandler(handler);
        assertSame(expected, actual);
    }

    /**
     * Test withContentValueHandler method.
     */
    @Test
    public void testWithContentValueHandler() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Object handler = new Object();
        ResolvedRecursiveType expected = resolvedRecursiveType;
        ResolvedRecursiveType actual = (ResolvedRecursiveType) resolvedRecursiveType.withContentValueHandler(handler);
        assertSame(expected, actual);
    }

    /**
     * Test withStaticTyping method.
     */
    @Test
    public void testWithStaticTyping() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        ResolvedRecursiveType expected = resolvedRecursiveType;
        ResolvedRecursiveType actual = (ResolvedRecursiveType) resolvedRecursiveType.withStaticTyping();
        assertSame(expected, actual);
    }

    /**
     * Test _narrow method with deprecated annotation.
     * <p>
     * This test does not exercise the method's functionality as it is deprecated,
     * but rather checks that it throws a {@link DeprecationException} when called.
     */
    @Test(expected = DeprecationException.class)
    public void testDeprecatedNarrowMethod() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Class<?> subclass = String.class;
        resolvedRecursiveType._narrow(subclass);
    }

    /**
     * Test refine method with null arguments.
     */
    @Test(expected = NullPointerException.class)
    public void testRefineWithNullArguments() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Object[] constructorArgs = new Object[] {};
        resolvedRecursiveType.getClass().getConstructor(Class.class, TypeBindings.class, JavaType.class, JavaType[].class).invoke(resolvedRecursiveType, constructorArgs);
    }

    /**
     * Test refine method with non-null arguments.
     */
    @Test
    public void testRefine() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        Class<?> rawType = String.class;
        TypeBindings newBindings = new TypeBindings();
        JavaType superClass = new ClassType(Object.class);
        JavaType[] superInterfaces = new JavaType[] {};
        Object[] constructorArgs = new Object[] { rawType, newBindings, superClass, superInterfaces };
        ResolvedRecursiveType expected = new ResolvedRecursiveType(rawType, newBindings);
        ResolvedRecursiveType actual = (ResolvedRecursiveType) resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        assertSame(expected, actual);
    }

    /**
     * Test isContainerType method.
     */
    @Test
    public void testIsContainerType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertFalse(resolvedRecursiveType.isContainerType());
    }
}