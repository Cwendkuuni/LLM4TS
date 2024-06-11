package org.mockito.internal.util.reflection.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;

@RunWith(MockitoJUnitRunner.class)
public class GenericMasterTest {

    // Test case for getGenericType method
    @Test
    public void testGetGenericType() {
        GenericMaster gm = new GenericMaster();

        try {
            Field field = this.getClass().getDeclaredField("someField"); // Replace "someField" with the name of a field in the test class
            Class expectedType = String.class; // Replace with the expected generic type
            Class actualType = gm.getGenericType(field);

            // Check for correct generic type
            if (actualType.equals(expectedType)) {
                System.out.println("Test passed: getGenericType returned correct generic type.");
            } else {
                throw new AssertionError("Test failed: getGenericType returned incorrect generic type.");
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new AssertionError("Test failed: getGenericType method threw an exception.", e);
        }
    }

    // Edge case 1: Test for null field
    @Test(expected = NullPointerException.class)
    public void testGetGenericType_NullField() {
        GenericMaster gm = new GenericMaster();
        Field field = null; // Replace with null field
        gm.getGenericType(field);
    }

    // Edge case 2: Test for non-generic field
    @Test
    public void testGetGenericType_NonGenericField() {
        GenericMaster gm = new GenericMaster();
        Field field = new Field("nonGenericField", int.class, null);
        Class actualType = gm.getGenericType(field);
        if (Object.class.equals(actualType)) {
            System.out.println("Test passed: getGenericType correctly returned Object.class for non-generic field.");
        } else {
            throw new AssertionError("Test failed: getGenericType expected Object.class for non-generic field, but got " + actualType.getName() + ".");
        }
    }

    // Edge case 3: Test for field with multiple type arguments
    @Test
    public void testGetGenericType_MultipleTypeArguments() {
        GenericMaster gm = new GenericMaster();
        try {
            Field field = List.class.getDeclaredField("list");
            Class<?> expectedType = List.class.getGenericType();
            Class<?>[] actualTypes = ((ParameterizedType) expectedType).getActualTypeArguments();
            Class actualType = (Class) actualTypes[0];

            Class returnedType = gm.getGenericType(field);
            if (actualType.isAssignableFrom(returnedType)) {
                System.out.println("Test passed: getGenericType correctly returned expected type for field with multiple type arguments.");
            } else {
                throw new AssertionError("Test failed: getGenericType expected " + actualType.getName() + ", but got " + returnedType.getName() + ".");
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new AssertionError("Test failed: getGenericType method failed to access field with multiple type arguments.", e);
        }
    }
}