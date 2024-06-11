import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import com.google.gson.TypeInfoImpl;
import com.google.gson.ParameterizedTypeImpl;
import com.google.gson.GenericArrayTypeImpl;
import org.junit.Test;
import org.junit.Assert;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type intArrayType = new int[0].getClass().getComponentType();
    TypeInfoArray intArrayTypeInfo = TypeInfoFactory.getTypeInfoForArray(intArrayType);
    Assert.assertEquals(TypeInfoArray.class, intArrayTypeInfo.getClass());
  }

  @Test
  public void testTypeInfoForField_ClassType() {
    try {
      Class<Integer> integerClass = Integer.class;
      Field field = integerClass.getDeclaredField("value");
      Type fieldType = field.getGenericType();
      TypeInfo intTypeInfo = TypeInfoFactory.getTypeInfoForField(field, field.getType());
      Assert.assertEquals(TypeInfo.class, intTypeInfo.getClass());
      Assert.assertEquals(Integer.TYPE, intTypeInfo.getType());
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testTypeInfoForField_ParameterizedType() {
    try {
      Class<Foo<Integer>> fooClass = Foo.class;
      Field field = fooClass.getDeclaredField("integerField");
      Type fieldType = field.getGenericType();
      TypeInfo<Integer> intTypeInfo = (TypeInfo<Integer>) TypeInfoFactory.getTypeInfoForField(field, field.getType());
      Assert.assertEquals(TypeInfo.class, intTypeInfo.getClass());
      Assert.assertEquals(Integer.TYPE, intTypeInfo.getType());
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForField_IllegalType() {
    try {
      Class<Foo<List<Integer>>> fooListClass = Foo.class.getGenericSuperclass();
      Field field = fooListClass.getDeclaredField("integerField"); // this field does not exist
      Type fieldType = field.getGenericType();
      TypeInfo<String> stringTypeInfo = TypeInfoFactory.getTypeInfoForField(field, field.getType());
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testGetActualType_ClassType() {
    Type intType = Integer.TYPE;
    Type actualType = TypeInfoFactory.getActualType(intType, null, null);
    Assert.assertEquals(intType, actualType);
  }

  @Test
  public void testGetActualType_ParameterizedType() {
    Type listIntegerType = new TypeToken<List<Integer>>() {}.getType();
    Type actualType = TypeInfoFactory.getActualType(listIntegerType, null, null);
    Assert.assertEquals(new TypeToken<List<Integer>>() {}.getType(), actualType);
  }

  @Test
  public void testGetActualType_GenericArrayType() {
    Type integerArrayType = new int[0].getClass().getComponentType();
    Type actualType = TypeInfoFactory.getActualType(new GenericArrayTypeImpl(Integer.TYPE), null, null);
    Assert.assertEquals(integerArrayType, actualType);
  }

  @Test
  public void testGetActualType_TypeVariable() {
    try {
      Class<Foo<Integer>> fooClass = Foo.class;
      TypeVariable<A> intTypeVariable = fooClass.getTypeParameters()[0];
      Type actualType = TypeInfoFactory.getActualType(intTypeVariable, null, null);
      Assert.assertEquals(Integer.TYPE, actualType);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualType_WildcardType() {
    Type wildcardType = new WildcardTypeImpl(null, null);
    Type actualType = TypeInfoFactory.getActualType(wildcardType, null, null);
  }

  @Test
  public void testExtractRealTypes() {
    Type listIntType = new TypeToken<List<Integer>>() {}.getType();
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(listIntType.getTypeArguments(), null, null);
    Assert.assertArrayEquals(new Type[] {Integer.TYPE, Integer.TYPE}, actualTypes);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testExtractRealTypes_ParentTypeIsNotParameterized() {
    Type fooListType = Foo.class.getGenericSuperclass();
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(fooListType.getTypeArguments(), null, null);
  }

  private static class Foo<A> {
    private A integerField;
  }
}