package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        // Test default construction
        SimpleType simpleType1 = new SimpleType(String.class);
        assertNotNull(simpleType1);
        assertEquals(String.class, simpleType1.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType1.getBindings());
        assertNull(simpleType1.getSuperClass());
        assertNull(simpleType1.getSuperInterfaces());
        assertFalse(simpleType1.isStatic());

        // Test construction with bindings, superClass and superInterfaces
        TypeBindings bindings = TypeBindings.builder()
                .appendBinding(int[].class, new JavaType(int[].class))
                .appendBinding(java.util.List.class, new JavaType(java.util.ArrayList.class))
                .build();
        SimpleType simpleType2 = new SimpleType(String.class, bindings,
                new JavaType(Number.class), new JavaType[0], null, null, false);
        assertEquals(String.class, simpleType2.getRawClass());
        assertEquals(bindings, simpleType2.getBindings());
        assertEquals(Number.class, simpleType2.getSuperClass());
        assertArrayEquals(new JavaType[0], simpleType2.getSuperInterfaces());
        assertFalse(simpleType2.isStatic());

        // Test copy constructor
        SimpleType simpleType3 = new SimpleType(simpleType1);
        assertEquals(simpleType1, simpleType3);

        // Test pass-through constructor used by ReferenceType
        SimpleType simpleType4 = new SimpleType(simpleType2);
        assertEquals(simpleType2, simpleType4);

        // Test construction using static factory method
        SimpleType simpleType5 = SimpleType.construct(String.class);
        assertEquals(String.class, simpleType5.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType5.getBindings());
        assertNull(simpleType5.getSuperClass());
        assertNull(simpleType5.getSuperInterfaces());
        assertFalse(simpleType5.isStatic());
    }

    @Test
    public void testNarrowing() {
        SimpleType stringType = new SimpleType(String.class);
        SimpleType stringSubclassType = new SimpleType(StringSubclass.class);

        SimpleType narrowedType1 = stringType.narrow(StringSubclass.class);
        assertEquals(stringSubclassType, narrowedType1);

        SimpleType narrowedType2 = stringType.narrow(Object.class);
        assertNull(narrowedType2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMapClass() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollectionClass() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArrayClass() {
        SimpleType.construct(int[].class);
    }

    @Test
    public void testHashCode() {
        SimpleType stringType1 = new SimpleType(String.class);
        SimpleType stringType2 = new SimpleType(String.class);
        SimpleType intType = new SimpleType(int.class);

        assertEquals(systemUnderTest.hashCode(), stringType1.hashCode());
        assertEquals(stringType1.hashCode(), stringType2.hashCode());
        assertNotEquals(stringType1.hashCode(), intType.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleType stringType1 = new SimpleType(String.class);
        SimpleType stringType2 = new SimpleType(String.class);
        SimpleType intType = new SimpleType(int.class);
        SimpleType stringType3 = new SimpleType(String.class);

        assertTrue(stringType1.equals(stringType1));
        assertTrue(stringType1.equals(stringType2));
        assertFalse(stringType1.equals(intType));
        assertTrue(stringType1.equals(stringType3));
    }

    @Test
    public void testToString() {
        SimpleType stringType = new SimpleType(String.class);
        assertEquals("[simple type, class java.lang.String]", stringType.toString());

        SimpleType listType = new SimpleType(ArrayList.class);
        assertEquals("[simple type, class java.util.ArrayList]", listType.toString());
    }

    @Test
    public void testCanonicalName() {
        SimpleType stringType = new SimpleType(String.class);
        assertEquals("java.lang.String", stringType.buildCanonicalName());

        SimpleType listType = new SimpleType(ArrayList.class);
        assertEquals("java.util.ArrayList<java.lang.Object>", listType.buildCanonicalName());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType stringType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        stringType.getErasedSignature(sb);
        assertEquals("java.lang.String", sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType listType = new SimpleType(ArrayList.class);
        StringBuilder sb = new StringBuilder();
        listType.getGenericSignature(sb);
        assertEquals("java.util.ArrayList<java.lang.Object>", sb.toString());
    }

    private static JavaType containedType(int index, SimpleType type) {
        if (type.getBindings().size() <= index) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        return type.getBindings().get(index);
    }

    private static JavaType[] containedTypes(SimpleType type) {
        JavaType[] result = new JavaType[type.getBindings().size()];
        for (int i = 0; i < type.getBindings().size(); i++) {
            result[i] = containedType(i, type);
        }
        return result;
    }

    private static void assertArrayEquals(JavaType[] expected, JavaType[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }

    private static class StringSubclass {
        // Empty class for testing narrowing
    }
}