import static org.junit.Assert.*;
import com.google.gson.*;
import java.lang.reflect.*;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[5].getClass().getComponentType();
    TypeInfo expectedTypeInfo = new TypeInfoArray(type);
    TypeInfo actualTypeInfo = TypeInfoFactory.TypeInfoForArray(type);
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForArrayWithNonArrayType() {
    Type type = String.class;
    TypeInfo actualTypeInfo = TypeInfoFactory.TypeInfoForArray(type);
    fail("Expected IllegalArgumentException but got: " + actualTypeInfo);
  }

  @Test
  public void testTypeInfoForField() throws NoSuchFieldException, IllegalAccessException {
    Class<Foo> fooClass = Foo.class;
    Field field = fooClass.getField("field");
    Type fieldType = field.getGenericType();
    Type definingClassType = field.getDeclaringClass();
    TypeInfo expectedTypeInfo = new TypeInfo(Integer.TYPE);
    TypeInfo actualTypeInfo = TypeInfoFactory.TypeInfoForField(field, definedClassType.getGenericType());
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForFieldWithNonGenericField() throws NoSuchFieldException, IllegalAccessException {
    Class<Foo> fooClass = Foo.class;
    Field field = fooClass.getField("field");
    Type fieldType = field.getType();
    Type definingClassType = field.getDeclaringClass().getGenericType();
    TypeInfo actualTypeInfo = TypeInfoFactory.TypeInfoForField(field, definingClassType);
    fail("Expected IllegalArgumentException but got: " + actualTypeInfo);
  }

  @Test
  public void testGetActualType() throws NoSuchFieldException, IllegalAccessException {
    Class<Foo> fooClass = Foo.class;
    Field field = fooClass.getField("field");
    Type fieldType = field.getGenericType();
    Type definingClassType = fooClass.getGenericType();
    Class<?> rawDefiningClass = TypeUtils.toRawClass(definingClassType);

    Type expectedType = Integer.TYPE;
    Type actualType = TypeInfoFactory.getActualType(fieldType, definingClassType, rawDefiningClass);
    assertEquals(expectedType, actualType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualTypeWithNonParameterizedType() {
    Class<Foo> fooClass = Foo.class;
    Field field = fooClass.getField("field");
    Type fieldType = field.getGenericType();
    Type definingClassType = fooClass.getGenericType();
    Type actualType = TypeInfoFactory.getActualType(definingClassType, null, null);
    fail("Expected UnsupportedOperationException but got: " + actualType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetActualTypeWithInvalidParentType() {
    Class<Foo> fooClass = Foo.class;
    Field field = fooClass.getField("field");
    Type fieldType = field.getGenericType();
    Type definingClassType = new Object() {};
    Type actualType = TypeInfoFactory.getActualType(fieldType, definingClassType, null);
    fail("Expected IllegalArgumentException but got: " + actualType);
  }

  @Test
  public void testExtractRealTypes() {
    Type[] types = new Type[] { Integer.TYPE, String.class };
    Type parentType = new ArrayList<>() {}.getClass().getGenericType();
    Class<?> rawParentClass = TypeUtils.toRawClass(parentType);
    Type[] expectedTypes = new Type[] { Integer.TYPE, String.class };
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(types, parentType, rawParentClass);
    assertArrayEquals(expectedTypes, actualTypes);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExtractRealTypesWithInvalidParentType() {
    Type[] types = new Type[] { Integer.TYPE, String.class };
    Type parentType = new Object() {};
    Class<?> rawParentClass = TypeUtils.toRawClass(parentType);
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(types, parentType, rawParentClass);
    fail("Expected IllegalArgumentException but got: " + actualTypes);
  }

  @Test
  public void testGetIndex() {
    TypeVariable<?>[] types = Foo.class.getTypeParameters();
    TypeVariable<?> typeToFind = TypeVariable.where(new TypeVariable<Integer>() {}, types);
    int expectedIndex = 0;
    int actualIndex = TypeInfoFactory.getIndex(types, typeToFind);
    assertEquals(expectedIndex, actualIndex);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetIndexWithMissingTypeVariable() {
    TypeVariable<?>[] types = Foo.class.getTypeParameters();
    TypeVariable<?> typeToFind = new TypeVariable<String>() {};
    int actualIndex = TypeInfoFactory.getIndex(types, typeToFind);
    fail("Expected IllegalStateException but got: " + actualIndex);
  }

  private static class Foo<A, B> {
    @SuppressWarnings("unused")
    private A field;
  }
}