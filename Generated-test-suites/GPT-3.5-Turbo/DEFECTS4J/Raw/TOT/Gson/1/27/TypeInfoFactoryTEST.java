import org.junit.*;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

  @Test
  public void getTypeInfoForArray_WithArray_ReturnsTypeInfoArray() {
    TypeInfoArray result = TypeInfoFactory.getTypeInfoForArray(int[].class);
    assertNotNull(result);
    assertEquals(int[].class, result.getType());
  }
  
  @Test
  public void getTypeInfoForArray_WithNonArray_ReturnsIllegalArgumentException() {
    try {
      TypeInfoFactory.getTypeInfoForArray(int.class);
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("Invalid type, must be an array", e.getMessage());
    }
  }

  @Test
  public void getTypeInfoForField_WithTypeVariable_ReturnsTypeInfo() {
    Field field = MyClass.class.getDeclaredField("typeVariableField");
    Type typeDefiningF = MyClass.class;
    TypeInfo result = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
    assertNotNull(result);
    assertEquals(TypeVariable.class, result.getType());
  }
  
  @Test
  public void getTypeInfoForField_WithParameterizedType_ReturnsTypeInfo() {
    Field field = MyClass.class.getDeclaredField("parameterizedField");
    Type typeDefiningF = MyClass.class;
    TypeInfo result = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
    assertNotNull(result);
    assertEquals(ParameterizedType.class, result.getType());
  }
  
  @Test
  public void getTypeInfoForField_WithGenericArrayType_ReturnsTypeInfo() {
    Field field = MyClass.class.getDeclaredField("genericArrayField");
    Type typeDefiningF = MyClass.class;
    TypeInfo result = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
    assertNotNull(result);
    assertEquals(GenericArrayType.class, result.getType());
  }
  
  @Test
  public void getTypeInfoForField_WithClass_ReturnsTypeInfo() {
    Field field = MyClass.class.getDeclaredField("classField");
    Type typeDefiningF = MyClass.class;
    TypeInfo result = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
    assertNotNull(result);
    assertEquals(Class.class, result.getType());
  }

  @Test
  public void getTypeInfoForField_WithWildcardType_ReturnsTypeInfo() {
    Field field = MyClass.class.getDeclaredField("wildcardField");
    Type typeDefiningF = MyClass.class;
    TypeInfo result = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
    assertNotNull(result);
    assertEquals(WildcardType.class, result.getType());
  }
  
  private class MyClass {
    private TypeVariable<String> typeVariableField;
    private ParameterizedType<List<Integer>> parameterizedField;
    private GenericArrayType<int[]> genericArrayField;
    private Class<?> classField;
    private WildcardType wildcardField;
  }

}