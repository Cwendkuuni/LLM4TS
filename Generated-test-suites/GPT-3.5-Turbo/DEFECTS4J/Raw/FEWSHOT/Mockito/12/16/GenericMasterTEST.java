
package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import org.junit.Test;
import org.mockito.Mockito;

public class GenericMasterTest {

    @Test
    public void testGetGenericType_WithGenericField_ReturnsCorrectType() {
        // Arrange
        Field field = Mockito.mock(Field.class);
        ParameterizedType parameterizedType = Mockito.mock(ParameterizedType.class);
        Class genericType = String.class;

        Mockito.when(field.getGenericType()).thenReturn(parameterizedType);
        Mockito.when(parameterizedType.getActualTypeArguments()).thenReturn(new Type[]{genericType});

        GenericMaster genericMaster = new GenericMaster();

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(genericType, result);
    }

    @Test
    public void testGetGenericType_WithNonGenericField_ReturnsObjectClass() {
        // Arrange
        Field field = Mockito.mock(Field.class);

        Mockito.when(field.getGenericType()).thenReturn(null);

        GenericMaster genericMaster = new GenericMaster();

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(Object.class, result);
    }
}
