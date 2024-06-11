import static org.junit.Assert.*;
import org.junit.*;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.WildcardType;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        // Typical use case - testing array of integers
        Type arrayType = Integer[].class;
        TypeInfoArray arrayTypeInfo = TypeInfoFactory.getTypeInfoForArray(arrayType);
        assertNotNull(arrayTypeInfo);
        assertEquals(arrayType, arrayTypeInfo.getType());
        
        // Edge case - testing empty array
        Type emptyArrayType = new Integer[0].getClass();
        TypeInfoArray emptyArrayTypeInfo = TypeInfoFactory.getTypeInfoForArray(emptyArrayType);
        assertNotNull(emptyArrayTypeInfo);
        assertEquals(emptyArrayType, emptyArrayTypeInfo.getType());
        
        // Error scenario - testing with non-array type
        Type nonArrayType = Integer.class;
        try {
            TypeInfoArray nonArrayTypeInfo = TypeInfoFactory.getTypeInfoForArray(nonArrayType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception is expected
        }
    }

    @Test
    public void testGetTypeInfoForField() throws NoSuchFieldException {
        // Typical use case - testing field with actual type
        Field field = DummyClass.class.getDeclaredField("dummyIntField");
        Type fieldType = field.getGenericType();
        TypeInfo fieldTypeInfo = TypeInfoFactory.getTypeInfoForField(field, DummyClass.class);
        assertNotNull(fieldTypeInfo);
        assertEquals(fieldType, fieldTypeInfo.getType());
        
        // Edge case - testing field with parameterized type
        Field field2 = DummyClass.class.getDeclaredField("dummyListField");
        Type fieldType2 = field2.getGenericType();
        TypeInfo fieldTypeInfo2 = TypeInfoFactory.getTypeInfoForField(field2, DummyClass.class);
        assertNotNull(fieldTypeInfo2);
        assertEquals(fieldType2, fieldTypeInfo2.getType());
        
        // Error scenario - testing with field that is not present in the given type
        Field nonExistentField = DummyClass.class.getDeclaredField("nonExistentField");
        Type fieldType3 = nonExistentField.getGenericType();
        try {
            TypeInfo fieldTypeInfo3 = TypeInfoFactory.getTypeInfoForField(nonExistentField, DummyClass.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception is expected
        }
    }

    private static class DummyClass {
        private int dummyIntField;
        private List<String> dummyListField;
    }
}