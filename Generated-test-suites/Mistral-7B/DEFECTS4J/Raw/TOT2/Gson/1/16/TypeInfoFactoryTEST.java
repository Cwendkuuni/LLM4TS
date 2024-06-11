import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.*;
import com.google.gson.*;
import com.google.gson.Field;
import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import com.google.gson.TypeInfoFactory;
import com.google.gson.TypeVariable;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[5].getClass(); // an integer array
    TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
    assertEquals(TypeInfoArray.class, typeInfoArray.getClass());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForNonArrayType() {
    Type type = String.class;
    TypeInfoFactory.getTypeInfoForArray(type);
  }

  @Test
  public void testTypeInfoForNullType() {
    Type type = null;
    assertNull(TypeInfoFactory.getTypeInfoForArray(type));
  }

  @Test
  public void testTypeInfoForField() {
    Field testField = mock(Field.class);
    when(testField.getType()).thenReturn(new Integer[5].getClass());

    Type typeDefiningF = mock(Type.class);
    when(typeDefiningF.getGenericSuperclass()).thenReturn(new ParameterizedTypeImpl(
        Field.class, new Type[] {testField.getType()}, null));

    Type actualType = TypeInfoFactory.getActualType(testField.getGenericType(),
        typeDefiningF, TypeUtils.class);
    assertEquals(TypeInfo.class, actualType.getClass());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForNullField() {
    Field testField = null;
    Type typeDefiningF = new Object() {}.getClass().getGenericSuperclass();
    Type rawParentClass = Object.class;
    TypeInfoFactory.getActualType(testField.getGenericType(), typeDefiningF, rawParentClass);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForNonFieldType() {
    Object testObject = new Object();
    Type typeDefiningF = new Object() {}.getClass().getGenericSuperclass();
    Type rawParentClass = Object.class;
    TypeInfoFactory.getActualType(testObject.getClass().getField("testField").getGenericType(),
        typeDefiningF, rawParentClass);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForNullTypeDefiningF() {
    Field testField = mock(Field.class);
    Type rawParentClass = Object.class;
    TypeInfoFactory.getActualType(testField.getGenericType(), null, rawParentClass);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForNonParameterizedType() {
    Field testField = mock(Field.class);
    Type rawParentClass = Object.class;
    Type typeDefiningF = testField.getType();
    TypeInfoFactory.getActualType(testField.getGenericType(), typeDefiningF, rawParentClass);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForNullRawParentClass() {
    Field testField = mock(Field.class);
    Type typeDefiningF = new Object() {}.getClass().getGenericSuperclass();
    TypeInfoFactory.getActualType(testField.getGenericType(), typeDefiningF, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForGenericArrayTypeParentType() {
    Field testField = mock(Field.class);
    Type rawParentClass = new Integer[5].getClass();
    Type typeDefiningF = new Object() {}.getClass().getGenericSuperclass();
    TypeInfoFactory.getActualType(testField.getGenericType(), typeDefiningF, rawParentClass);
  }

  @Test
  public void testTypeInfoForTypeVariableInParameterizedType() {
    Field testField = mock(Field.class);
    TypeVariable<Integer> typeVariable = new TypeVariable<>("T") {};
    Type typeDefiningF = new Object<>() {}.getTypeParameters()[0];
    Type rawParentClass = Object.class;

    // Test with exact match
    Type actualType = TypeInfoFactory.getActualType(testField.getGenericType(), typeDefiningF, rawParentClass);
    assertEquals(Integer.class, actualType);

    // Test with inexact match
    Type mismatchingTypeDefiningF = new ParameterizedTypeImpl(
        Object.class, new Type[] {typeVariable}, null);

    assertThrows(IllegalArgumentException.class, () -> {
      actualType = TypeInfoFactory.getActualType(testField.getGenericType(),
          mismatchingTypeDefiningF, rawParentClass);
    });
  }

  @Test
  public void testTypeInfoForWildcardTypeParentType() {
    Field testField = mock(Field.class);
    Type rawParentClass = Object.class;
    Type typeDefiningF = new WildcardTypeImpl(null, null);

    assertThrows(IllegalArgumentException.class, () -> {
      TypeInfoFactory.getActualType(testField.getGenericType(), typeDefiningF, rawParentClass);
    });

    typeDefiningF = new WildcardTypeImpl(
        TypeVariable.class, Arrays.asList(new TypeVariable[] { new TypeVariable<>("T") {} }));

    Type actualType = TypeInfoFactory.getActualType(testField.getGenericType(),
        typeDefiningF, rawParentClass);

    // WildcardType with upper bound is Object, which is the raw parent class, so the test should pass
    assertEquals(TypeInfo.class, actualType.getClass());
  }
}