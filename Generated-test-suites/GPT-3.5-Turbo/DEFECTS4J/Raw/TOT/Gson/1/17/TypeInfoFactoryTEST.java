import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

  @Test
  public void testGetTypeInfoForArray() {
    // Typical use case: Array type
    // Expected: TypeInfoArray object is returned
    Type arrayType = new String[0].getClass();
    TypeInfoArray result = TypeInfoFactory.getTypeInfoForArray(arrayType);
    assertNotNull(result);

    // Edge case: Null type
    // Expected: IllegalArgumentException is thrown
    try {
      TypeInfoFactory.getTypeInfoForArray(null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Type must be an array", e.getMessage());
    }
  }

  @Test
  public void testGetTypeInfoForField() {
    // Typical use case: Field with actual type
    // Expected: TypeInfo object with the actual type of the field is returned
    Field field = MyTestClass.class.getDeclaredField("stringField");
    TypeInfo result = TypeInfoFactory.getTypeInfoForField(field, MyTestClass.class);
    assertNotNull(result);
    assertEquals(String.class, result.getType());

    // Error scenario: Field with TypeVariable in parameterized type
    // Expected: UnsupportedOperationException is thrown
    field = MyTestClass.class.getDeclaredField("typeVariableListField");
    try {
      TypeInfoFactory.getTypeInfoForField(field, MyTestClass.class);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      assertEquals("Expecting parameterized type, got class com.google.gson.TypeInfoFactoryTest$MyTestClass.\n Are you missing the use of TypeToken idiom?\n See http://sites.google.com/site/gson/gson-user-guide#TOC-Serializing-and-Deserializing-Gener", e.getMessage());
    }
  }

  @Test
  public void testGetActualType() {
    // Typical use case: Type is Class
    // Expected: The same Class object is returned
    Class<?> type = String.class;
    Type parentType = MyTestClass.class;
    Class<?> rawParentClass = MyTestClass.class;
    Type result = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
    assertNotNull(result);
    assertEquals(type, result);

    // Error scenario: Type is not Class, ParameterizedType, GenericArrayType or TypeVariable
    // Expected: IllegalArgumentException is thrown
    type = new TypeInfoFactory();
    try {
      TypeInfoFactory.getActualType(type, parentType, rawParentClass);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Type 'com.google.gson.TypeInfoFactory' is not a Class, ParameterizedType, GenericArrayType or TypeVariable. Can't extract type.", e.getMessage());
    }
  }

  @Test
  public void testExtractRealTypes() {
    // Typical use case: Array of actual types
    // Expected: Array of actual types is returned
    Type[] actualTypeArguments = {String.class, Integer.class};
    Type parentType = MyTestClass.class;
    Class<?> rawParentClass = MyTestClass.class;
    Type[] result = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
    assertNotNull(result);
    assertEquals(String.class, result[0]);
    assertEquals(Integer.class, result[1]);

    // Edge case: Null actualTypeArguments
    // Expected: IllegalArgumentException is thrown
    try {
      TypeInfoFactory.extractRealTypes(null, parentType, rawParentClass);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("Actual type arguments cannot be null", e.getMessage());
    }
  }

  @Test
  public void testGetIndex() {
    // Typical use case: TypeVariable exists in types array
    // Expected: Index of TypeVariable is returned
    TypeVariable<?>[] types = MyTestClass.class.getTypeParameters();
    TypeVariable<?> type = types[0];
    int result = TypeInfoFactory.getIndex(types, type);
    assertEquals(0, result);

    // Error scenario: TypeVariable does not exist in types array
    // Expected: IllegalStateException is thrown
    type = new TypeVariableImpl();
    try {
      TypeInfoFactory.getIndex(types, type);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      assertEquals("How can the type variable not be present in the class declaration!", e.getMessage());
    }
  }

  private static class MyTestClass<T> {
    private String stringField;
    private List<T> typeVariableListField;
  }

  private static class TypeVariableImpl extends TypeVariable<String> {
    // Empty implementation for testing purposes
  }

}