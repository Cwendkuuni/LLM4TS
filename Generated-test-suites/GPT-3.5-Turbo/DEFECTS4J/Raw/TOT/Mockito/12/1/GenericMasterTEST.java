package org.mockito.internal.util.reflection;

import org.junit.Test;
import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class GenericMasterTest {

    @Test
    public void testGetGenericType_shouldReturnActualType_whenFieldIsGeneric() {
        // Arrange
        GenericMaster genericMaster = new GenericMaster();
        Field field = genericMaster.getClass().getDeclaredField("genericField");

        // Act
        Class<?> actualType = genericMaster.getGenericType(field);

        // Assert
        assertEquals(String.class, actualType);
    }

    @Test
    public void testGetGenericType_shouldReturnObjectClass_whenFieldIsNotGeneric() {
        // Arrange
        GenericMaster genericMaster = new GenericMaster();
        Field field = genericMaster.getClass().getDeclaredField("nonGenericField");

        // Act
        Class<?> actualType = genericMaster.getGenericType(field);

        // Assert
        assertEquals(Object.class, actualType);
    }

    @Test
    public void testGetGenericType_shouldReturnObjectClass_whenFieldIsNestedGeneric() {
        // Arrange
        GenericMaster genericMaster = new GenericMaster();
        Field field = genericMaster.getClass().getDeclaredField("nestedGenericField");

        // Act
        Class<?> actualType = genericMaster.getGenericType(field);

        // Assert
        assertEquals(Object.class, actualType);
    }

    @Test(expected = NoSuchFieldException.class)
    public void testGetGenericType_shouldThrowException_whenFieldDoesNotExist() throws NoSuchFieldException {
        // Arrange
        GenericMaster genericMaster = new GenericMaster();
        
        // Act
        genericMaster.getGenericType(null);

        // Assert (Expecting NoSuchFieldException)
    }
}