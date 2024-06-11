import com.google.gson.*;
import org.junit.Test;
import org.junit.Assert;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.List;

public class TypeInfoFactoryTest {
  
  @Test
  public void testGetTypeInfoForArray() {
    // typical use case
    Type type1 = new int[5].getClass();
    TypeInfoArray typeInfoArray1 = TypeInfoFactory.getTypeInfoForArray(type1);
    Assert.assertEquals(type1, typeInfoArray1.getType());
    
    // edge case
    Type type2 = new Object[0].getClass();
    TypeInfoArray typeInfoArray2 = TypeInfoFactory.getTypeInfoForArray(type2);
    Assert.assertEquals(type2, typeInfoArray2.getType());
    
    // error scenario
    Type type3 = new Object().getClass();
    try {
      TypeInfoArray typeInfoArray3 = TypeInfoFactory.getTypeInfoForArray(type3);
      Assert.fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }
  
  @Test
  public void testGetTypeInfoForField() throws NoSuchFieldException {
    Field f1 = SomeClass.class.getDeclaredField("field1");
    TypeInfo typeInfo1 = TypeInfoFactory.getTypeInfoForField(f1, SomeClass.class);
    Type expected1 = SomeClass.class.getField("field1").getGenericType();
    Assert.assertEquals(expected1, typeInfo1.getType());
    
    Field f2 = SomeClass.class.getDeclaredField("field2");
    TypeInfo typeInfo2 = TypeInfoFactory.getTypeInfoForField(f2, SomeClass.class);
    Type expected2 = SomeClass.class.getField("field2").getGenericType();
    Assert.assertEquals(expected2, typeInfo2.getType());
    
    Field f3 = SomeClass.class.getDeclaredField("field3");
    TypeInfo typeInfo3 = TypeInfoFactory.getTypeInfoForField(f3, SomeClass.class);
    Type expected3 = SomeClass.class.getField("field3").getGenericType();
    Assert.assertEquals(expected3, typeInfo3.getType());
  }
  
  @Test
  public void testGetActualType() {
    // typical use case - Class type
    Type t1 = Integer.class;
    Type parentType1 = SomeClass.class;
    Class<?> rawParentClass1 = SomeClass.class;
    Type actualType1 = TypeInfoFactory.getActualType(t1, parentType1, rawParentClass1);
    Assert.assertEquals(t1, actualType1);
    
    // typical use case - ParameterizedType
    Type t2 = new TypeToken<List<String>>() {}.getType();
    Type parentType2 = SomeClass.class;
    Class<?> rawParentClass2 = SomeClass.class;
    Type actualType2 = TypeInfoFactory.getActualType(t2, parentType2, rawParentClass2);
    Assert.assertEquals(((ParameterizedType)t2).getRawType(), actualType2);
    
    // typical use case - GenericArrayType
    Type t3 = new TypeToken<List<String>[]>() {}.getType();
    Type parentType3 = SomeClass.class;
    Class<?> rawParentClass3 = SomeClass.class;
    Type actualType3 = TypeInfoFactory.getActualType(t3, parentType3, rawParentClass3);
    Assert.assertEquals(((GenericArrayType)t3).getGenericComponentType(), actualType3);

    // typical use case - TypeVariable
    Type t4 = new TypeToken<List<String>>() {}.getType();
    Type parentType4 = new TypeToken<List<? super String>>() {}.getType();
    Class<?> rawParentClass4 = SomeClass.class;
    Type actualType4 = TypeInfoFactory.getActualType(t4, parentType4, rawParentClass4);
    Assert.assertEquals(((ParameterizedType)t4).getActualTypeArguments()[0], actualType4);
    
    // typical use case - WildcardType
    Type t5 = new TypeToken<List<? extends Number>>() {}.getType();
    Type parentType5 = SomeClass.class;
    Class<?> rawParentClass5 = SomeClass.class;
    Type actualType5 = TypeInfoFactory.getActualType(t5, parentType5, rawParentClass5);
    Assert.assertEquals(((WildcardType)t5).getUpperBounds()[0], actualType5);
    
    // error scenario
    Type t6 = new SomeClass().getClass();
    Type parentType6 = SomeClass.class;
    Class<?> rawParentClass6 = SomeClass.class;
    try {
      TypeInfoFactory.getActualType(t6, parentType6, rawParentClass6);
      Assert.fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }
  
  @Test
  public void testExtractRealTypes() {
    // typical use case
    Type[] actualTypeArgs1 = new Type[] {Integer.class, String.class};
    Type parentType1 = new TypeToken<List<String>>() {}.getType();
    Class<?> rawParentClass1 = TypeUtils.toRawClass(parentType1);
    Type[] expectedTypes1 = new Type[] {actualTypeArgs1[0], actualTypeArgs1[1]};
    Type[] retTypes1 = TypeInfoFactory.extractRealTypes(actualTypeArgs1, parentType1, rawParentClass1);
    Assert.assertArrayEquals(expectedTypes1, retTypes1);
    
    // error scenario
    Type[] actualTypeArgs2 = new Type[] {new SomeClass().getClass()};
    Type parentType2 = SomeClass.class;
    Class<?> rawParentClass2 = SomeClass.class;
    try {
      TypeInfoFactory.extractRealTypes(actualTypeArgs2, parentType2, rawParentClass2);
      Assert.fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }
  
  @Test
  public void testGetIndex() {
    // typical use case
    TypeVariable<?>[] types1 = SomeClass.class.getTypeParameters();
    TypeVariable<?> type1 = types1[0];
    int expectedIndex1 = 0;
    int retIndex1 = TypeInfoFactory.getIndex(types1, type1);
    Assert.assertEquals(expectedIndex1, retIndex1);
    
    // error scenario
    TypeVariable<?>[] types2 = new TypeVariable<?>[] {new TypeVariable<>(){}};
    TypeVariable<?> type2 = types1[0];
    try {
      TypeInfoFactory.getIndex(types2, type2);
      Assert.fail("Expected IllegalStateException to be thrown");
    } catch (IllegalStateException e) {
      // expected
    }
  }

  private static class SomeClass<T, U extends Number> {
    public T field1;
    public List<T> field2;
    public List<? extends U> field3;
  }
}