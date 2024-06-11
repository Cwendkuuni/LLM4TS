import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class GenericMasterTest {

    private GenericMaster genericMaster;
    
    @Before
    public void setUp() {
        genericMaster = new GenericMaster();
    }
    
    @Test
    public void testGetGenericType() throws NoSuchFieldException {
        Field field = getField("field");
        Class<?> expected = Object.class;
        
        Class<?> actual = genericMaster.getGenericType(field);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetGenericTypeWithParameterizedType() throws NoSuchFieldException {
        Field field = getField("parameterizedField");
        Class<?> expected = String.class;
        
        Class<?> actual = genericMaster.getGenericType(field);
        
        assertEquals(expected, actual);
    }
    
    private Field getField(String fieldName) throws NoSuchFieldException {
        return GenericMaster.class.getDeclaredField(fieldName);
    }
}