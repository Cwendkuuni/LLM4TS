package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    // Basic test cases for constructors

    @Test
    public void testSimpleTypeConstructorWithClass() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testSimpleTypeConstructorWithClassBindingsSuperClassSuperInts() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertNotNull(simpleType);
    }

    @Test
    public void testCopyConstructor() {
        SimpleType original = new SimpleType(String.class);
        SimpleType copy = new SimpleType(original);
        assertNotSame(original, copy);
        assertEquals(original.getClass(), copy.getClass());
        assertEquals(original.getClass(), copy.getClass());
        assertEquals(original.getBindings(), copy.getBindings());
    }

    @Test
    public void testPassThroughConstructor() {
        SimpleType simpleType = new SimpleType(ReferenceType.class, null, null, null, 0, null, false);
        assertNotNull(simpleType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenConstructingMap() {
        new SimpleType(HashMap.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenConstructingCollection() {
        new SimpleType(ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenConstructingArray() {
        new SimpleType(int[].class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenCallingDeprecatedConstructMethod() {
        new SimpleType(String.class);
    }

    // Basic test cases for methods

    @Test
    public void testNarrowMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType narrowedType = simpleType.narrow(String.class);
        assertSame(simpleType, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenNarrowingToUnrelatedClass() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType narrowedType = simpleType.narrow(Integer.class);
    }

    @Test
    public void testWithTypeHandlerMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType withTypeHandler = simpleType.withTypeHandler(new MyTypeHandler());
        assertNotSame(simpleType, withTypeHandler);
        assertEquals(simpleType.getClass(), withTypeHandler.getClass());
        assertNotSame(simpleType.getTypeHandler(), withTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithStaticTypingMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertNotSame(simpleType, withStaticTyping);
        assertEquals(simpleType.getClass(), withStaticTyping.getClass());
        assertTrue(withStaticTyping.isStatic());
        assertFalse(simpleType.isStatic());
    }

    // Tests for edge cases and exceptions

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenCallingNarrowMethodWithNullClass() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.narrow(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenCallingNarrowMethodWithSubclassOfSimpleType() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.narrow(StringBuffer.class);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionWhenCallingNarrowMethodWithParameterizedClass() {
        SimpleType simpleType = new SimpleType(List.class);
        simpleType.narrow(ArrayList.class);
    }

    @Test
    public void testCallingNarrowMethodWithSameClass() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType narrowedType = simpleType.narrow(String.class);
        assertSame(simpleType, narrowedType);
    }

    @Test
    public void testCallingNarrowMethodWithSuperclass() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType narrowedType = simpleType.narrow(Object.class);
        assertNotSame(simpleType, narrowedType);
        assertTrue(simpleType.isAssignableFrom(narrowedType));
    }

    @Test
    public void testCallingNarrowMethodWithInterface() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType narrowedType = simpleType.narrow(Serializable.class);
        assertNotSame(simpleType, narrowedType);
        assertTrue(simpleType.isAssignableFrom(narrowedType));
    }

    @Test
    public void testCallingNarrowMethodWithAbstractClass() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType narrowedType = simpleType.narrow(AbstractClass.class);
        assertNotSame(simpleType, narrowedType);
        assertTrue(simpleType.isAssignableFrom(narrowedType));
    }

    @Test
    public void testCallingNarrowMethodWithSelf() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType narrowedType = simpleType.narrow(String.class);
        assertSame(simpleType, narrowedType);
    }

    @Test
    public void testCallingNarrowMethodWithArray() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType narrowedType = simpleType.narrow(String[].class);
        assertNotSame(simpleType, narrowedType);
        assertTrue(simpleType.isAssignableFrom(narrowedType));
    }

    @Test
    public void testCallingNarrowMethodWithGenericType() {
        SimpleType simpleType = new SimpleType(List.class);
        SimpleType narrowedType = simpleType.narrow(ArrayList.class);
        assertNotSame(simpleType, narrowedType);
        assertTrue(simpleType.isAssignableFrom(narrowedType));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenCallingWithTypeHandlerWithNullHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withTypeHandler(null);
    }

    @Test
    public void testCallingWithTypeHandlerWithDifferentHandlers() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType withTypeHandler1 = simpleType.withTypeHandler(new Handler1());
        SimpleType withTypeHandler2 = simpleType.withTypeHandler(new Handler2());
        assertNotSame(withTypeHandler1, withTypeHandler2);
    }

    @Test
    public void testCallingWithTypeHandlerWithSameHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType withTypeHandler = simpleType.withTypeHandler(new Handler1());
        SimpleType withTypeHandlerAgain = simpleType.withTypeHandler(new Handler1());
        assertSame(withTypeHandler, withTypeHandlerAgain);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenCallingWithContentTypeHandlerWithNullHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentTypeHandler(null);
    }

    @Test
    public void testCallingWithContentTypeHandlerWithDifferentHandlers() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType withContentTypeHandler1 = simpleType.withContentTypeHandler(new Handler1());
        SimpleType withContentTypeHandler2 = simpleType.withContentTypeHandler(new Handler2());
        assertNotSame(withContentTypeHandler1, withContentTypeHandler2);
    }

    @Test
    public void testCallingWithContentTypeHandlerWithSameHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType withContentTypeHandler = simpleType.withContentTypeHandler(new Handler1());
        SimpleType withContentTypeHandlerAgain = simpleType.withContentTypeHandler(new Handler1());
        assertSame(withContentTypeHandler, withContentTypeHandlerAgain);
    }

    @Test
    public void testCallingWithValueHandlerWithDifferentHandlers() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType withValueHandler1 = simpleType.withValueHandler(new Handler1());
        SimpleType withValueHandler2 = simpleType.withValueHandler(new Handler2());
        assertNotSame(withValueHandler1, withValueHandler2);
    }

    @Test
    public void testCallingWithValueHandlerWithSameHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType withValueHandler = simpleType.withValueHandler(new Handler1());
        SimpleType withValueHandlerAgain = simpleType.withValueHandler(new Handler1());
        assertSame(withValueHandler, withValueHandlerAgain);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenCallingWithContentValueHandlerWithNullHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentValueHandler(null);
    }

    @Test
    public void testCallingWithContentValueHandlerWithDifferentHandlers() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType withContentValueHandler1 = simpleType.withContentValueHandler(new Handler1());
        SimpleType withContentValueHandler2 = simpleType.withContentValueHandler(new Handler2());
        assertNotSame(withContentValueHandler1, withContentValueHandler2);
    }

    @Test
    public void testCallingWithContentValueHandlerWithSameHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType withContentValueHandler = simpleType.withContentValueHandler(new Handler1());
        SimpleType withContentValueHandlerAgain = simpleType.withContentValueHandler(new Handler1());
        assertSame(withContentValueHandler, withContentValueHandlerAgain);
    }

    @Test
    public void testCallingWithStaticTypingWithFalse() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertFalse(withStaticTyping.isStatic());
    }

    @Test
    public void testCallingWithStaticTypingWithTrue() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testCallingRefineMethodWithNullClass() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType refinedType = simpleType.refine(null, null, null, null);
        assertNull(refinedType);
    }

    @Test
    public void testCallingRefineMethodWithClass() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType refinedType = simpleType.refine(String.class, null, null, null);
        assertNull(refinedType);
    }

    @Test
    public void testCallingRefineMethodWithBindings() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType refinedType = simpleType.refine(String.class, null, null, null);
        assertNull(refinedType);
    }

    @Test
    public void testCallingRefineMethodWithSuperClass() {
        SimpleType simpleType = new SimpleType(List.class);
        JavaType refinedType = simpleType.refine(ArrayList.class, null, null, null);
        assertNotNull(refinedType);
        assertEquals(ArrayList.class, refinedType.getRawClass());
    }

    @Test
    public void testCallingRefineMethodWithSuperInterfaces() {
        SimpleType simpleType = new SimpleType(List.class);
        JavaType refinedType = simpleType.refine(ArrayList.class, null, null, new Class[] {Serializable.class, Cloneable.class});
        assertNotNull(refinedType);
        assertEquals(ArrayList.class, refinedType.getRawClass());
        assertArrayEquals(new Class[] {Serializable.class, Cloneable.class}, refinedType.getTypes());
    }

    @Test
    public void testCallingRefineMethodWithSuperClassAndSuperInterfaces() {
        SimpleType simpleType = new SimpleType(List.class);
        JavaType refinedType = simpleType.refine(ArrayList.class, null, new Class[] {Serializable.class}, null);
        assertNotNull(refinedType);
        assertEquals(ArrayList.class, refinedType.getRawClass());
        assertArrayEquals(new Class[] {Serializable.class}, refinedType.getTypes());
    }
}