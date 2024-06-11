import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import com.google.gson.TypeInfoImpl;
import com.google.gson.ParameterizedTypeImpl;
import com.google.gson.GenericArrayTypeImpl;
import com.google.gson.TypeVariable;
import com.google.gson.reflect.TypeToken;
import com.google.common.base.Preconditions;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

  @org.junit.Test
  public void testTypeInfoForArray() {
    Type type = new int[5].getClass().getComponentType();
    TypeInfoArray typeInfo = TypeInfoFactory.getTypeInfoForArray(type);
    assertNotNull(typeInfo);
    assertEquals(TypeInfo.ARRAY, typeInfo.type);
    assertEquals(int.class, typeInfo.elementType);
  }

  @org.junit.Test
  public void testTypeInfoForField() throws NoSuchFieldException {
    String testClass = "com.example.TestClass";
    Class<?> clazz = Class.forName(testClass);
    Field field = clazz.getDeclaredField("field");
    Type fieldType = field.getGenericType();
    Type parentType = clazz.getGenericSuperclass();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, parentType);

    assertEquals(TypeInfo.FIELD, typeInfo.type);
    assertEquals(String.class, typeInfo.actualType);
  }

  @org.junit.Test
  public void testGetActualTypeForClass() {
    Type rawParentClass = String[].class;
    Type parentType = new TypeToken<String[]>() {}.getType();
    Type actualType = TypeInfoFactory.getActualType(String[].class, parentType, rawParentClass);
    assertEquals(String[].class, actualType);

    rawParentClass = new TypeToken<List<String>>() {}.getType();
    parentType = new TypeToken<List<String>>() {}.getType();
    actualType = TypeInfoFactory.getActualType(ArrayList.class, parentType, rawParentClass);
    assertEquals(ArrayList.class, actualType);
  }

  @org.junit.Test
  public void testGetActualTypeForParameterizedType() {
    Type rawParentClass = new TypeToken<Map<String, List<String>>>() {}.getType();
    Type parentType = new TypeToken<Map<String, List<String>>>() {}.getType();
    Type[] actualTypeArguments = new Type[] { String.class, new TypeToken<List<String>>() {}.getType() };
    Type actualType = new ParameterizedTypeImpl(Map.class, actualTypeArguments, parentType);
    Type actualTypeFromFactory = TypeInfoFactory.getActualType(actualType, parentType, rawParentClass);
    assertEquals(actualType, actualTypeFromFactory);
  }

  @org.junit.Test
  public void testGetActualTypeForGenericArrayType() {
    Type rawParentClass = new TypeToken<List<List<String>>>() {}.getType();
    Type parentType = new TypeToken<List<List<String>>>() {}.getType();
    Type componentType = new TypeToken<List<String>>() {}.getType();
    Type genericArrayType = new GenericArrayTypeImpl(componentType);
    Type actualType = TypeInfoFactory.getActualType(genericArrayType, parentType, rawParentClass);
    assertEquals(List[].class, actualType);
  }

  @org.junit.Test
  public void testGetActualTypeForTypeVariable() throws NoSuchFieldException {
    String testClass = "com.example.TestClassWithTypeVariables";
    Class<?> clazz = Class.forName(testClass);
    Field field = clazz.getDeclaredField("field");
    Type fieldType = field.getGenericType();
    Type parentType = clazz.getGenericSuperclass();
    TypeVariable<?> fieldTypeVariable = (TypeVariable<?>) fieldType;
    int index = TypeInfoFactory.getIndex(clazz.getTypeParameters(), fieldTypeVariable);
    Type actualType = TypeInfoFactory.getActualType(fieldType, parentType, clazz);
    assertEquals(clazz.getTypeParameters()[index], actualType);
  }

  @org.junit.Test(expected = UnsupportedOperationException.class)
  public void testGetActualTypeForWildcardType() {
    Type rawParentClass = new TypeToken<List<?>>() {}.getType();
    Type parentType = new TypeToken<List<?>>() {}.getType();
    Type wildcardType = new WildcardType(null, null);
    Type actualType = TypeInfoFactory.getActualType(wildcardType, parentType, rawParentClass);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testGetActualTypeForNullType() {
    Type rawParentClass = null;
    Type parentType = new TypeToken<List<String>>() {}.getType();
    Type actualType = TypeInfoFactory.getActualType(null, parentType, rawParentClass);
  }
}