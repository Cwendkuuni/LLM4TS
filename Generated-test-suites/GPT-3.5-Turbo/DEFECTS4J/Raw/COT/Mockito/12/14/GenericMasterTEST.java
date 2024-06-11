import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() throws NoSuchFieldException {
        // Create an instance of GenericMaster
        GenericMaster genericMaster = new GenericMaster();
        
        // Create a field to test
        Field field = GenericMaster.class.getDeclaredField("fieldName");
        
        // Test when the field is not generic
        Class result = genericMaster.getGenericType(field);
        assertEquals(Object.class, result);
        
        // Create a field to test
        Field genericField = GenericMaster.class.getDeclaredField("genericFieldName");
        
        // Test when the field is generic
        Class result2 = genericMaster.getGenericType(genericField);
        assertEquals(String.class, result2);
    }
}