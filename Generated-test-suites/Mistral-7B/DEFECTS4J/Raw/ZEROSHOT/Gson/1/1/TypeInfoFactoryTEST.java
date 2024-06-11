import static com.google.common.base.Preconditions.checkArgument;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class TypeInfoFactoryTest {

  private TypeInfoFactory factory;
  private Class<?> rawParentClass;
  private Field field;

  @Before
  public void setup() {
    factory = new TypeInfoFactory();
    rawParentClass = mock(Class.class);
    field = mock(Field.class);
  }

  @Test
  public void testTypeInfoForArray() {
    Type arrayType = new int[0].getClass().getComponentType();
    TypeInfoArray expectedTypeInfo = new TypeInfoArray(arrayType);
    TypeInfo actualTypeInfo = factory.TypeInfoFactory.getTypeInfoForArray(arrayType);
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForInvalidType() {
    Type invalidType = new Object();
    factory.TypeInfoFactory.getTypeInfoForArray(invalidType);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testTypeInfoForField() {
    Type fieldTypeVariable = new TypeVariable<Integer>() {}.getType();
    when(field.getGenericType()).thenReturn(new ParameterizedTypeImpl(fieldTypeVariable, new Type[]{}));
    Type actualType = new Integer[0].getClass();

    TypeInfo expectedTypeInfo = new TypeInfo(TypeUtils.toRawType(actualType));
    TypeInfo actualTypeInfo = factory.TypeInfoFactory.getTypeInfoForField(field, rawParentClass);
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test
  public void testGetActualTypeForClass() {
    when(rawParentClass.getTypeName()).thenReturn("ParentClass");
    Class<?> expectedType = String.class;
    Type actualType = factory.TypeInfoFactory.getActualType(expectedType, rawParentClass, rawParentClass);
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetActualTypeForParameterizedType() {
    when(rawParentClass.getTypeName()).thenReturn("ParentClass<A>");
    TypeVariable<Integer> integerTypeVariable = new TypeVariable<Integer>("A") {}.getType();
    Type[] actualTypeArguments = new Type[]{Integer.TYPE};
    when(rawParentClass.getGenericType()).thenReturn(new ParameterizedTypeImpl(rawParentClass, actualTypeArguments));

    Type expectedType = new TypeToken<ParentClass<Integer>>() {}.getType();
    Type actualType = factory.TypeInfoFactory.getActualType(expectedType, rawParentClass, rawParentClass);
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetActualTypeForGenericArrayType() {
    when(rawParentClass.getTypeName()).thenReturn("ParentClass<A>");
    TypeVariable<Integer> integerTypeVariable = new TypeVariable<Integer>("A") {}.getType();
    Type[] actualTypeArguments = new Type[]{new TypeToken<Integer[]>() {}.getType()};
    when(rawParentClass.getGenericType()).thenReturn(new ParameterizedTypeImpl(rawParentClass, actualTypeArguments));

    Type expectedType = new TypeToken<ParentClass<Integer[]>>() {}.getType();
    Type actualType = factory.TypeInfoFactory.getActualType(expectedType, rawParentClass, rawParentClass);
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetActualTypeForTypeVariable() {
    Type typeVariable = new TypeVariable<Integer>("A") {}.getType();
    when(rawParentClass.getTypeName()).thenReturn("ParentClass<A>");

    Type expectedType = Integer.TYPE;
    Type actualType = factory.TypeInfoFactory.getActualType(typeVariable, rawParentClass, rawParentClass);
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetActualTypeForWildcardType() {
    when(rawParentClass.getTypeName()).thenReturn("ParentClass<? extends Number>");

    Type expectedType = TypeToken.get(Number.class).getType();
    Type actualType = factory.TypeInfoFactory.getActualType(TypeToken.get(Number.class).getType(), rawParentClass, rawParentClass);
    assertEquals(expectedType, actualType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualTypeForUnsupportedType() {
    Type unsupportedType = new Object() {};
    factory.TypeInfoFactory.getActualType(unsupportedType, rawParentClass, rawParentClass);
  }
}

class ParentClass<T> {}