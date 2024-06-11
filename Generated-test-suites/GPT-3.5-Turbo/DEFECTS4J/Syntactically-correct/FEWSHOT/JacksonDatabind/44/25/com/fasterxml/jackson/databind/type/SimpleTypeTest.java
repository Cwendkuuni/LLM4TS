package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {
    @Test
    public void testConstructor() {
        SimpleType simpleType = new SimpleType(Integer.class);
        assertEquals(Integer.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isContainerType());
        assertTrue(simpleType.isConcrete());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertNull(simpleType.refine(String.class, null, null, null));
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        assertTrue(simpleType1.equals(simpleType2));

        SimpleType simpleType3 = new SimpleType(Integer.class);
        assertFalse(simpleType1.equals(simpleType3));
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(Double.class);
        assertEquals("[simple type, class java.lang.Double]", simpleType.toString());
    }
}