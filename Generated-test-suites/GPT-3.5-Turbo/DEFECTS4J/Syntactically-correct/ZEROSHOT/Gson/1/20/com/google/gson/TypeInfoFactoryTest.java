import static org.junit.Assert.*;
import org.junit.Test;

public class TypeInfoFactoryTest {
  
  @Test
  public void testGetTypeInfoForArray() {
    TypeInfoArray typeInfo = TypeInfoFactory.getTypeInfoForArray(new Object[10].getClass());
    assertNotNull(typeInfo);
    assertEquals(Object[].class, typeInfo.getType());
  }
  
  @Test
  public void testGetTypeInfoForField() throws NoSuchFieldException {
    Field field = TestClass.class.getDeclaredField("stringField");
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, TestClass.class);
    assertNotNull(typeInfo);
    assertEquals(String.class, typeInfo.getType());
  }
  
  @Test
  public void testGetActualType_withClass() {
    Type actualType = TypeInfoFactory.getActualType(Integer.class, null, null);
    assertEquals(Integer.class, actualType);
  }
  
  @Test
  public void testGetActualType_withParameterizedType() {
    ParameterizedType parameterizedType = new ParameterizedType() {
      public Type[] getActualTypeArguments() { return new Type[] {String.class, Integer.class}; }
      public Type getRawType() { return TestClass.class; }
      public Type getOwnerType() { return null; }
    };
    Type actualType = TypeInfoFactory.getActualType(parameterizedType, null, null);
    assertEquals(parameterizedType, actualType);
  }
  
  @Test
  public void testGetActualType_withGenericArrayType() {
    GenericArrayType genericArrayType = new GenericArrayType() {
      public Type getGenericComponentType() { return Integer.class; }
    };
    Type actualType = TypeInfoFactory.getActualType(genericArrayType, null, null);
    assertEquals(genericArrayType, actualType);
  }
  
  @Test
  public void testGetActualType_withTypeVariable() {
    TypeVariable<?> typeVariable = String.class.getTypeParameters()[0];
    ParameterizedType parameterizedType = new ParameterizedType() {
      public Type[] getActualTypeArguments() { return new Type[] {String.class, Integer.class}; }
      public Type getRawType() { return TestClass.class; }
      public Type getOwnerType() { return null; }
    };
    Type actualType = TypeInfoFactory.getActualType(typeVariable, parameterizedType, TestClass.class);
    assertEquals(Integer.class, actualType);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualType_withUnsupportedTypeVariable() {
    TypeVariable<?> typeVariable = String.class.getTypeParameters()[0];
    TypeInfoFactory.getActualType(typeVariable, Integer.class, Integer.class);
  }
  
  @Test
  public void testGetActualType_withWildcardType() {
    WildcardType wildcardType = new WildcardType() {
      public Type[] getUpperBounds() { return new Type[] {Number.class}; }
      public Type[] getLowerBounds() { return new Type[0]; }
    };
    Type actualType = TypeInfoFactory.getActualType(wildcardType, null, null);
    assertEquals(Number.class, actualType);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetActualType_withInvalidType() {
    Type invalidType = new Type() {};
    TypeInfoFactory.getActualType(invalidType, null, null);
  }
  
  @Test
  public void testExtractRealTypes() {
    Type[] actualTypeArguments = new Type[] {String.class, Integer.class};
    Type[] realTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, null, null);
    assertNotNull(realTypes);
    assertEquals(String.class, realTypes[0]);
    assertEquals(Integer.class, realTypes[1]);
  }
  
  @Test
  public void testGetIndex() {
    TypeVariable<?>[] types = TestClass.class.getTypeParameters();
    int index = TypeInfoFactory.getIndex(types, types[0]);
    assertEquals(0, index);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testGetIndex_withMissingTypeVariable() {
    TypeInfoFactory.getIndex(new TypeVariable<?>[] {}, new TypeVariableImpl<>());
  }
  
  // Helper class for testing
  private static class TestClass<T> {
    private String stringField;
    private T genericField;
  }
  
  // Dummy implementation of TypeVariable
  private static class TypeVariableImpl<T> implements TypeVariable<T> {
    public Type[] getBounds() { return new Type[0]; }
    public AnnotatedType[] getAnnotatedBounds() { return new AnnotatedType[0]; }
    public T getGenericDeclaration() { return null; }
    public String getName() { return null; }
    public String getTypeName() { return null; }
    public String toString() { return null; }
    public <D extends GenericDeclaration> D getGenericDeclaration(Class<D> type) { return null; }
    public Annotation[] getDeclaredAnnotations() { return new Annotation[0]; }
    public <A extends Annotation> A getDeclaredAnnotation(Class<A> annotationClass) { return null; }
    public <A extends Annotation> A[] getDeclaredAnnotationsByType(Class<A> annotationClass) { return null; }
    public Annotation[] getAnnotations() { return new Annotation[0]; }
    public <A extends Annotation> A getAnnotation(Class<A> annotationClass) { return null; }
    public <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationClass) { return null; }
    public <D extends GenericDeclaration> A getAnnotatedActualTypeArgument(Class<D> type, int index) { return null; }
  }
  
}