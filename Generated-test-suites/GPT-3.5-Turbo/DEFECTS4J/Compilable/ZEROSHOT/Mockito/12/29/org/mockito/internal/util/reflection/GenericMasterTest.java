package org.mockito.internal.util.reflection;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericMasterTest {

    @Test
    public void testGetGenericTypeWithGenericField() {
        Field field = getField("genericField");
        Class expectedType = String.class;
        GenericMaster genericMaster = new GenericMaster();
        
        Class actualType = genericMaster.getGenericType(field);
        
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetGenericTypeWithNonGenericField() {
        Field field = getField("nonGenericField");
        Class expectedType = Object.class;
        GenericMaster genericMaster = new GenericMaster();
        
        Class actualType = genericMaster.getGenericType(field);
        
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetGenericTypeWithNullField() {
        Field field = null;
        Class expectedType = Object.class;
        GenericMaster genericMaster = new GenericMaster();
        
        Class actualType = genericMaster.getGenericType(field);
        
        assertEquals(expectedType, actualType);
    }

    private Field getField(String fieldName) {
        try {
            return GenericMaster.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }
}