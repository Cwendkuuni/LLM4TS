
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericMasterTest {

    private GenericMaster genericMaster;

    @Before
    public void setUp() {
        genericMaster = new GenericMaster();
    }

    @Test
    public void testGetGenericTypeWithGenericField() throws NoSuchFieldException {
        // Create a field with a generic type
        Field field = SampleClass.class.getDeclaredField("genericList");

        // Test the getGenericType method
        Class<?> genericType = genericMaster.getGenericType(field);

        // Check if the generic type is the expected class
        assertEquals(Integer.class, genericType);
    }
    
    @Test
    public void testGetGenericTypeWithNonGenericField() throws NoSuchFieldException {
        // Create a field with a non-generic type
        Field field = SampleClass.class.getDeclaredField("nonGenericField");

        // Test the getGenericType method
        Class<?> genericType = genericMaster.getGenericType(field);

        // Check if the generic type is Object.class
        assertEquals(Object.class, genericType);
    }
    
    @Test
    public void testGetGenericTypeWithNestedGenericField() throws NoSuchFieldException {
        // Create a field with a nested generic type
        Field field = SampleClass.class.getDeclaredField("nestedGenericList");

        // Test the getGenericType method
        Class<?> genericType = genericMaster.getGenericType(field);

        // Check if the generic type is the expected class
        assertEquals(List.class, genericType);
    }

    private static class SampleClass {
        private List<Integer> genericList;
        private String nonGenericField;
        private List<List<String>> nestedGenericList;
    }
}
