import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Preconditions;
import com.google.gson.TypeUtils;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[5].getClass().getComponentType();
    TypeInfo expected = new TypeInfoArray(type);
    TypeInfo actual = TypeInfoFactory.TypeInfoForArray(type);
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForArrayWithNonArrayType() {
    Type type = String.class;
    TypeInfoFactory.TypeInfoForArray(type);
  }

  @Test
  public void testTypeInfoForField() throws NoSuchFieldException, IllegalAccessException {
    Class<Integer> clazz = Integer.class;
    Field field = clazz.getDeclaredField("VALUE");
    field.setAccessible(true);
    Type type = field.getGenericType();
    Type parentType = clazz;
    Class<?> rawParentClass = clazz;

    TypeInfo expected = new TypeInfo(Integer.TYPE);
    TypeInfo actual = TypeInfoFactory.getTypeInfoForField(field, parentType);
    assertSame(expected, actual);
  }

  @Test
  public void testGetActualTypeForClass() {
    Class<Integer> clazz = Integer.class;
    Type parentType = clazz;
    Class<?> rawParentClass = clazz;

    TypeInfo expected = new TypeInfo(Integer.TYPE);
    TypeInfo actual = TypeInfoFactory.getActualType(clazz.getTypeName(), parentType, rawParentClass);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetActualTypeForParameterizedType() {
    class Foo<A> {
      A a;
    }

    Foo<Integer> foo = new Foo<>();
    Type parentType = foo.getClass();
    Class<?> rawParentClass = Foo.class;

    TypeVariable<A> fieldTypeVariable = TypeToken.getType(Foo.class).getType(0);
    Type expected = Integer.TYPE;
    Type actual = TypeInfoFactory.getActualType(fieldTypeVariable, parentType, rawParentClass);
    assertEquals(expected, actual);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualTypeForNonParameterizedType() {
    class Foo {
      // No type variables
    }

    Foo foo = new Foo();
    Type parentType = foo.getClass();
    Class<?> rawParentClass = Foo.class;

    TypeVariable<?>[] typeVariables = rawParentClass.getTypeParameters();
    Preconditions.checkArgument(typeVariables.length == 0, "Unexpected type variables.");
    Type actuaType = TypeInfoFactory.getActualType(null, parentType, rawParentClass);
  }

  @Test
  public void testGetActualTypeForGenericArrayType() {
    class Foo<A> {
      A[] a;
    }

    Foo<Integer> foo = new Foo();
    Type parentType = foo.getClass();
    Class<?> rawParentClass = Foo.class;

    GenericArrayType genericArrayType = (GenericArrayType) foo.getClass().getComponentType();
    Type componentType = genericArrayType.getGenericComponentType();
    Type expected = Integer[].class;
    Type actual = TypeInfoFactory.getActualType(componentType, parentType, rawParentClass);
    assertSame(expected, actual);
  }

  @Test
  public void testGetActualTypeForTypeVariable() {
    class Foo<A> {
      A a;
    }

    Foo<Integer> foo = new Foo<>();
    Type parentType = foo.getClass();
    Class<?> rawParentClass = Foo.class;

    TypeVariable<A> fieldTypeVariable = TypeToken.getType(Foo.class).getType(0);
    Type expected = Integer.TYPE;
    Type actual = TypeInfoFactory.getActualType(fieldTypeVariable, parentType, rawParentClass);
    assertEquals(expected, actual);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualTypeForWildcardType() {
    class Foo<A> {
      List<A> list;
    }

    Foo<?> foo = new Foo<>();
    Type parentType = foo.getClass();
    Class<?> rawParentClass = Foo.class;

    Type actual = TypeInfoFactory.getActualType(null, parentType, rawParentClass);
  }
}