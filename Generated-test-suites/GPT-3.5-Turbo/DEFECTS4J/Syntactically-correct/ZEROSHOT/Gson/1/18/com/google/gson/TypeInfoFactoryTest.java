import static org.junit.Assert.*;
import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testGetTypeInfoForArray() {
    TypeInfoArray typeInfo = TypeInfoFactory.getTypeInfoForArray(new int[10]);
    assertNotNull(typeInfo);
    assertEquals(int[].class, typeInfo.getType());
  }
  
  @Test
  public void testGetTypeInfoForField() throws NoSuchFieldException {
    Field field = TestClass.class.getDeclaredField("field");
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, TestClass.class);
    assertNotNull(typeInfo);
    assertEquals(Integer.class, typeInfo.getType());
  }
  
  @Test
  public void testGetTypeInfoForFieldWithTypeVariable() throws NoSuchFieldException {
    Field field = TestClass.class.getDeclaredField("fieldWithTypeVariable");
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, TestClass.class);
    assertNotNull(typeInfo);
    assertEquals(String.class, typeInfo.getType());
  }
  
  @Test(expected=UnsupportedOperationException.class)
  public void testGetTypeInfoForFieldTypeVariableWithoutParameterizedType() throws NoSuchFieldException {
    Field field = TestClass.class.getDeclaredField("fieldWithTypeVariableWithoutParameterizedType");
    TypeInfoFactory.getTypeInfoForField(field, TestClass.class);
  }
  
  @Test
  public void testGetTypeInfoForFieldTypeVariableWithParameterizedType() throws NoSuchFieldException {
    Field field = TestClass.class.getDeclaredField("fieldWithTypeVariableWithParameterizedType");
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, TestClass.class);
    assertNotNull(typeInfo);
    assertEquals(Double.class, typeInfo.getType());
  }
  
  @Test
  public void testGetTypeInfoForFieldTypeVariableWithParameterizedTypeNested() throws NoSuchFieldException {
    Field field = TestClass.class.getDeclaredField("fieldWithTypeVariableWithParameterizedTypeNested");
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, TestClass.class);
    assertNotNull(typeInfo);
    assertEquals(Float.class, typeInfo.getType());
  }
  
  @Test
  public void testGetTypeInfoForFieldTypeVariableWithMultipleParameterizedTypesNested() throws NoSuchFieldException {
    Field field = TestClass.class.getDeclaredField("fieldWithTypeVariableWithMultipleParameterizedTypesNested");
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, TestClass.class);
    assertNotNull(typeInfo);
    assertEquals(Boolean.class, typeInfo.getType());
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testGetTypeInfoForInvalidType() throws NoSuchFieldException {
    Field field = TestClass.class.getDeclaredField("fieldWithInvalidType");
    TypeInfoFactory.getTypeInfoForField(field, TestClass.class);
  }
  
  @Test
  public void testExtractRealTypes() {
    Type[] types = {Integer.class, String.class};
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(types, TestClass.class, TestClass.class);
    assertNotNull(actualTypes);
    assertEquals(2, actualTypes.length);
    assertEquals(Integer.class, actualTypes[0]);
    assertEquals(String.class, actualTypes[1]);
  }
  
  @Test
  public void testGetIndex() {
    TypeVariable<?>[] types = TestClass.class.getTypeParameters();
    TypeVariable<?> type = types[0];
    int index = TypeInfoFactory.getIndex(types, type);
    assertEquals(0, index);
  }
  
  @Test(expected=IllegalStateException.class)
  public void testGetIndexNotFound() {
    TypeVariable<?>[] types = TestClass.class.getTypeParameters();
    TypeVariable<?> type = new TypeVariableImpl("T");
    TypeInfoFactory.getIndex(types, type);
  }
  
  private static class TestClass<T> {
    private Integer field;
    private T fieldWithTypeVariable;
    private T fieldWithTypeVariableWithoutParameterizedType;
    private List<Double> fieldWithTypeVariableWithParameterizedType;
    private Map<String, Float> fieldWithTypeVariableWithParameterizedTypeNested;
    private Map<Integer, String> fieldWithTypeVariableWithMultipleParameterizedTypesNested;
    private List<List<Boolean>> fieldWithInvalidType;
  }
}