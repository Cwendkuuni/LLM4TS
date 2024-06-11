package org.junit.jupiter.api.extension;

import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.TestGenericClass;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        Class testClass = TestGenericClass.class;
        Field testField = null; // initialize with a valid field here
        GenericMaster genericMaster = new GenericMaster();
        Class expectedType = Integer.class; // replace with the expected type

        Class actualType = genericMaster.getGenericType(testField);

        assertSame("Method getGenericType returned incorrect type", expectedType, actualType);
    }

    @Test(expected = NullPointerException.class)
    public void testGetGenericTypeWithNullField() {
        GenericMaster genericMaster = new GenericMaster();
        Field testField = null;

        try {
            genericMaster.getGenericType(testField);
        } catch (NullPointerException e) {
            // Test passes
        }
    }

    @Test(expected = IllegalAccessException.class)
    public void testGetGenericTypeWithInaccessibleField() {
        Class testClass = TestGenericClass.class;
        Field testField = testClass.getDeclaredField("privateField"); // replace with a private field name
        testField.setAccessible(true); // set accessible so test can run
        GenericMaster genericMaster = new GenericMaster();

        try {
            genericMaster.getGenericType(testField);
        } catch (IllegalAccessException e) {
            // Test passes
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGenericTypeWithNonParameterizedField() {
        Class testClass = TestGenericClass.class;
        Field testField = testClass.getDeclaredField("nonParameterizedField"); // replace with a non-parameterized field name
        GenericMaster genericMaster = new GenericMaster();

        genericMaster.getGenericType(testField); // no exception should be thrown, but the result should be Object.class

        Class actualType = genericMaster.getGenericType(testField);
        assertEquals("Method getGenericType returned incorrect type for non-generic field", Object.class, actualType);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetGenericTypeWithMultipleTypeArguments() {
        Class testClass = TestMultiArgumentGenericClass.class; // replace with a class having multiple type arguments
        Field testField = testClass.getDeclaredField("testField"); // replace with a valid field name
        GenericMaster genericMaster = new GenericMaster();

        try {
            genericMaster.getGenericType(testField);
        } catch (ArrayIndexOutOfBoundsException e) {
            // Test passes
        }
    }

}