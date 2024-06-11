package com.example.junit4;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    // Basic test cases for constructors

    @Test
    public void testSimpleTypeConstructorWithClass() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertEquals(cls.getName(), simpleType.getRawClass().getName());
    }

    @Test
    public void testSimpleTypeConstructorWithClassBindingsSuperClassSuperInterfaces() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertEquals(cls.getName(), simpleType.getRawClass().getName());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        SimpleType copiedSimpleType = new SimpleType(simpleType);
        assertEquals(simpleType.getRawClass(), copiedSimpleType.getRawClass());
    }

    @Test
    public void testSimpleTypePassThroughConstructor() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, 0, null, false);
        assertEquals(cls.getName(), simpleType.getRawClass().getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithArray() {
        Class<?> cls = int[].class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        Class<?> cls = HashMap.class;
        SimpleType simpleType = SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        Class<?> cls = ArrayList.class;
        SimpleType simpleType = SimpleType.construct(cls);
    }

    // Test for _narrow method

    @Test
    public void testNarrowMethodWithSameClass() {
        Class<?> cls = String.class;
        SimpleType simpleType1 = new SimpleType(cls);
        SimpleType simpleType2 = new SimpleType(cls);
        SimpleType narrowedType = simpleType1._narrow(String.class);
        assertEquals(simpleType1, narrowedType);
    }

    @Test
    public void testNarrowMethodWithSubclass() {
        Class<?> subclass = StringBuffer.class;
        Class<?> superclass = String.class;
        SimpleType simpleType = new SimpleType(superclass);
        SimpleType narrowedType = simpleType._narrow(subclass);
        assertTrue(narrowedType instanceof SimpleType);
        assertEquals(subclass, narrowedType.getRawClass());
    }

    // Test for isContainerType method

    @Test
    public void testIsContainerTypeMethodReturnsFalse() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertFalse(simpleType.isContainerType());
    }

    // Test for getErasedSignature and getGenericSignature methods

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        StringBuilder expected = new StringBuilder("Ljava/lang/String;");
        assertEquals(expected.toString(), simpleType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = List.class;
        Class<?> listElementType = Integer.class;
        SimpleType simpleType = new SimpleType(cls);
        StringBuilder expected = new StringBuilder("Ljava/util/List;<Ljava/lang/Integer;>");
        assertEquals(expected.toString(), simpleType.getGenericSignature(new StringBuilder()).toString());
    }

    // Test for equals method

    @Test
    public void testEqualsMethodReturnsTrueForSameObject() {
        Class<?> cls = String.class;
        SimpleType simpleType1 = new SimpleType(cls);
        SimpleType simpleType2 = new SimpleType(cls);
        assertTrue(simpleType1.equals(simpleType2));
    }

    @Test
    public void testEqualsMethodReturnsFalseForDifferentObjects() {
        Class<?> cls1 = String.class;
        Class<?> cls2 = Integer.class;
        SimpleType simpleType1 = new SimpleType(cls1);
        SimpleType simpleType2 = new SimpleType(cls2);
        assertFalse(simpleType1.equals(simpleType2));
    }

    // Test for _withContentType, _withTypeHandler, _withValueHandler, _withContentValueHandler, _withStaticTyping methods

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeMethodWithValueType() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        simpleType.withContentType(Integer.TYPE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithTypeHandlerMethodWithNullValueHandler() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        simpleType.withTypeHandler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithValueHandlerMethodWithNullValueHandler() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        simpleType.withValueHandler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandlerMethodWithNullContentValueHandler() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        simpleType.withContentValueHandler(null);
    }

    @Test
    public void testWithStaticTypingMethod() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        SimpleType staticType = simpleType.withStaticTyping();
        assertTrue(staticType.isStaticTyping());
    }

    // Test for refine method

    @Test
    public void testRefineMethodWithSameType() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        JavaType refinedType = simpleType.refine(cls, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testRefineMethodWithSubclass() {
        Class<?> superclass = String.class;
        Class<?> subclass = StringBuffer.class;
        SimpleType simpleType = new SimpleType(superclass);
        JavaType refinedType = simpleType.refine(subclass, TypeBindings.emptyBindings(), null, null);
        assertNotNull(refinedType);
        assertEquals(subclass, refinedType.getRawClass());
    }

    // Test for buildCanonicalName method

    @Test
    public void testBuildCanonicalNameMethodForSimpleType() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        StringBuilder expected = new StringBuilder("java.lang.String");
        assertEquals(expected.toString(), simpleType.buildCanonicalName().toString());
    }

    @Test
    public void testBuildCanonicalNameMethodForListType() {
        Class<?> listClass = List.class;
        Class<?> listElementType = Integer.class;
        SimpleType simpleType = new SimpleType(listClass);
        StringBuilder expected = new StringBuilder("java.util.List<java.lang.Integer>");
        assertEquals(expected.toString(), simpleType.buildCanonicalName().toString());
    }

    // Edge cases and exception handling scenarios test cases

    @Test
    public void testConstructUnsafeWithNullClass() {
        assertNull(SimpleType.constructUnsafe(null));
    }

    @Test(expected = NullPointerException.class)
    public void testConstructWithNullClass() {
        SimpleType.construct(null);
    }

    @Test
    public void testNarrowMethodWithNullSubclass() {
        Class<?> superclass = String.class;
        SimpleType simpleType = new SimpleType(superclass);
        assertNull(simpleType._narrow(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowMethodWithInvalidSubclass() {
        Class<?> superclass = String.class;
        Class<?> invalidSubclass = Object.class;
        SimpleType simpleType = new SimpleType(superclass);
        simpleType._narrow(invalidSubclass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandlerWithNullContentTypeHandler() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        simpleType.withContentTypeHandler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithValueHandlerWithNullValueHandler() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        simpleType.withValueHandler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandlerWithNullContentValueHandler() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        simpleType.withContentValueHandler(null);
    }

}