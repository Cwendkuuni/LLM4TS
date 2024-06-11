import static org.junit.Assert.*;

import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type intArrayType = new ArrayType(int[].class);
    TypeInfoArray intArrayTypeInfo = TypeInfoFactory.getTypeInfoForArray(intArrayType);
    // Perform assertions on intArrayTypeInfo
  }

  @Test
  public void testTypeInfoForField() throws NoSuchFieldException, IllegalAccessException {
    Class<Integer> intClass = Integer.class;
    Field field = intClass.getField("value");
    Type intFieldType = field.getGenericType();
    Type typeDefiningF = intClass; // or the enclosing class of 'field'
    TypeInfo intFieldTypeInfo = TypeInfoFactory.getTypeInfoForField(field, intFieldType);
    // Perform assertions on intFieldTypeInfo
  }

  @Test
  public void testGetActualType() {
    Type parameterizedType = new ParameterizedTypeImpl(List.class, new Type[]{Integer.class, Integer.class}, null);
    Type rawParentClass = List.class;
    Type actualType = TypeInfoFactory.getActualType(parameterizedType, null, rawParentClass);
    // Perform assertions on actualType
  }

  @Test
  public void testExtractRealTypes() {
    Type parameterizedType = new ParameterizedTypeImpl(List.class, new Type[]{Integer.class, Integer.class}, null);
    Type rawParentClass = List.class;
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(parameterizedType.getActualTypeArguments(), null, rawParentClass);
    // Perform assertions on actualTypes
  }

  @Test
  public void testGetIndex() {
    TypeVariable<Integer> intTypeVariable = new TypeVariable<Integer>("I", new Type[] {TypeVariable.class, Class.class}) {
      @Override
      public Type getType() {
        return Integer.TYPE;
      }
    };
    TypeVariable<?>[] typeVariables = new TypeVariable<?>[]{intTypeVariable};
    int expectedIndex = 0;
    TypeVariable<?> fieldTypeVariable = intTypeVariable;
    int actualIndex = TypeInfoFactory.getIndex(typeVariables, fieldTypeVariable);
    assertEquals(expectedIndex, actualIndex);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForArrayWithNullType() {
    // Test for IllegalArgumentException
    TypeInfoFactory.getTypeInfoForArray(null);
  }

  @Test(expected = NullPointerException.class)
  public void testTypeInfoForFieldWithNullField() {
    // Test for NullPointerException
    Field nullField = null;
    Type intType = Integer.TYPE;
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(nullField, intType);
    assertNull(info);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetActualTypeWithNullTypeToEvaluate() {
    // Test for IllegalArgumentException
    TypeInfoFactory.getActualType(null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExtractRealTypesWithNullActualTypeArguments() {
    // Test for IllegalArgumentException
    TypeInfoFactory.extractRealTypes(null, null, null);
  }

  @Test(expected = NullPointerException.class)
  public void testGetIndexWithNullTypes() {
    // Test for NullPointerException
    TypeVariable<?>[] nullTypes = null;
    TypeVariable<?> fieldTypeVariable = new TypeVariable<Integer>("I", new Type[] {TypeVariable.class, Class.class}) {
      @Override
      public Type getType() {
        return Integer.TYPE;
      }
    };
    int actualIndex = TypeInfoFactory.getIndex(nullTypes, fieldTypeVariable);
    assertNull(actualIndex);
  }
}