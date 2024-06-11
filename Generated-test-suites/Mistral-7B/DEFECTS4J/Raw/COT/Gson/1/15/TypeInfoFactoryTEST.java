import static org.junit.Assert.*;
import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import com.google.gson.TypeInfoImpl;
import com.google.gson.ParameterizedTypeImpl;
import com.google.gson.GenericArrayTypeImpl;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.WildcardType;
import java.lang.reflect.TypeVariable;

public class TypeInfoFactoryTest {

  @org.junit.Test
  public void testTypeInfoForArray() {
    Type type = new int[3].getClass().getComponentType();
    TypeInfoArray expectedTypeInfo = new TypeInfoArray(type);
    TypeInfo actualTypeInfo = TypeInfoFactory.TypeInfoForArray(type);
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @org.junit.Test
  public void testTypeInfoForField_ClassType() {
    try {
      Class<?> clazz = new Class<Foo>() {
        private static class Inner {
          int a;
        }

        private static class Bar {}

        private static Inner foo = new Inner();
      }.newInstance();

      Field field = clazz.getDeclaredField("foo");
      field.setAccessible(true);

      Type expectedType = Foo.class;
      TypeInfo expectedTypeInfo = new TypeInfo(expectedType);
      TypeType definingClass = TypeUtils.toRawClass(clazz);
      Type actualType = TypeInfoFactory.getTypeInfoForField(field, definingClass.getTypeName()).getType();
      TypeInfo actualTypeInfo = new TypeInfo(actualType);

      assertEquals(expectedTypeInfo, actualTypeInfo);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @org.junit.Test
  public void testTypeInfoForField_ParameterizedType() {
    try {
      Class<List<String>> listClass = ArrayList.class.getTypeParameters()[0].getRawType();
      Field field = listClass.getDeclaredField("size");
      field.setAccessible(true);

      Type expectedType = Integer.TYPE;
      TypeInfo expectedTypeInfo = new TypeInfo(expectedType);
      TypeType definingClass = TypeUtils.toRawClass(listClass);
      Type actualType = TypeInfoFactory.getTypeInfoForField(field, definingClass.getTypeName()).getType();
      TypeInfo actualTypeInfo = new TypeInfo(actualType);

      assertEquals(expectedTypeInfo, actualTypeInfo);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @org.junit.Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForField_UnknownType() {
    try {
      Class<?> clazz = new Class<Foo>() {
        private static class Inner {
          int a;
          String b;
        }

        private static class Bar {}

        private static Inner foo = new Inner();
      }.newInstance();

      Field field = clazz.getDeclaredField("b");
      field.setAccessible(true);

      Type definingClass = TypeUtils.toRawClass(clazz);
      Type actualType = TypeInfoFactory.getTypeInfoForField(field, definingClass.getTypeName()).getType();

      fail("Should have thrown UnsupportedOperationException");
    } catch (Exception e) {
      // expected
    }
  }

  @org.junit.Test
  public void testGetActualType_ClassType() {
    Class<Foo> clazz = Foo.class;
    Type expectedType = clazz;
    Type actualType = TypeInfoFactory.getActualType(clazz, null, null);
    assertEquals(expectedType, actualType);
  }

  @org.junit.Test
  public void testGetActualType_ParameterizedType() {
    Class<List<String>> listClass = ArrayList.class;
    Type expectedType = listClass.getTypeParameters()[0].getRawType();
    Type actualType = TypeInfoFactory.getActualType(listClass.getGenericSuperclass(), null, null);
    assertEquals(expectedType, actualType);
  }

  @org.junit.Test
  public void testGetActualType_GenericArrayType() {
    Type type = new int[3].getClass().getComponentType();
    Type expectedType = int[].class;
    Type actualType = TypeInfoFactory.getActualType(new GenericArrayTypeImpl(type), null, null);
    assertEquals(expectedType, actualType);
  }

  @org.junit.Test
  public void testGetActualType_TypeVariable() {
    Class<Foo<Integer>> clazz = new Foo<Integer>() {}.getClass();
    TypeVariable<Integer> integerTypeVariable = clazz.getTypeParameters()[0];
    Type expectedType = Integer.TYPE;
    Type actualType = TypeInfoFactory.getActualType(integerTypeVariable, null, null);
    assertEquals(expectedType, actualType);
  }

  @org.junit.Test
  public void testGetActualType_WildcardType() {
    Class<Foo<?>> clazz = Foo.class;
    WildcardType wildcardType = (WildcardType) clazz.getGenericSuperclass();
    Type expectedType = Object.class;
    Type actualType = TypeInfoFactory.getActualType(wildcardType, null, null);
    assertEquals(expectedType, actualType);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testGetActualType_InvalidType() {
    Class<Integer> clazz = Integer.class;
    Type expectedType = null;
    Type actualType = TypeInfoFactory.getActualType(clazz, null, null);
    fail("Should have thrown IllegalArgumentException");
  }

  @org.junit.Test
  public void testExtractRealTypes_ClassTypes() {
    Type[] inputTypes = new Type[] { String.class, Integer.TYPE };
    Type[] expectedTypes = new Type[] { String.class, Integer.TYPE };
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(inputTypes, null, null);
    assertArrayEquals(expectedTypes, actualTypes);
  }

  @org.junit.Test
  public void testExtractRealTypes_ParameterizedTypes() {
    Class<Map<String, List<Integer>>> mapClass = Map.class.getTypeParameters()[0].getType();
    Type[] inputTypes = new Type[] { mapClass, List.class };
    Type[] expectedTypes = new Type[] { Map.class, List.class };
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(inputTypes, null, null);
    assertArrayEquals(expectedTypes, actualTypes);
  }
}