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
        Field field = ExampleClass.class.getDeclaredField("exampleField");
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, ExampleClass.class);
        assertNotNull(typeInfo);
        assertEquals(field.getType(), typeInfo.getType());
    }

    @Test
    public void testGetTypeInfoForField_TypeVariable() throws NoSuchFieldException {
        Field field = ExampleClass.class.getDeclaredField("exampleFieldWithTypeVariable");
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, ExampleClass.class);
        assertNotNull(typeInfo);
        assertEquals(Integer.class, typeInfo.getType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetTypeInfoForField_NoParameterizedType() throws NoSuchFieldException {
        Field field = ExampleClass.class.getDeclaredField("exampleField");
        TypeInfoFactory.getTypeInfoForField(field, String.class);
    }

    @Test
    public void testGetActualType_ClassType() {
        Type actualType = TypeInfoFactory.getActualType(Integer.class, Integer.class, Integer.class);
        assertEquals(Integer.class, actualType);
    }

    @Test
    public void testGetActualType_ParameterizedType() {
        ParameterizedType parameterizedType = ExampleClass.class.getDeclaredFields()[1].getGenericType();
        Type actualType = TypeInfoFactory.getActualType(parameterizedType, ExampleClass.class, ExampleClass.class);
        assertTrue(actualType instanceof ParameterizedType);
        assertEquals(ExampleClass.class, ((ParameterizedType) actualType).getRawType());
    }

    @Test
    public void testGetActualType_GenericArrayType() {
        Type genericArrayType = ExampleClass.class.getDeclaredFields()[2].getGenericType();
        Type actualType = TypeInfoFactory.getActualType(genericArrayType, ExampleClass.class, ExampleClass.class);
        assertTrue(actualType instanceof GenericArrayType);
        assertEquals(String[].class, ((GenericArrayType) actualType).getGenericComponentType());
    }

    @Test
    public void testGetActualType_TypeVariable() {
        ParameterizedType parameterizedType = ExampleClass.class.getDeclaredFields()[1].getGenericType();
        TypeVariable<?> typeVariable = (TypeVariable<?>) parameterizedType.getActualTypeArguments()[0];
        Type actualType = TypeInfoFactory.getActualType(typeVariable, parameterizedType, ExampleClass.class);
        assertEquals(Integer.class, actualType);
    }

    @Test
    public void testGetActualType_WildcardType() {
        WildcardType wildcardType = (WildcardType) ExampleClass.class.getDeclaredFields()[3].getGenericType();
        Type actualType = TypeInfoFactory.getActualType(wildcardType, ExampleClass.class, ExampleClass.class);
        assertEquals(String.class, actualType);
    }

    private class ExampleClass {
        private String exampleField;
        private List<Integer> exampleFieldWithTypeVariable;
        private String[] exampleFieldWithGenericArrayType;
        private List<? extends String> exampleFieldWithWildcardType;
    }

}