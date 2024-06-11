package com.example;

import static org.junit.Assert.*;

import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import com.google.gson.TypeInfoFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type intArrayType = new TypeToken<int[]>() {}.getType();
    TypeInfoArray intInfoArray = TypeInfoFactory.getTypeInfoForArray(intArrayType);
    assertEquals(intArrayType, intInfoArray.getType());
  }

  @Test
  public void testTypeInfoForSimpleField() throws NoSuchFieldException {
    Class<Integer> clazz = Integer.class;
    int field = 0;
    Field intField = clazz.getField("value");
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(intField, clazz);
    assertEquals(Integer.TYPE, info.getType());
  }

  @Test
  public void testTypeInfoForParameterizedField() throws NoSuchFieldException {
    Class<Map<String, Integer>> clazz = Map.class;
    Map<String, Integer> map = new HashMap<>();
    Field mapField = clazz.getField("entrySet");
    Type mapEntryType = new TypeToken<Map.Entry<String, Integer>>() {}.getType();
    Type actualType = getActualType(mapField.getGenericType(), clazz, clazz);
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(mapField, clazz);
    assertEquals(Map.Entry.class, info.getType());
    assertEquals(actualType, info.getType().getComponentType());
  }

  @Test
  public void testTypeInfoForTypeVariable() {
    TypeVariable<Number> number = new TypeVariable<Number>() {};
    Class<?> clazz = Number.class;
    Field numberField = clazz.getField("TYPE");
    Type actualType = getActualType(numberField.getGenericType(), clazz, clazz);
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(numberField, clazz);
    assertEquals(Number.class, info.getType());
    assertEquals(actualType, info.getType().getType());
  }

  @Test
  public void testTypeInfoForWildcardType() {
    TypeList<?> typeList = new TypeToken<List<?>>() {}.getType();
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(typeList, Object.class);
    assertEquals(List.class, info.getType());
    assertNotNull(info.getType().getUpperBounds()[0]);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForIncompleteType() {
    Class<?> incompleteClass = new Class<>("IncompleteClass") {};
    Field incompleteField = incompleteClass.getField("value");
    Type actualType = getActualType(incompleteField.getGenericType(), incompleteClass, incompleteClass);
    fail("Should throw an UnsupportedOperationException for incomplete types.");
  }

  @Test(expected = NoSuchFieldException.class)
  public void testTypeInfoForNonExistentField() throws NoSuchFieldException {
    Class<String> clazz = String.class;
    Field nonExistentField = clazz.getField("nonExistentField");
    Type actualType = getActualType(nonExistentField.getGenericType(), clazz, clazz);
    fail("Should throw a NoSuchFieldException for non-existent fields.");
  }

  private Type getActualType(Type type, Type parentType, Class<?> rawParentClass) {
    // Implement the getActualType method here.
  }
}