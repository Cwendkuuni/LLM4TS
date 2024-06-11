import static org.junit.Assert.*;
import com.google.gson.TypeInfoFactory;
import com.google.gson.TypeInfoArray;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[10].getClass();
    TypeInfoArray expectedTypeInfo = new TypeInfoArray(type);
    TypeInfoArray actualTypeInfo = TypeInfoFactory.TypeInfoForArray(type);
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }
}