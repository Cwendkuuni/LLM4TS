import com.google.gson.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

  @Test
  public void testGetTypeInfoForArray() {
    TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(String[].class);
    assertNotNull(typeInfoArray);
    assertEquals(String[].class, typeInfoArray.getType());
  }

  @Test
  public void testGetTypeInfoForField() {
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(
        TypeInfoFactory.class.getDeclaredFields()[0], TypeInfoFactory.class);
    assertNotNull(typeInfo);
    assertEquals(ParameterizedTypeImpl.class, typeInfo.getType().getClass());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTypeInfoForInvalidArray() {
    TypeInfoFactory.getTypeInfoForArray(Integer.class);
  }

  @Test
  public void testGetActualTypeForClass() {
    Type actualType = TypeInfoFactory.getActualType(
        TypeInfoFactory.class, TypeInfoFactory.class, TypeInfoFactory.class);
    assertEquals(TypeInfoFactory.class, actualType);
  }

  @Test
  public void testGetActualTypeForTypeVariable() {
    Type actualType = TypeInfoFactory.getActualType(
        TypeInfoFactory.class.getTypeParameters()[0], TypeInfoFactory.class, TypeInfoFactory.class);
    assertEquals(TypeInfoFactory.class, actualType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetTypeInfoForTypeVariableWithoutParameterizedType() {
    TypeInfoFactory.getTypeInfoForField(
        TypeInfoFactory.class.getDeclaredFields()[0], TypeInfoFactory.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetActualTypeForInvalidType() {
    TypeInfoFactory.getActualType(Void.class, Object.class, Object.class);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetIndexForInvalidTypeVariable() {
    TypeInfoFactory.getIndex(new TypeVariable<?>[]{}, TypeInfoFactory.class.getTypeParameters()[0]);
  }

}