import static org.junit.Assert.*;
import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import com.google.gson.TypeInfoImpl;
import com.google.gson.ParameterizedTypeImpl;
import com.google.gson.GenericArrayTypeImpl;
import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeVariables;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.GenericArrayType;
import java.util.Arrays;

public class TypeInfoFactoryTest {

  @SuppressWarnings("unchecked")
  private static <T> Type getGenericType(Class<T> clazz) {
    return TypeToken.get(clazz).getType();
  }

  @SuppressWarnings("RawType")
  @org.junit.Test
  public void testTypeInfoForArray() {
    Type arrayType = new int[5].getClass().getComponentType();
    TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(arrayType);
    assertEquals(TypeInfo.ARRAY, typeInfoArray.getType());
  }

  @org.junit.Test
  public void testTypeInfoForField() throws NoSuchFieldException, IllegalAccessException {
    Class<Integer> clazz = Integer.class;
    Field field = clazz.getDeclaredField("value");
    Type fieldType = field.getGenericType();
    Type parentType = clazz;
    Class<?> rawParentClass = clazz;
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
    assertEquals(Integer.TYPE, typeInfo.getType());

    clazz = List.class;
    Type listType = new TypeToken<List<Integer>>() {}.getType();
    field = clazz.getDeclaredField("list");
    parentType = listType;
    rawParentClass = clazz;
    typeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
    assertEquals(TypeInfo.LIST, typeInfo.getType());

    clazz = Map.class;
    Type mapType = new TypeToken<Map<String, Integer>>() {}.getType();
    field = clazz.getDeclaredField("map");
    parentType = mapType;
    rawParentClass = clazz;
    typeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
    assertEquals(TypeInfo.MAP, typeInfo.getType());

    clazz = TypeVariable.class;
    Type typeVariableType = new TypeToken<TypeVariable<String>>() {}.getType();
    field = clazz.getDeclaredField("name");
    parentType = typeVariableType;
    rawParentClass = clazz;
    typeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
    assertEquals(TypeInfo.TYPE_VARIABLE, typeInfo.getType());

    clazz = TypeVariable.class;
    Type wildcardType = new TypeToken<List<?>>() {}.getType();
    field = clazz.getDeclaredField("values");
    parentType = wildcardType;
    rawParentClass = clazz;
    typeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);
    assertEquals(TypeInfo.WILDCARD, typeInfo.getType());
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testgetTypeInfoForFieldWithInvalidType() throws NoSuchFieldException, IllegalAccessException {
    Class<Integer> clazz = Integer.class;
    Field field = clazz.getDeclaredField("toString");
    Type parentType = clazz;
    Class<?> rawParentClass = clazz;
    TypeInfoFactory.getTypeInfoForField(field, parentType);
  }

  @org.junit.Test
  public void testGetActualType() {
    // Test for Class<?>
    Type intType = Integer.TYPE;
    Type actualType = TypeInfoFactory.getActualType(intType, null, null);
    assertEquals(intType, actualType);

    // Test for ParameterizedType
    TypeList<Integer> listType = new TypeList<Integer>() {};
    Type[] actualTypeArguments = new Type[] {Integer.TYPE};
    Type rawType = listType.getClass();
    Type ownerType = null;
    Type result = TypeInfoFactory.getActualType(listType, null, null);
    assertEquals(new ParameterizedTypeImpl(rawType, actualTypeArguments, ownerType), result);

    // Test for GenericArrayType
    Type[] intArrayComponentTypes = new Type[] {Integer.TYPE};
    Type genericArrayType = new GenericArrayTypeImpl(Integer.TYPE, intArrayComponentTypes);
    Type result1 = TypeInfoFactory.getActualType(genericArrayType, null, null);
    assertEquals(genericArrayType, result1);

    // Test for TypeVariable<T>
    TypeVariable<String> typeVariable = new TypeVariable<>("T") {};
    Type[] actualTypeArguments = new Type[] {String.class};
    Type ownerType1 = TypeToken.get(String[].class).getType();
    Type result2 = TypeInfoFactory.getActualType(typeVariable, null, null);
    assertEquals(new TypeVariable<>("T") {}, result2);

    TypeList<List<String>> listOfListTypeVariable = new TypeList<List<TypeVariable<String>>>() {};
    ownerType1 = TypeToken.getParameterized(List.class, TypeVariable.class).getType();
    Type[] actualTypeArguments1 = new Type[] {TypeVariable.class};
    result = TypeInfoFactory.getActualType(listOfListTypeVariable, null, null);
    assertEquals(new ParameterizedTypeImpl(
        new ParameterizedTypeImpl(List.class, new Type[] {TypeVariable.class}),
        actualTypeArguments1, ownerType1), result);

    // Test for WildcardType
    Type wildcardType = new WildcardType(null, new Type[] {Object.class, Integer.class});
    Type result3 = TypeInfoFactory.getActualType(wildcardType, null, null);
    assertEquals(null, result3);

    // Test for invalid type
    Type intWrapperType = Integer.class;
    Type result4 = TypeInfoFactory.getActualType(intWrapperType, null, null);
    fail("Unexpected result: " + result4);
  }

  @org.junit.Test(expected = UnsupportedOperationException.class)
  public void testInvalidGetActualType() {
    Type listType = new TypeToken<List<String>>() {}.getType();
    TypeVariable<String> stringTypeVariable = new TypeVariable<>("T") {};
    Type result = TypeInfoFactory.getActualType(stringTypeVariable, listType, null);
  }

  @org.junit.Test
  public void testExtractRealTypes() {
    Type listType = new TypeToken<List<Integer>>() {}.getType();
    Type[] actualTypeArguments = new Type[] {Integer.TYPE};
    Type ownerType = null;
    Type[] result = TypeInfoFactory.extractRealTypes(listType.getActualTypeArguments(), null, null);
    assertEquals(Arrays.asList(actualTypeArguments), Arrays.asList(result));

    Type mapType = new TypeToken<Map<String, Integer>>() {}.getType();
    String keyType = new TypeToken<String>() {}.getType();
    Integer valueType = new TypeToken<Integer>() {}.getType();
    Type[] actualTypeArguments1 = new Type[] {keyType, valueType};
    ownerType = new TypeToken<Map<String, Object>>() {}.getType();
    Type[] result1 = TypeInfoFactory.extractRealTypes(mapType.getActualTypeArguments(), null, null);
    assertEquals(Arrays.asList(actualTypeArguments1), Arrays.asList(result1));
  }

  @org.junit.Test(expected = UnsupportedOperationException.class)
  public void testInvalidExtractRealTypes() {
    Type listType = new TypeToken<List<String>>() {}.getType();
    Type[] actualTypeArguments = new Type[] {Integer.TYPE};
    Type parentType = null;
    Type[] result = TypeInfoFactory.extractRealTypes(listType, parentType, null);
  }

  @org.junit.Test
  public void testGetIndex() {
    TypeVariable<String> typeVariable1 = new TypeVariable<>("A") {};
    TypeVariable<String> typeVariable2 = new TypeVariable<>("B") {};
    TypeVariable<String> typeVariable3 = new TypeVariable<>("C") {};
    TypeVariable<String>[] types = new TypeVariable<?>[] {typeVariable1, typeVariable2, typeVariable3};
    TypeVariable<String> typeVariable = new TypeVariable<>("B") {};
    int index = TypeInfoFactory.getIndex(types, typeVariable);
    assertEquals(1, index);
  }
}