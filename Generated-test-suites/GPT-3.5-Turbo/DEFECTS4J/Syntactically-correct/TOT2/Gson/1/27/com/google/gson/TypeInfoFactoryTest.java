import static org.junit.Assert.*;
import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testGetTypeInfoForArray() {
    Type type = new Type() {};
    TypeInfoArray infoArray = TypeInfoFactory.getTypeInfoForArray(type);

    // Test typical use case
    assertNotNull(infoArray);

    // Test edge case
    assertTrue(infoArray instanceof TypeInfoArray);
  }

  @Test
  public void testGetTypeInfoForField() {
    Field f = TypeInfoFactory.class.getDeclaredFields()[0];
    Type typeDefiningF = TypeInfoFactory.class;
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(f, typeDefiningF);

    // Test typical use case
    assertNotNull(info);

    // Test error scenario
    assertNull(TypeInfoFactory.getTypeInfoForField(null, null));
  }

  @Test
  public void testGetActualType() {
    Type typeToEvaluate = new ParameterizedType() {
      @Override
      public Type getRawType() {
        return null;
      }
      @Override
      public Type getOwnerType() {
        return null;
      }
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {};
      }
    };
    Type parentType = TypeInfoFactory.class;
    Class<?> rawParentClass = TypeInfoFactory.class;
    Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);

    // Test typical use case
    assertNotNull(actualType);

    // Test edge case
    assertEquals(actualType, typeToEvaluate);
  }

  @Test
  public void testExtractRealTypes() {
    Type[] actualTypeArguments = new Type[] {new Type() {}};
    Type parentType = TypeInfoFactory.class;
    Class<?> rawParentClass = TypeInfoFactory.class;
    Type[] realTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);

    // Test typical use case
    assertNotNull(realTypes);
    assertEquals(1, realTypes.length);

    // Test edge case
    assertTrue(realTypes[0] instanceof Type);
  }

  @Test
  public void testGetIndex() {
    TypeVariable<?>[] types = TypeInfoFactory.class.getTypeParameters();
    TypeVariable<?> type = types[0];
    int index = TypeInfoFactory.getIndex(types, type);

    // Test typical use case
    assertTrue(index >= 0);

    // Test edge case
    assertEquals(0, index);
  }
}