import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;

public class GenericMasterTest {

    private GenericMaster genericMaster;
    
    @Before
    public void setUp() {
        genericMaster = new GenericMaster();
    }
    
    @Test
    public void testGetGenericType_nonGenericField() throws NoSuchFieldException {
        Field field = MyObject.class.getDeclaredField("nonGenericField");
        
        Class<?> result = genericMaster.getGenericType(field);
        
        assertEquals(Object.class, result);
    }
    
    @Test
    public void testGetGenericType_genericField() throws NoSuchFieldException {
        Field field = MyObject.class.getDeclaredField("genericField");
        
        Class<?> result = genericMaster.getGenericType(field);
        
        assertEquals(String.class, result);
    }
    
    private class MyObject {
        private Object nonGenericField;
        private List<String> genericField;
    }
}