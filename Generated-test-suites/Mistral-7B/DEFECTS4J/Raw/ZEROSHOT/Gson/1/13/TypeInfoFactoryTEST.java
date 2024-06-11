import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import com.google.gson.TypeInfoFactory;
import com.google.gson.TypeVariable;
import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class TypeInfoFactoryTest {

    @Test
    public void testTypeInfoForArray() {
        Type type = new int[0].getClass().getComponentType();
        TypeInfo expectedTypeInfo = new TypeInfoArray(type);
        TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(type);
        Assert.assertEquals(expectedTypeInfo, actualTypeInfo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForNonArrayType() {
        Field field = Integer.class.getDeclaredField("value");
        Type type = field.getType();
        TypeInfoFactory.getTypeInfoForArray(type);
    }

    @Test
    public void testTypeInfoForField() throws NoSuchFieldException, IllegalAccessException {
        Class<List<String>> clazz = List.class.getTypeName().replaceAll("\\.\\$", "").replaceAll("List", "").getClass();
        Field listSizeField = clazz.getDeclaredField("size");
        Type type = listSizeField.getGenericType();
        Type definingClassType = clazz.getGenericSuperclass();

        TypeInfo expectedTypeInfo = new TypeInfo(Integer.TYPE);
        TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(listSizeField, definingClassType);
        Assert.assertEquals(expectedTypeInfo, actualTypeInfo);
    }

    @Test
    public void testGetActualTypeForClass() {
        Class<String> stringClass = String.class;
        TypeInfo expectedTypeInfo = new TypeInfo(String.class);
        Type parentType = new TypeToken<List<String>>() {}.getType();
        Type definingClassType = List.class;

        TypeInfo actualTypeInfo = (TypeInfo) TypeInfoFactory.getActualType(stringClass.getTypeName(), parentType, definingClassType);
        Assert.assertEquals(expectedTypeInfo, actualTypeInfo);
    }

    @Test
    public void testGetActualTypeForParameterizedType() {
        Class<List<String>> listClass = List.class.getTypeName().replaceAll("\\.\\$", "").replaceAll("List", "").getClass();
        Type listType = listClass.getGenericSuperclass();
        Type parameterizedType = new TypeToken<List<String>>() {}.getType();
        Type rawParentClass = List.class;

        TypeInfo expectedTypeInfo = new TypeInfo(List.class);
        TypeInfo actualTypeInfo = (TypeInfo) TypeInfoFactory.getActualType(listType, parameterizedType, rawParentClass);
        Assert.assertEquals(expectedTypeInfo, actualTypeInfo);
    }

    @Test
    public void testGetActualTypeForGenericArrayType() {
        Class<String[]> stringArrayClass = String[].class;
        Type stringArrayType = stringArrayClass.getComponentType();
        Type parentType = new TypeToken<Object[]>() {}.getType();
        Type rawParentClass = Object[].class;

        TypeInfo expectedTypeInfo = new TypeInfoArray(String.class);
        TypeInfo actualTypeInfo = (TypeInfo) TypeInfoFactory.getActualType(stringArrayType, parentType, rawParentClass);
        Assert.assertEquals(expectedTypeInfo, actualTypeInfo);
    }

    @Test
    public void testGetActualTypeForTypeVariable() throws NoSuchFieldException, IllegalAccessException {
        Class<List<String>> clazz = List.class.getTypeName().replaceAll("\\.\\$", "").replaceAll("List", "").getClass();
        Field listSizeField = clazz.getDeclaredField("size");
        Type type = listSizeField.getGenericType();
        Type definingClassType = clazz.getGenericSuperclass();

        TypeVariable<Integer> sizeTypeVariable = definingClassType.getTypeParameters()[0];

        TypeInfo expectedTypeInfo = new TypeInfo(Integer.TYPE);
        TypeInfo actualTypeInfo = (TypeInfo) TypeInfoFactory.getActualType(sizeTypeVariable, type, definingClassType);
        Assert.assertEquals(expectedTypeInfo, actualTypeInfo);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperationExceptionForNonParameterizedType() {
        Type type = new Object()
                .getClass()
                .getSuperclass();
        TypeInfoFactory.getActualType(type, new Object()
                .getClass()
                .getGenericSuperclass(), Object.class);
    }

    @Test
    public void testExtractRealTypes() {
        Class<List<List<String>>> clazz = new TypeToken<List<List<String>>>() {}.getType();
        Type parentType = clazz.getGenericSuperclass();

        Type[] expectedTypes = new Type[]{List.class, List.class};
        Type[] actualTypes = TypeInfoFactory.extractRealTypes(clazz.getTypeParameters(), parentType, clazz);
        Assert.assertArrayEquals(expectedTypes, actualTypes);
    }

    @Test
    public void testGetIndex() {
        TypeVariable<Integer> sizeTypeVariable = new TypeVariable<Integer>("size") {};
        TypeVariable<?>[] typeVariables = new TypeVariable[]{new TypeVariable<Integer>("x") {}, sizeTypeVariable};
        int expectedIndex = 1;
        int actualIndex = TypeInfoFactory.getIndex(typeVariables, sizeTypeVariable);
        Assert.assertEquals(expectedIndex, actualIndex);
    }
}