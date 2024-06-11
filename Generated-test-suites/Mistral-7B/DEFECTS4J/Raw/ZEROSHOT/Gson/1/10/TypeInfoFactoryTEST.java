import com.google.gson.TypeInfoFactory;
import com.google.gson.TypeInfo;
import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeUtils;
import org.junit.Test;
import org.junit.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;

public class TypeInfoFactoryTest {

    @Test
    public void testTypeInfoForArray() {
        Type type = new TypeToken<int[]>() {}.getType();
        TypeInfo expectedTypeInfo = new TypeInfoArray(type);
        TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(type);
        Assert.assertEquals(expectedTypeInfo.getType(), actualTypeInfo.getType());
    }

    @Test
    public void testTypeInfoForField() throws NoSuchFieldException {
        Class<List<Integer>> clazz = new ArrayList<Integer>().getClass();
        Field listField = clazz.getDeclaredField("list");
        Type listType = listField.getGenericType();
        Type definingType = TypeToken.get(clazz).getType();
        TypeInfo expectedTypeInfo = new TypeInfo(new TypeToken<List<Integer>>() {}.getType());
        TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(listField, listType, definingType);
        Assert.assertEquals(expectedTypeInfo.getType(), actualTypeInfo.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTypeInfo() {
        Class<List<List<Integer>>> clazz = new ArrayList<List<Integer>>().getClass();
        Field listField = clazz.getDeclaredField("list");
        Type listType = listField.getGenericType();
        Type definingType = TypeToken.get(clazz).getType();
        TypeInfoFactory.getTypeInfoForField(listField, listType, definingType);
    }

    @Test
    public void testGetActualTypeForClass() {
        Class<List<Integer>> clazz = new ArrayList<Integer>().getClass();
        TypeInfo expectedTypeInfo = new TypeInfo(TypeToken.get(clazz).getType());
        Type actualType = TypeInfoFactory.getActualType(TypeToken.get(clazz).getType(), null, clazz);
        TypeInfo actualTypeInfo = new TypeInfo(actualType);
        Assert.assertEquals(expectedTypeInfo.getType(), actualTypeInfo.getType());
    }

    @Test
    public void testGetActualTypeForParameterizedType() {
        Class<List<List<Integer>>> clazz = new ArrayList<List<Integer>>().getClass();
        Type definingType = TypeToken.get(clazz).getType();
        Type listType = new TypeToken<List<Integer>>() {}.getType();
        Type[] actualTypeArguments = new Type[] {listType};
        Type rawType = new TypeToken<List<List<Integer>>>() {}.getType().getComponentType();
        TypeInfo expectedTypeInfo = new TypeInfo(new TypeParameterizedTypeImpl(rawType, actualTypeArguments, definingType));
        TypeInfo actualTypeInfo = (TypeInfo) TypeInfoFactory.getActualType(listType, definingType, clazz.getComponentType());
        Assert.assertEquals(expectedTypeInfo.getType(), actualTypeInfo.getType());
    }

    @Test
    public void testGetActualTypeForGenericArrayType() {
        Type type = new TypeToken<int[]>() {}.getType();
        Type rawType = TypeUtils.getRawType(type);
        Type componentType = TypeToken.get(int[].class).getType();
        TypeInfo expectedTypeInfo = new TypeInfoArray(new TypeInfo(componentType));
        TypeInfo actualTypeInfo = (TypeInfo) TypeInfoFactory.getActualType(componentType, null, rawType);
        Assert.assertEquals(expectedTypeInfo.getType(), actualTypeInfo.getType());
    }

    @Test
    public void testGetActualTypeForTypeVariable() {
        Class<List<List<Integer>>> clazz = new ArrayList<List<Integer>>().getClass();
        Type fieldTypeVariable = clazz.getGenericType();
        Type definingType = TypeToken.get(clazz).getType();
        Type[] classTypeVariables = definingType.getTypeParameters();
        int indexOfActualTypeArgument = 0;
        Type actualTypeArgument = new TypeToken<List<Integer>>() {}.getType();
        TypeInfo expectedTypeInfo = new TypeInfo(actualTypeArgument);
        TypeInfo actualTypeInfo = (TypeInfo) TypeInfoFactory.getTypeInfoForField(fieldTypeVariable, definingType).getActualType();
        Assert.assertEquals(expectedTypeInfo.getType(), actualTypeInfo.getType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetActualTypeForWildcardType() {
        Type type = TypeToken.get(List.class).getType();
        Type parentType = new TypeToken<ArrayList<List<Integer>>>() {}.getType();
        Class<?> rawParentClass = ArrayList.class;
        TypeInfoFactory.getActualType(new WildcardType(TypeUtils.getTypeBounds(type, parentType)), parentType, rawParentClass);
    }

    @Test
    public void testExtractRealTypes() {
        Type definingType = new TypeToken<List<List<List<Integer>>>>() {}.getType();
        Type[] actualTypeArguments = new Type[] {new TypeToken<List<Integer>>() {}.getType(), new TypeToken<List<Integer>>() {}.getType(), new TypeToken<List<Integer>>() {}.getType()};
        Type[] expectedTypes = new Type[] {new TypeToken<List<Integer>>() {}.getType(), new TypeToken<List<Integer>>() {}.getType(), new TypeToken<List<Integer>>() {}.getType()};
        TypeInfo[] expectedTypeInfos = new TypeInfo[3];
        for (int i = 0; i < 3; i++) {
            expectedTypeInfos[i] = new TypeInfo(new TypeToken<List<Integer>>() {}.getType());
        }
        TypeInfo[] actualTypeInfos = TypeInfoFactory.extractRealTypes(actualTypeArguments, definingType, ArrayList.class);
        Assert.assertArrayEquals(expectedTypes, actualTypes(actualTypeInfos));
    }

    @Test
    public void testGetIndexOfTypeVariable() {
        Type definingType = new TypeToken<List<List<List<Integer>>>>() {}.getType();
        TypeVariable<List<List<List<Integer>>>> listTypeVariable = definingType.getTypeParameters()[0];
        TypeVariable<List<Integer>> listIntTypeVariable = new TypeToken<List<Integer>>() {}.getType().getTypeParameters()[0];
        int expectedIndex = 0;
        int actualIndex = TypeInfoFactory.getIndex(new TypeVariable<?>[] {listIntTypeVariable, listIntTypeVariable, listIntTypeVariable}, listTypeVariable);
        Assert.assertEquals(expectedIndex, actualIndex);
    }
}