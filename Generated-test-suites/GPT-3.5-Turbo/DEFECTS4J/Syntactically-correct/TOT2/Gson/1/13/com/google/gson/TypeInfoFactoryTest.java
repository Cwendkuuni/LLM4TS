import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        Type arrayType = new TypeToken<String[]>() {}.getType();
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(arrayType);
        assertNotNull(typeInfoArray);
        assertEquals(arrayType, typeInfoArray.getType());
    }

    @Test
    public void testGetTypeInfoForField() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("testField");
        Type fieldType = new TypeToken<String>() {}.getType();
        TypeInfo typeInfoField = TypeInfoFactory.getTypeInfoForField(field, fieldType);
        assertNotNull(typeInfoField);
        assertEquals(fieldType, typeInfoField.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeInfoForFieldWithInvalidArgument() {
        try {
            TypeInfoFactory.getTypeInfoForField(null, null);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // test passed
        }
    }

    // Add more test cases for other public methods in TypeInfoFactory

    class TestClass {
        public String testField;
    }
}