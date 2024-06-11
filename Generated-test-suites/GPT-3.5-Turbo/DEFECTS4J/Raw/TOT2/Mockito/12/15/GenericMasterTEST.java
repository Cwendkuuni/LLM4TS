import org.junit.Test;
import static org.junit.Assert.*;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        
        // Typical use case
        Field field = YourClass.class.getField("exampleField");
        Class result = genericMaster.getGenericType(field);
        assertEquals(String.class, result);
        
        // Edge case: field is not a generic type
        Field nonGenericTypeField = YourClass.class.getField("nonGenericTypeField");
        Class nonGenericTypeResult = genericMaster.getGenericType(nonGenericTypeField);
        assertEquals(Object.class, nonGenericTypeResult);
        
        // Exception handling: field is null
        try {
            Field nullField = null;
            Class nullFieldResult = genericMaster.getGenericType(nullField);
            fail("Exception not thrown for null field");
        } catch (NoSuchFieldException e) {
            // Expected exception
        }
        
    }
}