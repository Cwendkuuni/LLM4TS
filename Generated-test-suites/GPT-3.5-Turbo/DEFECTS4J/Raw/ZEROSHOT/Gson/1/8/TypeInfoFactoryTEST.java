import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    @Test
    public void getTypeInfoForArrayTest() {
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(String[].class);
        assertNotNull(typeInfoArray);
        assertEquals(String[].class, typeInfoArray.getType());
    }

    @Test
    public void getTypeInfoForFieldTest() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("testField");
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, TestClass.class);
        assertNotNull(typeInfo);
        assertEquals(String.class, typeInfo.getType());
    }

    // Add more test cases for getTypeInfoForField() method

    private static class TestClass {
        private String testField;
    }
}