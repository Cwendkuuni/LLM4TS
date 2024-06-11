package org.mockito.internal.util.reflection;
     
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class GenericMasterTest {

    private GenericMaster genericMaster;

    @Before
    public void setUp() {
        genericMaster = new GenericMaster();
    }

    // Test getGenericType() method

    @Test
    public void testGetGenericType_WhenFieldIsGeneric_ShouldReturnCorrectGenericType() throws NoSuchFieldException {
        // Arrange
        Field field = MyClass.class.getDeclaredField("listField");

        // Act
        Class<?> genericType = genericMaster.getGenericType(field);

        // Assert
        Assert.assertEquals(String.class, genericType);
    }

    @Test
    public void testGetGenericType_WhenFieldIsNotGeneric_ShouldReturnObjectClass() throws NoSuchFieldException {
        // Arrange
        Field field = MyClass.class.getDeclaredField("nonGenericField");

        // Act
        Class<?> genericType = genericMaster.getGenericType(field);

        // Assert
        Assert.assertEquals(Object.class, genericType);
    }

    @Test
    public void testGetGenericType_WhenFieldIsNestedGeneric_ShouldReturnCorrectGenericType() throws NoSuchFieldException {
        // Arrange
        Field field = MyClass.class.getDeclaredField("nestedListField");

        // Act
        Class<?> genericType = genericMaster.getGenericType(field);

        // Assert
        Assert.assertEquals(Float.class, genericType);
    }

    @Test(expected = NoSuchFieldException.class)
    public void testGetGenericType_WhenInvalidField_ShouldThrowNoSuchFieldException() throws NoSuchFieldException {
        // Arrange
        Field field = MyClass.class.getDeclaredField("invalidField");

        // Act & Assert
        genericMaster.getGenericType(field);
    }

    // Supporting classes and fields
    private class MyClass {
        private List<String> listField;
        private Object nonGenericField;
        private List<List<Float>> nestedListField;
        // private String invalidField;
    }

}
