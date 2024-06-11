import org.junit.Test;
import static org.junit.Assert.*;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        
        Field field = null; //create a Field object for testing
        
        // Test case for generic field
        Class expectedGenericType = String.class;
        assertEquals(expectedGenericType, genericMaster.getGenericType(field));
        
        // Test case for non-generic field
        field = SampleClass.class.getDeclaredField("nonGenericField");
        assertEquals(Object.class, genericMaster.getGenericType(field));
    }
    
    static class SampleClass {
        List<String> genericField;
        String nonGenericField;
    }
}