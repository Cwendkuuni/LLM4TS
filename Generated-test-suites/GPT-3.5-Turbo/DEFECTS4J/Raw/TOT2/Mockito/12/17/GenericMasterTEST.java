import org.junit.Test;
import static org.junit.Assert.*;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        
        // Typical use case
        Field field = TestClass.class.getDeclaredField("listField");
        Class type = genericMaster.getGenericType(field);
        assertEquals(String.class, type);
        
        // Edge case: Checking for non-generic field
        Field fieldNoGenericType = TestClass.class.getDeclaredField("nonGenericField");
        Class typeNoGeneric = genericMaster.getGenericType(fieldNoGenericType);
        assertEquals(Object.class, typeNoGeneric);
        
        // Exception handling: Testing for null field
        try {
            Class typeNull = genericMaster.getGenericType(null);
            fail("Exception not thrown for null field");
        } catch (NoSuchFieldException e) {
            // Expected exception
        }
    }

    private static class TestClass {
        List<String> listField;
        int nonGenericField;
    }
}