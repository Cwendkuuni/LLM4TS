package com.example.junit;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * JUnit 4 test file for JavaType class
 */
public class JavaTypeTest {

    

    @Test
    public void testJavaTypeConstruction() {
        Class<Integer> intClass = Integer.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(intClass);
        assertEquals(intClass, javaType.getRawClass());
        assertTrue(javaType.isConcrete());
        assertFalse(javaType.isInterface());
        assertFalse(javaType.isEnumType());
        assertFalse(javaType.isPrimitive());
        assertFalse(javaType.isFinal());
        assertFalse(javaType.isAbstract());
        assertFalse(javaType.isArrayType());
        assertFalse(javaType.isMapLikeType());
        assertFalse(javaType.isCollectionLikeType());
        assertFalse(javaType.isContainerType());
        assertFalse(javaType.useStaticType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeConstructionWithInvalidClass() {
        JavaType javaType = new JavaType(null, 0, null, null, false);
    }

    @Test
    public void testJavaTypeNarrowing() {
        Class<Integer> intClass = Integer.class;
        Class<Byte> byteClass = Byte.class;
        JavaType intJavaType = TypeFactory.defaultInstance().constructType(intClass);
        JavaType byteJavaType = intJavaType.narrowBy(byteClass);
        assertEquals(byteClass, byteJavaType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeNarrowingWithIncompatibleClass() {
        Class<Integer> intClass = Integer.class;
        Class<Double> doubleClass = Double.class;
        JavaType intJavaType = TypeFactory.defaultInstance().constructType(intClass);
        JavaType doubleJavaType = intJavaType.narrowBy(doubleClass);
    }

    @Test
    public void testJavaTypeWidening() {
        Class<Integer> intClass = Integer.class;
        Class<Number> numberClass = Number.class;
        JavaType intJavaType = TypeFactory.defaultInstance().constructType(intClass);
        JavaType numberJavaType = intJavaType.widenBy(numberClass);
        assertEquals(numberClass, numberJavaType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeWideningWithIncompatibleClass() {
        Class<Integer> intClass = Integer.class;
        Class<String> stringClass = String.class;
        JavaType intJavaType = TypeFactory.defaultInstance().constructType(intClass);
        JavaType stringJavaType = intJavaType.widenBy(stringClass);
    }

    
}