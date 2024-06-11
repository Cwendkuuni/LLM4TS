package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        StringBuilder expected = new StringBuilder("java.lang.String");
        StringBuilder actual = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String[].class, TypeBindings.emptyBindings(), null, null);
        StringBuilder expected = new StringBuilder("java.lang.String[]");
        StringBuilder actual = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());

        SimpleType genericType = new SimpleType(List.class, TypeBindings.emptyBindings(), null, null);
        expected.setLength(0);
        expected.append("java.util.List");
        expected.append('<');
        expected.append("java.lang.String");
        expected.append('>');
        actual = genericType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        String expected = "java.lang.String";
        String actual = simpleType.buildCanonicalName();
        assertEquals(expected, actual);

        SimpleType arrayType = new SimpleType(int[].class, TypeBindings.emptyBindings(), null, null);
        expected = "int[]";
        actual = arrayType.buildCanonicalName();
        assertEquals(expected, actual);

        SimpleType genericType = new SimpleType(List.class, TypeBindings.emptyBindings(), null, null);
        expected = "java.util.List<java.lang.String>";
        actual = genericType.buildCanonicalName();
        assertEquals(expected, actual);

        SimpleType genericTypeWithTwoTypes = new SimpleType(Map.class, TypeBindings.bind("key", String.class), null, null);
        Type type = genericTypeWithTwoTypes.containedType(0);
        expected = "java.util.Map<java.lang.String,?";
        actual = genericTypeWithTwoTypes.buildCanonicalName();
        assertEquals(expected, actual.substring(0, expected.length() - 1));
        String keyCanonicalName = type.buildCanonicalName();
        expected = "java.lang.String";
        assertEquals(expected, keyCanonicalName);
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        StringBuilder expected = new StringBuilder("[simple type, class java.lang.String]");
        StringBuilder actual = new StringBuilder();
        actual = simpleType.toString();
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertTrue(simpleType1.equals(simpleType2));

        SimpleType simpleType3 = new SimpleType(int.class, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType1.equals(simpleType3));

        SimpleType simpleType4 = new SimpleType(String[].class, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType1.equals(simpleType4));
    }
}