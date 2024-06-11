import static com.google.common.truth.Truth.assertThat;
import com.google.gson.TypeInfoFactory;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;

public class TypeInfoFactoryTest {

  @org.junit.Test
  public void testTypeInfoForArray() {
    Type type = TypeToken.getIntArrayType().getType();
    TypeInfoArray typeInfo = TypeInfoFactory.getTypeInfoForArray(type);
    assertThat(typeInfo.getComponentType()).isInstanceOf(IntTypeInfo.class);
  }

  @org.junit.Test
  public void testTypeInfoForField_FieldWithTypeVariable() {
    Class<?> testClass = new Foo<>();
    try {
      Field field = testClass.getDeclaredField("a");
      Type type = field.getGenericType();
      TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, testClass.getGenericType());
      assertThat(typeInfo.getType()).isInstanceOf(IntegerTypeInfo.class);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  @org.junit.Test
  public void testTypeInfoForField_FieldWithParameterizedType() {
    Class<?> testClass = new Bar<>();
    try {
      Field field = testClass.getDeclaredField("list");
      Type type = field.getGenericType();
      TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, testClass.getGenericType());
      assertThat(typeInfo.getType()).isInstanceOf(ListTypeInfo.class);
      ListTypeInfo listTypeInfo = (ListTypeInfo) typeInfo.getType();
      Type elementType = listTypeInfo.getComponentType();
      assertThat(elementType).isInstanceOf(IntegerTypeInfo.class);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  @org.junit.Test
  public void testTypeInfoForField_FieldWithGenericArrayType() {
    Class<?> testClass = new Baz<>();
    try {
      Field field = testClass.getDeclaredField("array");
      Type type = field.getGenericType();
      TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, testClass.getGenericType());
      assertThat(typeInfo.getType()).isInstanceOf(IntArrayTypeInfo.class);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  @org.junit.Test
  public void testTypeInfoForField_FieldWithWildcardType() {
    Class<?> testClass = new Quux<>();
    try {
      Field field = testClass.getDeclaredField("list");
      Type type = field.getGenericType();
      TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, testClass.getGenericType());
      assertThat(typeInfo.getType()).isInstanceOf(ListTypeInfo.class);
      ListTypeInfo listTypeInfo = (ListTypeInfo) typeInfo.getType();
      Type elementType = listTypeInfo.getComponentType();
      assertThat(elementType).isInstanceOf(WildcardTypeInfo.class);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  private static class Foo<A> {
    A a;
  }

  private static class Bar<T> {
    List<T> list;
  }

  private static class Baz<T> {
    T[] array;
  }

  private static class Quux<T> {
    List<? extends T> list;
  }
}