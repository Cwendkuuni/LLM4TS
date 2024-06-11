import static org.junit.Assert.*;
import com.google.gson.TypeInfoFactory;
import com.google.gson.TypeVariable;
import com.google.gson.reflect.GenericArrayTypeImpl;
import com.google.gson.reflect.ParameterizedTypeImpl;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;

public class TypeInfoFactoryTest {

  @SuppressWarnings("unchecked")
  @Test
  public void testTypeInfoForArray() {
    Type type = new int[5].getClass().getComponentType();
    TypeInfoArray expectedTypeInfo = TypeInfoFactory.TypeInfoForArray(type);
    TypeInfoArray actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(type);
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testTypeInfoForField_TypeVariable() {
    Class<TestClass<?>> rawParentClass = TestClass.class;
    Field field = TestClass.class.getField("field");
    Type type = field.getGenericType();
    TypeInfo expectedTypeInfo = new TypeInfo(TestClass.A.class);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, type, rawParentClass);
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testTypeInfoForField_ParameterizedType() {
    Class<TestClass<?>> rawParentClass = TestClass.class;
    Field field = TestClass.class.getField("listField");
    Type type = field.getGenericType();
    TypeInfo expectedTypeInfo = new TypeInfo(new TypeVariable<Integer>() {});
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, type, rawParentClass);
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testTypeInfoForField_GenericArrayType() {
    Class<TestClass<?>> rawParentClass = TestClass.class;
    Field field = TestClass.class.getField("arrayField");
    Type type = field.getGenericType();
    TypeInfo expectedTypeInfo = new TypeInfo(int[].class);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, type, rawParentClass);
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @SuppressWarnings("unchecked")
  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForField_UnsupportedType() {
    Class<TestClass<?>> rawParentClass = TestClass.class;
    Field field = TestClass.class.getField("unsupportedField");
    Type type = field.getGenericType();
    TypeInfoFactory.getTypeInfoForField(field, type, rawParentClass);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testGetActualType_Class() {
    Type typeToEvaluate = int.class;
    Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, null, null);
    assertEquals(int.class, actualType);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testGetActualType_ParameterizedType() {
    Type parentType = new ParameterizedType() {}.getRawType();
    Type typeToEvaluate = new ParameterizedTypeImpl(Map.class, new Type[]{Object.class, Integer.class});
    Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, null);
    assertEquals(Map.class, actualType);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testGetActualType_GenericArrayType() {
    Type parentType = int[].class;
    Type typeToEvaluate = new GenericArrayTypeImpl(Integer.class);
    Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, null);
    assertEquals(Integer.class, actualType);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testGetActualType_TypeVariable() {
    Type parentType = new ParameterizedTypeImpl(Map.class, new Type[]{Object.class, Integer.class});
    TypeVariable<?> typeVariable = new TypeVariable<Integer>("I") {};
    Type actualType = TypeInfoFactory.getActualType(typeVariable, parentType, null);
    assertEquals(Integer.class, actualType);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testGetActualType_WildcardType() {
    Type typeToEvaluate = new WildcardType(new Type[] {Object.class, Number.class});
    Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, null, null);
    assertTrue(Object.class.isAssignableFrom(actualType));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetActualType_IllegalArgumentException() {
    Type parentType = null;
    Type typeToEvaluate = null;
    TypeInfoFactory.getActualType(typeToEvaluate, parentType, null);
  }

  @Test
  public void testExtractRealTypes() {
    Type[] expectedTypes = {int.class, String.class};
    Type[] parentType = new Type[] {new ParameterizedTypeImpl(Map.class, new Type[]{Object.class, Integer.class}), Integer.class};
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(parentType, null, null);
    assertArrayEquals(expectedTypes, actualTypes);
  }

  @Test
  public void testGetIndex() {
    TypeVariable<?>[] types = new TypeVariable<?>[] {new TypeVariable<Integer>("i") {}, new TypeVariable<String>("s") {}};
    TypeVariable<?> type = new TypeVariable<Integer>("i") {};
    int expectedIndex = 0;
    int actualIndex = TypeInfoFactory.getIndex(types, type);
    assertEquals(expectedIndex, actualIndex);
  }

  private static class TestClass<A> {
    @SuppressWarnings("unused")
    private A field;
    @SuppressWarnings("unused")
    private List<A> listField;
    private int[] arrayField;
    private Object unsupportedField;

    @SuppressWarnings("unchecked")
    static class A {}
  }

}