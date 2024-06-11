import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        // Typical Use Case: Test with an array of integers
        Type type = int[].class;
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
        assertNotNull(typeInfoArray);
        assertEquals(type, typeInfoArray.getType()); // Check that the type is correctly set
    }

    @Test
    public void testGetTypeInfoForField() throws NoSuchFieldException {
        // Typical Use Case: Test with a field of type String
        Field field = ExampleClass.class.getDeclaredField("exampleField");
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, ExampleClass.class);
        assertNotNull(typeInfo);
        assertEquals(String.class, typeInfo.getType()); // Check that the type is correctly set
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeInfoForArrayInvalidType() {
        // Error Scenario: Test with an invalid type (not an array)
        Type type = String.class;
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
    }

    @Test(expected = NoSuchFieldException.class)
    public void testGetTypeInfoForFieldInvalidField() throws NoSuchFieldException {
        // Error Scenario: Test with an invalid field (non-existent field)
        Field field = ExampleClass.class.getDeclaredField("nonExistentField");
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, ExampleClass.class);
    }

    // Additional Test Cases for TypeInfoFactory class can be added here

}

class ExampleClass {
    private String exampleField;
}