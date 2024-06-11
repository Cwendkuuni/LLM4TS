
package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class GenericMasterTest {
    @Mock
    private Field field;

    private GenericMaster genericMaster;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        genericMaster = new GenericMaster();
    }

    @Test
    public void testGetGenericType_ReturnsObjectClass_WhenFieldIsNotGeneric() {
        // Arrange
        Class expected = Object.class;

        // Act
        Class actual = genericMaster.getGenericType(field);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testGetGenericType_ReturnsParameterizedType_WhenFieldIsGeneric() {
        // Arrange
        ParameterizedType genericType = new ParameterizedTypeImpl();
        Field field = getMockField(genericType);
        Class expected = String.class;

        // Act
        Class actual = genericMaster.getGenericType(field);

        // Assert
        assertEquals(expected, actual);
    }

    private Field getMockField(Type genericType) {
        Field field = null;
        try {
            field = getClass().getDeclaredField("field");
            field.setAccessible(true);
            field.setGenericType(genericType);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return field;
    }

    private class ParameterizedTypeImpl implements ParameterizedType {
        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{String.class};
        }

        @Override
        public Type getRawType() {
            return null;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }
}
