package org.mockito.internal.util.reflection;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public class GenericMasterTest {

    @Test
    public void testGetGenericType_NonGenericField_ObjectClassReturned() {
        // Arrange
        GenericMaster genericMaster = new GenericMaster();
        Field field = getField("nonGenericField");

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        Assert.assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericType_GenericField_GenericTypeReturned() {
        // Arrange
        GenericMaster genericMaster = new GenericMaster();
        Field field = getField("genericField");

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        Assert.assertEquals(String.class, result);
    }

    @Test
    public void testGetGenericType_NestedGenericField_FirstLevelGenericTypeReturned() {
        // Arrange
        GenericMaster genericMaster = new GenericMaster();
        Field field = getField("nestedGenericField");

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        Assert.assertEquals(Integer.class, result);
    }

    private Field getField(String fieldName) {
        try {
            return GenericMaster.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}