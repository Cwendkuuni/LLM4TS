import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        // Create a GenericMaster instance
        GenericMaster genericMaster = new GenericMaster();

        // Create a field with parameterized type
        Field field = SomeClass.class.getField("list");

        // Test scenario where field has generic type
        assertEquals(String.class, genericMaster.getGenericType(field));

        // Test scenario where field does not have generic type
        Field field2 = SomeClass.class.getField("name");
        assertEquals(Object.class, genericMaster.getGenericType(field2));
    }

    // Edge cases and error scenarios can be tested by creating custom classes with nested generics and incorrect usage
    // Additional test cases for nested generics and incorrect usage should be added here

    private static class SomeClass {
        List<String> list;
        String name;
    }
}