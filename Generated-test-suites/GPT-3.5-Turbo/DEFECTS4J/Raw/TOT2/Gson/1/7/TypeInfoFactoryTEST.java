import static org.junit.Assert.*;
import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testGetTypeInfoForArray() {
    // Typical use case
    Type type = new Type[] {};
    assertNotNull(TypeInfoFactory.getTypeInfoForArray(type));

    // Edge case
    type = null;
    try {
      TypeInfoFactory.getTypeInfoForArray(type);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected exception
    }
  }

  @Test
  public void testGetTypeInfoForField() {
    // Typical use case
    Field field = TypeInfoFactory.class.getDeclaredFields()[0];
    Type typeDefiningF = TypeInfoFactory.class;
    assertNotNull(TypeInfoFactory.getTypeInfoForField(field, typeDefiningF));

    // Edge case
    field = null;
    typeDefiningF = TypeInfoFactory.class;
    try {
      TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Expected exception
    }
  }

  @Test
  public void testGetActualType() {
    // Typical use case
    Type typeToEvaluate = TypeInfoFactory.class;
    Type parentType = Object.class;
    Class<?> rawParentClass = Object.class;
    assertNotNull(TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass));

    // Edge case
    typeToEvaluate = null;
    parentType = null;
    rawParentClass = null;
    try {
      TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Expected exception
    }
  }

  @Test
  public void testExtractRealTypes() {
    // Typical use case
    Type[] actualTypeArguments = new Type[] {Object.class};
    Type parentType = Object.class;
    Class<?> rawParentClass = Object.class;
    assertNotNull(TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass));

    // Edge case
    actualTypeArguments = null;
    parentType = null;
    rawParentClass = null;
    try {
      TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Expected exception
    }
  }

  @Test
  public void testGetIndex() {
    // Typical use case
    TypeVariable<?>[] types = TypeInfoFactory.class.getTypeParameters();
    TypeVariable<?> type = TypeInfoFactory.class.getTypeParameters()[0];
    assertNotNull(TypeInfoFactory.getIndex(types, type));

    // Edge case
    types = null;
    type = null;
    try {
      TypeInfoFactory.getIndex(types, type);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Expected exception
    }
  }
}