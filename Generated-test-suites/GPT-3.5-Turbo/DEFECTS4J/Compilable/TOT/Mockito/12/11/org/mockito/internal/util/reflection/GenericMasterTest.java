package org.mockito.internal.util.reflection;
import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

public class GenericMasterTest {

    @Test
    public void testGetGenericType_nonGenericField_returnObjectClass() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = getField("nonGenericField");
        
        Class result = genericMaster.getGenericType(field);
        
        assertEquals(Object.class, result);
    }
    
    @Test
    public void testGetGenericType_genericField_returnGenericTypeClass() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = getField("genericField");
        
        Class result = genericMaster.getGenericType(field);
        
        assertEquals(String.class, result);
    }
    
    @Test
    public void testGetGenericType_nestedGenericField_returnObjectClass() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = getField("nestedGenericField");
        
        Class result = genericMaster.getGenericType(field);
        
        assertEquals(Object.class, result);
    }
    
    private Field getField(String fieldName) {
        try {
            return GenericMaster.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            fail("Field not found: " + fieldName);
            return null;
        }
    }
}