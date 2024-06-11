package org.mockito.internal.util.reflection;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();
        Field field = GenericMaster.class.getDeclaredField("generic");
        
        // Test when field is not generic
        Class result = genericMaster.getGenericType(field);
        assertEquals(result, Object.class);
        
        // Test when field is generic
        field = GenericMaster.class.getDeclaredField("genericList");
        result = genericMaster.getGenericType(field);
        assertEquals(result, String.class);
    }
}