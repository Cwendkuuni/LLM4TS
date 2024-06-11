import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

  @Test
  public void testGetTypeInfoForArray() {
    TypeInfoArray infoArray = TypeInfoFactory.getTypeInfoForArray(new String[5].getClass());
    assertNotNull(infoArray);
    assertEquals("java.lang.String[]", infoArray.toString());
  }

  @Test
  public void testGetTypeInfoForField() {
    TypeInfo fieldInfo = TypeInfoFactory.getTypeInfoForField(MyClass.class.getDeclaredField("myField"), MyClass.class);
    assertNotNull(fieldInfo);
    // Add more assertions as needed
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeForInvalidArray() {
    TypeInfoFactory.getTypeInfoForArray(Object.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeForInvalidField() throws NoSuchFieldException {
    TypeInfoFactory.getTypeInfoForField(MyClass.class.getDeclaredField("nonExistentField"), MyClass.class);
  }

  // Add more test cases as needed

  static class MyClass {
    private String myField;
  }
}