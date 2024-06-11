package org.mockito.internal.util.reflection.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class GenericMasterTest {

    @Test
    public void testGetGenericType() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();

        // Test with a generic field
        Field fieldWithGenericType = this.getClass().getDeclaredField("testField");
        fieldWithGenericType.setAccessible(true);
        Class expectedType = String.class;
        Class actualType = genericMaster.getGenericType(fieldWithGenericType);
        assertEquals(expectedType, actualType);

        // Test with a non-generic field
        Field nonGenericField = this.getClass().getDeclaredField("nonGenericField");
        nonGenericField.setAccessible(true);
        assertNull(genericMaster.getGenericType(nonGenericField));

        // Test with null field
        assertNull(genericMaster.getGenericType(null));

        // Test with private field
        Field privateField = this.getClass().getDeclaredField("privateField");
        privateField.setAccessible(true);
        assertNull(genericMaster.getGenericType(privateField));

        // Test with field of multiple types
        Field multipleTypesField = this.getClass().getDeclaredField("multipleTypesField");
        multipleTypesField.setAccessible(true);
        Class<?>[] expectedTypes = {Integer.class, String.class};
        Class<?>[] actualTypes = genericMaster.getGenericType(multipleTypesField).getActualTypeArguments();
        assertArrayEquals(expectedTypes, actualTypes);
    }

    // Add more test cases for edge cases and exceptions as needed

    private int testField;
    private String testStringField;
    private int[] multipleTypesField;
    private String privateField;

    // Add getters for the fields above as needed
}