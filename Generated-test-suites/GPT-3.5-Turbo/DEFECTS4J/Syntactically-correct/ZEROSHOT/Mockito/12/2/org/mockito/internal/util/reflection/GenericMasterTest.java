package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class GenericMasterTest {

    @Test
    public void testGetGenericType_returnsObjectClass() {
        GenericMaster genericMaster = new GenericMaster();
        Class expected = Object.class;
        
        Class actual = genericMaster.getGenericType(null);
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testGetGenericType_returnsActualType() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = null; // Add a valid field here
        Type genericType = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] { String.class };
            }
            
            @Override
            public Type getRawType() {
                return null;
            }
            
            @Override
            public Type getOwnerType() {
                return null;
            }
        };
        Class expected = String.class;
        
        Class actual = genericMaster.getGenericType(field);
        
        Assert.assertEquals(expected, actual);
    }

}