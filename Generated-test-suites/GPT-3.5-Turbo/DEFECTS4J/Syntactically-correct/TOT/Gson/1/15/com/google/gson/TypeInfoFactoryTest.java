import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

  @Test
  public void testGetTypeInfoForArray() {
    // Typical use case
    Type type = new Type() {};
    TypeInfoArray result = TypeInfoFactory.getTypeInfoForArray(type);
    assertNotNull(result);
    // Edge case
    Type nullType = null;
    try {
      result = TypeInfoFactory.getTypeInfoForArray(nullType);
      fail("NullPointerException not thrown");
    } catch (NullPointerException e) {
      // Exception expected
    }
  }

  @Test
  public void testGetTypeInfoForField() {
    // Typical use case
    Field f = new Field();
    Type typeDefiningF = new Type() {};
    TypeInfo result = TypeInfoFactory.getTypeInfoForField(f, typeDefiningF);
    assertNotNull(result);
    // Edge case
    Field nullField = null;
    try {
      result = TypeInfoFactory.getTypeInfoForField(nullField, typeDefiningF);
      fail("NullPointerException not thrown");
    } catch (NullPointerException e) {
      // Exception expected
    }
  }

  @Test
  public void testGetActualType() {
    // Typical use case
    Type typeToEvaluate = new Type() {};
    Type parentType = new Type() {};
    Class<?> rawParentClass = TypeInfoFactory.class;
    Type result = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    assertNotNull(result);
    // Edge case
    Type nullType = null;
    try {
      result = TypeInfoFactory.getActualType(nullType, parentType, rawParentClass);
      fail("NullPointerException not thrown");
    } catch (NullPointerException e) {
      // Exception expected
    }
  }

  @Test
  public void testExtractRealTypes() {
    // Typical use case
    Type[] actualTypeArguments = new Type[2];
    actualTypeArguments[0] = new Type() {};
    actualTypeArguments[1] = new Type() {};
    Type parentType = new Type() {};
    Class<?> rawParentClass = TypeInfoFactory.class;
    Type[] result = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
    assertNotNull(result);
    assertEquals(2, result.length);
    // Edge case
    Type[] nullTypeArguments = null;
    try {
      result = TypeInfoFactory.extractRealTypes(nullTypeArguments, parentType, rawParentClass);
      fail("NullPointerException not thrown");
    } catch (NullPointerException e) {
      // Exception expected
    }
  }
}