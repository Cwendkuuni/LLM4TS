package org.mockito.internal.util.reflection;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


public class GenericMasterTest {

    /**
     * Method to test getGenericType() when field is not generic
     */
    @Test
    public void testGetGenericTypeNotGeneric() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = getValueField();
        
        Class expected = Object.class;
        Class actual = genericMaster.getGenericType(field);
        
        Assert.assertEquals(expected, actual);
    }
    
    /**
     * Method to test getGenericType() when field is generic
     */
    @Test
    public void testGetGenericTypeGeneric() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = getGenericValueField();
        
        Class expected = String.class;
        Class actual = genericMaster.getGenericType(field);
        
        Assert.assertEquals(expected, actual);
    }
    
    /**
     * Helper method to return a field with no generic type
     *
     * @return non-generic field
     */
    private Field getValueField() {
        try {
            return GenericMaster.class.getDeclaredField("value");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Helper method to return a field with generic type
     *
     * @return generic field
     */
    private Field getGenericValueField() {
        try {
            return GenericMaster.class.getDeclaredField("genericValue");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        
        return null;
    }
}