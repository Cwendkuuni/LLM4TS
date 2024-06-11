import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

  @Test
  public void testGetTypeInfoForArray() {
    Type arrayType = new TypeToken<String[]>() {}.getType();
    TypeInfoArray infoArray = TypeInfoFactory.getTypeInfoForArray(arrayType);
    assertNotNull(infoArray);
    assertEquals(arrayType, infoArray.getType());
  }

  @Test
  public void testGetTypeInfoForField() {
    class TestClass {
      List<String> listField;
    }
    
    Field field = TestClass.class.getDeclaredField("listField");
    Type type = TestClass.class.getDeclaredField("listField").getGenericType();
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, type);
    assertNotNull(info);
    assertEquals(List.class, info.getType());
  }

  @Test
  public void testGetActualType_Class() {
    class TestClass<T> {
      T field;
    }
    Field field = TestClass.class.getDeclaredField("field");
    Type type = TestClass.class.getDeclaredField("field").getGenericType();
    Type actualType = TypeInfoFactory.getActualType(type, TestClass.class, TestClass.class);
    assertNotNull(actualType);
    assertEquals(Object.class, actualType);
  }

  @Test
  public void testGetActualType_ParameterizedType() {
    class TestClass<T> {
      List<T> listField;
    }
    Field field = TestClass.class.getDeclaredField("listField");
    Type type = TestClass.class.getDeclaredField("listField").getGenericType();
    Type actualType = TypeInfoFactory.getActualType(type, TestClass.class, TestClass.class);
    assertNotNull(actualType);
    assertEquals(List.class, actualType);
  }

  @Test
  public void testGetActualType_GenericArrayType() {
    Type arrayType = new TypeToken<String[]>() {}.getType();
    Type actualType = TypeInfoFactory.getActualType(arrayType, arrayType, String[].class);
    assertNotNull(actualType);
    assertEquals(String.class, actualType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualType_TypeVariable() {
    class TestClass<T> {
      T field;
    }
    Field field = TestClass.class.getDeclaredField("field");
    Type type = TestClass.class.getDeclaredField("field").getGenericType();
    TypeInfoFactory.getActualType(type, TestClass.class, TestClass.class);
  }

}