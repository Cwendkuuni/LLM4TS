
package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

public class GenericMasterTest {
  
    private GenericMaster genericMaster;
  
    @Before
    public void setUp() {
        genericMaster = new GenericMaster();
    }
  
    @Test
    public void testGetGenericType_withGenericField() throws Exception {
        Field field = SomeClass.class.getDeclaredField("genericField");
        
        Class expectedGenericType = SomeType.class;
        Class actualGenericType = genericMaster.getGenericType(field);
        
        assertEquals(expectedGenericType, actualGenericType);
    }
    
    @Test
    public void testGetGenericType_withNonGenericField() throws Exception {
        Field field = SomeClass.class.getDeclaredField("nonGenericField");
        
        Class expectedGenericType = Object.class;
        Class actualGenericType = genericMaster.getGenericType(field);
        
        assertEquals(expectedGenericType, actualGenericType);
    }
  
    private class SomeType {}
    
    private class SomeClass {
        private List<SomeType> genericField;
        private String nonGenericField;
    }
}
