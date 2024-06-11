import org.junit.Test;
import static org.junit.Assert.*;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        
        // Typical use case
        Field field = SomeClass.class.getDeclaredField("someField");
        Class expected = String.class;
        assertEquals(expected, genericMaster.getGenericType(field));
        
        // Edge cases
        // Test when field is not generic
        field = SomeClass.class.getDeclaredField("nonGenericField");
        expected = Object.class;
        assertEquals(expected, genericMaster.getGenericType(field));
        
        // Test nested generics
        field = SomeClass.class.getDeclaredField("nestedGenericField");
        expected = List.class;
        assertEquals(expected, genericMaster.getGenericType(field));
        
        // Error scenario
        // Test when field is null
        field = null;
        expected = Object.class;
        assertEquals(expected, genericMaster.getGenericType(field));
    }
}