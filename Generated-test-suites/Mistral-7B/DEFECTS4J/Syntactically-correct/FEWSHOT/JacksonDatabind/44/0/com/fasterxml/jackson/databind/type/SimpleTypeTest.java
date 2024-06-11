package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        // Test default construction
        SimpleType simpleType1 = new SimpleType(String.class);
        assertNotNull(simpleType1);
        assertEquals(String.class, simpleType1.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType1.getBindings());
        assertNull(simpleType1.getSuperclass());
        assertNull(simpleType1.getSuperInterfaces());

        // Test copy constructor
        SimpleType simpleType2 = new SimpleType(simpleType1);
        assertEquals(simpleType1, simpleType2);

        // Test construction with bindings
        SimpleType simpleType3 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertEquals(simpleType1, simpleType3);

        // Test construction with superclass and bindings
        SimpleType simpleType4 = new SimpleType(String.class, TypeBindings.emptyBindings(), List.class, null);
        assertFalse(simpleType1.equals(simpleType4));
        assertEquals(String.class, simpleType4.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType4.getBindings());
        assertEquals(List.class, simpleType4.getSuperclass());
        assertNull(simpleType4.getSuperInterfaces());

        // Test construction with superclass, bindings, and superinterfaces
        SimpleType simpleType5 = new SimpleType(String.class, TypeBindings.emptyBindings(), List.class, new JavaType[] { Map.class });
        assertFalse(simpleType1.equals(simpleType5));
        assertEquals(String.class, simpleType5.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType5.getBindings());
        assertEquals(List.class, simpleType5.getSuperclass());
        assertArrayEquals(new JavaType[] { Map.class }, simpleType5.getSuperInterfaces());

        // Test construction with value handler and type handler
        SimpleType simpleType6 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, new Object(), new Object());
        assertFalse(simpleType1.equals(simpleType6));
        assertEquals(String.class, simpleType6.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType6.getBindings());
        assertNull(simpleType6.getSuperclass());
        assertNull(simpleType6.getSuperInterfaces());
        assertEquals(simpleType6.getValueHandler(), simpleType6.getValueHandler());
        assertEquals(simpleType6.getTypeHandler(), simpleType6.getTypeHandler());

        // Test construction with static typing
        SimpleType simpleType7 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, true);
        assertFalse(simpleType1.equals(simpleType7));
        assertEquals(String.class, simpleType7.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType7.getBindings());
        assertNull(simpleType7.getSuperclass());
        assertNull(simpleType7.getSuperInterfaces());
        assertFalse(simpleType7.isStatic());

        // Test constructUnsafe method
        SimpleType simpleType8 = SimpleType.constructUnsafe(String.class);
        assertFalse(simpleType1.equals(simpleType8));
        assertEquals(String.class, simpleType8.getRawClass());
        assertNull(simpleType8.getBindings());
        assertNull(simpleType8.getSuperclass());
        assertNull(simpleType8.getSuperInterfaces());
        assertFalse(simpleType8.isStatic());

        // Test deprecated construct method
        SimpleType simpleType9 = SimpleType.construct(String.class);
        assertFalse(simpleType1.equals(simpleType9));
        assertEquals(String.class, simpleType9.getRawClass());
        assertNull(simpleType9.getBindings());
        assertNull(simpleType9.getSuperclass());
        assertNull(simpleType9.getSuperInterfaces());
        assertFalse(simpleType9.isStatic());
    }

    @Test
    public void testNarrowing() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(Number.class);

        JavaType narrowedType1 = simpleType1.narrow(String[].class);
        JavaType narrowedType2 = simpleType1.narrow(String[].class);
        JavaType narrowedType3 = simpleType1.narrow(Number.class);
        JavaType narrowedType4 = simpleType1.narrow(String[].class);
        JavaType narrowedType5 = simpleType2.narrow(String.class);
        JavaType narrowedType6 = simpleType2.narrow(Number[].class);

        assertNotNull(narrowedType1);
        assertNotNull(narrowedType2);
        assertNull(narrowedType3);
        assertNull(narrowedType4);
        assertNotNull(narrowedType5);
        assertNotNull(narrowedType6);

        assertEquals(String[].class, narrowedType1.getRawClass());
        assertEquals(String[].class, narrowedType2.getRawClass());
        assertEquals(Number.class, narrowedType3.getRawClass());
        assertEquals(String[].class, narrowedType4.getRawClass());
        assertEquals(String.class, narrowedType5.getRawClass());
        assertEquals(Number[].class, narrowedType6.getRawClass());
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType contentType = new SimpleType(Integer.class);

        try {
            simpleType.withContentType(contentType);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object typeHandler = new Object();

        SimpleType simpleTypeWithTypeHandler = simpleType.withTypeHandler(typeHandler);
        assertEquals(simpleType, simpleTypeWithTypeHandler);
        assertEquals(simpleType.getTypeHandler(), typeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object contentTypeHandler = new Object();

        try {
            simpleType.withContentTypeHandler(contentTypeHandler);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object valueHandler = new Object();

        SimpleType simpleTypeWithValueHandler = simpleType.withValueHandler(valueHandler);
        assertEquals(simpleType, simpleTypeWithValueHandler);
        assertEquals(simpleType.getValueHandler(), valueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        Object contentValueHandler = new Object();

        try {
            simpleType.withContentValueHandler(contentValueHandler);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType simpleTypeWithStaticTyping = simpleType.withStaticTyping();
        assertTrue(simpleTypeWithStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        Class<?> rawType = Integer.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = List.class;
        JavaType[] superInterfaces = new JavaType[] { Map.class };

        JavaType refinedType = simpleType.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(refinedType);
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(String[].class);

        String canonicalName = simpleType.buildCanonicalName();
        assertEquals("[Ljava.lang.String;", canonicalName);
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(List.class);

        StringBuilder expected = new StringBuilder("java.util.List");
        StringBuilder actual = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(List.class);

        StringBuilder expected = new StringBuilder("<Ljava.lang.Object;>");
        StringBuilder actual = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());

        expected.setLength(0);
        expected.append("<Ljava.util.List<Ljava.lang.String;>;>");
        simpleType = new SimpleType(List.class, TypeBindings.emptyBindings(), String.class);
        actual = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    private JavaType containedType(int index, JavaType[] types) {
        if (index >= 0 && index < types.length) {
            return types[index];
        } else {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
    }

    private void assertArrayEquals(JavaType[] expected, JavaType[] actual) {
        if (expected.length != actual.length) {
            fail("Array lengths do not match: expected=" + expected.length + ", actual=" + actual.length);
        }

        for (int i = 0; i < expected.length; i++) {
            assertEquals("Unexpected type at index " + i, expected[i], actual[i]);
        }
    }
}