package com.example.junit; // replace with your test package if needed

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructorWithClass() {
        SimpleType simpleType = new SimpleType(String.class);
        assertInstanceOf(SimpleType.class, simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testSimpleTypeConstructorWithClassBindingsSuperClassSuperInts() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertInstanceOf(SimpleType.class, simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeMethodWithClassThrowsException() {
        SimpleType.constructUnsafe(String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMethodWithClassThrowsException() {
        SimpleType.construct(String.class);
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder expected = new StringBuilder("[Ljava/lang/String;");
        StringBuilder actual = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder expected = new StringBuilder("Ljava/lang/String;");
        StringBuilder actual = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(String[].class);
        StringBuilder expected = new StringBuilder("[Ljava.lang.String;");
        StringBuilder actual = simpleType.buildCanonicalName();
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testIsContainerTypeReturnsFalse() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testToStringReturnsExpectedString() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder expected = new StringBuilder("[simple type, class [Ljava.lang.String;]");
        StringBuilder actual = simpleType.toString();
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testEqualsWithSameInstanceReturnsTrue() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = simpleType1;
        assertTrue(simpleType1.equals(simpleType2));
    }

    @Test
    public void testEqualsWithDifferentInstancesButSameValuesReturnsTrue() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        assertTrue(simpleType1.equals(simpleType2));
    }

    @Test
    public void testEqualsWithDifferentInstancesButDifferentValuesReturnsFalse() {
        SimpleType simpleType1 = new SimpleType(Integer.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        assertFalse(simpleType1.equals(simpleType2));
    }

    @Test
    public void testNarrowMethodWithSameClassReturnsSameInstance() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType narrowedType = simpleType.narrow(String.class);
        assertSame(simpleType, narrowedType);
    }

    @Test
    public void testNarrowMethodWithSubclassReturnsNewInstance() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType narrowedType = simpleType.narrow(StringBuffer.class);
        assertNotSame(simpleType, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowMethodWithUnrelatedClassThrowsException() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.narrow(Integer.class);
    }

    @Test
    public void testWithTypeHandlerReturnsNewInstance() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        SimpleType withTypeHandler = simpleType.withTypeHandler(handler);
        assertNotSame(simpleType, withTypeHandler);
    }

    @Test
    public void testWithContentTypeHandlerReturnsNull() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNull(simpleType.withContentTypeHandler(null));
    }

    @Test
    public void testWithValueHandlerReturnsNewInstance() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        SimpleType withValueHandler = simpleType.withValueHandler(handler);
        assertNotSame(simpleType, withValueHandler);
    }

    @Test
    public void testWithContentValueHandlerReturnsNull() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNull(simpleType.withContentValueHandler(null));
    }

    @Test
    public void testWithStaticTypingReturnsNewInstance() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertNotSame(simpleType, withStaticTyping);
    }

    @Test
    public void testRefineReturnsNull() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNull(simpleType.refine(String.class, null, null, null));
    }
}