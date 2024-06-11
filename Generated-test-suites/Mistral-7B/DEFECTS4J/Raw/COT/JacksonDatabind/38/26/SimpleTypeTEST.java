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
    public void testSimpleTypeCopyConstructor() {
        SimpleType original = new SimpleType(String.class);
        SimpleType copy = new SimpleType(original);
        assertNotSame(original, copy);
        assertEquals(original.getClass(), copy.getClass());
        assertEquals(original.getTypeId(), copy.getTypeId());
    }

    @Test
    public void testSimpleTypePassThroughConstructor() {
        SimpleType simpleType = new SimpleType(Integer.class, TypeBindings.emptyBindings(), null, null, 0, null, false);
        assertNotNull(simpleType);
    }

    @Test
    public void testSimpleTypeUnsafeConstructor() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructWithMap() {
        SimpleType.construct(HashMap.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructWithCollection() {
        SimpleType.construct(ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructWithArray() {
        SimpleType.construct(int[].class);
    }

    // Test methods for isContainerType()

    @Test
    public void testIsContainerTypeFalse() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    // Test methods for buildCanonicalName()

    @Test
    public void testBuildCanonicalNameForSimpleType() {
        SimpleType simpleType = new SimpleType(String.class);
        String canonicalName = simpleType.buildCanonicalName();
        assertEquals("java.lang.String", canonicalName);
    }

    @Test
    public void testBuildCanonicalNameForSimpleTypeWithGenericTypes() {
        SimpleType simpleType = new SimpleType(List.class, TypeBindings.emptyBindings(), null, Integer.TYPE);
        String canonicalName = simpleType.buildCanonicalName();
        assertEquals("java.util.List<java.lang.Integer>", canonicalName);
    }

    // Test methods for getErasedSignature() and getGenericSignature()

    @Test
    public void testGetErasedSignatureForSimpleType() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignatureForSimpleType() {
        SimpleType simpleType = new SimpleType(List.class, TypeBindings.emptyBindings(), null, Integer.TYPE);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("Ljava/util/List;<Ljava/lang/Integer;>", sb.toString());
    }

    // Test methods for equals()

    @Test
    public void testEqualsSameInstance() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = simpleType1;
        assertTrue(simpleType1.equals(simpleType2));
    }

    @Test
    public void testEqualsDifferentInstances() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        assertFalse(simpleType1.equals(simpleType2));
    }

    @Test
    public void testEqualsDifferentTypes() {
        SimpleType simpleType1 = new SimpleType(String.class);
        Object obj = new Object();
        assertFalse(simpleType1.equals(obj));
    }

    // Edge cases and exception handling tests

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithSubclassThatIsNotSubtype() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType subclass = new SimpleType(Integer.class);
        simpleType.narrow(subclass);
    }

    @Test
    public void testNarrowWithSameType() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType sameType = new SimpleType(String.class);
        JavaType narrowedType = simpleType.narrow(sameType);
        assertEquals(simpleType, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithNullType() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.narrow(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullClass() {
        SimpleType.construct(null);
    }
}