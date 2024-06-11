package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructor() {
        Class<?> cls = String.class;
        SimpleType simpleType1 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType simpleType3 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null, null, null, true);
        assertEquals(cls, simpleType1.getRawClass());
        assertEquals(cls, simpleType2.getRawClass());
        assertEquals(cls, simpleType3.getRawClass());
        assertEquals(false, simpleType1.isStatic());
        assertEquals(false, simpleType2.isStatic());
        assertEquals(true, simpleType3.isStatic());
    }

    @Test
    public void testConstructUnsafe() {
        try {
            SimpleType simpleType = SimpleType.constructUnsafe(String[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testDeprecatedConstruct() {
        SimpleType simpleType = SimpleType.construct(String.class);
    }

    @Test
    public void testNarrow() {
        Class<?> intClass = int.class;
        SimpleType simpleTypeInt = new SimpleType(intClass, TypeBindings.emptyBindings(), null, null);
        Class<?> doubleClass = double.class;
        SimpleType simpleTypeDouble = new SimpleType(doubleClass, TypeBindings.emptyBindings(), null, null);

        SimpleType intTypeNarrowed = simpleTypeInt._narrow(intClass);
        SimpleType intTypeExpected = new SimpleType(intClass, TypeBindings.emptyBindings(), null, null);
        assertEquals(intTypeExpected, intTypeNarrowed);

        SimpleType doubleTypeNarrowed = simpleTypeDouble._narrow(intClass);
        assertNull(doubleTypeNarrowed);

        SimpleType doubleTypeNarrowed2 = simpleTypeDouble._narrow(doubleClass);
        assertEquals(simpleTypeDouble, doubleTypeNarrowed2);
    }

    @Test
    public void testIsContainerType() {
        assertFalse(new SimpleType(String.class, TypeBindings.emptyBindings(), null, null).isContainerType());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> listClass = List.class;
        Class<?> intClass = int.class;
        Class<?> stringClass = String.class;

        SimpleType stringType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        SimpleType listType = new SimpleType(listClass, TypeBindings.emptyBindings(), null, null);
        SimpleType listOfIntType = new SimpleType(List.class, TypeBindings.emptyBindings(), null, new SimpleType[]{new SimpleType(intClass, TypeBindings.emptyBindings(), null, null)});

        assertEquals("java.lang.String", stringType.buildCanonicalName());
        assertEquals("java.util.List", listType.buildCanonicalName());
        assertEquals("java.util.List<java.lang.Integer>", listOfIntType.buildCanonicalName());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> listClass = List.class;
        Class<?> intClass = int.class;
        Class<?> stringClass = String.class;

        SimpleType stringType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        SimpleType listType = new SimpleType(listClass, TypeBindings.emptyBindings(), null, null);
        SimpleType listOfIntType = new SimpleType(List.class, TypeBindings.emptyBindings(), null, new SimpleType[]{new SimpleType(intClass, TypeBindings.emptyBindings(), null, null)});

        StringBuilder stringTypeSignature = new StringBuilder();
        stringType.getErasedSignature(stringTypeSignature);
        assertEquals("java.lang.String", stringTypeSignature.toString());

        StringBuilder listTypeSignature = new StringBuilder();
        listType.getErasedSignature(listTypeSignature);
        assertEquals("java.util.List", listTypeSignature.toString());

        StringBuilder listOfIntTypeSignature = new StringBuilder();
        listOfIntType.getErasedSignature(listOfIntTypeSignature);
        assertEquals("java.util.List", listOfIntTypeSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> listClass = List.class;
        Class<?> intClass = int.class;
        Class<?> stringClass = String.class;

        SimpleType stringType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        SimpleType listType = new SimpleType(listClass, TypeBindings.emptyBindings(), null, null);
        SimpleType listOfIntType = new SimpleType(List.class, TypeBindings.emptyBindings(), null, new SimpleType[]{new SimpleType(intClass, TypeBindings.emptyBindings(), null, null)});

        StringBuilder stringTypeSignature = new StringBuilder();
        stringType.getGenericSignature(stringTypeSignature);
        assertEquals("java.lang.String", stringTypeSignature.toString());

        StringBuilder listTypeSignature = new StringBuilder();
        listType.getGenericSignature(listTypeSignature);
        assertEquals("java.util.List<T>", listTypeSignature.toString());

        StringBuilder listOfIntTypeSignature = new StringBuilder();
        listOfIntType.getGenericSignature(listOfIntTypeSignature);
        assertEquals("java.util.List<java.lang.Integer>", listOfIntTypeSignature.toString());
    }
}