import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        // Typical use case
        Type[] arrayType = new Type[]{String.class, Integer.class};
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(arrayType);
        assertNotNull(typeInfoArray);
        assertEquals(arrayType, typeInfoArray.getType());

        // Edge case
        Type[] emptyArrayType = new Type[]{};
        TypeInfoArray typeInfoEmptyArray = TypeInfoFactory.getTypeInfoForArray(emptyArrayType);
        assertNotNull(typeInfoEmptyArray);
        assertEquals(emptyArrayType, typeInfoEmptyArray.getType());
    }

    @Test
    public void testGetTypeInfoForField() throws NoSuchFieldException {
        TypeInfo typeInfo;

        // Typical use case
        Field typicalField = SomeClass.class.getField("typicalField");
        typeInfo = TypeInfoFactory.getTypeInfoForField(typicalField, SomeClass.class);
        assertNotNull(typeInfo);
        // Verify type

        // Edge case: Field with no type information
        Field emptyField = SomeClass.class.getField("emptyField");
        typeInfo = TypeInfoFactory.getTypeInfoForField(emptyField, SomeClass.class);
        assertNotNull(typeInfo);
        // Verify type

        // Error scenario: Field with unsupported type
        Field unsupportedField = SomeClass.class.getField("unsupportedField");
        try {
            typeInfo = TypeInfoFactory.getTypeInfoForField(unsupportedField, SomeClass.class);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // expected exception
        }

    }

    public static class SomeClass {
        public String typicalField;
        public Object emptyField;
        public java.util.Date unsupportedField;
    }

}