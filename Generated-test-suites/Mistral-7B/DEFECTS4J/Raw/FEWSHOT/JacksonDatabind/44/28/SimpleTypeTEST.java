package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        try {
            SimpleType simpleType = SimpleType.construct(cls);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIsContainerType() {
        Class<?> cls = List.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null);
        assertTrue(simpleType.isContainerType());

        cls = String.class;
        simpleType = new SimpleType(cls, null, null, null);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null);
        StringBuilder expected = new StringBuilder("java.lang.String");
        StringBuilder actual = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = List.class;
        TypeVariable<List<?>> typeVariable = cls.getTypeParameters()[0];
        Type[] actualTypes = cls.getGenericInterfaces();
        SimpleType simpleType = new SimpleType(cls, null, null, null);
        StringBuilder expected = new StringBuilder("java.util.List<T>");
        StringBuilder actual = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());

        for (Type actualType : actualTypes) {
            if (actualType instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) actualType).getActualTypeArguments();
                Type actualTypeArgument = actualTypeArguments[0];
                if (actualTypeArgument instanceof TypeVariable) {
                    TypeVariable<?> expectedTypeVariable = (TypeVariable<?>) typeVariable;
                    assertEquals(expectedTypeVariable.getName(), ((TypeVariable<?>) actualTypeArgument).getName());
                }
            }
        }
    }

    @Test
    public void testNarrow() {
        Class<?> superClass = Object.class;
        Class<?> subclass = String.class;
        SimpleType simpleType = new SimpleType(superClass, null, null, null);
        SimpleType narrowedType = simpleType.narrow(subclass);
        assertEquals(subclass, narrowedType.getRawClass());
        assertNotSame(simpleType, narrowedType);
    }

    @Test
    public void testNarrowSameClass() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null);
        SimpleType narrowedType = simpleType.narrow(cls);
        assertEquals(simpleType, narrowedType);
    }

    @Test
    public void testNarrowNull() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, null, null, null);
        assertNull(simpleType.narrow(null));
    }
}