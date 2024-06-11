import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        // Typical use case
        Type arrayType = String[].class;
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(arrayType);
        assertNotNull(typeInfoArray);

        // Edge case: Empty array
        Type emptyArrayType = new String[0].getClass();
        TypeInfoArray emptyTypeInfoArray = TypeInfoFactory.getTypeInfoForArray(emptyArrayType);
        assertNotNull(emptyTypeInfoArray);

        // Error scenario: Invalid input
        try {
            TypeInfoArray invalidTypeInfoArray = TypeInfoFactory.getTypeInfoForArray(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void testGetTypeInfoForField() {
        // Typical use case
        Field field = MyClass.class.getDeclaredField("fieldName");
        Type parentType = MyClass.class;
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
        assertNotNull(typeInfo);

        // Edge case: Field with ParameterizedType
        Field parameterizedField = MyClass.class.getDeclaredField("parameterizedField");
        TypeInfo parameterizedTypeInfo = TypeInfoFactory.getTypeInfoForField(parameterizedField, parentType);
        assertNotNull(parameterizedTypeInfo);

        // Error scenario: Missing TypeToken idiom
        Field missingTypeTokenField = MyClass.class.getDeclaredField("missingTypeTokenField");
        try {
            TypeInfo typeInfoMissingTypeToken = TypeInfoFactory.getTypeInfoForField(missingTypeTokenField, parentType);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ignored) {
        }

    }

    // Additional test cases for other methods can be added here

}