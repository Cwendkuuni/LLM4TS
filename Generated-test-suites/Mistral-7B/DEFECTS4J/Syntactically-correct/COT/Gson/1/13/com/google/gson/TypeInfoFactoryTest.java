import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import com.google.gson.TypeInfoImpl;
import com.google.gson.ParameterizedTypeImpl;
import com.google.gson.GenericArrayTypeImpl;
import com.google.gson.TypeVariable;
import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[5].getClass().getComponentType();
    TypeInfoArray expectedTypeInfo = TypeInfoFactory.getTypeInfoForArray(type);
    TypeInfoArray actualTypeInfo = new TypeInfoArray(type);
    Assert.assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test
  public void testTypeInfoForField_GenericClass() {
    Class<Foo<Integer>> clazz = Foo.class;
    Field field = clazz.getField("myField");
    Type fieldType = field.getGenericType();
    Type ownerType = clazz;
    Type actualType = TypeInfoFactory.getActualType(fieldType, ownerType, clazz);
    TypeInfo<Integer> expectedTypeInfo = new TypeInfo<>(TypeToken.of(Integer.class).getType());
    TypeInfo<Integer> actualTypeInfo = (TypeInfo<Integer>) actualType;
    Assert.assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test
  public void testTypeInfoForField_RawClass() {
    Class<Foo> clazz = Foo.class;
    Field field = clazz.getField("myField");
    Type fieldType = field.getType();
    Type ownerType = clazz;
    Type actualType = TypeInfoFactory.getActualType(fieldType, ownerType, clazz);
    TypeInfo<Integer> expectedTypeInfo = new TypeInfo<>(Integer.TYPE);
    TypeInfo<Integer> actualTypeInfo = (TypeInfo<Integer>) actualType;
    Assert.assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test
  public void testTypeInfoForField_ArrayType() {
    Class<Foo> clazz = Foo.class;
    Field field = clazz.getField("myArrayField");
    Type fieldType = field.getType();
    Type ownerType = clazz;
    Type actualType = TypeInfoFactory.getActualType(fieldType, ownerType, clazz);
    TypeInfoArray expectedTypeInfo = new TypeInfoArray(Integer[].class);
    TypeInfoArray actualTypeInfo = (TypeInfoArray) actualType;
    Assert.assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test
  public void testTypeInfoForField_ParameterizedType() {
    Class<Foo<List<String>>> clazz = Foo.class;
    Field field = clazz.getField("myListField");
    Type fieldType = field.getGenericType();
    Type ownerType = clazz;
    Type actualType = TypeInfoFactory.getActualType(fieldType, ownerType, clazz);
    TypeInfo<List<String>> expectedTypeInfo = new TypeInfo<>(new TypeToken<List<String>>() {}.getType());
    TypeInfo<List<String>> actualTypeInfo = (TypeInfo<List<String>>) actualType;
    Assert.assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForField_ParentIsNotParameterizedType() {
    Class<Foo> clazz = Foo.class;
    Field field = clazz.getField("myField");
    Type ownerType = null; // Change this to a non-parameterized class
    Type actualType = TypeInfoFactory.getActualType(field.getGenericType(), ownerType, clazz);
  }

  @Test
  public void testTypeInfoForField_TypeVariable() {
    Class<Foo<Bar<?>>> clazz = Foo.class;
    Field field = clazz.getField("myField");
    Type fieldType = field.getGenericType();
    Type ownerType = clazz;
    Type actualType = TypeInfoFactory.getActualType(fieldType, ownerType, clazz);
    TypeVariable<?> typeVariable = clazz.getTypeParameters()[0];
    Type expectedType = TypeToken.of(typeVariable.getName()).getType();
    TypeInfo<?> actualTypeInfo = (TypeInfo<?>) actualType;
    Assert.assertEquals(expectedType, actualTypeInfo.getType());
  }

  @Test
  public void testTypeInfoForField_WildcardType() {
    Class<Foo<?>> clazz = Foo.class;
    Field field = clazz.getField("myField");
    Type ownerType = clazz;
    Type upperBound = Object.class;
    Type actualType = TypeInfoFactory.getActualType(field.getGenericType(), ownerType, clazz);
    Type expectedType = TypeToken.get(upperBound.getComponentType()).getType();
    TypeInfo<?> actualTypeInfo = (TypeInfo<?>) actualType;
    Assert.assertEquals(expectedType, actualTypeInfo.getType());
  }

  static class Foo<T> {
    @SuppressWarnings("unused")
    T myField;
    List<String> myListField;
    Integer[] myArrayField;
  }

}