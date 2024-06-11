import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(String[].class);
        assertEquals(typeInfoArray.getType(), String[].class);
    }

    @Test
    public void testGetTypeInfoForField() throws NoSuchFieldException {
        Field field = MyClass.class.getDeclaredField("myField");
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, MyClass.class);
        assertEquals(typeInfo.getType(), String.class);
    }

    private static class MyClass {
        private String myField;
    }
}