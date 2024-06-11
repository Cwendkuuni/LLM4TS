package com.example.junit;

import static org.junit.Assert.*;
import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import com.google.gson.TypeInvalidException;
import java.lang.reflect.*;
import org.junit.*;

public class TypeInfoFactoryTest {

  // Previous test cases

  @Test(expected = TypeInvalidException.class)
  public void testTypeInfoForArrayInvalidType() {
    Type intType = Integer.TYPE;
    assertThrows(TypeInvalidException.class, () -> TypeInfoFactory.getTypeInfoForArray(intType));
  }

  @Test
  public void testTypeInfoForArrayNonObjectComponentType() {
    Type intArrayType = new GenericArrayTypeImpl(int[].class);
    assertThrows(ClassCastException.class, () -> {
      TypeInfoArray intTypeInfoArray = TypeInfoFactory.getTypeInfoForArray(intArrayType);
      return (TypeInfoArray) TypeUtils.wrapWithArray(Short.TYPE);
    });
  }

  @Test
  public void testTypeInfoForFieldDoesNotExist() {
    assertThrows(NoSuchFieldException.class, () -> TypeInfoFactory.getTypeInfoForField(null, "Foo.class"));
  }

  @Test
  public void testTypeInfoForFieldTypeIsNotTypeVariable() {
    Field aField = Foo.class.getField("a");
    assertThrows(IllegalArgumentException.class, () -> TypeInfoFactory.getTypeInfoForField(aField, null));
  }

  @Test
  public void testTypeInfoForFieldTypeVariableNotDefinedInSameClass() {
    Field aField = Foo.class.getField("b");
    TypeVariable<A> typeVariable = (TypeVariable<A>) Foo.class.getGenericSuperclass().getTypeParameters()[0];
    assertThrows(IllegalArgumentException.class, () -> TypeInfoFactory.getTypeInfoForField(aField, null));
  }

  @Test
  public void testTypeInfoForFieldTypeVariableNoActualTypeArgument() {
    Field aField = Foo.class.getField("a");
    TypeVariable<A> typeVariable = (TypeVariable<A>) Foo.class.getGenericSuperclass().getTypeParameters()[0];
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(aField, null);
    assertNull(info.getType());
  }

  @Test
  public void testTypeInfoForFieldTypeVariableMultipleActualTypeArguments() {
    Field aField = Foo.class.getField("b");
    TypeVariable<A> typeVariable = (TypeVariable<A>) Foo.class.getGenericSuperclass().getTypeParameters()[0];
    Type typeDefiningF = null;
    try {
      typeDefiningF = new Foo<Integer, Bar>().getClass();
    } catch (NoSuchFieldException e) {
      throw new AssertionError("Failed to create test instance", e);
    }
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(aField, typeDefiningF);
    Type actualType = info.getType();
    assertEquals(Integer.TYPE, actualType);
    assertThrows(IllegalArgumentException.class, () -> TypeInfoFactory.getTypeInfoForField(aField, typeDefiningF));
  }
}