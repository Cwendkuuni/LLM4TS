
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        Type type = new Type() {};
        TypeInfoArray actualResult = TypeInfoFactory.getTypeInfoForArray(type);
        assertNotNull(actualResult);
    }

    @Test
    public void testGetTypeInfoForField() {
        Field field = new Field() {};
        Type typeDefiningF = new Type() {};
        TypeInfo actualResult = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
        assertNotNull(actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeInfoForField_withInvalidType_expectIllegalArgumentException() {
        Field field = new Field() {};
        TypeInfoFactory.getTypeInfoForField(field, null);
    }

    @Test
    public void testGetActualType_withClassType_expectSameTypeReturned() {
        Type classType = new Type() {};
        Type parentType = new Type() {};
        Class<?> rawParentClass = TypeInfoFactory.class;
        Type actualResult = TypeInfoFactory.getActualType(classType, parentType, rawParentClass);
        assertSame(classType, actualResult);
    }

    @Test
    public void testGetActualType_withParameterizedType_expectNewParameterizedTypeReturned() {
        Type parameterizedType = new ParameterizedType() {};
        Type parentType = new Type() {};
        Class<?> rawParentClass = TypeInfoFactory.class;
        Type actualResult = TypeInfoFactory.getActualType(parameterizedType, parentType, rawParentClass);
        assertNotNull(actualResult);
        assertTrue(actualResult instanceof ParameterizedType);
    }

    @Test
    public void testGetActualType_withGenericArrayType_expectNewArrayTypeReturned() {
        Type genericArrayType = new GenericArrayType() {};
        Type parentType = new Type() {};
        Class<?> rawParentClass = TypeInfoFactoryTest.class;
        Type actualResult = TypeInfoFactory.getActualType(genericArrayType, parentType, rawParentClass);
        assertNotNull(actualResult);
        assertTrue(actualResult instanceof GenericArrayType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetActualType_withTypeVariable_expectUnsupportedOperationException() {
        Type typeVariable = new TypeVariable<?>() {};
        Type parentType = new ParameterizedType() {};
        Class<?> rawParentClass = TypeInfoFactory.class;
        TypeInfoFactory.getActualType(typeVariable, parentType, rawParentClass);
    }

    @Test
    public void testGetActualType_withWildcardType_expectNewTypeReturned() {
        Type wildcardType = new WildcardType() {};
        Type parentType = new ParameterizedType() {};
        Class<?> rawParentClass = TypeInfoFactory.class;
        Type actualResult = TypeInfoFactory.getActualType(wildcardType, parentType, rawParentClass);
        assertNotNull(actualResult);
        assertFalse(actualResult instanceof WildcardType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetActualType_withUnsupportedType_expectIllegalArgumentException() {
        Type unsupportedType = new Type() {};
        Type parentType = new Type() {};
        Class<?> rawParentClass = TypeInfoFactoryTest.class;
        TypeInfoFactory.getActualType(unsupportedType, parentType, rawParentClass);
    }

    @Test
    public void testExtractRealTypes_withActualTypeArguments_expectNewTypesArrayReturned() {
        Type[] actualTypeArguments = new Type[] {new Type() {}, new ParameterizedType() {}};
        Type parentType = new Type() {};
        Class<?> rawParentClass = TypeInfoFactory.class;
        Type[] actualResult = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
        assertNotNull(actualResult);
        assertEquals(actualTypeArguments.length, actualResult.length);
    }

    @Test(expected = NullPointerException.class)
    public void testExtractRealTypes_withNullActualTypeArguments_expectNullPointerException() {
        TypeInfoFactory.extractRealTypes(null, null, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIndex_withNoMatchingType_expectIllegalStateException() {
        TypeVariable<?>[] types = new TypeVariable<?>[] {};
        TypeVariable<?> type = new TypeVariable<?>() {};
        TypeInfoFactory.getIndex(types, type);
    }

    @Test
    public void testGetIndex_withMatchingType_expectIndexReturned() {
        TypeVariable<?>[] types = new TypeVariable<?>[] {new TypeVariable<?>() {}};
        TypeVariable<?> type = types[0];
        int actualResult = TypeInfoFactory.getIndex(types, type);
        assertEquals(0, actualResult);
    }

}
