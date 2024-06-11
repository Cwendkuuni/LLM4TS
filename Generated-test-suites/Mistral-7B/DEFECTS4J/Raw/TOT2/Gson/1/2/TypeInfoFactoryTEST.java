import static org.junit.Assert.assertNotNull;
import com.google.gson.TypeInfoFactory;
import com.google.gson.TypeInfoArray;
import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[10].getClass();
    TypeInfoArray info = TypeInfoFactory.getTypeInfoForArray(type);
    assertNotNull(info);
  }
}