import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        Type type = new TypeToken<T[]>(){}.getType();
        TypeInfoArray arrayTypeInfo = TypeInfoFactory.getTypeInfoForArray(type);
        assertNotNull(arrayTypeInfo);
        assertEquals(TypeToken.get(T[].class).getType(), arrayTypeInfo.getType());
    }

    @Test
    public void testGetTypeInfoForField() {
        Field field = SampleClass.class.getDeclaredField("sampleField");
        Type typeDefiningField = SampleClass.class;
        TypeInfo fieldTypeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningField);
        assertNotNull(fieldTypeInfo);
        assertEquals(TypeToken.get(String.class).getType(), fieldTypeInfo.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeInfoForArrayInvalidType() {
        Type type = String.class;
        TypeInfoFactory.getTypeInfoForArray(type);
    }

    @Test(expected = NoSuchFieldException.class)
    public void testGetTypeInfoForFieldInvalidField() throws NoSuchFieldException {
        Field field = SampleClass.class.getDeclaredField("nonExistentField");
        Type typeDefiningField = SampleClass.class;
        TypeInfoFactory.getTypeInfoForField(field, typeDefiningField);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetTypeInfoForFieldTypeVariableWithoutParameterizedType() {
        Field field = SampleClass.class.getDeclaredField("typeVariableField");
        Type typeDefiningField = SampleClass.class;
        TypeInfoFactory.getTypeInfoForField(field, typeDefiningField);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeInfoForFieldInvalidType() {
        Field field = SampleClass.class.getDeclaredField("invalidField");
        Type typeDefiningField = SampleClass.class;
        TypeInfoFactory.getTypeInfoForField(field, typeDefiningField);
    }

    private static class SampleClass<T> {
        private T[] sampleField;
        private T typeVariableField;
        private InvalidFieldType invalidField;
    }

    private static class InvalidFieldType {}

}