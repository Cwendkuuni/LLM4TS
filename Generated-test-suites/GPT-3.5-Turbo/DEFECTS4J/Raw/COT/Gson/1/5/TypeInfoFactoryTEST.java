
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

  @Test
  public void testGetTypeInfoForArray() {
    TypeInfoArray arrayType = TypeInfoFactory.getTypeInfoForArray(String[].class);
    assertEquals(arrayType.getRawType(), String.class);
    assertFalse(arrayType.isParameterizedType());
    assertFalse(arrayType.isArray());
    assertFalse(arrayType.isTypeVariable());
    assertFalse(arrayType.isWildcardType());
    assertNull(arrayType.getActualType());
  }
  
  @Test
  public void testGetTypeForField() throws NoSuchFieldException {
    Field f = MyClass.class.getDeclaredField("myField");
    TypeInfo fieldType = TypeInfoFactory.getTypeInfoForField(f, MyClass.class);
    assertEquals(fieldType.getRawType(), String.class);
    assertFalse(fieldType.isParameterizedType());
    assertFalse(fieldType.isArray());
    assertFalse(fieldType.isTypeVariable());
    assertFalse(fieldType.isWildcardType());
    assertNull(fieldType.getActualType());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeForField_invalidField() {
    TypeInfoFactory.getTypeInfoForField(null, MyClass.class);
  }
  
  @Test
  public void testGetTypeForField_parameterizedField() throws NoSuchFieldException {
    Field f = MyClass.class.getDeclaredField("parameterizedField");
    TypeInfo fieldType = TypeInfoFactory.getTypeInfoForField(f, MyClass.class);
    assertEquals(fieldType.getRawType(), List.class);
    assertTrue(fieldType.isParameterizedType());
    assertFalse(fieldType.isArray());
    assertFalse(fieldType.isTypeVariable());
    assertFalse(fieldType.isWildcardType());
    assertEquals(fieldType.getActualType(), String.class);
  }
  
  @Test
  public void testGetTypeForField_typeVariableField() throws NoSuchFieldException {
    Field f = MyClass.class.getDeclaredField("typeVariableField");
    TypeInfo fieldType = TypeInfoFactory.getTypeInfoForField(f, MyClass.class);
    assertEquals(fieldType.getRawType(), Object.class);
    assertFalse(fieldType.isParameterizedType());
    assertFalse(fieldType.isArray());
    assertTrue(fieldType.isTypeVariable());
    assertFalse(fieldType.isWildcardType());
    assertNull(fieldType.getActualType());
  }
  
  @Test
  public void testGetTypeForField_wildcardField() throws NoSuchFieldException {
    Field f = MyClass.class.getDeclaredField("wildcardField");
    TypeInfo fieldType = TypeInfoFactory.getTypeInfoForField(f, MyClass.class);
    assertEquals(fieldType.getRawType(), List.class);
    assertTrue(fieldType.isParameterizedType());
    assertFalse(fieldType.isArray());
    assertFalse(fieldType.isTypeVariable());
    assertTrue(fieldType.isWildcardType());
    assertEquals(fieldType.getActualType(), String.class);
  }
  
    static class MyClass {
    String myField;
    List<String> parameterizedField;
    T typeVariableField;
    List<? extends String> wildcardField;
  }
}
