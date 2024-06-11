package com.example.junit;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        // Test default constructor
        SimpleType simpleType1 = new SimpleType(String.class);
        assertEquals(String.class, simpleType1.getRawClass());
        assertNull(simpleType1.getSuperClass());
        assertNull(simpleType1.getSuperInterfaces());

        // Test constructor with TypeBindings, superClass, and superInterfaces
        SimpleType simpleType2 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertEquals(String.class, simpleType2.getRawClass());
        assertNull(simpleType2.getSuperClass());
        assertNull(simpleType2.getSuperInterfaces());

        // Test copy constructor
        SimpleType simpleType3 = new SimpleType(simpleType1);
        assertEquals(simpleType1.getRawClass(), simpleType3.getRawClass());
        assertSame(simpleType1.getBindings(), simpleType3.getBindings());
        assertSame(simpleType1.getSuperClass(), simpleType3.getSuperClass());
        assertArrayEquals(simpleType1.getSuperInterfaces(), simpleType3.getSuperInterfaces());

        // Test constructor with valueHandler and typeHandler
        SimpleType simpleType4 = new SimpleType(int.class, TypeBindings.emptyBindings(), null, null, null, new IntValueHandler(), new IntTypeHandler());
        assertEquals(int.class, simpleType4.getRawClass());
        assertNull(simpleType4.getSuperClass());
        assertNull(simpleType4.getSuperInterfaces());
        assertNotNull(simpleType4.getValueHandler());
        assertNotNull(simpleType4.getTypeHandler());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String[].class);
        StringBuilder expected = new StringBuilder("[[Ljava.lang.String;]");
        StringBuilder actual = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(List.class);
        StringBuilder expected = new StringBuilder("java.util.List<");
        StringBuilder actual = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());

        simpleType = new SimpleType(Map.class);
        expected = new StringBuilder("java.util.Map<");
        expected.append("? extends K, ");
        expected.append("? extends V>");
        actual = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType1 = new SimpleType(String.class);
        assertFalse(simpleType1.isContainerType());

        SimpleType simpleType2 = new SimpleType(List.class);
        assertTrue(simpleType2.isContainerType());
    }

    @Test
    public void testToString() {
        SimpleType simpleType1 = new SimpleType(String.class);
        String expected = "[simple type, class java.lang.String]";
        String actual = simpleType1.toString();
        assertEquals(expected, actual);

        SimpleType simpleType2 = new SimpleType(List.class);
        expected = "[simple type, class java.util.List]";
        actual = simpleType2.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        SimpleType simpleType3 = new SimpleType(Integer.class);

        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType1 = new SimpleType(Object.class);
        SimpleType simpleType2 = new SimpleType(String.class);

        assertNull(simpleType1.narrow(String.class));
        assertEquals(simpleType2, simpleType2.narrow(String.class));
    }
}

class IntValueHandler {}
class IntTypeHandler {}