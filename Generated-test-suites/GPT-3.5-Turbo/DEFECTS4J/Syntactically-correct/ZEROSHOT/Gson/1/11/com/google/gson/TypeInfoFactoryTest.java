import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TypeInfoFactoryTest {
  
  @Test
  public void testGetTypeInfoForArray() {
    // Test case 1
    TypeInfoArray typeInfoArray1 = TypeInfoFactory.getTypeInfoForArray(String[].class);
    assertEquals(String[].class, typeInfoArray1.getType());
    
    // Test case 2
    TypeInfoArray typeInfoArray2 = TypeInfoFactory.getTypeInfoForArray(Integer[].class);
    assertEquals(Integer[].class, typeInfoArray2.getType());
  }
  
  @Test
  public void testGetTypeInfoForField() throws NoSuchFieldException {
    // Test case 1
    Field field1 = TypeInfoFactory.class.getDeclaredField("typeInfoArray");
    TypeInfo typeInfo1 = TypeInfoFactory.getTypeInfoForField(field1, TypeInfoFactory.class);
    assertEquals(TypeInfoArray.class, typeInfo1.getType());
    
    // Test case 2
    Field field2 = TypeInfoFactory.class.getDeclaredField("typeInfo");
    TypeInfo typeInfo2 = TypeInfoFactory.getTypeInfoForField(field2, TypeInfoFactory.class);
    assertEquals(TypeInfo.class, typeInfo2.getType());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeInfoForFieldInvalidType() throws NoSuchFieldException {
    Field field = TypeInfoFactory.class.getDeclaredField("invalidType");
    TypeInfoFactory.getTypeInfoForField(field, TypeInfoFactory.class);
  }
  
  @Test
  public void testGetActualType() {
    // Test case 1
    Type actualType1 = TypeInfoFactory.getActualType(String.class, TypeInfoFactory.class, TypeInfo.class);
    assertEquals(String.class, actualType1);
    
    // Test case 2
    Type actualType2 = TypeInfoFactory.getActualType(Integer.class, TypeInfoFactory.class, TypeInfo.class);
    assertEquals(Integer.class, actualType2);
  }
  
  @Test
  public void testExtractRealTypes() {
    // Test case 1
    Type[] actualTypeArguments1 = new Type[] {String.class, Integer.class};
    Type[] realTypes1 = TypeInfoFactory.extractRealTypes(actualTypeArguments1, TypeInfoFactory.class, TypeInfo.class);
    assertEquals(String.class, realTypes1[0]);
    assertEquals(Integer.class, realTypes1[1]);
    
    // Test case 2
    Type[] actualTypeArguments2 = new Type[] {Double.class, Boolean.class};
    Type[] realTypes2 = TypeInfoFactory.extractRealTypes(actualTypeArguments2, TypeInfoFactory.class, TypeInfo.class);
    assertEquals(Double.class, realTypes2[0]);
    assertEquals(Boolean.class, realTypes2[1]);
  }
  
  @Test
  public void testGetIndex() {
    TypeVariable<?>[] types = TypeInfoFactory.class.getTypeParameters();
    TypeVariable<?> type = types[0];
    int index = TypeInfoFactory.getIndex(types, type);
    assertEquals(0, index);
  }
}