import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        Type arrayType = new Type[] {}.getClass();
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(arrayType);
        assertNotNull(typeInfoArray);
        assertEquals(arrayType, typeInfoArray.getType());
    }

    @Test
    public void testGetTypeInfoForField() {
        try {
            Type type = TypeInfoFactoryTest.class.getDeclaredField("testField").getGenericType();
            TypeInfo fieldType = TypeInfoFactory.getTypeInfoForField(TestField, TypeInfoFactoryTest.class);
            assertNotNull(fieldType);
        } catch (NoSuchFieldException e) {
            fail("Field not found");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeInfoForArrayWithNonArrayType() {
        Type nonArrayType = String.class;
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(nonArrayType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetTypeInfoForFieldTypeVariableWithoutParameterizedType() {
        Type type = TypeInfoFactoryTest.class.getDeclaredField("testField").getGenericType();
        TypeInfo fieldType = TypeInfoFactory.getTypeInfoForField(TestField, String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeInfoForFieldTypeInvalidType() {
        Type invalidType = new Type() {};
        TypeInfo fieldType = TypeInfoFactory.getTypeInfoForField(TestField, TypeInfoFactoryTest.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetActualTypeWithInvalidType() {
        Type invalidType = new Type() {};
        TypeInfoFactory.getActualType(invalidType, invalidType, String.class);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIndexWithNonexistentType() {
        TypeVariable<?> type = new TypeVariable<TypeVariable>() {};
        TypeVariable<?>[] types = new TypeVariable[] {};
        TypeInfoFactory.getIndex(types, type);
    }
}