import static org.junit.Assert.*;
import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import com.google.gson.TypeInfoImpl;
import com.google.gson.ParameterizedTypeImpl;
import com.google.gson.GenericArrayTypeImpl;
import com.google.gson.TypeVariable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

final class TypeInfoFactoryTest {

  @org.junit.Test
  public void testTypeInfoForArray() {
    Type type = new int[0].getClass().getComponentType();
    TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
    assertInstanceof(TypeInfoArray.class, typeInfoArray);
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testTypeInfoForArrayWithNonArrayType() {
    Type type = String.class;
    TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
    fail("Expected AssertionError but got: " + typeInfoArray);
  }

  @org.junit.Test
  public void testTypeInfoForField() throws NoSuchFieldException, IllegalAccessException {
    Class<?> clazz = Integer[].class;
    Field field = clazz.getDeclaredField("length");
    Type type = field.getGenericType();
    Type typeDefiningF = clazz.getGenericSuperclass();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
    assertInstanceof(TypeInfoImpl.class, typeInfo);
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testTypeInfoForFieldWithNonGenericField() throws NoSuchFieldException, IllegalAccessException {
    Class<?> clazz = Integer[].class;
    Field field = clazz.getDeclaredField("class");
    Type type = field.getGenericType();
    Type typeDefiningF = clazz.getGenericSuperclass();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
    fail("Expected AssertionError but got: " + typeInfo);
  }

  @org.junit.Test
  public void testGetActualType() {
    Type fieldTypeVariable = TypeVariable.of(String.class, "T");
    Class<?> rawParentClass = Integer[].class;
    Type parentType = rawParentClass.getGenericSuperclass();
    Type actualType = TypeInfoFactory.getActualType(fieldTypeVariable, parentType, rawParentClass);
    assertInstanceof(String.class, actualType);
  }

  @org.junit.Test(expected = UnsupportedOperationException.class)
  public void testGetActualTypeWithParentTypeNotParameterizedType() {
    Type fieldTypeVariable = TypeVariable.of(String.class, "T");
    Class<?> rawParentClass = Object.class;
    Type parentType = rawParentClass.getGenericSuperclass();
    Type actualType = TypeInfoFactory.getActualType(fieldTypeVariable, parentType, rawParentClass);
    fail("Expected UnsupportedOperationException but got: " + actualType);
  }

  @org.junit.Test
  public void testExtractRealTypes() {
    Type[] actualTypeArguments = new Type[]{Integer.class, String.class};
    Type parentType = Integer[].class.getGenericSuperclass();
    Class<?> rawParentClass = Integer[].class;
    Type[] retTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
    assertArrayEquals(new Type[]{Integer.class, String.class}, retTypes);
  }

  @org.junit.Test(expected = UnsupportedOperationException.class)
  public void testExtractRealTypesWithParentTypeNotParameterizedType() {
    Type[] actualTypeArguments = new Type[]{Integer.class, String.class};
    Type parentType = Object.class;
    Class<?> rawParentClass = Integer[].class;
    Type[] retTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
    fail("Expected UnsupportedOperationException but got: " + retTypes);
  }

  @org.junit.Test
  public void testGetIndex() {
    TypeVariable<?>[] types = new TypeVariable<?>[]{TypeVariable.of(Integer.class, "T"), TypeVariable.of(String.class, "S")};
    TypeVariable<?> type = TypeVariable.of(String.class, "S");
    int index = TypeInfoFactory.getIndex(types, type);
    assertEquals(1, index);
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testGetIndexWithTypeVariableNotFound() {
    TypeVariable<?>[] types = new TypeVariable<?>[]{TypeVariable.of(Integer.class, "T")};
    TypeVariable<?> type = TypeVariable.of(String.class, "S");
    TypeInfoFactory.getIndex(types, type);
    fail("Expected IllegalStateException but got: N/A");
  }
}