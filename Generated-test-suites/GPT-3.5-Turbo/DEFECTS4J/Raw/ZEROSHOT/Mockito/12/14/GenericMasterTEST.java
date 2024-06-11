package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Test;

@SuppressWarnings("unchecked")
public class GenericMasterTest {

    @Test
    public void testGetGenericType_NonGenericField_ReturnsObjectClass() throws NoSuchFieldException {
        // Arrange
        GenericMaster gm = new GenericMaster();
        Field field = gm.getClass().getDeclaredField("nonGenericField");
        
        // Act
        Class result = gm.getGenericType(field);
        
        // Assert
        assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericType_GenericField_ReturnsActualGenericType() throws NoSuchFieldException {
        // Arrange
        GenericMaster gm = new GenericMaster();
        Field field = gm.getClass().getDeclaredField("genericField");
        
        // Act
        Class result = gm.getGenericType(field);
        
        // Assert
        assertEquals(String.class, result);
    }
}