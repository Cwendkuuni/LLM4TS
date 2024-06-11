import static org.junit.Assert.*;
import com.google.gson.*;
import java.lang.reflect.*;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[5].getClass().getGenericComponentType();
    TypeInfo expectedTypeInfo = new TypeInfoArray(type);
    TypeInfo actualTypeInfo = TypeInfoFactory.TypeInfoForArray(type);
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test
  public void testTypeInfoForField_FieldWithClassType() {
    try {
      Class<?> clazz = MyClass.class;
      Field field = clazz.getField("myField");
      Type fieldType = field.getType();
      Type definingClassType = clazz.getGenericSuperclass();
      TypeInfo expectedTypeInfo = new TypeInfo(fieldType);
      TypeInfo actualTypeInfo = TypeInfoFactory.TypeInfoForField(field, definingClassType);
      assertEquals(expectedTypeInfo, actualTypeInfo);
    } catch (NoSuchFieldException e) {
      fail("Field 'myField' not found in class MyClass.");
    }
  }

  @Test
  public void testTypeInfoForField_FieldWithParameterizedType() {
    try {
      Class<?> clazz = MyClassWithGenericField.class;
      Field field = clazz.getField("myField");
      Type genericFieldType = field.getGenericType();
      Type definingClassType = clazz.getGenericSuperclass();
      TypeInfo expectedTypeInfo = new TypeInfo(new TypeParameterizedTypeImpl(int.class, new Type[]{}));
      TypeInfo actualTypeInfo = TypeInfoFactory.TypeInfoForField(field, definingClassType);
      assertEquals(expectedTypeInfo, actualTypeInfo);
    } catch (NoSuchFieldException e) {
      fail("Field 'myField' not found in class MyClassWithGenericField.");
    }
  }

  @Test
  public void testTypeInfoForField_FieldWithTypeVariable() {
    try {
      Class<?> clazz = MyClassWithTypeVariableField.class;
      Field field = clazz.getField("myField");
      Type genericFieldType = field.getGenericType();
      Type definingClassType = clazz.getGenericSuperclass();
      TypeVariable<?> typeVariable = genericFieldType.getTypeVariables()[0];
      int index = TypeInfoFactory.getIndex((TypeVariable<?>[]) definingClassType.getTypeParameters(), typeVariable);
      Type actualType = ((ParameterizedType) definingClassType).getActualTypeArguments()[index];
      TypeInfo expectedTypeInfo = new TypeInfo(actualType);
      TypeInfo actualTypeInfo = TypeInfoFactory.TypeInfoForField(field, definingClassType);
      assertEquals(expectedTypeInfo, actualTypeInfo);
    } catch (NoSuchFieldException e) {
      fail("Field 'myField' not found in class MyClassWithTypeVariableField.");
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForField_FieldWithUnsupportedType() {
    try {
      Class<?> clazz = MyClassWithUnsupportedField.class;
      Field field = clazz.getField("myField");
      Type definingClassType = clazz.getGenericSuperclass();
      Type actualType = TypeInfoFactory.getActualType(field.getGenericType(), null, null);
      fail("Unexpected TypeInfo created for unsupported type.");
    } catch (NoSuchFieldException e) {
      fail("Field 'myField' not found in class MyClassWithUnsupportedField.");
    }
  }

  @Test
  public void testGetActualType_ClassType() {
    Class<?> rawParentClass = MyClass.class;
    Type typeToEvaluate = MyClass.class;
    Type expectedType = typeToEvaluate;
    Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, null, rawParentClass);
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetActualType_ParameterizedType() {
    Class<?> rawParentClass = MyClassWithGenericField.class;
    Type parentType = rawParentClass.getGenericSuperclass();
    Type typeToEvaluate = new TypeParameterizedTypeImpl(int[].class, new Type[] {int.class});
    Type expectedType = int[].class;
    Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetActualType_GenericArrayType() {
    Class<?> rawParentClass = MyClassWithArrayField.class;
    Type parentType = rawParentClass.getGenericSuperclass();
    Type typeToEvaluate = new GenericArrayTypeImpl(new Type[] {int.class});
    Type expectedType = int[].class;
    Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetActualType_TypeVariable() {
    Class<?> rawParentClass = MyClassWithTypeVariableField.class;
    Type parentType = rawParentClass.getGenericSuperclass();
    TypeVariable<?> fieldTypeVariable = new TypeVariable<Integer>("T") {};
    int index = TypeInfoFactory.getIndex((TypeVariable<?>[]) parentType.getTypeParameters(), fieldTypeVariable);
    Type actualType = ((ParameterizedType) parentType).getActualTypeArguments()[index];
    Type expectedType = int.class;
    Type typeToEvaluate = fieldTypeVariable;
    Type actualTypeEvaluated = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    assertEquals(expectedType, actualTypeEvaluated);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualType_WildcardType() {
    Class<?> rawParentClass = MyClassWithWildcardField.class;
    Type parentType = rawParentClass.getGenericSuperclass();
    Type typeToEvaluate = new WildcardTypeImpl(null, new Type[] {Object.class});
    Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    fail("Unexpected TypeInfo created for wildcard type.");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetActualType_InvalidType() {
    Class<?> rawParentClass = MyClassWithInvalidType.class;
    Type typeToEvaluate = new TypeVariable<Integer>("T") {};
    Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, null, rawParentClass);
    fail("Unexpected TypeInfo created for invalid type.");
  }

  @Test
  public void testExtractRealTypes_ClassTypes() {
    Type parentType = MyClassWithArrayField.class.getGenericSuperclass();
    Type[] actualTypeArguments = new Type[] {int.class, int.class};
    Type[] expectedTypeArguments = new Type[] {int[].class, int.class};
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, null);
    assertArrayEquals(expectedTypeArguments, actualTypes);
  }

  @Test
  public void testExtractRealTypes_ParameterizedTypes() {
    Type parentType = MyClassWithMapField.class.getGenericSuperclass();
    Type[] actualTypeArguments = new Type[] {Map.class, String.class, int.class};
    Type[] expectedTypeArguments = new Type[] {Map.class, Map.Entry.class, String.class, int.class};
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, null);
    assertArrayEquals(expectedTypeArguments, actualTypes);
  }

  private static class MyClass {
    int myField;
  }

  private static class MyClassWithGenericField<T> {
    T myField;
  }

  private static class MyClassWithTypeVariableField<T> {
    T myField;
  }

  private static class MyClassWithUnsupportedField<T> {
    T myField;
  }

  private static class MyClassWithArrayField {
    int[] myField;
  }

  private static class MyClassWithWildcardField {
    List<? extends Number> myField;
  }

  private static class MyClassWithMapField<K, V> {
    Map<K, V> myField;
  }
}