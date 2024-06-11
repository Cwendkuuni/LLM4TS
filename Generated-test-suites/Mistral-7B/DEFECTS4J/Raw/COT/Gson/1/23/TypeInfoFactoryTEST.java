import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import com.google.gson.TypeInfoFactory;
import com.google.gson.Preconditions;
import com.google.gson.TypeUtils;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[10].getClass().getComponentType();
    TypeInfo expected = new TypeInfoArray(type);
    TypeInfo actual = TypeInfoFactory.TypeInfoForArray(type);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetTypeInfoForField_TypeVariable_TypeDefinedInClass() {
    Class<MyClass> clazz = MyClass.class;
    Field field = clazz.getField("myField");
    Type type = field.getType();
    Type expected = new TypeVariable<Integer>("T") {};
    Type parentType = clazz.getGenericSuperclass();
    TypeInfo actual = TypeInfoFactory.getTypeInfoForField(field, parentType);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetTypeInfoForField_TypeVariable_TypeNotDefinedInClass() {
    Class<MyClass> clazz = MyClass.class;
    Field field = clazz.getField("myField");
    Type type = new TypeVariable<Integer>("T") {};
    Type expectedException = null; // UnsupportedOperationException
    try {
      TypeInfoFactory.getTypeInfoForField(field, type);
      fail("expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      assertEquals(expectedException.getMessage(), e.getMessage());
    }
  }

  @Test
  public void testGetTypeInfoForField_ParameterizedType() {
    Class<MyClass> clazz = MyClass.class;
    Field field = clazz.getField("myListField");
    Type type = field.getGenericType();
    Type expected = new ParameterizedTypeImpl(
        clazz.getComponentType(), new Type[] { Integer.TYPE }, null);
    Type parentType = clazz.getGenericSuperclass();
    TypeInfo actual = TypeInfoFactory.getTypeInfoForField(field, parentType);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetTypeInfoForField_GenericArrayType() {
    Class<MyClass> clazz = MyClass.class;
    Field field = clazz.getField("myArrayField");
    Type type = field.getGenericType();
    Type componentType = ((GenericArrayType) type).getGenericComponentType();
    Type expected = TypeUtils.wrapWithArray(new TypeInfo(componentType));
    Type parentType = clazz.getGenericSuperclass();
    TypeInfo actual = TypeInfoFactory.getTypeInfoForField(field, parentType);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetTypeInfoForField_WildcardType() {
    Class<MyClass> clazz = MyClass.class;
    Field field = clazz.getField("myListField");
    Type type = field.getGenericType();
    Type expected = TypeUtils.wrapWithArray(new TypeInfo(Object.class));
    Type parentType = clazz.getGenericSuperclass();
    TypeInfo actual = TypeInfoFactory.getTypeInfoForField(field, parentType);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetActualType_Class() {
    Class<MyClass> clazz = MyClass.class;
    Type expected = clazz;
    Type parentType = clazz.getGenericSuperclass();
    Type rawParentClass = Object.class;
    Type actual = TypeInfoFactory.getActualType(expected, parentType, rawParentClass);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetActualType_ParameterizedType() {
    Class<MyClass> clazz = MyClass.class;
    Type type = new ParameterizedTypeImpl(MyClassWithList.class, new Type[] { Integer.TYPE }, null);
    Type expected = MyClassWithList.class;
    Type parentType = clazz.getGenericSuperclass();
    Type rawParentClass = Object.class;
    Type actual = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetActualType_GenericArrayType() {
    Class<MyClass> clazz = MyClass.class;
    Type type = new GenericArrayTypeImpl(Integer.TYPE);
    Type expected = Integer[].class;
    Type parentType = clazz.getGenericSuperclass();
    Type rawParentClass = Object.class;
    Type actual = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetActualType_TypeVariable() {
    Class<MyClass> clazz = MyClass.class;
    TypeVariable<Integer> typeVariable = new TypeVariable<>("T") {};
    Type expected = Integer.class;
    Type parentType = clazz.getGenericSuperclass();
    Type rawParentClass = Object.class;
    Type actual = TypeInfoFactory.getActualType(typeVariable, parentType, rawParentClass);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetActualType_WildcardType() {
    Class<MyClass> clazz = MyClass.class;
    Type wildcardType = new WildcardType(null, new Type[] { Object.class });
    Type expected = Object.class;
    Type parentType = clazz.getGenericSuperclass();
    Type rawParentClass = Object.class;
    Type actual = TypeInfoFactory.getActualType(wildcardType, parentType, rawParentClass);
    assertEquals(expected, actual);
  }

  @Test
  public void testExtractRealTypes() {
    Type[] types = new Type[] { new TypeVariable<>("A") {}, new Class<?>[] { Integer.TYPE } };
    Type[] expected = new Type[] { Integer.TYPE, Integer.TYPE };
    Type parentType = null;
    Type rawParentClass = Object.class;
    Type[] actual = TypeInfoFactory.extractRealTypes(types, parentType, rawParentClass);
    assertArrayEquals(expected, actual);
  }

  // Add more test cases as needed for other methods in the class

}

class MyClass {
  int myField;
}

class MyClassWithList<T> {
  List<T> myListField;
}

class MyClassWithArray {
  int[] myArrayField;
}