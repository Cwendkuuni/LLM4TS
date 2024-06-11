package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<String> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertNotNull(simpleType);
        assertEquals(cls, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithArray() {
        Class<String[]> cls = String[].class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithMap() {
        Class<HashMap<String, Integer>> cls = HashMap.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithCollection() {
        Class<ArrayList<String>> cls = ArrayList.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
    }

    @Test
    public void testConstruct() {
        Class<String> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        assertNotNull(simpleType);
        assertEquals(cls, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        Class<String[]> cls = String[].class;
        SimpleType simpleType = SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        Class<HashMap<String, Integer>> cls = HashMap.class;
        SimpleType simpleType = SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        Class<ArrayList<String>> cls = ArrayList.class;
        SimpleType simpleType = SimpleType.construct(cls);
    }

    @Test
    public void testGetCanonicalName() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertEquals("java.lang.String", simpleType.getCanonicalName());
    }

    @Test
    public void testIsContainerType() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testToString() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertEquals("[simple type, class [Ljava.lang.String;]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        Class<String> cls = String.class;
        SimpleType simpleType1 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertTrue(simpleType1.equals(simpleType2));
    }

    @Test
    public void testNotEquals() {
        Class<String> cls = String.class;
        SimpleType simpleType1 = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        Object obj = new Object();
        assertFalse(simpleType1.equals(obj));
    }
}