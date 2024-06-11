
package com.google.gson.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.junit.jupiter.api.Test;

import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.TypeNameSerializer;
import com.google.gson.stream.TypeNameToken;

import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoFactory;
import com.google.gson.internal.Preconditions;
import com.google.gson.reflect.TypeUtils;
import com.google.gson.stream.TypeNameToken;

/**
 * Test class for TypeInfoFactory.
 */
public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type intArrayType = new TypeToken<int[]>() {}.getType();
    TypeInfo expectedTypeInfo = new TypeInfoArray(intArrayType);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(intArrayType);
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test
  public void testTypeInfoForField() throws NoSuchFieldException, IllegalAccessException {
    Object obj = new Object() {
      int i;
    };
    Field field = Object.class.getDeclaredField("i");
    Type fieldType = field.getType();
    TypeInfo expectedTypeInfo = new TypeInfo(fieldType);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, obj.getClass().getTypeName());
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForArrayWithInvalidType() {
    Type nonArrayType = new TypeToken<String>() {}.getType();
    assertThrows(IllegalArgumentException.class, () -> TypeInfoFactory.getTypeInfoForArray(nonArrayType));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForFieldWithInvalidType() throws NoSuchFieldException {
    Object obj = new Object() {
      int i;
    };
    Field field = Object.class.getDeclaredField("j"); // Non-existent field
    Type fieldType = obj.getClass().getTypeName();
    assertThrows(IllegalArgumentException.class, () -> TypeInfoFactory.getTypeInfoForField(field, fieldType));
  }

}
