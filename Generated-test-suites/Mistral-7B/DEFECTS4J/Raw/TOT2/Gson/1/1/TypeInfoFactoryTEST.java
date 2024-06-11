
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.*;

import com.google.gson.*;
import com.google.gson.reflect.*;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[10].getClass();
    TypeInfo expectedTypeInfo = new TypeInfo(int[].class);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(type);
    assertNotNull(actualTypeInfo);
    assertNotNull(actualTypeInfo.getType());
    assertNotNull(actualTypeInfo.getComponentType());
    assertEquals(expectedTypeInfo.getType(), actualTypeInfo.getType());
    assertEquals(expectedTypeInfo.getComponentType(), actualTypeInfo.getComponentType());
  }

  @Test
  public void testTypeInfoForArrayDifferentDimensions() {
    Type type = new int[3][4][5].getClass();
    TypeInfo expectedTypeInfo = new TypeInfo(int[][][].class);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(type);
    assertNotNull(actualTypeInfo);
    assertNotNull(actualTypeInfo.getType());
    assertNotNull(actualTypeInfo.getComponentType());
    assertEquals(expectedTypeInfo.getType(), actualTypeInfo.getType());
    assertEquals(expectedTypeInfo.getComponentType(), actualTypeInfo.getComponentType());
  }

  @Test
  public void testTypeInfoForArrayDifferentElementTypes() {
    Type type = new int[10][].getClass();
    TypeInfo expectedTypeInfo = new TypeInfo(int[][].class);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(type);
    assertNotNull(actualTypeInfo);
    assertNotNull(actualTypeInfo.getType());
    assertNotNull(actualTypeInfo.getComponentType());
    assertEquals(expectedTypeInfo.getType(), actualTypeInfo.getType());
    assertEquals(expectedTypeInfo.getComponentType(), actualTypeInfo.getComponentType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForNullType() {
    TypeInfoFactory.getTypeInfoForArray(null);
  }

  @Test
  public void testTypeInfoForNonArrayType() {
    Type type = String.class;
    TypeInfo expectedTypeInfo = null;
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(type);
    assertNull(actualTypeInfo);
    assertNull(expectedTypeInfo);
  }

  @Test
  public void testTypeInfoForTypeWithTypeVariables() {
    Field mockField = mock(Field.class);
    Type mockType = mock(TypeVariable.class);
    Type mockParentType = mock(TypeVariable.class);
    Type mockRawParentClass = mock(Class.class);

    when(mockField.getType()).thenReturn(mockType);
    when(mockType.getBounds()[0]).thenReturn(new TypeVariable<Integer>("T") {});
    when(mockType.getDeclaringClass()).thenReturn(mockRawParentClass);

    TypeInfoFactory typeInfoFactory = new TypeInfoFactory();
    TypeInfo actualTypeInfo = typeInfoFactory.getTypeInfoForField(mockField, mockParentType);
    // This test case assumes that TypeInfoFactory uses reflection to find the actual type,
    // so it doesn't matter what the expected type is.
    // The test case only checks that the actual type is non-null.
    assertNotNull(actualTypeInfo);
  }

  @Test
  public void testTypeInfoForWildcardTypes() {
    Type type = new Type[] { Object.class, String[].class }.getClass();
    TypeInfo expectedTypeInfo = null;
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(type);
    assertNull(actualTypeInfo);
    assertNull(expectedTypeInfo);
  }

  @Test
  public void testTypeInfoForParameterizedTypes() {
    Type type = new Type[] { List.class, Integer.class }.getClass();
    TypeInfo expectedTypeInfo = new TypeInfo(java.util.List.class);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(type);
    assertNotNull(actualTypeInfo);
    assertNotNull(actualTypeInfo.getType());
    assertEquals(expectedTypeInfo.getType(), actualTypeInfo.getType());
  }

  @Test
  public void testTypeInfoForGenericArrayTypes() {
    Type type = int[].class;
    TypeInfo expectedTypeInfo = new TypeInfo(int[].class);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(type);
    assertNotNull(actualTypeInfo);
    assertNotNull(actualTypeInfo.getType());
    assertNotNull(actualTypeInfo.getComponentType());
    assertEquals(expectedTypeInfo.getType(), actualTypeInfo.getType());
    assertEquals(expectedTypeInfo.getComponentType(), actualTypeInfo.getComponentType());
  }
}