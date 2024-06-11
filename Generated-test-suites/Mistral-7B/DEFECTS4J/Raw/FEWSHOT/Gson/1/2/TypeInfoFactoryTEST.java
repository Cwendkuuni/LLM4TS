package com.google.gson;

import static com.google.common.base.Preconditions.checkArgument;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[5].getClass().getComponentType();
    TypeInfo arrayTypeInfo = TypeInfoFactory.TypeInfoForArray(type);

    assertEquals("int[]", arrayTypeInfo.toString());
  }

  @Test
  public void testTypeInfoForField_TypeVariable() throws NoSuchFieldException {
    class MyClass<T> {
      private T field;

      public T getField() {
        return field;
      }

      public void setField(T value) {
        this.field = value;
      }
    }

    MyClass<String> myClassInstance = new MyClass<>();
    Field field = myClassInstance.getClass().getDeclaredField("field");
    field.setAccessible(true);

    Type type = field.getGenericType();
    TypeInfo fieldTypeInfo = TypeInfoFactory.getTypeInfoForField(field, type.getClass());

    assertEquals("java.lang.String", fieldTypeInfo.toString());
  }

  @Test
  public void testTypeInfoForField_Class() throws NoSuchFieldException {
    class MyClass {
      private int field;

      public int getField() {
        return field;
      }

      public void setField(int value) {
        this.field = value;
      }
    }

    MyClass myClassInstance = new MyClass();
    Field field = myClassInstance.getClass().getDeclaredField("field");
    field.setAccessible(true);

    Type type = field.getGenericType();
    if (type == null) {
      type = field.getType();
    }

    TypeInfo fieldTypeInfo = TypeInfoFactory.getTypeInfoForField(field, type.getClass());

    assertEquals("int", fieldTypeInfo.toString());
  }

  @Test
  public void testTypeInfoForField_ParameterizedType() throws NoSuchFieldException, InstantiationException {
    class MyClass<T> {
      private List<T> listField;

      public List<T> getListField() {
        return listField;
      }

      public void setListField(List<T> value) {
        this.listField = value;
      }
    }

    MyClass<String> myClassInstance = new MyClass<>();
    Field listField = myClassInstance.getClass().getDeclaredField("listField");
    listField.setAccessible(true);

    Type type = listField.getGenericType();

    TypeInfo listFieldTypeInfo = TypeInfoFactory.getTypeInfoForField(listField, type.getClass());

    assertEquals("java.util.List", listFieldTypeInfo.toString());
  }

  @Test
  public void testTypeInfoForField_GenericArrayType() throws NoSuchFieldException, InstantiationException {
    class MyClass {
      private String[] stringArrayField;

      public String[] getStringArrayField() {
        return stringArrayField;
      }

      public void setStringArrayField(String[] value) {
        this.stringArrayField = value;
      }
    }

    MyClass myClassInstance = new MyClass();
    Field stringArrayField = myClassInstance.getClass().getDeclaredField("stringArrayField");
    stringArrayField.setAccessible(true);

    Type type = stringArrayField.getGenericType();

    TypeInfo stringArrayFieldTypeInfo = TypeInfoFactory.getTypeInfoForField(stringArrayField, type.getClass());

    assertEquals("java.lang.String[]", stringArrayFieldTypeInfo.toString());
  }

  @Test
  public void testTypeInfoForField_WildcardType() throws NoSuchFieldException, InstantiationException {
    class MyClass {
      private List<?> wildcardListField;

      public List<?> getWildcardListField() {
        return wildcardListField;
      }

      public void setWildcardListField(List<?> value) {
        this.wildcardListField = value;
      }
    }

    MyClass myClassInstance = new MyClass();
    Field wildcardListField = myClassInstance.getClass().getDeclaredField("wildcardListField");
    wildcardListField.setAccessible(true);

    Type type = wildcardListField.getGenericType();

    TypeInfo wildcardListFieldTypeInfo = TypeInfoFactory.getTypeInfoForField(wildcardListField, type.getClass());

    assertEquals("java.util.List<?>", wildcardListFieldTypeInfo.toString());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForField_UnsupportedType() throws NoSuchFieldException {
    class MyClass {
      private Object field;

      public Object getField() {
        return field;
      }

      public void setField(Object value) {
        this.field = value;
      }
    }

    MyClass myClassInstance = new MyClass();
    Field field = myClassInstance.getClass().getDeclaredField("field");
    field.setAccessible(true);

    Type type = field.getGenericType();

    TypeInfo fieldTypeInfo = TypeInfoFactory.getTypeInfoForField(field, type.getClass());

    fail("Unexpected call to TypeInfoFactory.getTypeInfoForField() with an unsupported type.");
  }

  @Test
  public void testGetActualType_Class() {
    Type type = String.class;
    Type actualType = TypeInfoFactory.getActualType(type, null, null);

    assertEquals(String.class, actualType);
  }

  @Test
  public void testGetActualType_ParameterizedType() {
    class MyClass<T> {
      private List<T> listField;

      public List<T> getListField() {
        return listField;
      }

      public void setListField(List<T> value) {
        this.listField = value;
      }
    }

    Type type = new TypeToken<MyClass<String>>() {}.getType();
    Type parentType = new TypeToken<MyClass<?>>() {}.getType();
    Type rawParentClass = MyClass.class;

    Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);

    assertEquals(MyClass.class, actualType);
  }

  @Test
  public void testGetActualType_GenericArrayType() {
    Type type = new TypeToken<String[]>() {}.getType();
    Type parentType = null;
    Type rawParentClass = String[].class;

    Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);

    assertEquals(String[].class, actualType);
  }

  @Test
  public void testGetActualType_TypeVariable() {
    class MyClass<T> {
      private T field;

      public T getField() {
        return field;
      }

      public void setField(T value) {
        this.field = value;
      }
    }

    Type type = new TypeToken<MyClass<String>>() {}.getType();
    Type parentType = new TypeToken<MyClass<?>>() {}.getType();
    Type rawParentClass = MyClass.class;

    TypeVariable<Integer> fieldTypeVariable = (TypeVariable<Integer>) type.getComponentType();

    int index = TypeInfoFactory.getIndex(type.getTypeParameters(), fieldTypeVariable);

    Type actualType = TypeInfoFactory.getActualType(type.getActualTypeArguments()[index], parentType, rawParentClass);

    assertEquals(String.class, actualType);
  }

  @Test
  public void testGetActualType_WildcardType() {
    class MyClass<T> {
      private List<? extends T> wildcardListField;

      public List<? extends T> getWildcardListField() {
        return wildcardListField;
      }

      public void setWildcardListField(List<? extends T> value) {
        this.wildcardListField = value;
      }
    }

    Type type = new TypeToken<MyClass<?>>() {}.getType();
    Type parentType = null;
    Type rawParentClass = MyClass.class;

    Type wildcardType = type.getComponentType();

    Type actualType = TypeInfoFactory.getActualType(wildcardType, parentType, rawParentClass);

    fail("Unexpected call to TypeInfoFactory.getActualType() with a wildcard type.");
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualType_UnsupportedType() {
    Type type = new Object() {}.getClass().getGenericSuperclass();
    Type parentType = null;
    Type rawParentClass = Object.class;

    Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);

    fail("Unexpected call to TypeInfoFactory.getActualType() with an unsupported type.");
  }

  @Test
  public void testExtractRealTypes_EmptyTypes() {
    Type[] actualTypeArguments = new Type[0];
    Type parentType = null;
    Class<?> rawParentClass = Object.class;

    Type[] expectedTypes = new Type[0];

    Type[] retTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);

    assertArrayEquals(expectedTypes, retTypes);
  }

  @Test
  public void testExtractRealTypes_OneType() {
    Type[] actualTypeArguments = new Type[] { new DoubleType() };
    Type parentType = null;
    Class<?> rawParentClass = Object.class;

    Type[] expectedTypes = new Type[] { Double.TYPE };

    Type[] retTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);

    assertArrayEquals(expectedTypes, retTypes);
  }

  @Test
  public void testExtractRealTypes_MultipleTypes() {
    Type[] actualTypeArguments = new Type[] { new IntegerType(), new StringType() };
    Type parentType = null;
    Class<?> rawParentClass = Object.class;

    Type[] expectedTypes = new Type[] { Integer.TYPE, String.class };

    Type[] retTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);

    assertArrayEquals(expectedTypes, retTypes);
  }
}