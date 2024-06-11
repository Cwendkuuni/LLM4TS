
package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.junit.Test;

public class GenericMasterTest {

    @Test
    public void testGetGenericTypeNonGenericField() {
        GenericMaster gm = new GenericMaster();
        Field field = getField("nonGenericField");
        
        Class result = gm.getGenericType(field);
        
        assertEquals(Object.class, result);
    }
    
    @Test
    public void testGetGenericTypeGenericField() {
        GenericMaster gm = new GenericMaster();
        Field field = getField("genericField");
        
        Class result = gm.getGenericType(field);
        
        assertEquals(String.class, result);
    }

    @Test
    public void testGetGenericTypeNestedGenericField() {
        GenericMaster gm = new GenericMaster();
        Field field = getField("nestedGenericField");
        
        Class result = gm.getGenericType(field);
        
        assertEquals(Object.class, result);
    }
    
    private Field getField(String fieldName) {
        try {
            return TestClass.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static class TestClass {
        private String nonGenericField;
        private List<String> genericField;
        private List<List<String>> nestedGenericField;
    }

}
