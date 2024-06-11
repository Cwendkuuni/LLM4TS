import static org.junit.Assert.*;
import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testGetTypeInfoForArray() {
    Type type = new TypeToken<String[]>(){}.getType();
    TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
    assertNotNull(typeInfoArray);
  }

  @Test
  public void testGetTypeForField() {
    Type type = new TypeToken<String>(){}.getType();
    Field field = SomeClass.class.getField("someField");
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, type);
    assertNotNull(typeInfo);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeForFieldWithInvalidType() {
    Type type = new TypeToken<String>(){}.getType();
    Field field = SomeClass.class.getDeclaredField("invalidField");
    TypeInfoFactory.getTypeInfoForField(field, type);
  }

  @Test
  public void testGetActualTypeWithClass() {
    Type type = String.class;
    Type actualType = TypeInfoFactory.getActualType(type, SomeClass.class, String.class);
    assertEquals(type, actualType);
  }

  @Test
  public void testGetActualTypeWithParameterizedType() {
    Type type = new TypeToken<List<String>>(){}.getType();
    Type actualType = TypeInfoFactory.getActualType(type, SomeClass.class, List.class);
    assertNotNull(actualType);
  }

  @Test
  public void testGetActualTypeWithTypeVariable() {
    Type type = new TypeToken<T>(){}.getType();
    Type actualType = TypeInfoFactory.getActualType(type, SomeClass.class, T.class);
    assertNotNull(actualType);
  }

  @Test
  public void testGetActualTypeWithWildcardType() {
    Type type = new TypeToken<List<?>>(){}.getType();
    Type actualType = TypeInfoFactory.getActualType(type, SomeClass.class, List.class);
    assertNotNull(actualType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetActualTypeWithInvalidType() {
    TypeInfoFactory.getActualType(StringBuilder.class, SomeClass.class, StringBuilder.class);
  }

  @Test
  public void testExtractRealTypes() {
    Type type = new TypeToken<List<String>>(){}.getType();
    Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
    Type[] realTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, SomeClass.class, List.class);
    assertEquals(1, realTypes.length);
  }

  @Test
  public void testGetIndex() {
    TypeVariable<?>[] types = SomeClass.class.getTypeParameters();
    TypeVariable<?> type = types[0];
    int index = TypeInfoFactory.getIndex(types, type);
    assertEquals(0, index);
  }
}