// SimpleTypeTest1.java
package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

/**
 * Test cases for SimpleType constructor with Class.
 */
public class SimpleTypeTest1 {

    @Test
    public void testSimpleTypeConstructorWithClass() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        JavaType javaType = new SimpleType(cls).withoutValueHandler(null);
        assertEquals(javaType.getRawClass(), cls);
    }

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        JavaType javaType = simpleType.withoutValueHandler(null);
        assertEquals(javaType.getRawClass(), cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        Class<?> cls = ArrayList.class;
        SimpleType simpleType = new SimpleType(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        Class<?> cls = HashMap.class;
        SimpleType simpleType = new SimpleType(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        Class<?> cls = String[].class;
        SimpleType simpleType = new SimpleType(cls);
    }
}