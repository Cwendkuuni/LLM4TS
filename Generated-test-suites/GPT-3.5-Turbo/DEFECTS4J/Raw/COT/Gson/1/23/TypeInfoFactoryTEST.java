import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public class TypeInfoFactoryTest {
  
  @Test
  public void testGetTypeInfoForArray() {
    Type type = int[].class;
    TypeInfoArray actual = TypeInfoFactory.getTypeInfoForArray(type);
    TypeInfoArray expected = new TypeInfoArray(type);
    assertEquals(expected.getType(), actual.getType());
  }
  
  @Test
  public void testGetTypeInfoForField() throws NoSuchFieldException {
    Field f = TestClass.class.getField("field");
    Type typeDefiningF = TestClass.class;
    TypeInfo actual = TypeInfoFactory.getTypeInfoForField(f, typeDefiningF);
    TypeInfo expected = new TypeInfo(String.class);
    assertEquals(expected.getType(), actual.getType());
  }
  
  @Test
  public void testGetActualType_ClassType() {
    Type typeToEvaluate = String.class;
    Type parentType = null;
    Class<?> rawParentClass = null;
    Type actual = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    Type expected = String.class;
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetActualType_ParameterizedType() {
    Type typeToEvaluate = new ParameterizedTypeImpl(TestClass.class, new Type[] {String.class}, null);
    Type parentType = null;
    Class<?> rawParentClass = null;
    Type actual = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    Type expected = new ParameterizedTypeImpl(TestClass.class, new Type[] {String.class}, null);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetActualType_GenericArrayType() {
    Type typeToEvaluate = new GenericArrayTypeImpl(String.class);
    Type parentType = null;
    Class<?> rawParentClass = null;
    Type actual = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    Type expected = new GenericArrayTypeImpl(String.class);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetActualType_TypeVariable_ParameterizedType() {
    Type typeToEvaluate = new TypeVariableImpl("A", null, null);
    Type parentType = new ParameterizedTypeImpl(TestClass.class, new Type[] {String.class}, null);
    Class<?> rawParentClass = TestClass.class;
    Type actual = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    Type expected = String.class;
    assertEquals(expected, actual);
  }
  
  @Test (expected = UnsupportedOperationException.class)
  public void testGetActualType_TypeVariable_unsupported() {
    Type typeToEvaluate = new TypeVariableImpl("A", null, null);
    Type parentType = String.class;
    Class<?> rawParentClass = String.class;
    TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
  }
  
  @Test
  public void testGetActualType_WildcardType() {
    Type typeToEvaluate = new WildcardTypeImpl(new Type[] {String.class}, new Type[0]);
    Type parentType = null;
    Class<?> rawParentClass = null;
    Type actual = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    Type expected = String.class;
    assertEquals(expected, actual);
  }
  
  @Test (expected = IllegalArgumentException.class)
  public void testGetActualType_InvalidType() {
    Type typeToEvaluate = new Object();
    Type parentType = null;
    Class<?> rawParentClass = null;
    TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
  }
  
  @Test
  public void testExtractRealTypes() {
    Type[] actualTypeArguments = new Type[] {String.class};
    Type parentType = null;
    Class<?> rawParentClass = null;
    Type[] actual = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
    Type[] expected = new Type[] {String.class};
    assertArrayEquals(expected, actual);
  }
  
  @Test
  public void testGetIndex() {
    TypeVariable<?>[] types = new TypeVariable<?>[] {new TypeVariableImpl("A", null, null)};
    TypeVariable<?> type = new TypeVariableImpl("A", null, null);
    int actual = TypeInfoFactory.getIndex(types, type);
    int expected = 0;
    assertEquals(expected, actual);
  }
  
  @Test (expected = IllegalStateException.class)
  public void testGetIndex_TypeNotPresent() {
    TypeVariable<?>[] types = new TypeVariable<?>[] {new TypeVariableImpl("A", null, null)};
    TypeVariable<?> type = new TypeVariableImpl("B", null, null);
    TypeInfoFactory.getIndex(types, type);
  }
  
  // Additional test cases for edge cases and exception handling scenarios
  
  // Edge case: Testing a nested parameterized type
  @Test
  public void testGetTypeInfoForField_NestedParameterizedType() throws NoSuchFieldException {
    Field f = TestClass.class.getField("nestedField");
    Type typeDefiningF = TestClass.class;
    TypeInfo actual = TypeInfoFactory.getTypeInfoForField(f, typeDefiningF);
    TypeInfo expected = new TypeInfo(new ParameterizedTypeImpl(TestClass.InnerClass.class, new Type[] {String.class}, null));
    assertEquals(expected.getType(), actual.getType());
  }
  
  // Edge case: Testing an array of generic types
  @Test
  public void testGetTypeInfoForField_GenericArrayType() throws NoSuchFieldException {
    Field f = TestClass.class.getField("arrayField");
    Type typeDefiningF = TestClass.class;
    TypeInfo actual = TypeInfoFactory.getTypeInfoForField(f, typeDefiningF);
    TypeInfo expected = new TypeInfo(new GenericArrayTypeImpl(new ParameterizedTypeImpl(TestClass.InnerClass.class, new Type[] {String.class}, null)));
    assertEquals(expected.getType(), actual.getType());
  }
  
  // Exception handling: Testing unsupported type variable scenario
  @Test (expected = UnsupportedOperationException.class)
  public void testGetActualType_TypeVariable_unsupported_Exception() {
    Type typeToEvaluate = new TypeVariableImpl("A", null, null);
    Type parentType = null;
    Class<?> rawParentClass = null;
    TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
  }
  
  // Exception handling: Testing invalid type
  @Test (expected = IllegalArgumentException.class)
  public void testGetActualType_InvalidType_Exception() {
    Type typeToEvaluate = new Object();
    Type parentType = null;
    Class<?> rawParentClass = null;
    TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
  }
  
  // Exception handling: Testing type variable not present in the class declaration
  @Test (expected = IllegalStateException.class)
  public void testGetIndex_TypeNotPresent_Exception() {
    TypeVariable<?>[] types = new TypeVariable<?>[] {new TypeVariableImpl("A", null, null)};
    TypeVariable<?> type = new TypeVariableImpl("B", null, null);
    TypeInfoFactory.getIndex(types, type);
  }
  
  public static class TestClass {
    public String field;
    public TestClass.InnerClass<String> nestedField;
    public TestClass.InnerClass<String>[] arrayField;
    
    public static class InnerClass<A> { }
  }
}