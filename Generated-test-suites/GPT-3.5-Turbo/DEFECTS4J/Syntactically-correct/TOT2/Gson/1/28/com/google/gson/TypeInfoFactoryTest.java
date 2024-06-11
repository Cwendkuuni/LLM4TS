import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        TypeInfoArray arrayType = TypeInfoFactory.getTypeInfoForArray(String[].class);
        assertNotNull(arrayType);
        assertEquals(String[].class, arrayType.getType());
    }

    @Test
    public void testGetTypeInfoForField() {
        TypeInfo fieldTypeInfo = TypeInfoFactory.getTypeInfoForField(MyClass.class.getDeclaredField("myField"), MyClass.class);
        assertNotNull(fieldTypeInfo);
        // Add more assertions based on the actual field type information
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeInfoForFieldWithInvalidArgument() {
        TypeInfoFactory.getTypeInfoForField(null, null);
    }

    @Test
    public void testGetActualTypeForClass() {
        Type actualType = TypeInfoFactory.getActualType(String.class, MyClass.class, MyClass.class);
        assertEquals(String.class, actualType);
    }

    // Add more test cases for other methods in TypeInfoFactory class

}