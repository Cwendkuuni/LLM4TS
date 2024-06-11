import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import com.google.gson.TypeInfoFactory;
import com.google.gson.TypeInfoArray;
import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[5].getClass();
    TypeInfoArray infoArray = TypeInfoFactory.TypeInfoForArray(type);
    assertEquals("int[]", infoArray.getType().toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForNullArray() {
    Type type = null;
    TypeInfoArray infoArray = TypeInfoFactory.TypeInfoForArray(type);
  }

  @Test
  public void testTypeInfoForNonArrayType() {
    Type type = Double.TYPE;
    assertNull(TypeInfoFactory.TypeInfoForArray(type));
  }

  @Test
  public void testTypeInfoForPrimitiveArray() {
    Type type = int[].class;
    TypeInfoArray infoArray = TypeInfoFactory.TypeInfoForArray(type);
    assertEquals("int[]", infoArray.getType().toString());
  }

  @Test
  public void testTypeInfoForMultidimensionalArray() {
    Type type = int[][][].class;
    TypeInfoArray infoArray = TypeInfoFactory.TypeInfoForArray(type);
    assertEquals("int[][][]", infoArray.getType().toString());
  }
}

//