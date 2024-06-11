import static org.junit.Assert.*;

import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testGetTypeInfoForArray() {
    // Typical use case
    // Test that TypeInfoArray object is created for an array type
    Type type = new TypeToken<Integer[]>() {}.getType();
    assertNotNull(TypeInfoFactory.getTypeInfoForArray(type));
    
    // Edge case
    // Test that exception is thrown if type is not an array
    Type nonArrayType = Integer.class;
    assertThrows(IllegalArgumentException.class, () -> {
      TypeInfoFactory.getTypeInfoForArray(nonArrayType);
    });
  }

  @Test
  public void testGetTypeInfoForField() {
    // Typical use case
    // Test that TypeInfo object is created for a field type
    Field f = SomeClass.class.getDeclaredField("someField");
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(f, SomeClass.class);
    assertNotNull(typeInfo);
    
    // Edge case
    // Test that exception is thrown if field type is not found
    Field nonExistentField = SomeClass.class.getDeclaredField("nonExistentField");
    assertThrows(NoSuchFieldException.class, () -> {
      TypeInfoFactory.getTypeInfoForField(nonExistentField, SomeClass.class);
    });
  }

  @Test
  public void testGetActualType() {
    // Typical use case
    // Test that actual type is correctly evaluated for a given type
    Type type = SomeClass.class.getDeclaredField("someField").getGenericType();
    Type actualType = TypeInfoFactory.getActualType(type, SomeClass.class, SomeClass.class);
    assertNotNull(actualType);
    
    // Edge case
    // Test that exception is thrown if type cannot be evaluated
    Type invalidType = new TypeToken<InvalidClass>() {}.getType();
    assertThrows(IllegalArgumentException.class, () -> {
      TypeInfoFactory.getActualType(invalidType, SomeClass.class, SomeClass.class);
    });
  }
}