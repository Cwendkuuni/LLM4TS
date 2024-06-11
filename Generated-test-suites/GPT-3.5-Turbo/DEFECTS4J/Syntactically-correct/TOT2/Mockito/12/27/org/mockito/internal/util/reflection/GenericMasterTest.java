import org.junit.Test;
import static org.junit.Assert.*;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        
        // Typical case - field is generic
        Field field = TestClass.class.getDeclaredField("listField");
        Class result = genericMaster.getGenericType(field);
        assertEquals(String.class, result);
        
        // Edge case - field is not generic
        field = TestClass.class.getDeclaredField("nonGenericField");
        result = genericMaster.getGenericType(field);
        assertEquals(Object.class, result);
        
        // Error scenario - field is null
        field = null;
        result = genericMaster.getGenericType(field);
        assertEquals(Object.class, result);
    }
    
    // Test class to be used for reflection
    private static class TestClass {
        List<String> listField;
        String nonGenericField;
    }
}