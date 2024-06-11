import static org.junit.Assert.*;
import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testGetTypeInfoForArray() {
    // Typical use case: test for an array of integers
    TypeInfoArray arrayType = TypeInfoFactory.getTypeInfoForArray(Integer[].class);
    assertNotNull(arrayType);
    assertEquals(Integer[].class, arrayType.getType());

    // Edge case: test for an empty string array
    arrayType = TypeInfoFactory.getTypeInfoForArray(String[].class);
    assertNotNull(arrayType);
    assertEquals(String[].class, arrayType.getType());

    // Error scenario: test for a non-array type
    try {
      arrayType = TypeInfoFactory.getTypeInfoForArray(Integer.class);
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("java.lang.Integer is not an array type", e.getMessage());
    }
  }

  @Test
  public void testGetTypeInfoForField() throws NoSuchFieldException {
    // Typical use case: test for a field with type Integer in a class with actual type arguments
    Field f = MyClass.class.getDeclaredField("field1");
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(f, MyClass.class);
    assertNotNull(typeInfo);
    assertEquals(Integer.class, typeInfo.getType());

    // Edge case: test for a parameterized field with wildcard type
    f = MyClass.class.getDeclaredField("field2");
    typeInfo = TypeInfoFactory.getTypeInfoForField(f, MyClass.class);
    assertNotNull(typeInfo);
    assertTrue(typeInfo.getType() instanceof WildcardType);

    // Error scenario: test for a field with unknown type variable
    f = MyClass.class.getDeclaredField("field3");
    try {
      typeInfo = TypeInfoFactory.getTypeInfoForField(f, MyClass.class);
      fail("Expected UnsupportedOperationException to be thrown");
    } catch (UnsupportedOperationException e) {
      assertEquals("Expecting parameterized type, got class com.google.gson.TypeInfoFactoryTest$MyClass.\n Are you missing the use of TypeToken idiom?\n See http://sites.google.com/site/gson/gson-user-guide#TOC-Serializing-and-Deserializing-Gener", e.getMessage());
    }
  }

  @SuppressWarnings("unused")
  private static class MyClass<T> {
    private Integer field1;
    private List<? extends Number> field2;
    private T field3;
  }
}