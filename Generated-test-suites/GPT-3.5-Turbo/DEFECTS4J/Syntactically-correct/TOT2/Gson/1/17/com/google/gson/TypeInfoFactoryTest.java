import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray_typicalCase() {
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(String[].class);
        assertNotNull(typeInfoArray);
        assertEquals(String[].class, typeInfoArray.getType());
    }

    @Test
    public void testGetTypeInfoForField_typicalCase() {
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(MyClass.class.getDeclaredField("myField"), MyClass.class);
        assertNotNull(typeInfo);
        assertEquals(MyClass.class.getDeclaredField("myField").getGenericType(), typeInfo.getType());
    }

    @Test
    public void testGetActualType_Class() {
        Type actualType = TypeInfoFactory.getActualType(String.class, String.class, String.class);
        assertNotNull(actualType);
        assertEquals(String.class, actualType);
    }

    @Test
    public void testGetActualType_ParameterizedType() {
        ParameterizedType parameterizedType = new ParameterizedTypeImpl(MyClass.class, new Type[]{String.class}, null);
        Type actualType = TypeInfoFactory.getActualType(parameterizedType, MyClass.class, MyClass.class);
        assertNotNull(actualType);
        assertEquals(parameterizedType, actualType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetActualType_TypeVariable_withoutParameterizedType() {
        TypeVariable<?> typeVariable = MyClass.class.getTypeParameters()[0];
        TypeInfoFactory.getActualType(typeVariable, MyClass.class, MyClass.class);
    }

    @Test
    public void testExtractRealTypes_typicalCase() {
        Type[] actualTypeArguments = {String.class, Integer.class};
        Type[] realTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, MyClass.class, MyClass.class);
        assertNotNull(realTypes);
        assertEquals(actualTypeArguments.length, realTypes.length);
        assertEquals(String.class, realTypes[0]);
        assertEquals(Integer.class, realTypes[1]);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIndex_typeNotPresent() {
        TypeVariable<?>[] typeVariables = MyClass.class.getTypeParameters();
        TypeInfoFactory.getIndex(typeVariables, String.class);
    }

    private static class MyClass {
        private String myField;
    }
}