import static org.junit.Assert.*;
import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import com.google.gson.TypeInfoImpl;
import com.google.gson.ParameterizedTypeImpl;
import com.google.gson.GenericArrayTypeImpl;
import com.google.gson.TypeVariable;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public class TypeInfoFactoryTest {

  @org.junit.Test
  public void testTypeInfoForArray() {
    Type type = new int[10].getClass().getComponentType();
    TypeInfoArray expected = new TypeInfoArray(type);
    TypeInfoActual typeInfoActual = new TypeInfoFactory().getTypeInfoForArray(type);
    assertEquals(expected, typeInfoActual);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForArrayWithInvalidType() {
    Type type = String.class;
    new TypeInfoFactory().getTypeInfoForArray(type);
  }

  @org.junit.Test
  public void testTypeInfoForField() throws NoSuchFieldException, IllegalAccessException {
    Class<Person> clazz = Person.class;
    Field field = clazz.getDeclaredField("name");
    Type fieldType = field.getGenericType();
    Type typeDefiningF = clazz.getGenericSuperclass();
    Type parentType = typeDefiningF.getGenericSuperclass();
    Class<?> rawParentClass = (Class<?>) parentType;

    Type actualType = new TypeInfoFactory().getTypeInfoForField(field, typeDefiningF).getType();
    TypeInfoExpected expected = new TypeInfoImpl(String.class);
    assertEquals(expected, new TypeInfoActual(actualType));
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForFieldWithInvalidType() throws NoSuchFieldException, IllegalAccessException {
    Class<Person> clazz = Person.class;
    Field field = clazz.getDeclaredField("age"); // age is an int in this example
    Type fieldType = field.getGenericType();
    Type typeDefiningF = clazz.getGenericSuperclass();
    Type parentType = typeDefiningF.getGenericSuperclass();
    Class<?> rawParentClass = (Class<?>) parentType;

    new TypeInfoFactory().getTypeInfoForField(field, typeDefiningF);
  }

  @org.junit.Test
  public void testGetActualType() throws NoSuchFieldException, IllegalAccessException {
    Class<Person> clazz = Person.class;
    Field field = clazz.getDeclaredField("name");
    Type fieldType = field.getGenericType();
    Type typeDefiningF = clazz.getGenericSuperclass();
    Class<?> rawParentClass = (Class<?>) typeDefiningF.getGenericSuperclass();

    Type actualType = new TypeInfoFactory().getActualType(fieldType, typeDefiningF, rawParentClass);
    TypeInfoExpected expected = new TypeInfoImpl(String.class);
    assertEquals(expected, new TypeInfoActual(actualType));
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testGetActualTypeWithInvalidType() throws NoSuchFieldException, IllegalAccessException {
    Class<Person> clazz = Person.class;
    Field field = clazz.getDeclaredField("age"); // age is an int in this example
    Type fieldType = field.getGenericType();
    Type typeDefiningF = clazz.getGenericSuperclass();
    Class<?> rawParentClass = (Class<?>) typeDefiningF.getGenericSuperclass();

    new TypeInfoFactory().getActualType(fieldType, typeDefiningF, rawParentClass);
  }

  @org.junit.Test
  public void testExtractRealTypes() {
    Type[] actualTypeArguments = new Type[] { Integer.TYPE, String.class };
    Type parentType = new ParameterizedTypeImpl(Person.class, new Type[] { TypeVariable.class }, null);
    Class<?> rawParentClass = Person.class;

    Type[] expectedTypes = { new TypeInfoImpl(Integer.TYPE), new TypeInfoImpl(String.class) };
    assertArrayEquals(expectedTypes, new TypeInfoFactory().extractRealTypes(actualTypeArguments, parentType, rawParentClass));
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testExtractRealTypesWithInvalidParentType() {
    Type[] actualTypeArguments = new Type[] { Integer.TYPE, String.class };
    Type parentType = new Object() {};
    Class<?> rawParentClass = Person.class;

    new TypeInfoFactory().extractRealTypes(actualTypeArguments, parentType, rawParentClass);
  }

  @org.junit.Test
  public void testGetIndex() {
    TypeVariable<Integer> typeVariable = TypeVariable.of(Integer.TYPE, "i");
    TypeVariable<?>[] types = { TypeVariable.of(String.class, "s"), typeVariable, TypeVariable.of(Float.TYPE, "f") };

    int index = new TypeInfoFactory().getIndex(types, typeVariable);
    assertEquals(1, index);
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testGetIndexWithInvalidTypeVariable() {
    TypeVariable<String> typeVariable = TypeVariable.of(String.class, "s");
    TypeVariable<?>[] types = { TypeVariable.of(Integer.TYPE, "i"), TypeVariable.of(Float.TYPE, "f") };

    new TypeInfoFactory().getIndex(types, typeVariable);
  }

  private static class Person {
    private String name;
    private int age;
  }
}