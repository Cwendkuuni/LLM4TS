package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> rawType = String.class;
        SimpleType type = SimpleType.constructUnsafe(rawType);
        assertEquals(String.class, type.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), type.getBindings());
    }

    @Test
    public void testConstruct() {
        Class<?> rawType = String.class;
        SimpleType type = SimpleType.construct(rawType);
        assertEquals(String.class, type.getRawClass());
        assertNotNull(type.getBindings());
        assertEquals(TypeBindings.emptyBindings(), type.getBindings());
    }

    @Test
    public void testBasicConstruction() {
        Class<?> rawType = String.class;
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        SimpleType type = new SimpleType(rawType, TypeBindings.emptyBindings(), superClass, new JavaType[0]);
        assertEquals(String.class, type.getRawClass());
        assertNotNull(type.getBindings());
        assertEquals(TypeBindings.emptyBindings(), type.getBindings());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidType() {
        Class<?> rawType = new ArrayList<Integer>().getClass();
        SimpleType type = SimpleType.construct(rawType);
        fail("Should have thrown IllegalArgumentException");
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> rawType = String.class;
        SimpleType type = new SimpleType(rawType, TypeBindings.emptyBindings(), null, null);
        StringBuilder expected = new StringBuilder("java.lang.String");
        StringBuilder actual = type.getErasedSignature(new StringBuilder());
        assertEquals("Erased signatures should be equal", expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> rawType = String.class;
        SimpleType type = new SimpleType(rawType, TypeBindings.emptyBindings(), null, null);
        StringBuilder expected = new StringBuilder("java.lang.String");
        StringBuilder actual = type.getGenericSignature(new StringBuilder());
        assertEquals("Generic signatures should be equal", expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        Class<?> rawType = String.class;
        SimpleType type = new SimpleType(rawType, TypeBindings.emptyBindings(), null, null);
        String expected = "[simple type, class java.lang.String]";
        String actual = type.toString();
        assertEquals("ToString should be equal", expected, actual);
    }

    @Test
    public void testEquals() {
        Class<?> rawType1 = String.class;
        Class<?> rawType2 = Integer.class;
        SimpleType type1 = new SimpleType(rawType1, TypeBindings.emptyBindings(), null, null);
        SimpleType type2 = new SimpleType(rawType2, TypeBindings.emptyBindings(), null, null);
        assertFalse("Should not be equal", type1.equals(type2));

        SimpleType type3 = new SimpleType(rawType1, TypeBindings.emptyBindings(), null, null);
        assertTrue("Should be equal", type1.equals(type3));
    }
}