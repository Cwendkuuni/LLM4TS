import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        
        // Typical case
        Field field = SomeClass.class.getDeclaredField("someField");
        Class result = genericMaster.getGenericType(field);
        assertEquals(String.class, result);
        
        // Edge case: Field is not generic
        Field nonGenericField = SomeClass.class.getDeclaredField("nonGenericField");
        Class nonGenericResult = genericMaster.getGenericType(nonGenericField);
        assertEquals(Object.class, nonGenericResult);
        
        // Exception handling: Empty field
        try {
            Field emptyField = SomeClass.class.getDeclaredField("");
            Class emptyResult = genericMaster.getGenericType(emptyField);
        } catch (Exception e) {
            assertEquals(NoSuchFieldException.class, e.getClass());
        }
    }
}