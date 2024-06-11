import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

  @Test
  public void testGetTypeInfoForArray() {
    // Test case 1
    Type type1 = new int[10].getClass();
    TypeInfoArray info1 = TypeInfoFactory.getTypeInfoForArray(type1);
    assertEquals(int[].class, info1.getType());

    // Test case 2
    Type type2 = new String[5][].getClass();
    TypeInfoArray info2 = TypeInfoFactory.getTypeInfoForArray(type2);
    assertEquals(String[][].class, info2.getType());

    // Add more test cases as needed
  }

  @Test
  public void testGetTypeInfoForField() {
    // Test case 1
    Field field1 = MyClass.class.getDeclaredField("myField");
    TypeInfo info1 = TypeInfoFactory.getTypeInfoForField(field1, MyClass.class);
    assertEquals(String.class, info1.getType());

    // Test case 2
    Field field2 = MyClass.class.getDeclaredField("myGenericField");
    TypeInfo info2 = TypeInfoFactory.getTypeInfoForField(field2, MyClass.class);
    assertEquals(List.class, info2.getType());

    // Add more test cases as needed
  }

  // Helper class for testing
  private static class MyClass {
    private String myField;
    private List<Integer> myGenericField;
  }

}