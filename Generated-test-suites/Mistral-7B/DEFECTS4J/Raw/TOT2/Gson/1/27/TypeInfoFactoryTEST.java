import static org.junit.Assert.*;
import com.google.gson.TypeInfoFactory;
import com.google.gson.TypeInfo;

public class TypeInfoFactoryTest {
  @Test
  public void testTypeInfoForArray() {
    Type type = new int[5].getClass(); // an integer array
    TypeInfo expectedTypeInfo = new TypeInfo(int[].class);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(type);
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }
}