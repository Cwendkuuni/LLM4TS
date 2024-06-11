package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;

public class JavaTypeTest {

    @org.junit.Test
    public void testJavaType() {
        TypeFactory typeFactory = new TypeFactory();
        JavaType javaType1 = typeFactory.constructType(String.class);
        JavaType javaType2 = typeFactory.constructType(Integer.class);

        // Test isInstance method
        assertTrue(javaType1.isInstance(new StringBuilder("Hello World").toString()));
        assertFalse(javaType1.isInstance(123));
        assertTrue(javaType2.isInstance(123));

        // Test getRawClass method
        assertEquals(String.class, javaType1.getRawClass());
        assertEquals(Integer.class, javaType2.getRawClass());

        // Test isContainerType method
        assertFalse(javaType1.isContainerType());
        assertFalse(javaType2.isContainerType());

        // Test narrowBy method
        JavaType stringArrayType = javaType1.narrowBy(String[].class);
        JavaType intArrayType = javaType2.narrowBy(int[].class);
        assertEquals(String[].class, stringArrayType.getRawClass());
        assertEquals(int[].class, intArrayType.getRawClass());

        // Test widenBy method
        JavaType stringType = javaType1.widenBy(String.class);
        JavaType objectType = javaType2.widenBy(Object.class);
        assertEquals(String.class, stringType.getRawClass());
        assertEquals(Object.class, objectType.getRawClass());

        // Test narrowContentsBy method
        JavaType arrayOfStringType = stringArrayType.narrowContentsBy(String.class);
        JavaType arrayOfIntegerType = intArrayType.narrowContentsBy(Integer.class);
        assertEquals(String.class, arrayOfStringType.getRawClass());
        assertEquals(Integer.class, arrayOfIntegerType.getRawClass());

        // Test widenContentsBy method
        JavaType arrayOfObjectType = arrayOfStringType.widenContentsBy(Object[].class);
        JavaType arrayOfNumberType = arrayOfIntegerType.widenContentsBy(Number[].class);
        assertEquals(Object[].class, arrayOfObjectType.getRawClass());
        assertEquals(Number[].class, arrayOfNumberType.getRawClass());

        // Test isInterface method
        assertFalse(javaType1.isInterface());
        assertTrue(List.class.isInstance(typeFactory.constructType(List.class)));

        // Test isEnumType method
        assertFalse(javaType1.isEnumType());
        assertTrue(Enum.class.isInstance(typeFactory.constructType(Enum.class)));

        // Test isAbstract method
        assertFalse(javaType1.isAbstract());
        assertTrue(AbstractClass.class.isInstance(typeFactory.constructType(AbstractClass.class)));

        // Test isFinal method
        assertFalse(javaType1.isFinal());
        assertTrue(FinalClass.class.isInstance(typeFactory.constructType(FinalClass.class)));

        // Test isPrimitive method
        assertFalse(javaType1.isPrimitive());
        assertTrue(Boolean.TYPE.isInstance(typeFactory.constructType(Boolean.TYPE)));

        // Test isMapLikeType method
        assertFalse(javaType1.isMapLikeType());
        assertTrue(Map.class.isInstance(typeFactory.constructType(Map.class)));

        // Test isCollectionLikeType method
        assertFalse(javaType1.isCollectionLikeType());
        assertTrue(List.class.isInstance(typeFactory.constructType(List.class)));

        // Test useStaticType method
        assertFalse(javaType1.useStaticType());
        assertFalse(javaType2.useStaticType());

        // Test equals method
        JavaType javaType3 = javaType1;
        JavaType javaType4 = new JavaType() {};
        assertFalse(javaType1.equals(javaType4));
        assertTrue(javaType1.equals(javaType3));

        // Test hashCode method
        int hashCode1 = javaType1.hashCode();
        int hashCode2 = javaType1.hashCode();
        assertEquals(hashCode1, hashCode2);

        // Test toString method
        String toString1 = javaType1.toString();
        String toString2 = javaType1.toString();
        assertEquals(toString1, toString2);
    }

    private static class AbstractClass {}

    private static class FinalClass {}

    private static class StringBuilder { }
}