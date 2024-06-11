import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray_typicalCase() {
        Type type = new String[10].getClass();
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
        assertNotNull(typeInfoArray);
    }

    @Test
    public void testGetTypeInfoForArray_edgeCase() {
        Type type = new String[0].getClass();
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
        assertNotNull(typeInfoArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeInfoForArray_errorCase() {
        Type type = String.class;
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
        assertNotNull(typeInfoArray);
    }

    @Test
    public void testGetTypeInfoForField_typicalCase() {
        Field field = MyClass.class.getDeclaredField("myField");
        Type fieldType = field.getGenericType();
        Type typeDefiningField = MyClass.class;
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningField);
        assertNotNull(typeInfo);
    }

    @Test
    public void testGetTypeInfoForField_edgeCase() {
        Field field = MyClass.class.getDeclaredField("myField");
        Type fieldType = field.getGenericType();
        Type typeDefiningField = MyClass.class;
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningField);
        assertNotNull(typeInfo);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetTypeInfoForField_errorCase() {
        Field field = MyClass.class.getDeclaredField("myField");
        Type fieldType = field.getGenericType();
        Type typeDefiningField = String.class;
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningField);
        assertNotNull(typeInfo);
    }

    private static class MyClass {
        private String myField;
    }
}