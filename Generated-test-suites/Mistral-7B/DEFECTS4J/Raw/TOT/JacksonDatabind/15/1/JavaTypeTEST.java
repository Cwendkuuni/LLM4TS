package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class JavaTypeTest {

    @Test
    public void testJavaTypeConstruction() {
        Class<String> stringClass = String.class;
        JavaType stringType = TypeFactory.defaultInstance().constructType(stringClass);

        assertEquals(String.class, stringType.getRawClass());
        assertTrue(stringType.isConcrete());
        assertFalse(stringType.isAbstract());
        assertFalse(stringType.isArrayType());
        assertFalse(stringType.isEnumType());
        assertFalse(stringType.isInterface());
        assertFalse(stringType.isPrimitive());
        assertFalse(stringType.isFinal());
        assertTrue(stringType.isContainerType());
        assertFalse(stringType.isCollectionLikeType());
        assertFalse(stringType.isMapLikeType());
        assertFalse(stringType.useStaticType());

        JavaType subStringType = stringType.narrowBy(String.class);
        assertEquals(String.class, subStringType.getRawClass());
        assertEquals(stringType, subStringType);

        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType longType = TypeFactory.defaultInstance().constructType(long.class);

        JavaType intOrLongType = intType.widenBy(long.class);
        assertEquals(intOrLongType.getClass(), JavaType.class);
        assertEquals(long.class, intOrLongType.getRawClass());

        JavaType arrayIntType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType arrayStringType = TypeFactory.defaultInstance().constructType(String[].class);

        JavaType arrayIntOrStringType = arrayIntType.widenBy(String[].class);
        JavaType arrayIntOrStringType2 = arrayStringType.narrowBy(int[].class);
        assertEquals(int[].class, arrayIntOrStringType2.getRawClass());
        assertEquals(arrayIntOrStringType, arrayIntOrStringType2);

        JavaType mapType = TypeFactory.defaultInstance().constructType(HashMap.class);
        JavaType mapStringIntType = TypeFactory.defaultInstance()
                .constructType(Map.class, String.class, IntType.class);
        JavaType mapStringIntType2 = mapType.narrowBy(Map.class, String.class, IntType.class);
        assertEquals(mapStringIntType, mapStringIntType2);

        try {
            JavaType mapStringIntType3 = mapType.narrowBy(Map.class, String.class, LongType.class);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        JavaType collectionType = TypeFactory.defaultInstance()
                .constructType(ArrayList.class, String.class);
        JavaType collectionLikeType = TypeFactory.defaultInstance()
                .constructType(AbstractList.class, String.class);

        JavaType collectionLikeType2 = collectionType.narrowBy(AbstractList.class, String.class);
        assertEquals(collectionLikeType, collectionLikeType2);

        JavaType collectionType2 = TypeFactory.defaultInstance()
                .constructType(LinkedList.class, String.class);
        JavaType collectionLikeType3 = collectionType2.widenBy(AbstractList.class, String.class);
        assertEquals(collectionLikeType3, collectionType2);
    }

    private static class IntType extends JavaType {
        IntType() {
            super(int[].class, 0, null, null, false);
        }
    }
}