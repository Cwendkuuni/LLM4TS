
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        // Test input of array type
        Type type = new Type[]{};
        TypeInfoArray result = TypeInfoFactory.getTypeInfoForArray(type);
        assertNotNull(result);
        assertEquals(TypeInfoArray.class, result.getClass());
    }

    @Test
    public void testGetTypeInfoForField() {
        // Test input of Field with TypeVariable
        Field field = getClass().getDeclaredFields()[0];
        Type typeDefiningF = getClass();
        TypeInfo result = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
        assertNotNull(result);
        assertEquals(TypeInfo.class, result.getClass());
    }

    @Test
    public void testGetTypeInfoForField_withParameterizedType() {
        // Test input of Field with ParameterizedType
        Field field = getClass().getDeclaredFields()[1];
        Type typeDefiningF = getClass();
        TypeInfo result = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
        assertNotNull(result);
        assertEquals(TypeInfo.class, result.getClass());
    }

    @Test
    public void testGetTypeInfoForField_withGenericArrayType() {
        // Test input of Field with GenericArrayType
        Field field = getClass().getDeclaredFields()[2];
        Type typeDefiningF = getClass();
        TypeInfo result = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
        assertNotNull(result);
        assertEquals(TypeInfo.class, result.getClass());
    }

    @Test
    public void testGetTypeInfoForField_withTypeVariable() {
        // Test input of Field with TypeVariable
        Field field = getClass().getDeclaredFields()[3];
        Type typeDefiningF = getClass();
        TypeInfo result = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
        assertNotNull(result);
        assertEquals(TypeInfo.class, result.getClass());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetTypeInfoForField_withUnexpectedType() {
        // Test input of Field with unexpected type
        Field field = getClass().getDeclaredFields()[4];
        Type typeDefiningF = getClass();
        TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
    }

    @Test
    public void testGetActualType_withClassType() {
        // Test input of Class as type
        Type type = getClass();
        Type parentType = getClass();
        Class<?> rawParentClass = getClass();
        Type result = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals(getClass(), result);
    }

    @Test
    public void testGetActualType_withParameterizedType() {
        // Test input of ParameterizedType as type
        Type type = new ParameterizedTypeImpl(getClass(), new Type[]{Integer.class}, null);
        Type parentType = getClass();
        Class<?> rawParentClass = getClass();
        Type result = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertNotNull(result);
        assertEquals(ParameterizedTypeImpl.class, result.getClass());
    }

    @Test
    public void testGetActualType_withGenericArrayType() {
        // Test input of GenericArrayType as type
        Type type = new GenericArrayTypeImpl(Integer.class);
        Type parentType = getClass();
        Class<?> rawParentClass = getClass();
        Type result = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertNotNull(result);
        assertEquals(GenericArrayTypeImpl.class, result.getClass());
    }

    @Test
    public void testGetActualType_withTypeVariable() {
        // Test input of TypeVariable as type
        Type type = new TypeVariableImpl();
        Type parentType = new ParameterizedTypeImpl(getClass(), new Type[]{Integer.class}, null);
        Class<?> rawParentClass = getClass();
        Type result = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals(Integer.class, result);
    }

    @Test
    public void testGetActualType_withWildcardType() {
        // Test input of WildcardType as type
        Type type = new WildcardTypeImpl(new Type[]{Number.class});
        Type parentType = getClass();
        Class<?> rawParentClass = getClass();
        Type result = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals(Number.class, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetActualType_withInvalidType() {
        // Test input of invalid type
        Type type = "not a valid type";
        Type parentType = getClass();
        Class<?> rawParentClass = getClass();
        TypeInfoFactory.getActualType(type, parentType, rawParentClass);
    }

    @Test
    public void testExtractRealTypes() {
        // Test input of Type array
        Type[] actualTypeArguments = new Type[]{Integer.class};
        Type parentType = getClass();
        Class<?> rawParentClass = getClass();
        Type[] result = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals(Integer.class, result[0]);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIndex_withTypeNotFound() {
        // Test input where type is not found in types array
        TypeVariable<?>[] types = new TypeVariable[]{};
        TypeVariable<?> type = new TypeVariableImpl();
        TypeInfoFactory.getIndex(types, type);
    }
}
