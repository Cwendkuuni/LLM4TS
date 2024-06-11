import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

  @Test
  public void testGetTypeInfoForArray() {
    // Typical use case
    TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(int[].class);
    assertNotNull(typeInfoArray);
    
    // Edge case: Empty array
    TypeInfoArray typeInfoArrayEmpty = TypeInfoFactory.getTypeInfoForArray(new int[0]);
    assertNotNull(typeInfoArrayEmpty);
    
    // Error scenario: Passing non-array type
    try {
        TypeInfoFactory.getTypeInfoForArray(Integer.class);
        fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
        // As expected
    }
  }

  @Test
  public void testGetTypeInfoForField() {
    // Typical use case
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(TestClass.class.getDeclaredField("testField"), TestClass.class);
    assertNotNull(typeInfo);
    
    // Edge case: Field type is TypeVariable
    TypeInfo typeInfoTypeVariable = TypeInfoFactory.getTypeInfoForField(TestClass.class.getDeclaredField("testFieldWithTypeVariable"), TestClass.class);
    assertNotNull(typeInfoTypeVariable);

    // Error scenario: Passing null field
    try {
        TypeInfoFactory.getTypeInfoForField(null, TestClass.class);
        fail("Expected NullPointerException");
    } catch (NullPointerException e) {
        // As expected
    }

    // Error scenario: Passing null typeDefiningF
    try {
        TypeInfoFactory.getTypeInfoForField(TestClass.class.getDeclaredField("testField"), null);
        fail("Expected NullPointerException");
    } catch (NullPointerException e) {
        // As expected
    }
  }

  public static class TestClass {
    private int testField;
    
    private TypeVariable<String> testFieldWithTypeVariable;
  }

}