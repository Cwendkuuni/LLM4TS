
package org.mockito.internal.util.reflection;

import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.junit.Assert.*;

public class GenericMasterTest {

    @Test
    public void testGetGenericType_WhenFieldIsNull_ReturnObjectClass() {
        // Arrange
        GenericMaster genericMaster = new GenericMaster();
        Field field = null;
        
        // Act
        Class result = genericMaster.getGenericType(field);
        
        // Assert
        assertEquals(Object.class, result);
    }
    
    @Test
    public void testGetGenericType_WhenFieldIsNotGeneric_ReturnObjectClass() {
        // Arrange
        GenericMaster genericMaster = new GenericMaster();
        Field field = getField("nonGenericField");
        
        // Act
        Class result = genericMaster.getGenericType(field);
        
        // Assert
        assertEquals(Object.class, result);
    }
    
    @Test
    public void testGetGenericType_WhenFieldIsGeneric_ReturnGenericType() {
        // Arrange
        GenericMaster genericMaster = new GenericMaster();
        Field field = getField("genericField");
        
        // Act
        Class result = genericMaster.getGenericType(field);
        
        // Assert
        assertEquals(String.class, result);
    }
    
    @Test
    public void testGetGenericType_WhenFieldIsNestedGeneric_ReturnGenericTypeOfOuterClass() {
        // Arrange
        GenericMaster genericMaster = new GenericMaster();
        Field field = getField("nestedGenericField");
        
        // Act
        Class result = genericMaster.getGenericType(field);
        
        // Assert
        assertEquals(Integer.class, result);
    }
    
    private Field getField(String fieldName) {
        Field field = null;
        try {
            field = DummyClass.class.getDeclaredField(fieldName);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return field;
    }
    
    private class DummyClass {
        private String nonGenericField;
        private java.util.List<String> genericField;
        private java.util.List<java.util.Map<Integer, String>> nestedGenericField;
    }
}
