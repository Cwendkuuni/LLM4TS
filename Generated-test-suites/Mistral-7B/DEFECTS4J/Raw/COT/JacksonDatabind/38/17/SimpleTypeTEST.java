package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

import org.junit.Test;

public class SimpleTypeTest {

    // Basic test cases for constructors

    @Test
    public void testSimpleTypeConstructionWithClass() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testSimpleTypeConstructionWithClassBindingsSuperClassSuperInterfaces() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertNotNull(simpleType);
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType original = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        SimpleType copy = new SimpleType(original);
        assertEquals(original, copy);
    }

    @Test
    public void testSimpleTypePassThroughConstructor() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, 0, null, false);
        assertNotNull(simpleType);
    }

    // Test for constructor that should not be used by application code
    @Test(expected = IllegalStateException.class)
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
    }

    // Test for deprecated construct method
    @Test(expected = IllegalStateException.class)
    public void testConstructDeprecated() {
        SimpleType simpleType = SimpleType.construct(String.class);
    }

    // Test for _narrow method
    @Test
    public void testNarrow() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        SimpleType narrowedType = simpleType.withContentType(String[].class);
        assertNotSame(simpleType, narrowedType);
        assertFalse(simpleType.isInstance(new Object[] { "foo" }));
        assertTrue(narrowedType.isInstance(new Object[] { "foo", "bar" }));
    }

    // Test for methods that should not be called

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        new SimpleType(String.class).withContentType(String[].class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithTypeHandler() {
        new SimpleType(String.class).withTypeHandler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        new SimpleType(String.class).withContentTypeHandler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithValueHandler() {
        new SimpleType(String.class).withValueHandler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        new SimpleType(String.class).withContentValueHandler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithStaticTyping() {
        new SimpleType(String.class).withStaticTyping();
    }

    @Test(expected = NullPointerException.class)
    public void testRefineNullType() {
        new SimpleType(String.class).refine(null, TypeBindings.emptyBindings(), null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRefineWithInvalidSuperClass() {
        SimpleType stringType = new SimpleType(String.class);
        JavaType listType = new SimpleType(ArrayList.class);
        JavaType refinedType = stringType.refine(ArrayList.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    // Test for equals method
    @Test
    public void testEquals() {
        Class<String> stringClass = String.class;
        SimpleType simpleType1 = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType3 = new SimpleType(Integer.class, TypeBindings.emptyBindings(), null, null);
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }

    // Test for buildCanonicalName method
    @Test
    public void testBuildCanonicalName() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        String canonicalName = simpleType.buildCanonicalName();
        assertEquals("java.lang.String", canonicalName);

        JavaType listType = new SimpleType(ArrayList.class);
        String listCanonicalName = listType.buildCanonicalName();
        assertEquals("java.util.ArrayList", listCanonicalName);

        JavaType listOfStringsType = new SimpleType(ArrayList.class, TypeBindings.emptyBindings(), String.class, null);
        String listOfStringsCanonicalName = listOfStringsType.buildCanonicalName();
        assertEquals("java.util.ArrayList<java.lang.String>", listOfStringsCanonicalName);
    }

    // Test for getErasedSignature and getGenericSignature methods
    @Test
    public void testGetErasedSignatureAndGetGenericSignature() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("java.lang.String", sb.toString());

        simpleType.getGenericSignature(sb);
        assertEquals("java.lang.String", sb.toString());

        JavaType listType = new SimpleType(ArrayList.class);
        sb.setLength(0);
        listType.getErasedSignature(sb);
        assertEquals("java.util.List", sb.toString());

        sb.setLength(0);
        listType.getGenericSignature(sb);
        assertEquals("java.util.List<E>", sb.toString());

        JavaType listOfStringsType = new SimpleType(ArrayList.class, TypeBindings.emptyBindings(), String.class, null);
        sb.setLength(0);
        listOfStringsType.getErasedSignature(sb);
        assertEquals("java.util.List", sb.toString());

        sb.setLength(0);
        listOfStringsType.getGenericSignature(sb);
        assertEquals("java.util.List<java.lang.String>", sb.toString());
    }

    // Test for isContainerType method
    @Test
    public void testIsContainerType() {
        assertFalse(new SimpleType(String.class).isContainerType());
    }
}