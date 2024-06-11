
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GenericMasterTest {
    
    @Test
    public void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        
        // Test case for when field is generic
        Field field = MyClass.class.getDeclaredField("genericField");
        Class result = genericMaster.getGenericType(field);
        assertEquals(String.class, result);
        
        // Test case for when field is not generic
        Field field2 = MyClass.class.getDeclaredField("nonGenericField");
        Class result2 = genericMaster.getGenericType(field2);
        assertEquals(Object.class, result2);
    }
    
    class MyClass {
        private List<String> genericField;
        private String nonGenericField;
    }
}
