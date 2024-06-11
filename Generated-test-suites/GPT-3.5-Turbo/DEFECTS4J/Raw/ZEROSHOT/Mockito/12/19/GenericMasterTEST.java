package org.mockito.internal.util.reflection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GenericMasterTest {

    private GenericMaster genericMaster;

    @Before
    public void setUp() {
        genericMaster = new GenericMaster();
    }

    @Test
    public void testGetGenericType_WithGenericField_ReturnsCorrectClass() throws NoSuchFieldException {
        // Arrange
        Field field = getMockedField();
        Type genericType = mock(Type.class);
        when(field.getGenericType()).thenReturn(genericType);
        ParameterizedType parameterizedType = mock(ParameterizedType.class);
        when(genericType instanceof ParameterizedType).thenReturn(true);
        when(field.getGenericType()).thenReturn(parameterizedType);
        Class actualType = Object.class;
        when(parameterizedType.getActualTypeArguments()).thenReturn(new Type[]{actualType});

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(actualType, result);
    }

    @Test
    public void testGetGenericType_WithNonGenericField_ReturnsObjectClass() throws NoSuchFieldException {
        // Arrange
        Field field = getMockedField();
        Type genericType = null;
        when(field.getGenericType()).thenReturn(genericType);

        // Act
        Class result = genericMaster.getGenericType(field);

        // Assert
        assertEquals(Object.class, result);
    }

    private Field getMockedField() throws NoSuchFieldException {
        String fieldName = "fieldName";
        Class clazz = getClass();
        return clazz.getDeclaredField(fieldName);
    }
}