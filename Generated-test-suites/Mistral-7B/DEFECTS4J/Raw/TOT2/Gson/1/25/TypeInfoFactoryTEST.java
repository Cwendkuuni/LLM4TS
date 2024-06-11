
package com.example;

import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.TypeInfoFactory;
import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import java.lang.reflect.Field;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[5].getClass();
    TypeInfoArray actualTypeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
    TypeInfoArray expectedTypeInfoArray = new TypeInfoArray(type);
    assertEquals(expectedTypeInfoArray, actualTypeInfoArray);
  }

  @Test
  public void testTypeInfoForEmptyArray() {
    Type type = new Type[] {};
    assertNull(TypeInfoFactory.getTypeInfoForArray(type));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForNonArrayType() {
    Type type = String.class;
    assertNull(TypeInfoFactory.getTypeInfoForArray(type));
  }

  @Test
  public void testTypeInfoForField() throws NoSuchFieldException {
    Field field = Integer.TYPE.getField("Value");
    Type typeDefiningF = Integer.TYPE;
    Type actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
    TypeInfo expectedTypeInfo = new TypeInfo(Integer.TYPE);
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test(expected = NoSuchFieldException.class)
  public void testTypeInfoForNonExistentField() throws NoSuchFieldException {
    Field field = Integer.TYPE.getField("NonExistentField");
    Type typeDefiningF = Integer.TYPE;
    TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
  }

  @Test(expected = IllegalAccessException.class)
  public void testTypeInfoForNonAccessibleField() throws NoSuchFieldException {
    Field field = privateField; // A private field
    Type typeDefiningF = Integer.TYPE;
    TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForNonFieldType() {
    Field field = Integer.TYPE.getField("Value");
    Type typeDefiningF = String.class;
    assertNull(TypeInfoFactory.getTypeInfoForField(field, typeDefiningF));
  }

}
