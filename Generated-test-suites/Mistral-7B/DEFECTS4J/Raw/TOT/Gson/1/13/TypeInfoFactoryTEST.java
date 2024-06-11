import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import org.junit.Test;

public class TypeInfoFactoryTest {

  // Test for getTypeInfoForArray method
  @Test
  public void testTypeInfoForArray() {
    Type type = new TypeToken<int[]>() {}.getType();
    TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
    assertThat(typeInfoArray.getType()).isInstanceOf(int[].class);
  }

  // Test for getTypeInfoForField method with a simple field
  @Test
  public void testTypeInfoForSimpleField() throws NoSuchFieldException {
    Class<?> clazz = new SimpleClass();
    Field field = clazz.getDeclaredField("simpleField");
    Type type = field.getType();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass());
    assertThat(typeInfo.getType()).isEqualTo(type);
  }

  // Test for getTypeInfoForField method with a generic field
  @Test
  public void testTypeInfoForGenericField() throws NoSuchFieldException {
    Class<?> clazz = new GenericClass<>();
    Field field = clazz.getDeclaredField("genericField");
    Type type = field.getGenericType();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass());
    Type actualType = typeInfo.getType();
    if (actualType instanceof Class<?>) {
      assertThat(actualType.isAssignableFrom(String.class)).isTrue();
    } else {
      ParameterizedType parameterizedType = (ParameterizedType) actualType;
      Type actualComponentType = parameterizedType.getActualTypeArguments()[0];
      assertThat(actualComponentType).isInstanceOf(String.class);
    }
  }

  // Test for getTypeInfoForField method with a non-existing field
  @Test(expected = NoSuchFieldException.class)
  public void testTypeInfoForNonExistingField() throws NoSuchFieldException {
    Class<?> clazz = new SimpleClass();
    Field nonExistingField = clazz.getDeclaredField("nonExistingField");
    TypeInfoFactory.getTypeInfoForField(nonExistingField, clazz.getGenericSuperclass());
  }

  // Test for getTypeInfoForField method with a field of wildcard type
  @Test
  public void testTypeInfoForFieldOfWildcardType() throws NoSuchFieldException {
    Class<?> clazz = new WildcardClass<>();
    Field field = clazz.getDeclaredField("wildcardField");
    Type type = field.getGenericType();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass());
    Type actualType = typeInfo.getType();
    if (actualType instanceof Class<?>) {
      // A wildcard field can have a lower bound, e.g., List<? extends Number>.
      // To test this, we'll mock the lower bound type.
      Type lowerBoundType = mock(Number.class);
      when(lowerBoundType.getClass()).thenReturn(Integer.class);
      Type expectedType = TypeUtils.wrapWithWildcard(lowerBoundType);
      assertThat(actualType).isEqualTo(expectedType);
    } else {
      WildcardType wildcardType = (WildcardType) actualType;
      Type[] bounds = wildcardType.getUpperBounds();
      // A wildcard type can have multiple bounds, e.g., List<? extends Number & Comparable<? super Number>>.
      // Here, we'll test the single upper bound case (List<? extends Number>).
      Type expectedUpperBound = TypeUtils.wrapWithWildcard(Number.class);
      assertThat(Arrays.asList(bounds)).containsExactly(expectedUpperBound);
    }
  }

  // Test for getTypeInfoForField method with a field of parameterized type
  @Test
  public void testTypeInfoForFieldOfParameterizedType() throws NoSuchFieldException {
    Class<?> clazz = new NestedClassWithParameterizedType();
    Field field = clazz.getDeclaredField("nestedField");
    Type type = field.getGenericType();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass());
    Type actualType = typeInfo.getType();
    if (actualType instanceof Class<?>) {
      assertThat(actualType).isInstanceOf(NestedClass.class);
      // Test that the generic types within the nested class are properly extracted.
      NestedClass nestedClass = new NestedClass();
      Field nestedField = nestedClass.getClass().getDeclaredField("innerField");
      Type innerFieldType = nestedField.getGenericType();
      Type expectedInnerFieldType = new TypeToken<Integer>() {}.getType();
      assertThat(actualType).isInstanceOf(NestedClass.class);
      TypeInfo innerTypeInfo = TypeInfoFactory.getTypeInfoForField(nestedField, nestedClass.getClass());
      Type expectedInnerType = innerTypeInfo.getType();
      assertThat(expectedInnerType).isEqualTo(expectedInnerFieldType);
    } else {
      ParameterizedType expectedParameterizedType = new TypeToken<NestedClass<Integer>>() {}.getType();
      Type[] expectedTypeArguments = expectedParameterizedType.getTypeArguments();
      Type[] actualTypeArguments = ((ParameterizedType) actualType).getActualTypeArguments();
      for (int i = 0; i < expectedTypeArguments.length; i++) {
        Type expectedTypeArgument = expectedTypeArguments[i];
        Type actualTypeArgument = actualTypeArguments[i];
        if (expectedTypeArgument instanceof Class<?>) {
          assertThat(actualTypeArgument).isInstanceOf(expectedTypeArgument.getClass());
        } else {
          // A TypeVariable is considered a "Type" and not a "Class", so it needs special handling.
          TypeVariable<?> expectedTypeVariable = (TypeVariable<?>) expectedTypeArgument;
          TypeVariable<?> actualTypeVariable = (TypeVariable<?>) actualTypeArgument;

          // Check that the expected and actual TypeVariables have the same name.
          String expectedName = expectedTypeVariable.getName();
          String actualName = actualTypeVariable.getName();
          if (expectedName.equals(actualName)) {
            Type expectedBound = expectedTypeVariable.getBounds()[0];
            Type actualBound = actualTypeVariable.getBounds()[0];
            // Check that the expected and actual bounds are equivalent.
            if (expectedBound instanceof Class<?>) {
              assertThat(actualBound).isInstanceOf(expectedBound.getClass());
            } else {
              // A TypeVariable of a raw type (e.g., T) doesn't have a bound.
              if (expectedBound == null) {
                assertThat(actualBound).isNull();
              } else {
                throw new RuntimeException("Unexpected TypeVariable bound: expected " + expectedBound
                    + ", but got " + actualBound);
              }
            }
          } else {
            throw new RuntimeException("Unexpected TypeVariable: expected " + expectedTypeVariable
                + ", but got " + actualTypeVariable);
          }
        }
      }
    }
  }

  // Test for getActualType method with a TypeVariable of a raw class
  @Test
  public void testGetActualTypeForTypeVariableOfRawClass() throws NoSuchFieldException {
    Class<?> clazz = new NestedClassWithRawType();
    Field field = clazz.getDeclaredField("nestedField");
    Type type = field.getGenericType();
    Type actualType = TypeInfoFactory.getActualType(type, clazz.getGenericSuperclass(), clazz);
    Type expectedType = clazz.getComponentType();
    assertThat(actualType).isEqualTo(expectedType);
  }

  // Test for getActualType method with a TypeVariable of a parameterized type
  @Test
  public void testGetActualTypeForTypeVariableOfParameterizedType() throws NoSuchFieldException {
    Class<?> clazz = new NestedClassWithParameterizedType();
    Field field = clazz.getDeclaredField("nestedField");
    Type type = field.getGenericType();
    Type actualType = TypeInfoFactory.getActualType(type, clazz.getGenericSuperclass(), clazz);
    ParameterizedType expectedParameterizedType = new TypeToken<NestedClass<Integer>>() {}.getType();
    Type[] expectedTypeArguments = expectedParameterizedType.getTypeArguments();
    Type[] actualTypeArguments = ((ParameterizedType) actualType).getActualTypeArguments();
    for (int i = 0; i < expectedTypeArguments.length; i++) {
      Type expectedTypeArgument = expectedTypeArguments[i];
      Type actualTypeArgument = actualTypeArguments[i];
      if (expectedTypeArgument instanceof Class<?>) {
        assertThat(actualTypeArgument).isInstanceOf(expectedTypeArgument.getClass());
      } else {
        TypeVariable<?> expectedTypeVariable = (TypeVariable<?>) expectedTypeArgument;
        TypeVariable<?> actualTypeVariable = (TypeVariable<?>) actualTypeArgument;
        String expectedName = expectedTypeVariable.getName();
        String actualName = actualTypeVariable.getName();
        if (expectedName.equals(actualName)) {
          Type expectedBound = expectedTypeVariable.getBounds()[0];
          Type actualBound = actualTypeVariable.getBounds()[0];
          if (expectedBound instanceof Class<?>) {
            assertThat(actualBound).isInstanceOf(expectedBound.getClass());
          } else {
            Type expectedExpectedType = expectedBound;
            Type actualExpectedType = actualBound;
            if (expectedBound instanceof ParameterizedType) {
              expectedExpectedType = ExtractType.getRawType(((ParameterizedType) expectedBound).getActualTypeArguments()[0]);
            }
            if (actualBound instanceof ParameterizedType) {
              actualExpectedType = ExtractType.getRawType(((ParameterizedType) actualBound).getActualTypeArguments()[0]);
            }

            assertThat(expectedExpectedType).isEqualTo(actualExpectedType);
          }
        } else {
          throw new RuntimeException("Unexpected TypeVariable: expected " + expectedTypeVariable
              + ", but got " + actualTypeVariable);
        }
      }
    }
  }

  private static class SimpleClass {
    private int simpleField;
  }

  private static class GenericClass<T> {
    @SuppressWarnings("unused")
    private T genericField;
  }

  private static class WildcardClass<T> {
    @SuppressWarnings("unused")
    private T wildcardField;
  }

  private static class NestedClass<T> {
    @SuppressWarnings("unused")
    private T innerField;

    @SuppressWarnings("unused")
    private static class InnerClass {
      // This nested class is used to test TypeInfoFactory's ability to correctly extract the type
      // of a static nested field.
      private static final int INNER_FIELD = 42;
    }
  }

  private static class NestedClassWithParameterizedType<T> {
    private NestedClass<T> nestedField;
  }

  private static class NestedClassWithRawType {
    @SuppressWarnings("unused")
    private NestedClass nestedField;
  }

  private static class ExtractType {
    @SuppressWarnings("unchecked")
    private static <T> Class<T> getRawType(Type type) {
      if (type instanceof Class<?>) {
        return (Class<T>) type;
      } else if (type instanceof ParameterizedType) {
        Type rawType = ((ParameterizedType) type).getRawType();
        return (Class<T>) rawType;
      } else if (type instanceof GenericArrayType) {
        Type componentType = ((GenericArrayType) type).getGenericComponentType();
        return (Class<T>) ExtractType.getRawType(componentType);
      } else if (type instanceof TypeVariable<?>) {
        throw new RuntimeException("Unexpected TypeVariable: " + type);
      } else if (type instanceof WildcardType) {
        WildcardType wildcardType = (WildcardType) type;
        Type[] bounds = wildcardType.getUpperBounds();
        if (bounds.length == 0) {
          throw new RuntimeException("Unexpected wildcard type with no bounds");
        }
        return (Class<T>) ExtractType.getRawType(bounds[0]);
      } else {
        throw new RuntimeException("Unexpected type: " + type);
      }
    }
  }
}

// Test helper class to make the code more readable by hiding `Preconditions` checks
class Preconditions {
  public static void checkArgument(boolean condition) {
    // Empty implementation, as we don't want to include the Guava library in our test.
  }
}