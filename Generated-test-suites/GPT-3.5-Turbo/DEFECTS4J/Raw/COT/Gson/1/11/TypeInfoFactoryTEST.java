import org.junit.Test;

import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    // Test getTypeInfoForArray method
    @Test
    public void testGetTypeInfoForArray() {
        Type type = new TypeToken<String[]>() {}.getType();
        TypeInfoArray infoArray = TypeInfoFactory.getTypeInfoForArray(type);
        assertNotNull(infoArray);
        assertEquals(type, infoArray.getType());
    }

    // Test getTypeInfoForField method
    @Test
    public void testGetTypeInfoForField() throws NoSuchFieldException {
        Field field = MyClass.class.getField("myField");
        Type typeDefiningF = MyClass.class;
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
        assertNotNull(typeInfo);
        assertEquals(MyClass.MyField.class, typeInfo.getType());
    }

    // Helper class for testing
    public static class MyClass {
        public static class MyField {}
        public MyField myField;
    }

    // Additional test cases for edge cases and exceptions

    // Test getTypeInfoForArray method with non-array type
    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeInfoForArrayInvalidType() {
        Type type = String.class;
        TypeInfoArray infoArray = TypeInfoFactory.getTypeInfoForArray(type);
    }

    // Test getTypeInfoForField method with invalid field and type parameters
    @Test(expected = NoSuchFieldException.class)
    public void testGetTypeInfoForFieldInvalidField() throws NoSuchFieldException {
        Field field = MyClass.class.getField("nonExistentField");
        Type typeDefiningF = MyClass.class;
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
    }
}