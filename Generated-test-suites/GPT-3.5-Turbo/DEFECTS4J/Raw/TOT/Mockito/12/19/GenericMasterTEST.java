package org.mockito.internal.util.reflection;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class GenericMasterTest {

    GenericMaster genericMaster;

    @Test
    public void testGetGenericType_WhenFieldIsGeneric_ShouldReturnActualType() throws NoSuchFieldException {
        // Arrange
        Field field = GenericMasterTest.class.getDeclaredField("genericList");

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(String.class, result);
    }

    @Test
    public void testGetGenericType_WhenNoGenericType_ShouldReturnObjectClass() throws NoSuchFieldException {
        // Arrange
        Field field = GenericMasterTest.class.getDeclaredField("nonGenericField");

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericType_WhenNestedGeneric_ShouldReturnActualTypeOfNestedGeneric() throws NoSuchFieldException {
        // Arrange
        Field field = GenericMasterTest.class.getDeclaredField("nestedGenericField");

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(Integer.class, result);
    }

    @Test(expected = NoSuchFieldException.class)
    public void testGetGenericType_WhenFieldDoesNotExist_ShouldThrowNoSuchFieldException() throws NoSuchFieldException {
        // Arrange
        Field field = GenericMasterTest.class.getDeclaredField("nonExistentField");

        // Act
        genericMaster.getGenericType(field);

        // Assert - Throws exception
    }
}

// Define extra classes for testing nested generics
class GenericList<T> {}
class NestedGeneric<T> {
    GenericList<T> genericList;
}
class GenericMasterTest {
    GenericList<String> genericList;
    NestedGeneric<Integer> nestedGenericField;
    Object nonGenericField;
}