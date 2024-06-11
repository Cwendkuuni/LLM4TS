import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    // Test case for getTypeInfoForArray method
    @Test
    public void testGetTypeInfoForArray() {
        // Typical use case
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(String[].class);
        assertNotNull(typeInfoArray);

        // Edge case: Empty array
        TypeInfoArray emptyArrayTypeInfo = TypeInfoFactory.getTypeInfoForArray(new String[0].getClass());
        assertNotNull(emptyArrayTypeInfo);

        // Error scenario: Non-array type input
        try {
            TypeInfoFactory.getTypeInfoForArray(String.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Passed
        }
    }

    // Test case for getTypeInfoForField method
    @Test
    public void testGetTypeInfoForField() throws NoSuchFieldException {
        // Typical use case
        ClassA classA = new ClassA();
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(classA.getClass().getDeclaredField("field1"), ClassA.class);
        assertNotNull(typeInfo);

        // Edge case: Field with type TypeVariable
        TypeInfo typeVariableTypeInfo = TypeInfoFactory.getTypeInfoForField(classA.getClass().getDeclaredField("field2"), ClassA.class);
        assertNotNull(typeVariableTypeInfo);

        // Error scenario: Field not found
        try {
            TypeInfoFactory.getTypeInfoForField(classA.getClass().getDeclaredField("nonexistentField"), ClassA.class);
            fail("Expected NoSuchFieldException to be thrown");
        } catch (NoSuchFieldException e) {
            // Passed
        }
    }

    // Helper class for testing
    private class ClassA {
        private String field1;
        private TypeVariable field2;
    }
}