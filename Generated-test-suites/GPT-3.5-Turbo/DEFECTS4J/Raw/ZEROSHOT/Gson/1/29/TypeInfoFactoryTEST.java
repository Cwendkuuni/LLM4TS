import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

  @Test
  public void testGetTypeInfoForArray() {
    Type[] types = new Type[]{Integer[].class, String[].class, Double[].class};
    for (Type type : types) {
      TypeInfoArray arrayTypeInfo = TypeInfoFactory.getTypeInfoForArray(type);
      assertNotNull(arrayTypeInfo);
      assertEquals(type, arrayTypeInfo.getType());
    }
  }

  @Test
  public void testGetTypeInfoForField() throws NoSuchFieldException {
    Class<TestObject> clazz = TestObject.class;
    Field intField = clazz.getDeclaredField("intField");
    TypeInfo intTypeInfo = TypeInfoFactory.getTypeInfoForField(intField, clazz);
    assertNotNull(intTypeInfo);
    assertEquals(intField.getGenericType(), intTypeInfo.getType());

    Field stringField = clazz.getDeclaredField("stringField");
    TypeInfo stringTypeInfo = TypeInfoFactory.getTypeInfoForField(stringField, clazz);
    assertNotNull(stringTypeInfo);
    assertEquals(stringField.getGenericType(), stringTypeInfo.getType());

    Field objectField = clazz.getDeclaredField("objectField");
    TypeInfo objectTypeInfo = TypeInfoFactory.getTypeInfoForField(objectField, clazz);
    assertNotNull(objectTypeInfo);
    assertEquals(objectField.getGenericType(), objectTypeInfo.getType());
  }

  @Test
  public void testGetActualType() {
    // TODO: Implement test cases for getActualType method
  }

  @Test
  public void testExtractRealTypes() {
    // TODO: Implement test cases for extractRealTypes method
  }

  @Test
  public void testGetIndex() {
    // TODO: Implement test cases for getIndex method
  }

}