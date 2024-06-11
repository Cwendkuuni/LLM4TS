
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import java.lang.reflect.Field;
import static org.junit.Assert.assertEquals;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();
        Field field = GenericMaster.class.getDeclaredField("generic");

        // Test case for non-generic field
        Class nonGenericType = genericMaster.getGenericType(field);
        assertEquals(Object.class, nonGenericType);
    }
    
    // Additional test cases for identified edge cases and exceptions
    
    @Test
    public void testGetGenericTypeWithGenericField() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();
        
        // Test case for generic field
        Field field = GenericMaster.class.getDeclaredField("generic");
        Class genericType = genericMaster.getGenericType(field);
        assertEquals(String.class, genericType);
    }
    
    @Test
    public void testGetGenericTypeWithNestedGenerics() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();
        
        // Test case for nested generics
        Field field = GenericMaster.class.getDeclaredField("nestedGeneric");
        Class genericType = genericMaster.getGenericType(field);
        assertEquals(Integer.class, genericType);
    }
    
    @Test
    public void testGetGenericTypeWithNoActualTypeArguments() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();
        
        // Test case for no actual type arguments
        Field field = GenericMaster.class.getDeclaredField("noActualTypeArguments");
        Class genericType = genericMaster.getGenericType(field);
        assertEquals(Object.class, genericType);
    }
}