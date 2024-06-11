import static com.google.common.base.Preconditions.checkArgument;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import com.google.gson.TypeUtils;
import com.google.gson.reflect.GenericTypeImpl;
import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.WildcardTypeImpl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public class TypeInfoFactoryTest {

  @org.junit.Test
  public void testTypeInfoForArray() {
    Type type = new int[3].getClass().getComponentType();
    TypeInfoArray typeInfoArray = TypeInfoFactory.TypeInfoForArray(new TypeToken<int[]>() {}.getType());
    checkArgument(TypeInfoFactory.isArray(typeInfoArray.getType()), "Expected array type");
    assertEquals(int[].class, typeInfoArray.getType().getComponentType());
  }

  @org.junit.Test
  public void testTypeInfoForField_TypeVariable() {
    Class<Foo> clazz = Foo.class;
    Field field = clazz.getField("bar");
    Type type = field.getGenericType();
    Type infoType = TypeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass()).getType();

    assertEquals(Integer.TYPE, infoType.getType());
  }

  @org.junit.Test
  public void testTypeInfoForField_RawType() {
    Class<Foo> clazz = Foo.class;
    Field field = clazz.getField("bar");
    Type infoType = TypeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass()).getType();

    assertEquals(Integer.class, infoType.getType());
  }

  @org.junit.Test
  public void testTypeInfoForField_ParameterizedType() {
    Class<Foo> clazz = Foo.class;
    Field field = clazz.getField("baz");
    Type infoType = TypeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass()).getType();

    assertEquals(String.class, infoType.getType());
  }

  @org.junit.Test
  public void testTypeInfoForField_GenericArrayType() {
    Class<Foo> clazz = Foo.class;
    Field field = clazz.getField("qux");
    Type infoType = TypeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass()).getType();

    assertEquals(GenericTypeImpl.get(Integer[].class), infoType.getType());
  }

  @org.junit.Test
  public void testTypeInfoForField_TypeVariable_InParent() {
    Class<Foo> clazz = Foo.class;
    Field field = clazz.getField("quux");
    Type infoType = TypeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass()).getType();

    assertEquals(WildcardTypeImpl.wildcardTypeOf("? extends Number"), infoType.getType());
  }

  @org.junit.Test
  public void testTypeInfoForField_TypeVariable_NotInParent() {
    Class<Foo> clazz = Foo.class;
    Field field = clazz.getField("quux2");
    Type infoType = TypeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass()).getType();

    assertNull(infoType);
  }

  @org.junit.Test
  public void testTypeInfoForField_UnknownType() {
    Class<Foo> clazz = Foo.class;
    Field field = clazz.getField("quux3");
    Type infoType = TypeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass()).getType();

    assertNull(infoType);
  }

  @org.junit.Test
  public void testGetActualType_Class() {
    Class<Foo> clazz = Foo.class;
    Type actualType = TypeInfoFactory.getActualType(clazz, null, null);

    assertEquals(Foo.class, actualType);
  }

  @org.junit.Test
  public void testGetActualType_ParameterizedType() {
    Class<Bar> barClazz = Bar.class;
    Type parentType = new TypeToken<Foo<Bar>>() {}.getType();

    Type actualType = TypeInfoFactory.getActualType(new TypeToken<List<Bar>>() {}.getType(), parentType, null);

    assertEquals(List.class, actualType.getComponentType());
  }

  @org.junit.Test
  public void testGetActualType_GenericArrayType() {
    Class<Bar> barClazz = Bar.class;
    Type parentType = new TypeToken<Foo<Bar>>() {}.getType();

    Type actualType = TypeInfoFactory.getActualType(new TypeToken<Bar[]>() {}.getType(), parentType, null);

    assertEquals(Bar[].class, actualType);
  }

  @org.junit.Test
  public void testGetActualType_TypeVariable() {
    Class<Foo<Number>> clazz = Foo.class.getGenericSuperclass();
    TypeVariable<?> typeVariable = clazz.getGenericSuperclass().getTypeVariables()[0];
    Type parentType = new TypeToken<Foo<Number>>() {}.getType();

    Type actualType = TypeInfoFactory.getActualType(typeVariable, parentType, null);

    assertEquals(Number.class, actualType);
  }

  @org.junit.Test
  public void testGetActualType_WildcardType() {
    Class<Foo<? extends Number>> clazz = Foo.class.getGenericSuperclass();
    WildcardType wildcardType = (WildcardType) clazz.getGenericComponentTypes()[0];
    Type parentType = new TypeToken<Foo<? extends Number>>() {}.getType();

    Type actualType = TypeInfoFactory.getActualType(wildcardType, parentType, null);

    assertEquals(Number.class, actualType);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testGetActualType_UnknownType() {
    Type parentType = new TypeToken<Foo<Bar>>() {}.getType();
    Type unknownType = new Object() {}.getClass().getType();

    Type actualType = TypeInfoFactory.getActualType(unknownType, parentType, null);
  }

  @org.junit.Test
  public void testExtractRealTypes_Class() {
    Type[] types = TypeInfoFactory.extractRealTypes(
        new Type[] {String.class}, null, null);

    assertEquals(String.class, types[0].getType());
  }

  @org.junit.Test
  public void testExtractRealTypes_ParameterizedType() {
    Type parentType = new TypeToken<Foo<Bar>>() {}.getType();
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(
        new Type[] {new TypeToken<List<Bar>>() {}.getType()}, parentType, null);

    assertEquals(List.class, actualTypes[0].getComponentType());
  }

  @org.junit.Test
  public void testExtractRealTypes_GenericArrayType() {
    Type parentType = new TypeToken<Foo<Bar>>() {}.getType();
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(
        new Type[] {new TypeToken<Bar[]>() {}.getType()}, parentType, null);

    assertEquals(Bar[].class, actualTypes[0]);
  }

  @org.junit.Test
  public void testExtractRealTypes_TypeVariable() {
    Type parentType = new TypeToken<Foo<Number>>() {}.getType();
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(
        new Type[] {new TypeVariable<Number>() {}}, parentType, null);

    assertEquals(Number.class, actualTypes[0]);
  }

  @org.junit.Test
  public void testExtractRealTypes_WildcardType() {
    Type parentType = new TypeToken<Foo<? extends Number>>() {}.getType();
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(
        new Type[] {new WildcardTypeImpl(null, "? extends Number") {}}, parentType, null);

    assertEquals(Number.class, actualTypes[0]);
  }

  static class Foo<T> {}
  static class Bar {}

}