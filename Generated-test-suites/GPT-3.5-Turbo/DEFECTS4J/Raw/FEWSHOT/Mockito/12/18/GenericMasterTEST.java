package org.mockito.internal.util.reflection;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericMasterTest {

    private GenericMaster genericMaster = new GenericMaster();

    @Test
    public void testGetGenericType_ParameterizedField_ReturnsActualType() throws NoSuchFieldException {
        // Arrange
        Field field = MyClass.class.getDeclaredField("list");

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        Assert.assertEquals(String.class, result);
    }

    @Test
    public void testGetGenericType_NotParameterizedField_ReturnsObjectClass() throws NoSuchFieldException {
        // Arrange
        Field field = MyClass.class.getDeclaredField("notGeneric");

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        Assert.assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericType_NestedGenericField_ReturnsActualType() throws NoSuchFieldException {
        // Arrange
        Field field = MyClass.class.getDeclaredField("nestedList");

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        Assert.assertEquals(Double.class, result);
    }

    private class MyClass {
        private List<String> list;
        private String notGeneric;
        private List<List<Double>> nestedList;
    }
}