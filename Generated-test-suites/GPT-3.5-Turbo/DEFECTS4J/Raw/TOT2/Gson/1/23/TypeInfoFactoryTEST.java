import static org.junit.Assert.*;
import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testGetTypeInfoForArray() {
    Type type = new TypeToken<String[]>() {}.getType();
    TypeInfoArray result = TypeInfoFactory.getTypeInfoForArray(type);
    assertNotNull(result);
    assertEquals(String[].class, result.getRawType());
  }

  @Test
  public void testGetTypeInfoForField() {
    class TestClass {
      private List<String> list;
    }
    Field field = TestClass.class.getDeclaredField("list");
    Type type = TestClass.class.getDeclaredField("list").getGenericType();
    TypeInfo result = TypeInfoFactory.getTypeInfoForField(field, type);
    assertNotNull(result);
    assertEquals(new TypeToken<List<String>>() {}.getType(), result.getType());
  }
  
  @Test
  public void testGetActualType() {
    Type type = new TypeToken<List<String>>() {}.getType();
    Type actualType = TypeInfoFactory.getActualType(type, List.class, List.class.getClass());
    assertNotNull(actualType);
    assertEquals(new TypeToken<List<String>>() {}.getType(), actualType);
  }

  @Test
  public void testGetIndex() {
    TypeVariable<?>[] types = List.class.getTypeParameters();
    TypeVariable<?> type = (TypeVariable<?>) List.class.getTypeParameters()[0];
    int index = TypeInfoFactory.getIndex(types, type);
    assertEquals(0, index);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualTypeWithWrongInput() {
    Type type = new TypeToken<String>() {}.getType();
    TypeInfoFactory.getActualType(type, Type.class, Type.class.getClass());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetIndexWithWrongInput() {
    TypeVariable<?>[] types = List.class.getTypeParameters();
    TypeVariable<?> type = (TypeVariable<?>) String.class.getTypeParameters()[0];
    TypeInfoFactory.getIndex(types, type);
  }
}