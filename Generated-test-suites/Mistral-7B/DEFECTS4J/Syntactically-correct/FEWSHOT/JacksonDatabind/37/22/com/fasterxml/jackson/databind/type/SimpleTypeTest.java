package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTypeTest {
    @Test
    public void testSimpleTypeConstruction() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertEquals(0, simpleType.getBindings().size());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        TypeBase base = new TypeBase() {
            @Override
            public JavaType _narrow(Class<?> subclass) {
                return null;
            }
        };
        SimpleType copiedSimpleType = new SimpleType(base);
        assertEquals(simpleType.getClass(), copiedSimpleType.getClass());
        assertEquals(simpleType.getRawClass(), copiedSimpleType.getRawClass());
        assertEquals(simpleType.getBindings(), copiedSimpleType.getBindings());
        assertEquals(simpleType.getSuperClass(), copiedSimpleType.getSuperClass());
        assertArrayEquals(simpleType.getSuperInterfaces(), copiedSimpleType.getSuperInterfaces());
        assertEquals(simpleType.isStatic(), copiedSimpleType.isStatic());
    }

    @Test
    public void testSimpleTypeNarrowing() {
        Class<?> superClass = Object.class;
        Class<?> subClass = String.class;
        SimpleType simpleType = new SimpleType(subClass);
        JavaType narrowedType = simpleType._narrow(superClass);
        assertNotNull(narrowedType);
        assertEquals(subClass, narrowedType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenTryingToConstructMapType() {
        Class<?> mapClass = Map.class;
        new SimpleType(mapClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenTryingToConstructCollectionType() {
        Class<?> collectionClass = Collection.class;
        new SimpleType(collectionClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionWhenTryingToConstructArrayType() {
        Class<?> arrayClass = int[].class;
        new SimpleType(arrayClass);
    }

    @Test
    public void testSimpleTypeToString() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        StringBuilder expected = new StringBuilder("[simple type, class ");
        expected.append(cls.getName());
        expected.append("]");
        assertEquals(expected.toString(), simpleType.toString());
    }

    @Test
    public void testSimpleTypeEquals() {
        Class<?> cls1 = String.class;
        Class<?> cls2 = Integer.class;
        SimpleType simpleType1 = new SimpleType(cls1);
        SimpleType simpleType2 = new SimpleType(cls2);
        assertFalse(simpleType1.equals(simpleType2));

        SimpleType simpleType3 = new SimpleType(cls1);
        assertTrue(simpleType1.equals(simpleType3));

        SimpleType simpleType4 = new SimpleType(simpleType1);
        assertTrue(simpleType1.equals(simpleType4));
    }
}