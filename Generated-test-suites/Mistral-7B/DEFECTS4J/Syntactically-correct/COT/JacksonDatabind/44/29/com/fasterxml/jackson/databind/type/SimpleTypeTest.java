package com.fasterxml.jackson.databind.type; // import statements can be added if necessary

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructor() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = new SimpleType(simpleClass);
        assertEquals(simpleClass, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testSimpleTypeConstructorWithBindings() {
        Class<?> simpleClass = String.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        SimpleType simpleType = new SimpleType(simpleClass, bindings);
        assertEquals(simpleClass, simpleType.getRawClass());
        assertNotNull(simpleType.getSuperClass());
        assertArrayEquals(new JavaType[0], simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = new SimpleType(simpleClass);
        SimpleType copiedSimpleType = new SimpleType(simpleType);
        assertEquals(simpleType.getRawClass(), copiedSimpleType.getRawClass());
        assertEquals(simpleType.getSuperClass(), copiedSimpleType.getSuperClass());
        assertArrayEquals(simpleType.getSuperInterfaces(), copiedSimpleType.getSuperInterfaces());
        assertEquals(simpleType.isContainerType(), copiedSimpleType.isContainerType());
    }

    @Test
    public void testConstructUnsafe() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(simpleClass);
        assertEquals(simpleClass, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertArrayEquals(new JavaType[0], simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructDeprecated() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = SimpleType.construct(simpleClass);
    }

    @Test
    public void testNarrowMethod() {
        Class<?> superClass = Object.class;
        Class<?> subclass = String.class;
        JavaType superType = new SimpleType(superClass);
        SimpleType subType = new SimpleType(subclass);
        JavaType narrowedType = subType._narrow(superType.getType());
        assertEquals(subType, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowInvalidSubclass() {
        Class<?> superClass = Object.class;
        Class<?> subclass = Integer.class;
        JavaType superType = new SimpleType(superClass);
        SimpleType subType = new SimpleType(subclass);
        subType._narrow(superClass);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> simpleClass = List.class;
        JavaType javaType = new SimpleType(simpleClass);
        assertEquals("[java.util.List, interface java.util.List<E>]", javaType.buildCanonicalName());
    }

    @Test
    public void testEquals() {
        Class<?> simpleClass1 = String.class;
        Class<?> simpleClass2 = Integer.class;
        SimpleType simpleType1 = new SimpleType(simpleClass1);
        SimpleType simpleType2 = new SimpleType(simpleClass2);
        assertFalse(simpleType1.equals(simpleType2));

        SimpleType simpleType3 = new SimpleType(simpleClass1);
        assertTrue(simpleType1.equals(simpleType3));
    }

    @Test
    public void testToString() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = new SimpleType(simpleClass);
        StringBuilder expected = new StringBuilder("[simple type, class [Ljava.lang.String;]");
        assertEquals(expected.toString(), simpleType.toString());
    }
}