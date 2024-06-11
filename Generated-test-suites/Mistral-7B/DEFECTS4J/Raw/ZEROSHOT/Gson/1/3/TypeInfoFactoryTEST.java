import static com.google.common.base.Preconditions.checkArgument;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeUtils;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type intArrayType = new int[0].getClass();
    TypeInfo expectedTypeInfo = new TypeInfoArray(intArrayType);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(intArrayType);
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test
  public void testTypeInfoForField() throws NoSuchFieldException, IllegalAccessException {
    Class<DummyClass> rawParentClass = DummyClass.class;
    Class<DummySubClass> childClass = DummySubClass.class;
    DummySubClass instance = new DummySubClass();

    Field field = childClass.getField("intField");
    Type type = field.getGenericType();
    TypeInfo expectedTypeInfo = new TypeInfo(Integer.TYPE);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, type, rawParentClass);
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForFieldWithMissingTypeParameter() throws NoSuchFieldException, IllegalAccessException {
    Class<DummyClass> rawParentClass = DummyClass.class;
    Class<DummySubClass> childClass = DummySubClass.class;
    DummySubClass instance = new DummySubClass();

    Field field = childClass.getField("intArrayField");
    Type type = field.getGenericType();

    TypeInfoFactory.getTypeInfoForField(field, type, rawParentClass);
  }

  @Test
  public void testGetActualTypeForRawClass() {
    Class<String> rawParentClass = String.class;
    Class<DummyClass> actualClass = DummyClass.class;
    Type expectedType = actualClass;
    Type actualType = TypeInfoFactory.getActualType(expectedType, null, rawParentClass);
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetActualTypeForParameterizedType() {
    Class<DummyParentClass> rawParentClass = DummyParentClass.class;
    Class<DummyChildClass> expectedClass = DummyChildClass.class;
    Type type = new TypeToken<List<DummyChildClass>>() {}.getType();
    Type actualType = TypeInfoFactory.getActualType(type, null, rawParentClass);
    assertEquals(expectedClass, actualType);
  }

  @Test
  public void testGetActualTypeForGenericArrayType() {
    Class<String> rawParentClass = String.class;
    Class<String[]> expectedType = String[].class;
    Type type = new GenericArrayTypeImpl(String.class);
    Type actualType = TypeInfoFactory.getActualType(type, null, rawParentClass);
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetActualTypeForTypeVariable() {
    Class<DummyParentClass> rawParentClass = DummyParentClass.class;
    TypeVariable<String> typeVariable = rawParentClass.getTypeParameters()[0];
    Type expectedType = String.class;
    Type actualType = TypeInfoFactory.getActualType(typeVariable, null, rawParentClass);
    assertEquals(expectedType, actualType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualTypeForWildcardType() {
    Class<DummyParentClass> rawParentClass = DummyParentClass.class;
    Type wildcardType = new WildcardTypeImpl(null, null);
    Type actualType = TypeInfoFactory.getActualType(wildcardType, null, rawParentClass);
  }

  @Test
  public void testExtractRealTypes() {
    Class<DummyParentClass> rawParentClass = DummyParentClass.class;
    Type[] expectedTypes = new Type[] { Byte.TYPE, String.class, List.class };
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(new ParameterizedTypeImpl(rawParentClass, new Type[] { Byte.TYPE, new TypeToken<List<String>>() {}.getType() }, null).getActualTypeArguments(), null, rawParentClass);
    assertNotNull(actualTypes);
    assertEquals(expectedTypes.length, actualTypes.length);
    for (int i = 0; i < expectedTypes.length; ++i) {
      assertEquals(expectedTypes[i], actualTypes[i]);
    }
  }

  private static class DummyClass {}

  private static class DummySubClass extends DummyClass {
    int intField;
    int intArrayField[];
  }

  private static class DummyParentClass<A, B, C> {
    A a;
    List<B> list;
  }

  private static class TypeInfoArray implements TypeInfo {
    Type type;

    public TypeInfoArray(Type type) {
      this.type = type;
    }

    @Override
    public Type getType() {
      return type;
    }
  }

  private static class TypeVariableImpl implements TypeVariable<Object> {
    String name;

    public TypeVariableImpl(String name) {
      this.name = name;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public Type getGenericDeclaration() {
      return null;
    }

    @Override
    public Type[] getBounds() {
      return new Type[0];
    }
  }

  private static class ParameterizedTypeImpl implements ParameterizedType {
    Type rawType;
    Type[] actualTypeArguments;
    Type ownerType;

    public ParameterizedTypeImpl(Type rawType, Type[] actualTypeArguments, Type ownerType) {
      this.rawType = rawType;
      this.actualTypeArguments = actualTypeArguments;
      this.ownerType = ownerType;
    }

    @Override
    public Type getRawType() {
      return rawType;
    }

    @Override
    public Type[] getActualTypeArguments() {
      return actualTypeArguments;
    }

    @Override
    public Type getOwnerType() {
      return ownerType;
    }
  }

  private static class GenericArrayTypeImpl implements GenericArrayType {
    Type componentType;

    public GenericArrayTypeImpl(Type componentType) {
      this.componentType = componentType;
    }

    @Override
    public Type getComponentType() {
      return componentType;
    }

    @Override
    public Type getGenericComponentType() {
      return componentType;
    }
  }

  private static class WildcardTypeImpl implements WildcardType {

  }
}