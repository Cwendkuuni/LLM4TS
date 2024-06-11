
import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import com.google.gson.Preconditions;
import com.google.gson.TypeUtils;
import com.google.gson.ParameterizedTypeImpl;
import com.google.gson.GenericArrayTypeImpl;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public class TypeInfoFactoryTest {
  
  @Test
  public void testGetTypeInfoForArray() {
    Type type = new TypeInfoArray();
    TypeInfoArray result = TypeInfoFactory.getTypeInfoForArray(type);
    assertEquals(type, result.getType());
  }
  
  @Test
  public void testGetTypeInfoForField() {
    Field f = new Field();
    Type typeDefiningF = new Type();
    TypeInfo result = TypeInfoFactory.getTypeInfoForField(f, typeDefiningF);
    assertEquals(f.getGenericType(), result.getType());
  }
  
  @Test
  public void testGetActualType_ClassType() {
    Type typeToEvaluate = new Type();
    Type parentType = new Type();
    Class<?> rawParentClass = new Class<>();
    Type result = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    assertEquals(typeToEvaluate, result);
  }
  
  @Test
  public void testGetActualType_ParameterizedType() {
    Type typeToEvaluate = new ParameterizedTypeImpl();
    Type parentType = new Type();
    Class<?> rawParentClass = new Class<>();
    Type result = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    assertEquals(typeToEvaluate.getRawType(), result.getRawType());
  }
  
  @Test
  public void testGetActualType_GenericArrayType() {
    Type typeToEvaluate = new GenericArrayTypeImpl();
    Type parentType = new Type();
    Class<?> rawParentClass = new Class<>();
    Type result = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    assertEquals(typeToEvaluate.getGenericComponentType(), result.getGenericComponentType());
  }
  
  @Test
  public void testGetActualType_TypeVariable() {
    Type typeToEvaluate = new TypeVariable<>();
    Type parentType = new ParameterizedTypeImpl();
    Class<?> rawParentClass = new Class<>();
    Type result = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    assertEquals(parentType.getActualTypeArguments()[0], result);
  }
  
  @Test
  public void testGetActualType_WildcardType() {
    Type typeToEvaluate = new WildcardType();
    Type parentType = new Type();
    Class<?> rawParentClass = new Class<>();
    Type result = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    assertEquals(typeToEvaluate.getUpperBounds()[0], result.getUpperBounds()[0]);
  }
  
  @Test
  public void testGetActualType_InvalidType() {
    Type typeToEvaluate = new InvalidType();
    Type parentType = new Type();
    Class<?> rawParentClass = new Class<>();
    assertThrows(IllegalArgumentException.class, () -> 
      TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass));
  }
  
  @Test
  public void testExtractRealTypes() {
    Type[] actualTypeArguments = new Type[]{new Type(), new Type()};
    Type parentType = new ParameterizedTypeImpl();
    Class<?> rawParentClass = new Class<>();
    Type[] expected = new Type[]{parentType.getActualTypeArguments()[0], parentType.getActualTypeArguments()[1]};
    Type[] result = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testGetIndex_TypePresent() {
    TypeVariable<?>[] types = new TypeVariable[]{new TypeVariable<>()};
    TypeVariable<?> type = types[0];
    int result = TypeInfoFactory.getIndex(types, type);
    assertEquals(0, result);
  }
  
  @Test
  public void testGetIndex_TypeNotPresent() {
    TypeVariable<?>[] types = new TypeVariable[]{new TypeVariable<>()};
    TypeVariable<?> type = new TypeVariable<>();
    assertThrows(IllegalStateException.class, () -> 
      TypeInfoFactory.getIndex(types, type));
  }
}
