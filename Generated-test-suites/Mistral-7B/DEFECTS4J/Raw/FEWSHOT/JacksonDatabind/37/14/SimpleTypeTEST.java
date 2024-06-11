package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMap() {
        SimpleType simpleType = SimpleType.construct(HashMap.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructCollection() {
        SimpleType simpleType = SimpleType.construct(ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructArray() {
        SimpleType simpleType = SimpleType.construct(int[].class);
    }

    @Test
    public void testNarrow() {
        SimpleType stringType = new SimpleType(String.class);
        SimpleType stringWithIntBinding = new SimpleType(String.class, new TypeBindings() {{
            put(0, new SimpleType(Integer.TYPE));
        }}, null, null);
        SimpleType narrowedType = stringType._narrow(StringWithIntBinding.class);
        assertEquals(stringWithIntBinding, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowInvalidSubclass() {
        SimpleType stringType = new SimpleType(String.class);
        SimpleType integerType = new SimpleType(Integer.TYPE);
        stringType._narrow(integerType.getClass());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType stringType = new SimpleType(String.class);
        StringBuilder expected = new StringBuilder("java.lang.String");
        StringBuilder actual = stringType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType stringType = new SimpleType(String.class);
        StringBuilder expected = new StringBuilder("java.lang.String<T>");
        StringBuilder actual = stringType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType stringType = new SimpleType(String.class);
        StringBuilder expected = new StringBuilder("java.lang.String");
        StringBuilder actual = stringType.buildCanonicalName();
        assertEquals(expected.toString(), actual.toString());

        SimpleType listStringType = new SimpleType(ArrayList.class, null, null, new SimpleType(String.class));
        expected = new StringBuilder("java.util.ArrayList<java.lang.String>");
        actual = listStringType.buildCanonicalName();
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testEquals() {
        SimpleType stringType1 = new SimpleType(String.class);
        SimpleType stringType2 = new SimpleType(String.class);
        assertTrue(stringType1.equals(stringType2));

        SimpleType stringType3 = new SimpleType(Object.class);
        assertFalse(stringType1.equals(stringType3));

        SimpleType stringWithIntBindingType1 = new SimpleType(String.class, new TypeBindings() {{
            put(0, new SimpleType(Integer.TYPE));
        }}, null, null);
        SimpleType stringWithIntBindingType2 = new SimpleType(String.class, new TypeBindings() {{
            put(0, new SimpleType(Integer.TYPE));
        }}, null, null);
        assertTrue(stringWithIntBindingType1.equals(stringWithIntBindingType2));
    }
}