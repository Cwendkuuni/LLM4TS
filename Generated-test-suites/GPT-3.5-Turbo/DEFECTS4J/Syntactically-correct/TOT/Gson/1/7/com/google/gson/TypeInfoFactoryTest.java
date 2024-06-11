import static org.junit.Assert.*;
import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testGetTypeInfoForArray() {
    // Typical use case: type is an array
    TypeInfoArray info = TypeInfoFactory.getTypeInfoForArray(new Integer[10]);
    assertEquals(Integer[].class, info.getType());
    
    // Edge case: type is an empty array
    info = TypeInfoFactory.getTypeInfoForArray(new String[0]);
    assertEquals(String[].class, info.getType());
    
    // Error scenario: type is not an array
    try {
      info = TypeInfoFactory.getTypeInfoForArray(123);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Test passed
    }
  }

  @Test
  public void testGetTypeInfoForField() throws NoSuchFieldException {
    // Typical use case: field type is a Class
    Field field1 = DummyClass.class.getDeclaredField("stringField");
    TypeInfo info1 = TypeInfoFactory.getTypeInfoForField(field1, DummyClass.class);
    assertEquals(String.class, info1.getType());
    
    // Edge case: field type is a ParameterizedType
    Field field2 = DummyClass.class.getDeclaredField("listField");
    TypeInfo info2 = TypeInfoFactory.getTypeInfoForField(field2, DummyClass.class);
    assertEquals(ParameterizedType.class, info2.getType());
    
    // Error scenario: parent type is not a ParameterizedType
    try {
      Field field3 = DummyClass.class.getDeclaredField("integerField");
      TypeInfo info3 = TypeInfoFactory.getTypeInfoForField(field3, Integer.class);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Test passed
    }
  }
  
  // Dummy class for testing
  private static class DummyClass {
    public String stringField;
    public List<Integer> listField;
    public Integer integerField;
  }

}