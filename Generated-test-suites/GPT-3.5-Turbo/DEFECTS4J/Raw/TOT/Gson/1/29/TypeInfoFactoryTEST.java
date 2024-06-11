import static org.junit.Assert.*;
import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testGetTypeInfoForArray() {
    // Test typical use case
    TypeInfoArray infoArray = TypeInfoFactory.getTypeInfoForArray(String[].class);
    assertEquals(String[].class, infoArray.getType());

    // Test edge case with empty array
    TypeInfoArray emptyArray = TypeInfoFactory.getTypeInfoForArray(new String[0].getClass());
    assertEquals(String[].class, emptyArray.getType());

    // Test error scenario with non-array type
    try {
      TypeInfoArray errorArray = TypeInfoFactory.getTypeInfoForArray(String.class);
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      // Exception expected, test passed
    }
  }

  @Test
  public void testGetTypeInfoForField() {
    // Test typical use case
    TypeInfo infoField = TypeInfoFactory.getTypeInfoForField(MyClass.class.getDeclaredField("field"), MyClass.class);
    assertEquals(String.class, infoField.getType());

    // Test edge case with field of generic type
    TypeInfo genericField = TypeInfoFactory.getTypeInfoForField(MyClass.class.getDeclaredField("genericField"), MyClass.class);
    assertEquals(GenericType.class, genericField.getType());

    // Test error scenario with non-existent field
    try {
      TypeInfo errorField = TypeInfoFactory.getTypeInfoForField(MyClass.class.getDeclaredField("nonExistentField"), MyClass.class);
      fail("Expected NoSuchFieldException to be thrown");
    } catch (NoSuchFieldException e) {
      // Exception expected, test passed
    }

    // Test error scenario with invalid type definition
    try {
      TypeInfo errorField = TypeInfoFactory.getTypeInfoForField(MyClass.class.getDeclaredField("invalidTypeField"), MyClass.class);
      fail("Expected UnsupportedOperationException to be thrown");
    } catch (UnsupportedOperationException e) {
      // Exception expected, test passed
    }
  }

  private static class MyClass {
    private String field;
    private GenericType<String> genericField;
    private InvalidType invalidTypeField;
  }

  private static class GenericType<T> {
    private T value;
  }

  private static class InvalidType<T> {
    private T value;
  }

}