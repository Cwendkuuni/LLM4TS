package org.mockito.internal.util.reflection;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GenericMasterTest {

    @Test
    public void testGetGenericType_nonGenericField_returnsObjectClass() {
        GenericMaster genericMaster = new GenericMaster();
        
        Class expected = Object.class;
        Class actual = genericMaster.getGenericType(null);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetGenericType_genericField_returnsActualTypeArgument() {
        GenericMaster genericMaster = new GenericMaster();
        
        Field field = null; // TODO: Initialize field with a generic field
        
        Class expected = null; // TODO: Initialize expected with the expected actual type argument
        Class actual = genericMaster.getGenericType(field);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetGenericType_nestedGenerics_returnsActualTypeArgument() {
        GenericMaster genericMaster = new GenericMaster();
        
        Field field = null; // TODO: Initialize field with a nested generic field
        
        Class expected = null; // TODO: Initialize expected with the expected actual type argument
        Class actual = genericMaster.getGenericType(field);
        
        assertEquals(expected, actual);
    }
}