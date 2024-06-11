package org.junit.tests;

import org.apache.commons.lang.enums.JavaVersionEnum;
import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        ValuedEnum enumInstance = ValuedEnum.JAVA1_0;
        int expectedValue = 100;
        int actualValue = enumInstance.getValue();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetEnumNullEnumClass() {
        assertNull(ValuedEnum.getEnum(null, JavaVersionEnum.JAVA1_0.getValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumNegativeValue() {
        assertNull(ValuedEnum.getEnum(JavaVersionEnum.class, -1));
    }

    @Test
    public void testGetEnumZeroValue() {
        assertNull(ValuedEnum.getEnum(JavaVersionEnum.class, 0));
    }

    @Test
    public void testGetEnumNoMatchingEnum() {
        assertNull(ValuedEnum.getEnum(JavaVersionEnum.class, 200));
    }

    @Test
    public void testGetEnumDifferentEnumValue() {
        ValuedEnum java1_1 = JavaVersionEnum.JAVA1_1;
        ValuedEnum java1_2 = JavaVersionEnum.JAVA1_2;
        assertNotEquals(java1_1, java1_2);
        assertNotEquals(java1_1.getValue(), java1_2.getValue());
        assertNotEquals(java1_1, ValuedEnum.getEnum(JavaVersionEnum.class, java1_2.getValue()));
    }

    @Test
    public void testGetEnumDifferentClassLoader() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Class<?> otherClassLoaderClass = Class.forName("org.apache.commons.lang.enums.ValuedEnum_OtherClassLoader");
        Object otherClassLoaderEnumInstance = otherClassLoaderClass.getDeclaredConstructor().newInstance();
        int javaVersionValue = JavaVersionEnum.JAVA1_0.getValue();
        assertNotEquals(JavaVersionEnum.JAVA1_0, ValuedEnum.getEnum(otherClassLoaderClass, javaVersionValue));
    }

}