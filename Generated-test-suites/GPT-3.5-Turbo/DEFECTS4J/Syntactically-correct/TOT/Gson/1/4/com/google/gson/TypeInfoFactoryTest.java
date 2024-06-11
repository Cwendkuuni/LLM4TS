import static org.junit.Assert.*;
import org.junit.Test;

public class TypeInfoFactoryTest {

  //Test case for getTypeInfoForArray
  @Test
  public void testGetTypeInfoForArray() {
    TypeInfoArray typeInfo = TypeInfoFactory.getTypeInfoForArray(String[].class);
    assertNotNull(typeInfo);
    assertEquals(String[].class, typeInfo.getType());
  }

  //Test case for getTypeInfoForField
  @Test
  public void testGetTypeInfoForField() {
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(Object.class.getDeclaredField("hashCode"), Object.class);
    assertNotNull(typeInfo);
    assertEquals(Object.class, typeInfo.getType());
  }

  //Test case for private constructor
  @Test(expected = IllegalAccessException.class)
  public void testPrivateConstructor() throws Exception {
    TypeInfoFactory.class.getDeclaredConstructor().newInstance();
  }

  //Test case for getActualType with Class type
  @Test
  public void testGetActualTypeWithClassType() {
    Type actualType = TypeInfoFactory.getActualType(Integer.class, null, null);
    assertNotNull(actualType);
    assertEquals(Integer.class, actualType);
  }

  //Test case for getActualType with ParameterizedType
  @Test
  public void testGetActualTypeWithParameterizedType() {
    ParameterizedType parameterizedType = new ParameterizedType() {
      public Type[] getActualTypeArguments() {
        return new Type[] {Integer.class};
      }
      
      public Type getRawType() {
        return List.class;
      }
      
      public Type getOwnerType() {
        return null;
      }
    };

    Type actualType = TypeInfoFactory.getActualType(parameterizedType, null, null);
    assertNotNull(actualType);
    assertTrue(actualType instanceof ParameterizedType);
    ParameterizedType actualParameterizedType = (ParameterizedType) actualType;
    assertEquals(List.class, actualParameterizedType.getRawType());
    assertEquals(Integer.class, actualParameterizedType.getActualTypeArguments()[0]);
  }

  //Test case for getActualType with GenericArrayType
  @Test
  public void testGetActualTypeWithGenericArrayType() {
    GenericArrayType genericArrayType = new GenericArrayType() {
      public Type getGenericComponentType() {
        return Integer.class;
      }
    };

    Type actualType = TypeInfoFactory.getActualType(genericArrayType, null, null);
    assertNotNull(actualType);
    assertTrue(actualType instanceof GenericArrayType);
    GenericArrayType actualArrayType = (GenericArrayType) actualType;
    assertEquals(Integer.class, actualArrayType.getGenericComponentType());
  }

  //Test case for getActualType with TypeVariable
  @Test
  public void testGetActualTypeWithTypeVariable() {
    TypeVariable<?> typeVariable = new TypeVariableImpl();
    ParameterizedType parameterizedType = new ParameterizedType() {
      public Type[] getActualTypeArguments() {
        return new Type[] {Integer.class};
      }
      
      public Type getRawType() {
        return List.class;
      }
      
      public Type getOwnerType() {
        return null;
      }
    };

    Type actualType = TypeInfoFactory.getActualType(typeVariable, parameterizedType, List.class);
    assertNotNull(actualType);
    assertEquals(Integer.class, actualType);
  }

  //Test case for getActualType with WildcardType
  @Test
  public void testGetActualTypeWithWildcardType() {
    WildcardType wildcardType = new WildcardType() {
      public Type[] getUpperBounds() {
        return new Type[] {Number.class};
      }
      
      public Type[] getLowerBounds() {
        return new Type[0];
      }
    };

    Type actualType = TypeInfoFactory.getActualType(wildcardType, null, null);
    assertNotNull(actualType);
    assertEquals(Number.class, actualType);
  }

  //Test case for getActualType with invalid type
  @Test(expected = IllegalArgumentException.class)
  public void testGetActualTypeWithInvalidType() {
    TypeInfoFactory.getActualType(new Object(), null, null);
  }

  //Test case for getIndex
  @Test
  public void testGetIndex() {
    TypeVariable<?>[] types = new TypeVariable<?>[] {new TypeVariableImpl()};
    int index = TypeInfoFactory.getIndex(types, new TypeVariableImpl());
    assertEquals(0, index);
  }

  //Test case for getIndex with invalid type
  @Test(expected = IllegalStateException.class)
  public void testGetIndexWithInvalidType() {
    TypeVariable<?>[] types = new TypeVariable<?>[] {new TypeVariableImpl()};
    TypeInfoFactory.getIndex(types, new TypeVariableImpl2());
  }

  private static class TypeVariableImpl implements TypeVariable<Object> {
    public Type[] getBounds() { return null; }
    public D getGenericDeclaration() { return null; }
    public String getName() { return null; }
    public AnnotatedType[] getAnnotatedBounds() { return null; }
    public String toString() { return null; }
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) { return null; }
    public Annotation[] getAnnotations() { return null; }
    public Annotation[] getDeclaredAnnotations() { return null; }
    public boolean equals(Object obj) { return false; }
    public int hashCode() { return 0; }
  }

  private static class TypeVariableImpl2 implements TypeVariable<Object> {
    public Type[] getBounds() { return null; }
    public D getGenericDeclaration() { return null; }
    public String getName() { return null; }
    public AnnotatedType[] getAnnotatedBounds() { return null; }
    public String toString() { return null; }
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) { return null; }
    public Annotation[] getAnnotations() { return null; }
    public Annotation[] getDeclaredAnnotations() { return null; }
    public boolean equals(Object obj) { return false; }
    public int hashCode() { return 0; }
  }

}