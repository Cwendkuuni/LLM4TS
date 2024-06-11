
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

  @Test
  public void testGetTypeInfoForArray() {
    TypeInfoArray typeInfo = TypeInfoFactory.getTypeInfoForArray(int[].class);
    assertNotNull(typeInfo);
    assertEquals(int[].class, typeInfo.getType());
  }

  @Test
  public void testGetTypeInfoForField() throws NoSuchFieldException {
    Field field = MyClass.class.getDeclaredField("myField");
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, MyClass.class);
    assertNotNull(typeInfo);
    assertEquals(String.class, typeInfo.getType());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeInfoForArray_invalidType() {
    TypeInfoArray typeInfo = TypeInfoFactory.getTypeInfoForArray(String.class);
  }

  @Test(expected = NoSuchFieldException.class)
  public void testGetTypeInfoForField_invalidField() throws NoSuchFieldException {
    Field field = MyClass.class.getDeclaredField("invalidField");
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, MyClass.class);
  }
  
  private static class MyClass {
    private String myField;
  }
}
