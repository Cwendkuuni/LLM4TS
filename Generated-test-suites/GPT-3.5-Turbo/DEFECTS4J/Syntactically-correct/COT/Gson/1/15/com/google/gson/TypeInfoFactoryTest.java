
import org.junit.Assert;
import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class TypeInfoFactoryTest {
  
  @Test
  public void testGetTypeInfoForArray() {
    Type type = new TypeLiteral<String[]>() {}.getType();
    TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
    Assert.assertEquals(String[].class, typeInfoArray.getType());
  }

  @Test
  public void testGetTypeInfoForField() throws NoSuchFieldException {
    Field field = MyClass.class.getDeclaredField("myField");
    Type typeDefiningF = MyClass.class;
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
    Assert.assertEquals(String.class, typeInfo.getType());
  }
  
  // TODO: Generate test cases for potential edge cases and exceptions
  
}

class MyClass {
  public String myField;
}
