import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

  @Test
  public void testGetTypeInfoForArray() {
    // Test case 1
    Type type1 = String[].class;
    TypeInfoArray typeInfo1 = TypeInfoFactory.getTypeInfoForArray(type1);
    assertEquals(type1, typeInfo1.getType());
    
    // Test case 2
    Type type2 = int[].class;
    TypeInfoArray typeInfo2 = TypeInfoFactory.getTypeInfoForArray(type2);
    assertEquals(type2, typeInfo2.getType());
    
    // Test case 3
    Type type3 = Object[].class;
    TypeInfoArray typeInfo3 = TypeInfoFactory.getTypeInfoForArray(type3);
    assertEquals(type3, typeInfo3.getType());
  }

  @Test
  public void testGetTypeInfoForField() {
    // Test case 1
    Field field1 = MyClass.class.getDeclaredField("myField1");
    Type type1 = MyClass.class;
    TypeInfo typeInfo1 = TypeInfoFactory.getTypeInfoForField(field1, type1);
    assertEquals(field1.getGenericType(), typeInfo1.getActualType());

    // Test case 2
    Field field2 = MyClass.class.getDeclaredField("myField2");
    Type type2 = MyClass.class;
    TypeInfo typeInfo2 = TypeInfoFactory.getTypeInfoForField(field2, type2);
    assertEquals(field2.getGenericType(), typeInfo2.getActualType());

    // Test case 3
    Field field3 = MyClass.class.getDeclaredField("myField3");
    Type type3 = MyClass.class;
    TypeInfo typeInfo3 = TypeInfoFactory.getTypeInfoForField(field3, type3);
    assertEquals(field3.getGenericType(), typeInfo3.getActualType());
  }

  // Test helper class
  private static class MyClass {
    private List<String> myField1;
    private List<Integer> myField2;
    private List<Object> myField3;
  }

  @Test
  public void testGetActualType() {
    // Test case 1
    Type type1 = String.class;
    Type parentType1 = null;
    Class<?> rawParentClass1 = null;
    Type actualType1 = TypeInfoFactory.getActualType(type1, parentType1, rawParentClass1);
    assertEquals(type1, actualType1);

    // Test case 2
    Type type2 = new ParameterizedTypeImpl(List.class, new Type[] {String.class}, null);
    Type parentType2 = null;
    Class<?> rawParentClass2 = null;
    Type actualType2 = TypeInfoFactory.getActualType(type2, parentType2, rawParentClass2);
    assertEquals(String.class, actualType2);

    // Test case 3
    Type type3 = new GenericArrayTypeImpl(new ParameterizedTypeImpl(List.class, new Type[] {String.class}, null));
    Type parentType3 = null;
    Class<?> rawParentClass3 = null;
    Type actualType3 = TypeInfoFactory.getActualType(type3, parentType3, rawParentClass3);
    assertEquals(String.class, actualType3);

    // Test case 4
    Type type4 = new TypeVariableImpl("T", new Type[] {String.class});
    Type parentType4 = new ParameterizedTypeImpl(List.class, new Type[] {String.class}, null);
    Class<?> rawParentClass4 = List.class;
    Type actualType4 = TypeInfoFactory.getActualType(type4, parentType4, rawParentClass4);
    assertEquals(String.class, actualType4);

    // Test case 5
    Type type5 = new WildcardTypeImpl(new Type[] {String.class});
    Type parentType5 = new ParameterizedTypeImpl(List.class, new Type[] {String.class}, null);
    Class<?> rawParentClass5 = List.class;
    Type actualType5 = TypeInfoFactory.getActualType(type5, parentType5, rawParentClass5);
    assertEquals(String.class, actualType5);
  }

  @Test
  public void testExtractRealTypes() {
    // Test case 1
    Type[] types1 = new Type[] {String.class, Integer.class};
    Type parentType1 = null;
    Class<?> rawParentClass1 = null;
    Type[] actualTypes1 = TypeInfoFactory.extractRealTypes(types1, parentType1, rawParentClass1);
    assertArrayEquals(types1, actualTypes1);

    // Test case 2
    Type[] types2 = new Type[] {new TypeVariableImpl("T", new Type[] {String.class}), Integer.class};
    Type parentType2 = new ParameterizedTypeImpl(List.class, new Type[] {String.class}, null);
    Class<?> rawParentClass2 = List.class;
    Type[] actualTypes2 = TypeInfoFactory.extractRealTypes(types2, parentType2, rawParentClass2);
    Type[] expectedTypes2 = new Type[] {String.class, Integer.class};
    assertArrayEquals(expectedTypes2, actualTypes2);

    // Test case 3
    Type[] types3 = new Type[] {new WildcardTypeImpl(new Type[] {String.class}), Integer.class};
    Type parentType3 = new ParameterizedTypeImpl(List.class, new Type[] {String.class}, null);
    Class<?> rawParentClass3 = List.class;
    Type[] actualTypes3 = TypeInfoFactory.extractRealTypes(types3, parentType3, rawParentClass3);
    Type[] expectedTypes3 = new Type[] {String.class, Integer.class};
    assertArrayEquals(expectedTypes3, actualTypes3);
  }

  @Test
  public void testGetIndex() {
    // Test case 1
    TypeVariable<?>[] types1 = new TypeVariable<?>[] {new TypeVariableImpl("T", new Type[] {String.class}), new TypeVariableImpl("U", new Type[] {Integer.class})};
    TypeVariable<?> type1 = new TypeVariableImpl("T", new Type[] {String.class});
    int index1 = TypeInfoFactory.getIndex(types1, type1);
    assertEquals(0, index1);

    // Test case 2
    TypeVariable<?>[] types2 = new TypeVariable<?>[] {new TypeVariableImpl("T", new Type[] {String.class}), new TypeVariableImpl("U", new Type[] {Integer.class})};
    TypeVariable<?> type2 = new TypeVariableImpl("U", new Type[] {Integer.class});
    int index2 = TypeInfoFactory.getIndex(types2, type2);
    assertEquals(1, index2);

    // Test case 3
    TypeVariable<?>[] types3 = new TypeVariable<?>[] {new TypeVariableImpl("T", new Type[] {String.class}), new TypeVariableImpl("U", new Type[] {Integer.class})};
    TypeVariable<?> type3 = new TypeVariableImpl("V", new Type[] {Double.class});
    try {
      TypeInfoFactory.getIndex(types3, type3);
      fail("Expected IllegalStateException to be thrown");
    } catch (IllegalStateException e) {
      assertEquals("How can the type variable not be present in the class declaration!", e.getMessage());
    }
  }

  // Helper classes for testing
  private static class ParameterizedTypeImpl implements ParameterizedType {
    private final Type rawType;
    private final Type[] actualTypeArguments;
    private final Type ownerType;

    public ParameterizedTypeImpl(Type rawType, Type[] actualTypeArguments, Type ownerType) {
      this.rawType = rawType;
      this.actualTypeArguments = actualTypeArguments;
      this.ownerType = ownerType;
    }

    @Override
    public Type[] getActualTypeArguments() {
      return actualTypeArguments;
    }

    @Override
    public Type getRawType() {
      return rawType;
    }

    @Override
    public Type getOwnerType() {
      return ownerType;
    }
  }

  private static class GenericArrayTypeImpl implements GenericArrayType {
    private final Type genericComponentType;

    public GenericArrayTypeImpl(Type genericComponentType) {
      this.genericComponentType = genericComponentType;
    }

    @Override
    public Type getGenericComponentType() {
      return genericComponentType;
    }
  }

  private static class TypeVariableImpl implements TypeVariable<?> {
    private final String name;
    private final Type[] bounds;

    public TypeVariableImpl(String name, Type[] bounds) {
      this.name = name;
      this.bounds = bounds;
    }

    @Override
    public Type[] getBounds() {
      return bounds;
    }

    @Override
    public D getGenericDeclaration() {
      return null;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public String getTypeName() {
      return name;
    }
  }

  private static class WildcardTypeImpl implements WildcardType {
    private final Type[] upperBounds;

    public WildcardTypeImpl(Type[] upperBounds) {
      this.upperBounds = upperBounds;
    }

    @Override
    public Type[] getUpperBounds() {
      return upperBounds;
    }

    @Override
    public Type[] getLowerBounds() {
      return new Type[0];
    }
  }

}