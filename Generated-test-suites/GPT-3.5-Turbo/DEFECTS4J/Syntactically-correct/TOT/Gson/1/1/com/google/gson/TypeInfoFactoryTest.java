import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public class TypeInfoFactoryTest {

    @Test
    public void getTypeInfoForArray_WithValidArray_ReturnsTypeInfoArray() {
        Type type = new Type() {
            @Override
            public String getTypeName() {
                return "java.util.ArrayList";
            }
        };

        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);

        assertNotNull(typeInfoArray);
        assertEquals(type, typeInfoArray.getType());
    }

    @Test
    public void getTypeInfoForArray_WithInvalidArray_ThrowsIllegalArgumentException() {
        Type type = new Type() {
            @Override
            public String getTypeName() {
                return "java.util.List";
            }
        };

        try {
            TypeInfoFactory.getTypeInfoForArray(type);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Type must be an array", e.getMessage());
        }
    }

    @Test
    public void getTypeInfoForField_WithValidField_ReturnsTypeInfo() throws NoSuchFieldException {
        Field field = MyClass.class.getDeclaredField("str");
        Type typeDefiningF = MyClass.class;

        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);

        assertNotNull(typeInfo);
    }

    @Test
    public void getTypeInfoForField_WithInvalidField_ThrowsNoSuchFieldException() {
        try {
            Field field = MyClass.class.getDeclaredField("nonExistentField");
            Type typeDefiningF = MyClass.class;

            TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
            fail("Expected NoSuchFieldException to be thrown");
        } catch (NoSuchFieldException e) {
            assertEquals("Field 'nonExistentField' does not exist in class 'MyClass'", e.getMessage());
        }
    }

    @Test
    public void getTypeInfoForField_WithValidTypeVariable_ReturnsTypeInfo() throws NoSuchFieldException {
        Field field = MyClass.class.getDeclaredField("genericField");
        Type typeDefiningF = MyClass.class;

        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);

        assertNotNull(typeInfo);
    }

    @Test
    public void getTypeInfoForField_WithInvalidTypeVariable_ThrowsUnsupportedOperationException() throws NoSuchFieldException {
        try {
            Field field = MyClass.class.getDeclaredField("nonGenericField");
            Type typeDefiningF = MyClass.class;

            TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Expecting parameterized type, got java.lang.Class", e.getMessage());
        }
    }

    private static class MyClass {
        private String str;

        private Type nonGenericField;
        private TypeVariable<?> genericField;
    }
}