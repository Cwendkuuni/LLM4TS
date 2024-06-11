import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

  @Test
  public void testGetTypeInfoForArray() {
    Type type = new Type() {};

    TypeInfoArray result = TypeInfoFactory.getTypeInfoForArray(type);

    assertNotNull(result);
  }

  @Test
  public void testGetTypeInfoForField() {
    Field f = new Field();
    Type typeDefiningF = new Type() {};

    TypeInfo result = TypeInfoFactory.getTypeInfoForField(f, typeDefiningF);

    assertNotNull(result);
  }
}