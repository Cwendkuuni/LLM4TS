import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(new String[5]);
        assertEquals(String.class, typeInfoArray.getActualType());
    }

    @Test
    public void testGetTypeInfoForField() throws NoSuchFieldException {
        Class<?> classDefiningF = TypeInfoFactory.class;
        Field field = classDefiningF.getDeclaredField("typeDefiningF");
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, classDefiningF);
        assertEquals(TypeInfo.class, typeInfo.getActualType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeInfoForFieldWithInvalidType() throws NoSuchFieldException {
        Class<?> classDefiningF = TypeInfoFactory.class;
        Field field = classDefiningF.getDeclaredField("typeDefiningF");
        TypeInfoFactory.getTypeInfoForField(field, String.class);
    }

    @Test
    public void testGetActualTypeForTypeVariable() throws NoSuchFieldException {
        Class<?> classDefiningF = TypeInfoFactory.class;
        Field field = classDefiningF.getDeclaredField("typeToEvaluate");
        Type typeToEvaluate = field.getGenericType();
        Type parentType = classDefiningF;
        Class<?> rawParentClass = TypeInfoFactory.class;
        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertEquals(ParameterizedTypeImpl.class, actualType.getClass());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetActualTypeForTypeVariableWithoutParameterizedType() throws NoSuchFieldException {
        Class<?> classDefiningF = TypeInfoFactory.class;
        Field field = classDefiningF.getDeclaredField("typeToEvaluateWithoutParameterizedType");
        Type typeToEvaluate = field.getGenericType();
        Type parentType = classDefiningF;
        Class<?> rawParentClass = TypeInfoFactory.class;
        TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    }

    @Test
    public void testExtractRealTypes() {
        Type[] actualTypeArguments = {String.class, Integer.class};
        Type parentType = TypeInfoFactory.class;
        Class<?> rawParentClass = TypeInfoFactory.class;
        Type[] realTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
        assertEquals(String.class, realTypes[0]);
        assertEquals(Integer.class, realTypes[1]);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIndexForNonExistentTypeVariable() {
        TypeVariable<?>[] types = {TypeInfoFactory.class.getTypeParameters()[0]};
        TypeVariable<?> type = TypeInfoFactory.class.getTypeParameters()[1];
        TypeInfoFactory.getIndex(types, type);
    }
}