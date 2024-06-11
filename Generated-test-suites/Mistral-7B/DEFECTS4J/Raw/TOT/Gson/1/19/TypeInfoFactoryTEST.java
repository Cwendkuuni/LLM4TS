import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[10].getClass().getComponentType();
    TypeInfo expected = TypeInfoFactory.getTypeInfoForArray(TypeToken.getType(int[].class).getType());
    TypeInfo actual = TypeInfoFactory.getTypeInfoForArray(type);

    assertThat(actual.isArray()).isTrue();
    assertThat(actual.componentType()).isEqualTo(expected.componentType());
  }

  @Test
  public void testTypeInfoForField() throws NoSuchFieldException {
    Field field = TypeUtils.class.getDeclaredField("TYPES");
    field.setAccessible(true);
    Type expected = field.getType();
    Type parentType = TypeInfoFactory.class;
    Type actual = TypeInfoFactory.getTypeInfoForField(field, parentType).getType();

    Field expectedField = ExpectedType.class.getDeclaredField("TYPE");
    expectedField.setAccessible(true);
    Type expectedType = expectedField.getType();

    assertThat(actual).isInstanceOf(expectedType);
  }

  @Test
  public void testGetActualTypeForClass() {
    Type parentType = new Object() {
      Type field;
    }.getClass();
    Class<?> expected = Integer.class;
    Class<?> actual = (Class<?>) TypeInfoFactory.getActualType(expected, parentType, null);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testGetActualTypeForParameterizedType() {
    Type parentType = new Object() {
      List<Integer> list;
    }.getClass();
    Type expected = Integer.class;
    Type actual = (Class<?>) TypeInfoFactory.getActualType(
        new TypeToken<List<Integer>>() {}.getType().getActualTypeArguments()[0], parentType, null);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testGetActualTypeForGenericArrayType() {
    Type parentType = new Object() {
      int[] array;
    }.getClass();
    Class<?> expected = Integer.class;
    Class<?> actual = (Class<?>) TypeInfoFactory.getActualType(
        new TypeToken<int[]>() {}.getType().getComponentType(), parentType, null);

    assertThat(actual).isEqualTo(expected);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualTypeForTypeVariable() throws NoSuchFieldException {
    Field field = TypeUtils.class.getDeclaredField("TYPES");
    field.setAccessible(true);
    Type parentType = TypeInfoFactory.class;
    TypeVariable<?> typeVariable = TypeToken.getType(String.class).getType().getTypeVariables()[0];

    Type expected = String.class;
    Type actual = (Type) TypeInfoFactory.getActualType(typeVariable, parentType, null);

    // This test should fail since the TypeInfoFactory does not support TypeVariable directly
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualTypeForWildcardType() throws NoSuchFieldException {
    Field field = TypeUtils.class.getDeclaredField("TYPES");
    field.setAccessible(true);
    Type parentType = TypeInfoFactory.class;

    Type expected = TypeVariable.class;
    Type actual = (Type) TypeInfoFactory.getActualType(
        new WildcardType(TypeToken.getType(List.class).getType()), parentType, null);

    // This test should fail since the TypeInfoFactory does not support WildcardType directly
  }

  static class ExpectedType {
    public static Type TYPE = new TypeToken<List<String>>() {}.getType();
  }
}