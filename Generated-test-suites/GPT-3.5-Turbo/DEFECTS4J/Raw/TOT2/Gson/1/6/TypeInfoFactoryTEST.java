import static org.junit.Assert.*;
import org.junit.Test;

public class TypeInfoFactoryTest {
  
  @Test
  public void testGetTypeInfoForArray() {
    Type arrayType = new Type[]{}.getClass();
    TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(arrayType);
    assertNotNull(typeInfoArray);
    assertEquals(arrayType, typeInfoArray.getType());
  }
  
  @Test
  public void testGetTypeInfoForField() {
    class TestClass {
      public String testField;
    }
    
    Field field = TestClass.class.getDeclaredFields()[0];
    Type fieldType = field.getGenericType();
    Type declaringType = TestClass.class;
    
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, declaringType);
    assertNotNull(typeInfo);
    assertEquals(fieldType, typeInfo.getType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeInfoForArrayWithNonArrayType() {
    Type nonArrayType = String.class;
    TypeInfoFactory.getTypeInfoForArray(nonArrayType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetTypeInfoForFieldWithUnsupportedType() {
    class TestClass<T> {
      public T testField;
    }
    
    Field field = TestClass.class.getDeclaredFields()[0];
    Type fieldType = field.getGenericType();
    Type declaringType = TestClass.class;
    
    TypeInfoFactory.getTypeInfoForField(field, declaringType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeInfoForFieldWithInvalidType() {
    class TestClass {
      public List<String> testField;
    }
    
    Field field = TestClass.class.getDeclaredFields()[0];
    Type fieldType = field.getGenericType();
    Type declaringType = TestClass.class;
    
    TypeInfoFactory.getTypeInfoForField(field, declaringType);
  }
  
  // Add more test cases as needed

}