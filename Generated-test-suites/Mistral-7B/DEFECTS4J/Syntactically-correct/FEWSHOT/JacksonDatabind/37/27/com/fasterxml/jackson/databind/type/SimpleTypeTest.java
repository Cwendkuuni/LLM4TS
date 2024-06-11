package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType type = SimpleType.constructUnsafe(cls);
        assertNotNull(type);
        assertEquals(cls, type.getType());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertFalse(type.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithArray() {
        Class<?> cls = String[].class;
        SimpleType type = SimpleType.constructUnsafe(cls);
        fail("Should not reach here");
    }

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        SimpleType type = SimpleType.construct(cls);
        assertNotNull(type);
        assertEquals(cls, type.getType());
        assertNull(type.getSuperClass());
        assertNull(type.getSuperInterfaces());
        assertFalse(type.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        Class<?> cls = HashMap.class;
        SimpleType type = SimpleType.construct(cls);
        fail("Should not reach here");
    }

    @Test
    public void testNarrow() {
        Class<?> superClass = Object.class;
        Class<?> subClass = String.class;
        SimpleType superType = SimpleType.construct(superClass);
        SimpleType subType = SimpleType.construct(subClass);
        SimpleType narrowedType = subType._narrow(superClass);
        assertNotNull(narrowedType);
        assertEquals(subClass, narrowedType.getType());
        assertSame(superType, narrowedType._superClass);
        assertArrayEquals(new Class[0], narrowedType._superInterfaces);
        assertFalse(narrowedType.isContainerType());
    }

    @Test
    public void testNarrowWithSameType() {
        Class<?> cls = String.class;
        SimpleType type = SimpleType.construct(cls);
        SimpleType narrowedType = type._narrow(cls);
        assertSame(type, narrowedType);
    }

    @Test
    public void testNarrowWithDifferentType() {
        Class<?> superClass = Object.class;
        Class<?> subClass = String.class;
        SimpleType superType = SimpleType.construct(superClass);
        SimpleType subType = SimpleType.construct(subClass);
        SimpleType narrowedType = subType._narrow(int.class);
        assertNotNull(narrowedType);
        assertFalse(narrowedType.isInstanceOf(subClass));
        assertNull(narrowedType.getType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        SimpleType type = SimpleType.construct(cls);
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        String expected = "java.lang.String";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = List.class;
        Type[] typeArguments = new Type[] { String.class, Integer.class };
        JavaType javaType = TypeFactory.defaultInstance().constructType(List.class, typeArguments);
        SimpleType simpleType = (SimpleType) javaType;
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        String expected = "java.util.List<java.lang.String,java.lang.Integer>";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testToString() {
        Class<?> cls = String.class;
        SimpleType type = SimpleType.construct(cls);
        String expected = "[simple type, class " + cls.getName() + "]";
        assertEquals(expected, type.toString());
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = String.class;
        Class<?> cls2 = Integer.class;
        SimpleType type1 = SimpleType.construct(cls1);
        SimpleType type2 = SimpleType.construct(cls2);
        assertFalse(type1.equals(type2));

        SimpleType type3 = SimpleType.construct(cls1);
        assertTrue(type1.equals(type3));
    }
}