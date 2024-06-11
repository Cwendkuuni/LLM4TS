import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

  @Test
  public void testGetTypeInfoForArray() {
    Type type = new Type(){};
    TypeInfoArray result = TypeInfoFactory.getTypeInfoForArray(type);
    assertNotNull(result);
  }
  
  @Test
  public void testGetTypeInfoForField() {
    Field f = null;
    Type typeDefiningF = new Type(){};
    TypeInfo result = TypeInfoFactory.getTypeInfoForField(f, typeDefiningF);
    assertNotNull(result);
  }
  
  @Test
  public void testGetActualType_Class() {
    Type typeToEvaluate = new Type(){};
    Type parentType = new Type(){};
    Class<?> rawParentClass = TypeUtils.toRawClass(parentType);
    Type result = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    assertNotNull(result);
  }
  
  @Test
  public void testGetActualType_ParameterizedType() {
    Type typeToEvaluate = new ParameterizedTypeImpl(new Type(){}, new Type[]{}, new Type(){});
    Type parentType = new Type(){};
    Class<?> rawParentClass = TypeUtils.toRawClass(parentType);
    Type result = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    assertNotNull(result);
  }
  
  @Test
  public void testGetActualType_GenericArrayType() {
    Type typeToEvaluate = new GenericArrayTypeImpl(new Type(){});
    Type parentType = new Type(){};
    Class<?> rawParentClass = TypeUtils.toRawClass(parentType);
    Type result = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    assertNotNull(result);
  }
  
  @Test
  public void testGetActualType_TypeVariable() {
    Type typeToEvaluate = new TypeVariableImpl(new TypeVariable<>() {});
    Type parentType = new ParameterizedTypeImpl(new TypeVariable<>() {}, new Type[]{}, new Type(){});
    Class<?> rawParentClass = TypeUtils.toRawClass(parentType);
    Type result = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    assertNotNull(result);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualType_TypeVariable_UnsupportedOperationException() {
    Type typeToEvaluate = new TypeVariableImpl(new TypeVariable<>() {});
    Type parentType = new TypeVariableImpl(new TypeVariable<>() {});
    Class<?> rawParentClass = TypeUtils.toRawClass(parentType);
    TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
  }
  
  @Test
  public void testGetActualType_WildcardType() {
    Type typeToEvaluate = new WildcardTypeImpl(new Type(){}, new Type[]{});
    Type parentType = new Type(){};
    Class<?> rawParentClass = TypeUtils.toRawClass(typeToEvaluate);
    Type result = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    assertNotNull(result);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetActualType_InvalidType() {
    Type typeToEvaluate = new InvalidTypeImpl();
    Type parentType = new Type(){};
    Class<?> rawParentClass = TypeUtils.toRawClass(typeToEvaluate);
    TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
  }
  
  @Test
  public void testExtractRealTypes() {
    Type[] actualTypeArguments = new Type[]{new Type(){}};
    Type parentType = new Type(){};
    Class<?> rawParentClass = TypeUtils.toRawClass(parentType);
    Type[] result = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
    assertNotNull(result);
    assertEquals(1, result.length);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testGetIndex_InvalidTypeVariable() {
    TypeVariable<?>[] types = new TypeVariable[]{};
    TypeVariable<?> type = new TypeVariable<>() {};
    TypeInfoFactory.getIndex(types, type);
  }
}