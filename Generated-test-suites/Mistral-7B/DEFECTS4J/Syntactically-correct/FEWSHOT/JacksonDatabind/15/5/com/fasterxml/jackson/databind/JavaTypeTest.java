package com.fasterxml.jackson.databind;

import static org.junit.Assert.*;

import org.junit.Test;

public class JavaTypeTest {

    @Test
    public void testUseStaticType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        assertFalse(javaType.useStaticType());

        JavaType javaTypeWithStaticTyping = javaType.withStaticTyping();
        assertTrue(javaTypeWithStaticTyping.useStaticType());
    }

    @Test
    public void testNarrowBy() {
        JavaType integerType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType narrowedIntegerType = integerType.narrowBy(Integer.class);
        assertEquals(Integer.class, narrowedIntegerType.getRawClass());
        assertEquals(integerType.getClass(), narrowedIntegerType.getClass());

        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType narrowedStringType = stringType.narrowBy(String[].class);
        assertEquals(String[].class, narrowedStringType.getRawClass());
        assertEquals(stringType.getClass(), narrowedStringType.getClass());

        JavaType baseType = TypeFactory.defaultInstance().constructType(BaseClass.class);
        JavaType narrowedSubclassType = baseType.narrowBy(Subclass.class);
        assertEquals(Subclass.class, narrowedSubclassType.getRawClass());
        assertEquals(baseType.getClass(), narrowedSubclassType.getClass());

        try {
            JavaType invalidNarrowedType = baseType.narrowBy(InvalidClass.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWidenBy() {
        JavaType intArrayType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType widenedType = intArrayType.widenBy(int.class);
        assertEquals(int.class, widenedType.getRawClass());
        assertEquals(intArrayType.getClass(), widenedType.getClass());

        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType widenedType2 = stringType.widenBy(String[].class);
        assertEquals(String[].class, widenedType2.getRawClass());
        assertEquals(stringType.getClass(), widenedType2.getClass());
    }

    @Test
    public void testNarrowContentsBy() {
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType javaTypeWithContentType = listType.narrowContentsBy(Integer.class);
        assertEquals(Integer.class, javaTypeWithContentType.getRawClass());
        assertEquals(List.class, javaTypeWithContentType.getClass());
    }

    @Test
    public void testWidenContentsBy() {
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType javaTypeWithContentType = listType.widenContentsBy(Number.class);
        assertEquals(Number.class, javaTypeWithContentType.getRawClass());
        assertEquals(List.class, javaTypeWithContentType.getClass());
    }
}

class BaseClass {
}

class Subclass extends BaseClass {
}

class InvalidClass {
}

class List<T> {
}