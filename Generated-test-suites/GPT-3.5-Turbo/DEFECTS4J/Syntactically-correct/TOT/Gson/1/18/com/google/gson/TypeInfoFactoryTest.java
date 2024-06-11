import static org.junit.Assert.*;
import org.junit.Test;

public class TypeInfoFactoryTest {
  
  @Test
  public void testGetTypeInfoForArray_typicalCase() {
    // Create an array type
    Type arrayType = new TypeToken<List<String>[]>(){}.getType();
    
    // Call getTypeInfoForArray method
    TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(arrayType);
    
    // Check if the actual type matches the expected type
    assertEquals(arrayType, typeInfoArray.getActualType());
  }
  
  @Test
  public void testGetTypeInfoForArray_edgeCase() {
    // Create an array type with a generic component type
    Type arrayType = new TypeToken<List<?>[]>(){}.getType();
    
    // Call getTypeInfoForArray method
    TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(arrayType);
    
    // Check if the actual type matches the expected type
    assertEquals(arrayType, typeInfoArray.getActualType());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeInfoForArray_errorCase() {
    // Create a non-array type
    Type nonArrayType = new TypeToken<List<String>>(){}.getType();
    
    // Call getTypeInfoForArray method, expecting an IllegalArgumentException
    TypeInfoFactory.getTypeInfoForArray(nonArrayType);
  }
  
  @Test
  public void testGetTypeInfoForField_typicalCase() throws NoSuchFieldException {
    // Create a field object
    Field field = MyClass.class.getDeclaredField("myField");
    
    // Create a parameterized type
    Type parameterizedType = new TypeToken<List<String>>(){}.getType();
    
    // Call getTypeInfoForField method
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, parameterizedType);
    
    // Check if the actual type matches the expected type
    assertEquals(parameterizedType, typeInfo.getActualType());
  }
  
  @Test
  public void testGetTypeInfoForField_edgeCase() throws NoSuchFieldException {
    // Create a field object
    Field field = MyClass.class.getDeclaredField("myField");
    
    // Create a type variable
    TypeVariable<?> typeVariable = MyClass.class.getDeclaredMethod("myMethod", List.class).getTypeParameters()[0];
    
    // Create a parameterized type with the type variable
    Type parameterizedType = new TypeToken<MyClass<List<String>>>(){}.getType();
    
    // Call getTypeInfoForField method
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, parameterizedType);
    
    // Check if the actual type matches the expected type
    assertEquals(String.class, typeInfo.getActualType());
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testGetTypeInfoForField_errorCase() throws NoSuchFieldException {
    // Create a field object
    Field field = MyClass.class.getDeclaredField("myField");
    
    // Create a non-parameterized type
    Type nonParameterizedType = MyClass.class;
    
    // Call getTypeInfoForField method, expecting an UnsupportedOperationException
    TypeInfoFactory.getTypeInfoForField(field, nonParameterizedType);
  }
  
  private class MyClass<T> {
    private List<T> myField;
    
    public <E> void myMethod(List<E> param) {}
  }
}