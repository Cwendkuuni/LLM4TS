import com.google.gson.*; // for Preconditions and TypeUtils
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TypeInfoFactory.class})
public class TypeInfoFactoryTest {

  @Test
  public void testGetTypeInfoForArray() {
    Type arrayType = new GenericArrayTypeImpl(Integer.TYPE);
    TypeInfoArray infoArray = TypeInfoFactory.getTypeInfoForArray(arrayType);
    assertEquals(Integer.TYPE, infoArray.getComponentType());
  }

  @Test
  public void testGetTypeInfoForField_TypeVariable() throws NoSuchFieldException {
    Class<TestClass> testClass = mock(TestClass.class);
    Field field = PowerMockito.spy(TestClass.class.getDeclaredField("field1"));
    field.setAccessible(true);

    // Given
    TypeVariable<Integer> typeVariable = new TypeVariable<Integer>("T") {};
    Type typeDefiningF = TestClass.class;
    Type parentType = new ParameterizedTypeImpl(TestClass.class, typeVariable);
    Type actualType = Integer.TYPE;

    // When
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, parentType);

    // Then
    assertEquals(actualType, info.getType());
  }

  @Test
  public void testGetTypeInfoForField_TypeVariable_MissingInTypeDeclaration() {
    Class<TestClass> testClass = mock(TestClass.class);
    Field field = PowerMockito.spy(TestClass.class.getDeclaredField("field1"));
    field.setAccessible(true);

    // Given
    TypeVariable<Integer> typeVariable = new TypeVariable<Integer>("T") {};
    Type typeDefiningF = TestClass.class;
    Type parentType = mock(ParameterizedType.class);
    when(parentType.getOwnerType()).thenReturn(testClass);
    when(parentType.getActualTypeArguments()).thenReturn(new Type[0]);

    // When
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, parentType);

    // Then
    assertNull(info);
  }

  @Test
  public void testGetTypeInfoForField_ParameterizedType() throws NoSuchFieldException {
    // Given
    Class<TestClass> testClass = mock(TestClass.class);
    Field field = PowerMockito.spy(TestClass.class.getDeclaredField("field2"));
    field.setAccessible(true);
    Type parentType = new ParameterizedTypeImpl(TestClass.class, List.class, String.class);
    Type actualType = List.class;

    // When
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, parentType);

    // Then
    assertEquals(actualType, info.getType());
  }

  @Test
  public void testGetTypeInfoForField_GenericArrayType() throws NoSuchFieldException {
    // Given
    Class<TestClass> testClass = mock(TestClass.class);
    Field field = PowerMockito.spy(TestClass.class.getDeclaredField("field3"));
    field.setAccessible(true);
    Type parentType = new ParameterizedTypeImpl(TestClass.class, List.class, String.class);
    Type actualType = String[].class;

    // When
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, parentType);

    // Then
    assertEquals(actualType, info.getType());
  }

  @Test
  public void testGetTypeInfoForField_WildcardType() throws NoSuchFieldException {
    // Given
    Class<TestClass> testClass = mock(TestClass.class);
    Field field = PowerMockito.spy(TestClass.class.getDeclaredField("field4"));
    field.setAccessible(true);
    Type parentType = new WildcardTypeImpl(null, List.class);
    Type actualType = List.class;

    // When
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, parentType);

    // Then
    assertEquals(actualType, info.getType());
  }

  @Test
  public void testGetTypeInfoForField_UnknownType() throws NoSuchFieldException {
    // Given
    Class<TestClass> testClass = mock(TestClass.class);
    Field field = PowerMockito.spy(TestClass.class.getDeclaredField("field5"));
    field.setAccessible(true);
    Type parentType = new Object(){}.getClass().getGenericSuperclass();
    Type actualType = null;

    // When
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, parentType);

    // Then
    assertNull(info);
  }

  private static class TestClass {
    private int field1;

    @SuppressWarnings("unchecked")
    private List<Integer> field2;

    @SuppressWarnings("unchecked")
    private Integer[] field3;

    private List<? extends Number> field4;

    @SuppressWarnings("unchecked")
    private Object field5;
  }
}