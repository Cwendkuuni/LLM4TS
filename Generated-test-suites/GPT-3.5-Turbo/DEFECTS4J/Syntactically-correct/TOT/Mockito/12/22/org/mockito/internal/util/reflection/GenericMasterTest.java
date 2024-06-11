
package org.mockito.internal.util.reflection;

import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.List;

public class GenericMasterTest {

    @Test
    public void testGetGenericType_GenericField_ReturnsCorrectType() {
        // Arrange
        Field field = getField("genericField");

        // Act
        Class result = new GenericMaster().getGenericType(field);

        // Assert
        assertEquals(String.class, result);
    }

    @Test
    public void testGetGenericType_NonGenericField_ReturnsObjectType() {
        // Arrange
        Field field = getField("nonGenericField");

        // Act
        Class result = new GenericMaster().getGenericType(field);

        // Assert
        assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericType_NestedGenericField_ReturnsCorrectType() {
        // Arrange
        Field field = getField("nestedGenericField");

        // Act
        Class result = new GenericMaster().getGenericType(field);

        // Assert
        assertEquals(List.class, result);
    }

    @Test
    public void testGetGenericType_NullField_ReturnsObjectType() {
        // Arrange
        Field field = null;

        // Act
        Class result = new GenericMaster().getGenericType(field);

        // Assert
        assertEquals(Object.class, result);
    }

    private Field getField(String fieldName) {
        try {
            return GenericMasterTestClass.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class GenericMasterTestClass {
    private List<String> genericField;
    private String nonGenericField;
    private List<List<String>> nestedGenericField;
}

