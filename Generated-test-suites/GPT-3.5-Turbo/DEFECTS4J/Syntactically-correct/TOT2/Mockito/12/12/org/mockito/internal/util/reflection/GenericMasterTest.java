import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        
        // Typical use case
        Field field = TestClass.class.getDeclaredField("listField");
        Class result = genericMaster.getGenericType(field);
        assertEquals(String.class, result);
        
        // Edge case: Field is not generic
        Field nonGenericField = TestClass.class.getDeclaredField("nonGenericField");
        result = genericMaster.getGenericType(nonGenericField);
        assertEquals(Object.class, result);
        
        // Edge case: Nested generics (ignored)
        Field nestedGenericField = TestClass.class.getDeclaredField("nestedGenericField");
        result = genericMaster.getGenericType(nestedGenericField);
        assertEquals(Object.class, result);
    }
    
    class TestClass {
        List<String> listField;
        String nonGenericField;
        List<List<String>> nestedGenericField;
    }
}