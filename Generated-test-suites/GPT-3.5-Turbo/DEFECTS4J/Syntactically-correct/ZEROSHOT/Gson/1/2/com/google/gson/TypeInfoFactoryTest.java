import org.junit.Test;

import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        TypeInfoArray typeInfo = TypeInfoFactory.getTypeInfoForArray(int[].class);
        assertEquals(int[].class, typeInfo.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeInfoForArrayInvalid() {
        TypeInfoFactory.getTypeInfoForArray(int.class);
    }

    @Test
    public void testGetTypeInfoForField() throws NoSuchFieldException {
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(
                TypeInfoFactory.class.getDeclaredField("getTypeInfoForArray"),
                TypeInfoFactory.class);
        assertEquals(TypeInfoArray.class, typeInfo.getActualType());
    }

    @Test
    public void testGetTypeInfoForFieldGeneric() throws NoSuchFieldException {
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(
                TypeInfoFactory.class.getDeclaredField("getTypeInfoForField"),
                TypeInfoFactory.class);
        assertEquals(TypeInfo.class, typeInfo.getActualType());
    }

    @Test
    public void testGetActualTypeClass() {
        Type actualType = TypeInfoFactory.getActualType(String.class, TypeInfoFactory.class, TypeInfoFactory.class);
        assertEquals(String.class, actualType);
    }

    @Test
    public void testGetActualTypeParameterizedType() {
        Type actualType = TypeInfoFactory.getActualType(
                new TypeInfoFactory.ParameterizedTypeImpl(Iterable.class, new Type[]{String.class}, null),
                TypeInfoFactory.class, TypeInfoFactory.class);
        assertEquals(Iterable.class, actualType);
    }

    @Test
    public void testGetActualTypeGenericArrayType() {
        Type actualType = TypeInfoFactory.getActualType(
                new TypeInfoFactory.GenericArrayTypeImpl(String.class),
                TypeInfoFactory.class, TypeInfoFactory.class);
        assertEquals(String[].class, actualType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetActualTypeInvalidType() {
        TypeInfoFactory.getActualType(new Object(), TypeInfoFactory.class, TypeInfoFactory.class);
    }

    @Test
    public void testExtractRealTypes() {
        Type[] actualTypeArguments = {String.class, Integer.class};
        Type parentType = new TypeInfoFactory.ParameterizedTypeImpl(
                Iterable.class, actualTypeArguments, null);
        Class<?> rawParentClass = TypeInfoFactory.class;
        Type[] retTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
        assertArrayEquals(new Type[]{String.class, Integer.class}, retTypes);
    }

    @Test
    public void testGetIndex() {
        TypeVariable<?>[] types = TypeInfoFactory.class.getTypeParameters();
        int index = TypeInfoFactory.getIndex(types, types[0]);
        assertEquals(0, index);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIndexNotFound() {
        TypeVariable<?>[] types = TypeInfoFactory.class.getTypeParameters();
        TypeInfoFactory.getIndex(types, String.class);
    }
}