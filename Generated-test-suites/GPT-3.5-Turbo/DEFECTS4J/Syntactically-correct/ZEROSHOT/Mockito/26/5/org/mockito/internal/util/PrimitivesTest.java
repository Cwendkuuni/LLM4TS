import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {
  
  @Test
  public void testPrimitiveTypeOf_withPrimitiveClass_shouldReturnSameClass() {
    Class<Boolean> booleanClass = boolean.class;
    assertEquals(booleanClass, Primitives.primitiveTypeOf(booleanClass));
  }
  
  @Test
  public void testPrimitiveTypeOf_withWrapperClass_shouldReturnCorrespondingPrimitiveClass() {
    Class<Boolean> booleanClass = Boolean.class;
    assertEquals(boolean.class, Primitives.primitiveTypeOf(booleanClass));
  }
  
  @Test
  public void testIsPrimitiveWrapper_withPrimitiveClass_shouldReturnFalse() {
    Class<Boolean> booleanClass = boolean.class;
    assertFalse(Primitives.isPrimitiveWrapper(booleanClass));
  }
  
  @Test
  public void testIsPrimitiveWrapper_withWrapperClass_shouldReturnTrue() {
    Class<Boolean> booleanClass = Boolean.class;
    assertTrue(Primitives.isPrimitiveWrapper(booleanClass));
  }
  
  @Test
  public void testPrimitiveWrapperOf_withPrimitiveClass_shouldReturnWrapperInstance() {
    Class<Boolean> booleanClass = boolean.class;
    assertEquals(Boolean.FALSE, Primitives.primitiveWrapperOf(booleanClass));
  }
  
  @Test
  public void testPrimitiveWrapperOf_withWrapperClass_shouldReturnSameInstance() {
    Class<Boolean> booleanClass = Boolean.class;
    assertEquals(Boolean.FALSE, Primitives.primitiveWrapperOf(booleanClass));
  }
  
  @Test
  public void testPrimitiveValueOrNullFor_withPrimitiveType_shouldReturnPrimitiveValue() {
    Class<Boolean> booleanClass = boolean.class;
    assertEquals(false, Primitives.primitiveValueOrNullFor(booleanClass));
  }
  
  @Test
  public void testPrimitiveValueOrNullFor_withWrapperClass_shouldReturnNull() {
    Class<Boolean> booleanClass = Boolean.class;
    assertNull(Primitives.primitiveValueOrNullFor(booleanClass));
  }
  
  @Test
  public void testWrapperReturnValues_withPrimitiveClass_shouldReturnFalse() {
    Class<Boolean> booleanClass = boolean.class;
    assertFalse(Primitives.wrapperReturnValues.containsKey(booleanClass));
  }
  
  @Test
  public void testWrapperReturnValues_withWrapperClass_shouldReturnWrapperInstance() {
    Class<Boolean> booleanClass = Boolean.class;
    assertTrue(Primitives.wrapperReturnValues.containsKey(booleanClass));
    assertEquals(Boolean.FALSE, Primitives.wrapperReturnValues.get(booleanClass));
  }
  
  @Test
  public void testPrimitiveValues_withPrimitiveClass_shouldReturnPrimitiveValue() {
    Class<Boolean> booleanClass = boolean.class;
    assertTrue(Primitives.primitiveValues.containsKey(booleanClass));
    assertEquals(false, Primitives.primitiveValues.get(booleanClass));
  }
  
  @Test
  public void testPrimitiveValues_withWrapperClass_shouldReturnNull() {
    Class<Boolean> booleanClass = Boolean.class;
    assertFalse(Primitives.primitiveValues.containsKey(booleanClass));
  }
}