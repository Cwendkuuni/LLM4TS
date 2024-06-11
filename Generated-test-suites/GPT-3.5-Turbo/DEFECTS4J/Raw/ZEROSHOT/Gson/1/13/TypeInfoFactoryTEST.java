import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {
  
  @Test
  public void testGetTypeInfoForArray() {
    TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(new int[5]);
    assertNotNull(typeInfoArray);
    assertEquals(int[].class, typeInfoArray.getType());
  }
  
  @Test
  public void testGetTypeInfoForField() throws NoSuchFieldException {
    Field field = Person.class.getDeclaredField("name");
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, Person.class);
    assertNotNull(typeInfo);
    assertEquals(String.class, typeInfo.getType());
  }
  
  @Test
  public void testGetActualType_class() {
    Type actualType = TypeInfoFactory.getActualType(String.class, Person.class, Person.class);
    assertEquals(String.class, actualType);
  }
  
  @Test
  public void testGetActualType_ParameterizedType() {
    ParameterizedType parameterizedType = new ParameterizedType() {
      public Type[] getActualTypeArguments() {
        return new Type[] { String.class };
      }
      public Type getOwnerType() { return null; }
      public Type getRawType() { return List.class; }
    };
    Type actualType = TypeInfoFactory.getActualType(parameterizedType, Person.class, Person.class);
    assertEquals(parameterizedType, actualType);
  }
  
  @Test
  public void testGetActualType_GenericArrayType() {
    GenericArrayType genericArrayType = new GenericArrayType() {
      public Type getGenericComponentType() { return String.class; }
    };
    Type actualType = TypeInfoFactory.getActualType(genericArrayType, Person.class, Person.class);
    assertEquals(genericArrayType, actualType);
  }
  
  @Test
  public void testGetActualType_TypeVariable() {
    TypeVariable typeVariable = Person.class.getTypeParameters()[0];
    Type actualType = TypeInfoFactory.getActualType(typeVariable, Person.class, Person.class);
    assertNotNull(actualType);
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualType_TypeVariable_NotParameterizedType() {
    TypeVariable typeVariable = Person.class.getTypeParameters()[0];
    TypeInfoFactory.getActualType(typeVariable, String.class, String.class);
  }
  
  @Test
  public void testGetActualType_WildcardType() {
    WildcardType wildcardType = new WildcardType() {
      public Type[] getUpperBounds() { return new Type[] { String.class }; }
      public Type[] getLowerBounds() { return new Type[0]; }
    };
    Type actualType = TypeInfoFactory.getActualType(wildcardType, Person.class, Person.class);
    assertEquals(String.class, actualType);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetActualType_InvalidType() {
    Type invalidType = new Type() {};
    TypeInfoFactory.getActualType(invalidType, Person.class, Person.class);
  }
  
  @Test
  public void testExtractRealTypes() {
    Type[] actualTypeArguments = new Type[] { String.class, Integer.class, List.class };
    Type[] retTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, Person.class, Person.class);
    assertEquals(3, retTypes.length);
    assertEquals(String.class, retTypes[0]);
    assertEquals(Integer.class, retTypes[1]);
    assertEquals(List.class, retTypes[2]);
  }
  
  @Test
  public void testGetIndex_found() {
    TypeVariable[] types = Person.class.getTypeParameters();
    int index = TypeInfoFactory.getIndex(types, types[0]);
    assertEquals(0, index);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testGetIndex_notFound() {
    TypeVariable[] types = Person.class.getTypeParameters();
    TypeInfoFactory.getIndex(types, new TypeVariable() {});
  }
  
  private class Person<T> {
    private T name;
  }
}