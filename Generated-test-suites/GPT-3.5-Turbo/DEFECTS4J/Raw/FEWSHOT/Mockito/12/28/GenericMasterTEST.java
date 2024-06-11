
package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericMasterTest {

    @Test
    public void testGetGenericTypeReturnsObjectClassWhenFieldIsNotGeneric() {
        //Arrange
        Field field = mock(Field.class);
        when(field.getGenericType()).thenReturn(null);

        //Act
        Class result = GenericMaster.getGenericType(field);

        //Assert
        assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericTypeReturnsActualTypeWhenFieldIsGenericAndNotNested() {
        //Arrange
        Field field = mock(Field.class);
        ParameterizedType genericType = mock(ParameterizedType.class);
        Type actualType = String.class;
        when(field.getGenericType()).thenReturn(genericType);
        when(genericType.getActualTypeArguments()).thenReturn(new Type[]{actualType});

        //Act
        Class result = GenericMaster.getGenericType(field);

        //Assert
        assertEquals(actualType, result);
    }

    @Test
    public void testGetGenericTypeReturnsObjectClassWhenFieldIsNestedGeneric() {
        //Arrange
        Field field = mock(Field.class);
        ParameterizedType genericType = mock(ParameterizedType.class);
        when(field.getGenericType()).thenReturn(genericType);
        when(genericType.getActualTypeArguments()).thenReturn(new Type[]{mock(ParameterizedType.class)});

        //Act
        Class result = GenericMaster.getGenericType(field);

        //Assert
        assertEquals(Object.class, result);
    }
}
