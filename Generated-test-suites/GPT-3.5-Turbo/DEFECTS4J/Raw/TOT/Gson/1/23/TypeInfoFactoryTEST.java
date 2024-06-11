import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

  private TypeInfoArray typeInfoArray;
  private TypeInfo typeInfo;

  @Before
  public void setUp() {
    typeInfoArray = TypeInfoFactory.getTypeInfoForArray(String[].class);
    typeInfo = TypeInfoFactory.getTypeInfoForField(new Field(), ParentClass.class);
  }

  @Test
  public void testGetTypeInfoForArray() {
    assertNotNull(typeInfoArray);
    assertEquals(String[].class, typeInfoArray.getType());
  }

  @Test
  public void testGetTypeInfoForField() {
    assertNotNull(typeInfo);
  }

  @Test
  public void testGetTypeInfoForField_withTypeVariable() {
    TypeInfo typeInfoWithTypeVariable = TypeInfoFactory.getTypeInfoForField(new Field(), TypeVariableClass.class);
    assertNotNull(typeInfoWithTypeVariable);
    assertEquals(Integer.class, typeInfoWithTypeVariable.getType());
  }

  // Edge Case
  @Test
  public void testGetTypeInfoForField_withNonParameterizedType() {
    TypeInfo typeInfoWithNonParameterizedType = TypeInfoFactory.getTypeInfoForField(new Field(), NonParameterizedClass.class);
    assertNotNull(typeInfoWithNonParameterizedType);
  }

  // Error Scenario
  @Test(expected = UnsupportedOperationException.class)
  public void testGetTypeInfoForField_withTypeVariableAndNoParameterizedType() {
    TypeInfoFactory.getTypeInfoForField(new Field(), NoParameterizedClass.class);
  }
}

class ParentClass<T> {
  T field;
}

class TypeVariableClass extends ParentClass<Integer> {}

class NonParameterizedClass extends ParentClass {}

class NoParameterizedClass {}