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
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type intTypeArray = new int[0].getClass().getComponentType();
    TypeInfoArray intTypeInfoArray = TypeInfoFactory.getTypeInfoForArray(intTypeArray);
    assertEquals(TypeInfo.ARRAY, intTypeInfoArray.getType());
  }

  @Test
  public void testTypeInfoForField_ClassType() {
    try {
      Class<Integer> intClass = Integer.TYPE.getClass();
      Field field = intClass.getDeclaredField("value");
      Type typeDefiningF = intClass;
      TypeInfo intTypeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
      assertEquals(TypeInfo.PRIMITIVE, intTypeInfo.getType());
      assertEquals(int.class, intTypeInfo.getTypeValue());
    } catch (NoSuchFieldException e) {
      fail("Field value should exist in Integer class.");
    }
  }

  @Test
  public void testTypeInfoForField_ParameterizedType() {
    class ListWithInteger<T> {
      private List<T> list;

      public List<T> getList() {
        return list;
      }

      public void setList(List<T> list) {
        this.list = list;
      }
    }

    try {
      Field listField = ListWithInteger.class.getDeclaredField("list");
      Type typeDefiningF = ListWithInteger.class;
      Type listType = new TypeToken<List<Integer>>() {}.getType();
      Type listTypeParameterized = new ParameterizedType() {
        @Override
        public Type getOwnerType() {
          return ListWithInteger.class;
        }

        @Override
        public Type[] getActualTypeArguments() {
          return new Type[] {Integer.TYPE};
        }

        @Override
        public Type getRawType() {
          return List.class;
        }
      };
      TypeInfo listTypeInfo = TypeInfoFactory.getTypeInfoForField(listField, listTypeParameterized);
      assertEquals(TypeInfo.COLLECTION, listTypeInfo.getType());
      assertEquals(List.class, listTypeInfo.getTypeValue());
    } catch (NoSuchFieldException e) {
      fail("Field list should exist in ListWithInteger class.");
    }
  }

  @Test
  public void testTypeInfoForField_GenericArrayType() {
    try {
      Class<Integer> intClass = Integer.TYPE.getClass();
      Class<Integer[]> intArrayClass = intClass.getComponentType().getComponentType();
      Field field = intArrayClass.getDeclaredField("class");
      Type typeDefiningF = intArrayClass;
      Type infoType = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF).getType();
      assertEquals(TypeInfo.ARRAY, infoType);
    } catch (NoSuchFieldException e) {
      fail("Field class should exist in Integer[] class.");
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForField_TypeVariable_MissingParentType() {
    class Foo<A> {
      A a;
    }

    Field aField = Foo.class.getDeclaredField("a");
    Type typeDefiningF = Foo.class;
    TypeVariable<A> expectedTypeVariable = TypeVariable.where(Foo.class, "A");
    TypeInfo info = TypeInfoFactory.getTypeInfoForField(aField, typeDefiningF);
    fail("Expected UnsupportedOperationException, but got " + info);
  }

  @Test
  public void testTypeInfoForField_TypeVariable() {
    class Foo<A> {
      A a;
    }

    try {
      Field aField = Foo.class.getDeclaredField("a");
      Type typeDefiningF = Foo.class;
      TypeVariable<A> expectedTypeVariable = TypeVariable.where(Foo.class, "A");
      Type infoType = TypeInfoFactory.getTypeInfoForField(aField, typeDefiningF).getType();
      assertEquals(TypeInfo.TYPE_VARIABLE, infoType);
      assertEquals(expectedTypeVariable, infoType.getTypeValue());
    } catch (NoSuchFieldException e) {
      fail("Field a should exist in Foo class.");
    }
  }

  @Test
  public void testTypeInfoForField_WildcardType() {
    class Foo<A> {
      List<A> list;
    }

    try {
      Field listField = Foo.class.getDeclaredField("list");
      Type typeDefiningF = Foo.class;
      Type listType = new TypeToken<List<?>>() {}.getType();
      Type listTypeUpperBound = new WildcardType[] {new WildcardType(null, null)}[0];
      Type infoType = TypeInfoFactory.getTypeInfoForField(listField, listTypeUpperBound).getType();
      assertEquals(TypeInfo.COLLECTION, infoType);
    } catch (NoSuchFieldException e) {
      fail("Field list should exist in Foo class.");
    }
  }
}