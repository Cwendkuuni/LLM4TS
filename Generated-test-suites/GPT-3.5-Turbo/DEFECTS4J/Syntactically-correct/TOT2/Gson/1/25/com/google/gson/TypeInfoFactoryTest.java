import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        Type arrayType = new TypeToken<int[]>() {}.getType();
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(arrayType);
        assertNotNull(typeInfoArray);
        assertEquals(arrayType, typeInfoArray.getType());
    }

    @Test
    public void testGetTypeInfoForField() {
        class TestClass {
            private List<String> listField;
        }

        Field field = TestClass.class.getDeclaredField("listField");
        Type fieldType = field.getGenericType();
        Type declaringType = List.class;
        TypeInfo typeInfoField = TypeInfoFactory.getTypeInfoForField(field, declaringType);
        assertNotNull(typeInfoField);
        assertEquals(fieldType, typeInfoField.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeInfoForArrayWithInvalidType() {
        Type invalidType = String.class;
        TypeInfoFactory.getTypeInfoForArray(invalidType);
    }

    @Test(expected = NoSuchFieldException.class)
    public void testGetTypeInfoForInvalidField() throws NoSuchFieldException {
        class TestClass {
            private int intField;
        }

        Field field = TestClass.class.getDeclaredField("invalidField");
        Type declaringType = int.class;
        TypeInfoFactory.getTypeInfoForField(field, declaringType);
    }
}