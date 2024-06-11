import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.Preconditions;
import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeUtils;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.junit.Before;
import org.junit.Test;

public class TypeInfoFactoryTest {

  private TypeInfoFactory typeInfoFactory;
  private Field field;
  private Type typeDefiningF;
  private Class<?> classDefiningF;

  @Before
  public void setUp() {
    typeInfoFactory = new TypeInfoFactory();
    field = mock(Field.class);
    typeDefiningF = mock(Type.class);
    classDefiningF = mock(Class.class);
    when(typeDefiningF.getGenericType()).thenReturn(new ParameterizedTypeImpl(classDefiningF, new Type[] { TypeToken.of(int.class).getType() }, null));
    when(classDefiningF.getTypeParameters()).thenReturn(new TypeVariable[0]);
  }

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[10].getClass();
    TypeInfoArray typeInfoArray = typeInfoFactory.TypeInfoForArray(type);
    assertEquals(TypeInfoType.ARRAY, typeInfoArray.getType());
    assertSame(int[].class, typeInfoArray.getRawType());
  }

  @Test
  public void testTypeInfoForField_ClassType() {
    TypeInfo typeInfo = typeInfoFactory.TypeInfoForField(field, typeDefiningF);
    assertEquals(TypeInfoType.CLASS, typeInfo.getType());
    assertNull(typeInfo.getActualType());
  }

  @Test
  public void testTypeInfoForField_ParameterizedType() {
    Type type = new TypeToken<List<Integer>>() {}.getType();
    TypeInfo typeInfo = typeInfoFactory.TypeInfoForField(field, typeDefiningF);
    assertEquals(TypeInfoType.PARAMETERIZED, typeInfo.getType());
    assertEquals(Integer.class, typeInfo.getActualType());
  }

  @Test
  public void testTypeInfoForField_GenericArrayType() {
    Type type = new int[10].getClass();
    TypeInfo typeInfo = typeInfoFactory.TypeInfoForField(field, typeDefiningF);
    assertEquals(TypeInfoType.GENERIC_ARRAY, typeInfo.getType());
    assertSame(int[].class, typeInfo.getActualType());
  }

  @Test
  public void testTypeInfoForField_TypeVariable() {
    Type typeVariable = new TypeToken<List<A>>() {}.getType().getTypeComponents()[0];
    TypeInfo typeInfo = typeInfoFactory.TypeInfoForField(field, typeDefiningF);
    assertEquals(TypeInfoType.TYPEVARIABLE, typeInfo.getType());
    assertEquals(typeVariable, typeInfo.getActualType());
  }

  @Test
  public void testTypeInfoForField_WildcardType() {
    Type wildcardType = new TypeToken<List<?>>() {}.getType();
    TypeInfo typeInfo = typeInfoFactory.TypeInfoForField(field, typeDefiningF);
    assertEquals(TypeInfoType.WILDCARD, typeInfo.getType());
    assertNull(typeInfo.getActualType());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForField_InvalidType() {
    Type type = new Object() {};
    typeInfoFactory.TypeInfoForField(field, typeDefiningF);
  }
}

enum InfoType {
  CLASS, PARAMETERIZED, GENERIC_ARRAY, TYPEVARIABLE, WILDCARD, ARRAY
}

class TypeInfo {
  private InfoType type;
  private Type actualType;

  public TypeInfo(Type actualType) {
    this.type = InfoType.CLASS;
    this.actualType = actualType;
  }

  public InfoType getType() {
    return type;
  }

  public Type getActualType() {
    return actualType;
  }
}

class TypeInfoArray extends TypeInfo {
  public static final InfoType TYPE = InfoType.ARRAY;

  public TypeInfoArray(Type type) {
    super(type);
  }
}

class ParameterizedTypeImpl extends ParameterizedType {
  public ParameterizedTypeImpl(Type rawType, Type[] actualTypeArguments, Type ownerType) {
    super(rawType, actualTypeArguments, ownerType);
  }
}

class GenericArrayTypeImpl extends GenericArrayType {
  public GenericArrayTypeImpl(Type componentType) {
    super(componentType);
  }
}