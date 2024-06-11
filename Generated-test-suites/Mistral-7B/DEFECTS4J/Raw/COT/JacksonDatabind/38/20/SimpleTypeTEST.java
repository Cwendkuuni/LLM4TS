package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory; // if needed

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructorWithClass() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertTrue(simpleType.isContainerType()); // it should return false
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructStaticMethodWithCollection() {
        Class<?> cls = ArrayList.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructStaticMethodWithMap() {
        Class<?> cls = HashMap.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructStaticMethodWithArray() {
        Class<?> cls = int[].class;
        SimpleType.construct(cls);
    }

    @Test
    public void testNarrowMethod() {
        Class<?> superClass = Object.class;
        Class<?> subclass = String.class;
        SimpleType simpleType = new SimpleType(subclass);
        JavaType narrowType = simpleType.narrow(superClass);
        assertNotNull(narrowType);
        assertEquals(subclass, narrowType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowMethodWithInvalidSubclass() {
        Class<?> superClass = Object.class;
        Class<?> subclass = ArrayList.class;
        SimpleType simpleType = new SimpleType(subclass);
        simpleType.narrow(superClass);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = new ArrayList<>().getClass();
        SimpleType simpleType = new SimpleType(cls);
        String canonicalName = simpleType.buildCanonicalName();
        assertTrue(canonicalName.contains(ArrayList.class.getName()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentType(JavaType.construct(int.class));
    }

    @Test
    public void testEqualsMethod() {
        Class<?> cls1 = Integer.class;
        Class<?> cls2 = Integer.class;
        SimpleType simpleType1 = new SimpleType(cls1);
        SimpleType simpleType2 = new SimpleType(cls2);
        TypeBindings bindings = new TypeBindings();
        bindings.putBindings(Integer.class, new JavaType[] {Integer.TYPE});
        SimpleType simpleType3 = new SimpleType(cls1, bindings);
        SimpleType simpleType4 = new SimpleType(cls2, bindings);
        SimpleType simpleType5 = new SimpleType(cls1);
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
        assertFalse(simpleType1.equals(simpleType4));
        assertFalse(simpleType1.equals(simpleType5));
        assertFalse(simpleType1.equals(null));
    }
}