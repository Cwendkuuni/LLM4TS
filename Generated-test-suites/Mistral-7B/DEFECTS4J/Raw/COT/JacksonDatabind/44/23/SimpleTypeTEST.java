package com.fasterxml.jackson.databind.type; // make sure the package is correct

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertEquals(cls, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        Class<?> cls = HashMap.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        Class<?> cls = ArrayList.class;
        SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        Class<?> cls = String[].class;
        SimpleType.construct(cls);
    }

    @Test
    public void testNarrow() {
        Class<?> baseClass = Object.class;
        Class<?> subClass = String.class;
        SimpleType baseType = SimpleType.construct(baseClass);
        SimpleType subType = new SimpleType(subClass, TypeBindings.emptyBindings(), baseType, null);
        SimpleType narrowedType = baseType._narrow(subClass);
        assertEquals(subType, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithUnrelatedClass() {
        Class<?> baseClass = Object.class;
        Class<?> unrelatedClass = Integer.class;
        SimpleType baseType = SimpleType.construct(baseClass);
        SimpleType narrowedType = baseType._narrow(unrelatedClass);
    }

    @Test
    public void testWithContentType() {
        // Since SimpleType has no content type, this test should fail
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        try {
            simpleType.withContentType(TypeFactory.unknownType());
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        Object handler = new Object();
        SimpleType withHandler = simpleType.withTypeHandler(handler);
        assertEquals(handler, withHandler.getTypeHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.construct(cls);
        SimpleType staticType = simpleType.withStaticTyping();
        assertFalse(simpleType.isStatic());
        assertTrue(staticType.isStatic());
    }

    
}