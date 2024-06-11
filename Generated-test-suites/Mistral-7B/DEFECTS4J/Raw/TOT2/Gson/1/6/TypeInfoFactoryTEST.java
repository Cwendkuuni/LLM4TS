import static org.junit.Assert.assertInstanceOf;
import com.google.gson.TypeInfoArray;
import com.google.gson.TypeInfoFactory;
import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[10].getClass();
    TypeInfoArray typeInfo = TypeInfoFactory.getTypeInfoForArray(type);
    assertInstanceOf(TypeInfoArray.class, typeInfo);
  }
}