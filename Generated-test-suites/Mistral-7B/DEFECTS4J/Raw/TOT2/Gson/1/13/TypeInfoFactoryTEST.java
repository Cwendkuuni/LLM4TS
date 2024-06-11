import static org.junit.Assert.*;
import com.google.gson.TypeInfoArray;
import com.google.gson.TypeInfoFactory;
import java.lang.reflect.Type;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type integerTypeArray = new TypeToken<int[]>() {}.getType();
    TypeInfoArray intArrayTypeInfo = TypeInfoFactory.getTypeInfoForArray(integerTypeArray);
    assertNotNull(intArrayTypeInfo);
    assertEquals(TypeInfo.ARRAY, intArrayTypeInfo.type);
  }
}