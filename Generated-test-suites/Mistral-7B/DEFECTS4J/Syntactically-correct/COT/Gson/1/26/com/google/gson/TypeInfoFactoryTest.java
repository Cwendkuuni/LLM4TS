import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import com.google.gson.TypeInfoImpl;
import com.google.gson.ParameterizedTypeImpl;
import com.google.gson.GenericArrayTypeImpl;
import com.google.common.base.Preconditions;
import org.junit.Test;
import org.junit.Assert;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.WildcardType;
import java.lang.reflect.TypeVariable;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[3].getClass().getComponentType();
    TypeInfo expectedTypeInfo = new TypeInfoArray(type);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(type);
    Assert.assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test
  public void testTypeInfoForField_ClassType() {
    try {
      Class<Integer> clazz = Integer.class;
      Field field = clazz.getField("value");
      Type type = field.getType();

      TypeInfo expectedTypeInfo = new TypeInfo(Integer.TYPE);
      TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, null);
      Assert.assertEquals(expectedTypeInfo, actualTypeInfo);

      clazz = String.class;
      field = clazz.getField("value");
      type = field.getGenericType();

      expectedTypeInfo = new TypeInfo(String.class);
      actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, null);
      Assert.assertEquals(expectedTypeInfo, actualTypeInfo);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testTypeInfoForField_ParameterizedType() {
    try {
      Class<List<Integer>> clazz = ArrayList.class.getTypeParameters()[0].getType();
      Field field = clazz.getField("elementType");
      Type type = field.getGenericType();

      Type expectedType = new ParameterizedTypeImpl(Integer.TYPE, new Type[]{}, null);
      Type actualType = TypeInfoFactory.getActualType(type, null, null);
      Assert.assertSame(expectedType, actualType);

      clazz = new ArrayList<List<String>>().getClass();
      field = clazz.getField("element");
      type = field.getGenericType();

      expectedType = new ParameterizedTypeImpl(ArrayList.class, new Type[] { new TypeVariable<>("T") {}.getGenericDeclaration() }, null);
      Type elementType = ((ParameterizedType) expectedType.getActualTypeArguments()[0]).getActualTypeArguments()[0];
      expectedType = new ParameterizedTypeImpl(String.class, new Type[]{}, elementType);
      actualType = TypeInfoFactory.getActualType(type, null, null);
      Assert.assertSame(expectedType, actualType);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testTypeInfoForField_GenericArrayType() {
    Type type = new int[3].getClass().getComponentType();
    TypeInfo expectedTypeInfo = new TypeInfoArray(type);

    try {
      Class<int[]> clazz = int[].class;
      Field field = clazz.getField("CLASS");
      Type actualType = TypeInfoFactory.getActualType(field.getGenericType(), null, null);
      TypeInfo actualTypeInfo = new TypeInfoArray((GenericArrayType) actualType);
      Assert.assertEquals(expectedTypeInfo, actualTypeInfo);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForField_TypeVariable_NoParentType() {
    try {
      Class<List<Integer>> clazz = ArrayList.class.getTypeParameters()[0].getType();
      Field field = clazz.getField("elementType");
      Type type = field.getGenericType();

      TypeVariable<Integer> typeVariable = (TypeVariable<Integer>) type;
      TypeInfo expectedTypeInfo = new TypeInfo(Integer.class);
      TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, null);
      Assert.assertEquals(expectedTypeInfo, actualTypeInfo);

      // This should throw an UnsupportedOperationException since there's no parent type
      TypeInfo actualTypeInfo2 = TypeInfoFactory.getTypeInfoForField(field, typeVariable);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForField_TypeVariable_NoParameterizedType() {
    try {
      Class<List<Integer>> clazz = ArrayList.class;
      Field field = clazz.getField("elementType");
      Type type = field.getGenericType();

      TypeVariable<Integer> typeVariable = (TypeVariable<Integer>) type;
      TypeInfo expectedTypeInfo = new TypeInfo(Integer.class);
      TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, null);
      Assert.assertEquals(expectedTypeInfo, actualTypeInfo);

      // This should throw an UnsupportedOperationException since there's no parent type or parameterized type
      TypeInfo actualTypeInfo2 = TypeInfoFactory.getTypeInfoForField(field, typeVariable);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForField_WildcardType() {
    try {
      Class<List<?>> clazz = ArrayList.class;
      Field field = clazz.getField("elementType");
      Type type = field.getGenericType();

      WildcardType wildcardType = (WildcardType) type;
      Type upperBound = wildcardType.getUpperBounds()[0];
      TypeInfo expectedTypeInfo = new TypeInfo(Object.class);
      TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, null);
      Assert.assertEquals(expectedTypeInfo, actualTypeInfo);

      // This should throw an UnsupportedOperationException
      TypeInfo actualTypeInfo2 = TypeInfoFactory.getTypeInfoForField(field, wildcardType);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testExtractRealTypes() {
    Type[] types = new Type[] {
      new TypeVariable<>("T") {}.getGenericDeclaration(),
      new ParameterizedTypeImpl(Integer.TYPE, new Type[] {}, null),
      new WildcardType(null, new Type[] { new Class<?>[] { String.class } })
    };

    Type[] expectedTypes = new Type[] {
      String.class,
      Integer.TYPE,
      String.class
    };
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(types, null, null);
    Assert.assertArrayEquals(expectedTypes, actualTypes);
  }

  @Test
  public void testGetIndex() {
    TypeVariable<Integer>[] types = {
      new TypeVariable<>("A") {},
      new TypeVariable<>("B") {},
      new TypeVariable<>("C") {}
    };
    TypeVariable<Integer> typeVariable = new TypeVariable<>("A") {};
    int expectedIndex = 0;
    int actualIndex = TypeInfoFactory.getIndex(types, typeVariable);
    Assert.assertEquals(expectedIndex, actualIndex);
  }
}