package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[5].getClass().getComponentType();
    TypeInfoArray typeInfo = TypeInfoFactory.getTypeInfoForArray(type);
    assertEquals("array", typeInfo.componentType.getSimpleName());
    assertEquals(int.class, typeInfo.componentType.getComponentType());
  }

  @Test
  public void testTypeInfoForField_ClassType() {
    // Testing for a simple class type
    Class<String> rawParentClass = String.class;
    Class<Integer> clazz = Integer.class;
    Field field = clazz.getDeclaredField("value");
    Type type = field.getGenericType();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, type, rawParentClass);
    assertNotNull(typeInfo);
    assertEquals(clazz, typeInfo.type);
  }

  @Test
  public void testTypeInfoForField_ParameterizedType() {
    // Testing for a simple parameterized type
    Class<Map<String, List<Integer>>> rawParentClass = Map.class;
    Type mapType = Map.class;
    TypeList listType = new TypeToken<List<Integer>>() {}.getType();
    Type parameterizedType = new ParameterizedTypeImpl(mapType, new Type[] { String.class, listType }, null);
    Field field = Map.Entry.class.getDeclaredField("value");
    Type type = field.getGenericType();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, type, rawParentClass);
    assertNotNull(typeInfo);
    assertEquals(Map.Entry.class, typeInfo.type);

    // Testing for a parameterized type with generic array
    Class<List<List<Integer>>> rawParentClassArray = List.class;
    Type listTypeArray = new TypeToken<List<List<Integer>>>() {}.getType();
    Type parameterizedTypeArray =
        new ParameterizedTypeImpl(listTypeArray.getComponentType(), null, null);
    Type listTypeParameterized = new ParameterizedTypeImpl(List.class, new Type[] { Integer.class }, null);
    Type parameterizedTypeMap = new ParameterizedTypeImpl(Map.class, new Type[] { String.class, listTypeParameterized }, null);
    Field fieldArray = Map.Entry.class.getDeclaredField("value");
    Type typeArray = fieldArray.getGenericType();
    TypeInfo typeInfoArray = TypeInfoFactory.getTypeInfoForField(fieldArray, typeArray, rawParentClassArray);
    assertNotNull(typeInfoArray);
    assertEquals(List.class, typeInfoArray.type.getComponentType());
    assertEquals(List.class, typeInfoArray.type);
  }

  @Test
  public void testTypeInfoForField_TypeVariable() {
    // Testing for a type variable
    Class<Map<String, List<A>>> rawParentClass = Map.class;
    Type mapType = Map.class;
    TypeList listType = new TypeToken<List<A>>() {}.getType();
    Type parameterizedType = new ParameterizedTypeImpl(mapType, new Type[] { String.class, listType }, null);
    TypeVariable<A> typeVariable = listType.getType()
        .getDeclaredElement()
        .getTypeParameters()[0];
    TypeVariable<A> fieldTypeVariable = new TypeVariable<A>("A") {}.getType();
    Field field = Map.Entry.class.getDeclaredField("value");
    Type type = field.getGenericType();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, type, rawParentClass);
    assertNotNull(typeInfo);
    assertEquals(typeVariable, typeInfo.typeVariable);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForField_UnexpectedType() {
    // Testing for an unexpected type
    Class<Map<String, List<A>>> rawParentClass = Map.class;
    Type mapType = Map.class;
    TypeList listType = new TypeToken<List<A>>() {}.getType();
    Type parameterizedType = new ParameterizedTypeImpl(mapType, new Type[] { String.class, listType }, null);
    Field field = Object.class.getDeclaredField("toString");
    Type type = field.getGenericType();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, type, rawParentClass);
  }
}