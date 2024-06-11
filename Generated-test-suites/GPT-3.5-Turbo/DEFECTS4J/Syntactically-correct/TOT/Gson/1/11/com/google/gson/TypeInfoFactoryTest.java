import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(String[].class);
        assertNotNull(typeInfoArray);
        assertEquals(String[].class, typeInfoArray.getType());
    }

    @Test
    public void testGetTypeInfoForField() throws NoSuchFieldException {
        Field field = MyClass.class.getDeclaredField("myField");
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, MyClass.class);
        assertNotNull(typeInfo);
        assertEquals(MyClass.class, typeInfo.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeInfoForArrayInvalidArgument() {
        TypeInfoFactory.getTypeInfoForArray(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeInfoForFieldInvalidArgument() {
        TypeInfoFactory.getTypeInfoForField(null, null);
    }

    private static class MyClass {
        private String myField;
    }
}